package com.jaredrummler.baking.ui.details.adapter;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.model.Ingredient;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.data.model.Step;
import com.jaredrummler.baking.ui.RecyclerViewItemClickListener;
import com.jaredrummler.baking.utils.RecipeUtils;

final class DetailsViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> views = new SparseArray<>();

    DetailsViewHolder(View itemView, RecyclerViewItemClickListener listener) {
        super(itemView);
        if (itemView.findViewById(R.id.tv_step_desc) != null) {
            itemView.setOnClickListener(v -> listener.onClick(v, getAdapterPosition()));
        }
    }

    <T extends View> T findView(@IdRes int id) {
        View v = views.get(id);
        if (v == null) {
            v = itemView.findViewById(id);
            views.put(id, v);
        }
        //noinspection unchecked
        return (T) v;
    }

    void bind(Recipe recipe, Object item, int position) {
        if (item instanceof DetailsAdapter.Header) {
            bind((DetailsAdapter.Header) item);
        } else if (item instanceof Ingredient) {
            bind((Ingredient) item, position);
        } else if (item instanceof Step) {
            bind(recipe, (Step) item, position);
        }
    }

    private void bind(DetailsAdapter.Header header) {
        ((TextView) findView(R.id.tv_header)).setText(header.stringRes);
    }

    private void bind(Ingredient ingredient, int position) {
        itemView.setBackgroundColor(position % 2 == 0 ? 0x00000000 : 0x14000000);
        ((TextView) findView(R.id.tv_ingredient)).setText(RecipeUtils.formatIngredient(ingredient));
    }

    private void bind(Recipe recipe, Step step, int position) {
        // Get the step number
        int stepNumber = position - (recipe.getIngredients().size() + 1);
        // Show/Hide the top/bottom line depending on the step number.
        findView(R.id.top_line).setVisibility(View.VISIBLE);
        findView(R.id.bottom_line).setVisibility(View.VISIBLE);
        if (stepNumber == 1) {
            findView(R.id.top_line).setVisibility(View.GONE);
        } else if (stepNumber == recipe.getSteps().size()) {
            findView(R.id.bottom_line).setVisibility(View.GONE);
        }
        // Set the step info
        findView(R.id.iv_video).setVisibility(
                TextUtils.isEmpty(RecipeUtils.getVideoUrl(step)) ? View.GONE : View.VISIBLE);
        ((TextView) itemView.findViewById(R.id.tv_step_desc)).setText(step.getShortDescription());
        // If the first step is the instructions then increase the step by 1
        boolean isFirstStepIntro = "recipe introduction".equalsIgnoreCase(
                recipe.getSteps().get(0).getShortDescription()
        );
        TextView indicator = itemView.findViewById(R.id.step_indicator);
        if (stepNumber == 1 && isFirstStepIntro) {
            // The first step is the introduction.
            indicator.setText("•");
        } else if (isFirstStepIntro) {
            // The first step is the introduction. Decrease the step number by 1.
            indicator.setText(String.valueOf(--stepNumber));
        } else {
            indicator.setText(String.valueOf(stepNumber));
        }
    }

}