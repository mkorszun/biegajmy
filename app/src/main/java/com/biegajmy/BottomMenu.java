package com.biegajmy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class BottomMenu extends LinearLayout {

    private LayoutInflater inflater;
    private LinearLayout layout;

    public BottomMenu(Context context) {
        super(context);
        inflate(context);
    }

    public BottomMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context);
    }

    private void inflate(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (LinearLayout) inflater.inflate(R.layout.bottom_menu_layout, this, true);
    }
}
