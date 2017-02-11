package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Author:李烽
 * Date:2016-05-16
 * FIXME
 * Todo 倒计时文字和进度条
 */
@Deprecated
public class CountDownProgress extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "CountDownProgress";
    private SurfaceHolder mHolder;
    private Thread mThread;

    private Canvas mCanvas;
    private Paint mBarPaint;
    private Paint mTextPaint;

    private int mWidth, mHeight;

    private static int barColor = Color.parseColor("#FBB329");
    private static int defalutBarColor = Color.parseColor("#d5d5d5");
    private static int textColor = Color.parseColor("#333333");

    private int backColor = Color.WHITE;
    private boolean isActivate = true;

    private String textStr = "未开始";
    private float textSize = converTextSize(12), barWidth = converTextSize(1), barLength;


    private long startTime = 0;
    private long endTime = 0;
    private static int weight = 0;


    public CountDownProgress(Context context) {
        this(context, null);
    }

    public CountDownProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mThread = new Thread(this);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mBarPaint = new Paint();
        mBarPaint.setColor(defalutBarColor);
        mBarPaint.setAntiAlias(true);
        mBarPaint.setStrokeWidth(barWidth);

        mBarPaint.setTextSize(textSize);
        barLength = mBarPaint.measureText("00天00时00分00秒");

        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
    }

    /**
     * 开始计时
     */
    public void setStartAndEndTime(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void countdownShow() {
        if (startTime == 0 || endTime == 0)
            return;
        long now = System.currentTimeMillis();
//        DebugLog.i(TAG, "现在：" + now + "开始时间：" + startTime + "结束时间：" + endTime);
        long difference;
        if (startTime > now && endTime > now) {
            //未开始
            weight = 100;
            textStr = "未开始";
        } else if (startTime < now && endTime > now) {
            //正常计时
//            DebugLog.i(TAG, "正常计时");
            difference = endTime - now;
            weight = 100 - (int) (difference * 100 / (endTime - startTime));
            textStr = getTimeText(difference);
        } else if (startTime < now && endTime < now) {
            //已过期
//            DebugLog.i(TAG, "已过期");
            textStr = "已过期";
            isActivate = false;
        } else {
//            DebugLog.i(TAG, "其他情况");
            textStr = "";
            isActivate = false;
        }
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(backColor);

            writeText();

            drawBar();
        } catch (Exception e) {
        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);
        }

    }

    private void drawBar() {
        int proWidth = (int) (weight * barLength / 100);//进度
        float lineHeight = mHeight - barWidth-10;
//        DebugLog.i(TAG, "proWidth:" + proWidth);
        float startX = mWidth / 2 - barLength / 2;
        float Y = lineHeight;

        float endX = startX + barLength;

        mBarPaint.setColor(barColor);
        mCanvas.drawLine(startX, Y, startX + proWidth, Y, mBarPaint);
        mBarPaint.setColor(defalutBarColor);
        mCanvas.drawLine(startX + proWidth, Y, endX, Y, mBarPaint);

    }

    private void writeText() {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float measureText = mTextPaint.measureText(textStr);
        float posX = (mWidth - measureText) / 2;
        float posY = mHeight / 2 + fontHeight / 4;
        mCanvas.drawText(textStr, posX, posY, mTextPaint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHeight = getHeight();
        mWidth = getWidth();
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        mHolder.removeCallback(this);
//        mHolder = null;
//        mThread = null;

    }

    @Override
    public void run() {
        while (isActivate) {
            countdownShow();
            draw();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    /**
     * 将sp转化为像素
     *
     * @param textSize
     * @return
     */
    private float converTextSize(float textSize) {
        Context c = getContext();
        Resources r;
        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();
        float size = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, textSize, r.getDisplayMetrics());
        return size;
    }
}
