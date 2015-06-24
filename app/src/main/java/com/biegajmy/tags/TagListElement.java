package com.biegajmy.tags;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.biegajmy.R;
import com.veinhorn.tagview.TagView;

public class TagListElement extends LinearLayout {

    public TagListElement(Context context, String text, int color) {
        super(context, null);

        View rootView = View.inflate(context, R.layout.tag_view_element, this);
        TagView tagView = (TagView) rootView.findViewById(R.id.tagview);

        tagView.setText(text);
        tagView.setTagColor(color);
    }
}
