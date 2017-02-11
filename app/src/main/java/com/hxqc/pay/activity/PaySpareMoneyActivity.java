package com.hxqc.pay.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.paymethodlibrary.util.PayMethodConstant;
import com.hxqc.pay.fragment.NoDataFragment;
import com.hxqc.pay.fragment.OrderStep2PayOnlineFragment;
import com.hxqc.pay.inter.OnFragmentChangeListener;
import com.hxqc.pay.util.ConstantValue;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.EventBus;

import hxqc.mall.R;

/**
 * 支付余款
 */

public class PaySpareMoneyActivity extends BackActivity implements OnFragmentChangeListener {

    InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_spare_money);

        //手势关闭
//        SlidrConfig config = new SlidrConfig.Builder()
//                .primaryColor(getResources().getColor(R.color.toast_yellow))
//                .sensitivity(1f).build();
//        Slidr.attach(this, config);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("付款");

        if (TextUtils.isEmpty(getIntent().getStringExtra(ConstantValue.PAY_MAIN_ORDER_ID))) {
            getFragmentManager().beginTransaction().add(R.id.fl_only_pay, new NoDataFragment()).commit();

        }else {
            OrderStep2PayOnlineFragment fragment = new OrderStep2PayOnlineFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ConstantValue.PAY_MAIN_ORDER_ID, getIntent().getStringExtra(ConstantValue.PAY_MAIN_ORDER_ID));
            bundle.putInt(PayConstant.PAY_STATUS_FLAG,getIntent().getIntExtra(PayConstant.PAY_STATUS_FLAG,PayConstant.PAY_ONLY_ONLINE_PAID));
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().add(R.id.fl_only_pay, fragment).commit();
        }




    }


    @Override
    public void OnFragmentChange(int s, Fragment fragmentToOpen) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DebugLog.i("yijipay", "onActivityResult--p" + resultCode);
        if (requestCode == PayConstant.YJF_PAY_REQUEST_CODE) //支付成功
        {
            EventBus.getDefault().post(new EventGetSuccessModel(resultCode,"",PayMethodConstant.MICROPAY_TYPE));
        }
    }


}
