package com.hxqc.aroundservice.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hxqc.aroundservice.adapter.MyQueryOrderAdapter;
import com.hxqc.aroundservice.model.IllegalOrderDetail;
import com.hxqc.mall.activity.BackActivity;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 23
 * FIXME
 * Todo 我的违章订单页面
 */
public class MyIllegalOrderActivity extends BackActivity {

    private RecyclerView mOrderView;
    private MyQueryOrderAdapter myQueryOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_illegal_order);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        if (myQueryOrderAdapter == null) {
            IllegalOrderDetail illegalOrder = new IllegalOrderDetail();
            ArrayList<IllegalOrderDetail> illegalOrders = new ArrayList<IllegalOrderDetail>();
            illegalOrders.add(illegalOrder);
            myQueryOrderAdapter = new MyQueryOrderAdapter(this, illegalOrders);
            mOrderView.setLayoutManager(new LinearLayoutManager(this));
            mOrderView.setHasFixedSize(true);
            mOrderView.setAdapter(myQueryOrderAdapter);
        } else {

        }
    }


    private void initData() {

    }

    private void initView() {
        mOrderView = (RecyclerView) findViewById(R.id.my_illegal_order);
    }


}
