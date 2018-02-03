package com.jaredrummler.baking.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "ingredients",
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = "recipeId")}
)
public class Ingredient implements Parcelable {

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ForeignKey(
            entity = Recipe.class,
            parentColumns = {"id"},
            childColumns = {"recipeId"},
            onDelete = ForeignKey.CASCADE)
    @ColumnInfo(name = "recipeId")
    private int recipeId;
    @ColumnInfo(name = "quantity")
    @SerializedName("quantity")
    private Double quantity;
    @ColumnInfo(name = "measure")
    @SerializedName("measure")
    private String measure;
    @ColumnInfo(name = "ingredient")
    @SerializedName("ingredient")
    private String ingredient;

    public Ingredient() {
    }

    protected Ingredient(Parcel in) {
        this.id = in.readInt();
        this.recipeId = in.readInt();
        this.quantity = (Double) in.readValue(Double.class.getClassLoader());
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.recipeId);
        dest.writeValue(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }
}
