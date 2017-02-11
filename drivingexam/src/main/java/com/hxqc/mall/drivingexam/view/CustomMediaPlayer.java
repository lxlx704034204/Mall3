package com.hxqc.mall.drivingexam.view;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.drivingexam.ui.dialog.CommonDialog;
import com.hxqc.mall.drivingexam.utils.ActivitySwitcherExam;
import com.hxqc.mall.drivingexam.utils.NetWorkUtil;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhaofan on 2016/8/22.
 */
public class CustomMediaPlayer extends LinearLayout implements View.OnClickListener {
    public static final String DOWNLOAD_MEDIA = "download_media";
    private VideoView videoView;
    private TextView mTotalTime, mNowTime, mProgressText;
    private ImageView mStartBtn, thumbImg, fullScreenBtn;
    private ImageView downloadBtn, PauseBtn, shadow;
    private ProgressBar loading;
    private SeekBar seekBar;
    boolean isStart = false;
    private String filePath, fileName;
    String fullPath;
    private Context context;

    private CircleProgressBar mBar;
    private boolean isDownload = false;

    public CustomMediaPlayer(Context context) {
        this(context, null);
    }

    public CustomMediaPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomMediaPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }


    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_mediaplayer, this);
        videoView = (VideoView) findViewById(R.id.vidio_view);
        mTotalTime = (TextView) findViewById(R.id.total_time);
        mNowTime = (TextView) findViewById(R.id.now_time);
        mStartBtn = (ImageView) findViewById(R.id.start);
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        thumbImg = (ImageView) findViewById(R.id.thumb_img);
        fullScreenBtn = (ImageView) findViewById(R.id.fullscreen);
        downloadBtn = (ImageView) findViewById(R.id.download);
        PauseBtn = (ImageView) findViewById(R.id.pause);
        loading = (ProgressBar) findViewById(R.id.load_progress);
        shadow = (ImageView) findViewById(R.id.shadow);
        mProgressText = (TextView) findViewById(R.id.progress_text);

        bindListener();

        mBar = (CircleProgressBar) findViewById(R.id.myProgress);
    }

    private void bindListener() {
        mStartBtn.setOnClickListener(this);
        fullScreenBtn.setOnClickListener(this);
        shadow.setOnClickListener(this);
        downloadBtn.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(mSeekChange);
    }

    Handler handler = new Handler();

    public void setMediaPlayer(String filePath, String fileName) {
        downloadBtn.setVisibility(View.GONE);
        mBar.setVisibility(GONE);
        shadow.setVisibility(GONE);
        mProgressText.setVisibility(GONE);
        isDownload = false;
        this.filePath = filePath;
        this.fileName = fileName;
        fullPath = filePath + "/" + fileName;
        videoView.setVideoPath(fullPath);
        videoView.pause();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                DebugLog.i("通知", " 播放结束");
                seekBar.setProgress(0);
                videoView.seekTo(0);
                videoView.pause();
                mediaStop();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mediaStop();
                downloadBtn.setVisibility(View.VISIBLE);
                downloadBtn.setImageResource(R.drawable.xiazai);
                shadow.setVisibility(VISIBLE);
                return false;
            }
        });

        handler.postDelayed(run_scroll_up, 0);
    }

    Runnable run_scroll_up = new Runnable() {
        public void run() {
            handler.postDelayed(this, 1000);
            int currentPosition = videoView.getCurrentPosition();
            int duration = videoView.getDuration();
            //    DebugLog.i("videoView", videoView.getCurrentPosition() + "  " + videoView.getDuration());
            mTotalTime.setText("/" + figureTime(duration));
            mNowTime.setText(figureTime(currentPosition));

            int a = seekBar.getProgress();
            int pro = duration <= 0 ? 0 : (int) (100f * (currentPosition / (float) duration));
            if (videoView.isPlaying())
                mediaStart();
            //  seekBar.setProgress(a > pro ? a + 1 : pro);
            seekBar.setProgress(pro);
            mStartBtn.setImageResource(videoView.isPlaying() ? R.drawable.stop : R.drawable.start);

        }
    };


    /**
     * 转换时间
     */
    private String figureTime(long millis) {
        long m, s;
        m = millis / (60 * 1000);
        s = (millis / 1000) - m * 60;
        return (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
    }


    private SeekBar.OnSeekBarChangeListener mSeekChange = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            DebugLog.i("onProgressChanged", progress + " " + fromUser);
            if (fromUser) {
                videoView.seekTo((int) ((progress) / 100f * (videoView.getDuration())));
                videoView.start();
                mediaStart();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    public void mediaStop() {
        isStart = false;
        thumbImg.setVisibility(View.VISIBLE);
    }

    public void mediaStart() {
        isStart = true;
        if (thumbImg.isShown())
            thumbImg.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        //播放
        if (id == R.id.start) {
            if (!isStart) {
                mediaStart();
            }
            if (!videoView.isPlaying()) {
                videoView.start();
                mStartBtn.setImageResource(R.drawable.stop);
            } else {
                videoView.pause();
                mStartBtn.setImageResource(R.drawable.start);
            }
        }
        //全屏
        else if (id == R.id.fullscreen) {
            ActivitySwitcherExam.toVideoviewFullscreen(context,fullPath,videoView.getCurrentPosition());
        }
        //下载
        else if (id == R.id.download) {
            if (isDownload)
                return;
            if (NetWorkUtil.isWifiConnected(context))
                download();
            else
                ShowDialog();
        }
    }

    public void download() {
        isDownload = true;
        downloadBtn.setImageResource(R.drawable.xiazaizhong);
        mBar.setVisibility(View.VISIBLE);
        shadow.setVisibility(View.VISIBLE);
        mProgressText.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new Event("", DOWNLOAD_MEDIA));

    }

    private void ShowDialog() {
        CommonDialog mDialog = CommonDialog.getInstance((Activity) context);
        mDialog.setTitle("提示").setContent("当前网络环境为2G/3G/4G，你确定继续下载视频么？")
                .setLeftButton("暂不下载", null)
                .setRightButton("继续下载", new CommonDialog.RightBtnClickListener() {
                    @Override
                    public void onRightBtnClickListener(View v) {
                        download();
                    }
                })
                .show("");
    }


    public void pause() {
        if (videoView != null) {
            videoView.pause();
        }

    }

    public void start() {
        if (videoView != null) {
            videoView.start();
        }

    }

    public void seekTo(int obj) {
        if (videoView != null) {
            videoView.seekTo(obj);
        }
    }


    public void cancel() {
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }



    public int getCurrentPosition() {
        return videoView.getCurrentPosition();
    }

    public void setProgress(int pro, String currentStr, String totalStr) {
        mBar.setProgress(pro, null);
        mProgressText.setText(currentStr + "/" + totalStr);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DebugLog.e("media palyer", "onDetachedFromWindow");
        handler.removeCallbacksAndMessages(null);
        videoView.stopPlayback();
    }
}
