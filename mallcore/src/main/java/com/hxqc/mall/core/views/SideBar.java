package com.hxqc.mall.core.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


/**
 * 边侧字母导航
 * <p/>
 * author: 胡俊杰
 * since: 2015-07-1
 * Copyright:恒信汽车电子商务有限公司
 */
public class SideBar extends View {
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    public String[] defalutTagString = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public StringBuffer mb2 = new StringBuffer();
    private int mChoose = -1;
    private Paint mPaint = new Paint();
//    private Bitmap mBmp;

    private TextView mTextDialog;

    public String[] sideTag;

    public void setSideTag(String[] sideTag) {
        this.sideTag = sideTag;
        defalutTagString = sideTag;
        invalidate();
    }

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        mBmp = (context.getResources().getDrawable(
//                R.drawable.ic_fire)) != null ? ((BitmapDrawable) context.getResources().getDrawable(
//                R.drawable.ic_fire)).getBitmap() : null;
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
//        mBmp = (context.getResources().getDrawable(
//                R.drawable.ic_fire)) != null ? ((BitmapDrawable) context.getResources().getDrawable(
//                R.drawable.ic_fire)).getBitmap() : null;
    }

    public SideBar(Context context) {
        super(context);
//        mBmp = (context.getResources().getDrawable(
//                R.drawable.ic_fire)) != null ? ((BitmapDrawable) context.getResources().getDrawable(
//                R.drawable.ic_fire)).getBitmap() : null;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (sideTag != null && defalutTagString.length > 0) {

            int height = getHeight() ;
            int width = getWidth();
            int singleHeight = height / defalutTagString.length;
//        if (mChoose >= 1 && mBmp != null) {
//            canvas.drawBitmap(this.mBmp, 8.0F, singleHeight * (mChoose - 1), null);
//        }
//            float xPos1 = width / 2  - 2;
//            float yPos1 = mBmp.getHeight();
//            canvas.drawBitmap(mBmp, xPos1, 0f, mPaint);

            for (int i = 0; i < defalutTagString.length; i++) {
                mPaint.setColor(Color.parseColor("#2095F2"));
                mPaint.setAntiAlias(true);
                mPaint.setTextSize(38);
                float xPos = width / 2 - mPaint.measureText(defalutTagString[i]) / 2 - 2;
                float yPos = singleHeight * i + singleHeight;
                canvas.drawText(defalutTagString[i], xPos, yPos , mPaint);
                mPaint.reset();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (sideTag == null) return false;
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = mChoose;

        final int c = (int) (y / getHeight() * (defalutTagString.length + 1));

        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mChoose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < defalutTagString.length) {
                        if (onTouchingLetterChangedListener != null) {
                            mb2.setLength(0);
                            if (c == 0) {
                                onTouchingLetterChangedListener.onTouchingLetterChanged(c, "#",
                                        mb2);
                            } else {
                                onTouchingLetterChangedListener.onTouchingLetterChanged(c,
                                        defalutTagString[c - 1], mb2);
                            }

                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(defalutTagString[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setVisibility(View.VISIBLE);
                        }
                        mChoose = c;
                        invalidate();
                    }

                }

                break;
        }
        return true;
    }


    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(int index, String s, StringBuffer s1);
    }
}
