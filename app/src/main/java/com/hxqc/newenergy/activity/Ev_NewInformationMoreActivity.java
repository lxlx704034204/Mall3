package com.hxqc.newenergy.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.hxqc.newenergy.adapter.EVRecommendedMoreAdapter;

import hxqc.mall.R;

/**
 * 说明: 最新资讯
 * author: 何玉
 * since: 2016/3/16.
 * Copyright:恒信汽车电子商务有限公司
 */
public class Ev_NewInformationMoreActivity extends ToolBarActivity {
    private ListView mNewInformation_More_Listview;
    private EVRecommendedMoreAdapter mEV_Adapter_NewInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_information_more);
        toolbarInit();
        mNewInformation_More_Listview = (ListView) findViewById(R.id.newinformation_more_list);
        mEV_Adapter_NewInformation = new EVRecommendedMoreAdapter(Ev_NewInformationMoreActivity.this);
        mNewInformation_More_Listview.setAdapter(mEV_Adapter_NewInformation);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();

    }

    @Override
    public void onResume() {
        super.onResume();
        closePopWindowMenu();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
