package com.biegajmy.validators;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.widget.TextView;
import java.util.Map;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean public class TextFormValidator {

    @RootContext Context context;

    public boolean validateEmail(TextView email, int res) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            return true;
        } else {
            email.setError(context.getResources().getString(res));
            return false;
        }
    }

    public boolean validate(Map<TextView, Integer> form) {
        boolean result = true;

        for (Map.Entry<TextView, Integer> e : form.entrySet()) {
            String msg = context.getResources().getString(e.getValue());
            result = result & validate(e.getKey(), msg);
        }

        return result;
    }

    public boolean validate1(Map<TextInputLayout, Integer> form) {
        boolean result = true;

        for (Map.Entry<TextInputLayout, Integer> e : form.entrySet()) {
            e.getKey().setErrorEnabled(false);
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

    private boolean validate(final TextInputLayout view, String errorMessage) {
        TextView tw = (TextView) view.getChildAt(0);
        if (tw.getText().toString().isEmpty()) {
            view.setError(errorMessage);
            return false;
        }
        return true;
    }
}
