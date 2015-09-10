package com.biegajmy.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.events.EventMainActivity;
import com.biegajmy.model.User;
import com.biegajmy.utils.SystemUtils;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import java.io.File;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_user_details) @OptionsMenu(R.menu.menu_user_details) public class UserDetailsActivity
    extends ActionBarActivity implements MaterialDialog.ListCallback {

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 1;
    private static final String IMAGE = "image/*";

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

    @Click(R.id.userPhoto) public void selectPicture() {
        new MaterialDialog.Builder(this).items(R.array.photo_sources).itemsCallback(this).show();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String path = SystemUtils.getPath(this, selectedImageUri);
                UserBackendService_.intent(this).scalePhotoFromPath(path).start();
            }

            if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                UserBackendService_.intent(this).scalePhotoFromBitmap(photo).start();
            }
        }
    }

    @Override public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
        switch (i) {
            case 0:
                fromGallery();
                break;
            case 1:
                fromCamera();
                break;
        }
    }

    @OptionsItem(android.R.id.home) public void backHome() {
        NavUtils.navigateUpTo(this, new Intent(this, EventMainActivity.class));
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(UserEventBus.UpdateUserEventOk event) {
        finish();
    }

    @Subscribe public void event(UserEventBus.UpdateUserEventFailed event) {
        Toast.makeText(this, R.string.user_update_failed_msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(UserEventBus.UpdateUserPhotoOk event) {
        Toast.makeText(this, "Zdjęcie zaktualizowane", Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(UserEventBus.UpdateUserPhotoFailed event) {
        Toast.makeText(this, R.string.user_photo_update_failed_msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(UserEventBus.ScalePhotoOK event) {
        UserBackendService_.intent(this).updatePhoto(event.path).start();
        Uri uri = Uri.fromFile(new File(event.path));
        Picasso.with(this).load(uri).into(userPhoto);
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void fromGallery() {
        Intent intent = new Intent();
        intent.setType(IMAGE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, ""), SELECT_PICTURE);
    }

    private void fromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
