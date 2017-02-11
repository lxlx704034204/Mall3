package com.hxqc.mall.launch.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;

/**
 * 说明:找密码第三步
 *
 * author: 吕飞
 * since: 2015-03-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class ForgetPasswordStep3Fragment extends SetPasswordFragment {
    Button mLoginView;//登陆

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forget_password_step3, container, false);
        mLoginView = (Button) rootView.findViewById(R.id.login);
        if (mIsChangePasswordActivity) {
            mLoginView.setText(R.string.me_complete_launch);
        } else {
            mLoginView.setText(R.string.me_login_launch);
        }
        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserInfoHelper.getInstance().loginAction(mContext, ActivitySwitchBase.ENTRANCE_SHOP,
                        new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ActivitySwitchBase.toMain(mContext,0);
                    }
                });
                getActivity().finish();
            }
        });
        return rootView;

    }

    @Override
    public String fragmentDescription() {
        if (mIsChangePasswordActivity) {
            return "修改密码第三步";
        } else {
            return "找回密码第三步";
        }
    }
}


