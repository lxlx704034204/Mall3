package com.hxqc.mall.views.auto;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.PaymentType;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:胡俊杰
 * Date: 2015/10/18
 * FIXME
 * Todo 付款方式选择
 */
public class AutoVerifyPaymentView extends RelativeLayout implements View.OnClickListener {
//    TextView mDesView;
    Button mInstallmentView;//分期
    CheckBox mOffLineView;//预付订金
    OnPaymentClickListener onPaymentClickListener;
    TextView mInstallmentDesView;//分期描述
    TextView mOfflineDesView;//线下描述

    public void setOnPaymentClickListener(OnPaymentClickListener onPaymentClickListener) {
        this.onPaymentClickListener = onPaymentClickListener;
    }


    public interface OnPaymentClickListener {
        void onPaymentClick(PaymentType payment, View v);
    }

    @Override
    public void onClick(View v) {
        PaymentType paymentType = (PaymentType) v.getTag();
//        if (paymentType != null)
//            mDesView.setText(paymentType.paymentDesc);
        if (onPaymentClickListener != null) {
            onPaymentClickListener.onPaymentClick(paymentType, v);
        }
        switch (v.getId()) {
            case R.id.payment_type_off_line:
                setOfflineDes(true);
                break;
            case R.id.payment_type_installment:
                setOfflineDes(false);
                break;
        }
    }

    public AutoVerifyPaymentView(Context context) {
        super(context);
    }

    public AutoVerifyPaymentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_auto_verify_pay, this);
        mOffLineView = (CheckBox) findViewById(R.id.payment_type_off_line);
        mOffLineView.setOnClickListener(this);
        mInstallmentView = (Button) findViewById(R.id.payment_type_installment);
        mInstallmentView.setOnClickListener(this);
        mInstallmentDesView= (TextView) findViewById(R.id.installment_des);
        mOfflineDesView= (TextView) findViewById(R.id.off_line_des);
    }

    public AutoVerifyPaymentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * <font color='red'>红色字</font>
     * @param paymentTypes
     */
    public void setPaymentTypes(ArrayList< PaymentType > paymentTypes) {
        if (paymentTypes == null) {
            this.setVisibility(View.GONE);
            return;
        }
        int size = paymentTypes.size();
        if (size >= 1){
            mOffLineView.setTag(paymentTypes.get(0));
            mOfflineDesView.setText(Html.fromHtml(paymentTypes.get(0).paymentDesc));
        }

        if (size >= 2){
            mInstallmentView.setTag(paymentTypes.get(1));
            mInstallmentDesView.setText(Html.fromHtml(paymentTypes.get(1).paymentDesc));
        }

        setOfflineDes(true);
    }


    public void setOfflineDes(boolean isChoose) {
        if (isChoose) {
            //订金
            mInstallmentView.setBackgroundResource(R.drawable.bg_tag);
            mInstallmentView.setTextColor(getResources().getColor(R.color.check_false_text));
            mOffLineView.setChecked(true);
        } else {
            //分期
            mInstallmentView.setBackgroundResource(R.drawable.ic_chick);
            mInstallmentView.setTextColor(getResources().getColor(R.color.check_true_text));
            mOffLineView.setChecked(false);
        }
    }

}
