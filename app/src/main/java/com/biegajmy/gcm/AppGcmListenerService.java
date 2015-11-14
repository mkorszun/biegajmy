package com.biegajmy.gcm;

import android.os.Bundle;
import android.util.Log;
import com.biegajmy.notifications.NotificationSender;
import com.biegajmy.utils.JSONUtils;
import com.google.android.gms.gcm.GcmListenerService;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.json.JSONObject;

@EService public class AppGcmListenerService extends GcmListenerService {

    private static final String TAG = AppGcmListenerService.class.getName();
    private static final String MESSAGE = "default";
    private static final String PAYLOAD = "payload";
    private static final String APS = "aps";
    private static final String ALERT = "alert";

    @Bean NotificationSender notificationSender;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, String.format("Received message %s from %s", data, from));
        String string = data.getString(MESSAGE);
        JSONObject msg = JSONUtils.toObject(string);

        try {
            JSONObject aps = msg.getJSONObject(APS);
            String message = aps.getString(ALERT);
            JSONObject payload = msg.getJSONObject(PAYLOAD);
            String event_id = payload.getString("event_id");
            String event_name = payload.getString("event_name");
            notificationSender.send(message, event_id, event_name);
        } catch (Exception e) {
            Log.e(TAG, "Failed to read push message", e);
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}