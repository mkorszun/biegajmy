package com.biegajmy.events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.location.LastLocation;
import com.biegajmy.model.Event;
import com.biegajmy.task.ListEventExecutor;
import com.biegajmy.task.ListEventTask;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import java.util.List;

import static com.biegajmy.events.EventDetailFragment.ARG_EVENT;

public class EventListFragment extends ListFragment {

    private int lastRange;
    private Activity activity;
    private EventListAdapter adapter;
    private LocalStorage storage;
    private Bus bus = EventListBus.getInstance();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);

        activity = getActivity();
        storage = new LocalStorage(activity);
        adapter = new EventListAdapter(activity);
    }

    @Override public void onResume() {
        super.onResume();
        loadData(lastRange);
    }

    @Override public void onListItemClick(ListView listView, View view, int position, long id) {
        Intent detailIntent = new Intent(activity, EventDetailActivity_.class);
        detailIntent.putExtra(ARG_EVENT, adapter.get(position));
        startActivity(detailIntent);
    }

    @Subscribe public void reloadEvents(EventRange range) {
        lastRange = range.getMax();
        loadData(range.getMax());
    }

    @Subscribe public void updateEvent(Event event) {
        adapter.update(event);
    }

    private void loadData(int max) {
        LastLocation pos = storage.getLastLocation();

        new ListEventTask(new ListEventExecutor() {
            @Override public void onSuccess(List<Event> events) {
                adapter.setData(events);
                setListAdapter(adapter);
            }

            @Override public void onFailure(Exception e) {
                Toast.makeText(activity, "Exception: " + e, Toast.LENGTH_LONG).show();
            }
        }).execute(storage.getToken2().token, pos.lat, pos.lng, max);
    }
}
