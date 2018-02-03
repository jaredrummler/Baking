package com.jaredrummler.baking.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.jaredrummler.baking.data.local.IngredientDao;
import com.jaredrummler.baking.data.local.RecipeDao;
import com.jaredrummler.baking.data.local.RecipeDatabase;
import com.jaredrummler.baking.data.local.StepDao;
import com.jaredrummler.baking.data.model.Ingredient;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.data.model.RecipeModel;
import com.jaredrummler.baking.data.model.Step;
import com.jaredrummler.baking.data.remote.BakingApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class RecipeRepository {

    public LiveData<Resource<List<Recipe>>> loadRecipes() {
        return new NetworkBoundResource<List<Recipe>, List<Recipe>>() {

            @Override
            protected void saveCallResult(@NonNull List<Recipe> recipes) {
                RecipeDatabase database = RecipeDatabase.getInstance();
                RecipeDao recipeDao = database.recipeDao();
                IngredientDao ingredientDao = database.ingredientDao();
                StepDao stepDao = database.stepDao();
                database.beginTransaction();
                try {
                    recipeDao.insert(recipes);
                    for (Recipe recipe : recipes) {
                        for (Ingredient ingredient : recipe.getIngredients()) {
                            ingredient.setRecipeId(recipe.getId());
                        }
                        for (Step step : recipe.getSteps()) {
                            step.setRecipeId(recipe.getId());
                        }
                        ingredientDao.insert(recipe.getIngredients());
                        stepDao.insert(recipe.getSteps());
                    }
                    database.setTransactionSuccessful();
                } finally {
                    database.endTransaction();
                }
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                RecipeDatabase db = RecipeDatabase.getInstance();
                return Transformations.map(db.recipeDao().getRecipes(), input -> {
                    List<Recipe> recipes = new ArrayList<>();
                    for (RecipeModel model : input) {
                        recipes.add(new Recipe(model));
                    }
                    return recipes;
                });
            }

            @NonNull
            @Override
            protected Call<List<Recipe>> createCall() {
                return BakingApiClient.getInstance().getRecipes();
            }
        }.getAsLiveData();
    }

}
