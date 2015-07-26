package com.biegajmy.validators;

import android.content.Context;
import android.widget.TextView;
import java.util.Map;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean public class TextFormValidator {

    @RootContext Context context;

    public boolean validate(Map<TextView, Integer> form) {
        boolean result = true;

        for (Map.Entry<TextView, Integer> e : form.entrySet()) {
            String msg = context.getResources().getString(e.getValue());
            result = result & validate(e.getKey(), msg);
        }

        return result;
    }

    private boolean validate(final TextView view, String errorMessage) {
        if (view.getText().toString().isEmpty()) {
            view.setError(errorMessage);
            return false;
        }
        return true;
    }
}
