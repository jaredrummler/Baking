package com.jaredrummler.baking.ui.steps;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.model.Step;
import com.jaredrummler.baking.image.GlideApp;
import com.jaredrummler.baking.media.MediaPlayer;
import com.jaredrummler.baking.utils.RecipeUtils;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;


public class StepsFragment extends Fragment implements MediaPlayer.Listener {

    private static final String TAG = "StepsFragment";

    private static final String EXTRA_STEP = "extras.STEP";
    private static final String STATE_SEEK_POS = "state.SEEK_POS";
    private static final String STATE_PLAY_WHEN_READY = "state.PLAY_WHEN_READY";

    @BindView(R.id.player)
    SimpleExoPlayerView playerView;

    @BindView(R.id.thumbnail)
    ImageView thumbnailImage;

    @BindView(R.id.tv_short_desc)
    TextView shortDescText;

    @BindView(R.id.tv_description)
    TextView descText;

    @BindBool(R.bool.isTablet)
    boolean isTablet;

    private MediaSessionCompat mediaSession;
    private MediaPlayer player;
    private Step step;
    private long seekPos = 0;
    private boolean playWhenReady = true;

    public static StepsFragment newInstance(Step step) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        step = getArguments().getParcelable(EXTRA_STEP);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            initializePlayer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            initializePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            seekPos = savedInstanceState.getLong(STATE_SEEK_POS, 0);
            playWhenReady = savedInstanceState.getBoolean(STATE_PLAY_WHEN_READY, true);
        }
        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set the thumbnail image if it exists.
        if (!TextUtils.isEmpty(step.getThumbnailURL())) {
            thumbnailImage.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            GlideApp.with(getActivity())
                    .load(step.getThumbnailURL())
                    .into(thumbnailImage);
        }
        // Set the description
        if (shortDescText != null) {
            shortDescText.setText(step.getShortDescription());
            if (!TextUtils.equals(step.getShortDescription(), step.getDescription())) {
                descText.setText(step.getDescription());
            } else {
                descText.setVisibility(View.GONE);
                if (RecipeUtils.isIntoStep(step)) shortDescText.setVisibility(View.GONE);
            }
        }
        // Set the player to fullscreen if we are in landscape mode on a phone
        if (!isTablet && getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            makePlayerFullscreen();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_SEEK_POS, seekPos);
        outState.putBoolean(STATE_PLAY_WHEN_READY, playWhenReady);
    }

    @Override
    public void onPlay(Uri uri) {
        mediaSession.setActive(true);
        if (playerView.getPlayer() == null) {
            playerView.setPlayer(player.getPlayer());
        }
    }

    @Override
    public void onPause(Uri uri) {
        mediaSession.setActive(false);
    }

    private void initializePlayer() {
        if (player == null) {
            player = new MediaPlayer(getActivity(), this);
            mediaSession = new MediaSessionCompat(getContext(), TAG);
            mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mediaSession.setMediaButtonReceiver(null);
            PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PLAY |
                            PlaybackStateCompat.ACTION_PAUSE |
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                            PlaybackStateCompat.ACTION_PLAY_PAUSE);
            mediaSession.setPlaybackState(stateBuilder.build());
            mediaSession.setCallback(player.getCallback());
        }

        String videoUrl = step.getVideoURL();
        if (!TextUtils.isEmpty(videoUrl)) {
            playerView.setVisibility(View.VISIBLE);
            player.play(Uri.parse(videoUrl));
            playerView.setPlayer(player.getPlayer());
            player.seekTo(seekPos);
            if (!playWhenReady) player.getPlayer().setPlayWhenReady(false);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            // Save the resume position
            seekPos = player.getPlayer().getCurrentPosition();
            playWhenReady = player.getPlayer().getPlayWhenReady();
            // Cleanup
            player.stop();
            player = null;
        }
    }

    private void makePlayerFullscreen() {
        if (TextUtils.isEmpty(RecipeUtils.getVideoUrl(step))) return;
        // Expand the view
        playerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        // Hide the other content
        shortDescText.setVisibility(View.GONE);
        descText.setVisibility(View.GONE);
        // Hide the ActionBar and buttons
        if (getActivity() instanceof StepsView) {
            ((StepsView) getActivity()).onFullscreen();
        }
    }

}
