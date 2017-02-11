package com.hxqc.mall.usedcar.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.usedcar.R;

/**
 * 说明:快速选择的侧边栏，有字母
 *
 * @author: 吕飞
 * @since: 2015-08-03
 * Copyright:恒信汽车电子商务有限公司
 */
public class SideBar extends View {
    public static StringBuffer mb2 = new StringBuffer("");
    public  String[] mb1 = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private int mChoose = -1;
    private Paint mPaint = new Paint();
    private Bitmap mBmp;

    private TextView mTextDialog;

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        mBmp = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.sidebar_selected_bg)).getBitmap();
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
//        mBmp = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.sidebar_selected_bg)).getBitmap();
    }

    public SideBar(Context context) {
        super(context);
//        mBmp = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.sidebar_selected_bg)).getBitmap();
    }

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / mb1.length;

        if (mChoose >= 1) {
//        	canvas.drawBitmap(this.mBmp, 8.0F, singleHeight * (mChoose-1), null);
        }

        for (int i = 0; i < mb1.length; i++) {
            mPaint.setColor(getContext().getResources().getColor(R.color.text_blue));
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(32);
            float xPos = width / 2 - mPaint.measureText(mb1[i]) / 2 - 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(mb1[i], xPos, yPos, mPaint);
            mPaint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = mChoose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * mb1.length);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mChoose = -1;
            invalidate();
            if (mTextDialog != null) {
                mTextDialog.setVisibility(View.INVISIBLE);
            }

        } else {
            if (oldChoose != c) {
                if (c >= 0 && c < mb1.length) {
                    if (listener != null) {
                        mb2.setLength(0);
                        listener.onTouchingLetterChanged(mb1[c], mb2);
                    }
                    if (mTextDialog != null) {
                        mTextDialog.setText(mb1[c]);
                    }
                    if (mTextDialog != null) {
                        mTextDialog.setVisibility(View.VISIBLE);
                    }
                    mChoose = c;
                    invalidate();
                }
            }


        }
        return true;
    }


    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s, StringBuffer s1);
    }
}