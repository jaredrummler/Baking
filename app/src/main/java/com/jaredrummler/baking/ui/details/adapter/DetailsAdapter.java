package com.jaredrummler.baking.ui.details.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.model.Ingredient;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.data.model.Step;
import com.jaredrummler.baking.ui.RecyclerViewItemClickListener;

import java.util.LinkedList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsViewHolder>
        implements RecyclerViewItemClickListener {

    private static final int VIEW_TYPE_HEADING = 1;
    private static final int VIEW_TYPE_INGREDIENT = 2;
    private static final int VIEW_TYPE_STEP = 3;

    private final List<Object> items = new LinkedList<>();
    private final RecipeStepClickListener listener;
    private final Recipe recipe;

    public DetailsAdapter(Recipe recipe, RecipeStepClickListener listener) {
        this.listener = listener;
        this.recipe = recipe;
        items.add(new Header(R.string.ingredients));
        items.addAll(recipe.getIngredients());
        items.add(new Header(R.string.steps));
        items.addAll(recipe.getSteps());
    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(getLayoutRes(viewType), parent, false);
        return new DetailsViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {
        Object item = items.get(position);
        holder.bind(recipe, item, position);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof Header) {
            return VIEW_TYPE_HEADING;
        } else if (item instanceof Ingredient) {
            return VIEW_TYPE_INGREDIENT;
        } else if (item instanceof Step) {
            return VIEW_TYPE_STEP;
        } else {
            throw new IllegalStateException("Unknown item at " + position);
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(View view, int position) {
        Object item = items.get(position);
        if (item instanceof Step) {
            Step step = (Step) item;
            listener.onClick(recipe, step);
        }
    }

    @LayoutRes
    private int getLayoutRes(int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADING:
                return R.layout.item_header;
            case VIEW_TYPE_INGREDIENT:
                return R.layout.item_ingredient;
            case VIEW_TYPE_STEP:
                return R.layout.item_step;
            default:
                throw new IllegalStateException();
        }
    }

    public interface RecipeStepClickListener {

        void onClick(Recipe recipe, Step step);
    }

    static final class Header {

        @StringRes
        final int stringRes;

        Header(@StringRes int stringRes) {
            this.stringRes = stringRes;
        }
    }

}
