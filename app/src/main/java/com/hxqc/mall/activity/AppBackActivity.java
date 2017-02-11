package com.hxqc.mall.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.hxqc.mall.api.ApiClient;
import com.hxqc.mall.core.util.SharedPreferencesHelper;

/**
 * 说明:
 * <p/>
 * author: 吕飞
 * since: 2015-05-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class AppBackActivity extends BackActivity {
    protected ApiClient mApiClient;
    protected SharedPreferencesHelper mSharedPreferencesHelper;
    public InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiClient = new ApiClient();
        mSharedPreferencesHelper = new SharedPreferencesHelper(this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

}
