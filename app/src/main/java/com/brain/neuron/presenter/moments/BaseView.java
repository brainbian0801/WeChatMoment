package com.brain.neuron.presenter.moments;

import android.app.Activity;
import android.content.Context;

public interface BaseView<T> {
    void setPresenter(T presenter);
    Context getBaseContext();
    Activity getBaseActivity();
}
