package com.hxqc.carcompare.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.hxqc.carcompare.ui.compare.CarCompareDetailActivity;


/**
 * 车型对比 横向滑动
 * Created by zhaofan on 2016/10/9.
 */
public class CHScrollView extends HorizontalScrollView {

    CarCompareDetailActivity activity;

    public CHScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        activity = (CarCompareDetailActivity) context;
    }


    public CHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (CarCompareDetailActivity) context;
    }

    public CHScrollView(Context context) {
        super(context);
        activity = (CarCompareDetailActivity) context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //进行触摸赋值
        activity.mTouchView = this;
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        //当当前的CHSCrollView被触摸时，滑动其它
        if (activity.mTouchView == this) {
            activity.onScrollChanged(l, t, oldl, oldt);
        } else {
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }
}
