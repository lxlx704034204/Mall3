package com.hxqc.mall.photolibrary.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author: wanghao
 * Date: 2015-03-24
 * FIXME
 * Todo
 */
public class HackyViewPager extends ViewPager {

	private boolean isLocked;

	public HackyViewPager(Context context) {
		super(context);
		isLocked = false;
	}

	public HackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		isLocked = false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!isLocked) {
			try {
				return super.onInterceptTouchEvent(ev);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return !isLocked && super.onTouchEvent(ev);
	}

	public void toggleLock() {
		isLocked = !isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public boolean isLocked() {
		return isLocked;
	}
}
