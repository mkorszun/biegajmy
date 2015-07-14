package com.biegajmy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.biegajmy.events.EventMainActivity_;
import com.biegajmy.model.User;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import java.util.Arrays;
import org.androidannotations.annotations.EFragment;

import static com.biegajmy.user.UserUtils.getAge;
import static com.biegajmy.user.UserUtils.getPhotoUrl;

@EFragment public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getName();

    LocalStorage storage;
    private Session session;
    private LoginButton login;
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new LocalStorage(getActivity().getApplicationContext());
        uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        login = (LoginButton) rootView.findViewById(R.id.authButton);
        login.setReadPermissions(Arrays.asList("public_profile", "user_birthday", "user_location"));
        login.setFragment(this);
        return rootView;
    }

    @Override public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void onSessionStateChange(final Session session, SessionState state,
        Exception exception) {
        if (state.isOpened() && (this.session == null || isSessionChanged(session))) {

            this.session = session;
            Log.i(TAG, "Logged in...");

            Request.newMeRequest(session, new Request.GraphUserCallback() {

                @Override public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {

                        int age = getAge(user.getBirthday());
                        String firstName = user.getFirstName();
                        String lastName = user.getLastName();
                        String location = user.getLocation().getName();
                        String accessToken = session.getAccessToken();

                        Log.i(TAG, "First Name: " + firstName);
                        Log.i(TAG, "Last Name: " + lastName);
                        Log.i(TAG, "Token: " + accessToken);
                        Log.i(TAG, "Location: " + location);
                        Log.i(TAG, "Birthday: " + user.getBirthday());
                        Log.i(TAG, "Age: " + age);

                        Intent it =
                            new Intent(getActivity().getBaseContext(), EventMainActivity_.class);

                        if (!storage.hasUser()) {
                            User userData = new User();
                            userData.setAge(age);
                            userData.setFirstName(firstName);
                            userData.setLastName(lastName);
                            userData.setLocation(location);
                            userData.setPhoto_url(getPhotoUrl(user.getId()));
                            storage.updateUser(userData);
                        }

                        storage.updateToken(accessToken);
                        startActivity(it);
                    }
                }
            }).executeAsync();
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    }

    private boolean isSessionChanged(Session session) {

        if (this.session.getState() != session.getState()) {
            return true;
        }

        if (this.session.getAccessToken() != null) {
            if (!this.session.getAccessToken().equals(session.getAccessToken())) {
                return true;
            }
        } else if (session.getAccessToken() != null) {
            return true;
        }

        return false;
    }
}
