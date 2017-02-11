package com.hxqc.pay.activity;

import android.os.Bundle;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.pay.fragment.FinishDepositFragment;

import hxqc.mall.R;

public class FinishDepositActivity extends BackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_deposit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("支付订金");
        FinishDepositFragment fragment = new FinishDepositFragment();
        getFragmentManager().beginTransaction().add(R.id.fl_finish_deposit, fragment).commit();

    }


}
