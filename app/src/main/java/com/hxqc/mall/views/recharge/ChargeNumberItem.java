package com.hxqc.mall.views.recharge;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-03-01
 * FIXME
 * Todo 金额按钮
 */
public class ChargeNumberItem extends LinearLayout implements Checkable {
    private LinearLayout rootLayout;
    private TextView textView;
    private boolean isChecked = false;

    public ChargeNumberItem(Context context) {
        this(context, null);
    }

    public ChargeNumberItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ChargeNumberItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_charge_number_item, this);
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        textView = (TextView) findViewById(R.id.number);

    }

    private  void initCheckStatus() {
        rootLayout.setBackgroundResource(isChecked ? R.drawable.bg_choosesumview_btn_checked : R.drawable.bg_choosesumview_btn);
        textView.setTextColor(Color.parseColor(isChecked ? "#ffffff" : "#f97b5c"));
    }

    public void setNumber(int number) {
        textView.setText(number + getContext().getString(R.string.choose_yuan));
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        initCheckStatus();
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
