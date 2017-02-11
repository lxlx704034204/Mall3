package com.hxqc.mall.views.order;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hxqc.mall.R;


/**
 * 说明:订单详情里面的item
 *
 * author: 吕飞
 * since: 2015-04-07
 * Copyright:恒信汽车电子商务有限公司
 */
public class OrderItem extends RelativeLayout {
    public TextView mLeftTextView;
    public TextView mRightTextView;

    public OrderItem(Context context) {
        super(context);
    }

    public OrderItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_item_order, this);
        mLeftTextView = (TextView) findViewById(R.id.left_text);
        mRightTextView = (TextView) findViewById(R.id.right_text);
    }
}
