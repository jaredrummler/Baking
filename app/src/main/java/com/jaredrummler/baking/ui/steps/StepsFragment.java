package com.jaredrummler.baking.ui.steps;

import android.net.Uri;
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
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.jaredrummler.baking.R;
import com.jaredrummler.baking.data.model.Step;
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

    @BindView(R.id.player)
    SimpleExoPlayerView playerView;
    @BindView(R.id.tv_short_desc)
    TextView shortDescText;
    @BindView(R.id.tv_description)
    TextView descText;
    @BindBool(R.bool.isTablet)
    boolean isTablet;

    private MediaSessionCompat mediaSession;
    private MediaPlayer player;
    private Step step;
    private long seekPos;

    public static StepsFragment newInstance(Step step) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStop() {
        player.stop();
        super.onStop();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            seekPos = savedInstanceState.getLong(STATE_SEEK_POS, 0);
        }

        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, view);

        step = getArguments().getParcelable(EXTRA_STEP);

        if (!TextUtils.isEmpty(step.getVideoURL())) {
            playerView.setVisibility(View.VISIBLE);
            player.play(Uri.parse(step.getVideoURL()));
            playerView.setPlayer(player.getPlayer());
        }

        if (shortDescText != null) {
            shortDescText.setText(step.getShortDescription());
            if (!TextUtils.equals(step.getShortDescription(), step.getDescription())) {
                descText.setText(step.getDescription());
            } else {
                descText.setVisibility(View.GONE);
                if (RecipeUtils.isIntoStep(step)) shortDescText.setVisibility(View.GONE);
            }
        }

        if (!isTablet && getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            makePlayerFullscreen();
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        seekPos = player.getPlayer().getCurrentPosition();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_SEEK_POS, seekPos);
    }

    @Override
    public void onPlay(Uri uri) {
        mediaSession.setActive(true);
        if (playerView.getPlayer() == null) {
            playerView.setPlayer(player.getPlayer());
            player.seekTo(seekPos);
        }
    }

    @Override
    public void onPause(Uri uri) {
        mediaSession.setActive(false);
    }

    private void makePlayerFullscreen() {
        if (TextUtils.isEmpty(step.getVideoURL())) return;
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
