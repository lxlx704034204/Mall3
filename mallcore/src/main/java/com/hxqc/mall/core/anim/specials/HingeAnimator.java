package com.hxqc.mall.core.anim.specials;

import android.view.View;

import com.hxqc.mall.core.anim.BaseViewAnimator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Author: wanghao
 * Date: 2015-05-07
 * FIXME
 * Todo
 */
public class HingeAnimator extends BaseViewAnimator{
    @Override
    protected void prepare(View target) {
        float x = target.getPaddingLeft();
        float y = target.getPaddingTop();
        getAnimatorAgent().playTogether(
//                Glider.glide(Skill.SineEaseInOut, 1300, ObjectAnimator.ofFloat(target, "rotation", 0, 80, 60, 80, 60, 60)),
                ObjectAnimator.ofFloat(target, "translationY", 0, 0, 0, 0, 0, 700),
                ObjectAnimator.ofFloat(target, "alpha", 1, 1, 1, 1, 1, 1),
                ObjectAnimator.ofFloat(target, "pivotX", x, x, x, x, x, x),
                ObjectAnimator.ofFloat(target, "pivotY", y, y, y, y, y, y)
        );
        setDuration(1300);
    }
}
