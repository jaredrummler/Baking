package com.jaredrummler.baking.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;

import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.model.Ingredient;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.data.model.Step;

public class ShareUtils {

    private ShareUtils() {
        throw new AssertionError("no instances");
    }

    /**
     * Share a recipe
     *
     * @param context The activity context
     * @param recipe  A recipe to share
     */
    public static void share(Context context, Recipe recipe) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/html");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, recipe.getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, getShareHtml(recipe));
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share)));
    }

    private static Spanned getShareHtml(@NonNull Recipe recipe) {
        StringBuilder html = new StringBuilder();
        html.append("<h2>").append(recipe.getName()).append("</h2>");
        html.append("<p><b>Ingredients</b></p>");
        for (Ingredient ingredient : recipe.getIngredients()) {
            html.append(RecipeUtils.formatIngredient(ingredient)).append("<br/>");
        }
        html.append("<p><b>Steps</b></p>");
        for (Step step : recipe.getSteps()) {
            if (RecipeUtils.isIntoStep(step)) continue;
            html.append("<p><b>").append(step.getShortDescription()).append("</b></p>");
            html.append(step.getDescription()).append("<br/>");
        }
        return Html.fromHtml(html.toString());
    }

}
