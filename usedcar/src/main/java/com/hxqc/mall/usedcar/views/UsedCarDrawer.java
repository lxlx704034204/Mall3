package com.hxqc.mall.usedcar.views;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.hxqc.mall.core.views.vedit.EditTextValidatorView;

/**
 * 说明:二手车专用侧滑，监听关闭作为验证
 *
 * @author: 吕飞
 * @since: 2016-12-12
 * Copyright:恒信汽车电子商务有限公司
 */

public class UsedCarDrawer extends DrawerLayout {
    EditTextValidatorView mEditTextValidatorView;
    public UsedCarDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                mEditTextValidatorView.validate();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }
    public void open(EditTextValidatorView editTextValidatorView){
        openDrawer(Gravity.RIGHT);
        mEditTextValidatorView=editTextValidatorView;
    }
}
