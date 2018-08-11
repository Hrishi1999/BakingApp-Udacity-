package gridentertainment.net.bakingapp;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Objects;

import gridentertainment.net.bakingapp.Models.Ingredients;
import gridentertainment.net.bakingapp.Models.Steps;


public class StepDetailFragment extends Fragment {

    PlayerView playerView;
    SimpleExoPlayer player;
    TextView descTv;
    TextView stepTv;
    ImageView imageView;
    ArrayList<Steps> steps;

    String description;
    String img;
    String video_url;
    String short_description;
    int currentPosition;
    long player_pos;
    boolean isPlaying;
    boolean isTablet;

    FragmentListener listener;

    public StepDetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        isTablet = getResources().getBoolean(R.bool.isTablet);

        playerView = rootView.findViewById(R.id.video_view);
        descTv = rootView.findViewById(R.id.tv_desc);
        imageView = rootView.findViewById(R.id.imageViewStep);
        stepTv = rootView.findViewById(R.id.stepTv);

        final FloatingActionButton forward_button = rootView.findViewById(R.id.fltForwardButton);
        final FloatingActionButton backbutton = rootView.findViewById(R.id.fltBackButton);

        if(savedInstanceState == null)
        {
            Bundle bundle = getArguments();
            steps = bundle.getParcelableArrayList("steps");
            currentPosition = bundle.getInt("index");
            description = steps.get(currentPosition).getDescription();
            short_description = steps.get(currentPosition).getShortDescription();
            img = steps.get(currentPosition).getThumbnailURL();
            video_url = steps.get(currentPosition).getVideoURL();
        }
        else {
            steps = savedInstanceState.getParcelableArrayList("steps");
            currentPosition = savedInstanceState.getInt("index");
            description = steps.get(currentPosition).getDescription();
            short_description = steps.get(currentPosition).getShortDescription();
            img = steps.get(currentPosition).getThumbnailURL();
            video_url = steps.get(currentPosition).getVideoURL();
            player_pos = savedInstanceState.getLong("player_pos");
            isPlaying = savedInstanceState.getBoolean("player_state");
        }

        final int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet)
        {
            if(!TextUtils.isEmpty(video_url))
            {
                ViewGroup.LayoutParams params = playerView.getLayoutParams();
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                playerView.setLayoutParams(params);
                descTv.setVisibility(View.GONE);
                stepTv.setVisibility(View.GONE);
                backbutton.setVisibility(View.GONE);
                forward_button.setVisibility(View.GONE);
                hideSystemUi();
            }
        }

        updateViews(video_url, img);
        if(!Objects.isNull(player_pos) && !TextUtils.isEmpty(video_url))
        {
            player.setPlayWhenReady(isPlaying);
            player.seekTo(player_pos);
        }
        stepTv.setText(short_description);

        if(!TextUtils.isEmpty(video_url))
        {
            player.addListener(new Player.DefaultEventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playWhenReady && playbackState == Player.STATE_READY) {
                        isPlaying = true;
                    }
                    else {
                        isPlaying = false;
                    }
                }
            });
        }

        backbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(currentPosition!=0)
                {
                    releasePlayer();
                    descTv.setText(steps.get(currentPosition-1).getDescription());
                    stepTv.setText(steps.get(currentPosition-1).getShortDescription());
                    updateViews(steps.get(currentPosition-1).getVideoURL(),steps.get(currentPosition-1).getThumbnailURL());
                    currentPosition=currentPosition-1;
                    forward_button.setEnabled(true);
                }
                else
                {
                    backbutton.setEnabled(false);
                }
            }
        });

        forward_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(currentPosition!=steps.size()-1)
                {
                    releasePlayer();
                    descTv.setText(steps.get(currentPosition+1).getDescription());
                    stepTv.setText(steps.get(currentPosition+1).getShortDescription());
                    updateViews(steps.get(currentPosition+1).getVideoURL(),steps.get(currentPosition+1).getThumbnailURL());
                    currentPosition = currentPosition+1;
                    backbutton.setEnabled(true);
                }
                else
                {
                    forward_button.setEnabled(false);
                }
            }
        });

        return rootView;
        }

        @Override
        public void onStart(){
            super.onStart();
            descTv.setText(description);
        }

        public void initializePlayer(Uri uri) {
            if (player == null) {
                player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                        new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(player);
            player.setPlayWhenReady(true);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);

            ViewGroup.LayoutParams params = playerView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            playerView.setLayoutParams(params);
        }
    }

    private void updateViews(String video, String image)
    {
        if(!TextUtils.isEmpty(video_url))
        {
            initializePlayer(Uri.parse(video));
            imageView.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
        }
        else if (!TextUtils.isEmpty(img)) {
            playerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(image)
                    .placeholder(R.color.colorAccent)
                    .error(R.color.colorAccent)
                    .into(imageView);
        }
        else
        {
            playerView.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.placeholder);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            playerView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height=600;
            playerView.setLayoutParams(params);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("steps", steps);
        outState.putInt("index",currentPosition);

        try
        {
            outState.putLong("player_pos", player.getCurrentPosition());
            outState.putBoolean("player_state", isPlaying);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setFragmentListener(FragmentListener listener) {
        this.listener = listener;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer(Uri.parse(video_url));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        if(isTablet)
        {
            return;
        }
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        imageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }


}
