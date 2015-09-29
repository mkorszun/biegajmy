package com.biegajmy.auth;

import android.app.Activity;
import android.content.Intent;
import com.afollestad.materialdialogs.MaterialDialog;
import com.biegajmy.R;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean public class LoginDialog extends MaterialDialog.ButtonCallback {

    @RootContext Activity context;

    public void actionConfirmation(int res) {
        new MaterialDialog.Builder(context).content(res)
            .positiveText(R.string.auth_required_yes)
            .negativeText(R.string.auth_required_no)
            .callback(this)
            .show();
    }

    @Override public void onPositive(MaterialDialog dialog) {
        context.startActivityForResult(new Intent(context, LoginActivity.class), 111);
    }
}
