package com.biegajmy.events.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import com.biegajmy.BuildConfig;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.events.EventListBus;
import com.biegajmy.events.EventMainActivity;
import com.biegajmy.events.form.update.EventUpdateActivity_;
import com.biegajmy.events.form.update.EventUpdateFragment;
import com.biegajmy.general.ShareActivity;
import com.biegajmy.model.Event;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;

@EActivity(R.layout.activity_event_detail) @OptionsMenu(R.menu.menu_event_detail) public class EventDetailActivity
    extends ShareActivity {

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
        EventListBus.getInstance().register(this);

        if (savedInstanceState == null) {
            Event basicEvent = (Event) getIntent().getSerializableExtra(EventDetailFragment.ARG_EVENT);
            Event fullEvent = storage.get(basicEvent.id, Event.class);
            this.event = fullEvent != null ? fullEvent : basicEvent;
            this.owner = event.user.equals(storage.getUser()) && storage.hasToken();
        }

        setTitle(event.headline);
        setDescription(event.description);
        setURL(BuildConfig.EVENT_PAGE_URL + event.id);

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
        return super.onCreateOptionsMenu(menu);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        EventListBus.getInstance().unregister(this);
    }

    @OptionsItem(android.R.id.home) public void backHome() {
        NavUtils.navigateUpTo(this, new Intent(this, EventMainActivity.class));
    }

    @OptionsItem(R.id.action_edit_event) public void edit() {
        EventUpdateActivity_.intent(this).extra(EventUpdateFragment.ARG_EVENT, event).start();
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(EventListBus.EventUpdateOK event) {
        setContent(this.event = event.event);
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void setContent(Event event) {
        setTitle(event.headline);
        setDescription(event.description);
        setURL(BuildConfig.EVENT_PAGE_URL + event.id);
        getSupportActionBar().setTitle(event.headline);

        Fragment fragment;
        FragmentManager fm = getSupportFragmentManager();

        if ((fragment = fm.findFragmentById(R.id.event_detail_container)) != null) {
            ((EventDetailFragment) fragment).setContent(event);
        } else {
            fragment = EventDetailFragment_.builder().arg(EventDetailFragment.ARG_EVENT, event).build();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_container, fragment).commit();
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
