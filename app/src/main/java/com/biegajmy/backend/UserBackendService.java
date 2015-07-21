package com.biegajmy.backend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.biegajmy.LocalStorage;
import com.biegajmy.model.User;
import com.biegajmy.tags.TagListBus;
import com.biegajmy.task.UpdateUserExecutor;
import com.biegajmy.task.UpdateUserTask;
import com.squareup.otto.Subscribe;

public class UserBackendService extends Service {

    private static final String TAG = UserBackendService.class.getName();

    private LocalStorage localStorage;

    @Override public void onCreate() {
        Log.d(TAG, "Starting user backend service");
        localStorage = new LocalStorage(getApplicationContext());
        TagListBus.getInstance().register(this);
    }

    @Override public void onDestroy() {
        Log.d(TAG, "Stoping user backend service");
        TagListBus.getInstance().unregister(this);
    }

    @Override public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Subscribe public void updateTags(TagListBus.UpdateTagsEvent event) {
        User user = localStorage.getUser();
        String token = localStorage.getToken();

        user.setTags(event.tags);
        localStorage.updateUser(user);

        new UpdateUserTask(new UpdateUserExecutor() {
            @Override public void onSuccess(String id) {
                Log.d(TAG, String.format("Successfuly updated user: %s", id));
            }

            @Override public void onFailure(Exception e) {
                Log.e(TAG, "Failed to update user", e);
            }
        }).execute(token, user);
    }
}
