package com.biegajmy.events.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.biegajmy.BuildConfig;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.auth.LoginActivity;
import com.biegajmy.auth.LoginDialog;
import com.biegajmy.backend.error.ConflictError;
import com.biegajmy.events.EventBackendService_;
import com.biegajmy.events.EventListBus;
import com.biegajmy.events.EventMainActivity;
import com.biegajmy.events.form.update.EventUpdateActivity_;
import com.biegajmy.events.form.update.EventUpdateFragment;
import com.biegajmy.general.ModelActivity;
import com.biegajmy.model.Event;
import com.biegajmy.model.User;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EActivity(R.layout.activity_event_detail) public class EventDetailActivity extends ModelActivity<Event> {

    private boolean owner;
    private boolean isMember;

    @Bean protected LocalStorage storage;
    @Bean protected LoginDialog loginDialog;

    @ViewById(R.id.event_edit) protected Button edit;
    @ViewById(R.id.event_join) protected Button join;

    @StringRes(R.string.share) protected String SHARE;
    @StringRes(R.string.event_join) protected String JOIN_TXT;
    @StringRes(R.string.event_leave) protected String LEAVE_TXT;
    @StringRes(R.string.event_delete) protected String DELETE_TXT;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected String getModelKey() {
        return EventDetailFragment.ARG_EVENT;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        EventListBus.getInstance().register(this);

        if (savedInstanceState == null) {
            Event fullEvent = storage.get(model.id, Event.class);
            this.model = fullEvent != null ? fullEvent : model;
        }
    }

    @AfterViews public void setup() {
        setContent(this.model);
        EventBackendService_.intent(this).getEvent(model.id).start();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == LoginActivity.AUTH_OK && requestCode == LoginDialog.JOIN_EVENT_REQUEST) {
            EventBackendService_.intent(this).joinEvent(model.id, !isMember).start();
            updateActions(model);
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        updateActions(model);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventListBus.getInstance().unregister(this);
    }

    @OptionsItem(android.R.id.home) public void backHome() {
        NavUtils.navigateUpTo(this, new Intent(this, EventMainActivity.class));
    }

    @Click(R.id.event_edit) public void edit() {
        EventUpdateActivity_.intent(this).extra(EventUpdateFragment.ARG_EVENT, model).start();
    }

    @Click(R.id.event_join) public void action() {
        if (storage.hasToken()) {
            if (model.spots == 1 && owner && isMember) {
                EventBackendService_.intent(this).deleteEvent(model.id).start();
            } else {
                EventBackendService_.intent(this).joinEvent(model.id, !isMember).start();
            }
        } else {
            loginDialog.actionConfirmation(R.string.auth_required_join, LoginDialog.JOIN_EVENT_REQUEST);
        }
    }

    @Click(R.id.event_share) public void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE, model.headline);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, model.headline);
        sendIntent.putExtra(Intent.EXTRA_TEXT, BuildConfig.EVENT_PAGE_URL + model.id);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, SHARE));
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(EventListBus.EventUpdateOK event) {
        setContent(this.model = event.event);
    }

    @Subscribe public void event(EventListBus.EventJoinLeaveOK event) {
        setContent(this.model = event.event);
    }

    @Subscribe public void event(EventListBus.EventJoinLeaveNOK event) {
        boolean isConflict = event.exception instanceof ConflictError;
        int resId = isConflict ? R.string.event_join_error_already_participating : R.string.event_error_msg;
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(EventListBus.DeleteEventOK event) {
        finish();
    }

    @Subscribe public void event(EventListBus.DeleteEventNOK event) {
        Toast.makeText(this, R.string.event_error_msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(EventListBus.GetEventDetailsOK event) {
        setContent(this.model = event.event);
    }

    @Subscribe public void event(EventListBus.GetEventDetailsNOK event) {
        Toast.makeText(this, R.string.event_error_msg, Toast.LENGTH_LONG).show();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void setContent(Event event) {
        getSupportActionBar().setTitle(event.headline);
        updateActions(event);

        Fragment fragment;
        FragmentManager fm = getSupportFragmentManager();

        if ((fragment = fm.findFragmentById(R.id.event_detail_container)) != null) {
            ((EventDetailFragment) fragment).setContent(event);
        } else {
            fragment = EventDetailFragment_.builder().arg(EventDetailFragment.ARG_EVENT, event).build();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_container, fragment).commit();
        }
    }

    private void updateActions(Event event) {
        User user = storage.getUser();
        User eventOwner = event.user;
        boolean authenticated = storage.hasToken();

        isMember = event.participants.contains(user);
        owner = eventOwner != null ? eventOwner.equals(user) && authenticated : false;

        join.setText(msgForAction());
        join.setSelected(isMember);

        edit.setVisibility(owner ? View.VISIBLE : View.GONE);
        edit.setEnabled(owner);
    }

    private String msgForAction() {
        return isMember ? (model.spots == 1 && owner ? DELETE_TXT : LEAVE_TXT) : JOIN_TXT;
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
