package com.hxqc.newenergy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
import android.widget.GridView;

/**
 * 说明:
 * author: 何玉
 * since: 2016/3/14.
 * Copyright:恒信汽车电子商务有限公司
 */
public class ExanablelistviewNoScroll extends ExpandableListView {

    public ExanablelistviewNoScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExanablelistviewNoScroll(Context context) {
        super(context);
        setOverScrollMode(GridView.OVER_SCROLL_NEVER);
    }

    public ExanablelistviewNoScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(GridView.OVER_SCROLL_NEVER);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
