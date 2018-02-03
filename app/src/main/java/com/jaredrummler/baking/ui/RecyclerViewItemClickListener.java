package com.jaredrummler.baking.ui;

import android.view.View;

/**
 * Callback when a view is clicked from the ViewHolder
 */
public interface RecyclerViewItemClickListener {

    /**
     * Called when an item is clicked
     *
     * @param view     The view that was clicked.
     * @param position The position of the item.
     */
    void onClick(View view, int position);
}
