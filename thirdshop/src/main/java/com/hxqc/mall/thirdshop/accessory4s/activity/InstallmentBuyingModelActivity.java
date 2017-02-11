package com.hxqc.mall.thirdshop.accessory4s.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.adapter.InstallmentBuyingModelAdapter;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 16
 * Des: 分期购车-车型
 * FIXME
 * Todo
 */

public class InstallmentBuyingModelActivity extends BackActivity {

    private RecyclerView mModelListView;
    private InstallmentBuyingModelAdapter mInstallmentBuyingModelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_installment_buying_model);

        initView();

        initData();

        initEvent();
    }

    private void initData() {

        initAdapter();
    }

    private void initAdapter() {
        mModelListView.setLayoutManager(new LinearLayoutManager(this));
        mModelListView.setHasFixedSize(true);
        if (mInstallmentBuyingModelAdapter == null) {
            mInstallmentBuyingModelAdapter = new InstallmentBuyingModelAdapter(this);
            mModelListView.setAdapter(mInstallmentBuyingModelAdapter);
        }
    }

    private void initEvent() {

    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("奔驰GLA");
        mModelListView = (RecyclerView) findViewById(R.id.instalment_buying_model_list);
    }

}
