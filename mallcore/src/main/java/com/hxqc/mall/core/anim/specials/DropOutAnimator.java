package com.hxqc.mall.core.anim.specials;

import android.view.View;

import com.hxqc.mall.core.anim.BaseViewAnimator;
import com.hxqc.mall.core.anim.easing.Glider;
import com.hxqc.mall.core.anim.easing.Skill;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Author: wanghao
 * Date: 2015-05-06
 * FIXME
 * Todo
 */
public class DropOutAnimator extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        int distance = target.getTop() + target.getHeight();
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                Glider.glide(Skill.BounceEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "translationY", -distance, 0))
        );
    }
}
