package com.biegajmy.events.user;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.events.EventListAdapter;
import com.biegajmy.model.Event;
import com.biegajmy.task.ListUserEventExecutor;
import com.biegajmy.task.ListUserEventTask;
import java.util.List;

public class EventUserListFragment extends ListFragment {

    private EventListAdapter adapter;
    private LocalStorage storage;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new LocalStorage(getActivity());
        adapter = new EventListAdapter(getActivity());
        loadData();
    }

    private void loadData() {

        new ListUserEventTask(new ListUserEventExecutor() {
            @Override public void onSuccess(List<Event> events) {
                adapter.setData(events);
                setListAdapter(adapter);
            }

            @Override public void onFailure(Exception e) {
                Toast.makeText(EventUserListFragment.this.getActivity(), "Exception: " + e,
                    Toast.LENGTH_LONG).show();
            }
        }).execute(storage.getUser().id, storage.getToken());
    }
}
