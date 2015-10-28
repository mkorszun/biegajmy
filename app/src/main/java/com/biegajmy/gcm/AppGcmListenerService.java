package com.biegajmy.gcm;

import android.os.Bundle;
import android.util.Log;
import com.biegajmy.utils.JSONUtils;
import com.google.android.gms.gcm.GcmListenerService;
import org.androidannotations.annotations.EService;

@EService public class AppGcmListenerService extends GcmListenerService {

    private static final String TAG = AppGcmListenerService.class.getName();
    private static final String ARGS = "args";
    private static final String MESSAGE = "message";

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, String.format("Received message %s from %s", data, from));
        String msg = data.getString(MESSAGE);
        String[] args = JSONUtils.toArray(data.getString(ARGS));
    }

    //********************************************************************************************//
    //********************************************************************************************//
}