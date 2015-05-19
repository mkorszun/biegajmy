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
            setInitialPoint();
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

    private void setUpTracking() {
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override public void onMarkerDragStart(Marker marker) {
            }

            @Override public void onMarkerDrag(Marker marker) {
            }

            @Override public void onMarkerDragEnd(Marker marker) {
                LatLng newPosition = marker.getPosition();
                Log.d(TAG, "New marker position: " + newPosition);
                currentPosition = newPosition;
            }
        });
        marker.setDraggable(true);
    }

    private void setInitialPoint() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, ZOOM));
        CameraPosition.Builder builder = new CameraPosition.Builder();
        CameraPosition cameraPosition = builder.target(initialPosition).zoom(ZOOM).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        marker = map.addMarker(new MarkerOptions().position(initialPosition).title(title));
    }
}
