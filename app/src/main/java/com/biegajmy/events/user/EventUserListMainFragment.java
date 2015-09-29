package com.biegajmy.events.user;

import android.content.Intent;
import android.support.v4.app.Fragment;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.auth.LoginActivity;
import com.biegajmy.auth.LoginDialog;
import com.biegajmy.events.form.create.EventNewActivity_;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

@EFragment(R.layout.fragment_user_event_list_main) @OptionsMenu(R.menu.menu_add) public class EventUserListMainFragment
    extends Fragment {

    @Bean LoginDialog loginDialog;
    @Bean LocalStorage localStorage;

    @OptionsItem(R.id.action_add_event) public void newEvent() {
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
}
