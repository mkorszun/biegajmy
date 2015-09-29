package com.biegajmy;

import android.content.Context;
import com.biegajmy.location.LocationService_;
import com.splunk.mint.Mint;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean public class ServiceManager {

    @RootContext Context context;

    public void start() {
        LocationService_.intent(context).start();
        Mint.initAndStartSession(context, BuildConfig.MINT_TOKEN);
    }
}
