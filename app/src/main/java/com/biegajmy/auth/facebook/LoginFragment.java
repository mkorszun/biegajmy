package com.biegajmy.auth.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;
import com.biegajmy.R;
import com.biegajmy.auth.LoginActivity;
import com.biegajmy.gcm.AppRegistrationService_;
import com.biegajmy.user.UserBackendService_;
import com.biegajmy.user.UserEventBus;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

@EFragment(R.layout.fragment_login) public class LoginFragment extends Fragment
    implements FacebookCallback<LoginResult> {

    private static final String TAG = LoginFragment.class.getName();

    private CallbackManager callbackManager;
    @ViewById(R.id.authButton) protected LoginButton login;
    @StringArrayRes(R.array.facebook_permissions) protected String[] PERMISSIONS;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserEventBus.getInstance().register(this);
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        UserEventBus.getInstance().unregister(this);
    }

    @AfterViews public void setUp() {
        login.setReadPermissions(PERMISSIONS);
        login.setFragment(this);
        login.registerCallback(callbackManager, this);
    }

    @Override public void onSuccess(LoginResult loginResult) {
        String accessToken = loginResult.getAccessToken().getToken();
        Log.d(TAG, String.format("Login successful: %s", accessToken));
        UserBackendService_.intent(getActivity()).checkToken(accessToken).start();
    }

    @Override public void onCancel() {
        Log.d(TAG, "Login cancelled");
        getActivity().finish();
    }

    @Override public void onError(FacebookException e) {
        Log.d(TAG, "Login failed", e);
        getActivity().finish();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(UserEventBus.TokenOKEvent event) {
        AppRegistrationService_.intent(getActivity()).registration().start();
        UserBackendService_.intent(getActivity()).syncUser().start();
    }

    @Subscribe public void event(UserEventBus.TokenNOKEvent event) {
        showError(event.reason);
        LoginManager.getInstance().logOut();
        getActivity().setResult(LoginActivity.AUTH_FAILED);
        getActivity().finish();
    }

    @Subscribe public void event(UserEventBus.SyncUserEventOK event) {
        getActivity().setResult(LoginActivity.AUTH_OK);
        getActivity().finish();
    }

    @Subscribe public void event(UserEventBus.SyncUserEventNOK event) {
        LoginManager.getInstance().logOut();
        Toast.makeText(getActivity(), R.string.login_unknown_error, Toast.LENGTH_LONG).show();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private void showError(UserEventBus.TokenNOKEvent.Reason reason) {
        switch (reason) {
            case EMAIL_EXISTS:
                Toast.makeText(getActivity(), R.string.email_already_used_error, Toast.LENGTH_LONG).show();
                break;
            case UNKNOWN:
                Toast.makeText(getActivity(), R.string.user_token_request_failed_msg, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
