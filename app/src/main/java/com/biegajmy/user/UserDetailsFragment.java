package com.biegajmy.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.biegajmy.R;
import com.biegajmy.general.ModelFragment;
import com.biegajmy.model.User;
import com.biegajmy.model.UserSettings;
import com.biegajmy.utils.PhotoUtils;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import java.io.File;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_user_details) public class UserDetailsFragment extends ModelFragment<User>
    implements MaterialDialog.ListCallback {

    public static final String USER_ARG = "USER_ARG";

    @ViewById(R.id.user_photo) protected ImageView userPhoto;
    @ViewById(R.id.firstname) protected TextView firstName;
    @ViewById(R.id.lastname) protected TextView lastName;
    @ViewById(R.id.bio) protected EditText bio;
    @ViewById(R.id.telephone) protected EditText telephone;
    @ViewById(R.id.www) protected EditText www;
    @ViewById(R.id.email) protected EditText email;

    @ViewById(R.id.new_comment_setting) protected SwitchCompat newCommentSetting;
    @ViewById(R.id.new_participant_setting) protected SwitchCompat newParticipantSetting;
    @ViewById(R.id.leaving_participant_setting) protected SwitchCompat leavingParticipantSetting;
    @ViewById(R.id.event_updated_setting) protected SwitchCompat eventUpdatedSetting;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected String getModelKey() {
        return USER_ARG;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserEventBus.getInstance().register(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        UserEventBus.getInstance().unregister(this);
    }

    @AfterViews void setContent() {
        PhotoUtils.set(model.photo_url, getActivity(), userPhoto);
        firstName.setText(model.firstName);
        lastName.setText(model.lastName);
        bio.setText(model.bio);
        telephone.setText(model.telephone);
        www.setText(model.www);
        email.setText(model.email);
        setSettings(model.settings);
    }

    @Click({R.id.user_photo, R.id.user_photo_button}) public void selectPicture() {
        new MaterialDialog.Builder(getActivity()).items(R.array.photo_sources).itemsCallback(this).show();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == PhotoUtils.SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            UserBackendService_.intent(getActivity()).scalePhotoFromPath(selectedImageUri).start();
        }

        if (resultCode == Activity.RESULT_OK && requestCode == PhotoUtils.CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            UserBackendService_.intent(getActivity()).scalePhotoFromBitmap(photo).start();
        }
    }

    @Override public void onSelection(MaterialDialog dialog, View view, int i, CharSequence c) {
        switch (i) {
            case 0:
                PhotoUtils.fromGallery(getParentFragment());
                break;
            case 1:
                PhotoUtils.fromCamera(getParentFragment());
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
        this.model = user;
        setContent();
    }

    public void update() {
        model.firstName = firstName.getText().toString();
        model.lastName = lastName.getText().toString();
        model.bio = bio.getText().toString();
        model.telephone = telephone.getText().toString();
        model.www = www.getText().toString();
        model.email = email.getText().toString();
        model.settings.onNewComment = newCommentSetting.isChecked();
        model.settings.onNewParticipant = newParticipantSetting.isChecked();
        model.settings.onUpdate = eventUpdatedSetting.isChecked();
        model.settings.onLeavingParticipant = leavingParticipantSetting.isChecked();
        UserBackendService_.intent(getActivity()).updateUser(model).start();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void setSettings(UserSettings settings) {
        newCommentSetting.setChecked(settings.onNewComment);
        newParticipantSetting.setChecked(settings.onNewParticipant);
        eventUpdatedSetting.setChecked(settings.onUpdate);
        leavingParticipantSetting.setChecked(settings.onLeavingParticipant);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
