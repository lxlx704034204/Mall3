package com.hxqc.mall.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import hxqc.mall.R;


/**
 * Author: HuJunJie
 * Date: 2015-05-21
 * FIXME
 * Todo
 */
public class BottomBarToBuy extends LinearLayout {

    TextView mDesView;
    View rootView;

    public BottomBarToBuy(Context context) {
        super(context);
    }

    public BottomBarToBuy(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_auto_detail_buy, this);
        mDesView = (TextView) findViewById(R.id.buy_des);
        rootView = findViewById(R.id.view_layout);
    }

    /**
     * 不可购买
     */
    public void setNotCanBuy(String des) {
        mDesView.setText(des);
        mDesView.setTextColor(Color.WHITE);
        rootView.setBackgroundColor(getResources().getColor(
                R.color.item_detail_bottom_bar_background));
    }

    /**
     * 可以购买
     */
    public void setCanBuy(String des) {
        mDesView.setText(des);
        mDesView.setTextColor(Color.WHITE);
        rootView.setBackgroundColor(getResources().getColor(
                R.color.cursor_orange));
    }
}
