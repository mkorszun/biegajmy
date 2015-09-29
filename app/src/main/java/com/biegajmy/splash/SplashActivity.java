package com.biegajmy.splash;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import com.biegajmy.R;
import com.biegajmy.ServiceManager;
import com.biegajmy.events.EventMainActivity_;
import com.biegajmy.location.LocationDialog;
import com.biegajmy.location.LocationServiceStatus;
import com.biegajmy.location.LocationUpdatesBus;
import com.biegajmy.tags.TagBackendService_;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import static com.biegajmy.location.LocationUpdatesBus.LastLocationRequestEvent;
import static com.biegajmy.location.LocationUpdatesBus.LastLocationUpdatedEvent;

@EActivity(R.layout.activity_splash) public class SplashActivity extends ActionBarActivity {

    private static final String TAG = SplashActivity.class.getName();

    @Bean ServiceManager serviceManager;
    @Bean LocationDialog locationDialog;
    @Bean LocationUpdatesBus locationUpdatesBus;
    @Bean LocationServiceStatus locationServiceStatus;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @AfterInject public void setup() {
        serviceManager.start();
        locationUpdatesBus.register(this);
        handleLocationServices();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        locationUpdatesBus.unregister(this);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Returning from location settings. Checking if enabled");

        if (locationServiceStatus.isEnabled()) {
            Log.d(TAG, "Location services are enabled. Obtaining last location");
            locationUpdatesBus.post(new LastLocationRequestEvent());
        } else {
            Log.d(TAG, "Location services still disabled. Terminating");
            finish();
        }
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(LastLocationUpdatedEvent event) {
        if (locationServiceStatus.isEnabled()) {
            Log.d(TAG, "Last location obtained. Starting main activity");
            TagBackendService_.intent(this).getTagRecommendations().start();
            TagBackendService_.intent(this).getPopularTags().start();
            EventMainActivity_.intent(this).start();
            finish();
        }
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void handleLocationServices() {
        if (!locationServiceStatus.isEnabled()) {
            locationDialog.build().show();
        } else {
            locationUpdatesBus.post(new LastLocationRequestEvent());
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
