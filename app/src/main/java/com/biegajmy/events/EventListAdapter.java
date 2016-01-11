package com.biegajmy.events;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.biegajmy.R;
import com.biegajmy.gcm.MessageType;
import com.biegajmy.gcm.UserMessageBus;
import com.biegajmy.model.Event;
import com.biegajmy.model.User;
import com.biegajmy.utils.StringUtils;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventListAdapter extends ArrayAdapter<Event> {

    private static final String TAG = EventListAdapter.class.getName();

    private User user;
    private List<Event> events;
    private boolean messagesEnabled;
    private LayoutInflater inflater;
    private Map<String, Integer> labels = new HashMap<>();
    private Map<String, Set<MessageType>> messages;

    public EventListAdapter(Context context) {
        this(context, R.layout.event_list_item, new ArrayList<Event>(), new HashMap<String, Set<MessageType>>());
    }

    public EventListAdapter(Context context, Map<String, Set<MessageType>> messages) {
        this(context, R.layout.event_list_item, new ArrayList<Event>(), messages);
    }

    public EventListAdapter(Context context, int resource, List<Event> events, Map<String, Set<MessageType>> messages) {
        super(context, resource, events);
        this.events = events;
        this.messages = messages;
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
        ((TextView) view.findViewById(R.id.event_distance)).setText(StringUtils.doubleToString(item.distance) + " km");

        TextView spots = (TextView) view.findViewById(R.id.spots);
        spots.setVisibility(item.spots > 3 ? View.VISIBLE : View.GONE);
        if (item.spots > 3) spots.setText(String.valueOf(item.spots));

        int spotsImg = item.spots == 2 ? R.drawable.two_runners : R.drawable.three_runners;
        int participantsImg = item.spots <= 1 ? R.drawable.one_runner : spotsImg;
        int ownerImg = item.user.equals(user) ? R.drawable.my_event : (item.official ? R.drawable.official_event : 0);

        if (messagesEnabled) {
            view.findViewById(R.id.message).setVisibility(hasMessage(item.id) ? View.VISIBLE : View.GONE);
            view.findViewById(R.id.alert).setVisibility(hasAlert(item.id) ? View.VISIBLE : View.GONE);
        }

        ((ImageView) view.findViewById(R.id.event_list_item_image)).setImageResource(participantsImg);
        ImageView eventIndicator = (ImageView) view.findViewById(R.id.event_indicator);
        eventIndicator.setVisibility(ownerImg != 0 ? View.VISIBLE : View.GONE);
        if (ownerImg != 0) eventIndicator.setImageResource(ownerImg);

        Log.v(TAG, String.format("Position %d date %s", position, dateTime.getDate().toString()));
        setLabel(position, view, dateTime.getDate().toLongString());

        return view;
    }

    //********************************************************************************************//
    // Public API
    //********************************************************************************************//

    public void setData(List<Event> events) {
        clear();
        addAll(events);
        prepareLabels();
    }

    public Event get(int id) {
        return events.get(id);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void registerForMessages() {
        this.messagesEnabled = true;
        UserMessageBus.getInstance().register(this);
    }

    @Subscribe public void event(UserMessageBus.UpdateMessages event) {
        Log.d(TAG, "Refreshing list view on new message");
        messages.put(event.id, event.types);
        notifyDataSetChanged();
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
            String label = dateTime.getDate().toLongString();
            if (!labels.containsKey(label)) labels.put(label, i);
        }
    }

    private boolean hasMessage(String id) {
        Set<MessageType> types = messages.get(id);
        if (types == null || types.size() == 0) return false;
        return types.contains(MessageType.new_comment);
    }

    private boolean hasAlert(String id) {
        Set<MessageType> types = messages.get(id);
        if (types == null || types.size() == 0) return false;
        return types.contains(MessageType.new_participant)
            || types.contains(MessageType.leaving_participant)
            || types.contains(MessageType.event_updated);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
