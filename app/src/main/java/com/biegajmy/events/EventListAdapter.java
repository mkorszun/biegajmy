package com.biegajmy.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.biegajmy.R;
import com.biegajmy.model.Event;
import com.squareup.picasso.Picasso;
import java.util.List;

public class EventListAdapter extends ArrayAdapter<Event> {

    private final LayoutInflater inflater;
    private Context context;
    private List<Event> events;

    public EventListAdapter(Context context) {
        this(context, R.layout.event_list_item);
    }

    public EventListAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView == null) {
            view = inflater.inflate(R.layout.event_list_item, parent, false);
        } else {
            view = convertView;
        }

        Event item = getItem(position);
        ((TextView) view.findViewById(R.id.event_headline)).setText(item.headline);
        ((TextView) view.findViewById(R.id.event_date)).setText(item.dateAndTime);

        ImageView userPhoto = ((ImageView) view.findViewById(R.id.user_photo));
        Picasso.with(context).load(item.user.photo_url).into(userPhoto);

        return view;
    }

    public void setData(List<Event> events) {
        clear();
        addAll(events);
        this.events = events;
    }

    public void update(Event event) {
        events.set(events.indexOf(event), event);
        notifyDataSetChanged();
    }
}
