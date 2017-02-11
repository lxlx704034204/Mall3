package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;

/**
 * Author: wanghao
 * Date: 2015-12-02
 * FIXME  支付方式条目
 * Todo
 */
public class PaymentPieceView extends RelativeLayout {

    ImageView mIconView;
    TextView mLabelView;
    CheckBox mCheckView;

    public PaymentPieceView(Context context) {
        super(context);
        initView();
    }

    public PaymentPieceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.t_item_sales_payment_view, this);
        mIconView = (ImageView) findViewById(R.id.payment_icon);
        mLabelView = (TextView) findViewById(R.id.payment_label);
        mCheckView = (CheckBox) findViewById(R.id.cb_payment_check);
    }

    public void setValues(String img,String payName){
        ImageUtil.setImage(getContext(), mIconView, img);
        mLabelView.setText(payName);
    }

    public void setChecked(boolean t) {
        mCheckView.setChecked(t);
    }
}
