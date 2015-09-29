package com.biegajmy.location;

import android.os.Handler;
import android.os.Looper;
import com.squareup.otto.Bus;
import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton) public class LocationUpdatesBus extends Bus {

    private static final Handler mainThread = new Handler(Looper.getMainLooper());

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

    public static class LocationServiceStartedEvent {
    }

    public static class LastLocationUpdatedEvent {
    }

    public static class LastLocationRequestEvent {
    }
}
