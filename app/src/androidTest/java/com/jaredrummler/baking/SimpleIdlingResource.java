package com.jaredrummler.baking;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleIdlingResource implements IdlingResource {

    private AtomicBoolean isIdle = new AtomicBoolean(true);
    private ResourceCallback callback;

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdle.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    public void setIdleState(boolean idle) {
        isIdle.set(idle);
        if (idle && callback != null) {
            callback.onTransitionToIdle();
        }
    }

}
