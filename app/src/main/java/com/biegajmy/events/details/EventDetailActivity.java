package com.biegajmy.events.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.events.EventMainActivity;
import com.biegajmy.events.form.update.EventUpdateActivity_;
import com.biegajmy.events.form.update.EventUpdateFragment;
import com.biegajmy.model.Event;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;

@EActivity(R.layout.activity_event_detail) @OptionsMenu(R.menu.menu_event_detail) public class EventDetailActivity
    extends AppCompatActivity {

    private static final String EVENT_ARG = "EVENT_ARG";

    @OptionsMenuItem(R.id.action_edit_event) protected MenuItem edit;
    @OptionsMenuItem(R.id.action_delete_event) protected MenuItem delete;

    private Event event;
    private boolean owner;
    @Bean protected LocalStorage storage;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            this.event = (Event) getIntent().getSerializableExtra(EventDetailFragment.ARG_EVENT);
            this.owner = event.user.equals(storage.getUser());
        }

        supportActionBar.setTitle(event.headline);
        Fragment fragment = EventDetailFragment_.builder().arg(EventDetailFragment.ARG_EVENT, event).build();
        getSupportFragmentManager().beginTransaction().add(R.id.event_detail_container, fragment).commit();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EVENT_ARG, event);
        super.onSaveInstanceState(outState);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.event = (Event) savedInstanceState.getSerializable(EVENT_ARG);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        edit.setVisible(owner);
        edit.setEnabled(owner);
        delete.setVisible(owner);
        delete.setEnabled(owner);
        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem(android.R.id.home) public void backHome() {
        NavUtils.navigateUpTo(this, new Intent(this, EventMainActivity.class));
    }

    @OptionsItem(R.id.action_edit_event) public void edit() {
        EventUpdateActivity_.intent(this).extra(EventUpdateFragment.ARG_EVENT, event).start();
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
