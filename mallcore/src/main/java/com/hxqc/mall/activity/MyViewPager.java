package com.hxqc.mall.activity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hxqc.util.DebugLog;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年10月18日
 */
public class MyViewPager extends ViewPager {
    private final static String TAG = MyViewPager.class.getSimpleName();
    private Context mContext;


    public MyViewPager(Context context) {
        super(context);
        DebugLog.e(TAG, "This is one param function");
    }


    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        DebugLog.e(TAG, "This function has two params");
    }


    boolean flag;


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        DebugLog.e(TAG, " ----------------- onTouchEvent -----------------");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                DebugLog.e("onTouchEvent", "---------------> ActionDown");
                break;

            case MotionEvent.ACTION_MOVE:
                DebugLog.e("onTouchEvent", "---------------> ActionMove");
                break;

            case MotionEvent.ACTION_UP:
                DebugLog.e("onTouchEvent", "---------------> ActionUp");
                break;
        }

        flag = super.onTouchEvent(ev);

        DebugLog.e(TAG, " onTouchEvent : " + flag);

        return flag;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean action_down_flag = false;
        boolean action_up_flag = false;

        View view = findViewWithTag("tag");
        DebugLog.e(TAG, view.toString());

        DebugLog.e(TAG, " ----------------- dispatchTouchEvent -----------------");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (view != null && isViewUnder(view, (int) ev.getX(), (int) ev.getY())) {
                    action_down_flag = true;
                    DebugLog.e(TAG, " ========================= ");
                }

                DebugLog.e("dispatchTouchEvent", "=================== ActionDown");
                break;

            case MotionEvent.ACTION_MOVE:
                DebugLog.e("dispatchTouchEvent", "=================== ActionMove");
                break;

            case MotionEvent.ACTION_UP:

                if (view != null && isViewUnder(view, (int) ev.getX(), (int) ev.getY())) {
                    action_down_flag = true;
                    DebugLog.e(TAG, " ========================= ");
                }

                DebugLog.e("dispatchTouchEvent", "=================== ActionUp");
                break;
        }

        if (action_down_flag && action_up_flag) {
            view.performClick();
            action_down_flag = false;
            action_up_flag = false;
            return true;
        }

        flag = super.dispatchTouchEvent(ev);

        DebugLog.e(TAG, " dispatchTouchEvent : " + flag);

        return flag;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        DebugLog.e(TAG, " ----------------- onInterceptTouchEvent -----------------");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                DebugLog.e("onInterceptTouchEvent", " >>>>>>>>>>>>>> ActionDown");
                break;

            case MotionEvent.ACTION_MOVE:
                DebugLog.e("onInterceptTouchEvent", " >>>>>>>>>>>>>> ActionMove");
                break;

            case MotionEvent.ACTION_UP:
                DebugLog.e("onInterceptTouchEvent", " >>>>>>>>>>>>>> ActionUp");
                break;
        }

        flag = super.onInterceptTouchEvent(ev);

        DebugLog.e(TAG, " onInterceptTouchEvent : " + flag);

        return flag;
    }


    private boolean isViewUnder(View view, int x, int y) {
        if (view == null) return false;
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        DebugLog.e(TAG, " screenX : " + screenX + " screenY : " + screenY + " viewLocation[0] : " + viewLocation[0] + " viewLocation[1] " + viewLocation[1] +
        " width : " + view.getWidth() + " height : " + view.getHeight() + " x : " + x + " y : " + y + " " + parentLocation[0] + " " + parentLocation[1]);
        return screenX >= viewLocation[0] && screenX < viewLocation[0] + view.getWidth() &&
                screenY >= viewLocation[1] && screenY < viewLocation[1] + view.getHeight();
    }
}
