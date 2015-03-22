package com.biegajmy;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new LoginFragment())
                .commit();
        }
    }
}
