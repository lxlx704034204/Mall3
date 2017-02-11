package com.hxqc.mall.core.anim.slider;

import android.view.View;

import com.hxqc.mall.core.anim.BaseViewAnimator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Author: wanghao
 * Date: 2015-05-06
 * FIXME
 * Todo
 */
public class SlideOutUpAnimator extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
//        ViewGroup parent = (ViewGroup)target.getParent();
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 1, 0),
                ObjectAnimator.ofFloat(target,"translationY",0,-target.getBottom())
        );
    }
}
