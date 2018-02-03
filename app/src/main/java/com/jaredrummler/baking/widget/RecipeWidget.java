package com.jaredrummler.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.ui.details.DetailsActivity;

import static android.content.ContentResolver.SCHEME_CONTENT;
import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    static final String PREF_NAME = "widget_prefs";
    static final String EXTRA_RECIPE = "extras.RECIPE";
    static final String EXTRA_BUNDLE = "extras.BUNDLE";
    static final Gson GSON = new Gson();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Get the Recipe from SharedPreferences via the appWidgetId
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String json = preferences.getString(Integer.toString(appWidgetId), null);
        if (json == null) {
            return;
        }
        Recipe recipe = GSON.fromJson(json, Recipe.class);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        // Set the title
        views.setTextViewText(R.id.recipe_name, recipe.getName());
        Intent service = new Intent(context, WidgetService.class);
        // https://stackoverflow.com/a/11387266/1048340
        service.setData(Uri.fromParts(SCHEME_CONTENT, String.valueOf(appWidgetId), null));
        // Add the recipe as an extra to a bundle.
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RECIPE, recipe);
        service.putExtra(EXTRA_BUNDLE, bundle);
        // Set the remote adapter for the ListView
        views.setRemoteAdapter(R.id.ingredients, service);
        // Set the click intent for the ListView items
        PendingIntent template = getListItemClickIntent(context, recipe, appWidgetId);
        views.setPendingIntentTemplate(R.id.ingredients, template);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static PendingIntent getListItemClickIntent(Context context, Recipe recipe, int appWidgetId) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.setData(Uri.fromParts(SCHEME_CONTENT, String.valueOf(appWidgetId), null));
        intent.putExtra(DetailsActivity.EXTRA_RECIPE_ID, recipe.getId());
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}

