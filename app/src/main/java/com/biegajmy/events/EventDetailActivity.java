package com.biegajmy.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import com.biegajmy.R;
import java.io.Serializable;
import org.androidannotations.annotations.EActivity;

import static com.biegajmy.events.EventDetailFragment.ARG_EVENT;

@EActivity(R.layout.activity_event_detail) public class EventDetailActivity
    extends ActionBarActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            Serializable event = getIntent().getSerializableExtra(ARG_EVENT);
            arguments.putSerializable(ARG_EVENT, event);

            EventDetailFragment_ fragment = new EventDetailFragment_();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                .add(R.id.event_detail_container, fragment)
                .commit();
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, EventListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
