package com.biegajmy.user;

import com.squareup.otto.Bus;
import java.util.ArrayList;

public class UserEventBus extends Bus {

    private static UserEventBus bus;

    public static synchronized Bus getInstance() {
        if (bus == null) {
            bus = new UserEventBus();
        }

        return bus;
    }

    public static class UpdateUserTagsEvent {

        public ArrayList<String> tags;

        public UpdateUserTagsEvent(ArrayList<String> tags) {
            this.tags = tags;
        }
    }

    public static class SyncUserDataEvent {

    }
}
