package com.example.movieinfo.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.movieinfo.BuildConfig;
import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayerActivity extends AppCompatActivity {

    private final String LOG_TAG = "YoutubePlayerActivity";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        context = this;

        // Get videoId from intent
        Intent intent = getIntent();
        String videoId = intent.getStringExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_VIDEO_ID_KEY);

        // Initialize Views
        YouTubePlayerFragment ytPlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubePlayerFragment);

        ytPlayerFragment.initialize(BuildConfig.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoId);
                // enable automatic control of the orientation.
                youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
                // automatically enter fullscreen when the device enters landscape orientation
                youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(context, getString(R.string.label_youtube_player_error_display), Toast.LENGTH_LONG).show();
                Log.d(LOG_TAG, String.format("YouTubePlayerView Initialize Failed! Reason: %s",  youTubeInitializationResult.name()));
            }
        });

    }
}