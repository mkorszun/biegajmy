package com.biegajmy.events.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import com.biegajmy.R;
import com.biegajmy.events.EventMainActivity;
import com.biegajmy.model.Event;
import java.io.Serializable;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;

@EActivity(R.layout.activity_event_detail) public class EventDetailActivity extends ActionBarActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            Serializable event = getIntent().getSerializableExtra(EventDetailFragment.ARG_EVENT);
            arguments.putSerializable(EventDetailFragment.ARG_EVENT, event);

            supportActionBar.setTitle(((Event) event).headline);
            EventDetailFragment_ fragment = new EventDetailFragment_();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_container, fragment).commit();
        }
    }

    @OptionsItem(android.R.id.home) public void backHome() {
        NavUtils.navigateUpTo(this, new Intent(this, EventMainActivity.class));
    }
}