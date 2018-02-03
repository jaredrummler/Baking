package com.jaredrummler.baking.media;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

public class MediaPlayer {

    private final MediaSessionCallback callback;
    private final SimpleExoPlayer player;
    private final Listener listener;
    private final Context context;
    private Uri uri;

    public MediaPlayer(Context context, Listener listener) {
        this.callback = new MediaSessionCallback(this);
        this.context = context.getApplicationContext();
        this.listener = listener;
        this.player = createPlayer();
    }

    public void prepare(Uri uri) {
        if (!uri.equals(this.uri)) {
            String userAgent = Util.getUserAgent(context, context.getPackageName());
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent);
            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .setExtractorsFactory(new DefaultExtractorsFactory())
                    .createMediaSource(uri);
            player.prepare(mediaSource);
            this.uri = uri;
        }
    }

    public void play(Uri uri) {
        prepare(uri);
        player.setPlayWhenReady(true);
        if (listener != null) {
            listener.onPlay(this.uri);
        }
    }

    public void pause() {
        player.setPlayWhenReady(false);
        if (listener != null) {
            listener.onPause(uri);
        }
    }

    public void stop() {
        player.stop();
        player.release();
    }

    public void seekTo(long position) {
        player.seekTo(position);
    }

    public MediaSessionCallback getCallback() {
        return callback;
    }

    public Uri getUri() {
        return uri;
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    private SimpleExoPlayer createPlayer() {
        TransferListener<? super DataSource> bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory
                = new AdaptiveTrackSelection.Factory((BandwidthMeter) bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        return ExoPlayerFactory.newSimpleInstance(context, trackSelector);
    }

    public interface Listener {

        void onPlay(Uri uri);

        void onPause(Uri uri);

    }


}
