package com.hxqc.mall.views.bill;

import android.content.Context;
import android.util.AttributeSet;

import com.hxqc.mall.core.views.BaseMapView;

/**
 * Author:李烽
 * Date:2016-03-05
 * FIXME
 * Todo 标题内容横条
 */
public class NormalMapView extends BaseMapView {


    public NormalMapView(Context context) {
        this(context, null);
    }

    public NormalMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setOnValueClickListener(OnValueClickListener onValueClickListener) {
        this.onValueClickListener = onValueClickListener;
    }
}
