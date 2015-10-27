package com.biegajmy.events.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.events.EventBackendService_;
import com.biegajmy.events.EventListAdapter;
import com.biegajmy.events.EventListBus;
import com.biegajmy.events.details.EventDetailActivity_;
import com.biegajmy.general.RefreshableListFragment;
import com.biegajmy.location.LastLocation;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import static com.biegajmy.events.details.EventDetailFragment.ARG_EVENT;

public class EventSearchFragment extends RefreshableListFragment implements SwipeRefreshLayout.OnRefreshListener {

    private int lastRange = 5000;
    private String lastTag = "";
    private Activity activity;
    private EventListAdapter adapter;
    private LocalStorage storage;
    private Bus bus = EventListBus.getInstance();

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        storage = new LocalStorage(activity);
        adapter = new EventListAdapter(activity);
        adapter.setUser(storage.getUser());

        bus.register(this);
        setListAdapter(adapter);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setFastScrollEnabled(true);
        getListView().setDrawSelectorOnTop(true);
        setListShown(true);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setOnRefreshListener(this);
    }

    @Override public void onResume() {
        super.onResume();
        loadData(lastRange, lastTag);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        adapter.clear();

        setOnRefreshListener(null);
        setListAdapter(null);
    }

    @Override public void onListItemClick(ListView listView, View view, int position, long id) {
        Intent detailIntent = new Intent(activity, EventDetailActivity_.class);
        detailIntent.putExtra(ARG_EVENT, adapter.get(position));
        startActivity(detailIntent);
    }

    @Override public void onRefresh() {
        loadData(lastRange, lastTag);
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(EventSearchRange range) {
        loadData(lastRange = range.getMax(), lastTag = range.getTags());
    }

    @Subscribe public void event(EventListBus.SearchEventsOK event) {
        setRefreshing(false);
        adapter.setData(event.events);
    }

    @Subscribe public void event(EventListBus.SearchEventsNOK event) {
        setRefreshing(false);
        Toast.makeText(getActivity(), R.string.event_search_error_msg, Toast.LENGTH_LONG).show();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void loadData(int max, String tag) {
        LastLocation pos = storage.getLastLocation();
        EventBackendService_.intent(getActivity()).searchEvents(pos.lat, pos.lng, max, tag).start();
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
