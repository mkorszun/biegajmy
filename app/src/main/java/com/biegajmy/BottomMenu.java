package com.biegajmy;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.biegajmy.events.user.EventUserListActivity_;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;

@EViewGroup(R.layout.bottom_menu_layout) public class BottomMenu extends LinearLayout {

    private Context context;

    public BottomMenu(Context context) {
        super(context);
        this.context = context;
    }

    public BottomMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Click(R.id.all_events) public void allEvents() {
        Toast.makeText(context, "Display all events", Toast.LENGTH_LONG).show();
        context.startActivity(new Intent(context, EventUserListActivity_.class));
    }

    @Click(R.id.user_events) public void userEvents() {
        Toast.makeText(context, "Display user events", Toast.LENGTH_LONG).show();
        context.startActivity(new Intent(context, EventUserListActivity_.class));
    }

    @Click(R.id.user_tags) public void userTags() {
        Toast.makeText(context, "Display user tags", Toast.LENGTH_LONG).show();
        context.startActivity(new Intent(context, EventUserListActivity_.class));
    }
}
