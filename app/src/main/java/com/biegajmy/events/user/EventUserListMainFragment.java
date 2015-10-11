package com.biegajmy.events.user;

import android.content.Intent;
import android.support.v4.app.Fragment;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.auth.LoginActivity;
import com.biegajmy.auth.LoginDialog;
import com.biegajmy.events.form.create.EventNewActivity_;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_user_event_list_main) public class EventUserListMainFragment extends Fragment {

    @Bean LoginDialog loginDialog;
    @Bean LocalStorage localStorage;

    @Click(R.id.event_add) public void newEvent() {
        if (localStorage.hasToken()) {
            startActivity(new Intent(getActivity(), EventNewActivity_.class));
        } else {
            loginDialog.actionConfirmation(R.string.auth_required_create, LoginDialog.CREATE_EVENT_REQUEST);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == LoginActivity.AUTH_OK && requestCode == LoginDialog.CREATE_EVENT_REQUEST) {
            startActivity(new Intent(getActivity(), EventNewActivity_.class));
        }
    }
}
