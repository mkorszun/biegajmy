package com.biegajmy.general;

import android.os.Bundle;
import java.io.Serializable;

public abstract class ModelFragment<T extends Serializable> extends GenericFragment {

    protected T model;

    protected abstract String getModelKey();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) model = (T) arguments.getSerializable(getModelKey());
    }

    @Override public void onDestroy() {
        super.onDestroy();
        model = null;
    }

    @Override public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (state != null) state.putSerializable(getModelKey(), model);
    }

    @Override public void onViewStateRestored(Bundle state) {
        super.onViewStateRestored(state);
        Serializable serializable;
        if (state != null && (serializable = state.getSerializable(getModelKey())) != null) {
            model = (T) serializable;
        }
    }
}
