package com.hxqc.mall.launch.activity;

import android.os.Bundle;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.launch.util.ActivitySwitchAuthenticate;
import com.hxqc.mall.launch.util.LaunchSharedPreferencesHelper;

/**
 * Author: wanghao
 * Date: 2016-01-25
 * FIXME
 * Todo
 */
public class BaseAuthenticateActivity extends BackActivity {
    protected LaunchSharedPreferencesHelper mSharedPreferencesHelper;

    int mEntrance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferencesHelper = new LaunchSharedPreferencesHelper(this);
        mEntrance = getIntent().getIntExtra(ActivitySwitchAuthenticate.ENTRANCE, ActivitySwitchBase.ENTRANCE_SHOP);
    }

    protected void loginAction(final String userName, String password, int loginType) {
        UserInfoHelper.getInstance().loginAction(this, userName, password, loginType);
    }

}
