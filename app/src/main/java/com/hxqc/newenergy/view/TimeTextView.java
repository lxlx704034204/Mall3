package com.hxqc.newenergy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.util.DateUtil;
import com.hxqc.util.DebugLog;

import hxqc.mall.R;

/**
 * 说明:
 * author: 何玉
 * since: 2016/3/25.
 * Copyright:恒信汽车电子商务有限公司
 */
public class TimeTextView extends TextView implements Runnable {
    //    Paint mPaint; //画笔,包含了画几何图形、文本等的样式和颜色信息
    private long[] times;
    private long mday, mhour, mmin, msecond;//天，小时，分钟，秒
    private boolean run = false; //是否启动了
//    public String state;


    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public TimeTextView(Context context) {
        super(context);
    }

    public long[] getTimes() {
        return times;
    }

    public void setTimes(long[] times) {
        this.times = times;
        mday = times[0];
        mhour = times[1];
        mmin = times[2];
        msecond = times[3];
    }

    /**
     * 倒计时计算
     */
    private synchronized void computeTime() {
        msecond--;
        if (msecond < 0) {
            mmin--;
            msecond = 59;
            if (mmin < 0) {
                mmin = 59;
                mhour--;
                if (mhour < 0) {
                    // 倒计时结束
                    mhour = 59;
                    mday--;
                }
            }
        }
        String strTime = mday + "天" + mhour + "小时" +
                mmin + "分钟" + msecond + "秒";
//            this.setText(Html.fromHtml(strTime));
        this.setText(strTime);
        DebugLog.i("Tag", "message  "+this.hashCode());
        postDelayed(this, 1000);
    }

    public boolean isRun() {
        return run;
    }

    public void setEndState() {
        this.setText("活动已结束");
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public void stopRun() {
        run = false;
    }

    @Override
    public void run() {
        //标示已经启动
        if (run) {
            computeTime();
        }

    }


    public int getDealsStatus(long startTime, long ServceTiem, long EndTime) {

        int status = 0;

        if (startTime < ServceTiem && ServceTiem < EndTime) {
            status = 2;
        }
        if (startTime > ServceTiem) {
            status = 1;//活动未开始
        }

        if (startTime < ServceTiem && ServceTiem > EndTime) {
            status = 3;
        }
        return status;
    }


    public void setDate(ImageView imageView, TimeTextView timeTextView, TextView StateText, String StartTime, String ServersTime, String EndTime) {

        long startTime = DateUtil.getTime(StartTime);
        long serversTime = DateUtil.getTime(ServersTime);
        long endTime = DateUtil.getTime(EndTime);

        int state = getDealsStatus(startTime, serversTime, endTime);

        switch (state) {
            case 1:

                imageView.setBackgroundResource(R.drawable.temai_nostart);
                StateText.setText("距离开始时间：");
                long NoStartTime = startTime - serversTime;
                timeTextView.setTimes(getTime(NoStartTime));
                if (!timeTextView.isRun()) {
                    run = true;
                    timeTextView.run();
                }
                break;
            case 2:

                imageView.setBackgroundResource(R.drawable.temai_state);
                long status = endTime - serversTime;
                StateText.setText("剩余时间：");
                timeTextView.setTimes(getTime(status));
                if (!timeTextView.isRun()) {
                    run = true;
                    timeTextView.run();
                }
                break;
            case 3:
                imageView.setBackgroundResource(R.drawable.temai_end);
                StateText.setText("活动已结束");
                break;

        }

    }

    public long[] getTime(long differ) {
        int DAY = 60 * 60 * 24;
        int HOUR = 60 * 60;

        int MINUTE = 60;
        long day = differ / DAY;
        differ = differ - day * DAY;
        long hour = differ / HOUR;
        differ = differ - hour * HOUR;
        long minute = differ / MINUTE;
        differ = differ - minute * MINUTE;
        long[] tim = new long[]{day, hour, minute, differ};


//        return day+"天"+hour+"小时"+minute+"分钟"+differ+"秒";
        return tim;
    }

    public void setTimer(ImageView imageView, TimeTextView timeTextView, TextView StateText, String StertTime, String ServersTime, String EndTime) {

        long starttime[] = getTime(StertTime);
        long serverstime[] = getTime(ServersTime);
        long endtime[] = getTime(EndTime);

        int state = getState(starttime, serverstime, endtime);

        switch (state) {
            case 1:
                imageView.setBackgroundResource(R.drawable.temai_nostart);
                StateText.setText("开始时间");
                timeTextView.setTimes(getTimerTime(serverstime, endtime));
                if (!timeTextView.isRun()) {
                    timeTextView.run();
                }
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.temai_state);

                StateText.setText("剩余时间");
                timeTextView.setTimes(getTimerTime(serverstime, endtime));
                if (!timeTextView.isRun()) {
                    timeTextView.run();
                }
                break;
            case 3:
                imageView.setBackgroundResource(R.drawable.temai_end);
                StateText.setText("活动已结束");
                break;

        }

    }


    /**
     * 计算倒计时活动昨天
     *
     * @param starttime
     *         活动开始时间
     * @param serverstime
     *         服务器当前时间
     * @param endtime
     *         活动结束时间
     * @return 活动状态  1代表活动未开始  2代表活动进行中  3代表活动已结束
     */
    public int getState(long starttime[], long serverstime[], long endtime[]) {
        int state = 0;
        if (starttime[0] < serverstime[0] && serverstime[0] < endtime[0]) {
            state = 2;//活动进行中
        }
        if (starttime[0] > serverstime[0]) {
            state = 1;//活动未开始
        }

        if (starttime[0] < serverstime[0] && serverstime[0] > endtime[0]) {
            state = 3;//活动已结束
        }

        //遍历活动开始时间和服务器时间的值
        for (int i = 0; i < starttime.length; i++) {
            if (starttime[i] < serverstime[i] && serverstime[i] < endtime[i]) {
                state = 1;// 1代表活动未开始
                break;
            } else {
                state = 2;//活动进行中
                break;
            }
        }
        if (state == 2) {
            for (int i = 0; i < serverstime.length; i++) {
                if (serverstime[i] < endtime[i]) {
                    state = 3;
                    break;
                }
            }
        }
        return state;
    }


    /**
     * 将字符串时间转换为数组格式保存 方便计算
     *
     * @param Time
     */
    public long[] getTime(String Time) {
        long time[] = new long[4];
        String temp = Time.substring(5, 7);
        int te = Integer.parseInt(temp.replace("0", ""));
        te = te * 30;
        String day = Time.substring(8, 10);
        String hour = Time.substring(11, 13);
        String mind = Time.substring(14, 16);
        String msecond = Time.substring(17, 19);
        time[0] = Long.valueOf(day) + te;
        time[1] = Long.valueOf(hour);
        time[2] = Long.valueOf(mind);
        time[3] = Long.valueOf(msecond);
        return time;
    }

    /**
     * 计算计时器数据
     *
     * @param time1
     *         开始时间 或者系统当前时间
     * @param time2
     *         系统当前时间或者 活动结束时间
     * @return 计算出两个时间的差值并保存到数组中
     */
    public long[] getTimerTime(long time1[], long time2[]) {
        long time[] = new long[time1.length];
        for (int i = 0; i < time1.length; i++) {
            time[i] = time1[i] > time2[i] ? time1[i] - time2[i] : time2[i] - time1[i];
        }
        return time;
    }
}
