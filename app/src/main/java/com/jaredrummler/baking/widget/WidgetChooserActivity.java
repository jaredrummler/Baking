package com.jaredrummler.baking.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.ui.recipes.RecipesAdapter;
import com.jaredrummler.baking.ui.recipes.RecipesFragment;

import butterknife.ButterKnife;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;
import static com.jaredrummler.baking.widget.RecipeWidget.GSON;
import static com.jaredrummler.baking.widget.RecipeWidget.PREF_NAME;

/**
 * An activity to select which recipe to set for the {@link RecipeWidget widget}.
 */
public class WidgetChooserActivity extends AppCompatActivity implements RecipesAdapter.RecipeClickListener {

    private int appWidgetId = INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_chooser);

        ButterKnife.bind(this);

        appWidgetId = getIntent().getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
        if (appWidgetId == INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_recipes, new RecipePickerFragment())
                    .commit();
        }
    }

    @Override
    public void onClick(Recipe recipe) {
        // Save the recipe to SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(String.valueOf(appWidgetId), GSON.toJson(recipe));
        editor.apply();
        // Update the Widget
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        RecipeWidget.updateAppWidget(this, widgetManager, appWidgetId);
        // Set the result
        Intent resultValue = new Intent();
        resultValue.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public static class RecipePickerFragment extends RecipesFragment {

        @Override
        public void onClick(Recipe recipe) {
            if (getActivity() instanceof RecipesAdapter.RecipeClickListener) {
                ((RecipesAdapter.RecipeClickListener) getActivity()).onClick(recipe);
            }
        }

    }

}
