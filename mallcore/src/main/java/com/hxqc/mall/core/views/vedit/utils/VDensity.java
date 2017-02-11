package com.hxqc.mall.core.views.vedit.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Author:  wh
 * Date:  2016/11/2
 * FIXME
 * Todo
 */

public class VDensity {
    public static int dp2px(Context context, float dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return Math.round(px);
    }
}
