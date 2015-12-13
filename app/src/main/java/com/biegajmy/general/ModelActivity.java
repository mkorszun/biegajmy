package com.biegajmy.general;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.io.Serializable;

public abstract class ModelActivity<T extends Serializable> extends AppCompatActivity {

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    protected T model;

    protected abstract String getModelKey();

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        Serializable serializable = getIntent().getSerializableExtra(getModelKey());
        if (serializable != null) model = (T) serializable;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        model = null;
    }

    @Override public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (state != null) state.putSerializable(getModelKey(), model);
    }

    @Override protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Serializable serializable;
        if (state != null && (serializable = state.getSerializable(getModelKey())) != null) {
            model = (T) serializable;
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
