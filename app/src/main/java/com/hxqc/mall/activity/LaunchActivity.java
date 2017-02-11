package com.hxqc.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hxqc.mall.config.router.RouteManager;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.mall.reactnative.manager.RNPatchUpdateExecuteManager;
import com.umeng.analytics.MobclickAgent;

import hxqc.mall.BuildConfig;
import hxqc.mall.R;


public class LaunchActivity extends NoBackActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boot_anim);
		new RouteManager().checkRouter(this);
		MobclickAgent.setCatchUncaughtExceptions(!BuildConfig.DEBUG);
		setAppDefaultValue();
		rnOperate();
	}

	/**
	 * rn的 文件加载更新操作
	 */
	private void rnOperate() {
		RNPatchUpdateExecuteManager.launchInitFile(this);
		turnToMain();
	}

	/**
	 * 跳转到 主页
	 */
	private void turnToMain() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				finish();
				Intent intent = new Intent();
				String actionString = getIntent().getAction();
				if (Intent.ACTION_VIEW.equals(actionString)) {
					intent.setAction(actionString);
					intent.setData(getIntent().getData());
				}
				intent.setClass(LaunchActivity.this, MainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
			}
		}, 500);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void setAppDefaultValue() {
		new com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper(this).setPositionTranslateForSiteGroup(false);
		SharedPreferencesHelper helper = new SharedPreferencesHelper(this);
		helper.setPositionTranslate(false);
		helper.setLoadPosition(false);
	}
}
