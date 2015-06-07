package com.biegajmy.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.SeekBar;
import com.biegajmy.R;
import com.biegajmy.user.UserDetailsActivity_;
import com.biegajmy.model.Event;
import com.squareup.otto.Bus;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import static com.biegajmy.events.EventDetailFragment.ARG_EVENT;

@EActivity(R.layout.activity_event_list) @OptionsMenu(R.menu.menu_event_list)
public class EventListActivity extends ActionBarActivity implements EventListFragment.Callbacks,
    SeekBar.OnSeekBarChangeListener {

    private boolean mTwoPane;
    private Bus bus = EventListBus.getInstance();

    @ViewById(R.id.event_detail_container) protected View container;
    @ViewById(R.id.seekBar) protected SeekBar seekBar;

    @AfterViews public void setContent() {
        if (container != null) {
            mTwoPane = true;
            ((EventListFragment) getSupportFragmentManager().findFragmentById(
                R.id.event_list)).setActivateOnItemClick(true);
        }

        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override public void onItemSelected(Event event) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(ARG_EVENT, event);

            EventDetailFragment_ fragment = new EventDetailFragment_();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_detail_container, fragment)
                .commit();
        } else {
            Intent detailIntent = new Intent(this, EventDetailActivity_.class);
            detailIntent.putExtra(ARG_EVENT, event);
            startActivity(detailIntent);
        }
    }

    @OptionsItem(R.id.action_create_event) void createEvent() {
        startActivity(new Intent(this, EventNewActivity_.class));
    }

    @OptionsItem(R.id.action_user_details) void editUser() {
        startActivity(new Intent(this, UserDetailsActivity_.class));
    }

    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //bus.post(new EventRange(progress));
    }

    @Override public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override public void onStopTrackingTouch(SeekBar seekBar) {
        bus.post(new EventRange(seekBar.getProgress()));
    }
}
