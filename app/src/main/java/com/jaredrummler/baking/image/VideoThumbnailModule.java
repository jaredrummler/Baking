package com.jaredrummler.baking.image;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.jaredrummler.baking.App;

import java.io.InputStream;

@GlideModule
public class VideoThumbnailModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        // http://bumptech.github.io/glide/doc/configuration.html#applications
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(App.getApp().getOkHttpClient()));
        registry.replace(VideoThumbnail.class, InputStream.class, new VideoThumbnailFactory());
    }

}
