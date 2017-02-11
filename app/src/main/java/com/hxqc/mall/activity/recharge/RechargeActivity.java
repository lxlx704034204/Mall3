package com.hxqc.mall.activity.recharge;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.control.RechargeHelper;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.interfaces.LoadDataCallBack;
import com.hxqc.mall.core.model.AmountConfig;
import com.hxqc.mall.core.model.recharge.PrepaidHistory;
import com.hxqc.mall.core.model.recharge.RechargeRequest;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.dialog.LoadingDialog;
import com.hxqc.mall.views.recharge.ChooseNumberView;
import com.hxqc.mall.views.recharge.InputPhoneView;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-02-29
 * FIXME
 * Todo 充值界面
 */
public class RechargeActivity extends BackActivity implements
        View.OnClickListener, ChooseNumberView.OnNumberListener, TextView.OnEditorActionListener,
        InputPhoneView.OnEditNextListener, ChooseNumberView.OnEditListener {
    private InputPhoneView inputPhoneView;
    private ChooseNumberView chooseNumberView;
    private RechargeRequest rechargeRequest;
    private TextView send_score;
    private int max = -1;
    private LoadingDialog dialog;
    private LoadingDialog dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        rechargeRequest = new RechargeRequest();
        dialog = new LoadingDialog(this);
        dialog2 = new LoadingDialog(this);
        initView();
        initEvent();
        loadHistory();
        loadAmountConfig();
    }

    private void initEvent() {
        findViewById(R.id.commit_charge).setOnClickListener(this);
        chooseNumberView.setOnNumberListener(this);
        chooseNumberView.getEditText().setOnEditorActionListener(this);
        send_score.setOnClickListener(this);
        inputPhoneView.setOnEditNextListener(this);
        chooseNumberView.setOnEditListener(this);
    }

    private void initView() {
        inputPhoneView = (InputPhoneView) findViewById(R.id.input_view);
        inputPhoneView.setPhoneNumber(UserInfoHelper.getInstance().getPhoneNumber(this));
        chooseNumberView = (ChooseNumberView) findViewById(R.id.choose_number);
        send_score = (TextView) findViewById(R.id.send_score);
    }

    private void loadAmountConfig() {
        dialog.show();
        RechargeHelper.getIntance(this).loadAmountConfig(new LoadDataCallBack<AmountConfig>() {
            @Override
            public void onDataNull(String message) {

            }

            @Override
            public void onDataGot(AmountConfig amountConfig) {
                if (dialog.isShowing() && dialog != null)
                    dialog.dismiss();
                chooseNumberView.setVisibility(View.VISIBLE);
                chooseNumberView.setTxts(amountConfig.list);
                max = amountConfig.max;
                chooseNumberView.setMax(max);
            }
        });
    }

    private void loadHistory() {
        dialog2.show();
        RechargeHelper.getIntance(this).loadRecord(new LoadDataCallBack<ArrayList<PrepaidHistory>>() {
            @Override
            public void onDataNull(String message) {

            }

            @Override
            public void onDataGot(ArrayList<PrepaidHistory> prepaidHistories) {
                if (dialog2.isShowing() && dialog2 != null)
                    dialog2.dismiss();
                inputPhoneView.addHistory(prepaidHistories);
                inputPhoneView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_score:
                //送积分
                //去积分规则
                break;
            case R.id.commit_charge:
                //提交
                commit();
                break;
        }
    }

    /**
     * 提交
     */
    private void commit() {
        //去充值
        /*模拟*/
//        ActivitySwitcher.toPaymentWayList(this, new RechargeRequest());
//        finish();

        String phoneNumber = inputPhoneView.phoneNumber();
        String name = inputPhoneView.name();
        float number = chooseNumberView.getNumber();
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastHelper.showRedToast(this, getString(R.string.empty_charge_phone));
            return;
        }
        if (number == -1) {
            ToastHelper.showRedToast(this, getString(R.string.empty_charge_number));
            return;
        }
        rechargeRequest.phone_number = phoneNumber;
        rechargeRequest.charge_number = number + "";
        rechargeRequest.name = name;
        RechargeHelper.getIntance(this).createOrder(phoneNumber, number + "", new LoadDataCallBack<String>() {
            @Override
            public void onDataNull(String message) {
                ToastHelper.showRedToast(RechargeActivity.this, message);
            }

            @Override
            public void onDataGot(String orderID) {
                rechargeRequest.orderID = orderID;
//                ActivitySwitcher.toPaymentWayList(RechargeActivity.this, rechargeRequest);
                ActivitySwitcher.toRechargePayList(RechargeActivity.this, rechargeRequest);
                finish();
            }
        });
    }

    @Override
    public void onNumberChange(float number) {
        RechargeHelper.getIntance(this).canGetScore(number + "", new LoadDataCallBack<Integer>() {
            @Override
            public void onDataNull(String message) {
                send_score.setVisibility(View.GONE);
            }

            @Override
            public void onDataGot(Integer obj) {
                send_score.setText(String.format("可送积分%d", obj));
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RechargeHelper.getIntance(this).destory();
        dialog2 = null;
        dialog = null;
    }


    @Override
    public void onEditNext() {
        DebugLog.i("onEditNext", "onEditNext");
        inputPhoneView.clearFocus();
        chooseNumberView.getEditText().setFocusable(true);
        chooseNumberView.getEditText().setFocusableInTouchMode(true);
        chooseNumberView.getEditText().requestFocus();
        chooseNumberView.getEditText().requestFocusFromTouch();
    }

    @Override
    public void onEditDone() {
        commit();
    }
}
