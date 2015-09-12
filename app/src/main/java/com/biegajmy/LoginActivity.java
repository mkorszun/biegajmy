package com.biegajmy;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.biegajmy.location.LocationService_;
import com.biegajmy.tags.TagBackendService_;
import com.splunk.mint.Mint;

public class LoginActivity extends ActionBarActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LocationService_.intent(getApplication()).start();
        TagBackendService_.intent(this).getTagRecommendations().start();
        TagBackendService_.intent(this).getPopularTags().start();
        Mint.initAndStartSession(this, BuildConfig.MINT_TOKEN);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new LoginFragment_()).commit();
        }
    }
}
