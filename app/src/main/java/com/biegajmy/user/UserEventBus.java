package com.biegajmy.user;

import android.os.Handler;
import android.os.Looper;
import com.squareup.otto.Bus;

public class UserEventBus extends Bus {

    private static UserEventBus bus;
    private static final Handler mainThread = new Handler(Looper.getMainLooper());

    public static synchronized Bus getInstance() {
        if (bus == null) {
            bus = new UserEventBus();
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
    // Update user events
    //********************************************************************************************//

    public static class UpdateUserEventOk {
    }

    public static class UpdateUserEventFailed {
        public Exception exception;

        public UpdateUserEventFailed(Exception e) {
            this.exception = e;
        }
    }

    //********************************************************************************************//
    // Check token events
    //********************************************************************************************//

    public static class TokenOKEvent {
    }

    public static class TokenNOKEvent {
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
