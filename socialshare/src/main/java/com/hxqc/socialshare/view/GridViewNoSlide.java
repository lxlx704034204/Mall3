package com.hxqc.socialshare.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 不滑动gridView
 *
 * author 胡俊杰
 */
public class GridViewNoSlide extends GridView {
    public GridViewNoSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewNoSlide(Context context) {
        super(context);
    }

    public GridViewNoSlide(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
