package com.biegajmy.events.search;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import com.biegajmy.R;
import com.biegajmy.events.EventMainActivity;
import com.biegajmy.utils.SystemUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_event_list_main) @OptionsMenu(R.menu.menu_search) public class EventSearchMainFragment
    extends Fragment implements DrawerLayout.DrawerListener {

    @ViewById(R.id.drawer_layout) protected DrawerLayout drawerLayout;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @AfterViews public void setContent() {
        drawerLayout.setDrawerListener(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        drawerLayout.setDrawerListener(null);
    }

    @Override public void onDrawerClosed(View drawerView) {
        SystemUtils.hideKeyboard(getActivity());
        FragmentManager childFragmentManager = getChildFragmentManager();
        EventSearchSettingsFragment fr =
            (EventSearchSettingsFragment) childFragmentManager.findFragmentById(R.id.event_search_settings);
        fr.setClearMode();
    }

    @OptionsItem(R.id.action_search_settings) public void settings() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
            ((EventMainActivity) getActivity()).enableFAB(true);
        } else {
            drawerLayout.openDrawer(Gravity.RIGHT);
            ((EventMainActivity) getActivity()).enableFAB(false);
        }
    }

    @Override public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override public void onDrawerOpened(View drawerView) {

    }

    @Override public void onDrawerStateChanged(int newState) {

    }

    //********************************************************************************************//
    //********************************************************************************************//
}
