package com.biegajmy.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.biegajmy.R;
import com.biegajmy.model.User;
import com.biegajmy.model.UserSettings;
import com.biegajmy.utils.SystemUtils;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import java.io.File;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_user_details) public class UserDetailsFragment extends Fragment
    implements MaterialDialog.ListCallback, View.OnTouchListener {

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 1;
    private static final String IMAGE = "image/*";

    public static final String USER_ARG = "USER_ARG";

    @ViewById(R.id.user_form) View mainView;
    @ViewById(R.id.userPhoto) ImageView userPhoto;
    @ViewById(R.id.firstname) TextView firstName;
    @ViewById(R.id.lastname) TextView lastName;
    @ViewById(R.id.bio) EditText bio;
    @ViewById(R.id.telephone) EditText telephone;
    @ViewById(R.id.www) EditText www;
    @ViewById(R.id.email) EditText email;

    @ViewById(R.id.new_comment_setting) protected SwitchCompat newCommentSetting;
    @ViewById(R.id.new_participant_setting) protected SwitchCompat newParticipantSetting;
    @ViewById(R.id.leaving_participant_setting) protected SwitchCompat leavingParticipantSetting;
    @ViewById(R.id.event_updated_setting) protected SwitchCompat eventUpdatedSetting;

    private User user;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserEventBus.getInstance().register(this);
        user = (User) getArguments().getSerializable(USER_ARG);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        UserEventBus.getInstance().unregister(this);
        mainView.setOnTouchListener(null);
    }

    @AfterViews void setContent() {
        Picasso.with(getActivity()).load(user.photo_url).into(userPhoto);

        firstName.setText(user.firstName);
        lastName.setText(user.lastName);
        bio.setText(user.bio);
        telephone.setText(user.telephone);
        www.setText(user.www);
        email.setText(user.email);
        mainView.setOnTouchListener(this);
        setSettings(user.settings);
    }

    @Override public boolean onTouch(View v, MotionEvent event) {
        SystemUtils.hideKeyboard(getActivity());
        return false;
    }

    @Click(R.id.userPhoto) public void selectPicture() {
        new MaterialDialog.Builder(getActivity()).items(R.array.photo_sources).itemsCallback(this).show();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                UserBackendService_.intent(getActivity()).scalePhotoFromPath(selectedImageUri).start();
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
    // API
    //********************************************************************************************//

    public void updateUser(User user) {
        this.user = user;
        setContent();
    }

    public void update() {
        user.firstName = firstName.getText().toString();
        user.lastName = lastName.getText().toString();
        user.bio = bio.getText().toString();
        user.telephone = telephone.getText().toString();
        user.www = www.getText().toString();
        user.email = email.getText().toString();
        user.settings.onNewComment = newCommentSetting.isChecked();
        user.settings.onNewParticipant = newParticipantSetting.isChecked();
        user.settings.onUpdate = eventUpdatedSetting.isChecked();
        user.settings.onLeavingParticipant = leavingParticipantSetting.isChecked();
        UserBackendService_.intent(getActivity()).updateUser(user).start();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void fromGallery() {
        Intent intent = new Intent();
        intent.setType(IMAGE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getParentFragment().startActivityForResult(Intent.createChooser(intent, ""), SELECT_PICTURE);
    }

    private void fromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        getParentFragment().startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void setSettings(UserSettings settings) {
        newCommentSetting.setChecked(settings.onNewComment);
        newParticipantSetting.setChecked(settings.onNewParticipant);
        eventUpdatedSetting.setChecked(settings.onUpdate);
        leavingParticipantSetting.setChecked(settings.onLeavingParticipant);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
