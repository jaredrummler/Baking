package com.jaredrummler.baking.media;

import android.support.v4.media.session.MediaSessionCompat;

public class MediaSessionCallback extends MediaSessionCompat.Callback {

    private final MediaPlayer player;

    public MediaSessionCallback(MediaPlayer player) {
        this.player = player;
    }

    @Override
    public void onPlay() {
        super.onPlay();
        player.play(player.getUri());
    }

    @Override
    public void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        player.stop();
    }

    @Override
    public void onSeekTo(long pos) {
        super.onSeekTo(pos);
        player.seekTo(pos);
    }

}
