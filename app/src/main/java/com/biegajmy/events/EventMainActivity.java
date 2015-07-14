package com.biegajmy.events;

import android.support.v7.app.ActionBarActivity;
import com.biegajmy.BottomMenu;
import com.biegajmy.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_event_list) @OptionsMenu(R.menu.menu_event_list)
public class EventMainActivity extends ActionBarActivity implements BottomMenu.BottomMenuListener {

    @ViewById(R.id.bottom_menu) BottomMenu menu;

    @AfterViews public void initialize() {
        menu.setListener(this);
    }

    @Override public void onAllEvents() {
        EventListMainFragment fragment = new EventListMainFragment_();
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit();
    }

    @Override public void onUserEvents() {
        EventUserListFragment fragment = new EventUserListFragment();
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit();
    }

    @Override public void onUserTags() {
        // TODO
    }
}
