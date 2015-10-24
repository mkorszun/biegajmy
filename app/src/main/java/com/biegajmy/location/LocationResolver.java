package com.biegajmy.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean public class LocationResolver {

    @RootContext protected Context context;
    private Geocoder geoCoder;

    @AfterInject protected void setup() {
        geoCoder = new Geocoder(context, Locale.getDefault());
    }

    public String getCity(double lat, double lng) {
        try {
            String city = null;
            List<Address> addresses = geoCoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) city = addressToCity(addresses.get(0));
            return city;
        } catch (IOException e) {
            return null;
        }
    }

    public LatLng getLocation(String address) {
        try {
            LatLng loc = null;
            List<Address> addresses = geoCoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) loc = addressToLocation(addresses.get(0));
            return loc;
        } catch (IOException e) {
            return null;
        }
    }

    private LatLng addressToLocation(Address address) {
        return new LatLng(address.getLatitude(), address.getLongitude());
    }

    private String addressToCity(Address address) {
        return address.getLocality();
    }
}
