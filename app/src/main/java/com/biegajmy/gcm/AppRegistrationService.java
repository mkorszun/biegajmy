package com.biegajmy.gcm;

import android.util.Log;
import com.biegajmy.BuildConfig;
import com.biegajmy.model.Device;
import com.biegajmy.user.UserBackendService_;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;
import org.androidannotations.api.support.app.AbstractIntentService;

@EIntentService public class AppRegistrationService extends AbstractIntentService {

    private static final String TAG = AppRegistrationService.class.getName();

    public AppRegistrationService() {
        super(TAG);
    }

    //********************************************************************************************//
    // Actions
    //********************************************************************************************//

    @ServiceAction void registration() {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String scope = GoogleCloudMessaging.INSTANCE_ID_SCOPE;
            String gcm_token = instanceID.getToken(BuildConfig.SENDER_ID, scope, null);
            Log.d(TAG, String.format("GCM token obtained: %s", gcm_token));
            UserBackendService_.intent(getApplicationContext()).updateDevice(new Device(gcm_token)).start();
        } catch (Exception e) {
            Log.e(TAG, "Failed to update device info", e);
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
