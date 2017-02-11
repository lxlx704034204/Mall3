package com.hxqc.mall.drivingexam.ui.helpyoupass.picturedescribedetail;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.hxqc.mall.core.base.BaseActivity;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.drivingexam.R;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.EventBus;

/**
 * 全屏播放
 * Created by zhaofan on 2016/8/22.
 */
public class VideoviewFullscreenActivity extends BaseActivity {
    public static final String FULL_SCREEN_RESET = "full_screen_reset";
    VideoView videoView;
    int CurrectTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_videoview_fullscreen);
        bindView();
        init();
    }


    public void bindView() {
        videoView = (VideoView) findViewById(R.id.vidio_view);
    }


    public void init() {
        String fullPath = getIntent().getStringExtra("path");
        CurrectTime = getIntent().getIntExtra("CurrentPosition", 0);
        Uri uri = Uri.parse(fullPath);
        videoView.setVideoURI(uri);
        videoView.setMediaController(new MediaController(this));
        videoView.seekTo(CurrectTime);
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                DebugLog.i("通知", " 播放结束");
                videoView.seekTo(0);
                videoView.pause();
            }
        });
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new Event(videoView.getCurrentPosition(), FULL_SCREEN_RESET));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }
}
