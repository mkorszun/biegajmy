package com.biegajmy;

import android.content.Context;
import android.util.Log;
import com.biegajmy.gcm.AppRegistrationService_;
import com.biegajmy.gcm.UserMessageService_;
import com.biegajmy.location.LocationService_;
import com.crashlytics.android.Crashlytics;
import com.splunk.mint.Mint;
import io.fabric.sdk.android.Fabric;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean(scope = EBean.Scope.Singleton) public class ServiceManager {

    private static final String TAG = ServiceManager.class.getName();

    @RootContext Context context;

    @AfterInject public void initialize() {
        Fabric.with(context, new Crashlytics());
        Mint.initAndStartSession(context, BuildConfig.MINT_TOKEN);
        AppRegistrationService_.intent(context).registration().start();
        Log.d(TAG, "Started monitoring services");
    }

    public synchronized void start() {
        LocationService_.intent(context).start();
        UserMessageService_.intent(context).start();
        Log.d(TAG, "Started location and message services");
    }

    public synchronized void stop() {
        LocationService_.intent(context).stop();
        UserMessageService_.intent(context).stop();
        Log.d(TAG, "Stopped all background services");
    }
}
