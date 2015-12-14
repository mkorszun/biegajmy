package com.biegajmy.events.user;

import android.app.Activity;
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
import com.biegajmy.gcm.UserMessageBus;
import com.biegajmy.general.RefreshableListFragment;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import static com.biegajmy.events.details.EventDetailFragment.ARG_EVENT;

@EFragment public class EventUserListFragment extends RefreshableListFragment
    implements SwipeRefreshLayout.OnRefreshListener {

    private Activity activity;
    private LocalStorage storage;
    private EventListAdapter adapter;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        storage = new LocalStorage(getActivity());
        adapter = new EventListAdapter(activity, storage.getMessages());

        adapter.registerForMessages();
        adapter.setUser(storage.getUser());

        EventListBus.getInstance().register(this);
        setListAdapter(adapter);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setFastScrollEnabled(true);
        getListView().setDrawSelectorOnTop(true);
        setListShown(true);
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
        if (storage.hasToken()) {
            EventBackendService_.intent(getActivity()).listUserEvents().start();
        } else {
            setRefreshing(false);
        }
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventListBus.getInstance().unregister(this);
    }

    @Override public void onListItemClick(ListView listView, View view, int position, long id) {
        EventDetailActivity_.intent(activity).extra(ARG_EVENT, adapter.get(position)).start();
        UserMessageBus.getInstance().post(new UserMessageBus.RemoveMessages(adapter.get(position).id));
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe @UiThread public void event(EventListBus.ListUserEventsOK event) {
        adapter.setData(event.events);
        setEmpty(event.events.isEmpty());
        setRefreshing(false);
    }

    @Subscribe @UiThread public void event(EventListBus.ListUserEventsNOK event) {
        Toast.makeText(getActivity(), R.string.event_error_msg, Toast.LENGTH_LONG).show();
        setRefreshing(false);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
