package com.biegajmy.location;

import android.location.LocationManager;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

@EBean public class LocationServiceStatus {

    @SystemService LocationManager locationManager;

    public boolean isEnabled() {
        try {
            return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            return false;
        }
    }
}
