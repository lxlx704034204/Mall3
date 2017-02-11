package com.hxqc.mall.core.anim.roll;

import android.view.View;

import com.hxqc.mall.core.anim.BaseViewAnimator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Author: wanghao
 * Date: 2015-05-06
 * FIXME
 * Todo
 */
public class RollInAnimator extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                ObjectAnimator.ofFloat(target,"translationX",-(target.getWidth()-target.getPaddingLeft() - target.getPaddingRight()),0),
                ObjectAnimator.ofFloat(target,"rotation",-120,0)
        );
    }
}
