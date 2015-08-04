package com.biegajmy.events.participants;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.biegajmy.R;
import com.squareup.picasso.Picasso;

public class EventParticipantsLayout extends LinearLayout {

    public EventParticipantsLayout(Context context, String photoURL) {
        super(context, null);
        View rootView = View.inflate(context, R.layout.participant_view, this);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.user_photo);
        Picasso.with(context).load(photoURL).into(imageView);
    }
}
