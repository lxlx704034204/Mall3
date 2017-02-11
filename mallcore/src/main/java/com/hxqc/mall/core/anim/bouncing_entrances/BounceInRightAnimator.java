package com.hxqc.mall.core.anim.bouncing_entrances;

import android.view.View;

import com.hxqc.mall.core.anim.BaseViewAnimator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Author: wanghao
 * Date: 2015-05-06
 * FIXME
 * Todo
 */
public class BounceInRightAnimator extends BaseViewAnimator{
    @Override
    protected void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "translationX", target.getMeasuredWidth() + target.getWidth(), -30, 10, 0),
                ObjectAnimator.ofFloat(target,"alpha",0,1,1,1)
        );
    }
}
