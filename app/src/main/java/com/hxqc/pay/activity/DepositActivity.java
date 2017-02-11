package com.hxqc.pay.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.paymethodlibrary.util.PayMethodConstant;
import com.hxqc.pay.fragment.NoDataFragment;
import com.hxqc.pay.fragment.OrderStep2DepositFragment;
import com.hxqc.pay.inter.OnFragmentChangeListener;
import com.hxqc.pay.util.ConstantValue;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.EventBus;

import hxqc.mall.R;


/**
 * 订金支付
 */

public class DepositActivity extends BackActivity implements OnFragmentChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("支付订金");
        String orderID = getIntent().getStringExtra(ConstantValue.PAY_MAIN_ORDER_ID);
        if (TextUtils.isEmpty(orderID)) {
            getFragmentManager().beginTransaction().add(R.id.fl_deposit, new NoDataFragment()).commit();
        } else {
            OrderStep2DepositFragment fragment = new OrderStep2DepositFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ConstantValue.PAY_MAIN_ORDER_ID, orderID);
            bundle.putInt(PayConstant.PAY_STATUS_FLAG, getIntent().getIntExtra(PayConstant.PAY_STATUS_FLAG, PayConstant.PAY_FLOW_NORMAL_ONLINE));
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().add(R.id.fl_deposit, fragment).commit();
        }
    }

    @Override
    public void OnFragmentChange(int s, Fragment fragmentToOpen) {
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
