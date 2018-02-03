package com.jaredrummler.baking;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.baking.data.local.IngredientDao;
import com.jaredrummler.baking.data.local.RecipeDao;
import com.jaredrummler.baking.data.local.RecipeDatabase;
import com.jaredrummler.baking.data.local.StepDao;
import com.jaredrummler.baking.data.model.Ingredient;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.data.model.RecipeModel;
import com.jaredrummler.baking.data.model.Step;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RecipeDatabaseTest {

    private static final int EXPECTED_NUM_RECIPES = 4;
    private static final String EXPECTED_RECIPE_NAME = "Nutella Pie";
    private static final int EXPECTED_SERVINGS = 8;
    private static final double EXPECTED_QUANTITY = 400;

    private static String readAsset(Context context, String filename) throws IOException {
        InputStream stream = context.getAssets().open("test_response.json");
        int size = stream.available();
        byte[] buffer = new byte[size];
        stream.read(buffer);
        stream.close();
        return new String(buffer, "UTF-8");
    }

    // https://stackoverflow.com/a/44271247/1048340
    private static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        //noinspection unchecked
        return (T) data[0];
    }

    private RecipeDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, RecipeDatabase.class).build();
    }

    @Test
    public void inserted_dummy_data_correctly() throws Exception {
        RecipeDao recipeDao = mDb.recipeDao();
        IngredientDao ingredientDao = mDb.ingredientDao();
        StepDao stepDao = mDb.stepDao();

        // Read dummy data
        String json = readAsset(InstrumentationRegistry.getContext(), "test_response.json");
        Gson gson = new Gson();
        List<Recipe> recipes = gson.fromJson(json, new TypeToken<List<Recipe>>() {
        }.getType());

        // Insert recipes into the database
        mDb.beginTransaction();
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
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }

        // Query database for recipes
        List<RecipeModel> result = getValue(recipeDao.getRecipes());
        RecipeModel nutellaPie = result.get(0);
        RecipeModel yellowCake = result.get(2);
        RecipeModel cheeseCake = result.get(3);

        // Confirm database has correct values
        assertEquals(EXPECTED_NUM_RECIPES, result.size());
        assertEquals(EXPECTED_RECIPE_NAME, nutellaPie.recipe.getName());
        assertEquals(EXPECTED_SERVINGS, cheeseCake.recipe.getServings().intValue());
        assertEquals(EXPECTED_QUANTITY, yellowCake.ingredients.get(0).getQuantity(), 0);
    }

    @After
    public void closeDb() {
        mDb.close();
    }


}
