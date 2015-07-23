package com.biegajmy.location;

import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;

public class LastLocation implements Serializable {

    public double lat;
    public double lng;
    public long lastUpdate;

    public LastLocation() {
    }

    public LastLocation(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LatLng get() {
        return new LatLng(lat, lng);
    }
}
