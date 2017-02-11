package com.hxqc.newenergy.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.newenergy.api.NewEnergyApiClient;
import com.hxqc.newenergy.bean.EVNewWenergySubsidyDatailBean;
import com.hxqc.newenergy.view.ModelsandsubsidiesDatailView;
import com.hxqc.util.JSONUtils;

import hxqc.mall.R;

/**
 * 说明:   补贴详情界面
 * author: 何玉
 * since: 2016年3月9日
 * Copyright:恒信汽车电子商务有限公司
 */
public class EV_ModelsandsubsidiesDatailActivity extends ToolBarActivity {


    BaseSharedPreferencesHelper mBaseSharedPreferencesHelper;
    NewEnergyApiClient mNewEnergy_ApiClient;
    String mAutoId;
    String area;
    ModelsandsubsidiesDatailView mModelsandsubsidiesDatailView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_modelsandsubsidies_datail);
        toolbarInit();
        if (!TextUtils.isEmpty(getIntent().getStringExtra("autoID"))) {
            mAutoId = getIntent().getStringExtra("autoID");
        }
        area = TextUtils.isEmpty(getIntent().getStringExtra("area")) ? "武汉市" : getIntent().getStringExtra("area");
        mBaseSharedPreferencesHelper = new BaseSharedPreferencesHelper(EV_ModelsandsubsidiesDatailActivity.this);
        mNewEnergy_ApiClient = new NewEnergyApiClient();

        inIt();

        getSubsidiesData();

    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    public void inIt() {

        mModelsandsubsidiesDatailView = (ModelsandsubsidiesDatailView) findViewById(R.id.ModelsandsubsidiesDatailView);
    }

    public void getSubsidiesData() {

        mNewEnergy_ApiClient.getSubsidyDetailData(area, mAutoId, new LoadingAnimResponseHandler(this, true) {
            @Override
            public void onSuccess(String response) {
                EVNewWenergySubsidyDatailBean data = JSONUtils.fromJson(response, EVNewWenergySubsidyDatailBean.class);
                if (data != null) {

                    mModelsandsubsidiesDatailView.setData(data);
                } else {
                    mModelsandsubsidiesDatailView.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        closePopWindowMenu();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
