package com.biegajmy.backend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.biegajmy.LocalStorage;
import com.biegajmy.model.Token;
import com.biegajmy.model.User;
import com.biegajmy.task.GetUserTask;
import com.biegajmy.task.TaskExecutor;
import com.biegajmy.task.UpdateUserTask;
import com.biegajmy.user.UserEventBus;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

@EService public class UserBackendService extends Service {

    private static final String TAG = UserBackendService.class.getName();

    Token token;
    @Bean LocalStorage localStorage;
    Bus userBus = UserEventBus.getInstance();

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    @Override public void onCreate() {
        Log.d(TAG, "Starting user backend service");
        token = localStorage.getToken2();
        userBus.register(this);
        syncUserData(null);
    }

    @Override public void onDestroy() {
        Log.d(TAG, "Stopping user backend service");
        userBus.unregister(this);
    }

    @Override public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //********************************************************************************************//
    // Event handlers
    //********************************************************************************************//

    @Subscribe public void updateTags(UserEventBus.UpdateUserTagsEvent event) {
        User user = localStorage.getUser();
        user.setTags(event.tags);
        localStorage.updateUser(user);

        new UpdateUserTask(new TaskExecutor<User>() {
            @Override public void onSuccess(User user) {
                Log.d(TAG, "Successfully updated user");
            }

            @Override public void onFailure(Exception e) {
                Log.e(TAG, "Failed to update user", e);
            }
        }).execute(token.id, token.token, user);
    }

    @Subscribe public void syncUserData(UserEventBus.SyncUserDataEvent event) {
        new GetUserTask(new TaskExecutor<User>() {
            @Override public void onSuccess(User user) {
                Log.d(TAG, "Successfully synced user");
                localStorage.updateUser(user);
            }

            @Override public void onFailure(Exception e) {
                Log.e(TAG, "Failed to sync user data", e);
            }
        }).execute(token.id, token.token);
    }

    @Subscribe public void updateUserData(UserEventBus.UpdateUserEvent event) {
        final User user = event.user;
        new UpdateUserTask(new TaskExecutor<User>() {
            @Override public void onSuccess(User user) {
                Log.d(TAG, "Successfully updated user");
                localStorage.updateUser(user);
                userBus.post(new UserEventBus.UpdateUserEventOk());
            }

            @Override public void onFailure(Exception e) {
                Log.e(TAG, "Failed to update user", e);
                userBus.post(new UserEventBus.UpdateUserEventFailed(e));
            }
        }).execute(token.id, token.token, user);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
