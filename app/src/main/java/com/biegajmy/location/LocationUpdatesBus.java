package com.biegajmy.location;

import android.os.Handler;
import android.os.Looper;
import com.squareup.otto.Bus;

public class LocationUpdatesBus extends Bus {

    private static LocationUpdatesBus bus;
    private static final Handler mainThread = new Handler(Looper.getMainLooper());

    public static synchronized Bus getInstance() {
        if (bus == null) {
            bus = new LocationUpdatesBus();
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

    public static class LastLocationChangedEvent {
        public LastLocation location;

        public LastLocationChangedEvent(LastLocation location) {
            this.location = location;
        }
    }

    public static class LastLocationRequestEvent {

    }
}
