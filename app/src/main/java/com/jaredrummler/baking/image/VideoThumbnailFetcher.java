package com.jaredrummler.baking.image;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

final class VideoThumbnailFetcher implements DataFetcher<InputStream> {

    private final VideoThumbnail thumbnail;

    VideoThumbnailFetcher(VideoThumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            Map<String, String> headers = new HashMap<>();
            retriever.setDataSource(thumbnail.getUrl(), headers);
            Bitmap bitmap = retriever.getFrameAtTime();
            if (bitmap != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                callback.onDataReady(new ByteArrayInputStream(bitmapdata));
                return;
            }
        } finally {
            retriever.release();
        }
        callback.onLoadFailed(new Exception("Could not load bitmap from " + thumbnail.getUrl()));
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void cancel() {

    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }

}
