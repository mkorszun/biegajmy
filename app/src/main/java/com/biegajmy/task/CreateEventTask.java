package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.NewEvent;
import com.biegajmy.model.User;

public class CreateEventTask extends AsyncTask<Object, Void, Void> {

    private CreateEventExecutor executor;
    private Exception exception;

    public CreateEventTask(CreateEventExecutor executor) {
        this.executor = executor;
    }

    @Override
    protected Void doInBackground(Object... args) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            NewEvent arg = (NewEvent) args[1];
            backend.createEvent(args[0].toString(), arg);
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
