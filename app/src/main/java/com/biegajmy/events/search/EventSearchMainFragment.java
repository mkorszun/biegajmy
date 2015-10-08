package com.biegajmy.events.search;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.auth.LoginActivity;
import com.biegajmy.auth.LoginDialog;
import com.biegajmy.events.form.create.EventNewActivity_;
import com.biegajmy.utils.SystemUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_event_list_main) @OptionsMenu(R.menu.menu_search) public class EventSearchMainFragment
    extends Fragment implements DrawerLayout.DrawerListener {

    @Bean protected LocalStorage localStorage;
    @Bean protected LoginDialog loginDialog;

    @ViewById(R.id.drawer_layout) protected DrawerLayout drawerLayout;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

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
        SystemUtils.hideKeyboard(getActivity());
        FragmentManager childFragmentManager = getChildFragmentManager();
        EventSearchSettingsFragment fr =
            (EventSearchSettingsFragment) childFragmentManager.findFragmentById(R.id.event_search_settings);
        fr.setClearMode();
    }

    @Override public void onDrawerStateChanged(int newState) {

    }

    @OptionsItem(R.id.action_search_settings) public void settings() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    @Click(R.id.event_add) public void newEvent() {
        if (localStorage.hasToken()) {
            startActivity(new Intent(getActivity(), EventNewActivity_.class));
        } else {
            loginDialog.actionConfirmation(R.string.auth_required_create);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == LoginActivity.AUTH_OK) {
            startActivity(new Intent(getActivity(), EventNewActivity_.class));
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
