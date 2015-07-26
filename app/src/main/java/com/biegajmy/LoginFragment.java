package com.biegajmy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;
import com.biegajmy.splash.SplashActivity_;
import com.biegajmy.user.UserEventBus;
import com.biegajmy.user.UserEventBus.CheckTokenEvent;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.fragment_login) public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getName();

    private Session session;
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    @ViewById(R.id.authButton) LoginButton login;
    @StringRes(R.string.user_token_request_failed_msg) String MSG;
    @StringArrayRes(R.array.facebook_permissions) String[] PERMISSIONS;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
        uiHelper.onCreate(savedInstanceState);
        UserEventBus.getInstance().register(this);
    }

    @AfterViews public void setUp() {
        login.setReadPermissions(PERMISSIONS);
        login.setFragment(this);
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
        UserEventBus.getInstance().unregister(this);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(UserEventBus.TokenOKEvent event) {
        UserEventBus.getInstance().post(new UserEventBus.SyncUserDataEvent());
        final Intent it = new Intent(getActivity(), SplashActivity_.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(it);
        getActivity().finish();
    }

    @Subscribe public void event(UserEventBus.TokenNOKEvent event) {
        Toast.makeText(getActivity(), MSG, Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    //********************************************************************************************//
    // Event handlers
    //********************************************************************************************//

    private void onSessionStateChange(final Session session, SessionState state,
        Exception exception) {
        if (state.isOpened() && (this.session == null || isSessionChanged(session))) {

            this.session = session;
            Log.i(TAG, "Logged in...");

            Request.newMeRequest(session, new Request.GraphUserCallback() {

                @Override public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        String accessToken = session.getAccessToken();
                        UserEventBus.getInstance().post(new CheckTokenEvent(accessToken));
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

    //********************************************************************************************//
    //********************************************************************************************//
}
