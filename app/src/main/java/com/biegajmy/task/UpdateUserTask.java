package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.User;
import retrofit.client.Header;
import retrofit.client.Response;

public class UpdateUserTask extends AsyncTask<Object, Void, String> {

    private UpdateUserExecutor executor;
    private Exception exception;

    public UpdateUserTask(UpdateUserExecutor executor) {
        this.executor = executor;
    }

    @Override protected String doInBackground(Object... args) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            Response resp = backend.createUser(args[0].toString(), (User) args[1]);
            return getUserId(resp);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override protected void onPostExecute(String v) {
        if (exception != null) {
            executor.onFailure(exception);
        } else {
            executor.onSuccess(v);
        }
    }

    private String getUserId(Response resp) {
        for (Header h : resp.getHeaders()) {
            if ("Location".equals(h.getName())) {
                return h.getValue();
            }
        }
        return null;
    }
}
