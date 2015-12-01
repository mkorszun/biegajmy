package com.biegajmy.tags;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.biegajmy.R;

public class TagListElement extends LinearLayout {

    public TagListElement(Context context, String text, OnClickListener listener, boolean editable) {
        super(context, null);

        View rootView = View.inflate(context, R.layout.tag_view_element, this);
        TextView tagView = (TextView) rootView.findViewById(R.id.tag_view);

        tagView.setText(text);

        if (listener != null) {
            rootView.setOnClickListener(listener);
            if (editable) rootView.findViewById(R.id.delete_tag).setVisibility(View.VISIBLE);
        }
    }
}
