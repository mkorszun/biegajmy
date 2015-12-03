package com.biegajmy.location;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.events.EventMapBuilder;
import com.biegajmy.utils.SystemUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_event_location) @OptionsMenu(R.menu.menu_event_location) public class LocationActivity
    extends AppCompatActivity implements TextView.OnEditorActionListener {

    public static final int LOCATION_PROVIDED = 456;
    public static final String LOCATION_ARG = "LOCATION_ARG";

    private GoogleMap mMap;

    @Bean protected EventMapBuilder eventMap;
    @Bean protected LocalStorage localStorage;
    @Bean protected LocationResolver locationResolver;

    @ViewById(R.id.address_search) protected EditText address;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @AfterViews public void setup() {
        LatLng loc = getIntent().getParcelableExtra(LOCATION_ARG);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpMap(loc);

        address.setOnEditorActionListener(this);
        address.setText(localStorage.getCurrentCity());
        address.setSelection(address.getText().length());
    }

    @OptionsItem(android.R.id.home) public void close() {
        finish();
    }

    @OptionsItem(R.id.action_location_update) public void updateLocation() {
        setResult(eventMap.getCurrentPosition());
        finish();
    }

    @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        setAddress(v.getText().toString());
        return false;
    }

    @Click(R.id.address_search_button) public void search() {
        setAddress(address.getText().toString());
        SystemUtils.hideKeyboard(this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        address.setOnEditorActionListener(null);
        eventMap.clear();
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

    private void setAddress(String address) {
        LatLng location = locationResolver.getLocation(address);
        if (location != null) {
            eventMap.updatePosition(location);
        } else {
            Toast.makeText(this, R.string.location_not_found, Toast.LENGTH_LONG).show();
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
