package com.biegajmy.events;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.biegajmy.R;
import com.biegajmy.model.Event;
import com.biegajmy.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventListAdapter extends ArrayAdapter<Event> {

    private static final String TAG = EventListAdapter.class.getName();

    private User user;
    private List<Event> events;
    private LayoutInflater inflater;
    private Map<String, Integer> labels = new HashMap<>();
    private EventComparator comparator = new EventComparator();

    public EventListAdapter(Context context) {
        this(context, R.layout.event_list_item, new ArrayList<Event>());
    }

    public EventListAdapter(Context context, int resource, List<Event> events) {
        super(context, resource, events);
        this.events = events;
        setData(this.events);
    }

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public View getView(int position, View convertView, ViewGroup parent) {

        Event item = getItem(position);
        EventDateTime dateTime = new EventDateTime();
        dateTime.set(item.timestamp);

        View view = convertView == null ? inflate(getContext(), parent) : convertView;
        ((TextView) view.findViewById(R.id.event_headline)).setText(item.headline);
        ((TextView) view.findViewById(R.id.event_date)).setText(dateTime.getTime().toString());
        ((TextView) view.findViewById(R.id.event_distance)).setText(item.distance + " km");

        int resId = item.user.equals(user) ? R.color.my_event_color
            : (item.official ? R.color.official_event : R.color.someone_else_event_color);
        view.findViewById(R.id.event_list_item_layout).setBackgroundResource(resId);

        Log.v(TAG, String.format("Position %d date %s", position, dateTime.getDate().toString()));
        setLabel(position, view, dateTime.getDate().toString());

        return view;
    }

    //********************************************************************************************//
    // Public API
    //********************************************************************************************//

    public void setData(List<Event> events) {
        clear();
        addAll(events);
        sort(comparator);
        prepareLabels();
    }

    public Event get(int id) {
        return events.get(id);
    }

    public void setUser(User user) {
        this.user = user;
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private View inflate(Context context, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        return inflater.inflate(R.layout.event_list_item, parent, false);
    }

    private void setLabel(int position, View view, String label) {
        if (labels.get(label) == position) {
            Log.v(TAG, String.format("Position %d date %s label should be here", position, label));
            ((TextView) view.findViewById(R.id.event_list_label)).setText(label);
            view.findViewById(R.id.event_list_label).setVisibility(View.VISIBLE);
        } else {
            Log.v(TAG, String.format("Position %d date %s label should not be here", position, label));
            ((TextView) view.findViewById(R.id.event_list_label)).setText("");
            view.findViewById(R.id.event_list_label).setVisibility(View.GONE);
        }
    }

    private void prepareLabels() {
        labels.clear();
        EventDateTime dateTime = new EventDateTime();
        for (int i = 0; i < events.size(); i++) {
            dateTime.set(events.get(i).timestamp);
            String label = dateTime.getDate().toString();
            if (!labels.containsKey(label)) labels.put(label, i);
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
