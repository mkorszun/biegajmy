package com.biegajmy.user;

import com.biegajmy.model.User;
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

    public static class UpdateUserEvent {
        public User user;

        public UpdateUserEvent(User user) {
            this.user = user;
        }
    }

    public static class UpdateUserEventOk {
    }

    public static class UpdateUserEventFailed {
        public Exception exception;

        public UpdateUserEventFailed(Exception e) {
            this.exception = e;
        }
    }

    public static class SyncUserDataEvent {

    }
}
