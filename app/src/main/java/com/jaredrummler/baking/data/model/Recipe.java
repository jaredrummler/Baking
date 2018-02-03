package com.jaredrummler.baking.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private Integer id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @Ignore
    @SerializedName("ingredients")
    private List<Ingredient> ingredients;

    @Ignore
    @SerializedName("steps")
    private List<Step> steps;

    @ColumnInfo(name = "servings")
    @SerializedName("servings")
    private Integer servings;

    @ColumnInfo(name = "image")
    @SerializedName("image")
    private String image;

    public Recipe() {

    }

    public Recipe(RecipeModel model) {
        ingredients = model.ingredients;
        steps = model.steps;
        id = model.recipe.id;
        image = model.recipe.image;
        name = model.recipe.name;
        servings = model.recipe.servings;
    }

    protected Recipe(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        this.steps = in.createTypedArrayList(Step.CREATOR);
        this.servings = (Integer) in.readValue(Integer.class.getClassLoader());
        this.image = in.readString();
    }

    private static Recipe fromModel(RecipeModel model) {
        Recipe recipe = new Recipe();
        recipe.setIngredients(model.ingredients);
        recipe.setSteps(model.steps);
        recipe.setId(model.recipe.getId());
        recipe.setImage(model.recipe.getImage());
        recipe.setName(model.recipe.getName());
        recipe.setServings(model.recipe.getServings());
        return recipe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.steps);
        dest.writeValue(this.servings);
        dest.writeString(this.image);
    }
}
