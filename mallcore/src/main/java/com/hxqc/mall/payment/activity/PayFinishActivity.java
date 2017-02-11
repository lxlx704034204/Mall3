package com.hxqc.mall.payment.activity;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.OtherUtil;


/**
 * 说明:支付完成界面
 *
 * @author: 吕飞
 * @since: 2016-03-15
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class PayFinishActivity extends NoBackActivity implements View.OnClickListener {

    String textServer = "如有疑问，请拨打";
    String textNumber = "400-1868-555";
    TextView mCallView;
    Button mFinishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_finish);
        mCallView = (TextView) findViewById(R.id.pay_finish_call);
        mFinishBtn = (Button) findViewById(R.id.btn_deposit_pay_finish);
        mCallView.setText(initTipStr());
        mCallView.setOnClickListener(this);
        mFinishBtn.setOnClickListener(this);
        new BaseSharedPreferencesHelper(this).setOrderChange(true);
    }

    private SpannableStringBuilder initTipStr() {
        String str = textServer + textNumber;
        return OtherUtil.toCallText(str, textServer.length(), str.length(), "#DF2B36");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pay_finish_call) {
            OtherUtil.callPhone(this, textNumber);
        } else if (v.getId() == R.id.btn_deposit_pay_finish) {
            pressButtonFinish();
        }
    }

    protected void pressButtonFinish() {
        finishPay();
        new BaseSharedPreferencesHelper(this).setOrderChange(true);
        ActivitySwitchBase.toMain(this, 2);
        finish();
    }

    //完成之后要做的事
    protected abstract void finishPay();

    @Override
    public void onBackPressed() {
    }
}
