package com.biegajmy.user;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import com.biegajmy.LocalStorage;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.backend.error.AuthError;
import com.biegajmy.backend.error.ConflictError;
import com.biegajmy.backend.error.NotFoundError;
import com.biegajmy.model.Device;
import com.biegajmy.model.NewUser;
import com.biegajmy.model.Token;
import com.biegajmy.model.User;
import com.biegajmy.user.UserEventBus.UpdateUserEventFailed.Reason;
import com.biegajmy.utils.PhotoUtils;
import com.squareup.otto.Bus;
import java.io.File;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;
import org.androidannotations.api.support.app.AbstractIntentService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import static com.biegajmy.user.UserEventBus.LoginNOK;
import static com.biegajmy.user.UserEventBus.LoginOK;
import static com.biegajmy.user.UserEventBus.PasswordResetNOK;
import static com.biegajmy.user.UserEventBus.PasswordResetOK;
import static com.biegajmy.user.UserEventBus.RegistrationNOK;
import static com.biegajmy.user.UserEventBus.RegistrationOK;
import static com.biegajmy.user.UserEventBus.ScalePhotoOK;
import static com.biegajmy.user.UserEventBus.SyncUserEventNOK;
import static com.biegajmy.user.UserEventBus.SyncUserEventOK;
import static com.biegajmy.user.UserEventBus.TokenNOKEvent;
import static com.biegajmy.user.UserEventBus.TokenOKEvent;
import static com.biegajmy.user.UserEventBus.UpdateUserEventFailed;
import static com.biegajmy.user.UserEventBus.UpdateUserEventOk;
import static com.biegajmy.user.UserEventBus.UpdateUserPhotoFailed;
import static com.biegajmy.user.UserEventBus.UpdateUserPhotoOk;
import static com.biegajmy.user.UserEventBus.getInstance;

@EIntentService public class UserBackendService extends AbstractIntentService {

    private static final String TAG = UserBackendService.class.getName();

    @Bean LocalStorage localStorage;
    private Bus userBus = getInstance();
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
            User user = backend.getUser(id, token);
            localStorage.updateUser(user);
            Log.d(TAG, "Successfully synced user");
            userBus.post(new SyncUserEventOK(user));
        } catch (Exception e) {
            Log.e(TAG, "Failed to sync user data", e);
            userBus.post(new SyncUserEventNOK());
        }
    }

    @ServiceAction public void updateUser(User user) {
        try {
            String id = localStorage.getToken().id;
            String token = localStorage.getToken().token;
            User updated = backend.updateUser(id, token, user);
            localStorage.updateUser(user);
            Log.d(TAG, "Successfully updated user");
            userBus.post(new UpdateUserEventOk(updated));
        } catch (ConflictError e) {
            Log.w(TAG, "Failed to update user", e);
            userBus.post(new UpdateUserEventFailed(Reason.EMAIL_EXISTS));
        } catch (Exception e) {
            Log.e(TAG, "Failed to update user", e);
            userBus.post(new UpdateUserEventFailed(Reason.UNKNOWN));
        }
    }

    @ServiceAction public void checkToken(String socialToken) {
        if (!localStorage.hasToken()) {
            Log.d(TAG, "Token not available. Requesting it from backend");

            try {
                Token token = backend.createToken(socialToken);
                localStorage.updateToken(token);

                Log.d(TAG, "Token request succeeded. Updating token locally");
                localStorage.updateToken(token);
                userBus.post(new TokenOKEvent());
            } catch (Exception e) {
                Log.e(TAG, "Token request failed", e);
                userBus.post(new TokenNOKEvent());
            }
        } else {
            Log.d(TAG, "Token already stored locally");
            userBus.post(new TokenOKEvent());
        }
    }

    @ServiceAction public void updatePhoto(String path) {
        if (path == null) return;
        backend.updatePhoto(localStorage.getToken().id, localStorage.getToken().token,
            new TypedFile("application/octet-stream", new File(path)), new Callback<User>() {

                @Override public void success(User user, Response response) {
                    Log.d(TAG, "Photo update successfully");
                    localStorage.updateUser(user);
                    userBus.post(new UpdateUserPhotoOk());
                }

                @Override public void failure(RetrofitError error) {
                    Log.e(TAG, "Failed to update photo", error);
                    userBus.post(new UpdateUserPhotoFailed(error));
                }
            });
    }

    @ServiceAction public void updateDevice(Device device) {
        try {
            Token token = localStorage.getToken();
            if (token == null) return;
            backend.updateDevice(device, token.id, token.token);
            Log.d(TAG, String.format("Updated device info: %s", device));
        } catch (Exception e) {
            Log.d(TAG, "Failed to update device info", e);
        }
    }

    @ServiceAction public void login(String username, String password) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build(username, password);
            Token token = backend.createToken();
            localStorage.updateToken(token);
            userBus.post(new LoginOK());
        } catch (AuthError e) {
            Log.w(TAG, "Failed to login", e);
            userBus.post(new LoginNOK(LoginNOK.Reason.NO_MATCH));
        } catch (Exception e) {
            Log.e(TAG, "Failed to login", e);
            userBus.post(new LoginNOK(LoginNOK.Reason.UNKNOWN));
        }
    }

    @ServiceAction public void register(NewUser newUser) {
        try {
            backend.createUser(newUser);
            userBus.post(new RegistrationOK());
        } catch (ConflictError e) {
            Log.w(TAG, "Failed to register", e);
            userBus.post(new RegistrationNOK(RegistrationNOK.Reason.USER_EXISTS));
        } catch (Exception e) {
            Log.e(TAG, "Failed to register", e);
            userBus.post(new RegistrationNOK(RegistrationNOK.Reason.UNKNOWN));
        }
    }

    @ServiceAction public void resetPassword(String email) {
        try {
            backend.resetPassword(email);
            userBus.post(new PasswordResetOK());
        } catch (NotFoundError e) {
            Log.w(TAG, "Failed to reset password", e);
            userBus.post(new PasswordResetNOK(PasswordResetNOK.Reason.USER_NOT_FOUND));
        } catch (Exception e) {
            Log.w(TAG, "Failed to reset password", e);
            userBus.post(new PasswordResetNOK(PasswordResetNOK.Reason.UNKNOWN));
        }
    }

    @ServiceAction public void scalePhotoFromPath(Uri path) {
        File file = PhotoUtils.scale(this, path);
        if (file != null) userBus.post(new ScalePhotoOK(file.getAbsolutePath()));
    }

    @ServiceAction public void scalePhotoFromBitmap(Bitmap bitmap) {
        File file = PhotoUtils.scale(this, bitmap);
        if (file != null) userBus.post(new ScalePhotoOK(file.getAbsolutePath()));
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
