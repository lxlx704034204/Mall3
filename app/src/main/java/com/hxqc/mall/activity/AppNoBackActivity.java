package com.hxqc.mall.activity;

import android.os.Bundle;

import com.hxqc.mall.api.ApiClient;
import com.hxqc.mall.core.util.SharedPreferencesHelper;

/**
 * 说明:
 *
 * author: 吕飞
 * since: 2015-05-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class AppNoBackActivity extends NoBackActivity {
    protected ApiClient mApiClient;
    protected SharedPreferencesHelper mSharedPreferencesHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiClient=new ApiClient();
        mSharedPreferencesHelper=new SharedPreferencesHelper(this);
    }
}
