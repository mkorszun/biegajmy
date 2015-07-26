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
import com.biegajmy.model.Event;
import com.biegajmy.tags.TagListFragment;
import com.biegajmy.tags.TagListFragment_;
import com.biegajmy.task.JoinEventExecutor;
import com.biegajmy.task.JoinEventTask;
import com.biegajmy.user.UserBasicDetailsFragment_;
import java.util.ArrayList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.fragment_event_detail) public class EventDetailFragment extends Fragment {

    public static final String ARG_EVENT = "event";
    public static final String GEO_QUERY = "geo:%f,%f?q=%f,%f(%s)";
    private static final String TAG = EventDetailFragment.class.getName();

    private Event event;
    private boolean isMember;
    private Activity activity;
    private FragmentManager fm;

    @Bean LocalStorage storage;
    @ViewById(R.id.event_duration) protected TextView duration;
    @ViewById(R.id.event_time) protected TextView date;
    @ViewById(R.id.event_spots) protected TextView spots;
    @ViewById(R.id.event_description) protected TextView description;
    @ViewById(R.id.event_join) protected Button joinButton;

    @StringRes(R.string.event_join_error_msg) protected String ERROR_MSG;
    @StringRes(R.string.event_join) protected String JOIN_TXT;
    @StringRes(R.string.event_leave) protected String LEAVE_TXT;

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
        }
    }

    @Click(R.id.event_location) public void showLocation() {
        Double lat = event.location.coordinates.get(0);
        Double lon = event.location.coordinates.get(1);
        String label = event.headline;
        String uriString = String.format(GEO_QUERY, lat, lon, lat, lon, label);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
        startActivity(intent);
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

    private void updateEventContent() {
        duration.setText(String.valueOf(event.duration));
        date.setText(event.dateAndTime);
        spots.setText(String.valueOf(event.spots));
        description.setText(event.description);
        isMember = event.participants.contains(storage.getUser());
        joinButton.setText(msgForAction());
    }

    private void updateEventParticipants() {
        Fragment fr = EventParticipantsFragment.newInstance(event.id, event.participants);
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

    private String msgForAction() {
        return isMember ? LEAVE_TXT : JOIN_TXT;
    }
}
