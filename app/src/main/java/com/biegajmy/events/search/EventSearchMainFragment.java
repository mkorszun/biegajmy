package com.biegajmy.events.search;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import com.biegajmy.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_event_list_main) public class EventSearchMainFragment extends Fragment
    implements DrawerLayout.DrawerListener {

    @ViewById(R.id.drawer_layout) protected DrawerLayout drawerLayout;

    @Override public void onDestroy() {
        super.onDestroy();
        drawerLayout.setDrawerListener(null);
    }

    @AfterViews public void setContent() {
        drawerLayout.setDrawerListener(this);
    }

    @Override public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override public void onDrawerOpened(View drawerView) {

    }

    @Override public void onDrawerClosed(View drawerView) {

    }

    @Override public void onDrawerStateChanged(int newState) {

    }

    @Click(R.id.event_search_filter_button) public void settings() {
        if (drawerLayout.isDrawerOpen(Gravity.END)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            drawerLayout.openDrawer(Gravity.END);
        }
    }
}
