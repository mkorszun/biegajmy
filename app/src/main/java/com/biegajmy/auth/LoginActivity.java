package com.biegajmy.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import com.biegajmy.R;
import com.biegajmy.events.EventMainActivity;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;

@EActivity(R.layout.activity_login) public class LoginActivity extends AppCompatActivity {

    public static final int AUTH_OK = 456;
    public static final int AUTH_FAILED = 789;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.facebook_auth_container, com.biegajmy.auth.facebook.LoginFragment_.builder().build())
                .add(R.id.password_auth_container, com.biegajmy.auth.password.RegistrationFragment_.builder().build())
                .commit();
        }
    }

    @OptionsItem(android.R.id.home) @Override public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            NavUtils.navigateUpTo(this, new Intent(this, EventMainActivity.class));
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
