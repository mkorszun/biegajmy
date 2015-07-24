package com.biegajmy;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.biegajmy.backend.UserBackendService_;
import com.biegajmy.location.LocationService_;

public class LoginActivity extends ActionBarActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LocationService_.intent(getApplication()).start();
        UserBackendService_.intent(getApplication()).start();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new LoginFragment())
                .commit();
        }
    }
}
