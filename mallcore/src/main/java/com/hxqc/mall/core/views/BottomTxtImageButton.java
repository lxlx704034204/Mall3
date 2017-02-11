package com.hxqc.mall.core.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.util.ScreenUtil;

/**
 * Author:李烽
 * Date:2016-03-26
 * FIXME
 * Todo
 */
public class BottomTxtImageButton extends LinearLayout {
    public BottomTxtImageButton(Context context) {
        this(context, null);
    }

    public BottomTxtImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomTxtImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        ImageView imageView = new ImageView(context);
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(0, 0, 0, ScreenUtil.dip2px(context, 10));
        imageView.setLayoutParams(layoutParams2);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomTxtImageButton);
        float textSize = typedArray.getDimension(R.styleable.BottomTxtImageButton_bottom_text_size, 7);
        textSize = px2dip(context, textSize);
        int color = typedArray.getColor(R.styleable.BottomTxtImageButton_bottom_text_color, Color.parseColor("#333333"));
        String string = typedArray.getString(R.styleable.BottomTxtImageButton_bottom_text);
        textView.setText(string);
        textView.setTextSize(textSize);
        textView.setTextColor(color);
        Drawable drawable = typedArray.getDrawable(R.styleable.BottomTxtImageButton_image_src);
        if (drawable != null)
            imageView.setImageDrawable(drawable);
        typedArray.recycle();
        addView(imageView);
        addView(textView);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
