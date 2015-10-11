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
    // Sync user
    //********************************************************************************************//

    public static class SyncUserEventOK {

    }

    public static class SyncUserEventNOK {

    }

    //********************************************************************************************//
    // Update user
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
    // Check token
    //********************************************************************************************//

    public static class TokenOKEvent {
    }

    public static class TokenNOKEvent {
    }

    //********************************************************************************************//
    // Update photo
    //********************************************************************************************//

    public static class UpdateUserPhotoOk {
    }

    public static class UpdateUserPhotoFailed {
        public Exception exception;

        public UpdateUserPhotoFailed(Exception e) {
            this.exception = e;
        }
    }

    //********************************************************************************************//
    // Scale photo
    //********************************************************************************************//

    public static class ScalePhotoOK {
        public String path;

        public ScalePhotoOK(String path) {
            this.path = path;
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
