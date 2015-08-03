package com.biegajmy.events;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean public class EventMapBuilder {

    private static final String TAG = EventMapBuilder.class.getName();
    private static final int ZOOM = 13;

    @RootContext Context context;

    private GoogleMap map;
    private LatLng initialPosition;
    private LatLng currentPosition;
    private Marker marker;
    private String title;
    private boolean draggable = false;

    public void build() {

        if (initialPosition != null) {
            setPosition(initialPosition);
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

    public void updateMarker(LatLng loc) {
        setPosition(loc);
    }

    private void setUpTracking() {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override public void onMapClick(LatLng latLng) {
                Log.d(TAG, "New marker position: " + latLng);
                marker.setPosition(latLng);
                currentPosition = latLng;
            }
        });
    }

    private void setPosition(LatLng position) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM));
        CameraPosition.Builder builder = new CameraPosition.Builder();
        CameraPosition cameraPosition = builder.target(position).zoom(ZOOM).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        marker = map.addMarker(new MarkerOptions().position(position).title(title));
    }
}
