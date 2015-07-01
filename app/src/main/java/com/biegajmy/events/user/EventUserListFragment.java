package com.biegajmy.events.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.events.EventListAdapter;
import com.biegajmy.events.EventUpdateActivity_;
import com.biegajmy.events.EventUpdateFragment;
import com.biegajmy.model.Event;
import com.biegajmy.task.ListUserEventExecutor;
import com.biegajmy.task.ListUserEventTask;
import java.util.List;

public class EventUserListFragment extends ListFragment {

    private EventListAdapter adapter;
    private LocalStorage storage;
    private List<Event> events;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new LocalStorage(getActivity());
        adapter = new EventListAdapter(getActivity());
        loadData();
    }

    private void loadData() {

        new ListUserEventTask(new ListUserEventExecutor() {
            @Override public void onSuccess(List<Event> events) {
                EventUserListFragment.this.events = events;
                adapter.setData(events);
                setListAdapter(adapter);
            }

            @Override public void onFailure(Exception e) {
                Toast.makeText(EventUserListFragment.this.getActivity(), "Exception: " + e,
                    Toast.LENGTH_LONG).show();
            }
        }).execute(storage.getUser().id, storage.getToken());
    }

    @Override public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Intent it = new Intent(getActivity(), EventUpdateActivity_.class);
        it.putExtra(EventUpdateFragment.ARG_EVENT, events.get(position));
        startActivity(it);
    }
}
