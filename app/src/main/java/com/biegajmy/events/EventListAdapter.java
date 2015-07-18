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
import java.util.List;

public class EventListAdapter extends ArrayAdapter<Event> {

    private List<Event> events;
    private LayoutInflater inflater;

    public EventListAdapter(Context context) {
        this(context, R.layout.event_list_item, new ArrayList<Event>());
    }

    public EventListAdapter(Context context, int resource, List<Event> events) {
        super(context, resource, events);
        this.events = events;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {

        Event item = getItem(position);
        View view = convertView == null ? inflate(getContext(), parent) : convertView;
        ((TextView) view.findViewById(R.id.event_headline)).setText(item.headline);
        ((TextView) view.findViewById(R.id.event_date)).setText(item.dateAndTime);
        ((TextView) view.findViewById(R.id.event_distance)).setText("20km");

        return view;
    }

    public void setData(List<Event> events) {
        clear();
        addAll(events);
        notifyDataSetChanged();
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

    private View inflate(Context context, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        return inflater.inflate(R.layout.event_list_item, parent, false);
    }
}
