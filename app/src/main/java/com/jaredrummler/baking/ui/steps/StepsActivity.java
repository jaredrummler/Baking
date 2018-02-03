package com.jaredrummler.baking.ui.steps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.data.model.Step;
import com.jaredrummler.baking.utils.RecipeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepsActivity extends AppCompatActivity implements StepsView {

    public static final String EXTRA_RECIPE = "extras.RECIPE";
    public static final String EXTRA_STEP_POSITION = "extras.STEP_POSITION";
    private static final String TAG = "StepsActivity";

    @BindView(R.id.btn_previous)
    Button btnPrevious;

    @BindView(R.id.btn_next)
    Button btnNext;

    private Recipe recipe;
    private int stepPos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
        stepPos = getIntent().getIntExtra(EXTRA_STEP_POSITION, 0);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(recipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_steps);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            loadStep(stepPos);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.btn_next)
    @Override
    public void onNext() {
        loadStep(++stepPos);
    }

    @OnClick(R.id.btn_previous)
    @Override
    public void onPrevious() {
        loadStep(--stepPos);
    }

    @Override
    public void onFullscreen() {
        // Hide the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Hide the buttons
        btnPrevious.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);

        // Make SystemUI fullscreen
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void loadStep(int position) {
        // Get the step
        List<Step> steps = recipe.getSteps();
        Step step = steps.get(position);
        // Create the fragment
        StepsFragment fragment = StepsFragment.newInstance(step);
        // Load the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.steps_fragment, fragment)
                .commit();
        // Update the button visibility
        if (position == 0) {
            btnPrevious.setVisibility(View.GONE);
        } else if (position == steps.size() - 1) {
            btnNext.setVisibility(View.GONE);
        } else {
            btnPrevious.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
        }
        // Set ActionBar Subtitle
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (RecipeUtils.isIntoStep(step)) {
                actionBar.setSubtitle(getString(R.string.introduction));
            } else {
                boolean hasIntro = RecipeUtils.isIntoStep(steps.get(0));
                int start = stepPos + (hasIntro ? 0 : 1);
                int end = steps.size() - (hasIntro ? 1 : 0);
                actionBar.setSubtitle(getString(R.string.step_subtitle, start, end));
            }
        }
    }

}
