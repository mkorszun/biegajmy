package com.biegajmy.backend;

import android.util.Base64;
import com.biegajmy.BuildConfig;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class BackendInterfaceFactory {

    private static final String AUTHORIZATION = "Authorization";
    private static final String URL = BuildConfig.BACKEND_URL;

    public static BackendInterface build() {
        return build(URL);
    }

    public static BackendInterface build(String username, String password) {
        return build(URL, username, password);
    }

    //********************************************************************************************//
    // Services
    //********************************************************************************************//

    public static BackendInterface build(String url) {
        RestAdapter restAdapter = new RestAdapter.Builder().setErrorHandler(new BackendErrorHandler())
            .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.BASIC : RestAdapter.LogLevel.NONE)
            .setEndpoint(url)
            .build();
        return restAdapter.create(BackendInterface.class);
    }

    public static BackendInterface build(String url, String username, String password) {
        RestAdapter restAdapter = new RestAdapter.Builder().setErrorHandler(new BackendErrorHandler())
            .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.BASIC : RestAdapter.LogLevel.NONE)
            .setRequestInterceptor(requestInterceptor(username, password))
            .setEndpoint(url)
            .build();
        return restAdapter.create(BackendInterface.class);
    }

    //********************************************************************************************//
    // Auth interceptors
    //********************************************************************************************//

    private static RequestInterceptor requestInterceptor(final String username, final String password) {
        return new RequestInterceptor() {
            @Override public void intercept(RequestFacade request) {
                String string = "Basic " + basic(username, password);
                request.addHeader("Accept", "application/json");
                request.addHeader(AUTHORIZATION, string);
            }
        };
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private static String basic(String username, String password) {
        final String credentials = username + ":" + password;
        return Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
