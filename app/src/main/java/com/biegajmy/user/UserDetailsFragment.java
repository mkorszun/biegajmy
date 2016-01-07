package com.biegajmy.user;

import android.app.Activity;
import android.content.Context;
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
import com.biegajmy.user.UserEventBus.UpdateUserEventFailed.Reason;
import com.biegajmy.utils.PhotoUtils;
import com.biegajmy.validators.TextFormValidator;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.fragment_user_details) public class UserDetailsFragment extends ModelFragment<User>
    implements MaterialDialog.ListCallback {

    public static final String USER_ARG = "USER_ARG";

    @ViewById(R.id.user_photo) protected ImageView userPhoto;
    @ViewById(R.id.firstname) protected EditText firstName;
    @ViewById(R.id.lastname) protected EditText lastName;
    @ViewById(R.id.telephone) protected EditText telephone;
    @ViewById(R.id.www) protected EditText www;
    @ViewById(R.id.email) protected EditText email;
    @ViewById(R.id.bio) protected EditText bio;

    @ViewById(R.id.new_comment_setting) protected SwitchCompat newCommentSetting;
    @ViewById(R.id.new_participant_setting) protected SwitchCompat newParticipantSetting;
    @ViewById(R.id.leaving_participant_setting) protected SwitchCompat leavingParticipantSetting;
    @ViewById(R.id.event_updated_setting) protected SwitchCompat eventUpdatedSetting;

    @Bean protected TextFormValidator textFormValidator;
    @StringRes(R.string.user_update_failed_email_exists) protected String EMAIL_EXISTS;

    private Context context;
    private UserDetailsChangeListener userDetailsChangeListener;

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

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @AfterViews void setContent() {
        PhotoUtils.set(model.photo_url, this.context, userPhoto);
        firstName.setText(model.firstName);
        lastName.setText(model.lastName);
        bio.setText(model.bio);
        telephone.setText(model.telephone);
        www.setText(model.www);
        email.setText(model.email);
        newCommentSetting.setChecked(model.settings.onNewComment);
        newParticipantSetting.setChecked(model.settings.onNewParticipant);
        eventUpdatedSetting.setChecked(model.settings.onUpdate);
        leavingParticipantSetting.setChecked(model.settings.onLeavingParticipant);
    }

    @TextChange({ R.id.firstname, R.id.lastname, R.id.bio, R.id.telephone, R.id.www, R.id.email }) @CheckedChange({
        R.id.new_comment_setting, R.id.new_participant_setting, R.id.leaving_participant_setting,
        R.id.event_updated_setting
    }) public void onDataChanged() {
        if (userDetailsChangeListener != null) userDetailsChangeListener.onChange(getUser());
    }

    @Click({ R.id.user_photo, R.id.user_photo_button }) public void selectPicture() {
        new MaterialDialog.Builder(this.context).items(R.array.photo_sources).itemsCallback(this).show();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == PhotoUtils.SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            int orientation = PhotoUtils.getOrientation(this.context, selectedImageUri);
            UserBackendService_.intent(this.context).scalePhotoFromPath(selectedImageUri, orientation).start();
        }

        if (resultCode == Activity.RESULT_OK && requestCode == PhotoUtils.CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            UserBackendService_.intent(this.context).scalePhotoFromBitmap(photo).start();
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
        if (userDetailsChangeListener != null) userDetailsChangeListener.onUpdate(event.user);
        Toast.makeText(this.context, R.string.user_update_ok_msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(UserEventBus.UpdateUserEventFailed event) {
        errorForReason(event.reason);
    }

    @Subscribe public void event(UserEventBus.UpdateUserPhotoOk event) {
        Toast.makeText(this.context, R.string.user_photo_update_ok_msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe public void event(UserEventBus.UpdateUserPhotoFailed event) {
        Toast.makeText(this.context, R.string.user_photo_update_failed_msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe @UiThread public void event(UserEventBus.ScalePhotoOK event) {
        UserBackendService_.intent(this.context).updatePhoto(event.path).start();
        Uri uri = Uri.fromFile(new File(event.path));
        Picasso.with(this.context).load(uri).into(userPhoto);
    }

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    public void update() {
        if (textFormValidator.validate(fields()) && textFormValidator.validateEmail(email,
            R.string.email_invalid_error)) {
            UserBackendService_.intent(this.context).updateUser(getUser()).start();
        }
    }

    public void setUserDetailsChangeListener(UserDetailsChangeListener userDetailsChangeListener) {
        this.userDetailsChangeListener = userDetailsChangeListener;
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private User getUser() {
        User user = new User();
        user.id = model.id;
        user.photo_url = model.photo_url;

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
        return user;
    }

    private Map<TextView, Integer> fields() {
        Map<TextView, Integer> map = new HashMap<>();
        map.put(firstName, R.string.user_form_field_missing);
        map.put(lastName, R.string.user_form_field_missing);
        map.put(email, R.string.user_form_field_missing);
        return map;
    }

    private void errorForReason(Reason reason) {
        switch (reason) {
            case EMAIL_EXISTS:
                email.setError(EMAIL_EXISTS);
                break;
            case UNKNOWN:
                Toast.makeText(this.context, R.string.user_update_failed_msg, Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this.context, R.string.user_update_failed_msg, Toast.LENGTH_LONG).show();
                break;
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
