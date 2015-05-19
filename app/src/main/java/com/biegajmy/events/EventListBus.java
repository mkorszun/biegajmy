package com.biegajmy.events;

import com.squareup.otto.Bus;

public class EventListBus extends Bus {

    private static EventListBus bus;

    public static synchronized Bus getInstance() {
        if (bus == null) {
            bus = new EventListBus();
        }

        return bus;
    }
}
