package com.biegajmy.events;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.model.Event;
import com.biegajmy.task.ListEventExecutor;
import com.biegajmy.task.ListEventTask;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import java.util.List;

public class EventListFragment extends ListFragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private Callbacks mCallbacks = sDummyCallbacks;
    private List<Event> events;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private EventListAdapter adapter;
    private LocalStorage storage;
    private LocationManager locationManager;
    private Bus bus = EventListBus.getInstance();

    public interface Callbacks {
        public void onItemSelected(Event event);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override public void onItemSelected(Event event) {
        }
    };

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
        storage = new LocalStorage(getActivity());
        adapter = new EventListAdapter(getActivity());
        locationManager =
            (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        loadData(2000);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(
            STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }

    @Override public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onItemSelected(events.get(position));
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Subscribe public void reloadEvents(EventRange range) {
        loadData(range.getMax());
    }

    @Subscribe public void updateEvent(Event event) {
        adapter.update(event);
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(
            activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    private void loadData(int max) {
        Location pos = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        new ListEventTask(new ListEventExecutor() {
            @Override public void onSuccess(List<Event> events) {
                EventListFragment.this.events = events;
                adapter.setData(events);
                setListAdapter(adapter);
            }

            @Override public void onFailure(Exception e) {
                Toast.makeText(EventListFragment.this.getActivity(), "Exception: " + e,
                    Toast.LENGTH_LONG).show();
            }
        }).execute(storage.getToken(), pos.getLatitude(), pos.getLongitude(), max);
    }
}
