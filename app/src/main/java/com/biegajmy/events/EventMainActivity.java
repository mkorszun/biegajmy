package com.biegajmy.events;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.biegajmy.R;
import com.biegajmy.events.search.EventSearchMainFragment_;
import com.biegajmy.events.user.EventUserListMainFragment_;
import com.biegajmy.general.SlidingTabLayout;
import com.biegajmy.general.ViewPagerAdapter;
import com.biegajmy.location.LocationService_;
import com.biegajmy.user.UserDetailsMainFragment_;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static java.util.Arrays.asList;

@EActivity(R.layout.activity_event_list) public class EventMainActivity extends AppCompatActivity {

    private static final String[] OPTIONS = new String[] { "Szukaj", "Dodaj", "Profil" };
    @ViewById(R.id.event_main_pager) protected ViewPager pager;
    @ViewById(R.id.event_main_tabs) protected SlidingTabLayout tabs;

    @AfterViews public void initialize() {
        FragmentManager fm = getSupportFragmentManager();
        pager.setAdapter(new ViewPagerAdapter(fm, OPTIONS, getFragments()));
        pager.setOffscreenPageLimit(3);

        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(null);
        tabs.setCustomTabView(R.layout.tab_view_layout, R.id.tab_custom_layout);
        tabs.setViewPager(pager);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        tabs.setViewPager(null);

        LocationService_.intent(getApplication()).stop();
    }

    private List<Fragment> getFragments() {
        return asList(new Fragment[] {
            new EventSearchMainFragment_(), new EventUserListMainFragment_(), new UserDetailsMainFragment_()
        });
    }
}
