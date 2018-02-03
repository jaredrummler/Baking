package com.jaredrummler.baking.ui.details;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsFragment extends Fragment implements RecipeStepClickListener {

    private static final String TAG = "DetailsFragment";

    private static final String EXTRA_RECIPE = "extras.RECIPE";
    private static final String STATE_RECYCLER_LAYOUT_MANAGER = "recipes.RECYCLER_LAYOUT_MANAGER";

    public static DetailsFragment newInstance(Recipe recipe) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rv_directions)
    RecyclerView recyclerView;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new DetailsAdapter(recipe, this));
        recyclerView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState() called with: outState = [" + outState + "]");
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_RECYCLER_LAYOUT_MANAGER, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewStateRestored() called with: savedInstanceState = [" + savedInstanceState + "]");
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable state = savedInstanceState.getParcelable(STATE_RECYCLER_LAYOUT_MANAGER);
            recyclerView.getLayoutManager().onRestoreInstanceState(state);
        }
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
