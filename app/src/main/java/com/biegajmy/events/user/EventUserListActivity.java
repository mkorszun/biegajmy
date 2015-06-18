package com.biegajmy.events.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.SeekBar;
import com.biegajmy.R;
import com.biegajmy.model.Event;
import com.biegajmy.user.UserDetailsActivity_;
import com.squareup.otto.Bus;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import static com.biegajmy.events.EventDetailFragment.ARG_EVENT;

@EActivity(R.layout.activity_user_event_list) @OptionsMenu(R.menu.menu_event_list)
public class EventUserListActivity extends ActionBarActivity {

}
