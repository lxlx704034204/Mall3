package com.hxqc.pay.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hxqc.util.DebugLog;


/**
 * Author :liukechong
 * Date : 2015-11-06
 * FIXME
 * Todo
 */
public class ImageCoverProgress extends View {
    private static final String TAG = "ImageCoverProgress";

    public ImageCoverProgress(Context context) {
        super(context);
    }

    public ImageCoverProgress(Context context, AttributeSet attrs) {
        super(context, attrs);


    }


    public ImageCoverProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint mPaint;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mPaint = new Paint();
        mPaint.setColor(0xaaaaaaaa);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {


        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        mPaint.setStrokeWidth(getWidth());
        if (max == 1) {
            radio = getProgress() * 1.0f / getMax();
        }
        if (radio >= 0 && radio <= 1) {
            canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight() * (1 - radio), mPaint);
            DebugLog.d(TAG, "onDraw() returned: " + getHeight() * (1 - radio));
        }
        canvas.restore();

    }

    private int progress = 1;
    private int max = 1;
    private float radio = 1.0f;

    public synchronized float getRadio() {
        return radio;
    }

    public synchronized void setRadio(float radio) {
        this.radio = radio;
        invalidate();
    }

    public synchronized void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public synchronized int getProgress() {
        return progress;
    }

    public synchronized void setMax(int max) {
        this.max = max;
    }

    public synchronized int getMax() {
        return max;
    }
}
