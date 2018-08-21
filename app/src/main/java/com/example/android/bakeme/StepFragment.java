package com.example.android.bakeme;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakeme.model.Recipe;
import com.example.android.bakeme.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

public class StepFragment extends Fragment {

    private Step mStep;
    private Recipe mRecipe;
    private int mPosition;
    private boolean mIsTablet;
    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private ImageView mImageView;
    private final static String PLAY_WHEN_READY = "play_when_ready";
    private final static String CURRENT_POSITION = "current_position";
    private boolean mPlayWhenReady = true;
    private long mCurrentPosition;

    public StepFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            mCurrentPosition = savedInstanceState.getLong(CURRENT_POSITION);
        }
        if (getArguments().containsKey(Recipe.class.getSimpleName())) {
            mRecipe = (Recipe) getArguments().getSerializable(Recipe.class.getSimpleName());
            mPosition = getArguments().getInt(DetailsActivity.POSITION, 0);
            mStep = mRecipe.getSteps().get(mPosition);
        }
        mIsTablet = getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        mPlayerView = rootView.findViewById(R.id.player);
        mImageView = rootView.findViewById(R.id.image);
        if (mStep != null) {
            if (mIsTablet) {
                rootView.findViewById(R.id.step).setVisibility(View.GONE);
            } else {
                ((TextView) rootView.findViewById(R.id.step)).setText(mStep.getShortDescription());
            }
            ((TextView) rootView.findViewById(R.id.description)).setText(mStep.getDescription());
        }
        if (!TextUtils.isEmpty(mStep.getVideo())) {
            mImageView.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(mStep.getVideo());
            initializePlayer(uri);
        } else if (!TextUtils.isEmpty(mStep.getImage())) {
            mPlayerView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(mStep.getImage()).placeholder(R.drawable.pastries).error(R.drawable.pastries).into(mImageView);
        } else {
            mPlayerView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            mImageView.setImageResource(R.drawable.pastries);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void initializePlayer(Uri videoUri) {
        if (mExoPlayer == null) {
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(getContext(), userAgent)).createMediaSource(videoUri);
            mExoPlayer.prepare(mediaSource);
            if (mCurrentPosition > 0) {
                mExoPlayer.seekTo(mCurrentPosition);
            }
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (mExoPlayer != null) {
            bundle.putBoolean(PLAY_WHEN_READY, mExoPlayer.getPlayWhenReady());
            bundle.putLong(CURRENT_POSITION, mExoPlayer.getCurrentPosition());
        }
    }
}
