package com.biegajmy.task;

import android.os.AsyncTask;
import com.biegajmy.backend.BackendInterface;
import com.biegajmy.backend.BackendInterfaceFactory;
import com.biegajmy.model.Comment;
import com.biegajmy.model.CommentList;
import com.biegajmy.model.Event;
import java.util.List;

public class CommentEventTask extends AsyncTask<String, Void, List<Comment>> {

    private TaskExecutor executor;
    private Exception exception;

    public CommentEventTask(TaskExecutor<List<Comment>> executor) {
        this.executor = executor;
    }

    @Override protected List<Comment> doInBackground(String... params) {
        try {
            BackendInterface backend = BackendInterfaceFactory.build();
            String eventID = params[0].toString();
            String token = params[1].toString();
            String msg = params[2].toString();
            return backend.comment(eventID, msg, token).comments;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override protected void onPostExecute(List<Comment> comments) {
        if (exception != null) {
            executor.onFailure(exception);
        } else {
            executor.onSuccess(comments);
        }
    }
}
