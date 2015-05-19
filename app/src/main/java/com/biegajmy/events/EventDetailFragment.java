package com.biegajmy.events;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;
import com.biegajmy.R;
import com.biegajmy.model.Event;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_event_detail) public class EventDetailFragment extends Fragment {

    public static final String ARG_EVENT = "event";

    private GoogleMap mMap;
    private Event event;

    @Bean EventMapBuilder mapBuilder;
    @ViewById(R.id.headline) protected TextView headline;
    @ViewById(R.id.description) protected TextView description;
    @ViewById(R.id.date) protected TextView date;
    //@ViewById(R.id.duration) protected TextView duration;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_EVENT)) {
            event = (Event) getArguments().getSerializable(ARG_EVENT);
        }
    }

    @AfterViews public void setContent() {
        if (event != null) {
            headline.setText(event.headline);
            description.setText(event.description);
            date.setText(event.dateAndTime);
            //duration.setText(event.duration);
            if (mMap == null) {
                Double lat = event.location.coordinates.get(0);
                Double lon = event.location.coordinates.get(1);
                setUpMap(lat, lon);
            }
        }
    }

    private void setUpMap(double lat, double lon) {
        FragmentManager cfm = getChildFragmentManager();
        Fragment fr = cfm.findFragmentById(R.id.event_location);

        if ((mMap = ((SupportMapFragment) fr).getMap()) != null) {
            mapBuilder.setMap(mMap)
                .setInitialPosition(new LatLng(lat, lon))
                .setDraggable(false)
                .setTitle(event.headline)
                .build();
        }
    }
}
