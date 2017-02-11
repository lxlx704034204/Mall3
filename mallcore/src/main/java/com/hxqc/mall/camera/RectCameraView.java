package com.hxqc.mall.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hxqc.util.DisplayTools;

/**
 * 说明:证件照矩形框
 *
 * @author: 吕飞
 * @since: 2016-10-31
 * Copyright:恒信汽车电子商务有限公司
 */
public class RectCameraView extends ImageView {
    public int mScreenWidth;
    public int mScreenHeight;
    Paint mPaint;
    Paint mCornerPaint;
    RectF mRectF;
    public float mRectWidth;
    public float mRectHeight;
    float length1 = DisplayTools.dip2px(getContext(), 145);
    float length2 = DisplayTools.dip2px(getContext(), 16);

    public RectCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getScreenSize(getContext());
        initView();
    }

    private void getScreenSize(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
        mRectWidth = (float) (mScreenWidth * 0.85);
        if (((RectCameraActivity) context).mType > 300) {
            mRectHeight = mRectWidth * 54 / 85;
        } else {
            mRectHeight = mRectWidth * 66 / 95;
        }
    }

    private void initView() {
        mPaint = new Paint();
        mCornerPaint = new Paint();
        mPaint.setAlpha(50);
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);// 防抖动
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);// 空心
        mRectF = new RectF((mScreenWidth - mRectWidth) / 2, length1, mScreenWidth - (mScreenWidth - mRectWidth) / 2, length1 + mRectHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(mRectF, mPaint);
        mCornerPaint.setStrokeWidth(4);
        mCornerPaint.setColor(Color.GREEN);
        canvas.drawLine((mScreenWidth - mRectWidth) / 2, length1, (mScreenWidth - mRectWidth) / 2 + length2, length1, mCornerPaint);
        canvas.drawLine((mScreenWidth - mRectWidth) / 2, length1, (mScreenWidth - mRectWidth) / 2, length1 + length2, mCornerPaint);
        canvas.drawLine((mScreenWidth - mRectWidth) / 2, length1 + mRectHeight, (mScreenWidth - mRectWidth) / 2 + length2, length1 + mRectHeight, mCornerPaint);
        canvas.drawLine((mScreenWidth - mRectWidth) / 2, length1 + mRectHeight, (mScreenWidth - mRectWidth) / 2, length1 + mRectHeight - length2, mCornerPaint);
        canvas.drawLine((mScreenWidth - mRectWidth) / 2 + mRectWidth, length1, (mScreenWidth - mRectWidth) / 2 + mRectWidth - length2, length1, mCornerPaint);
        canvas.drawLine((mScreenWidth - mRectWidth) / 2 + mRectWidth, length1, (mScreenWidth - mRectWidth) / 2 + mRectWidth, length1 + length2, mCornerPaint);
        canvas.drawLine((mScreenWidth - mRectWidth) / 2 + mRectWidth, length1 + mRectHeight, (mScreenWidth - mRectWidth) / 2 + mRectWidth - length2, length1 + mRectHeight, mCornerPaint);
        canvas.drawLine((mScreenWidth - mRectWidth) / 2 + mRectWidth, length1 + mRectHeight, (mScreenWidth - mRectWidth) / 2 + mRectWidth, length1 + mRectHeight - length2, mCornerPaint);
    }
}
