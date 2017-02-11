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
public class ZoomInUpAnimator extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        ViewGroup parent = (ViewGroup)target.getParent();
        int distance = parent.getHeight() - target.getTop();
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 0, 1, 1),
                ObjectAnimator.ofFloat(target,"scaleX",0.1f,0.475f,1),
                ObjectAnimator.ofFloat(target,"scaleY",0.1f,0.475f,1),
                ObjectAnimator.ofFloat(target,"translationY",distance,-60,0)
        );
    }
}
