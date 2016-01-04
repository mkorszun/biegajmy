package com.biegajmy.utils;

import android.content.Context;
import com.afollestad.materialdialogs.MaterialDialog;
import com.biegajmy.R;

public class DialogUtils {

    public interface ActionCallback {
        void ok();
    }

    public static void actionConfirmation(Context context, int res, final ActionCallback callback) {
        new MaterialDialog.Builder(context).content(res)
            .positiveText(R.string.yes)
            .negativeText(R.string.no)
            .callback(new MaterialDialog.ButtonCallback() {
                @Override public void onPositive(MaterialDialog dialog) {
                    callback.ok();
                }
            })
            .show();
    }
}
