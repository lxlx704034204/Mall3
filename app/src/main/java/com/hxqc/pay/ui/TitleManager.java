package com.hxqc.pay.ui;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.widget.RadioButton;

import com.hxqc.pay.activity.PayMainActivity;
import com.hxqc.pay.util.ConstantValue;

import hxqc.mall.R;

/**
 * Created by CPR062 on 2015/3/4.
 * <p/>
 * 支付导航栏 1 2 3 4步 切换
 */
public class TitleManager {

    final static int step1 = 0;
    final static int step2 = 1;
    final static int step3 = 2;
    final static int step4 = 3;

    public static TitleManager titleManagerInstance = new TitleManager();

    public static TitleManager getInstance() {
        if (titleManagerInstance == null) {
            synchronized (TitleManager.class) {
                if (titleManagerInstance == null) {
                    titleManagerInstance = new TitleManager();
                }
            }
        }
        return titleManagerInstance;
    }

    RadioButton rb_sign;
    RadioButton rb_payment;
    RadioButton rb_information;
    RadioButton rb_complete;

    Activity main;

    public void onDestroy(){
        if (titleManagerInstance!=null)
            titleManagerInstance = null;
    }

    public void changeTitle(Activity main1, int s) {
        this.main = main1;
        rb_sign = (RadioButton) main.findViewById(R.id.rb_sign_contract);
        rb_payment = (RadioButton) main.findViewById(R.id.rb_payment);
        rb_information = (RadioButton) main.findViewById(R.id.rb_complete_information);
        rb_complete = (RadioButton) main.findViewById(R.id.rb_complete);
        changeTitleStatus(s);
    }

    void colorChange(int step) {

        if (step == step1) {
            rb_sign.setChecked(true);
            rb_sign.setTextColor(main.getResources().getColor(R.color.white));
            rb_payment.setTextColor(main.getResources().getColor(R.color.title_disable_red));
            rb_information.setTextColor(main.getResources().getColor(R.color.title_disable_red));
            rb_complete.setTextColor(main.getResources().getColor(R.color.title_disable_red));
        } else if (step == step2) {
            rb_payment.setChecked(true);
            rb_sign.setTextColor(main.getResources().getColor(R.color.title_disable_red));
            rb_payment.setTextColor(main.getResources().getColor(R.color.white));
            rb_information.setTextColor(main.getResources().getColor(R.color.title_disable_red));
            rb_complete.setTextColor(main.getResources().getColor(R.color.title_disable_red));
        } else if (step == step3) {
            rb_information.setChecked(true);
            rb_sign.setTextColor(main.getResources().getColor(R.color.title_disable_red));
            rb_payment.setTextColor(main.getResources().getColor(R.color.title_disable_red));
            rb_information.setTextColor(main.getResources().getColor(R.color.white));
            rb_complete.setTextColor(main.getResources().getColor(R.color.title_disable_red));
        } else if (step == step4) {
            rb_complete.setChecked(true);
            rb_sign.setTextColor(main.getResources().getColor(R.color.title_disable_red));
            rb_payment.setTextColor(main.getResources().getColor(R.color.title_disable_red));
            rb_information.setTextColor(main.getResources().getColor(R.color.title_disable_red));
            rb_complete.setTextColor(main.getResources().getColor(R.color.white));
        }

//左上右下
        rb_sign.setPadding(36, 0, 0, 0);
        rb_payment.setPadding(0, 0, 0, 0);
        rb_payment.setCompoundDrawablePadding(12);
        rb_information.setPadding(0, 0, 0, 0);
        rb_complete.setPadding(0, 0, 0, 0);
        rb_complete.setCompoundDrawablePadding(12);
    }

    void changeTitleStatus(int frag) {
        PayMainActivity payMainActivity = (PayMainActivity) main;
        ActionBar actionBar = payMainActivity.getSupportActionBar();
        assert actionBar != null;
        if (frag == ConstantValue.OS1_CONFIRM_CONT_FRAG) {
            actionBar.setTitle("签订合同");
            colorChange(step1);
        } else if (frag == ConstantValue.OS1_EDIT_CONT_FRAG) {
            actionBar.setTitle("签订合同");
            colorChange(step1);
        } else if (frag == ConstantValue.OS2_DEPOSIT_FRAG) {
            actionBar.setTitle("支付订金");
            colorChange(step2);
        } else if (frag == ConstantValue.OS2_PAY_ONLINE_FRAG) {
            actionBar.setTitle("线上付款");
            colorChange(step2);
        } else if (frag == ConstantValue.OS3_COMPLETE_INFO_FRAG) {
            actionBar.setTitle("完善信息");
            colorChange(step3);
        } else if (frag == ConstantValue.OS4_COMPLETE_FRAG) {
            actionBar.setTitle("完成订单");
            colorChange(step4);
        }
    }
}

