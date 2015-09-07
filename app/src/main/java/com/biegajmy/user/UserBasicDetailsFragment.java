package com.biegajmy.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import com.biegajmy.R;
import com.biegajmy.model.User;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_user_basic_details) public class UserBasicDetailsFragment extends Fragment {

    public static final String ARG_USER = "ARG_USER";
    private User user;

    @ViewById(R.id.user_photo) CircleImageView userPhoto;
    @ViewById(R.id.user_name) TextView userName;
    @ViewById(R.id.user_www) TextView userWWW;
    @ViewById(R.id.user_email) TextView userEmail;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getArguments().getSerializable(ARG_USER);
    }

    @AfterViews public void setContent() {
        Picasso.with(getActivity()).load(user.photo_url).into(userPhoto);
        userName.setText(String.format("%s %s", user.firstName, user.lastName));
        userWWW.setText(user.www);
        userEmail.setText(user.email);
    }
}
