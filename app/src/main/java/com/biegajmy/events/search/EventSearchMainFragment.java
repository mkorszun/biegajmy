package com.biegajmy.events.search;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.biegajmy.R;
import com.biegajmy.events.EventMainActivity;
import com.biegajmy.general.ModelFragment;
import com.biegajmy.utils.SystemUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_event_list_main) @OptionsMenu(R.menu.menu_search) public class EventSearchMainFragment
    extends ModelFragment<Boolean> implements DrawerLayout.DrawerListener {

    @ViewById(R.id.drawer_layout) protected DrawerLayout drawerLayout;
    @ViewById(R.id.empty_list_label) protected TextView emptyLabel;
    @OptionsMenuItem(R.id.action_search_settings) protected MenuItem search;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected String getModelKey() {
        return "IS_OPEN_ARG";
    }

    @AfterViews public void setContent() {
        drawerLayout.setDrawerListener(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        drawerLayout.setDrawerListener(null);
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

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        boolean isOpen = model != null && model;
        if (search != null) search.setTitle(isOpen ? R.string.search_close : R.string.search);
        ((EventMainActivity) getActivity()).enableFAB(!isOpen);
    }

    @Override public void onDrawerClosed(View drawerView) {
        SystemUtils.hideKeyboard(getActivity());
        FragmentManager childFragmentManager = getChildFragmentManager();
        Fragment fr = childFragmentManager.findFragmentById(R.id.event_search_settings);

        ((EventSearchSettingsFragment) fr).setClearMode();
        ((EventMainActivity) getActivity()).enableFAB(true);

        model = false;
        search.setTitle(R.string.search);
    }

    @Override public void onDrawerOpened(View drawerView) {
        model = true;
        search.setTitle(R.string.search_close);
    }

    @Override public void onDrawerSlide(View drawerView, float slideOffset) {
        //Not used
    }

    @Override public void onDrawerStateChanged(int newState) {
        //Not used
    }

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    public void onEmpty(boolean empty) {
        emptyLabel.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
