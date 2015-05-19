package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.User;

public class CreateUserTask extends AsyncTask<Object, Void, Void> {

    private CreateUserExecutor executor;
    private Exception exception;

    public CreateUserTask(CreateUserExecutor executor) {
        this.executor = executor;
    }

    @Override
    protected Void doInBackground(Object... args) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            backend.createUser(args[0].toString(), (User)args[1]);
            return null;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void v) {
        if (exception != null) {
            executor.onFailure(exception);
        } else {
            executor.onSuccess();
        }
    }
}
