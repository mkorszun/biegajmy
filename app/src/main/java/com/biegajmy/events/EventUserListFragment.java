package com.biegajmy.events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.general.RefreshableListFragment;
import com.biegajmy.model.Event;
import com.biegajmy.task.DeleteEventExecutor;
import com.biegajmy.task.DeleteEventTask;
import com.biegajmy.task.ListUserEventExecutor;
import com.biegajmy.task.ListUserEventTask;
import java.util.List;

import static com.biegajmy.events.EventDetailFragment.ARG_EVENT;

public class EventUserListFragment extends RefreshableListFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = EventUserListFragment.class.getName();

    int positionSelected;
    private Activity activity;
    private LocalStorage storage;
    private EventListAdapter adapter;
    private android.view.ActionMode actionMode;

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new LocalStorage(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = getActivity();
        adapter = new EventListAdapter(activity);

        setListAdapter(adapter);
        loadData();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override public void onResume() {
        super.onResume();
        loadData();
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setOnRefreshListener(this);
        registerLongClick();
    }

    @Override public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Event event = adapter.get(position);

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
    // Helpers
    //********************************************************************************************//

    private ActionMode.Callback ACTION_MODE_CALLBACK = new ActionMode.Callback() {

        private int statusBarColor;

        @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_event_user_list, menu);
            return true;
        }

        @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete_event:
                    deleteEvent(positionSelected);
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };

    private void loadData() {
        new ListUserEventTask(new ListUserEventExecutor() {
            @Override public void onSuccess(List<Event> events) {
                adapter.setData(events);
                setRefreshing(false);
            }

            @Override public void onFailure(Exception e) {
                Log.e(TAG, "Failed to list user events", e);
                String msg = getResources().getString(R.string.event_error_msg);
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                setRefreshing(false);
            }
        }).execute(storage.getToken().id, storage.getToken().token);
    }

    private void deleteEvent(final int position) {
        new DeleteEventTask(new DeleteEventExecutor() {
            @Override public void onSuccess() {
                Toast.makeText(activity, "Event deleted", Toast.LENGTH_SHORT).show();
                adapter.delete(position);
            }

            @Override public void onFailure(Exception e) {
                Log.e(TAG, "Failed to delete event", e);
                String msg = getResources().getString(R.string.event_error_msg);
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        }).execute(adapter.get(position).id, storage.getToken().token);
    }

    private void registerLongClick() {
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (actionMode != null) {
                    return false;
                }

                actionMode = activity.startActionMode(ACTION_MODE_CALLBACK);
                positionSelected = position;
                view.setSelected(true);
                setSelection(position);
                return true;
            }
        });
    }

    @Override public void onRefresh() {
        loadData();
    }
    //********************************************************************************************//
    //********************************************************************************************//
}
