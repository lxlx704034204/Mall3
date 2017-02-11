package com.hxqc.mall.fragment;

import android.content.Context;

import com.hxqc.mall.api.ApiClient;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.SharedPreferencesHelper;

/**
 * * 说明:Fragment基类
 *
 * author: 吕飞
 * since: 2015-03-12
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class BaseFragment extends FunctionFragment {
    public SharedPreferencesHelper mSharedPreferencesHelper;
    public ApiClient mApiClient;
    public Context mContext;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mContext = activity;
        mSharedPreferencesHelper = new SharedPreferencesHelper(mContext);
        mApiClient = new ApiClient();
    }


}
