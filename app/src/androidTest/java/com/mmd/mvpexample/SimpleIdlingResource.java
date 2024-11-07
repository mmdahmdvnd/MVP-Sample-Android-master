package com.mmd.mvpexample;

import androidx.test.espresso.IdlingResource;

public class SimpleIdlingResource implements IdlingResource {

    private volatile ResourceCallback callback;
    private boolean isIdleNow = true;

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        this.isIdleNow = isIdleNow;
        if (isIdleNow && callback != null) {
            callback.onTransitionToIdle();
        }
    }
}
