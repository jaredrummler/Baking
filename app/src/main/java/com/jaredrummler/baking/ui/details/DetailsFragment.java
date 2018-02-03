package com.jaredrummler.baking.ui.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.data.model.Step;
import com.jaredrummler.baking.ui.details.adapter.DetailsAdapter;
import com.jaredrummler.baking.ui.details.adapter.DetailsAdapter.RecipeStepClickListener;
import com.jaredrummler.baking.ui.steps.StepsActivity;

import butterknife.BindBool;
import butterknife.ButterKnife;

public class DetailsFragment extends Fragment implements RecipeStepClickListener {

    private static final String EXTRA_RECIPE = "extras.RECIPE";

    public static DetailsFragment newInstance(Recipe recipe) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @BindBool(R.bool.isTablet)
    boolean isTablet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Recipe recipe = getArguments().getParcelable(EXTRA_RECIPE);
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        RecyclerView recyclerView = view.findViewById(R.id.rv_directions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new DetailsAdapter(recipe, this));
        return view;
    }

    @Override
    public void onClick(Recipe recipe, Step step) {
        if (isTablet) {
            if (getActivity() instanceof DetailsActivity) {
                ((DetailsActivity) getActivity()).openStep(recipe, recipe.getSteps().indexOf(step));
            }
        } else {
            Intent intent = new Intent(getActivity(), StepsActivity.class);
            intent.putExtra(StepsActivity.EXTRA_RECIPE, recipe);
            intent.putExtra(StepsActivity.EXTRA_STEP_POSITION, recipe.getSteps().indexOf(step));
            startActivity(intent);
        }
    }

}
