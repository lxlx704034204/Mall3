package com.hxqc.mall.activity.recharge;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.auto.util.ScreenUtil;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.model.recharge.RechargeRequest;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.payment.activity.PayBaseActivity;
import com.hxqc.util.OtherUtil;

import hxqc.mall.R;

/**
 * Author:  wh
 * Date:  2016/11/16
 * FIXME
 * Todo
 */

public abstract class RechargePayBaseActivity extends PayBaseActivity {

    public static final String DATA_TAG
            = "com.hxqc.mall.activity.recharge.RechargePayActivity_data_tag ";

    protected RechargeRequest rechargeRequest;
    protected String paymentID;

    @Override
    protected View getTileView() {
        rechargeRequest = (RechargeRequest) getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA)
                .getSerializable(DATA_TAG);
        return getChildHeadView() == null ? defaultTitleView() : getChildHeadView();
    }

    private View defaultTitleView() {
        TextView textView = new TextView(this);
        textView.setText("提交成功，请您尽快充值");
        textView.setTextColor(getResources().getColor(R.color.green_txt));
        textView.setTextSize(20);
        int padding = ScreenUtil.dip2px(this, 16);
        textView.setPadding(padding, padding, padding, padding);
        textView.setBackgroundColor(Color.parseColor("#efefef"));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        return textView;
    }

    @Override
    protected void getPayList() {
        new UserApiClient().listPayment(getPayListResponseHandler(rechargeRequest.charge_number,
                "充值金额：" + OtherUtil.amountFormat(rechargeRequest.charge_number, true)));
    }

    @Override
    public void balancePay(String pwd) {
        //余额支付，充值不支持
    }

    @Override
    public void pay() {
        new UserApiClient().payOnline(rechargeRequest.orderID, mPaymentTypeChoiceView.getPaymentID(), getPayResponseHandler());
    }

    @Override
    public void toFinishPay() {
        ActivitySwitcher.toReChargeSuccess(this, rechargeRequest.orderID);
    }

    @Override
    public void offlinePay() {
        //线下支付，充值不支持
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    @Override
    public boolean onSupportNavigateUp() {
        showExitDialog();
        return false;
    }

    private void showExitDialog() {
        String title = TextUtils.isEmpty(getDialogTitle()) ? "确认要离开付款页面？" : getDialogTitle();
        String content = TextUtils.isEmpty(getDialogContent()) ? "确定离开将丢失充值信息，下次充值需重新填写" : getDialogContent();
        new NormalDialog(this, title, content) {
            @Override
            protected void doNext() {
                dialogDoNext();
            }
        }.show();
    }

    public abstract String getDialogTitle();

    public abstract String getDialogContent();

    public abstract View getChildHeadView();

    public abstract void dialogDoNext();
}
