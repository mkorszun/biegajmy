package com.biegajmy.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.model.User;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;

@EFragment(R.layout.fragment_user_details_main) @OptionsMenu(R.menu.menu_user_details)
public class UserDetailsMainFragment extends Fragment {

    @Bean protected LocalStorage storage;
    @OptionsMenuItem(R.id.action_user_save) protected MenuItem save;
    private Bus userEventBus = UserEventBus.getInstance();

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userEventBus.register(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        userEventBus.unregister(this);
    }

    @AfterInject public void setup() {
        setContent();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fr = getChildFragment();
        if (fr != null) fr.onActivityResult(requestCode, resultCode, data);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        save.setVisible(storage.hasToken() && storage.getUser() != null);
    }

    @OptionsItem(R.id.action_user_save) public void save() {
        Fragment fr = getChildFragment();
        if (fr != null && fr instanceof UserDetailsFragment) {
            ((UserDetailsFragment) fr).update();
        }
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(UserEventBus.SyncUserEventOK event) {
        User user = storage.getUser();
        Fragment fr = getChildFragment();
        if (fr != null && fr instanceof UserDetailsFragment) {
            ((UserDetailsFragment) fr).updateUser(storage.getUser());
        } else {
            Fragment userDetails = UserDetailsFragment_.builder().arg(UserDetailsFragment.USER_ARG, user).build();
            getChildFragmentManager().beginTransaction()
                .replace(R.id.user_details_container, userDetails)
                .commitAllowingStateLoss();
        }
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private Fragment getChildFragment() {
        return getChildFragmentManager().findFragmentById(R.id.user_details_container);
    }

    private void setContent() {
        User user;
        if (storage.hasToken() && (user = storage.getUser()) != null) {
            Fragment userDetails = UserDetailsFragment_.builder().arg(UserDetailsFragment.USER_ARG, user).build();
            getChildFragmentManager().beginTransaction().replace(R.id.user_details_container, userDetails).commit();
        } else {
            Fragment emptyDetails = UserDetailsEmptyFragment_.builder().build();
            getChildFragmentManager().beginTransaction().replace(R.id.user_details_container, emptyDetails).commit();
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
