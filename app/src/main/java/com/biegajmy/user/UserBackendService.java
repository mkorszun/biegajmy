package com.biegajmy.user;

import android.util.Log;
import com.biegajmy.LocalStorage;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.Token;
import com.biegajmy.model.User;
import com.squareup.otto.Bus;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;
import org.androidannotations.api.support.app.AbstractIntentService;

@EIntentService public class UserBackendService extends AbstractIntentService {

    private static final String TAG = UserBackendService.class.getName();

    @Bean LocalStorage localStorage;
    private Bus userBus = UserEventBus.getInstance();
    private BackendInterface backend = BackendInterfaceFactory.build();

    public UserBackendService() {
        super(TAG);
    }

    //********************************************************************************************//
    // Actions
    //********************************************************************************************//

    @ServiceAction public void syncUser() {
        try {
            String id = localStorage.getToken().id;
            String token = localStorage.getToken().token;
            localStorage.updateUser(backend.getUser(id, token));
            Log.d(TAG, "Successfully synced user");
        } catch (Exception e) {
            Log.e(TAG, "Failed to sync user data", e);
        }
    }

    @ServiceAction public void updateUser(User user) {
        try {
            String id = localStorage.getToken().id;
            String token = localStorage.getToken().token;
            localStorage.updateUser(backend.updateUser(id, token, user));

            Log.d(TAG, "Successfully updated user");
            userBus.post(new UserEventBus.UpdateUserEventOk());
        } catch (Exception e) {
            Log.e(TAG, "Failed to update user", e);
            userBus.post(new UserEventBus.UpdateUserEventFailed(e));
        }
    }

    @ServiceAction public void checkToken(String socialToken) {
        if (!localStorage.hasToken()) {
            Log.d(TAG, "Token not available. Requesting it from backend");

            try {
                Token token = backend.createToken(socialToken);
                localStorage.updateToke(token);

                Log.d(TAG, "Token request succeeded. Updating token locally");
                localStorage.updateToke(token);
                userBus.post(new UserEventBus.TokenOKEvent());
            } catch (Exception e) {
                Log.e(TAG, "Token request failed", e);
                userBus.post(new UserEventBus.TokenNOKEvent());
            }
        } else {
            Log.d(TAG, "Token already stored locally");
            userBus.post(new UserEventBus.TokenOKEvent());
        }
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
