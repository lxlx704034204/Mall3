package com.hxqc.mall.core.views;

/**
 * 说明:固定高宽比的图片
 *
 * author: 吕飞
 * since: 2015-09-10
 * Copyright:恒信汽车电子商务有限公司
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hxqc.mall.core.R;


public class HeightVSWidthImageView extends ImageView {
    private float heightVsWidth;

    public HeightVSWidthImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HeightVSWidthImage);
        heightVsWidth = typedArray.getFloat(R.styleable.HeightVSWidthImage_heightVsWidth, 1f);
        typedArray.recycle();
    }

    public HeightVSWidthImageView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth() * heightVsWidth));
    }
}
