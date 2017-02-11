package com.hxqc.mall.views.order;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;

import hxqc.mall.R;

/**
 * Author: wanghao
 * Date: 2016-01-18
 * FIXME
 * Todo
 */
public class UnloginOrderView extends RelativeLayout {

    private Button mLogin;


    public UnloginOrderView(Context context) {
        super(context);
        initView();
    }

    public UnloginOrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_unlogin_order, this);
        mLogin = (Button) findViewById(R.id.login);
        mLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivitySwitchAuthenticate.toLogin(getContext());
                UserInfoHelper.getInstance().loginAction(getContext(), new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ActivitySwitchBase.toMain(getContext(), 2);
                    }
                });
            }
        });
    }


}
