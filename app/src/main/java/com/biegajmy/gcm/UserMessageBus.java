package com.biegajmy.gcm;

import android.os.Handler;
import android.os.Looper;
import com.squareup.otto.Bus;
import java.util.Set;

public class UserMessageBus extends Bus {

    private static UserMessageBus bus;
    private static final Handler mainThread = new Handler(Looper.getMainLooper());

    public static synchronized Bus getInstance() {
        if (bus == null) {
            bus = new UserMessageBus();
        }

        return bus;
    }

    @Override public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mainThread.post(new Runnable() {
                @Override public void run() {
                    post(event);
                }
            });
        }
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    public static class NewMessage {
        public String id;
        public MessageType type;

        public NewMessage(String id, MessageType type) {
            this.id = id;
            this.type = type;
        }
    }

    public static class RemoveMessages {
        public String id;

        public RemoveMessages(String id) {
            this.id = id;
        }
    }

    public static class UpdateMessages {
        public String id;
        public Set<MessageType> types;

        public UpdateMessages(String id, Set<MessageType> types) {
            this.id = id;
            this.types = types;
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
