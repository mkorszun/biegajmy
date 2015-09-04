package com.biegajmy.tags;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.biegajmy.R;

public class TagListElement extends LinearLayout {

    public TagListElement(Context context, String text, int color) {
        super(context, null);

        View rootView = View.inflate(context, R.layout.tag_view_element, this);
        TextView tagView = (TextView) rootView.findViewById(R.id.tag_view);

        tagView.setText(text);
        rootView.setBackgroundColor(color);
    }

    public TagListElement(Context context, String text, int color, OnClickListener listener) {
        super(context, null);

        View rootView = View.inflate(context, R.layout.tag_view_element, this);
        TextView tagView = (TextView) rootView.findViewById(R.id.tag_view);
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.tag_view_layout);

        tagView.setText(text);
        layout.setBackgroundColor(color);

        if (listener != null) {
            rootView.setOnClickListener(listener);
            rootView.findViewById(R.id.delete_tag).setVisibility(View.VISIBLE);
        }
    }
}
