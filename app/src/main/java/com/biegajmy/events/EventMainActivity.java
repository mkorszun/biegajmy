package com.biegajmy.events;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.ServiceManager;
import com.biegajmy.auth.LoginActivity;
import com.biegajmy.auth.LoginDialog;
import com.biegajmy.events.form.create.EventNewActivity_;
import com.biegajmy.events.search.EventSearchMainFragment_;
import com.biegajmy.events.user.EventUserListMainFragment_;
import com.biegajmy.general.SlidingTabLayout;
import com.biegajmy.general.ViewPagerAdapter;
import com.biegajmy.user.UserDetailsMainFragment_;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import static java.util.Arrays.asList;

@EActivity(R.layout.activity_event_list) public class EventMainActivity extends AppCompatActivity
    implements ViewPager.OnPageChangeListener {

    private static final String[] OPTIONS = new String[] { "Szukaj", "Dodaj", "Profil" };

    @ViewById(R.id.event_main_pager) protected ViewPager pager;
    @ViewById(R.id.event_main_tabs) protected SlidingTabLayout tabs;
    @ViewById(R.id.event_add) protected FloatingActionButton fab;

    @Bean protected LoginDialog loginDialog;
    @Bean protected LocalStorage localStorage;
    @Bean protected ServiceManager serviceManager;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @AfterViews @UiThread public void initialize() {
        FragmentManager fm = getSupportFragmentManager();
        pager.setAdapter(new ViewPagerAdapter(fm, OPTIONS, getFragments()));
        pager.setOffscreenPageLimit(3);
        pager.addOnPageChangeListener(this);

        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(null);
        tabs.setCustomTabView(R.layout.tab_view_layout, R.id.tab_custom_layout);
        tabs.setViewPager(pager);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        tabs.setViewPager(null);
        pager.addOnPageChangeListener(null);
        serviceManager.stop();
    }

    @Click(R.id.event_add) public void newEvent() {
        if (localStorage.hasToken()) {
            startActivity(new Intent(this, EventNewActivity_.class));
        } else {
            loginDialog.actionConfirmation(R.string.auth_required_create, LoginDialog.CREATE_EVENT_REQUEST);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == LoginActivity.AUTH_OK && requestCode == LoginDialog.CREATE_EVENT_REQUEST) {
            startActivity(new Intent(this, EventNewActivity_.class));
        }

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        fab.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
    }

    @Override public void onPageSelected(int position) {

    }

    @Override public void onPageScrollStateChanged(int state) {

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
