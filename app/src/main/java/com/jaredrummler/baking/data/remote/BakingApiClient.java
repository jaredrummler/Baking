package com.jaredrummler.baking.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.jaredrummler.baking.App.getApp;

public class BakingApiClient {

    private static final BakingApi INSTANCE;

    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BakingApi.BASE_URL)
                .client(getApp().getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        INSTANCE = retrofit.create(BakingApi.class);
    }

    private BakingApiClient() {
        throw new AssertionError("no instances");
    }

    public static BakingApi getInstance() {
        return INSTANCE;
    }

}
