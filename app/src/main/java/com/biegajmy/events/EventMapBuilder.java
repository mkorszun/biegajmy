package com.biegajmy.events;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.Locale;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean public class EventMapBuilder {

    private static final String TAG = EventMapBuilder.class.getName();
    private static final String GEO_QUERY = "geo:%f,%f?q=%f,%f(%s)";
    private static final int ZOOM = 13;

    @RootContext Context context;

    private GoogleMap map;
    private LatLng initialPosition;
    private LatLng currentPosition;
    private Marker marker;
    private String title;
    private boolean draggable = false;

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    public void build() {

        if (initialPosition != null) {
            setInitialPositin(initialPosition);
        }

        if (draggable) {
            setUpTracking();
        }
    }

    public EventMapBuilder setMap(GoogleMap map) {
        this.map = map;
        return this;
    }

    public EventMapBuilder setInitialPosition(LatLng initialPosition) {
        this.initialPosition = initialPosition;
        this.currentPosition = initialPosition;
        return this;
    }

    public EventMapBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public EventMapBuilder setDraggable(boolean draggable) {
        this.draggable = draggable;
        return this;
    }

    public LatLng getCurrentPosition() {
        return currentPosition;
    }

    public EventMapBuilder setOnClickListener(GoogleMap.OnMapClickListener l) {
        map.getUiSettings().setAllGesturesEnabled(false);
        map.setOnMapClickListener(l);
        return this;
    }

    public EventMapBuilder setOnMarkerClickListener(GoogleMap.OnMarkerClickListener l) {
        map.setOnMarkerClickListener(l);
        return this;
    }

    public void updateMarker(LatLng loc) {
        currentPosition = loc;
        marker.setPosition(loc);
        animate(loc);
    }

    public void clear() {
        setOnClickListener(null);
        setOnMarkerClickListener(null);
        map.clear();
    }

    public void startGoogleMaps(String label) {
        Double lat = currentPosition.latitude;
        Double lon = currentPosition.longitude;
        String uriString = String.format(Locale.ENGLISH, GEO_QUERY, lat, lon, lat, lon, label);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
        context.startActivity(intent);
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void setUpTracking() {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override public void onMapClick(LatLng latLng) {
                Log.d(TAG, "New marker position: " + latLng);
                marker.setPosition(latLng);
                currentPosition = latLng;
            }
        });
    }

    private void setInitialPositin(LatLng position) {
        marker = map.addMarker(new MarkerOptions().position(position).title(title));
        animate(position);
    }

    private void animate(LatLng position) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM));
        CameraPosition.Builder builder = new CameraPosition.Builder();
        CameraPosition cameraPosition = builder.target(position).zoom(ZOOM).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
