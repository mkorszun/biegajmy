package com.biegajmy.events.details;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.comments.CommentsListPlaceholderFragment;
import com.biegajmy.comments.CommentsListPlaceholderFragment_;
import com.biegajmy.events.EventDateTime;
import com.biegajmy.events.EventMapBuilder;
import com.biegajmy.events.EventPace;
import com.biegajmy.events.participants.EventParticipantsFragment;
import com.biegajmy.events.participants.EventParticipantsFragment_;
import com.biegajmy.general.ModelFragment;
import com.biegajmy.model.Event;
import com.biegajmy.tags.TagListFragment;
import com.biegajmy.tags.TagListFragment_;
import com.biegajmy.user.UserBasicDetailsFragment;
import com.biegajmy.user.UserBasicDetailsFragment_;
import com.biegajmy.utils.StringUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import java.util.ArrayList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_event_detail) public class EventDetailFragment extends ModelFragment<Event>
    implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    public static final String ARG_EVENT = "ARG_EVENT";

    private EventDateTime eventDateTime = new EventDateTime();

    @Bean protected LocalStorage storage;
    @Bean protected EventMapBuilder eventMap;

    @ViewById(R.id.event_date) protected TextView date;
    @ViewById(R.id.event_time) protected TextView time;
    @ViewById(R.id.event_pace) protected TextView pace;
    @ViewById(R.id.event_distance) protected TextView distance;
    @ViewById(R.id.event_description) protected TextView description;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected String getModelKey() {
        return ARG_EVENT;
    }

    @AfterViews @UiThread public void setContent() {
        if (model != null) {
            updateEventContent();
            updateEventLocation();
        }
    }

    @Override public void onDestroy() {
        super.onDestroy();
        eventMap.clear();
    }

    @Override public boolean onMarkerClick(Marker marker) {
        eventMap.startGoogleMaps(model.headline);
        return true;
    }

    @Override public void onMapClick(LatLng latLng) {
        eventMap.startGoogleMaps(model.headline);
    }

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    public void setContent(Event event) {
        this.model = event;
        setContent();
    }

    //********************************************************************************************//
    // Components
    //********************************************************************************************//

    private void updateEventContent() {
        eventDateTime.set(model.timestamp);
        date.setText(eventDateTime.getDate().toString());
        time.setText(eventDateTime.getTime().toString());

        String pace = new EventPace(model.pace).toString();
        this.pace.setText(pace != null ? pace + " min/km" : "-");
        distance.setText(StringUtils.doubleToString(model.distance) + " km");
        description.setText(model.description);
        boolean isMember = model.participants.contains(storage.getUser());
        ArrayList<String> tags = model.tags == null ? new ArrayList<String>() : model.tags;

        getChildFragmentManager().beginTransaction()
            .replace(R.id.event_participants_container, EventParticipantsFragment_.builder()
                .arg(EventParticipantsFragment.ARG_EVENT_ID, model.id)
                .arg(EventParticipantsFragment.ARG_PARTICIPANTS, model.participants)
                .build())
            .replace(R.id.event_comments, CommentsListPlaceholderFragment_.builder()
                .arg(CommentsListPlaceholderFragment.EVENT_ID_ARG, model.id)
                .arg(CommentsListPlaceholderFragment.COMMENTS_ARG, new ArrayList(model.comments))
                .arg(CommentsListPlaceholderFragment.COMMENTS_READ_ONLY_ARG, !isMember)
                .build())
            .replace(R.id.event_tags, TagListFragment_.builder()
                .arg(TagListFragment.ARGS_TAGS, tags)
                .arg(TagListFragment.ARGS_TAG_RES_ID, R.layout.tag_view_element_read_only)
                .build())
            .replace(R.id.event_owner,
                UserBasicDetailsFragment_.builder().arg(UserBasicDetailsFragment.ARG_USER, model.user).build())
            .commitAllowingStateLoss();
    }

    private void updateEventLocation() {
        if (model.location == null) return;
        double lat = model.location.coordinates.get(1);
        double lng = model.location.coordinates.get(0);
        setUpMap(new LatLng(lat, lng));
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void setUpMap(final LatLng loc) {
        FragmentManager cfm = getChildFragmentManager();
        Fragment fr = cfm.findFragmentById(R.id.event_location);

        ((SupportMapFragment) fr).getMapAsync(new OnMapReadyCallback() {
            @Override public void onMapReady(GoogleMap googleMap) {
                eventMap.setInitialPosition(loc)
                    .setMap(googleMap)
                    .setTitle("")
                    .setOnMarkerClickListener(EventDetailFragment.this)
                    .setOnClickListener(EventDetailFragment.this)
                    .build();
            }
        });
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
