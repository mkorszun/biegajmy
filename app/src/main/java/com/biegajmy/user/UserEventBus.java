package com.biegajmy.user;

import android.os.Handler;
import android.os.Looper;
import com.biegajmy.model.User;
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
        public User user;

        public SyncUserEventOK(User user) {
            this.user = user;
        }
    }

    public static class SyncUserEventNOK {

    }

    //********************************************************************************************//
    // Update user
    //********************************************************************************************//

    public static class UpdateUserEventOk {
        public User user;

        public UpdateUserEventOk(User user) {
            this.user = user;
        }
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
    // Login
    //********************************************************************************************//

    public static class LoginOK {

    }

    public static class LoginNOK {
        public enum Reason {NO_MATCH, UNKNOWN}

        public Reason reason;

        public LoginNOK(Reason reason) {
            this.reason = reason;
        }
    }

    //********************************************************************************************//
    // Registration
    //********************************************************************************************//

    public static class RegistrationOK {

    }

    public static class RegistrationNOK {
        public enum Reason {USER_EXISTS, UNKNOWN}

        public Reason reason;

        public RegistrationNOK(Reason reason) {
            this.reason = reason;
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
