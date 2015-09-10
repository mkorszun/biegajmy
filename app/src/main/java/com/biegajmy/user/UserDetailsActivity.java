package com.biegajmy.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.events.EventMainActivity;
import com.biegajmy.model.User;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_user_details) @OptionsMenu(R.menu.menu_user_details) public class UserDetailsActivity
    extends ActionBarActivity {

    @Bean LocalStorage storage;
    @ViewById(R.id.userPhoto) ImageView userPhoto;
    @ViewById(R.id.firstname) TextView firstName;
    @ViewById(R.id.lastname) TextView lastName;
    @ViewById(R.id.bio) EditText bio;
    @ViewById(R.id.telephone) EditText telephone;
    @ViewById(R.id.www) EditText www;
    @ViewById(R.id.email) EditText email;

    private User user;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected void onCreate(Bundle savedInstanceState) {
        user = storage.getUser();
        super.onCreate(savedInstanceState);
        UserEventBus.getInstance().register(this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        UserEventBus.getInstance().unregister(this);
    }

    @AfterViews void setContent() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Picasso.with(this).load(user.photo_url).into(userPhoto);

        firstName.setText(user.firstName);
        lastName.setText(user.lastName);
        bio.setText(user.bio);
        telephone.setText(user.telephone);
        www.setText(user.www);
        email.setText(user.email);
    }

    @OptionsItem(R.id.action_user_save) void update() {
        user.firstName = firstName.getText().toString();
        user.lastName = lastName.getText().toString();
        user.bio = bio.getText().toString();
        user.telephone = telephone.getText().toString();
        user.www = www.getText().toString();
        user.email = email.getText().toString();
        UserBackendService_.intent(this).updateUser(user).start();
    }

    @OptionsItem(android.R.id.home) public void backHome() {
        NavUtils.navigateUpTo(this, new Intent(this, EventMainActivity.class));
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void onUpdateSuccess(UserEventBus.UpdateUserEventOk event) {
        finish();
    }

    @Subscribe public void onUpdateFailed(UserEventBus.UpdateUserEventFailed event) {
        Toast.makeText(this, R.string.user_update_failed_msg, Toast.LENGTH_LONG).show();
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
