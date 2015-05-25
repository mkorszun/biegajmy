package com.biegajmy.backend;

import android.util.Log;
import com.biegajmy.model.Error;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;

public class BackendErrorHandler implements ErrorHandler {

    private static final String TAG = BackendErrorHandler.class.getName();

    @Override public Throwable handleError(RetrofitError cause) {
        try {
            Error error = (Error) cause.getBodyAs(Error.class);
            return new BackendError(error.reason);
        } catch (Exception e) {
            Log.e(TAG, "Unknown backend error", e);
            return new BackendError("Unknown error");
        }
    }
}
