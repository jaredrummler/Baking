package com.jaredrummler.baking;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

public class StethoApp extends App {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

    @Override
    protected OkHttpClient.Builder okHttpClientBuilder() {
        return super.okHttpClientBuilder().addNetworkInterceptor(new StethoInterceptor());
    }
}
