package com.biegajmy.events.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.auth.LoginActivity;
import com.biegajmy.auth.LoginDialog;
import com.biegajmy.comments.CommentsListPlaceholderFragment;
import com.biegajmy.comments.CommentsListPlaceholderFragment_;
import com.biegajmy.comments.CommentsUtils;
import com.biegajmy.events.EventBackendService_;
import com.biegajmy.events.EventDateTime;
import com.biegajmy.events.EventListBus;
import com.biegajmy.events.EventMapBuilder;
import com.biegajmy.events.participants.EventParticipantsFragment;
import com.biegajmy.events.participants.EventParticipantsFragment_;
import com.biegajmy.model.Event;
import com.biegajmy.tags.TagListFragment;
import com.biegajmy.tags.TagListFragment_;
import com.biegajmy.user.UserBasicDetailsFragment;
import com.biegajmy.user.UserBasicDetailsFragment_;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.fragment_event_detail) @OptionsMenu(R.menu.menu_fragment_event_details)
public class EventDetailFragment extends Fragment
    implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    public static final String ARG_EVENT = "ARG_EVENT";

    private Event event;
    private boolean isMember;
    private boolean owner;
    private EventDateTime eventDateTime = new EventDateTime();

    @Bean protected LocalStorage storage;
    @Bean protected EventMapBuilder eventMap;
    @Bean protected LoginDialog loginDialog;

    @ViewById(R.id.event_date) protected TextView date;
    @ViewById(R.id.event_time) protected TextView time;
    @ViewById(R.id.event_pace) protected TextView pace;
    @ViewById(R.id.event_distance) protected TextView distance;
    @ViewById(R.id.event_description) protected TextView description;

    @StringRes(R.string.event_join) protected String JOIN_TXT;
    @StringRes(R.string.event_leave) protected String LEAVE_TXT;
    @StringRes(R.string.event_delete) protected String DELETE_TXT;

    @OptionsMenuItem(R.id.action_delete_event) protected MenuItem delete;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = (Event) getArguments().getSerializable(ARG_EVENT);
        EventListBus.getInstance().register(this);
    }

    @Override public void onResume() {
        super.onResume();
        EventBackendService_.intent(getActivity()).getEvent(event.id).start();
    }

    @AfterViews public void setContent() {
        if (event != null) {
            updateEventContent();
            updateEventLocation();
        }
    }

    @Override public void onDestroy() {
        super.onDestroy();
        eventMap.clear();
        EventListBus.getInstance().unregister(this);
    }

    @Override public boolean onMarkerClick(Marker marker) {
        eventMap.startGoogleMaps(event.headline);
        return true;
    }

    @Override public void onMapClick(LatLng latLng) {
        eventMap.startGoogleMaps(event.headline);
    }

    @OptionsItem(R.id.action_delete_event) public void action() {
        if (storage.hasToken()) {
            if (event.spots == 1 && owner && isMember) {
                EventBackendService_.intent(getActivity()).deleteEvent(event.id).start();
            } else {
                EventBackendService_.intent(getActivity()).joinEvent(event.id, !isMember).start();
            }
        } else {
            loginDialog.actionConfirmation(R.string.auth_required_join, LoginDialog.JOIN_EVENT_REQUEST);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == LoginActivity.RESULT_OK && requestCode == LoginDialog.JOIN_EVENT_REQUEST) {
            EventBackendService_.intent(getActivity()).joinEvent(event.id, !isMember).start();
        }
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        delete.setTitle(msgForAction());
        super.onCreateOptionsMenu(menu, inflater);
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(EventListBus.EventJoinLeaveOK event) {
        this.event = event.event;
        setContent();
    }

    @Subscribe public void event(EventListBus.EventJoinLeaveNOK event) {
        Toast.makeText(getActivity(), R.string.event_error_msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(EventListBus.GetEventDetailsOK event) {
        this.event = event.event;
        setContent();
    }

    @Subscribe public void event(EventListBus.GetEventDetailsNOK event) {
        Toast.makeText(getActivity(), R.string.event_error_msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(EventListBus.DeleteEventOK event) {
        getActivity().finish();
    }

    @Subscribe public void event(EventListBus.DeleteEventNOK event) {
        Toast.makeText(getActivity(), R.string.event_error_msg, Toast.LENGTH_LONG).show();
    }

    //********************************************************************************************//
    // Components
    //********************************************************************************************//

    private void updateEventContent() {
        eventDateTime.set(event.timestamp);
        date.setText(eventDateTime.getDate().toString());
        time.setText(eventDateTime.getTime().toString());
        pace.setText(event.pace + " MIN/KM");
        distance.setText(event.distance + " KM");
        description.setText(event.description);
        isMember = event.participants.contains(storage.getUser());
        owner = event.user.equals(storage.getUser()) && storage.hasToken();
        if (delete != null) delete.setTitle(msgForAction());

        getChildFragmentManager().beginTransaction()
            .replace(R.id.event_participants_container, EventParticipantsFragment_.builder()
                .arg(EventParticipantsFragment.ARG_EVENT_ID, event.id)
                .arg(EventParticipantsFragment.ARG_PARTICIPANTS, event.participants)
                .build())
            .replace(R.id.event_comments, CommentsListPlaceholderFragment_.builder()
                .arg(CommentsListPlaceholderFragment.EVENT_ID_ARG, event.id)
                .arg(CommentsListPlaceholderFragment.COMMENTS_ARG, CommentsUtils.getLast(event.comments))
                .arg(CommentsListPlaceholderFragment.COMMENTS_READ_ONLY_ARG, !isMember)
                .build())
            .replace(R.id.event_tags,
                TagListFragment_.builder().arg(TagListFragment.ARGS_TAGS, new ArrayList(event.tags)).build())
            .replace(R.id.event_owner,
                UserBasicDetailsFragment_.builder().arg(UserBasicDetailsFragment.ARG_USER, event.user).build())
            .commit();
    }

    private void updateEventLocation() {
        double lat = event.location.coordinates.get(1);
        double lng = event.location.coordinates.get(0);
        setUpMap(new LatLng(lat, lng));
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private String msgForAction() {
        return isMember ? (event.spots == 1 && owner ? DELETE_TXT : LEAVE_TXT) : JOIN_TXT;
    }

    private void setUpMap(LatLng loc) {

        GoogleMap mMap;
        FragmentManager cfm = getChildFragmentManager();
        Fragment fr = cfm.findFragmentById(R.id.event_location);

        if ((mMap = ((SupportMapFragment) fr).getMap()) != null) {
            eventMap.setInitialPosition(loc)
                .setMap(mMap)
                .setTitle("")
                .setOnMarkerClickListener(this)
                .setOnClickListener(this)
                .build();
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
