package com.biegajmy.validators;

import android.widget.TextView;
import com.biegajmy.R;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.IntegerRes;
import org.androidannotations.annotations.res.StringRes;

@EBean(scope = EBean.Scope.Singleton) public class PasswordValidator {

    @IntegerRes(R.integer.min_password_length) int MIN_PASSWORD_LENGTH;
    @StringRes(R.string.password_too_short) protected String PASSWORD_TOO_SHORT;
    @StringRes(R.string.password_do_not_match) protected String PASSWORD_DO_NOT_MATCH;

    public boolean validate(TextView view1, TextView view2) {

        if (!view1.getText().toString().equals(view2.getText().toString())) {
            view2.setError(PASSWORD_DO_NOT_MATCH);
            return false;
        }

        if (view1.getText().length() < MIN_PASSWORD_LENGTH) {
            view1.setError(PASSWORD_TOO_SHORT);
            return false;
        }

        return true;
    }
}
