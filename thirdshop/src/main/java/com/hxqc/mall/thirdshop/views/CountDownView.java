package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.util.DebugLog;

/**
 * Author:李烽
 * Date:2016-05-04
 * FIXME
 * Todo 倒计时文字和进度条
 */
@Deprecated
public class CountDownView extends LinearLayout {
    private static final int COUNTDOWN = 0;//倒计时的handler信息
    private static final String TAG = "com.hxqc.mall.thirdshop.views.CountDownView";
    private final TextView timetext;
    private final View progressview;
    private LinearLayout progresslayout;
    private static int weight = 0;
    //    private Timer mTimer;

    //    Handler mHandler =
//            new Handler(new Handler.Callback() {
//                @Override
//                public boolean handleMessage(Message msg) {
//                    if (msg.what == COUNTDOWN) {
//                        countdownShow();
//                        countdownStart();
//                    }
//                    return false;
//                }
//            });
//    Handler mTimeHandler = new Handler();
    private static long endTime;
    private static long startTime;
    private static boolean isActivate = true;
    private Runnable runnable;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int width = progresslayout.getWidth();
//        DebugLog.i(TAG, "width:" + width);
        ViewGroup.LayoutParams layoutParams = progressview.getLayoutParams();
        layoutParams.width = width;
        progressview.setLayoutParams(layoutParams);
    }

    /**
     * 更新时间
     */
    public void countdownShow() {
        long now = System.currentTimeMillis();
        DebugLog.i(TAG, "现在：" + now + "开始时间：" + startTime + "结束时间：" + endTime);
        String timeText;
        long difference;
        if (startTime > now && endTime > now) {
            //未开始
            DebugLog.i(TAG, "未开始");
            weight = 100;
//            if (mTimer != null) {
//                mTimer.cancel();
//                mTimer = null;
//            }

            timeText = "未开始";
        } else if (startTime < now && endTime > now) {
            //正常计时
            DebugLog.i(TAG, "正常计时");
            difference = endTime - now;
            weight = 100 - (int) (difference * 100 / (endTime - startTime));
            timeText = getTimeText(difference);
        } else if (startTime < now && endTime < now) {
            //已过期
            DebugLog.i(TAG, "已过期");
//            if (mTimer != null) {
//                mTimer.cancel();
//                mTimer = null;
//            }
            timeText = "已过期";
            isActivate = false;
        } else {
            DebugLog.i(TAG, "其他情况");
//            if (mTimer != null) {
//                mTimer.cancel();
//                mTimer = null;
//            }
            timeText = "";
            isActivate = false;
        }

        LinearLayout.LayoutParams layoutParams = (LayoutParams) progressview.getLayoutParams();
        layoutParams.weight = weight;
        progressview.setLayoutParams(layoutParams);

        timetext.setText(timeText);

    }


    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_count_down, this);
        progresslayout = (LinearLayout) findViewById(R.id.progress_layout);
        progressview = findViewById(R.id.progress_view);
        timetext = (TextView) findViewById(R.id.time_text);
//        mHandler = new MyHandler();
//        mTimeHandler = new MyHandler();
//        runnable = new MyRunnable();
    }


    public void setStartAndEndTime(long startTime, long endTime) {
        DebugLog.e(startTime + "", endTime + "");
        this.startTime = startTime;
        this.endTime = endTime;
        countdownStart();
    }


    /**
     * 开始倒计时
     */
    private void countdownStart() {
//        if (mTimer != null) {
//            return;
//        }
//        mTimer = new Timer();
//        mTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                mHandler.sendEmptyMessage(COUNTDOWN);
//            }
//        }, 0, 1000);

//        if (isActivate)
//            mTimeHandler.postDelayed(runnable, 1000);
    }


    /**
     * 毫秒转化
     *
     * @param ms
     * @return
     */
    private static String getTimeText(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        String dayStr = day < 10 ? "0" + day : "" + day;
        String hourStr = hour < 10 ? "0" + hour : "" + hour;
        String minuteStr = minute < 10 ? "0" + minute : "" + minute;
        String secondStr = second < 10 ? "0" + second : "" + second;

        return dayStr + "天" + hourStr + "时" + minuteStr + "分" + secondStr + "秒";
    }

    public void onDestroy() {
//        mTimeHandler.removeCallbacks(runnable);
//        mHandler.removeMessages(COUNTDOWN);
//        runnable = null;
//        mTimeHandler = null;
//        mHandler = null;
    }

//    class MyRunnable implements Runnable {
//        @Override
//        public void run() {
//            mHandler.sendEmptyMessage(COUNTDOWN);
//        }
//    }

//    class MyHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == COUNTDOWN) {
//                countdownShow();
//                countdownStart();
//            }
//        }
//    }
}
