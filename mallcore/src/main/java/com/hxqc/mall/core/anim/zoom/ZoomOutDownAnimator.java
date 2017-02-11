package com.hxqc.mall.core.anim.zoom;

import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.anim.BaseViewAnimator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Author: wanghao
 * Date: 2015-05-06
 * FIXME
 * Todo
 */
public class ZoomOutDownAnimator extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        ViewGroup parent = (ViewGroup)target.getParent();
        int distance = parent.getHeight() - target.getTop();
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 1, 1, 0),
                ObjectAnimator.ofFloat(target,"scaleX",1,0.475f,0.1f),
                ObjectAnimator.ofFloat(target,"scaleY",1,0.475f,0.1f),
                ObjectAnimator.ofFloat(target,"translationY",0,-60,distance)
        );
    }
}
