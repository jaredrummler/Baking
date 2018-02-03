package com.jaredrummler.baking.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.jaredrummler.baking.data.model.Step;

import java.util.List;

@Dao
public interface StepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Step> steps);

    @Query("SELECT * FROM steps WHERE recipeId = :recipeId")
    LiveData<List<Step>> getSteps(int recipeId);

}
