package com.biegajmy.events.participants;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import com.biegajmy.model.User;
import java.util.ArrayList;

public class EventParticipantsFragment extends ListFragment {

    private static final String ARG_PARTICIPANTS = "ARG_PARTICIPANTS";
    private ArrayList<User> participants;

    public static EventParticipantsFragment newInstance(ArrayList<User> participants) {
        EventParticipantsFragment fragment = new EventParticipantsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARTICIPANTS, participants);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        participants = (ArrayList<User>) getArguments().getSerializable(ARG_PARTICIPANTS);
        EventParticipantsListAdapter adapter = new EventParticipantsListAdapter(getActivity());

        setListAdapter(adapter);
        adapter.setData(participants);
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}
