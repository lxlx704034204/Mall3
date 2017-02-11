package com.hxqc.mall.launch.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.LogoutAction;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.launch.activity.ForgetPasswordActivity;
import com.hxqc.mall.launch.api.ApiClientAuthenticate;
import com.hxqc.mall.launch.view.InputPassword;
import com.hxqc.util.DebugLog;

/**
 * 说明:找密码二步
 * <p/>
 * author: 吕飞
 * since: 2015-03-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class ForgetPasswordStep2Fragment extends SetPasswordFragment {
	Button mNextView;//下一步
	InputPassword mInputPasswordView;//密码栏
	ImageView mStrongView;//密码强
	ImageView mMediumView;//密码中
	ImageView mWeakView;//密码弱

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_forget_password_step2, container, false);
		mNextView = (Button) rootView.findViewById(R.id.next);
		mStrongView = (ImageView) rootView.findViewById(R.id.strong);
		mMediumView = (ImageView) rootView.findViewById(R.id.medium);
		mWeakView = (ImageView) rootView.findViewById(R.id.weak);
		mInputPasswordView = (InputPassword) rootView.findViewById(R.id.input_password);
		mInputPasswordView.setHint(R.string.me_new_password_hint);
		mInputPasswordView.getEditText().setFloatingLabelText(mContext.getResources().getString(R.string.me_new_password_hint));
		mNextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mNextView.setClickable(false);
				if (canNext()) {
					new ApiClientAuthenticate().setPassword(((ForgetPasswordActivity) mContext).mPhoneNumber,
							((ForgetPasswordActivity) mContext).mIdentifyingCode, mInputPasswordView.getPassword(),
							UserInfoHelper.getInstance().getDeviceToken(),
							new LoadingAnimResponseHandler(mContext) {
								@Override
								public void onFinish() {
									mNextView.setClickable(true);
									super.onFinish();
								}

								@Override
								public void onSuccess(String response) {
									DebugLog.i("ApiClient", "修改密码 ----" + response);
									((ForgetPasswordActivity) mContext).mStep = ForgetPasswordActivity.FORGET_PASSWORD_STEP_3;
									if (mIsChangePasswordActivity) {
										new LogoutAction().onLogoutSuccess(getActivity());
									}
									FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
									fragmentTransaction.setCustomAnimations(R.anim.fragment_right_in, R.anim.fragment_left_out);
									fragmentTransaction.add(R.id.fragment_container, ((ForgetPasswordActivity) mContext).mForgetPasswordStep3Fragment);
									fragmentTransaction.remove(ForgetPasswordStep2Fragment.this).commit();
								}
							});

				} else {
					mNextView.setClickable(true);
				}
			}
		});
		mInputPasswordView.getEditText().addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				showStrength();
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		return rootView;
	}

	private boolean canNext() {
		return FormatCheck.checkPassword(mInputPasswordView.getPassword(), mContext, true) < FormatCheck.NO_PASSWORD;
	}

	private void showStrength() {
		int i = FormatCheck.checkPassword(mInputPasswordView.getPassword(), mContext, false);
		switch (i) {
			case FormatCheck.PASSWORD_WEAK:
				weak();
				break;
			case FormatCheck.PASSWORD_MEDIUM:
				medium();
				break;
			case FormatCheck.PASSWORD_STRONG:
				strong();
				break;
			default:
				disable();
				break;
		}
	}

	private void disable() {
		mWeakView.setImageResource(R.drawable.ic_password_strength_gray);
		mMediumView.setImageResource(R.drawable.ic_password_strength_gray);
		mStrongView.setImageResource(R.drawable.ic_password_strength_gray);
	}

	private void strong() {
		mWeakView.setImageResource(R.drawable.ic_password_strength_orange);
		mMediumView.setImageResource(R.drawable.ic_password_strength_orange);
		mStrongView.setImageResource(R.drawable.ic_password_strength_orange);
	}

	private void medium() {
		mWeakView.setImageResource(R.drawable.ic_password_strength_orange);
		mMediumView.setImageResource(R.drawable.ic_password_strength_orange);
		mStrongView.setImageResource(R.drawable.ic_password_strength_gray);
	}

	private void weak() {
		mWeakView.setImageResource(R.drawable.ic_password_strength_orange);
		mMediumView.setImageResource(R.drawable.ic_password_strength_gray);
		mStrongView.setImageResource(R.drawable.ic_password_strength_gray);
	}

	@Override
	public String fragmentDescription() {
		if (mIsChangePasswordActivity) {
			return "修改密码第二步";
		} else {
			return "找回密码第二步";
		}
	}
}









