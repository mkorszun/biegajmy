package com.biegajmy.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.biegajmy.R;

public class LoginActivity extends AppCompatActivity {

    public static final int AUTH_OK = 456;
    public static final int AUTH_FAILED = 789;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, LoginFragment_.builder().build())
                .commit();
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
