package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.hxqc.mall.thirdshop.R;

/**
 * Author:李烽
 * Date:2016-05-10
 * FIXME
 * Todo 标签
 */
public class LabelView extends View {

    private Paint mPaint, mTextPaint;

    private int mBackColor = Color.parseColor("#E12B38"), mTextColor = Color.parseColor("#ffffff");

    private float mWidth, mHeight, textSize = converTextSize(12);


    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setmBackColor(int mBackColor) {
        this.mBackColor = mBackColor;
        invalidate();
    }

    private String text = "文字";

    private Path mPath;

    public LabelView(Context context) {
        this(context, null);
    }

    public LabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Label);
        mBackColor = typedArray.getColor(R.styleable.Label_labelBackColor, mBackColor);
        text = typedArray.getString(R.styleable.Label_labelTextValue);

        mPaint = new Paint();
        mTextPaint = new Paint();
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth() - mHeight / 2;
        Log.i(mHeight + "", mWidth + "");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mBackColor);
        mPaint.setAntiAlias(true);
        mPath.moveTo(0, 0);
        mPath.lineTo(mWidth, 0);
        mPath.lineTo(mWidth + mHeight / 2, mHeight / 2);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
//        mTextPaint.setTextAlign(Paint.Align.CENTER);
        float measureText = mTextPaint.measureText(text);
        float posX = (mWidth - measureText) / 2;
//        float posY = mHeight - (mHeight - textSize) / 2;
        float posY = mHeight / 2 + fontTotalHeight / 4;
        canvas.drawText(text, posX, posY, mTextPaint);

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
