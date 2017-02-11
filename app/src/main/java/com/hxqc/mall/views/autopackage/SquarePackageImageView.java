package com.hxqc.mall.views.autopackage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 根据宽度设置图片高度
 * SquareImageView
 *
 * author 胡俊杰
 *         TODO
 */
public class SquarePackageImageView extends ImageView {

    public SquarePackageImageView(Context context) {
        super(context);
    }

    public SquarePackageImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() != getMeasuredWidth()) {
            setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth() * 0.56));
        }
    }

}
