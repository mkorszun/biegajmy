package com.biegajmy.location;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.biegajmy.LocalStorage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

@EService public class LocationService extends Service
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = LocationService.class.getName();
    private static final int LOCATION_UPDATE_INTERVAL = 4 * 60 * 1000;
    private static final int LOCATION_UPDATE_FASTEST_INTERVAL = 30 * 1000;

    @Bean LocalStorage localStorage;
    @Bean LocationResolver locationResolver;
    @Bean LocationUpdatesBus locationUpdatesBus;

    private LastLocation lastLocation;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate() {
        Log.d(TAG, "Starting location service");
        googleApiClient = buildGoogleApiClient();
        locationRequest = buildLocationRequest();
        googleApiClient.connect();
        locationUpdatesBus.register(this);
    }

    @Override public void onDestroy() {
        Log.d(TAG, "Stopping location service");
        locationUpdatesBus.unregister(this);
        removeLocationUpdates();
        googleApiClient.disconnect();
    }

    @Override public IBinder onBind(Intent intent) {
        return null;
    }

    //********************************************************************************************//
    // Google api callbacks
    //********************************************************************************************//

    @Override public void onConnected(Bundle bundle) {
        Log.d(TAG, "Google api connection created. Requesting location updates");
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override public void onConnectionSuspended(int i) {
        Log.w(TAG, String.format("Google api connection suspended, cause: %d", i));
    }

    @Override public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, String.format("Google api connection failed, cause: %s", connectionResult.toString()));
    }

    @Override @Background public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        lastLocation = localStorage.updateLastLocation(latitude, longitude);
        Log.d(TAG, String.format("Updating last location to: %f %f", latitude, longitude));
        locationUpdatesBus.post(new LocationUpdatesBus.LastLocationUpdatedEvent());
        String city = locationResolver.getCity(latitude, longitude);
        if (city != null) localStorage.updateCurrentCity(city);
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe @Background public void lastLocationRequest(LocationUpdatesBus.LastLocationRequestEvent e) {
        LastLocation lastLocation = getLastLocation();
        Log.d(TAG, String.format("Received location update request. Current location: %s", lastLocation));
        if (lastLocation != null) locationUpdatesBus.post(new LocationUpdatesBus.LastLocationUpdatedEvent());
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private LastLocation getLastLocation() {
        return lastLocation != null ? lastLocation : localStorage.getLastLocation();
    }

    private void removeLocationUpdates() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    private LocationRequest buildLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_UPDATE_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return locationRequest;
    }

    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
