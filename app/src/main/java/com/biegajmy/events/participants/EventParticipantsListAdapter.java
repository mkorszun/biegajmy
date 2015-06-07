package com.biegajmy.events.participants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.biegajmy.R;
import com.biegajmy.model.User;
import com.squareup.picasso.Picasso;
import java.util.List;

public class EventParticipantsListAdapter extends ArrayAdapter<User> {

    private final LayoutInflater inflater;
    private Context context;

    public EventParticipantsListAdapter(Context context) {
        this(context, R.layout.participant_list_item);
    }

    public EventParticipantsListAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView == null) {
            view = inflater.inflate(R.layout.participant_list_item, parent, false);
        } else {
            view = convertView;
        }

        User item = getItem(position);
        ((TextView) view.findViewById(R.id.participant_fist_name)).setText(item.firstName);
        ((TextView) view.findViewById(R.id.participant_last_name)).setText(item.lastName);

        ImageView userPhoto = ((ImageView) view.findViewById(R.id.participant_photo));
        Picasso.with(context).load(item.photo_url).into(userPhoto);

        return view;
    }

    public void setData(List<User> events) {
        clear();
        addAll(events);
    }
}
