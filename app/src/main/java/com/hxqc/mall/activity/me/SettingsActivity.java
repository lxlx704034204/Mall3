package com.hxqc.mall.activity.me;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.hxqc.mall.activity.AppBackActivity;
import com.hxqc.mall.auto.controler.AutoSPControl;
import com.hxqc.mall.config.update.VersionManager;
import com.hxqc.mall.core.controler.LogoutAction;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.DataCleanManager;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.launch.util.ActivitySwitchAuthenticate;
import com.hxqc.mall.views.MeItem;
import com.hxqc.mall.views.SettingsItem;

import hxqc.mall.R;


/**
 * 说明:设置界面
 * <p/>
 * author: 吕飞
 * since: 2015-03-10
 * Copyright:恒信汽车电子商务有限公司
 */
public class SettingsActivity extends AppBackActivity implements View.OnClickListener {
	MeItem mChangePasswordView;//修改密码
	MeItem mChangePayPasswordView;//修改密码
	MeItem mForgotPayPasswordView;//修改密码
	MeItem mDeliveryAddressView;//收货地址
	MeItem mAdviceView;//意见反馈
	SettingsItem mVersionCheckView;//版本更新
	SettingsItem mCacheClearView;//清除缓存
	MeItem mHelpView;// 帮助中心
	MeItem mAboutUsView;//关于我们
	Button mExitView;//退出登录
	Handler mHandler = new Handler();
	Runnable mShowCache = new Runnable() {
		@Override
		public void run() {
			try {
				showCache();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	Runnable mClearCache = new Runnable() {
		@Override
		public void run() {
			clearCache();

		}
	};
//    private UmengUpdateListener umengUpdateListener;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		//手势关闭
//        SlidrConfig config = new SlidrConfig.Builder()
//                .primaryColor(getResources().getColor(R.color.primary))
//                .sensitivity(1f).build();
//        Slidr.attach(this, config);
		initView();
		initItems();
		initEvent();
		update();
	}

	private void initEvent() {
		mChangePasswordView.setOnClickListener(this);
		mChangePayPasswordView.setOnClickListener(this);
		mForgotPayPasswordView.setOnClickListener(this);
		mDeliveryAddressView.setOnClickListener(this);
		mAdviceView.setOnClickListener(this);
		mVersionCheckView.setOnClickListener(this);
		mCacheClearView.setOnClickListener(this);
		mHelpView.setOnClickListener(this);
		mAboutUsView.setOnClickListener(this);
		mExitView.setOnClickListener(this);
	}

	private void initView() {
		mChangePasswordView = (MeItem) findViewById(R.id.change_password);
		mChangePayPasswordView = (MeItem) findViewById(R.id.change_pay_password);
		mForgotPayPasswordView = (MeItem) findViewById(R.id.forget_pay_password);

		mDeliveryAddressView = (MeItem) findViewById(R.id.delivery_address);
		mAdviceView = (MeItem) findViewById(R.id.advice);
		mVersionCheckView = (SettingsItem) findViewById(R.id.version_check);
		mCacheClearView = (SettingsItem) findViewById(R.id.cache_clear);
		mHelpView = (MeItem) findViewById(R.id.help);
		mAboutUsView = (MeItem) findViewById(R.id.about_us);
		mExitView = (Button) findViewById(R.id.exit);
	}

	private void initItems() {
		mChangePasswordView.setLeftIcon(R.drawable.ic_settings_password);
		mChangePasswordView.setLeftText(R.string.title_activity_me_change_password);
		mChangePayPasswordView.setLeftIcon(R.drawable.ic_password);
		mChangePayPasswordView.setLeftText(R.string.title_activity_me_change_pay_password);
		mForgotPayPasswordView.setLeftIcon(R.drawable.ic_forget_password);
		mForgotPayPasswordView.setLeftText(R.string.title_activity_me_forget_pay_password);

		mDeliveryAddressView.setLeftIcon(R.drawable.ic_setting_deliveryaddress);
		mDeliveryAddressView.setLeftText(R.string.title_activity_me_delivery_address);
		mAdviceView.setLeftIcon(R.drawable.ic_setting_comment);
		mAdviceView.setLeftText(R.string.title_activity_me_advice);
		mVersionCheckView.setLeftIcon(R.drawable.ic_settings_versioncheck);
		mVersionCheckView.setLeftText(R.string.me_version_check);
		mCacheClearView.setLeftIcon(R.drawable.ic_setting_clear);
		mCacheClearView.setLeftText(R.string.me_cache_clear);
		mCacheClearView.setRightText("缓存计算中...", R.color.straight_matter_and_secondary_text);
		mHandler.post(mShowCache);
		mHelpView.setLeftIcon(R.drawable.ic_setting_productdetail_help);
		mHelpView.setLeftText(R.string.title_activity_me_helpCenter);

		mHelpView.setVisibility(View.GONE);//暂时隐藏

		mAboutUsView.setLeftIcon(R.drawable.ic_settings_about_us);
		mAboutUsView.setLeftText(R.string.title_activity_me_about_us);

	}


	@Override
	protected void onResume() {
		super.onResume();
		if (UserInfoHelper.getInstance().isLogin(this)) {
			mChangePasswordView.setVisibility(View.VISIBLE);
			mChangePayPasswordView.setVisibility(View.VISIBLE);
			mForgotPayPasswordView.setVisibility(View.VISIBLE);
			mExitView.setVisibility(View.VISIBLE);
		} else {
			mChangePasswordView.setVisibility(View.GONE);
			mChangePayPasswordView.setVisibility(View.GONE);
			mForgotPayPasswordView.setVisibility(View.GONE);
			mExitView.setVisibility(View.GONE);

		}
	}

	private void showCache() throws Exception {
		mCacheClearView.setRightText(DataCleanManager.getTotalCacheSize(this),
				R.color.straight_matter_and_secondary_text);
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		switch (i) {
			case R.id.change_password:
//                if (UserInfoHelper.getInstance().isLogin(this)) {
				ActivitySwitchAuthenticate.toChangePassword(this);
//                } else {
//                    ActivitySwitchAuthenticate.toLogin(this, SettingsActivity.class.getName());
//                }
				break;
			case R.id.change_pay_password:
//                if (UserInfoHelper.getInstance().isLogin(this)) {
				if (!UserInfoHelper.getInstance().hasPayPassword(SettingsActivity.this)) {
					new NormalDialog(this, "提醒：", "您尚未设置支付密码", "去设置") {
						@Override
						protected void doNext() {
							ActivitySwitchBase.toRealNameAuthentication(getContext());
						}
					}.show();
				} else
					ActivitySwitcher.modifierPaidPWD(this);
//                } else {
//                    ActivitySwitchAuthenticate.toLogin(this, SettingsActivity.class.getName());
//                }
				break;
			case R.id.forget_pay_password:
//                if (UserInfoHelper.getInstance().isLogin(this)) {
				if (!UserInfoHelper.getInstance().hasPayPassword(SettingsActivity.this)) {
					new NormalDialog(this, "提醒：", "您尚未设置支付密码", "去设置") {
						@Override
						protected void doNext() {
							ActivitySwitchBase.toRealNameAuthentication(getContext());
						}
					}.show();
				} else
					ActivitySwitcher.forgetPaidPWD(this);
//                } else {
//                    ActivitySwitchAuthenticate.toLogin(this, SettingsActivity.class.getName());
//                }
				break;

			case R.id.delivery_address:

				UserInfoHelper.getInstance().loginAction(this, new UserInfoHelper.OnLoginListener() {
					@Override
					public void onLoginSuccess() {
						ActivitySwitcher.toDeliveryAddress(SettingsActivity.this);
					}
				});

				break;
			case R.id.advice:
				ActivitySwitcher.toAdvice(this);
				break;
			case R.id.version_check:
				new VersionManager(this).showUpdateDialog(this);
				break;
			case R.id.cache_clear:
				mCacheClearView.setClickable(false);
				mHandler.post(mClearCache);
				break;
			case R.id.help:
				ActivitySwitcher.toHelpCenter(this);
				break;
			case R.id.about_us:
				ActivitySwitcher.toAboutUs(this);
				break;
			case R.id.exit:
				AutoSPControl.saveDialogCount(0);
				UserInfoHelper.getInstance().exit(this, new LogoutAction());
				finish();
				break;
		}
	}

	private void update() {
		boolean hasNewVersion = new VersionManager(this).hasNewVersion(this);
		if (hasNewVersion) {
			mVersionCheckView.setRightText(getResources().getString(R.string.me_has_new_version), R.color.text_blue);
		} else {
			mVersionCheckView.setRightText("当前为最新版本", R.color.text_blue);
		}
	}


	private void clearCache() {
		if (!TextUtils.isEmpty(mCacheClearView.mRightTextView.getText())) {
			mCacheClearView.mRightTextView.setVisibility(View.GONE);
			mCacheClearView.mProgressBarView.setVisibility(View.VISIBLE);
			DataCleanManager.clearAllCache(this);
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mCacheClearView.mRightTextView.setVisibility(View.VISIBLE);
					mCacheClearView.setRightText("清理完毕", R.color.straight_matter_and_secondary_text);
					mCacheClearView.mProgressBarView.setVisibility(View.GONE);
				}
			}, 3000);
		}
	}


}
