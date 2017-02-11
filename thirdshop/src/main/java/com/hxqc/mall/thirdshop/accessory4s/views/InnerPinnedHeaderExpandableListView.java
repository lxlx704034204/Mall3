package com.hxqc.mall.thirdshop.accessory4s.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hxqc.widget.PinnedHeaderExpandableListView;

/**
 * 适用于ScrollView内部
 * Created by huangyi on 16/8/1.
 */
public class InnerPinnedHeaderExpandableListView extends PinnedHeaderExpandableListView {

    public InnerPinnedHeaderExpandableListView(Context context) {
        super(context);
    }

    public InnerPinnedHeaderExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerPinnedHeaderExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

}
