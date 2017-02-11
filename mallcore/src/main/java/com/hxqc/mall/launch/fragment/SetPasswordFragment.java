package com.hxqc.mall.launch.fragment;

import android.content.Context;

import com.hxqc.mall.launch.activity.ChangePasswordActivity;


/**
 * 说明:找密码和换密码基类fragment
 *
 * author: 吕飞
 * since: 2015-03-24
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class SetPasswordFragment extends BaseLaunchFragment {
    public boolean mIsChangePasswordActivity;//是否是ChangePasswordActivity

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mIsChangePasswordActivity = activity instanceof ChangePasswordActivity;
    }
}
