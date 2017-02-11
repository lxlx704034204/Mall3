package com.hxqc.aroundservice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 25
 * FIXME
 * Todo
 */
public class IllegalAutoInfoView extends LinearLayout {

    private TextView mMoneyView;
    private TextView mServiceView;
    private TextView mNumberView;
    private ImageView mBGView;

    public IllegalAutoInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_illegal_auto_info, this);
        mMoneyView = (TextView) findViewById(R.id.illegal_auto_info_money);
        mServiceView = (TextView) findViewById(R.id.illegal_auto_info_service);
        mNumberView = (TextView) findViewById(R.id.illegal_auto_info_number);
        mBGView = (ImageView) findViewById(R.id.illegal_auto_info_bg);

    }

    /**
     * @param resid
     */
    public void setBackground(int resid) {
        mBGView.setImageResource(resid);
    }

    /**
     * @param num
     * @param service
     * @param money
     */
    public void setData(CharSequence num, CharSequence service, CharSequence money) {
        mNumberView.setText(num);
        mServiceView.setText("服务类型:  " + service);
        mMoneyView.setText(money);
    }

    /**
     * @param bgVisibility
     * @param numVisibility
     * @param serviceVisibility
     * @param moneyVisibility
     */
    public void setVisibility(int bgVisibility, int numVisibility, int serviceVisibility, int moneyVisibility) {
        mBGView.setVisibility(bgVisibility);
        mNumberView.setVisibility(numVisibility);
        mServiceView.setVisibility(serviceVisibility);
        mMoneyView.setVisibility(moneyVisibility);
    }
}
