package com.biegajmy.backend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.biegajmy.LocalStorage;
import com.biegajmy.model.User;
import com.biegajmy.task.GetUserTask;
import com.biegajmy.task.TaskExecutor;
import com.biegajmy.task.UpdateUserExecutor;
import com.biegajmy.task.UpdateUserTask;
import com.biegajmy.user.UserEventBus;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

@EService public class UserBackendService extends Service {

    private static final String TAG = UserBackendService.class.getName();

    @Bean LocalStorage localStorage;

    //********************************************************************************************//
    // API
    //********************************************************************************************//

    @Override public void onCreate() {
        Log.d(TAG, "Starting user backend service");
        UserEventBus.getInstance().register(this);
        syncUserData(null);
    }

    @Override public void onDestroy() {
        Log.d(TAG, "Stopping user backend service");
        UserEventBus.getInstance().unregister(this);
    }

    @Override public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //********************************************************************************************//
    // Event handlers
    //********************************************************************************************//

    @Subscribe public void updateTags(UserEventBus.UpdateUserTagsEvent event) {
        User user = localStorage.getUser();
        String token = localStorage.getToken();

        user.setTags(event.tags);
        localStorage.updateUser(user);

        new UpdateUserTask(new UpdateUserExecutor() {
            @Override public void onSuccess(String id) {
                Log.d(TAG, String.format("Successfully updated user: %s", id));
            }

            @Override public void onFailure(Exception e) {
                Log.e(TAG, "Failed to update user", e);
            }
        }).execute(token, user);
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
        }).execute(localStorage.getToken());
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
