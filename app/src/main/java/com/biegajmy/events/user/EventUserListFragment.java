package com.biegajmy.events.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.events.EventBackendService_;
import com.biegajmy.events.EventDetailActivity_;
import com.biegajmy.events.EventListAdapter;
import com.biegajmy.events.EventListBus;
import com.biegajmy.events.form.update.EventUpdateActivity_;
import com.biegajmy.events.form.update.EventUpdateFragment;
import com.biegajmy.general.RefreshableListFragment;
import com.biegajmy.model.Event;
import com.squareup.otto.Subscribe;

import static com.biegajmy.events.EventDetailFragment.ARG_EVENT;

public class EventUserListFragment extends RefreshableListFragment implements SwipeRefreshLayout.OnRefreshListener {

    private Activity activity;
    private LocalStorage storage;
    private EventListAdapter adapter;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new LocalStorage(getActivity());
        EventListBus.getInstance().register(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        adapter = new EventListAdapter(activity);

        setListAdapter(adapter);
        onRefresh();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setOnRefreshListener(this);
    }

    @Override public void onRefresh() {
        EventBackendService_.intent(getActivity()).listUserEvents().start();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventListBus.getInstance().unregister(this);
    }

    @Override public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        actionForEvent(adapter.get(position));
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(EventListBus.ListUserEventsOK event) {
        adapter.setData(event.events);
        setRefreshing(false);
    }

    @Subscribe public void event(EventListBus.ListUserEventsNOK event) {
        Toast.makeText(getActivity(), R.string.event_error_msg, Toast.LENGTH_LONG).show();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void actionForEvent(Event event) {
        if (event.user.equals(storage.getUser())) {
            Intent it = new Intent(activity, EventUpdateActivity_.class);
            it.putExtra(EventUpdateFragment.ARG_EVENT, event);
            startActivity(it);
        } else {
            Intent detailIntent = new Intent(activity, EventDetailActivity_.class);
            detailIntent.putExtra(ARG_EVENT, event);
            startActivity(detailIntent);
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
