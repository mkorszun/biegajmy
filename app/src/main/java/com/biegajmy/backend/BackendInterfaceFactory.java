package com.biegajmy.backend;

import com.biegajmy.BuildConfig;
import retrofit.RestAdapter;

public class BackendInterfaceFactory {

    public static final String URL = BuildConfig.BACKEND_URL;

    public static BackendInterface build() {
        return build(URL);
    }

    public static BackendInterface build(String url) {
        RestAdapter restAdapter = new RestAdapter.Builder().setErrorHandler(new BackendErrorHandler())
            .setLogLevel(RestAdapter.LogLevel.BASIC)
            .setEndpoint(url)
            .build();
        return restAdapter.create(BackendInterface.class);
    }
}
