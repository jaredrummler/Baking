package com.jaredrummler.baking.ui.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.jaredrummler.baking.data.local.RecipeDatabase;
import com.jaredrummler.baking.data.model.Recipe;

public class DetailsViewModel extends ViewModel {

    private final LiveData<Recipe> recipe;

    public DetailsViewModel(int recipeId) {
        recipe = Transformations.map(RecipeDatabase.getInstance().recipeDao().getRecipe(recipeId), Recipe::new);
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final int id;

        public Factory(int id) {
            this.id = id;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new DetailsViewModel(id);
        }
    }

}
