package com.biegajmy.location;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import com.biegajmy.R;
import com.biegajmy.events.EventMapBuilder;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

@EActivity(R.layout.activity_event_location) @OptionsMenu(R.menu.menu_event_location)
public class LocationActivity extends ActionBarActivity {

    public static final int LOCATION_PROVIDED = 456;
    public static final String LOCATION_ARG = "LOCATION_ARG";

    private GoogleMap mMap;
    @Bean protected EventMapBuilder eventMap;

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    @AfterViews public void setup() {
        LatLng loc = getIntent().getParcelableExtra(LOCATION_ARG);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpMap(loc);
    }

    @OptionsItem(android.R.id.home) public void close() {
        finish();
    }

    @OptionsItem(R.id.action_location_update) public void updateLocation() {
        setResult(eventMap.getCurrentPosition());
        finish();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void setUpMap(LatLng loc) {

        FragmentManager cfm = getSupportFragmentManager();
        Fragment fr = cfm.findFragmentById(R.id.event_location);

        if ((mMap = ((SupportMapFragment) fr).getMap()) != null) {
            eventMap.setDraggable(true).setInitialPosition(loc).setMap(mMap).setTitle("").build();
        }
    }

    private void setResult(LatLng loc) {
        Intent intent = new Intent();
        intent.putExtra(LOCATION_ARG, loc);

        if (getParent() == null) {
            setResult(LOCATION_PROVIDED, intent);
        } else {
            getParent().setResult(LOCATION_PROVIDED, intent);
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
