package com.biegajmy.location;

import com.squareup.otto.Bus;

public class LocationUpdatesBus extends Bus {

    private static LocationUpdatesBus bus;

    public static synchronized Bus getInstance() {
        if (bus == null) {
            bus = new LocationUpdatesBus();
        }

        return bus;
    }

    public static class LastLocationChangedEvent {
        public LastLocation location;
    }

    public static class LastLocationRequestEvent {

    }
}
