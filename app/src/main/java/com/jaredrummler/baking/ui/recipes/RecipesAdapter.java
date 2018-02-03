package com.jaredrummler.baking.ui.recipes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.model.Recipe;
import com.jaredrummler.baking.image.GlideApp;
import com.jaredrummler.baking.image.VideoThumbnail;
import com.jaredrummler.baking.ui.RecyclerViewItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> implements RecyclerViewItemClickListener {

    private final RecipeClickListener clickListener;
    private final List<Recipe> recipes;

    public RecipesAdapter(@NonNull RecipeClickListener clickListener, @NonNull List<Recipe> recipes) {
        this.clickListener = clickListener;
        this.recipes = recipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Recipe recipe = recipes.get(position);
        // Set the recipe name
        holder.nameTextView.setText(recipe.getName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable leftDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_person_white_24dp);
            holder.servingsTextView.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
        } else {
            Drawable leftDrawable = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_person_white_24dp, null);
            holder.servingsTextView.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
        }
        holder.servingsTextView.setText(String.valueOf(recipe.getServings()));

        // Set the recipe image (uses the last step's video thumbnail)
        Drawable placeholder = getPlaceholder(holder.itemView.getContext());
        GlideApp.with(holder.itemView.getContext())
                .load(VideoThumbnail.from(recipe))
                .placeholder(placeholder)
                .error(placeholder)
                .into(holder.videoThumbnail);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    @Override
    public void onClick(View view, int position) {
        clickListener.onClick(recipes.get(position));
    }

    public void setItems(List<Recipe> items) {
        recipes.clear();
        recipes.addAll(items);
        notifyDataSetChanged();
    }

    private Drawable getPlaceholder(Context context) {
        Drawable placeholder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            placeholder = AppCompatResources.getDrawable(context, R.drawable.bg_recipe_placeholder);
        } else {
            placeholder = VectorDrawableCompat.create(context.getResources(), R.drawable.bg_recipe_placeholder, null);
        }
        return placeholder;
    }

    public interface RecipeClickListener {

        void onClick(Recipe recipe);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_recipe_video_thumbnail)
        ImageView videoThumbnail;

        @BindView(R.id.tv_servings)
        TextView servingsTextView;

        @BindView(R.id.tv_recipe_name)
        TextView nameTextView;

        ViewHolder(View itemView, RecyclerViewItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener((view) -> listener.onClick(view, getAdapterPosition()));
        }
    }

}
