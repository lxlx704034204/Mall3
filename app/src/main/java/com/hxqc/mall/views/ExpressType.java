package com.hxqc.mall.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hxqc.mall.R;


/**
 * 说明:订单详情中配送方式item
 *
 * author: 吕飞
 * since: 2015-04-07
 * Copyright:恒信汽车电子商务有限公司
 */
public class ExpressType extends RelativeLayout {
    TextView mPickupPointAddressView;
    Context mContext;

    public ExpressType(Context context) {
        super(context);
    }

    public ExpressType(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_express_type, this);
        mPickupPointAddressView = (TextView) findViewById(R.id.pickup_point_address);
    }

    public void setPickupPoint(String address, Context context) {
        mPickupPointAddressView.setText(address);
        this.mContext = context;
    }
}
