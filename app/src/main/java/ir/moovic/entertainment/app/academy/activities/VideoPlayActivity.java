package ir.moovic.entertainment.app.academy.activities;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;
import ir.moovic.entertainment.controller.AppConfig;

public class VideoPlayActivity extends MyBaseActivity {


    MediaController mController;
    boolean alwaysLandscape = true;

    Uri uri;
    String video;
    @BindView(R.id.vp)
    VideoView VideoPlayer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        setLandscape(alwaysLandscape);
        ButterKnife.bind(this);
        initVideo();
    }

    private void setLandscape(boolean isLandscape) {
        if (isLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    private void initVideo() {
        if (getIntent().hasExtra("video")) {

            video = getIntent().getStringExtra("video");

            if (!TextUtils.isEmpty(video)) {

                this.uri = Uri.parse(AppConfig.mediaUrl(video));
            }
        }
        VideoPlayer.bringToFront();
        VideoPlayer.setVideoURI(uri);
        mController = new FullScreenMediaController(this);
        mController.setAnchorView(VideoPlayer);
        VideoPlayer.start();
        mController.setMediaPlayer(new MediaController.MediaPlayerControl() {


            @Override
            public void start() {
                VideoPlayer.start();
            }

            @Override
            public void pause() {
                VideoPlayer.pause();
            }

            @Override
            public int getDuration() {
                return VideoPlayer.getDuration();
            }

            @Override
            public int getCurrentPosition() {
                return VideoPlayer.getCurrentPosition();
            }

            @Override
            public void seekTo(int pos) {

            }

            @Override
            public boolean isPlaying() {
                return VideoPlayer.isPlaying();
            }

            @Override
            public int getBufferPercentage() {
                return 0;
            }

            @Override
            public boolean canPause() {
                return true;
            }

            @Override
            public boolean canSeekBackward() {
                return true;
            }

            @Override
            public boolean canSeekForward() {
                return true;
            }

            @Override
            public int getAudioSessionId() {
                return 0;
            }
        });
        VideoPlayer.setMediaController(mController);
    }


    public class FullScreenMediaController extends MediaController {

        private AppCompatImageButton fullScreen;

        WeakReference<VideoPlayActivity> ref;
        VideoPlayActivity activity;

        public FullScreenMediaController(VideoPlayActivity activity) {
            super(activity);
            this.ref = new WeakReference<>(activity);

        }

        @Override
        public void setAnchorView(View view) {

            super.setAnchorView(view);

            //image button for full screen to be added to media controller
            fullScreen = new AppCompatImageButton(super.getContext());

            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT;
            params.rightMargin = 80;
            params.topMargin=25;

            //fullscreen indicator
            if (!alwaysLandscape){
            addView(fullScreen, params);
            setFullScreen(alwaysLandscape);
            }

            //add listener to image button to handle full screen and exit full screen events
            fullScreen.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity = ref.get();
                    activity.setLandscape(!activity.alwaysLandscape);


                    if (!activity.alwaysLandscape) activity.alwaysLandscape = true;
                    else activity.alwaysLandscape = false;

                    setFullScreen(activity.alwaysLandscape);
                }
            });

        }


        private void setFullScreen(boolean isLandscape){

            if (isLandscape) {
                Glide.with(fullScreen)
                        .load(R.drawable.ic_maximize)
                        .into(fullScreen);

            } else {
                Glide.with(fullScreen)
                        .load(R.drawable.ic_full_screen_exit)
                        .into(fullScreen);

            }
            fullScreen.setAlpha(.5f);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mController.show();
        return false;
    }

}
