package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.Event;
import java.util.List;

public class ListEventTask extends AsyncTask<Object, Void, List<Event>> {

    private ListEventExecutor executor;
    private Exception exception;

    public ListEventTask(ListEventExecutor executor) {
        this.executor = executor;
    }

    @Override protected List<Event> doInBackground(Object... args) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            double x = (double) args[1];
            double y = (double) args[2];
            int max = (int) args[3];
            return backend.listEvents(x, y, max, args[0].toString());
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