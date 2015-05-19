package com.biegajmy.events;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.model.NewEvent;
import com.biegajmy.task.CreateEventExecutor;
import com.biegajmy.task.CreateEventTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import java.util.LinkedList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import static java.util.Arrays.asList;

@EFragment(R.layout.fragment_event_new) @OptionsMenu(R.menu.menu_event_new)
public class EventNewFragment extends Fragment {

    private Activity activity;
    private GoogleMap mMap;

    @Bean LocalStorage storage;
    @Bean EventMapBuilder eventMap;
    @ViewById(R.id.new_event_headline) TextView headline;
    @ViewById(R.id.new_event_description) TextView description;
    @ViewById(R.id.new_event_date) TextView date;
    @ViewById(R.id.new_event_duration) TextView duration;
    @ViewById(R.id.new_event_spots) TextView spots;
    @ViewById(R.id.new_event_tags) TextView tags;
    @SystemService LocationManager locationManager;

    @AfterViews public void setContent() {
        activity = getActivity();

        if (mMap == null) {
            Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            setUpMap(loc.getLatitude(), loc.getLongitude());
        }
    }

    @OptionsItem(R.id.action_event_save) void createEvent() {
        NewEvent event = new NewEvent();
        event.headline = headline.getText().toString();
        event.description = description.getText().toString();
        event.dateAndTime = date.getText().toString();
        event.duration = Integer.valueOf(duration.getText().toString());
        event.spots = Integer.valueOf(spots.getText().toString());
        event.tags = new LinkedList(asList(tags.getText().toString().split(" ")));
        event.x = eventMap.getCurrentPosition().latitude;
        event.y = eventMap.getCurrentPosition().longitude;

        new CreateEventTask(new CreateEventExecutor() {
            @Override public void onSuccess() {
                Toast.makeText(activity, "Event saved", Toast.LENGTH_LONG).show();
                activity.finish();
            }

            @Override public void onFailure(Exception e) {
                Toast.makeText(activity, "Event creation failed: " + e, Toast.LENGTH_LONG).show();
            }
        }).execute(storage.getToken(), event);
    }

    private void setUpMap(double lat, double lon) {

        FragmentManager cfm = getChildFragmentManager();
        Fragment fr = cfm.findFragmentById(R.id.new_event_location);

        if ((mMap = ((SupportMapFragment) fr).getMap()) != null) {
            eventMap.setDraggable(true)
                .setInitialPosition(new LatLng(lat, lon))
                .setMap(mMap)
                .setTitle("")
                .build();
        }
    }
}
