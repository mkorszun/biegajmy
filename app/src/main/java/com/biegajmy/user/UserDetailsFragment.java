package com.biegajmy.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.biegajmy.LocalStorage;
import com.biegajmy.R;
import com.biegajmy.model.User;
import com.biegajmy.utils.SystemUtils;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import java.io.File;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_user_details) @OptionsMenu(R.menu.menu_user_details) public class UserDetailsFragment
    extends Fragment implements MaterialDialog.ListCallback {

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

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserEventBus.getInstance().register(this);
    }

    @Override public void onResume() {
        super.onResume();
        user = storage.getUser();
        setContent();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        UserEventBus.getInstance().unregister(this);
    }

    @AfterViews void setContent() {
        if (user != null) {
            Picasso.with(getActivity()).load(user.photo_url).into(userPhoto);

            firstName.setText(user.firstName);
            lastName.setText(user.lastName);
            bio.setText(user.bio);
            telephone.setText(user.telephone);
            www.setText(user.www);
            email.setText(user.email);
        }
    }

    @OptionsItem(R.id.action_user_save) void update() {
        if (user != null) {
            user.firstName = firstName.getText().toString();
            user.lastName = lastName.getText().toString();
            user.bio = bio.getText().toString();
            user.telephone = telephone.getText().toString();
            user.www = www.getText().toString();
            user.email = email.getText().toString();
            UserBackendService_.intent(getActivity()).updateUser(user).start();
        }
    }

    @Click(R.id.userPhoto) public void selectPicture() {
        if (user != null) {
            new MaterialDialog.Builder(getActivity()).items(R.array.photo_sources).itemsCallback(this).show();
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String path = SystemUtils.getPath(getActivity(), selectedImageUri);
                UserBackendService_.intent(getActivity()).scalePhotoFromPath(path).start();
            }

            if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                UserBackendService_.intent(getActivity()).scalePhotoFromBitmap(photo).start();
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

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(UserEventBus.UpdateUserEventOk event) {
        Toast.makeText(getActivity(), R.string.user_update_ok_msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(UserEventBus.UpdateUserEventFailed event) {
        Toast.makeText(getActivity(), R.string.user_update_failed_msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(UserEventBus.UpdateUserPhotoOk event) {
        Toast.makeText(getActivity(), R.string.user_photo_update_ok_msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(UserEventBus.UpdateUserPhotoFailed event) {
        Toast.makeText(getActivity(), R.string.user_photo_update_failed_msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(UserEventBus.ScalePhotoOK event) {
        UserBackendService_.intent(getActivity()).updatePhoto(event.path).start();
        Uri uri = Uri.fromFile(new File(event.path));
        Picasso.with(getActivity()).load(uri).into(userPhoto);
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
