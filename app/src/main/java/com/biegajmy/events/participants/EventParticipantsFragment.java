package com.biegajmy.events.participants;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import com.biegajmy.events.EventListBus;
import com.biegajmy.model.Event;
import com.biegajmy.model.User;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;

public class EventParticipantsFragment extends ListFragment {

    private static final String ARG_EVENT_ID = "ARG_EVENT_ID";
    private static final String ARG_PARTICIPANTS = "ARG_PARTICIPANTS";

    private String eventId;
    private ArrayList<User> participants;
    private EventParticipantsListAdapter adapter;

    public static EventParticipantsFragment newInstance(String id, ArrayList<User> participants) {
        EventParticipantsFragment fragment = new EventParticipantsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARTICIPANTS, participants);
        args.putString(ARG_EVENT_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventListBus.getInstance().register(this);

        eventId = getArguments().getString(ARG_EVENT_ID);
        participants = (ArrayList<User>) getArguments().getSerializable(ARG_PARTICIPANTS);
        adapter = new EventParticipantsListAdapter(getActivity());

        setListAdapter(adapter);
        adapter.setData(participants);
    }

    @Subscribe public void eventUpdated(Event event) {
        if (eventId.equals(event.id)) {
            participants = event.participants;
            adapter.setData(participants);
        }
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventListBus.getInstance().unregister(this);
    }
}
