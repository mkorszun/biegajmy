package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.User;

public class GetUserTask extends AsyncTask<String, Void, User> {

    private Exception exception;
    private TaskExecutor executor;

    public GetUserTask(TaskExecutor executor) {
        this.executor = executor;
    }

    @Override protected User doInBackground(String... params) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            return backend.getUser(params[0]);
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
