package com.biegajmy.events.form.create;

import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.events.EventBackendService_;
import com.biegajmy.events.EventListBus;
import com.biegajmy.events.EventPace;
import com.biegajmy.events.form.EventFormFragment;
import com.biegajmy.model.NewEvent;
import com.biegajmy.utils.StringUtils;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_event_form) public class EventNewFragment extends EventFormFragment {

    @Bean protected LocalStorage storage;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected String getModelKey() {
        return "";
    }

    @Override public LatLng location() {
        return storage.getLastLocation().get();
    }

    @Override public void afterViews() {
        EventListBus.getInstance().register(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventListBus.getInstance().unregister(this);
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
        event.pace = eventPace.getPace();
        EventBackendService_.intent(getActivity()).createEvent(event).start();
    }

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    @Override public ArrayList<String> setTags() {
        return new ArrayList();
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(EventListBus.EventCreateOK event) {
        Toast.makeText(getActivity(), R.string.event_created, Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    @Subscribe public void event(EventListBus.EventCreateNOK event) {
        Toast.makeText(getActivity(), R.string.event_create_failed, Toast.LENGTH_LONG).show();
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
