package com.biegajmy.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.biegajmy.R;
import com.biegajmy.model.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventListAdapter extends ArrayAdapter<Event> {

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
        sort(comparator);
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
        setLabel(position, view, dateTime.getDate().toString());

        return view;
    }

    //********************************************************************************************//
    // Public API
    //********************************************************************************************//

    public void setData(List<Event> events) {
        clear();
        labels.clear();
        addAll(events);
        notifyDataSetChanged();
        sort(comparator);
    }

    public void update(Event event) {
        events.set(events.indexOf(event), event);
        notifyDataSetChanged();
    }

    public void delete(int id) {
        events.remove(id);
        notifyDataSetChanged();
    }

    public Event get(int id) {
        return events.get(id);
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
        if (!labels.containsKey(label)) {
            ((TextView) view.findViewById(R.id.event_list_label)).setText(label);
            view.findViewById(R.id.event_list_label).setVisibility(View.VISIBLE);
            labels.put(label, position);
        }

        if (labels.get(label) == position) {
            ((TextView) view.findViewById(R.id.event_list_label)).setText(label);
            view.findViewById(R.id.event_list_label).setVisibility(View.VISIBLE);
        } else {
            ((TextView) view.findViewById(R.id.event_list_label)).setText("");
            view.findViewById(R.id.event_list_label).setVisibility(View.GONE);
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
