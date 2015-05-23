package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;

public class JoinEventTask extends AsyncTask<Object, Void, Void> {

    private JoinEventExecutor executor;
    private Exception exception;

    public JoinEventTask(JoinEventExecutor executor) {
        this.executor = executor;
    }

    @Override protected Void doInBackground(Object... args) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            backend.joinEvent(args[1].toString(), args[0].toString());
            return null;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override protected void onPostExecute(Void v) {
        if (exception != null) {
            executor.onFailure(exception);
        } else {
            executor.onSuccess();
        }
    }
}
