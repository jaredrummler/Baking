package com.jaredrummler.baking.data.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class RecipeModel {

    @Embedded
    public Recipe recipe;

    @Relation(entity = Ingredient.class, parentColumn = "id", entityColumn = "recipeId")
    public List<Ingredient> ingredients;

    @Relation(entity = Step.class, parentColumn = "id", entityColumn = "recipeId")
    public List<Step> steps;

}
