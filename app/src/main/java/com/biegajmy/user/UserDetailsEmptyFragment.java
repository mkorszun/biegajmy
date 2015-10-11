package com.biegajmy.user;

import android.content.Intent;
import android.support.v4.app.Fragment;
import com.biegajmy.R;
import com.biegajmy.auth.LoginActivity;
import com.biegajmy.auth.LoginDialog;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_user_details_empty) public class UserDetailsEmptyFragment extends Fragment {

    @Click(R.id.create_profile_button) public void createProfile() {
        startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginDialog.CREATE_PROFILE_REQUEST);
    }
}
