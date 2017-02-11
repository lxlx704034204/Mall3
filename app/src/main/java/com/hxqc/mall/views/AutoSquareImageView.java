package com.hxqc.mall.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 根据宽度设置图片高度
 * 车辆详情，企业介绍  比例1:0.47
 *
 * author 胡俊杰
 *         TODO
 */
public class AutoSquareImageView extends ImageView {

    public AutoSquareImageView(Context context) {
        super(context);
    }

    public AutoSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() != getMeasuredWidth()) {
            setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth()*0.47));
        }
    }

}
