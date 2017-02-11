package com.hxqc.newenergy.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.newenergy.adapter.EVCarDealsAdapter;
import com.hxqc.newenergy.api.NewEnergyApiClient;
import com.hxqc.newenergy.bean.PromotionAuto;
import com.hxqc.newenergy.util.EVSharePreferencesHelper;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:新能源特卖页面
 * author: 何玉
 * since: 2016年3月9日
 * Copyright:恒信汽车电子商务有限公司
 */
public class Ev_NewEnergyCarTemaiActivity extends ToolBarActivity {
    private ListView mNewWenergyCarDeals_List = null;//新能源汽车特卖列表
    private EVCarDealsAdapter mEvCarDeals_adapter = null;//新车特卖列表适配器

    NewEnergyApiClient mNewEnergy_ApiClient;
    ArrayList< PromotionAuto > mSpecialOffers;//特卖数据
    //    Timer mTimer;
    String itemCategory;//特卖类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_newenergyvartemai);
        mNewWenergyCarDeals_List = (ListView) findViewById(R.id.newwenergycarmai_list);
        mEvCarDeals_adapter = new EVCarDealsAdapter(Ev_NewEnergyCarTemaiActivity.this);
        mNewEnergy_ApiClient = new NewEnergyApiClient();
        toolbarInit();
        getSpecialOffer();
    }

    /**
     * 获取特卖列表
     */
    public void getSpecialOffer() {

        mNewEnergy_ApiClient.getSpecialOffer(new EVSharePreferencesHelper(Ev_NewEnergyCarTemaiActivity.this)
                .getCity(), 20, 15, 1, new LoadingAnimResponseHandler(this, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList< PromotionAuto > objectArrayList = JSONUtils.fromJson(response, new TypeToken< ArrayList< PromotionAuto > >() {
                });


                mEvCarDeals_adapter.setData(objectArrayList);
                if (mNewWenergyCarDeals_List.getAdapter() == null) {

                    mNewWenergyCarDeals_List.setAdapter(mEvCarDeals_adapter);
                } else {
                    mEvCarDeals_adapter.notifyDataSetChanged();
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
    protected void onDestroy() {
        super.onDestroy();
        mEvCarDeals_adapter.stopTimer();
    }
}
