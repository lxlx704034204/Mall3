package com.hxqc.mall.thirdshop.accessory.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;


/**
 * 说明:订单详情下部item
 *
 * @author: 吕飞
 * @since: 2016-2-25
 * Copyright:恒信汽车电子商务有限公司
 */
public class OrderDetailItem extends RelativeLayout {
    TextView mLeftView;
    TextView mRightView;
    TextView mTopView;
    TextView mBottomView;
    String mLeftText;
    int mRightTextColor;

    public OrderDetailItem(Context context) {
        super(context);
        initView();
    }

    public OrderDetailItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.OrderDetailItem);
        mLeftText = typedArray.getString(R.styleable.OrderDetailItem_leftText);
        mRightTextColor = typedArray.getColor(R.styleable.OrderDetailItem_rightTextColor, getResources().getColor(R.color.text_color_title));
        typedArray.recycle();
        initLeft();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_order_detail_item, this);
        mLeftView = (TextView) findViewById(R.id.left);
        mRightView = (TextView) findViewById(R.id.right);
        mTopView = (TextView) findViewById(R.id.top);
        mBottomView = (TextView) findViewById(R.id.bottom);
    }

    private void initLeft() {
        mLeftView.setText(mLeftText);
        mRightView.setTextColor(mRightTextColor);
    }

    public void setRightText(String rightText) {
        mRightView.setText(rightText);
    }

    public void setRightText(String topText, String bottomText) {
        mTopView.setText(topText);
        mBottomView.setText(bottomText);
    }
}

