package com.biegajmy.gcm;

import android.os.Bundle;
import android.util.Log;
import com.biegajmy.R;
import com.biegajmy.notifications.NotificationSender;
import com.biegajmy.utils.JSONUtils;
import com.google.android.gms.gcm.GcmListenerService;
import java.util.HashMap;
import java.util.Map;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.res.StringRes;
import org.json.JSONObject;

@EService public class AppGcmListenerService extends GcmListenerService {

    private static final String TAG = AppGcmListenerService.class.getName();
    private static final String MESSAGE = "default";
    private static final String PAYLOAD = "payload";
    private static final String APS = "aps";

    @StringRes(R.string.push_message_event_updated) protected String EVENT_UPDATED;
    @StringRes(R.string.push_message_new_participant) protected String NEW_PARTICIPANT;
    @StringRes(R.string.push_message_new_comment) protected String NEW_COMMENT;

    @StringRes(R.string.push_message_event_updated_type) protected String EVENT_UPDATED_TYPE;
    @StringRes(R.string.push_message_new_participant_type) protected String NEW_PARTICIPANT_TYPE;
    @StringRes(R.string.push_message_new_comment_type) protected String NEW_COMMENT_TYPE;

    private Map<String, String> MESSAGES = new HashMap<>();

    @Bean NotificationSender notificationSender;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @AfterInject public void setup() {
        MESSAGES.put(NEW_PARTICIPANT_TYPE, NEW_PARTICIPANT);
        MESSAGES.put(NEW_COMMENT_TYPE, NEW_COMMENT);
        MESSAGES.put(EVENT_UPDATED_TYPE, EVENT_UPDATED);
    }

    @Override public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, String.format("Received message %s from %s", data, from));
        String string = data.getString(MESSAGE);
        JSONObject msg = JSONUtils.toObject(string);

        try {
            JSONObject aps = msg.getJSONObject(APS);
            JSONObject payload = aps.getJSONObject(PAYLOAD);
            String msg_type = payload.getString("msg_type");
            String event_id = payload.getString("event_id");
            String event_name = payload.getString("event_name");
            String format = MESSAGES.get(msg_type);
            notificationSender.send(String.format(format, event_name), event_id, event_name);
        } catch (Exception e) {
            Log.e(TAG, "Failed to read push message", e);
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}