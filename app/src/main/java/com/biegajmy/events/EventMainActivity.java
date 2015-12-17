package com.biegajmy.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.ServiceManager;
import com.biegajmy.auth.LoginActivity;
import com.biegajmy.auth.LoginDialog;
import com.biegajmy.events.form.create.EventNewActivity_;
import com.biegajmy.events.search.EventSearchMainFragment_;
import com.biegajmy.events.user.EventUserListMainFragment_;
import com.biegajmy.gcm.UserMessageBus;
import com.biegajmy.general.GenericActivity;
import com.biegajmy.general.ViewPagerAdapter;
import com.biegajmy.user.UserDetailsMainFragment_;
import com.squareup.otto.Subscribe;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import static java.util.Arrays.asList;

@EActivity(R.layout.activity_event_list) public class EventMainActivity extends GenericActivity
    implements ViewPager.OnPageChangeListener {

    @ViewById(R.id.event_main_pager) protected ViewPager pager;
    @ViewById(R.id.event_main_tabs) protected TabLayout tabs;
    @ViewById(R.id.event_add) protected FloatingActionButton fab;
    @ViewById(R.id.event_main_toolbar) protected Toolbar toolbar;

    @Bean protected LoginDialog loginDialog;
    @Bean protected LocalStorage localStorage;
    @Bean protected ServiceManager serviceManager;

    private int myRunsIconResource = R.drawable.my_runs_selector;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserMessageBus.getInstance().register(this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        serviceManager.stop();
        pager.addOnPageChangeListener(null);
        UserMessageBus.getInstance().unregister(this);
    }

    @AfterViews @UiThread public void initialize() {
        FragmentManager fm = getSupportFragmentManager();
        pager.setAdapter(new ViewPagerAdapter(fm, getFragments()));
        pager.setOffscreenPageLimit(3);
        pager.addOnPageChangeListener(this);
        tabs.setupWithViewPager(pager);

        boolean hasMessages = !localStorage.getMessages().isEmpty();
        if (hasMessages) myRunsIconResource = R.drawable.my_runs_selector_notification;

        tabs.getTabAt(0).setIcon(R.drawable.search_selector);
        tabs.getTabAt(1).setIcon(myRunsIconResource);
        tabs.getTabAt(2).setIcon(R.drawable.settings_selector);

        setSupportActionBar(toolbar);
    }

    @Click(R.id.event_add) public void newEvent() {
        if (localStorage.hasToken()) {
            startActivity(new Intent(this, EventNewActivity_.class));
        } else {
            loginDialog.actionConfirmation(LoginDialog.CREATE_EVENT_REQUEST);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == LoginActivity.AUTH_OK && requestCode == LoginDialog.CREATE_EVENT_REQUEST) {
            startActivity(new Intent(this, EventNewActivity_.class));
        }
    }

    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        enableFAB(!(position == 2));

        if (position == 1 && myRunsIconResource != R.drawable.my_runs_selector) {
            tabs.getTabAt(1).setIcon(myRunsIconResource = R.drawable.my_runs_selector);
        }
    }

    @Override public void onPageSelected(int position) {
        //NOT USED
    }

    @Override public void onPageScrollStateChanged(int state) {
        //NOT USED
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(UserMessageBus.NewMessage event) {
        if (myRunsIconResource != R.drawable.my_runs_selector_notification) {
            tabs.getTabAt(1).setIcon(myRunsIconResource = R.drawable.my_runs_selector_notification);
        }
    }

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    public void enableFAB(boolean enable) {
        fab.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private List<Fragment> getFragments() {
        return asList(new Fragment[] {
            new EventSearchMainFragment_(), new EventUserListMainFragment_(), new UserDetailsMainFragment_()
        });
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
