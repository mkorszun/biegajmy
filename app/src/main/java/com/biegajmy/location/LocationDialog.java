package com.biegajmy.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import com.biegajmy.R;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;

@EBean public class LocationDialog extends AlertDialog {

    @StringRes(R.string.splash_location_services_disabled) String MSG;
    @StringRes(R.string.splash_location_services_dialog_yes) String YES;
    @StringRes(R.string.splash_location_services_dialog_no) String NO;

    @RootContext Activity context;

    public LocationDialog(Context context) {
        super(context);
    }

    public AlertDialog build() {
        final Builder builder = new Builder(context);
        builder.setMessage(MSG)
            .setCancelable(false)
            .setPositiveButton(YES, new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    Intent it = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivityForResult(it, 1);
                }
            })
            .setNegativeButton(NO, new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                    context.finish();
                }
            });

        return builder.create();
    }
}
