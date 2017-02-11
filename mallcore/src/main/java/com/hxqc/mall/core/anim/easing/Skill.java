package com.hxqc.mall.core.anim.easing;

/**
 * Author: wanghao
 * Date: 2015-05-06
 * FIXME
 * Todo
 */
public enum Skill {
//    BackEaseIn(BackEaseIn.class),
//    BackEaseOut(BackEaseOut.class),
//    BackEaseInOut(BackEaseInOut.class),
//    BounceEaseIn(BounceEaseIn.class),

//    BounceEaseInOut(BounceEaseInOut.class),
//    CircEaseIn(CircEaseIn.class),
//    CircEaseOut(CircEaseOut.class),
//    CircEaseInOut(CircEaseInOut.class),
//    CubicEaseIn(CubicEaseIn.class),
//    CubicEaseOut(CubicEaseOut.class),
//    CubicEaseInOut(CubicEaseInOut.class),
//    ElasticEaseIn(ElasticEaseIn.class),
//    ElasticEaseOut(ElasticEaseOut.class),
//    ExpoEaseIn(ExpoEaseIn.class),
//    ExpoEaseOut(ExpoEaseOut.class),
//    ExpoEaseInOut(ExpoEaseInOut.class),
//    QuadEaseIn(QuadEaseIn.class),
//    QuadEaseOut(QuadEaseOut.class),
//    QuadEaseInOut(QuadEaseInOut.class),
//    QuintEaseIn(QuintEaseIn.class),
//    QuintEaseOut(QuintEaseOut.class),
//    QuintEaseInOut(QuintEaseInOut.class),
//    SineEaseIn(SineEaseIn.class),
//    SineEaseOut(SineEaseOut.class),
//    SineEaseInOut(SineEaseInOut.class),
//    Linear(Linear.class),
        BounceEaseOut(BounceEaseOut.class);

    private Class easingMethod;

    Skill(Class clazz) {
        this.easingMethod = clazz;
    }

    public BaseEasingMethod getMethod(float duration) {
        try {
            return (BaseEasingMethod)this.easingMethod.getConstructor(new Class[]{Float.TYPE}).newInstance(duration);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new Error("Can not init easingMethod instance");
        }
    }

}
