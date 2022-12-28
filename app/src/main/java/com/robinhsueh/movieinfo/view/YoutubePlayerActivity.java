package com.robinhsueh.movieinfo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import com.robinhsueh.movieinfo.R;
import com.robinhsueh.movieinfo.model.StaticParameter;
import com.robinhsueh.movieinfo.utils.YtFullScreenHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController;

public class YoutubePlayerActivity extends AppCompatActivity {

    private final String LOG_TAG = "YoutubePlayerActivity";
    private Context context;
    private YouTubePlayerView ytPlayerView;
    private YtFullScreenHelper ytFullScreenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        context = this;

        // Get videoId from intent
        Intent intent = getIntent();
        String videoId = intent.getStringExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_VIDEO_ID_KEY);

        // Initialize Views
        ytPlayerView = findViewById(R.id.youtubePlayerView);

        // Initialize YtFullScreenHelper
        ytFullScreenHelper = new YtFullScreenHelper(this);

        initYouTubePlayerView(videoId);
    }


    /**
     * Initialize YoutubePlayerView
     * @param videoId videoId
     */
    private void initYouTubePlayerView(String videoId) {
        // Set lifecycle observer to ytPlayerView
        getLifecycle().addObserver(ytPlayerView);

        // set player options
        IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                .controls(0)
                .build();

        // Initialize YoutubePlayerView
        ytPlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onStateChange(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                // loop the video
                if (state == PlayerConstants.PlayerState.ENDED) {
                    youTubePlayer.seekTo(0f);
                }
            }

            @Override
            public void onError(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                if(error.equals(PlayerConstants.PlayerError.VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER)){
                    // video can't play in embedded player (age or region restricted)

                }
            }

            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                // using pre-made custom ui
                DefaultPlayerUiController defaultPlayerUiController = new DefaultPlayerUiController(ytPlayerView, youTubePlayer);
                // hide specific menu item
                defaultPlayerUiController.showUi(true).showVideoTitle(true).showSeekBar(true).showDuration(true).showCurrentTime(true).showYouTubeButton(true).showFullscreenButton(false);
                ytPlayerView.setCustomPlayerUi(defaultPlayerUiController.getRootView());

                // load the video and play it
                youTubePlayer.loadVideo(videoId, 0f);
            }
        }, true, iFramePlayerOptions);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ytFullScreenHelper.enterFullScreen();
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            ytFullScreenHelper.exitFullScreen();
        }
    }


}