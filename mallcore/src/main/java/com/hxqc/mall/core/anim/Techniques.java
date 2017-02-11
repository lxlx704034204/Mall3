package com.hxqc.mall.core.anim;

import com.hxqc.mall.core.anim.attention.BounceAnimator;
import com.hxqc.mall.core.anim.attention.TadaAnimator;
import com.hxqc.mall.core.anim.bouncing_entrances.BounceInAnimator;
import com.hxqc.mall.core.anim.bouncing_entrances.BounceInDownAnimator;
import com.hxqc.mall.core.anim.bouncing_entrances.BounceInLeftAnimator;
import com.hxqc.mall.core.anim.bouncing_entrances.BounceInRightAnimator;
import com.hxqc.mall.core.anim.bouncing_entrances.BounceInUpAnimator;
import com.hxqc.mall.core.anim.roll.RollInAnimator;
import com.hxqc.mall.core.anim.roll.RollOutAnimator;
import com.hxqc.mall.core.anim.rotate.RotateInDownLeftAnimator;
import com.hxqc.mall.core.anim.rotate.RotateOutUpRightAnimator;
import com.hxqc.mall.core.anim.slider.SlideInUpAnimator;
import com.hxqc.mall.core.anim.slider.SlideOutUpAnimator;
import com.hxqc.mall.core.anim.specials.DropOutAnimator;
import com.hxqc.mall.core.anim.zoom.ZoomInUpAnimator;
import com.hxqc.mall.core.anim.zoom.ZoomOutDownAnimator;

/**
 * Author: wanghao
 * Date: 2015-05-06
 * FIXME
 * 各种动画的 枚举
 */
public enum Techniques {



    DropOut(DropOutAnimator.class),
//    Landing(LandingAnimator.class),
//    TakingOff(TakingOffAnimator.class),
//
//    Flash(FlashAnimator.class),
//    Pulse(PulseAnimator.class),
//    RubberBand(RubberBandAnimator.class),
//    Shake(ShakeAnimator.class),
//    Swing(SwingAnimator.class),
//    Wobble(WobbleAnimator.class),
    Bounce(BounceAnimator.class),
    Tada(TadaAnimator.class),
//    StandUp(StandUpAnimator.class),
//    Wave(WaveAnimator.class),
//
//    Hinge(HingeAnimator.class),


    BounceIn(BounceInAnimator.class),
    BounceInDown(BounceInDownAnimator.class),
    BounceInLeft(BounceInLeftAnimator.class),
    BounceInRight(BounceInRightAnimator.class),
    BounceInUp(BounceInUpAnimator.class),
//
//    FadeIn(FadeInAnimator.class),
//    FadeInUp(FadeInUpAnimator.class),
//    FadeInDown(FadeInDownAnimator.class),
//    FadeInLeft(FadeInLeftAnimator.class),
//    FadeInRight(FadeInRightAnimator.class),
//
//    FadeOut(FadeOutAnimator.class),
//    FadeOutDown(FadeOutDownAnimator.class),
//    FadeOutLeft(FadeOutLeftAnimator.class),
//    FadeOutRight(FadeOutRightAnimator.class),
//    FadeOutUp(FadeOutUpAnimator.class),
//
//    FlipInX(FlipInXAnimator.class),
//    FlipOutX(FlipOutXAnimator.class),
//    FlipInY(FlipInYAnimator.class),
//    FlipOutY(FlipOutYAnimator.class),
//    RotateIn(RotateInAnimator.class),
    RotateInDownLeft(RotateInDownLeftAnimator.class),
//    RotateInDownRight(RotateInDownRightAnimator.class),
//    RotateInUpLeft(RotateInUpLeftAnimator.class),
//    RotateInUpRight(RotateInUpRightAnimator.class),
//
//    RotateOut(RotateOutAnimator.class),
//    RotateOutDownLeft(RotateOutDownLeftAnimator.class),
//    RotateOutDownRight(RotateOutDownRightAnimator.class),
//    RotateOutUpLeft(RotateOutUpLeftAnimator.class),
    RotateOutUpRight(RotateOutUpRightAnimator.class),
//
//    SlideInLeft(SlideInLeftAnimator.class),
//    SlideInRight(SlideInRightAnimator.class),
    SlideInUp(SlideInUpAnimator.class),
//    SlideInDown(SlideInDownAnimator.class),
//
//    SlideOutLeft(SlideOutLeftAnimator.class),
//    SlideOutRight(SlideOutRightAnimator.class),
    SlideOutUp(SlideOutUpAnimator.class),
//    SlideOutDown(SlideOutDownAnimator.class),
//
//    ZoomIn(ZoomInAnimator.class),
//    ZoomInDown(ZoomInDownAnimator.class),
//    ZoomInLeft(ZoomInLeftAnimator.class),
//    ZoomInRight(ZoomInRightAnimator.class),
    ZoomInUp(ZoomInUpAnimator.class),
//
//    ZoomOut(ZoomOutAnimator.class),
    ZoomOutDown(ZoomOutDownAnimator.class),
//    ZoomOutLeft(ZoomOutLeftAnimator.class),
//    ZoomOutRight(ZoomOutRightAnimator.class),
//    ZoomOutUp(ZoomOutUpAnimator.class),

    RollIn(RollInAnimator.class),
    RollOut(RollOutAnimator.class);

    private Class animatorClazz;

    Techniques(Class clazz) {
        animatorClazz = clazz;
    }

    public BaseViewAnimator getAnimator() {
        try {
            return (BaseViewAnimator) animatorClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }

}
