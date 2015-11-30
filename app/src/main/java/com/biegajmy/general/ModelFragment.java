package com.biegajmy.general;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import java.io.Serializable;

public abstract class ModelFragment<T extends Serializable> extends Fragment {

    protected T model;

    protected abstract String getModelKey();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = (T) getArguments().getSerializable(getModelKey());
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
