package com.jaredrummler.baking.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.model.Ingredient;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.ui.details.DetailsActivity;
import com.jaredrummler.baking.utils.RecipeUtils;

/**
 * The Adapter for the Widget's ListView items.
 */
final class WidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private final Recipe recipe;

    WidgetViewFactory(Context context, Recipe recipe) {
        this.context = context;
        this.recipe = recipe;
    }

    @Override
    public void onCreate() {
        // no-op
    }

    @Override
    public void onDataSetChanged() {
        // no-op
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        return recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_item);
        Ingredient ingredient = recipe.getIngredients().get(position);
        row.setTextViewText(android.R.id.text1, RecipeUtils.formatIngredient(ingredient));
        int bgColor = position % 2 == 0 ? 0xFFFFFFFF : 0xFFEFF0F1;
        row.setInt(android.R.id.text1, "setBackgroundColor", bgColor);
        Intent i = new Intent();
        i.putExtra(DetailsActivity.EXTRA_RECIPE_ID, recipe.getId());
        row.setOnClickFillInIntent(android.R.id.text1, i);
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
