package com.jaredrummler.baking.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.image.GlideApp;
import com.jaredrummler.baking.image.VideoThumbnail;
import com.jaredrummler.baking.ui.steps.StepsFragment;
import com.jaredrummler.baking.utils.RecipeUtils;
import com.jaredrummler.baking.utils.ShareUtils;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extras.RECIPE_ID";

    private DetailsViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        // Setup the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        if (id == -1) {
            throw new RuntimeException("Invalid recipe ID");
        }

        viewModel = ViewModelProviders.of(this, new DetailsViewModel.Factory(id)).get(DetailsViewModel.class);
        viewModel.getRecipe().observe(this, this::loadRecipeDetails);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_share:
                ShareUtils.share(this, viewModel.getRecipe().getValue());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadRecipeDetails(Recipe recipe) {
        getSupportActionBar().setTitle(recipe.getName());
        setImageHeader(recipe);
        setStepsAmount(recipe);
        setIngredientsAmount(recipe);
        setServingsAmount(recipe);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_directions, DetailsFragment.newInstance(recipe))
                .commit();

        if (findViewById(R.id.fragment_steps) != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_steps, StepsFragment.newInstance(recipe.getSteps().get(0)))
                    .commit();
        }
    }

    public void openStep(Recipe recipe, int position) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_steps, StepsFragment.newInstance(recipe.getSteps().get(position)))
                .commit();
    }

    private void setImageHeader(Recipe recipe) {
        Drawable placeholder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            placeholder = AppCompatResources.getDrawable(DetailsActivity.this, R.drawable.bg_recipe_placeholder);
        } else {
            placeholder = VectorDrawableCompat.create(getResources(), R.drawable.bg_recipe_placeholder, null);
        }
        GlideApp.with(getApplicationContext())
                .load(VideoThumbnail.from(recipe))
                .error(placeholder)
                .into(((ImageView) findViewById(R.id.iv_recipe_image)));
    }

    private void setStepsAmount(Recipe recipe) {
        LinearLayout layout = findViewById(R.id.steps_amount);
        TextView amount = layout.findViewById(R.id.amount);
        TextView label = layout.findViewById(R.id.label);
        amount.setText(String.valueOf(RecipeUtils.getNumSteps(recipe)));
        label.setText(R.string.steps);
    }

    private void setIngredientsAmount(Recipe recipe) {
        LinearLayout layout = findViewById(R.id.ingredients_amount);
        TextView amount = layout.findViewById(R.id.amount);
        TextView label = layout.findViewById(R.id.label);
        amount.setText(String.valueOf(recipe.getIngredients().size()));
        label.setText(R.string.ingredients);
    }

    private void setServingsAmount(Recipe recipe) {
        LinearLayout layout = findViewById(R.id.servings_amount);
        TextView amount = layout.findViewById(R.id.amount);
        TextView label = layout.findViewById(R.id.label);
        amount.setText(String.valueOf(recipe.getServings()));
        label.setText(R.string.servings);
    }

}
