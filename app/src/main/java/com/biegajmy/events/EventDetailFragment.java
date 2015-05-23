package com.biegajmy.events;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.model.Event;
import com.biegajmy.task.JoinEventExecutor;
import com.biegajmy.task.JoinEventTask;
import com.squareup.picasso.Picasso;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_event_detail) public class EventDetailFragment extends Fragment {

    public static final String ARG_EVENT = "event";
    public static final String GEO_QUERY = "geo:%f,%f?q=%f,%f(%s)";

    private Event event;
    private Activity activity;

    @Bean LocalStorage storage;
    @ViewById(R.id.event_duration) protected TextView duration;
    @ViewById(R.id.event_time) protected TextView date;
    @ViewById(R.id.event_spots) protected TextView spots;
    @ViewById(R.id.event_description) protected TextView description;
    @ViewById(R.id.event_user_photo) protected ImageView userPhoto;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        if (getArguments().containsKey(ARG_EVENT)) {
            event = (Event) getArguments().getSerializable(ARG_EVENT);
        }
    }

    @AfterViews public void setContent() {
        if (event != null) {
            duration.setText(String.valueOf(event.duration));
            date.setText(event.dateAndTime);
            spots.setText(String.valueOf(event.spots));
            description.setText(event.description);
            Picasso.with(activity).load(event.user.photo_url).into(userPhoto);
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
        String token = storage.getToken();
        new JoinEventTask(new JoinEventExecutor() {
            @Override public void onSuccess() {
                Toast.makeText(activity, "Joined event", Toast.LENGTH_LONG).show();
            }

            @Override public void onFailure(Exception e) {
                Toast.makeText(activity, "Failed to join event: " + e, Toast.LENGTH_LONG).show();
            }
        }).execute(token, eventId);
    }
}
