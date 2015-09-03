package com.biegajmy.events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.general.RefreshableListFragment;
import com.biegajmy.location.LastLocation;
import com.biegajmy.model.Event;
import com.biegajmy.task.ListEventExecutor;
import com.biegajmy.task.ListEventTask;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import java.util.List;

import static com.biegajmy.events.EventDetailFragment.ARG_EVENT;

public class EventListFragment extends RefreshableListFragment implements SwipeRefreshLayout.OnRefreshListener {

    private int lastRange = 5000;
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

        bus.register(this);
        setListAdapter(adapter);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setFastScrollEnabled(true);
        setListShown(true);
        loadData(lastRange);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setOnRefreshListener(this);
    }

    @Override public void onResume() {
        super.onResume();
        loadData(lastRange);
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
        loadData(lastRange);
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(EventRange range) {
        loadData(lastRange = range.getMax());
    }

    @Subscribe public void event(Event event) {
        adapter.update(event);
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void loadData(int max) {
        LastLocation pos = storage.getLastLocation();

        new ListEventTask(new ListEventExecutor() {
            @Override public void onSuccess(List<Event> events) {
                setRefreshing(false);
                adapter.setData(events);
            }

            @Override public void onFailure(Exception e) {
                setRefreshing(false);
                Toast.makeText(activity, activity.getResources().getString(R.string.event_search_error_msg),
                    Toast.LENGTH_LONG).show();
            }
        }).execute(storage.getToken().token, pos.lat, pos.lng, max);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
