package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hxqc.util.DebugLog;

/**
 * Created by zhaofan
 * 冒号前面对齐的TextView
 */
public class AlignTextView extends TextView {

    private int mTextY;
    private int mViewWidth;

    public AlignTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        mViewWidth = getMeasuredWidth();
        String text = getText().toString();
        //  mTextY = (int) getTextSize();
        //获取Text高
        Paint.FontMetrics fm = paint.getFontMetrics();
        mTextY = (int) (Math.ceil(fm.descent - fm.ascent));

        int lineStart = 0;
        int lineEnd = length();

        //   float width = getTextSize() * line.length();

        float width = StaticLayout.getDesiredWidth(text, lineStart,
                lineEnd, getPaint());

        DebugLog.e("", width + "");

        drawScaledText(canvas, text, width);


    }

    private void drawScaledText(Canvas canvas, String line,
                                float lineWidth) {
        float x = 0;
        int gapCount = line.length() - 1;

        float d = (mViewWidth - lineWidth) / gapCount;  //每个文字的width
        for (int i = 0; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mTextY, getPaint());
            x += cw + d;
        }
    }

}
