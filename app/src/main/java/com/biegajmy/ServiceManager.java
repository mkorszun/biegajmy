package com.biegajmy;

import android.content.Context;
import com.biegajmy.gcm.AppRegistrationService_;
import com.biegajmy.location.LocationService_;
import com.crashlytics.android.Crashlytics;
import com.splunk.mint.Mint;
import io.fabric.sdk.android.Fabric;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean public class ServiceManager {

    @RootContext Context context;

    public void start() {
        Fabric.with(context, new Crashlytics());
        LocationService_.intent(context).start();
        Mint.initAndStartSession(context, BuildConfig.MINT_TOKEN);
        AppRegistrationService_.intent(context).registration().start();
    }
}
