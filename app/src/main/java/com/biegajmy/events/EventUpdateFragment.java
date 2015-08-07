package com.biegajmy.events;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.biegajmy.R;
import com.biegajmy.model.Event;
import com.biegajmy.model.NewEvent;
import com.biegajmy.task.UpdateEventExecutor;
import com.biegajmy.task.UpdateEventTask;
import com.biegajmy.utils.StringUtils;
import com.google.android.gms.maps.model.LatLng;
import java.util.LinkedList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import static java.util.Arrays.asList;

@EFragment(R.layout.fragment_event_form) public class EventUpdateFragment extends EventFormFragment {

    public static final String ARG_EVENT = "ARG_EVENT";

    private Event event;
    private Activity activity;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = (Event) getArguments().getSerializable(ARG_EVENT);
    }

    @AfterViews public void setContent() {
        super.setContent();

        activity = getActivity();
        eventDateTime.set(event.dateAndTime);

        headline.setText(event.headline);
        description.setText(event.description);
        date.setText(eventDateTime.getDate().toString());
        time.setText(eventDateTime.getTime().toString());
        tags.setText(StringUtils.join(event.tags, " "));
        distance.setText(String.valueOf(event.distance));
        pace.setText(String.valueOf(event.pace));
    }

    @Override public void save() {
        NewEvent event = new NewEvent();
        event.headline = headline.getText().toString();
        event.description = description.getText().toString();
        event.dateAndTime = eventDateTime.toString();
        event.tags = new LinkedList(asList(tags.getText().toString().split(" ")));
        event.x = eventMap.getCurrentPosition().latitude;
        event.y = eventMap.getCurrentPosition().longitude;
        event.distance = Integer.valueOf(distance.getText().toString());
        event.pace = Double.valueOf(pace.getText().toString());

        new UpdateEventTask(new UpdateEventExecutor() {
            @Override public void onSuccess(Event event) {
                Toast.makeText(activity, "Event updated", Toast.LENGTH_LONG).show();
                activity.finish();
            }

            @Override public void onFailure(Exception e) {
                Toast.makeText(activity, "Event update failed: " + e, Toast.LENGTH_LONG).show();
            }
        }).execute(this.event.id, event, storage.getToken().token);
    }

    @Override public LatLng location() {
        return new LatLng(event.location.coordinates.get(0), event.location.coordinates.get(1));
    }
}
