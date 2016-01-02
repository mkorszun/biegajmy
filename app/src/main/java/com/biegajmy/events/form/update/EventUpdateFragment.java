package com.biegajmy.events.form.update;

import android.os.Bundle;
import android.widget.Toast;
import com.biegajmy.R;
import com.biegajmy.events.EventBackendService_;
import com.biegajmy.events.EventListBus;
import com.biegajmy.events.form.EventFormFragment;
import com.biegajmy.model.NewEvent;
import com.biegajmy.utils.StringUtils;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_event_form) public class EventUpdateFragment extends EventFormFragment {

    public static final String ARG_EVENT = "ARG_EVENT";

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected String getModelKey() {
        return ARG_EVENT;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventListBus.getInstance().register(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventListBus.getInstance().unregister(this);
    }

    @Override public void afterViews() {
        eventDateTime.set(model.timestamp);
        headline.setText(model.headline);
        description.setText(model.description);
        date.setText(eventDateTime.getDate().toString());
        time.setText(eventDateTime.getTime().toString());
        distance.setText(String.valueOf(model.distance));
        pace.setText(StringUtils.doubleToString(model.pace));
    }

    @Override public void save() {
        NewEvent event = new NewEvent();
        event.headline = headline.getText().toString();
        event.description = description.getText().toString();
        event.timestamp = eventDateTime.getTimestamp();
        event.tags = getTags();
        event.x = eventMap.getCurrentPosition().latitude;
        event.y = eventMap.getCurrentPosition().longitude;
        event.distance = StringUtils.stringToDouble(distance.getText().toString());
        event.pace = StringUtils.stringToDouble(pace.getText().toString());
        EventBackendService_.intent(getActivity()).updateEvent(this.model.id, event).start();
    }

    @Override public LatLng location() {
        return new LatLng(model.location.coordinates.get(1), model.location.coordinates.get(0));
    }

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    @Override public ArrayList<String> setTags() {
        return new ArrayList(model.tags);
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(EventListBus.EventUpdateOK event) {
        Toast.makeText(getActivity(), R.string.event_updated, Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    @Subscribe public void event(EventListBus.EventUpdateNOK event) {
        Toast.makeText(getActivity(), R.string.event_update_failed, Toast.LENGTH_LONG).show();
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
