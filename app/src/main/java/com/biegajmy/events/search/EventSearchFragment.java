package com.biegajmy.events.search;

import android.content.Context;
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
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import static com.biegajmy.events.details.EventDetailFragment.ARG_EVENT;

@EFragment public class EventSearchFragment extends RefreshableListFragment
    implements SwipeRefreshLayout.OnRefreshListener {

    private String lastTag = "";
    private int lastRange = 5000;

    private Context context;
    private EventListAdapter adapter;
    private Bus bus = EventListBus.getInstance();

    @Bean protected LocalStorage storage;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @AfterInject public void setup() {
        adapter = new EventListAdapter(this.context);
        adapter.setUser(storage.getUser());
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

    @Override @UiThread public void onListItemClick(ListView listView, View view, int position, long id) {
        Intent detailIntent = new Intent(this.context, EventDetailActivity_.class);
        detailIntent.putExtra(ARG_EVENT, adapter.get(position));
        startActivity(detailIntent);
    }

    @Override public void onRefresh() {
        loadData(lastRange, lastTag);
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe @UiThread public void event(EventSearchRange range) {
        loadData(lastRange = range.getMax(), lastTag = range.getTags());
    }

    @Subscribe @UiThread public void event(EventListBus.SearchEventsOK event) {
        setRefreshing(false);
        setEmptyPlaceholder(event.events.isEmpty());
        adapter.setData(event.events);
    }

    @Subscribe @UiThread public void event(EventListBus.SearchEventsNOK event) {
        setRefreshing(false);
        Toast.makeText(this.context, R.string.event_search_error_msg, Toast.LENGTH_LONG).show();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    @UiThread protected void loadData(int max, String tag) {
        setRefreshing(true);
        adapter.setUser(storage.getUser());
        LastLocation pos = storage.getLastLocation();
        EventBackendService_.intent(this.context).searchEvents(pos.lat, pos.lng, max, tag).start();
    }

    private void setEmptyPlaceholder(boolean empty) {
        setEmpty(empty);
        ((EventSearchMainFragment) getParentFragment()).onEmpty(empty);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
