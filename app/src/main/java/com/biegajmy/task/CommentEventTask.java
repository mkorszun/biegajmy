package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.Event;

public class CommentEventTask extends AsyncTask<String, Void, Event> {

    private TaskExecutor executor;
    private Exception exception;

    public CommentEventTask(TaskExecutor<Event> executor) {
        this.executor = executor;
    }

    @Override protected Event doInBackground(String... params) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            String eventID = params[0].toString();
            String token = params[1].toString();
            String msg = params[2].toString();
            return backend.comment(eventID, msg, token);
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
