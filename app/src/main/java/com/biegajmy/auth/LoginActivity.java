package com.biegajmy.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.biegajmy.R;
import com.biegajmy.events.EventMainActivity;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login) public class LoginActivity extends AppCompatActivity {

    public static final int AUTH_OK = 456;
    public static final int AUTH_FAILED = 789;

    @ViewById(R.id.registration_button) protected Button registrationButton;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.facebook_auth_container, com.biegajmy.auth.facebook.LoginFragment_.builder().build())
                .add(R.id.password_auth_container, com.biegajmy.auth.password.LoginFragment_.builder().build())
                .commit();
        }
    }

    @Click(R.id.registration_button) public void register() {
        Fragment fragment = com.biegajmy.auth.password.RegistrationFragment_.builder().build();
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.password_auth_container, fragment)
            .addToBackStack(null)
            .commit();
        registrationButton.setVisibility(View.GONE);
    }

    @OptionsItem(android.R.id.home) public void backHome() {
        NavUtils.navigateUpTo(this, new Intent(this, EventMainActivity.class));
    }

    @Override public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            getSupportFragmentManager().popBackStack();
            registrationButton.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
