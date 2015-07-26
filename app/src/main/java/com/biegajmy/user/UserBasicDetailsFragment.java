package com.biegajmy.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import com.biegajmy.R;
import com.biegajmy.model.User;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_user_basic_details) public class UserBasicDetailsFragment
    extends Fragment {

    private static final String ARG_USER = "ARG_USER";
    private User user;

    @ViewById(R.id.user_photo) CircularImageView userPhoto;
    @ViewById(R.id.user_name) TextView userName;
    @ViewById(R.id.user_bio) TextView userBio;

    public static UserBasicDetailsFragment_ newInstance(User user) {
        UserBasicDetailsFragment_ fragment = new UserBasicDetailsFragment_();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_USER);
        }
    }

    @AfterViews public void setContent() {
        Picasso.with(getActivity()).load(user.photo_url).into(userPhoto);
        userName.setText(String.format("%s (%d)", user.firstName, 20));
        userBio.setText(user.bio);
    }
}
