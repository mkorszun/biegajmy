package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;

public class DeleteEventTask extends AsyncTask<Object, Void, Void> {

    private DeleteEventExecutor executor;
    private Exception exception;

    public DeleteEventTask(DeleteEventExecutor executor) {
        this.executor = executor;
    }

    @Override protected Void doInBackground(Object... params) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            backend.deleteEvent((String) params[0], (String) params[1]);
            return null;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override protected void onPostExecute(Void param) {
        if (exception != null) {
            executor.onFailure(exception);
        } else {
            executor.onSuccess();
        }
    }
}
