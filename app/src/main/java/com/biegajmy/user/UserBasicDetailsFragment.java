package com.biegajmy.user;

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
    @ViewById(R.id.user_name) TextView userName;
    @ViewById(R.id.user_www) TextView userWWW;
    @ViewById(R.id.user_email) TextView userEmail;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override protected String getModelKey() {
        return ARG_USER;
    }

    @AfterViews public void setContent() {
        if (model != null) {
            Picasso.with(getActivity()).load(model.photo_url).into(userPhoto);
            userName.setText(model.toString());
            userWWW.setText(model.www);
            userEmail.setText(model.email);
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
