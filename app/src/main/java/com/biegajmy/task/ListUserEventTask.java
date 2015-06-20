package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.Event;
import java.util.List;

public class ListUserEventTask extends AsyncTask<String, Void, List<Event>> {

    private ListUserEventExecutor executor;
    private Exception exception;

    public ListUserEventTask(ListUserEventExecutor executor) {
        this.executor = executor;
    }

    @Override protected List<Event> doInBackground(String... args) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            String userId = args[0];
            String token = args[1];
            return backend.listEvents(userId, token);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override protected void onPostExecute(List<Event> events) {
        if (exception != null) {
            executor.onFailure(exception);
        } else {
            executor.onSuccess(events);
        }
    }
}
