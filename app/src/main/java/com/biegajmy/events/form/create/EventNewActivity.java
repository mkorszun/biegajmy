package com.biegajmy.events.form.create;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import com.biegajmy.R;
import com.biegajmy.events.EventMainActivity;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;

@EActivity(R.layout.activity_event_new) public class EventNewActivity extends ActionBarActivity {
    @OptionsItem(android.R.id.home) public void backHome() {
        NavUtils.navigateUpTo(this, new Intent(this, EventMainActivity.class));
    }
}
