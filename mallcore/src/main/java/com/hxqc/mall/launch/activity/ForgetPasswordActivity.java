package com.hxqc.mall.launch.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.hxqc.mall.core.R;
import com.hxqc.mall.launch.fragment.ForgetPasswordStep1Fragment;
import com.hxqc.mall.launch.fragment.ForgetPasswordStep2Fragment;
import com.hxqc.mall.launch.fragment.ForgetPasswordStep3Fragment;
import com.hxqc.mall.launch.util.ActivitySwitchAuthenticate;
import com.umeng.analytics.MobclickAgent;

/**
 * 说明:忘记密码，找回密码
 *
 * author: 吕飞
 * since: 2015-03-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class ForgetPasswordActivity extends BaseAuthenticateActivity {
    public String mPhoneNumber;
    public String mIdentifyingCode;
    public String mWrotePhoneNumber = "";
    public int mStep = 1;
    public FragmentManager mFragmentManager;
    public static final int FORGET_PASSWORD_STEP_1 = 1;
    public static final int FORGET_PASSWORD_STEP_2 = 2;
    public static final int FORGET_PASSWORD_STEP_3 = 3;
    public ForgetPasswordStep1Fragment mForgetPasswordStep1Fragment = new ForgetPasswordStep1Fragment();
    public ForgetPasswordStep2Fragment mForgetPasswordStep2Fragment = new ForgetPasswordStep2Fragment();
    public ForgetPasswordStep3Fragment mForgetPasswordStep3Fragment = new ForgetPasswordStep3Fragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_only_fragment);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWrotePhoneNumber = getIntent().getStringExtra(ActivitySwitchAuthenticate.WROTE_NUM);
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.fragment_right_in, R.anim.fragment_left_out);
        mFragmentTransaction.add(R.id.fragment_container, mForgetPasswordStep1Fragment);
        mFragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        back();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        switch (mStep) {
            case FORGET_PASSWORD_STEP_1:
            case FORGET_PASSWORD_STEP_2:
                finish();
                break;
            case FORGET_PASSWORD_STEP_3:
                if (mForgetPasswordStep3Fragment.mIsChangePasswordActivity) {
                    ActivitySwitchAuthenticate.toMain(this, 2);
                }
                finish();
                break;
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
