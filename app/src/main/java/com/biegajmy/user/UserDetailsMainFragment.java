package com.biegajmy.user;

import android.content.Context;
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
import org.androidannotations.annotations.UiThread;

@EFragment(R.layout.fragment_user_details_main) @OptionsMenu(R.menu.menu_user_details)
public class UserDetailsMainFragment extends Fragment implements UserDetailsChangeListener {

    @Bean protected LocalStorage storage;
    @OptionsMenuItem(R.id.action_user_save) protected MenuItem save;

    private User user;
    private Context context;
    private int hashCode = -1;
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

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @AfterInject @UiThread public void setup() {
        setContent();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fr = getChildFragment();
        if (fr != null) fr.onActivityResult(requestCode, resultCode, data);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        boolean visible = storage.hasToken() && user != null;
        save.setVisible(visible && hashCode != user.hashCode());
    }

    @OptionsItem(R.id.action_user_save) public void save() {
        Fragment fr = getChildFragment();
        if (fr != null && fr instanceof UserDetailsFragment) {
            ((UserDetailsFragment) fr).update();
        }
    }

    @Override public void onChange(User user) {
        if (save != null) save.setVisible(hashCode != (this.user = user).hashCode());
    }

    @Override public void onUpdate(User user) {
        hashCode = user.hashCode();
        onChange(user);
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(UserEventBus.SyncUserEventOK event) {
        updateUserDetails(event.user);
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private Fragment getChildFragment() {
        return getChildFragmentManager().findFragmentById(R.id.user_details_container);
    }

    private void setContent() {
        if (storage.hasToken() && (user = storage.getUser()) != null) {
            UserBackendService_.intent(context).syncUser().start();
            updateUserDetails(user);
        } else {
            Fragment emptyDetails = UserDetailsEmptyFragment_.builder().build();
            getChildFragmentManager().beginTransaction()
                .replace(R.id.user_details_container, emptyDetails)
                .commitAllowingStateLoss();
        }
    }

    private void updateUserDetails(User user) {
        hashCode = user.hashCode();

        UserDetailsFragment userDetails =
            UserDetailsFragment_.builder().arg(UserDetailsFragment.USER_ARG, user).build();
        userDetails.setUserDetailsChangeListener(this);

        getChildFragmentManager().beginTransaction()
            .replace(R.id.user_details_container, userDetails)
            .commitAllowingStateLoss();
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
