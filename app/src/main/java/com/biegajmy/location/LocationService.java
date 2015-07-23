package com.biegajmy.location;

import android.app.Service;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import com.biegajmy.LocalStorage;
import com.biegajmy.location.LocationUpdatesBus.LastLocationChangedEvent;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.SystemService;

import static android.location.LocationManager.NETWORK_PROVIDER;

@EService public class LocationService extends Service implements GpsStatus.Listener {

    private static final String TAG = LocationService.class.getName();

    @Bean LocalStorage localStorage;
    @SystemService LocationManager locationManager;

    @Override public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Starting location service");
        LocationUpdatesBus.getInstance().register(this);
        locationManager.addGpsStatusListener(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Stopping location service");
        LocationUpdatesBus.getInstance().unregister(this);
        locationManager.removeGpsStatusListener(this);
    }

    @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public void onGpsStatusChanged(int event) {
        Log.d(TAG, "Location update received");
        updateLastKnownLocation();
    }

    @Subscribe public void lastLocationRequest(LocationUpdatesBus.LastLocationRequestEvent e) {
        Log.d(TAG, "Location update requested");
        updateLastKnownLocation();
    }

    private boolean updateLastKnownLocation() {
        Location location = locationManager.getLastKnownLocation(NETWORK_PROVIDER);

        if (location != null) {
            locationManager.removeGpsStatusListener(this);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LastLocation lastLocation = localStorage.updateLastLocation(latitude, longitude);

            Log.d(TAG, String.format("Updating last location to: %f %f", latitude, longitude));
            LastLocationChangedEvent locationEvent = new LastLocationChangedEvent();
            locationEvent.location = lastLocation;
            LocationUpdatesBus.getInstance().post(locationEvent);
            return true;
        }

        return false;
    }
}
