package com.hxqc.mall.activity.recharge;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.model.recharge.RechargeResult;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.util.JSONUtils;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-03-10
 * FIXME
 * Todo 充值成功界面
 */
public class RechargeSuccessActivity extends NoBackActivity {
    public static final String DATA_TAG = "com.hxqc.mall.activity.recharge.BackActivity_data_tag";
    //    private RechargeRequest rechargeRequest;
    private TextView memberName, memberPhone, chargeNumber, pointAwarded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_recharge_successed);
//        rechargeRequest = (RechargeRequest) getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getSerializable(DATA_TAG);
        memberName = (TextView) findViewById(R.id.member_name);
        memberPhone = (TextView) findViewById(R.id.member_phone);
        chargeNumber = (TextView) findViewById(R.id.charge_number);
        pointAwarded = (TextView) findViewById(R.id.point_awarded);

        loadData();
    }

    private String getOrderID() {
        return getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(DATA_TAG);
    }

    private void upView(RechargeResult rechargeRequest) {
        memberName.setText(rechargeRequest.name);
        if (!TextUtils.isEmpty(rechargeRequest.phoneNumber))
            if (rechargeRequest.phoneNumber.length() == 11) {
                String phone_number = rechargeRequest.phoneNumber;
                String substring1 = phone_number.substring(0, 3);
                String substring2 = phone_number.substring(3, 7);
                String substring3 = phone_number.substring(7, 11);
                memberPhone.setText(String.format("%s %s %s", substring1, substring2, substring3));
            } else
                memberPhone.setText(rechargeRequest.phoneNumber);
        chargeNumber.setText(String.format("%s元", rechargeRequest.money));
        pointAwarded.setText(String.format("%s分", rechargeRequest.score));
    }

    private void loadData() {
        new UserApiClient().orderDetail(getOrderID(), new LoadingAnimResponseHandler(this, true) {
            @Override
            public void onSuccess(String response) {
                RechargeResult rechargeResult = JSONUtils.fromJson(response, RechargeResult.class);
                if (rechargeResult != null)
                    upView(rechargeResult);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
