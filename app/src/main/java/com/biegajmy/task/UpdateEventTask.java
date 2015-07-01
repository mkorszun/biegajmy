package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.Event;
import com.biegajmy.model.NewEvent;

public class UpdateEventTask extends AsyncTask<Object, Void, Event> {

    private UpdateEventExecutor executor;
    private Exception exception;

    public UpdateEventTask(UpdateEventExecutor executor) {
        this.executor = executor;
    }

    @Override protected Event doInBackground(Object... params) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            return backend.updateEvent((String) params[0], (NewEvent) params[1], (String) params[2]);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override protected void onPostExecute(Event event) {
        if (exception != null) {
            executor.onFailure(exception);
        } else {
            executor.onSuccess(event);
        }
    }
}
