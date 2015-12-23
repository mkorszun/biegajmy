package com.biegajmy.user;

import android.view.View;
import android.widget.TextView;
import com.biegajmy.R;
import com.biegajmy.general.ModelFragment;
import com.biegajmy.model.User;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_user_basic_details) public class UserBasicDetailsFragment extends ModelFragment<User> {

    public static final String ARG_USER = "ARG_USER";

    @ViewById(R.id.user_photo) CircleImageView userPhoto;
    @ViewById(R.id.user_firstname) TextView firstName;
    @ViewById(R.id.user_lastname) TextView lastName;
    @ViewById(R.id.user_www) TextView userWWW;
    @ViewById(R.id.user_email) TextView userEmail;
    @ViewById(R.id.user_description) TextView userDescription;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected String getModelKey() {
        return ARG_USER;
    }

    @AfterViews public void setContent() {
        if (model != null) {
            if (model.photo_url != null) Picasso.with(getActivity()).load(model.photo_url).into(userPhoto);
            firstName.setText(model.firstName);
            lastName.setText(model.lastName);
            userWWW.setText(model.www);
            userEmail.setText(model.email);
            userDescription.setText(model.bio);
            hideEmpty(model);
        }
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void hideEmpty(User user) {
        firstName.setVisibility(user.firstName == null || user.firstName.isEmpty() ? View.GONE : View.VISIBLE);
        lastName.setVisibility(user.lastName == null || user.lastName.isEmpty() ? View.GONE : View.VISIBLE);
        userWWW.setVisibility(user.www == null || user.www.isEmpty() ? View.GONE : View.VISIBLE);
        userEmail.setVisibility(user.email == null || user.email.isEmpty() ? View.GONE : View.VISIBLE);
        userDescription.setVisibility(user.bio == null || user.bio.isEmpty() ? View.GONE : View.VISIBLE);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
