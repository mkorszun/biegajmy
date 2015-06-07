package com.biegajmy;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.biegajmy.events.EventListActivity;
import com.biegajmy.model.User;
import com.biegajmy.task.CreateUserExecutor;
import com.biegajmy.task.CreateUserTask;
import com.google.common.base.Joiner;
import com.squareup.picasso.Picasso;
import java.util.LinkedList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import static java.util.Arrays.asList;

@EActivity(R.layout.activity_user_details) public class UserDetailsActivity
    extends ActionBarActivity {

    @Bean LocalStorage storage;
    @ViewById(R.id.userPhoto) ImageView userPhoto;
    @ViewById(R.id.name) TextView name;
    @ViewById(R.id.age) TextView age;
    @ViewById(R.id.location) TextView location;
    @ViewById(R.id.bio) EditText bio;
    @ViewById(R.id.tags) EditText tags;

    private User user;

    @AfterViews void setContent() {
        user = storage.getUser();
        Picasso.with(this).load(user.photo_url).into(userPhoto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name.setText(user.firstName + " " + user.lastName);
        age.setText(String.valueOf(user.age));
        location.setText(user.location);
        bio.setText(user.bio);
        tags.setText(Joiner.on(" ").join(user.tags));
    }

    @Click(R.id.update) void update() {
        user.setBio(bio.getText().toString());
        user.setTags(new LinkedList(asList(tags.getText().toString().split(" "))));
        saveUser(storage.getToken(), user);
    }

    @OptionsItem(android.R.id.home) public void backHome() {
        NavUtils.navigateUpTo(this, new Intent(this, EventListActivity.class));
    }

    private void saveUser(String token, final User user) {
        new CreateUserTask(new CreateUserExecutor() {
            @Override public void onSuccess(String id) {
                user.id = id;
                storage.updateUser(user);
                Toast.makeText(UserDetailsActivity.this, "User updated", Toast.LENGTH_LONG).show();
            }

            @Override public void onFailure(Exception e) {
                Toast.makeText(UserDetailsActivity.this, "User update failed: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            }
        }).execute(token, user);
    }
}
