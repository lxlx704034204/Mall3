package com.hxqc.mall.launch.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.hxqc.mall.core.R;
import com.hxqc.mall.core.views.VoiceCaptchaView;
import com.hxqc.mall.launch.activity.ForgetPasswordActivity;
import com.hxqc.mall.launch.api.ApiClientAuthenticate;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.launch.view.InputIdentifyingCodeNoButton;
import com.hxqc.mall.launch.view.InputPhoneVerificationCode;

/**
 * 说明:找密码第一步
 *
 * author: 吕飞
 * since: 2015-03-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class ForgetPasswordStep1Fragment extends SetPasswordFragment implements View.OnClickListener {
    InputPhoneVerificationCode mInputPhoneNumberView;//手机号
    InputIdentifyingCodeNoButton mInputIdentifyingCodeView;//验证码
    Button mNextView;//下一步
    TextView mLoginPCView;
    TextView mWebsiteView;
    TextView mCurrentNumberView;//当前手机号
    VoiceCaptchaView mVoiceCaptchaView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forget_password_step1, container, false);
        ((ForgetPasswordActivity) mContext).mStep = ForgetPasswordActivity.FORGET_PASSWORD_STEP_1;
        mCurrentNumberView = (TextView) rootView.findViewById(R.id.current_number);
        mInputPhoneNumberView = (InputPhoneVerificationCode) rootView.findViewById(R.id.input_phone_number);
        mInputIdentifyingCodeView = (InputIdentifyingCodeNoButton) rootView.findViewById(R.id.input_identifying_code);
        mNextView = (Button) rootView.findViewById(R.id.next);
        mLoginPCView = (TextView) rootView.findViewById(R.id.login_pc);
        mWebsiteView = (TextView) rootView.findViewById(R.id.website);
        if (mIsChangePasswordActivity) {
            mLoginPCView.setVisibility(View.INVISIBLE);
            mWebsiteView.setVisibility(View.INVISIBLE);
            mCurrentNumberView.setVisibility(View.VISIBLE);
            String mNumber = UserInfoHelper.getInstance().getPhoneNumber(getContext());
            mCurrentNumberView.setText(mContext.getResources().getString(R.string.me_user_phone_number) + mNumber.substring(0, 3) + "****" + mNumber.substring(7));
        } else {
            mInputPhoneNumberView.mPhoneNumberView.setText(((ForgetPasswordActivity) mContext).mWrotePhoneNumber);
            mLoginPCView.setVisibility(View.VISIBLE);
            mWebsiteView.setVisibility(View.VISIBLE);
            mCurrentNumberView.setVisibility(View.GONE);
        }
        mInputPhoneNumberView.getCountdownButton().setOnClickListener(this);
        mNextView.setOnClickListener(this);
        mVoiceCaptchaView= (VoiceCaptchaView) rootView.findViewById(R.id.voice_captcha);
        mVoiceCaptchaView.setVoiceCaptchaListener(new VoiceCaptchaView.VoiceCaptchaListener() {
            @Override
            public void getVoiceCaptcha() {
                new ApiClientAuthenticate().isRegistered2(mInputPhoneNumberView.getPhoneNumber(),mVoiceCaptchaView.getVoiceCaptchaResponseHandler());
            }

            @Override
            public String getPhoneNumber() {
                return mInputPhoneNumberView.getPhoneNumber();
            }
        });
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.get_identifying_code){
            if (FormatCheck.checkPhoneNumber(mInputPhoneNumberView.getPhoneNumber(), mContext) == FormatCheck.CHECK_SUCCESS) {
                if (mIsChangePasswordActivity && !(mInputPhoneNumberView.getPhoneNumber().equals(UserInfoHelper.getInstance().getPhoneNumber(getContext())))) {
                    ToastHelper.showYellowToast(mContext, R.string.me_phone_number_error);
                } else {
                    mInputPhoneNumberView.getCountdownButton().identifyingStart(mInputPhoneNumberView.getPhoneNumber());

                }
            }
        }else if (i == R.id.next){
            mNextView.setClickable(false);
            if (canNext()) {
                new ApiClientAuthenticate().sendCode(mInputPhoneNumberView.getPhoneNumber(), mInputIdentifyingCodeView.getIdentifyingCode(), new BaseMallJsonHttpResponseHandler(mContext) {
                    @Override
                    public void onFinish() {
                        mNextView.setClickable(true);
                        super.onFinish();
                    }

                    @Override
                    public void onSuccess(String response) {
                        ((ForgetPasswordActivity) mContext).mPhoneNumber = mInputPhoneNumberView.getPhoneNumber();
                        ((ForgetPasswordActivity) mContext).mIdentifyingCode = mInputIdentifyingCodeView.getIdentifyingCode();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.fragment_right_in, R.anim.fragment_left_out);
                        fragmentTransaction.add(R.id.fragment_container, ((ForgetPasswordActivity) mContext).mForgetPasswordStep2Fragment);
                        fragmentTransaction.remove(ForgetPasswordStep1Fragment.this).commit();
//                            ((ForgetPasswordActivity) mContext).mStep = ForgetPasswordActivity.FORGET_PASSWORD_STEP_2;
                    }
                });
            }else {
                mNextView.setClickable(true);
            }
        }
    }

    private boolean canNext() {
        return FormatCheck.checkPhoneNumber(mInputPhoneNumberView.getPhoneNumber(), mContext) == FormatCheck.CHECK_SUCCESS && FormatCheck.checkIdentifyingCode(mInputIdentifyingCodeView.getIdentifyingCode(), mContext) == FormatCheck.CHECK_SUCCESS;
    }

    @Override
    public String fragmentDescription() {
        if (mIsChangePasswordActivity) {
            return "修改密码第一步";
        } else {
            return "找回密码第一步";
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mInputIdentifyingCodeView.mIdentifyingCodeView.setText("");
    }
}
