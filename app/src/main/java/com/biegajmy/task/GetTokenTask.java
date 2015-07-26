package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.Token;

public class GetTokenTask extends AsyncTask<String, Void, Token> {

    private Exception exception;
    private TaskExecutor executor;

    public GetTokenTask(TaskExecutor executor) {
        this.executor = executor;
    }

    @Override protected Token doInBackground(String... params) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            return backend.createToken(params[0]);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override protected void onPostExecute(Token token) {
        if (exception != null) {
            executor.onFailure(exception);
        } else {
            executor.onSuccess(token);
        }
    }
}
