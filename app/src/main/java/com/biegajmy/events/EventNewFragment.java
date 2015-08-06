package com.biegajmy.events;

import android.app.Activity;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.model.NewEvent;
import com.biegajmy.task.CreateEventExecutor;
import com.biegajmy.task.CreateEventTask;
import com.google.android.gms.maps.model.LatLng;
import java.util.LinkedList;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import static java.util.Arrays.asList;

@EFragment(R.layout.fragment_event_form) public class EventNewFragment extends EventFormFragment {

    @Bean LocalStorage storage;

    @Override public LatLng location() {
        return storage.getLastLocation().get();
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
        final Activity activity = getActivity();

        new CreateEventTask(new CreateEventExecutor() {
            @Override public void onSuccess() {
                Toast.makeText(activity, "Event saved", Toast.LENGTH_LONG).show();
                activity.finish();
            }

            @Override public void onFailure(Exception e) {
                Toast.makeText(activity, "Event creation failed: " + e, Toast.LENGTH_LONG).show();
            }
        }).execute(storage.getToken().token, event);
    }
}
