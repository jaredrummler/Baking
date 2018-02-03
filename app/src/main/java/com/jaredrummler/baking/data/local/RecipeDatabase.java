package com.jaredrummler.baking.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.jaredrummler.baking.App;
import com.jaredrummler.baking.data.model.Ingredient;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.data.model.Step;

@Database(
        entities = {Recipe.class, Ingredient.class, Step.class},
        version = RecipeDatabase.DATABASE_VERSION,
        exportSchema = false
)
public abstract class RecipeDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "recipes.db";
    public static final int DATABASE_VERSION = 1;

    private static RecipeDatabase instance;

    public static RecipeDatabase getInstance() {
        if (instance == null) {
            synchronized (RecipeDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            App.getApp(), RecipeDatabase.class, DATABASE_NAME
                    ).build();
                }
            }
        }
        return instance;
    }

    public abstract RecipeDao recipeDao();

    public abstract IngredientDao ingredientDao();

    public abstract StepDao stepDao();

}
