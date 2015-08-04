package com.biegajmy.events.participants;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.biegajmy.R;
import com.biegajmy.events.EventListBus;
import com.biegajmy.model.Event;
import com.biegajmy.model.User;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.apmem.tools.layouts.FlowLayout;

@EFragment(R.layout.fragment_event_participants) public class EventParticipantsFragment extends Fragment {

    public static final String ARG_EVENT_ID = "ARG_EVENT_ID";
    public static final String ARG_PARTICIPANTS = "ARG_PARTICIPANTS";

    private String eventId;
    @ViewById(R.id.event_participants) FlowLayout flowLayout;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventListBus.getInstance().register(this);
        eventId = getArguments().getString(ARG_EVENT_ID);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventListBus.getInstance().unregister(this);
    }

    @AfterViews public void setup() {
        update((ArrayList<User>) getArguments().getSerializable(ARG_PARTICIPANTS));
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void eventUpdated(Event event) {
        if (eventId.equals(event.id)) update(event.participants);
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void update(ArrayList<User> participants) {
        flowLayout.removeAllViews();
        for (User u : participants) {
            flowLayout.addView(new EventParticipantsLayout(getActivity(), u.photo_url));
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
