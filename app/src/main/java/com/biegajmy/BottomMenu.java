package com.biegajmy;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;

@EViewGroup(R.layout.bottom_menu_layout) public class BottomMenu extends LinearLayout {

    private BottomMenuListener listener;

    public interface BottomMenuListener {
        void onAllEvents();

        void onUserEvents();

        void onUserTags();
    }

    public BottomMenu(Context context) {
        super(context);
    }

    public BottomMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Click(R.id.all_events) public void allEvents() {
        listener.onAllEvents();
    }

    @Click(R.id.user_events) public void userEvents() {
        listener.onUserEvents();
    }

    @Click(R.id.user_tags) public void userTags() {
        listener.onUserTags();
    }

    public void setListener(BottomMenuListener listener) {
        this.listener = listener;
    }
}
