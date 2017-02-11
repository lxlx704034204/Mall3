package com.hxqc.pay.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.model.OrderPayRequest;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.paymethodlibrary.util.PayMethodConstant;
import com.hxqc.pay.fragment.NoDataFragment;
import com.hxqc.pay.fragment.OrderStep1EditContractFragment;
import com.hxqc.pay.fragment.OrderStep2DepositFragment;
import com.hxqc.pay.fragment.OrderStep2PayOnlineFragment;
import com.hxqc.pay.fragment.OrderStep3CompleteInformationFragment;
import com.hxqc.pay.inter.OnFragmentChangeListener;
import com.hxqc.pay.ui.TitleManager;
import com.hxqc.pay.util.ConstantValue;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.EventBus;

import hxqc.mall.R;


public class PayMainActivity extends NoBackActivity implements OnFragmentChangeListener {

    int frag_step = ConstantValue.OS1_EDIT_CONT_FRAG;
    TitleManager titleManager;
    InputMethodManager inputMethodManager;
    BaseSharedPreferencesHelper sharedPreferencesHelper;
    int pay_type = PayConstant.PAY_FLOW_NORMAL_ONLINE;
   public Toolbar mToolBar;

    @Override
    public void OnFragmentChange(int s, Fragment fragmentToOpen) {
        frag_step = s;
        titleManager.changeTitle(this, s);
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().setCustomAnimations(
                R.animator.slide_fragment_in,
                R.animator.slide_fragment_out,
                R.animator.slide_fragment_in,
                R.animator.slide_fragment_out
        ).add(R.id.fragment_place, fragmentToOpen).addToBackStack(null).commitAllowingStateLoss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mToolBar = (Toolbar) findViewById(R.id.toolbar_pay);
        mToolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backOut();
            }
        });

        sharedPreferencesHelper = new BaseSharedPreferencesHelper(this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        titleManager = TitleManager.getInstance();
        OrderPayRequest requestOrder = getIntent().getParcelableExtra(ConstantValue.ORDER_PAY_REQUEST);
        pay_type = getIntent().getIntExtra(PayConstant.PAY_STATUS_FLAG, PayConstant.PAY_ONLY_ONLINE_PAID);
        String order_id = getIntent().getStringExtra(ConstantValue.PAY_MAIN_ORDER_ID);

        DebugLog.i("PayMainActivity", pay_type + "");
//        DebugLog.i("PayMainActivity", order_id);
        if (requestOrder == null && TextUtils.isEmpty(order_id) && getIntent().getParcelableExtra(ConstantValue.ORDER_DATA) == null) {
            titleManager.changeTitle(this, ConstantValue.OS1_EDIT_CONT_FRAG);
            getFragmentManager().beginTransaction().add(R.id.fragment_place, new NoDataFragment()).commit();

        } else {
            //TODO 获取数据判断   数据与接收的   支付方式
            Bundle mData = new Bundle();
            //正常线上线下未签合同
            if (pay_type == PayConstant.PAY_FLOW_DEPOSIT_ONLINE || pay_type == PayConstant.PAY_FLOW_NORMAL_ONLINE) {
                OrderStep1EditContractFragment editContractFragment = new OrderStep1EditContractFragment();
                titleManager.changeTitle(this, frag_step);
                mData.putParcelable(ConstantValue.ORDER_PAY_REQUEST, requestOrder);
                mData.putInt(PayConstant.PAY_STATUS_FLAG, pay_type);
                editContractFragment.setArguments(mData);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_place, editContractFragment).commit();
            }
            //普通订单 已签合同 未付款 线上
//            else if (pay_type == PayConstant.NORMAL_HAD_CONTRACT_ONLINE) {
//                OrderStep2PayOnlineFragment fragment = new OrderStep2PayOnlineFragment();
//                titleManager.changeTitle(this, ConstantValue.OS2_PAY_ONLINE_FRAG);
//                mData.putString(ConstantValue.pay_main_order_id, order_id);
//                mData.putInt(PayConstant.PAY_STATUS_FLAG, pay_type);
//                fragment.setArguments(mData);
//                getFragmentManager().beginTransaction().add(R.id.fragment_place, fragment).commit();
//            }

            //普通订单 已签合同 未付款 线下,特卖线下合同已签
            else if (pay_type == PayConstant.NORMAL_HAD_CONTRACT_UNDERLINE || pay_type == PayConstant.SECKILL_HAD_CONTRACT_UNDERLINE) {
                OrderStep2DepositFragment fragment = new OrderStep2DepositFragment();
                titleManager.changeTitle(this, ConstantValue.OS2_DEPOSIT_FRAG);
                mData.putString(ConstantValue.PAY_MAIN_ORDER_ID, order_id);
                mData.putInt(PayConstant.PAY_STATUS_FLAG, pay_type);
                fragment.setArguments(mData);
                getFragmentManager().beginTransaction().add(R.id.fragment_place, fragment).commit();

            }
            //特卖线上线下流程未签合同
            else if (pay_type == PayConstant.PAY_ONLY_DEPOSIT_ONLINE || pay_type == PayConstant.PAY_ONLY_DEPOSIT_UNDERLINE) {
                OrderStep1EditContractFragment editContractFragment = new OrderStep1EditContractFragment();
                titleManager.changeTitle(this, frag_step);
                requestOrder = new OrderPayRequest();
                requestOrder.isSeckill = "1";
                requestOrder.orderID = order_id;
                mData.putParcelable(ConstantValue.ORDER_PAY_REQUEST, requestOrder);
                mData.putInt(PayConstant.PAY_STATUS_FLAG, pay_type);
                editContractFragment.setArguments(mData);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_place, editContractFragment).commit();
            }//特卖线上合同已签,  已有订单 支付部分或者未支付  未完善信息,  普通订单 已签合同 未付款 线上
            else if (pay_type == PayConstant.SECKILL_HAD_CONTRACT_ONLINE || pay_type == PayConstant.uncomplete_info_pay_part || pay_type == PayConstant.NORMAL_HAD_CONTRACT_ONLINE) {
                OrderStep2PayOnlineFragment fragment = new OrderStep2PayOnlineFragment();
                titleManager.changeTitle(this, ConstantValue.OS2_PAY_ONLINE_FRAG);
                mData.putString(ConstantValue.PAY_MAIN_ORDER_ID, order_id);
                mData.putInt(PayConstant.PAY_STATUS_FLAG, pay_type);
                fragment.setArguments(mData);
                getFragmentManager().beginTransaction().add(R.id.fragment_place, fragment).commit();
            }
            //特卖线下合同已签

//            else if (pay_type == PayConstant.SECKILL_HAD_CONTRACT_UNDERLINE) {
//                FinishDepositFragment fragment = new FinishDepositFragment();
//                titleManager.changeTitle(this, ConstantValue.OS2_PAY_ONLINE_FRAG);
//                mData.putString(ConstantValue.pay_main_order_id, order_id);
//                mData.putInt(PayConstant.PAY_STATUS_FLAG, pay_type);
//                fragment.setArguments(mData);
//                getFragmentManager().beginTransaction().add(R.id.fragment_place, fragment).commit();
//            }

            // 已有订单 支付部分或者未支付  未完善信息
//            else if (pay_type == PayConstant.uncomplete_info_pay_part){
//
//                OrderStep2PayOnlineFragment fragment = new OrderStep2PayOnlineFragment();
//                titleManager.changeTitle(this, ConstantValue.OS2_PAY_ONLINE_FRAG);
//                mData.putString(ConstantValue.pay_main_order_id, order_id);
//                mData.putInt(PayConstant.PAY_STATUS_FLAG, pay_type);
//                fragment.setArguments(mData);
//                getFragmentManager().beginTransaction().add(R.id.fragment_place, fragment).commit();
//
//            }
            //            //已有订单完善信息
            else if (pay_type == PayConstant.JUST_COMPLETE_INFO) {

                OrderStep3CompleteInformationFragment fragment = new OrderStep3CompleteInformationFragment();
                titleManager.changeTitle(this, ConstantValue.OS3_COMPLETE_INFO_FRAG);
                mData.putString(ConstantValue.PAY_MAIN_ORDER_ID, order_id);
                fragment.setArguments(mData);
                getFragmentManager().beginTransaction().add(R.id.fragment_place, fragment).commit();

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (titleManager!=null)
            titleManager.onDestroy();
    }

    final String back_str = "确定退出购买流程吗？";

    private void backOut() {
        int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        if (!isCf) {
            if (
                    pay_type == PayConstant.PAY_ONLY_DEPOSIT_ONLINE ||
                    pay_type == PayConstant.PAY_FLOW_NORMAL_ONLINE
                    ) {
                if (backStackEntryCount == 1 || backStackEntryCount == 3) {

                    if (backStackEntryCount == 3) {
                        titleManager.changeTitle(this, ConstantValue.OS2_PAY_ONLINE_FRAG);
                    }
                    getFragmentManager().popBackStack();
                } else {
                    if (backStackEntryCount == 0) {
                        backDialog(back_str);
                    } else {
                        backDialog(back_str);
                    }
                }
            } else if (
                    pay_type == PayConstant.PAY_ONLY_DEPOSIT_UNDERLINE ||
                            pay_type == PayConstant.PAY_FLOW_DEPOSIT_ONLINE ||
                    pay_type == PayConstant.uncomplete_info_pay_part ||
                            pay_type == PayConstant.SECKILL_HAD_CONTRACT_ONLINE ||
                            pay_type == PayConstant.NORMAL_HAD_CONTRACT_ONLINE
                    ) {
                if (backStackEntryCount == 1) {
                    getFragmentManager().popBackStack();
                } else {
                    backDialog(back_str);
                }
            } else if (
                    pay_type == PayConstant.JUST_COMPLETE_INFO ||

                            pay_type == PayConstant.SECKILL_HAD_CONTRACT_UNDERLINE ||
                            pay_type == PayConstant.NORMAL_HAD_CONTRACT_UNDERLINE
                    ) {
                backDialog(back_str);
            } else {
                backDialog(back_str);
            }
        } else {
            ActivitySwitchBase.toMain(PayMainActivity.this,0);
            finish();
        }
    }

    //    是否是完成页
    boolean isCf = false;

    public void setCompleteFrag(boolean isCf) {
        this.isCf = isCf;
    }

    //退出提示框
    public void backDialog(String text) {

        NormalDialog dialog = new NormalDialog(PayMainActivity.this, text) {
            @Override
            protected void doNext() {
                finish();
            }
        };
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        DebugLog.i("iscf", isCf + "-----");
        backOut();

    }


    //关闭软件盘 touch事件
    @Override
    public boolean dispatchTouchEvent(@Nullable MotionEvent ev) {



        if (ev != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

//            if (isFastDoubleClick()) {
//                return true;
//            }
        }

        return super.dispatchTouchEvent(ev);
    }

//    public  boolean isFastDoubleClick() {
//        long time = System.currentTimeMillis();
//        long timeD = time - lastClickTime;
//        if (timeD >= 0 && timeD <= 1000) {
//            return true;
//        } else {
//            lastClickTime = time;
//            return false;
//        }
//    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DebugLog.i("yijipay", "onActivityResult--p" + resultCode);
        if (requestCode == PayConstant.YJF_PAY_REQUEST_CODE) //支付成功
        {
            EventBus.getDefault().post(new EventGetSuccessModel(resultCode, "", PayMethodConstant.MICROPAY_TYPE));
        }
    }

}
