package com.biegajmy.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;

public class AppInstanceIDListenerService extends InstanceIDListenerService {

    @Override public void onTokenRefresh() {
        AppRegistrationService_.intent(getApplication()).registration().start();
    }
}
