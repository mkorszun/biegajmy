package com.biegajmy.auth;

import android.app.Activity;
import com.afollestad.materialdialogs.MaterialDialog;
import com.biegajmy.R;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean public class LoginDialog extends MaterialDialog.ButtonCallback {

    public static final int CREATE_EVENT_REQUEST = 111;
    public static final int CREATE_PROFILE_REQUEST = 222;
    public static final int JOIN_EVENT_REQUEST = 333;

    @RootContext Activity context;
    private int requestCode;

    public void actionConfirmation(int requestCode) {
        actionConfirmation(R.string.auth_required_create, requestCode);
    }

    public void actionConfirmation(int res, int requestCode) {
        this.requestCode = requestCode;
        new MaterialDialog.Builder(context).content(res)
            .positiveText(R.string.auth_required_yes)
            .negativeText(R.string.auth_required_no)
            .callback(this)
            .show();
    }

    @Override public void onPositive(MaterialDialog dialog) {
        LoginActivity_.intent(context).startForResult(requestCode);
    }
}
