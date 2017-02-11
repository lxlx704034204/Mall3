package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * Author:李烽
 * Date:2016-05-10
 * FIXME
 * Todo 销售区域的标签
 */
public class AreaLabel extends LabelView {
    public AreaLabel(Context context) {
        this(context, null);
    }

    public AreaLabel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AreaLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setArea(int areaID) {
        if (areaID == 20) {
            setmBackColor(Color.parseColor("#1F96F0"));
            setText("售全国");
        } else if (areaID == 10) {
            setmBackColor(Color.parseColor("#E12B38"));
            setText("售本省");
        } else if (areaID == 30) {
            setmBackColor(Color.parseColor("#E45012"));
            setText("售本市");
        } else {
            setmBackColor(Color.parseColor("#dfdfdf"));
            setText("areaID错误");
        }
    }
}
