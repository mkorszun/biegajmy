package com.biegajmy.events;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.events.participants.EventParticipantsFragment;
import com.biegajmy.events.participants.EventParticipantsFragment_;
import com.biegajmy.model.Event;
import com.biegajmy.tags.TagListFragment;
import com.biegajmy.tags.TagListFragment_;
import com.biegajmy.task.JoinEventExecutor;
import com.biegajmy.task.JoinEventTask;
import com.biegajmy.user.UserBasicDetailsFragment_;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.fragment_event_detail) public class EventDetailFragment extends Fragment {

    public static final String ARG_EVENT = "ARG_EVENT";
    private static final String GEO_QUERY = "geo:%f,%f?q=%f,%f(%s)";
    private static final String TAG = EventDetailFragment.class.getName();

    private Event event;
    private GoogleMap mMap;
    private boolean isMember;
    private Activity activity;
    private FragmentManager fm;
    private EventDateTime eventDateTime = new EventDateTime();

    @Bean protected LocalStorage storage;
    @Bean protected EventMapBuilder eventMap;

    @ViewById(R.id.event_date) protected TextView date;
    @ViewById(R.id.event_time) protected TextView time;
    @ViewById(R.id.event_pace) protected TextView pace;
    @ViewById(R.id.event_distance) protected TextView distance;
    @ViewById(R.id.event_description) protected TextView description;
    @ViewById(R.id.event_join) protected Button joinButton;

    @StringRes(R.string.event_join_error_msg) protected String ERROR_MSG;
    @StringRes(R.string.event_join) protected String JOIN_TXT;
    @StringRes(R.string.event_leave) protected String LEAVE_TXT;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = getActivity();
        this.fm = getChildFragmentManager();

        if (getArguments().containsKey(ARG_EVENT)) {
            event = (Event) getArguments().getSerializable(ARG_EVENT);
        }
    }

    @AfterViews public void setContent() {
        if (event != null) {
            updateEventContent();
            updateEventParticipants();
            updateEventTags();
            updateEventOwner();
            updateEventLocation();
        }
    }

    @Click(R.id.event_join) public void joinEvent() {
        String eventId = event.id;
        String token = storage.getToken().token;

        new JoinEventTask(new JoinEventExecutor() {
            @Override public void onSuccess(Event e) {
                EventDetailFragment.this.event = e;
                EventListBus.getInstance().post(e);
                updateEventContent();
            }

            @Override public void onFailure(Exception e) {
                Toast.makeText(activity, ERROR_MSG, Toast.LENGTH_LONG).show();
                Log.e(TAG, String.format("%s failed", msgForAction()), e);
            }
        }).execute(token, eventId, !isMember);
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void updateEventContent() {
        eventDateTime.set(event.dateAndTime);
        date.setText(eventDateTime.getDate().toString());
        time.setText(eventDateTime.getTime().toString());
        pace.setText(event.pace + " MIN/KM");
        distance.setText(event.distance + " KM");
        description.setText(event.description);
        isMember = event.participants.contains(storage.getUser());
        joinButton.setText(msgForAction());
    }

    private void updateEventParticipants() {
        Fragment fr = EventParticipantsFragment_.builder()
            .arg(EventParticipantsFragment.ARG_EVENT_ID, event.id)
            .arg(EventParticipantsFragment.ARG_PARTICIPANTS, event.participants)
            .build();
        fm.beginTransaction().replace(R.id.event_participants_container, fr).commit();
    }

    private void updateEventTags() {
        ArrayList tags = new ArrayList(event.tags);
        TagListFragment_.FragmentBuilder_ builder = TagListFragment_.builder();
        TagListFragment fr = builder.arg(TagListFragment.ARGS_TAGS, tags).build();
        fm.beginTransaction().replace(R.id.event_tags, fr).commit();
    }

    private void updateEventOwner() {
        UserBasicDetailsFragment_ fr = UserBasicDetailsFragment_.newInstance(event.user);
        fm.beginTransaction().replace(R.id.event_owner, fr).commit();
    }

    private void updateEventLocation() {
        double lat = event.location.coordinates.get(0);
        double lng = event.location.coordinates.get(1);
        setUpMap(new LatLng(lat, lng));
    }

    private String msgForAction() {
        return isMember ? LEAVE_TXT : JOIN_TXT;
    }

    private void setUpMap(LatLng loc) {

        FragmentManager cfm = getChildFragmentManager();
        Fragment fr = cfm.findFragmentById(R.id.event_location);

        if ((mMap = ((SupportMapFragment) fr).getMap()) != null) {
            eventMap.setInitialPosition(loc)
                .setMap(mMap)
                .setTitle("")
                .setOnClickListener(getMapClickListener())
                .build();
        }
    }

    private GoogleMap.OnMapClickListener getMapClickListener() {
        return new GoogleMap.OnMapClickListener() {
            @Override public void onMapClick(LatLng latLng) {
                Double lat = event.location.coordinates.get(0);
                Double lon = event.location.coordinates.get(1);
                String label = event.headline;
                String uriString = String.format(GEO_QUERY, lat, lon, lat, lon, label);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
                startActivity(intent);
            }
        };
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
