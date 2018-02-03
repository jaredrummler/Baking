package com.jaredrummler.baking.widget;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViewsService;

import com.jaredrummler.baking.data.model.Recipe;

import static com.jaredrummler.baking.widget.RecipeWidget.EXTRA_BUNDLE;
import static com.jaredrummler.baking.widget.RecipeWidget.EXTRA_RECIPE;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
        Recipe recipe = bundle.getParcelable(EXTRA_RECIPE);
        return new WidgetViewFactory(this, recipe);
    }

}
