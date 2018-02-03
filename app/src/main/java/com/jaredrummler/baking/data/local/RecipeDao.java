package com.jaredrummler.baking.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.data.model.RecipeModel;

import java.util.List;

@Dao
public interface RecipeDao {

    @Transaction
    @Query("SELECT * FROM recipes")
    LiveData<List<RecipeModel>> getRecipes();

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    LiveData<RecipeModel> getRecipe(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Recipe> recipes);

}
