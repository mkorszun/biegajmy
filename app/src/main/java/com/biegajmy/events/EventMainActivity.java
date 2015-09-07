package com.biegajmy.events;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import com.biegajmy.R;
import com.biegajmy.backend.UserBackendService_;
import com.biegajmy.events.search.EventSearchMainFragment_;
import com.biegajmy.events.user.EventUserListMainFragment_;
import com.biegajmy.general.SlidingTabLayout;
import com.biegajmy.general.ViewPagerAdapter;
import com.biegajmy.location.LocationService_;
import com.biegajmy.user.UserDetailsActivity_;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import static java.util.Arrays.asList;

@EActivity(R.layout.activity_event_list) @OptionsMenu(R.menu.menu_event_list) public class EventMainActivity
    extends ActionBarActivity {

    private static final String[] OPTIONS = new String[] { "Szukaj", "Dodaj" };
    @ViewById(R.id.event_main_pager) protected ViewPager pager;
    @ViewById(R.id.event_main_tabs) protected SlidingTabLayout tabs;

    @AfterViews public void initialize() {
        FragmentManager fm = getSupportFragmentManager();
        pager.setAdapter(new ViewPagerAdapter(fm, OPTIONS, getFragments()));

        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(null);
        tabs.setCustomTabView(R.layout.tab_view_layout, R.id.tab_custom_layout);
        tabs.setViewPager(pager);
    }

    @Override protected void onDestroy() {
        super.onDestroy();

        LocationService_.intent(getApplication()).stop();
        UserBackendService_.intent(getApplication()).stop();
        tabs.setViewPager(null);
    }

    @OptionsItem(R.id.action_user_details) void editUser() {
        startActivity(new Intent(this, UserDetailsActivity_.class));
    }

    private List<Fragment> getFragments() {
        return asList(new Fragment[] { new EventSearchMainFragment_(), new EventUserListMainFragment_() });
    }
}
