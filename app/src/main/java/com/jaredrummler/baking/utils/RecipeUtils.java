package com.jaredrummler.baking.utils;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.jaredrummler.baking.App;
import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.model.Ingredient;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.data.model.Step;

import java.util.List;

public class RecipeUtils {

    public static String formatIngredient(Ingredient ingredient) {
        StringBuilder result = new StringBuilder();
        Resources resources = App.getApp().getResources();

        result.append(doubleToStringFraction(ingredient.getQuantity()));
        result.append(' ');

        String measure = ingredient.getMeasure();
        int quatity = (int) Math.floor(ingredient.getQuantity());
        if (quatity == 0) quatity = 1;

        switch (measure.toUpperCase()) {
            case "CUP":
                result.append(resources.getQuantityString(R.plurals.cups, quatity)).append(' ');
                break;
            case "G":
                result.append(resources.getQuantityString(R.plurals.grams, quatity)).append(' ');
                break;
            case "K": // WTF is K
                result.append(resources.getQuantityString(R.plurals.kilogram, quatity)).append(' ');
                break;
            case "OZ":
                result.append(resources.getQuantityString(R.plurals.onces, quatity)).append(' ');
                break;
            case "TBLSP":
                result.append(resources.getQuantityString(R.plurals.tablespoons, quatity)).append(' ');
                break;
            case "TSP":
                result.append(resources.getQuantityString(R.plurals.teaspoons, quatity)).append(' ');
                break;
            case "UNIT":
                // nothing
                break;
            default:
                result.append(measure).append(' ');
        }

        result.append(ingredient.getIngredient().toLowerCase());
        return result.toString();
    }

    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // https://stackoverflow.com/a/379265/1048340
    public static String doubleToStringFraction(Double d) {
        StringBuilder result = new StringBuilder();
        int floor = (int) Math.floor(d);
        if (floor > 0) {
            result.append(floor);
        }
        int whole = (int) ((d - Math.floor(d)) * 10000);
        int gcd = gcd(whole, 10000);
        int num = whole / gcd;
        int den = 10000 / gcd;
        if (num != 0) {
            if (floor > 0) {
                result.append(' ');
            }
            result.append(num + "/" + den);
        }
        return result.toString();
    }

    /**
     * Get the number of steps in a recipe. This will exclude the recipe introduction as a step.
     *
     * @param recipe The recipe
     * @return The number of steps in the recipe
     */
    public static int getNumSteps(@NonNull Recipe recipe) {
        List<Step> steps = recipe.getSteps();
        int size = steps.size();
        if (steps.size() == 0) return 0;
        return RecipeUtils.isIntoStep(steps.get(0)) ? size - 1 : size;
    }

    public static boolean isIntoStep(@NonNull Step step) {
        return "recipe introduction".equalsIgnoreCase(step.getShortDescription());
    }

}
