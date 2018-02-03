package com.jaredrummler.baking.ui.recipes;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.Resource;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.ui.SpaceItemDecoration;
import com.jaredrummler.baking.ui.details.DetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesFragment extends Fragment implements RecipesAdapter.RecipeClickListener {

    private static final String STATE_RECYCLER_LAYOUT_MANAGER = "recipes.RECYCLER_LAYOUT_MANAGER";

    @BindView(R.id.error_view)
    ViewGroup errorView;

    @BindView(R.id.rv_recipes)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;

    @BindInt(R.integer.rv_recipes_grid_count)
    int gridColumnCount;

    private RecipesViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getActivity(), R.dimen.recipe_divider_height));
        recyclerView.setAdapter(new RecipesAdapter(this, new ArrayList<>()));
        recyclerView.setHasFixedSize(true);

        viewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_RECYCLER_LAYOUT_MANAGER, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable state = savedInstanceState.getParcelable(STATE_RECYCLER_LAYOUT_MANAGER);
            recyclerView.getLayoutManager().onRestoreInstanceState(state);
        }
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.getRecipes().observe(this, resource -> {
            if (listener != null) listener.onObserved(resource);
            switch (resource.status) {
                case SUCCESS:
                    recyclerView.setVisibility(View.VISIBLE);
                    errorView.setVisibility(View.GONE);
                    progressBar.hide();
                    ((RecipesAdapter) recyclerView.getAdapter()).setItems(resource.data);
                    break;
                case ERROR:
                    recyclerView.setVisibility(View.GONE);
                    errorView.setVisibility(View.VISIBLE);
                    progressBar.hide();
                    break;
                case LOADING:
                    recyclerView.setVisibility(View.GONE);
                    errorView.setVisibility(View.GONE);
                    progressBar.show();
                    break;
            }
        });
    }

    private ResponseListener listener;

    @VisibleForTesting
    public void setListener(ResponseListener listener) {
        this.listener = listener;
    }

    @VisibleForTesting
    public interface ResponseListener {

        void onObserved(Resource<List<Recipe>> resource);
    }

}
