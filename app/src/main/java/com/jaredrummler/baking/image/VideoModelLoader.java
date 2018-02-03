package com.jaredrummler.baking.image;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

import java.io.InputStream;

public class VideoModelLoader implements ModelLoader<VideoThumbnail, InputStream> {

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull VideoThumbnail thumbnail, int width, int height, @NonNull Options options) {
        return new ModelLoader.LoadData<>(new ObjectKey(thumbnail), new VideoThumbnailFetcher(thumbnail));
    }

    @Override
    public boolean handles(@NonNull VideoThumbnail thumbnail) {
        return true;
    }

}
