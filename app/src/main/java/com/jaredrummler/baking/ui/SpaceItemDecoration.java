package com.jaredrummler.baking.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Adds space between items in the RecyclerView
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int space;

    public SpaceItemDecoration(Context context, @DimenRes int dimension) {
        this.space = (int) context.getResources().getDimension(dimension);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int childPosition = parent.getChildAdapterPosition(view);
        outRect.top = childPosition == 0 ? space : 0;
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            if (childPosition < spanCount) {
                outRect.top = space;
            }
        }
        outRect.bottom = space;
    }

}
