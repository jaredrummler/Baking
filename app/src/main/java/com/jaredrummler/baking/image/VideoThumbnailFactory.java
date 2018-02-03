package com.jaredrummler.baking.image;

import android.support.annotation.NonNull;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

public class VideoThumbnailFactory implements ModelLoaderFactory<VideoThumbnail, InputStream> {

    @NonNull
    @Override
    public ModelLoader<VideoThumbnail, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
        return new VideoModelLoader();
    }

    @Override
    public void teardown() {

    }

}
