package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.Event;

public class JoinEventTask extends AsyncTask<Object, Void, Event> {

    private JoinEventExecutor executor;
    private Exception exception;

    public JoinEventTask(JoinEventExecutor executor) {
        this.executor = executor;
    }

    @Override protected Event doInBackground(Object... args) {
        try {
            boolean join = (boolean) args[2];
            BackendInterface backend = BackendInterfaceFactory.build();
            if (join) {
                return backend.joinEvent(args[1].toString(), args[0].toString());
            } else {
                return backend.leaveEvent(args[1].toString(), args[0].toString());
            }
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override protected void onPostExecute(Event v) {
        if (exception != null) {
            executor.onFailure(exception);
        } else {
            executor.onSuccess(v);
        }
    }
}
