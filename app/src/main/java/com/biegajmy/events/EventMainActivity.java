package com.biegajmy.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.biegajmy.BottomMenu;
import com.biegajmy.R;
import com.biegajmy.backend.UserBackendService_;
import com.biegajmy.location.LocationService_;
import com.biegajmy.tags.TagEditListFragment_;
import com.biegajmy.user.UserDetailsActivity_;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_event_list) @OptionsMenu(R.menu.menu_event_list)
public class EventMainActivity extends ActionBarActivity implements BottomMenu.BottomMenuListener {

    @ViewById(R.id.bottom_menu) BottomMenu menu;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventListMainFragment fragment = new EventListMainFragment_();
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        LocationService_.intent(getApplication()).stop();
        UserBackendService_.intent(getApplication()).stop();
    }

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
        EventUserListMainFragment_ fragment = new EventUserListMainFragment_();
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit();
    }

    @Override public void onUserTags() {
        TagEditListFragment_ fragment = new TagEditListFragment_();
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit();
    }

    @OptionsItem(R.id.action_user_details) void editUser() {
        startActivity(new Intent(this, UserDetailsActivity_.class));
    }
}
