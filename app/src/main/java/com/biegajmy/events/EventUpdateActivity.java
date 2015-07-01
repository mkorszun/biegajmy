package com.biegajmy.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import com.biegajmy.R;
import com.biegajmy.events.user.EventUserListActivity;
import com.biegajmy.model.Event;
import java.io.Serializable;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;

import static com.biegajmy.events.EventUpdateFragment.ARG_EVENT;

@EActivity(R.layout.activity_event_update) public class EventUpdateActivity
    extends ActionBarActivity {

    @OptionsItem(android.R.id.home) public void backHome() {
        NavUtils.navigateUpTo(this, new Intent(this, EventUserListActivity.class));
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            Event event = (Event)getIntent().getSerializableExtra(ARG_EVENT);
            arguments.putSerializable(ARG_EVENT, event);

            supportActionBar.setTitle(((Event) event).headline);
            EventUpdateFragment_ fragment = new EventUpdateFragment_();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                .add(R.id.event_update_container, fragment)
                .commit();
        }
    }
}

