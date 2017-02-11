package com.hxqc.newenergy.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.newenergy.adapter.EVRecommendedMoreAdapter;
import com.hxqc.newenergy.api.NewEnergyApiClient;
import com.hxqc.newenergy.bean.EVNewenergyAutoSample;
import com.hxqc.newenergy.util.EVSharePreferencesHelper;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:
 * author: 何玉
 * since: 2016/3/16.
 * Copyright:恒信汽车电子商务有限公司
 */
public class Ev_RecommendMoreActivity extends ToolBarActivity {
    private ListView mRecommended_More_Listview;

    private EVRecommendedMoreAdapter mEVRecommendAdapter;

    int count = 15, page = 1;

    NewEnergyApiClient mNewEnergy_ApiClient;
    EVSharePreferencesHelper EVSharePreferencesHelper;
    String mRecommenedType ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_recommended_more);
        toolbarInit();
        mRecommenedType = getIntent().getStringExtra("recommendType");
        mNewEnergy_ApiClient = new NewEnergyApiClient();
        EVSharePreferencesHelper = new EVSharePreferencesHelper(Ev_RecommendMoreActivity.this);
        mRecommended_More_Listview = (ListView) findViewById(R.id.recommended_more_list);
        mEVRecommendAdapter = new EVRecommendedMoreAdapter(Ev_RecommendMoreActivity.this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        getRecommendListData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();

    }

    public void getRecommendListData() {

        mNewEnergy_ApiClient.getRecommendListData(EVSharePreferencesHelper.getCity(),
                EVSharePreferencesHelper.getLatitudeBD(), EVSharePreferencesHelper.getLongitudeBD(), count,
                page, mRecommenedType, new LoadingAnimResponseHandler(this, true) {
                    @Override
                    public void onSuccess(String response) {
//               RecommendBean  mRecommend_list = JSONUtils.fromJson(response, RecommendBean.class );
                        ArrayList< EVNewenergyAutoSample > mRecommend_list =
                                JSONUtils.fromJson(response, new TypeToken< ArrayList< EVNewenergyAutoSample > >() {
                        });

                        if (mRecommend_list != null) {

                            mEVRecommendAdapter.setData(mRecommend_list);
                            if (mRecommended_More_Listview.getAdapter() == null) {
                                mRecommended_More_Listview.setAdapter(mEVRecommendAdapter);
                            } else {
                                mEVRecommendAdapter.notifyDataSetChanged();
                            }

                        } else {

                            mRecommended_More_Listview.setVisibility(View.GONE);
                        }
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        closePopWindowMenu();
    }

}
