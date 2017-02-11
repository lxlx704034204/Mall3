package com.hxqc.mall.launch.fragment;

import android.content.Context;

import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.launch.util.LaunchSharedPreferencesHelper;

/**
 * Author: wanghao
 * Date: 2016-01-25
 * FIXME
 * Todo
 */
public abstract class BaseLaunchFragment  extends FunctionFragment {

    public LaunchSharedPreferencesHelper mSharedPreferencesHelper;
    public Context mContext;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mContext = activity;
        mSharedPreferencesHelper = new LaunchSharedPreferencesHelper(mContext);
    }

}
