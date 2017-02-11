package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.views.autotext.AutofitTextView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.SpecialCarDetailMainActivity;

/**
 * Function: 第三方店铺特卖车辆详情 倒计时控件
 *
 * @author 袁秉勇
 * @since 2016年05月05日
 */
public class TimeCounterView extends LinearLayout implements Runnable {
    private final static String TAG = TimeCounterView.class.getSimpleName();
    private Context mContext;

    private TextView mTimeDayView;
    private AutofitTextView mTimeHourView, mTimeMinView, mTimeSecondView;

    private LinearLayout mTimeDetailView;

    private long[] times;
    private long mday, mhour, mmin, msecond;// 天，小时，分钟，秒
    private boolean run = false; // 是否启动了

    private boolean stopCount = false;
    private boolean syncTimer = false;

    private boolean isStarted;
    private CallBack callBack;


    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    public void setSyncTimer(boolean syncTimer) {
        this.syncTimer = syncTimer;
        if (syncTimer) syncTime();
    }


    public TimeCounterView(Context context) {
        this(context, null);
    }


    public TimeCounterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public TimeCounterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initView(context);
    }


    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_time_counter, this);

        mTimeDayView = (TextView) this.findViewById(R.id.time_day);
        mTimeDetailView = (LinearLayout) this.findViewById(R.id.time_desc);
        mTimeHourView = (AutofitTextView) this.findViewById(R.id.time_hour);
        mTimeMinView = (AutofitTextView) this.findViewById(R.id.time_min);
        mTimeSecondView = (AutofitTextView) this.findViewById(R.id.time_second);
    }


    public void setDate(boolean isStarted, long time) {
        this.isStarted = isStarted;
        this.setTimes(getTimes(time));
    }


    public long[] getTimes(long time) {
        int DAY = 60 * 60 * 24;
        int HOUR = 60 * 60;
        int MINUTE = 60;

        long day = time / DAY;
        time = time - day * DAY;
        long hour = time / HOUR;
        time = time - hour * HOUR;
        long minute = time / MINUTE;
        time = time - minute * MINUTE;
        long[] times = new long[]{day, hour, minute, time};

        return times;
    }


    private void setTimes(long[] times) {
        this.times = times;
        mday = times[0];
        mhour = times[1];
        mmin = times[2];
        msecond = times[3];
    }


    /** 倒计时核心 **/
    private void ComputeTime() {
        msecond--;
        if (msecond < 0) {
            mmin--;
            msecond = 59;
            if (mmin < 0) {
                mmin = 59;
                mhour--;
                if (mhour < 0) {
                    mhour = 59;
                    mday--;
                }
            }
        }
    }


    public boolean isRun() {
        return run;
    }


    public void setRun(boolean run) {
        this.run = run;
    }


    @Override
    public void run() {
        if (stopCount) return;

        // 标示已经启动
        run = true;
        ComputeTime();
        if (mday < 0) {
            if (!isStarted) {
                if (callBack != null) callBack.callBack();
                showStartStyle();
            } else {
                showEndStyle();
            }
            return;
        }

        /** 同步时间 **/
        if (syncTimer) {
            syncTime();
        }

        if (isStarted) {
            mTimeDayView.setText("距离结束" + mday + "天");
        } else {
            mTimeDayView.setText("距离开始" + mday + "天");
        }

        mTimeHourView.setText(mhour < 10 ? "0" + mhour : mhour + "");
        mTimeMinView.setText(mmin < 10 ? "0" + mmin : mmin + "");
        mTimeSecondView.setText(msecond < 10 ? "0" + msecond : msecond + "");
        postDelayed(this, 1000);
    }


    public void showEndStyle() {
        mTimeDayView.setText("活动已结束");
        mTimeDayView.setTextColor(getResources().getColor(R.color.text_gray));
        mTimeDetailView.setVisibility(GONE);
        run = false;
    }

    public void showStartStyle() {
        mTimeDayView.setText("活动已开始");
        mTimeDetailView.setVisibility(GONE);
        run = false;
    }


    /** 销毁实例防止内存泄漏 **/
    public void stopCount() {
        stopCount = true;
    }

    private void syncTime() {
        if (mContext instanceof SpecialCarDetailMainActivity) {
            SpecialCarDetailMainActivity.TIMER = mday * 24 * 60 * 60 + mhour * 60 * 60 + mmin * 60 + msecond;
        }
    }

    public interface CallBack {
        void callBack();
    }
}
