package com.biegajmy.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import com.biegajmy.R;
import com.biegajmy.events.EventMainActivity_;
import com.biegajmy.location.LocationDialog;
import com.biegajmy.location.LocationServiceStatus;
import com.biegajmy.location.LocationUpdatesBus;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import static com.biegajmy.location.LocationUpdatesBus.LastLocationChangedEvent;
import static com.biegajmy.location.LocationUpdatesBus.LastLocationRequestEvent;

@EActivity(R.layout.activity_splash) public class SplashActivity extends ActionBarActivity {

    private static final String TAG = SplashActivity.class.getName();

    Bus bus;
    @Bean LocationDialog locationDialog;
    @Bean LocationServiceStatus locationServiceStatus;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus = LocationUpdatesBus.getInstance();
        bus.register(this);

        if (!locationServiceStatus.isEnabled()) {
            locationDialog.build().show();
        }

        bus.post(new LastLocationRequestEvent());
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Returning from location settings. Checking if enabled");

        if (locationServiceStatus.isEnabled()) {
            Log.d(TAG, "Location services are enabled. Requesting last location");
            bus.post(new LastLocationRequestEvent());
        } else {
            Log.d(TAG, "Location services still disabled. Terminating");
            finish();
        }
    }

    @Subscribe public void onLastLocation(LastLocationChangedEvent event) {
        Log.d(TAG, "Last location obtained. Starting main activity");
        Intent intent = new Intent(this, EventMainActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }
}
