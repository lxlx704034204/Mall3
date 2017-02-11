package com.hxqc.mall.core.anim.rotate;

import android.view.View;

import com.hxqc.mall.core.anim.BaseViewAnimator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Author: wanghao
 * Date: 2015-05-06
 * FIXME
 * Todo
 */
public class RotateOutUpRightAnimator extends BaseViewAnimator {

    @Override
    protected void prepare(View target) {
        float x = target.getWidth() - target.getPaddingRight();
        float y = target.getHeight() - target.getPaddingBottom();
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 1, 0),
                ObjectAnimator.ofFloat(target,"rotation",0,90),
                ObjectAnimator.ofFloat(target,"pivotX",x,x),
                ObjectAnimator.ofFloat(target,"pivotY",y,y)
        );
    }
}
