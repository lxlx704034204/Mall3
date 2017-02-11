package com.hxqc.mall.core.views.vedit.utils;

import android.graphics.Color;

/**
 * Author:  wh
 * Date:  2016/11/2
 * FIXME
 * Todo
 */

public class VColors {
    public static boolean isLight(int color) {
        return Math.sqrt(
                Color.red(color) * Color.red(color) * .241 +
                        Color.green(color) * Color.green(color) * .691 +
                        Color.blue(color) * Color.blue(color) * .068) > 130;
    }
}
