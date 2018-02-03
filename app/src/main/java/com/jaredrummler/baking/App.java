package com.jaredrummler.baking;

import android.app.Application;

import okhttp3.OkHttpClient;

public class App extends Application {

    private static App app;
    private OkHttpClient okHttpClient;

    public static App getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Application context should be safe to save as static member
        App.app = this;

        // Create OkHttpClient. Used to add Stetho interceptor in debug build
        okHttpClient = okHttpClientBuilder().build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    protected OkHttpClient.Builder okHttpClientBuilder() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true);
    }

}
