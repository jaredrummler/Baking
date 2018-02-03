package com.jaredrummler.baking.ui.recipes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.jaredrummler.baking.data.RecipeRepository;
import com.jaredrummler.baking.data.Resource;
import com.jaredrummler.baking.data.model.Recipe;

import java.util.List;

public class RecipesViewModel extends ViewModel {

    private LiveData<Resource<List<Recipe>>> recipes;

    public RecipesViewModel() {
        recipes = new RecipeRepository().loadRecipes();
    }

    public LiveData<Resource<List<Recipe>>> getRecipes() {
        return recipes;
    }

}
