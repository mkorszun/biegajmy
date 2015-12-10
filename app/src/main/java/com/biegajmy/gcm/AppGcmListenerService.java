package com.biegajmy.gcm;

import android.os.Bundle;
import android.util.Log;
import com.biegajmy.BuildConfig;
import com.biegajmy.notifications.NotificationSender;
import com.biegajmy.utils.JSONUtils;
import com.google.android.gms.gcm.GcmListenerService;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.json.JSONObject;

@EService public class AppGcmListenerService extends GcmListenerService {

    private static final String TAG = AppGcmListenerService.class.getName();
    private static final String MESSAGE = "message";
    private static final String PARAMS = "params";

    @Bean NotificationSender notificationSender;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, String.format("Received message %s from %s", data, from));
        if (!from.equals(BuildConfig.SENDER_ID)) return;

        try {
            String message = data.getString(MESSAGE);
            String paramsStr = data.getString(PARAMS);
            JSONObject params = JSONUtils.toObject(paramsStr);

            String event_id = params.getString("event_id");
            String event_name = params.getString("event_name");
            String msg_type = params.getString("msg_type");

            MessageType type = MessageType.valueOf(msg_type);
            UserMessageBus.getInstance().post(new UserMessageBus.NewMessage(event_id, type));
            notificationSender.send(message, event_id, event_name);
        } catch (Exception e) {
            Log.e(TAG, "Failed to read push message", e);
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}