package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.User;

public class UpdateUserTask extends AsyncTask<Object, Void, User> {

    private TaskExecutor executor;
    private Exception exception;

    public UpdateUserTask(TaskExecutor executor) {
        this.executor = executor;
    }

    @Override protected User doInBackground(Object... args) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            String userId = args[0].toString();
            String token = args[1].toString();
            User user = (User) args[2];
            return backend.updateUser(userId, token, user);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override protected void onPostExecute(User user) {
        if (exception != null) {
            executor.onFailure(exception);
        } else {
            executor.onSuccess(user);
        }
    }
}
