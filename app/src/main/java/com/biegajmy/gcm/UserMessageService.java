package com.biegajmy.gcm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.biegajmy.LocalStorage;
import com.biegajmy.model.Event;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

@EService public class UserMessageService extends Service {

    private static final String TAG = UserMessageService.class.getName();

    @Bean protected LocalStorage localStorage;

    private final Event EVENT = new Event();
    private HashMap<String, Set<MessageType>> messages;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Nullable @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY_COMPATIBILITY;
    }

    @Override public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Starting user message service");
        this.messages = localStorage.getMessages();

        checkMessages();
        UserMessageBus.getInstance().register(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Stopping user message service");
        UserMessageBus.getInstance().unregister(this);

        checkMessages();
        localStorage.updateMessages(messages);
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe @Background public void event(UserMessageBus.NewMessage event) {
        Log.d(TAG, String.format("New message for event: %s", event.id));
        Set<MessageType> types = messages.get(event.id);
        if (types == null) types = new HashSet<>();

        if (!types.contains(event.type)) {
            types.add(event.type);
            messages.put(event.id, types);

            Log.d(TAG, String.format("Message for event %s not yet seen. Notifying", event.id));
            UserMessageBus.getInstance().post(new UserMessageBus.UpdateMessages(event.id, types));
        }
    }

    @Subscribe @Background public void event(UserMessageBus.RemoveMessages event) {
        if (messages.containsKey(event.id)) {
            messages.remove(event.id);

            Log.d(TAG, String.format("Removing messages for event: %s", event.id));
            UserMessageBus.getInstance().post(new UserMessageBus.UpdateMessages(event.id, null));
        }
    }

    @Subscribe @Background public void event(UserMessageBus.CheckMessages event) {
        Log.d(TAG, "Checking messages for user");
        checkMessages();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    @Background protected void checkMessages() {
        List<Event> myEvents = localStorage.get("USER_EVENTS", ArrayList.class);
        if (myEvents == null) return;

        for (String id : messages.keySet()) {
            EVENT.id = id;
            if (!myEvents.contains(EVENT)) {
                Log.d(TAG, String.format("Removing messages for event: %s", id));
                messages.remove(id);
            }
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
