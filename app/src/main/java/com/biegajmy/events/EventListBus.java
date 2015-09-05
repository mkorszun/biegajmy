package com.biegajmy.events;

import android.os.Handler;
import android.os.Looper;
import com.biegajmy.model.Event;
import com.squareup.otto.Bus;
import java.util.List;

public class EventListBus extends Bus {

    private static EventListBus bus;
    private static final Handler mainThread = new Handler(Looper.getMainLooper());

    public static synchronized Bus getInstance() {
        if (bus == null) {
            bus = new EventListBus();
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

    public static class EventCreateOK {

    }

    public static class EventCreateNOK {

    }

    public static class EventUpdateOK {
        public Event event;

        public EventUpdateOK(Event event) {
            this.event = event;
        }
    }

    public static class EventUpdateNOK {

    }

    public static class ListUserEventsOK {
        public List<Event> events;

        public ListUserEventsOK(List<Event> events) {
            this.events = events;
        }
    }

    public static class ListUserEventsNOK {

    }
}
