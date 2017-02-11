package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;

/**
 * Author:李烽
 * Date:2016-05-04
 * FIXME
 * Todo 倒计时文字和进度条
 */
public class CountDownView_1 extends LinearLayout {
    private static final String TAG = "com.hxqc.mall.thirdshop.views.CountDownView";
    private static final String TEXT_NORMAL = "剩余时间：";
    private static final String TEXT_OVER_TIME = "活动已结束";
    private static final String TEXT_NOT_START = "距活动开始：";
    private final TextView timeText;//倒计时的字
    private final View progressView;
    private final TextView label;//前面的字
    private final LinearLayout timeLayout;
    private LinearLayout progressLayout;
    private static int weight = 0;

    private long endTime = 0;
//    private long now = 0;
    private long startTime = 0;
    private boolean isActivate = true;

    public boolean isRunning() {
        return isRunning;
    }

    private boolean isRunning = false;//记录运行状态

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int width = progressLayout.getWidth();
//        DebugLog.i(TAG, "width:" + width);
        ViewGroup.LayoutParams layoutParams = progressView.getLayoutParams();
        layoutParams.width = width;
        progressView.setLayoutParams(layoutParams);
    }

    /**
     * 更新时间
     */
    private void countdownShow() {
        long now = System.currentTimeMillis();
//        DebugLog.i(TAG, "现在：" + now + "开始时间：" + startTime + "结束时间：" + endTime);
        String timeText;
        long difference;
        if (startTime > now && endTime > now) {
            //未开始
//            DebugLog.i(TAG, "未开始");
            weight = 100;
            timeLayout.setVisibility(VISIBLE);
            label.setText(TEXT_NOT_START);
            difference = startTime - now;
            timeText = getTimeText(difference);
        } else if (startTime < now && endTime > now) {
            //正常计时
//            DebugLog.i(TAG, "正常计时");
            timeLayout.setVisibility(VISIBLE);
            label.setText(TEXT_NORMAL);
            difference = endTime - now;
            weight = 100 - (int) (difference * 100 / (endTime - startTime));
            timeText = getTimeText(difference);
        } else if (startTime < now && endTime < now) {
            //已过期
//            DebugLog.i(TAG, "已过期");
            timeLayout.setVisibility(GONE);
            label.setText(TEXT_OVER_TIME);
            timeText = "已过期";
            isActivate = false;
            isRunning = false;
        } else {
//            DebugLog.i(TAG, "其他情况");
            timeText = "";
            isActivate = false;
            isRunning = false;
        }
        LayoutParams layoutParams = (LayoutParams) progressView.getLayoutParams();
        layoutParams.weight = weight;
        progressView.setLayoutParams(layoutParams);
        this.timeText.setText(timeText);
//        now++;
        mHandler.postDelayed(r, 999);
    }


    public CountDownView_1(Context context) {
        this(context, null);
    }

    public CountDownView_1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView_1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_count_down, this);
        progressLayout = (LinearLayout) findViewById(R.id.progress_layout);
        progressView = findViewById(R.id.progress_view);
        timeText = (TextView) findViewById(R.id.time_text);
        label = (TextView) findViewById(R.id.label);
        timeLayout = (LinearLayout) findViewById(R.id.time_layout);
    }


    public void setStartAndEndTime(long startTime,  long endTime) {
        if (this.startTime == 0)
            this.startTime = startTime;
//        if (this.now == 0)
//            this.now = now;
        if (this.endTime == 0)
            this.endTime = endTime;
    }


    /**
     * 开始倒计时
     */
    public void countdownStart() {
        r.run();
        isRunning = true;
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
        isActivate = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }

    Handler mHandler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            if (isActivate)
                countdownShow();
        }
    };
}
