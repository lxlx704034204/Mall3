package com.hxqc.mall.activity.order;

import android.os.Bundle;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.fragment.order.MyOrderEntranceFragment;

import hxqc.mall.R;

public class MyOrderActivity extends NoBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        MyOrderEntranceFragment fragment = (MyOrderEntranceFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myorder_fragment);
        fragment.showBackButton();
    }

}
