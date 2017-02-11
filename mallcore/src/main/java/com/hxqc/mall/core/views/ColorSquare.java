package com.hxqc.mall.core.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hxqc.util.DisplayTools;

/**
 * Author: HuJunJie
 * Date: 2015-04-08
 * FIXME
 * Todo  选择色块
 */
public class ColorSquare extends ImageView {
    Paint paint;
    int space;//与阴影间隔
    int size = 2;
    String[] colors;
    int height;//色块宽度

    public ColorSquare(Context context) {
        super(context);
        paint = new Paint();
        space = DisplayTools.dip2px(context, 2);
    }

    public ColorSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        space = DisplayTools.dip2px(context, 2);
    }

    public ColorSquare(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        space = DisplayTools.dip2px(context, 2);
    }

    public void setColors(String[] colors) {
        this.colors = colors;
        size = colors.length;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (colors == null || TextUtils.isEmpty(colors[0])) return;
        //阴影
        paint.setColor(Color.parseColor("#D0D0D0"));
        canvas.drawRect(space, space, getWidth(), getHeight(), paint);
        height = (getHeight() - space) / size;
        for (int i = 0; i < size; i++) {
            String color = colors[i];
            if (TextUtils.isEmpty(color)) continue;
            try {
                paint.setColor(Color.parseColor(color));
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
            canvas.drawRect(0, height * i, getWidth() - space, height * (i + 1), paint);
        }


    }


}
