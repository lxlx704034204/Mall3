package com.hxqc.mall.core.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hxqc.util.DebugLog;

/**
 * Function: TextView文字和Drawable居中
 *
 * @author 袁秉勇
 * @since 2016年04月01日
 */
public class DrawableCenterTextView extends TextView {
    private final static String TAG = DrawableCenterTextView.class.getSimpleName();
    private Context mContext;


    public DrawableCenterTextView(Context context) {
        super(context);
    }


    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public DrawableCenterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableRight = drawables[2];
            if (drawableRight != null) {
                float textWidth = getPaint().measureText(getText().toString());
                DebugLog.e(TAG, "text width is " + textWidth);
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableRight.getIntrinsicWidth();
                DebugLog.e(TAG, "drawable width is " + drawableWidth + " textView width is " + getWidth() + " getResource width is " + getResources().getDisplayMetrics().widthPixels);
                float bodyWidth = textWidth + drawableWidth + drawablePadding + this.getPaddingLeft() + this.getPaddingRight();
                DebugLog.e(TAG, "bodyWidth is " + bodyWidth + " drawablePadding is " + drawablePadding + "paddingLeft " + getPaddingLeft() + " paddingRight " + getPaddingRight() + " xxx " +(getWidth()-bodyWidth));
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            }
        }
        super.onDraw(canvas);
    }
}
