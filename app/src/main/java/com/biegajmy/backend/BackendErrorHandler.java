package com.biegajmy.backend;

import android.util.Log;
import com.biegajmy.backend.error.AuthError;
import com.biegajmy.backend.error.BackendError;
import com.biegajmy.backend.error.ConflictError;
import com.biegajmy.backend.error.NotFoundError;
import com.biegajmy.model.Error;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;

public class BackendErrorHandler implements ErrorHandler {

    private static final String TAG = BackendErrorHandler.class.getName();

    @Override public Throwable handleError(RetrofitError cause) {
        try {
            Error error = (Error) cause.getBodyAs(Error.class);
            switch (cause.getResponse().getStatus()) {
                case 401:
                    return new AuthError(error.reason);
                case 404:
                    return new NotFoundError(error.reason);
                case 409:
                    return new ConflictError(error.reason);
                default:
                    return new BackendError(error.reason);
            }
        } catch (Exception e) {
            Log.e(TAG, "Unknown backend error", e);
            return new BackendError("Unknown error");
        }
    }
}
