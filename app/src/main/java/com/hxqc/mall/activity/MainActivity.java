package com.hxqc.mall.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.hxqc.autonews.fragments.AutoInfoFragment_3;
import com.hxqc.mall.config.router.RouteOpenActivityUtil;
import com.hxqc.mall.config.update.VersionManager;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.SubjoinInfo;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.PgyerUpdate;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.FragmentTabHost;
import com.hxqc.mall.core.views.dialog.LoadingDialog;
import com.hxqc.mall.fragment.BackHandledFragment;
import com.hxqc.mall.fragment.BlankUnLoginOrderFragment;
import com.hxqc.mall.fragment.auto.ActiveEventFragment;
import com.hxqc.mall.fragment.me.MeFragment;
import com.hxqc.mall.reactnative.HomeFragment_RN;
import com.hxqc.mall.reactnative.manager.RNPatchUpdateExecuteManager;
import com.hxqc.mall.reactnative.nativemodule.FetchForToolData;
import com.hxqc.mall.thirdshop.utils.HomeSiteDataUtil;
import com.hxqc.mall.usedcar.utils.UsedCarSPHelper;
import com.hxqc.tinker.util.TinkerAutoFixStartUtil;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.hxqc.util.PermissionUtil;
import com.hxqc.xiaoneng.ChatManager;
import com.sdu.didi.openapi.DIOpenSDK;
import com.umeng.analytics.MobclickAgent;

import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import hxqc.mall.BuildConfig;
import hxqc.mall.R;

public class MainActivity extends AppNoBackActivity implements BackHandledFragment.BackHandledInterface, TabHost.OnTabChangeListener {

	public static final int MAIN_CHANGE_TAB = 0;
	public static final int AUTO_INFOMATION_CHANGE_TAB = 1;
	private static Boolean isExit = false;
	public FragmentTabHost mTabHost;
	public String autpInfoType = "";
	//是否是活动页webview
	boolean activeBackAction = false;
	HomeKeyListener homeKeyListener;
	//    LoadingDialog loadingDialog;
	GetHomeNameListener getHomeNameListener;
	private boolean isFromHome = false;
	private String HomeToTabID = "";
	private Class fragmentArray[] = {HomeFragment_RN.class, AutoInfoFragment_3.class, BlankUnLoginOrderFragment.class, MeFragment.class};
	private int iconArray[] = {R.drawable.ic_tabbar_home, R.drawable.ic_tabbar_promotion, R.drawable.ic_tabbar_order, R.drawable.ic_tabbar_me};
	private String[] titleArray;
	public Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == MAIN_CHANGE_TAB) {
				changeCurrentTab(Integer.parseInt((String) msg.obj));
			} else if (msg.what == AUTO_INFOMATION_CHANGE_TAB) {
				DebugLog.w("js_test", "autoInformationTabChangeTo  5555:");
				toAutoInfoList((String) msg.obj);
			}
			return false;
		}
	});
	private BackHandledFragment mBackHandedFragment;
//    private TextView unReadMark;//未读订单消息


	private void checkLocationPermission() {
		if (Build.VERSION.SDK_INT >= 23) {
			String per = Manifest.permission.ACCESS_COARSE_LOCATION;
			boolean b = PermissionUtil.checkPermission(this, per);
			if (!b) PermissionUtil.requestPermission(this, per, "位置信息");
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PgyerUpdate.update(this);
		//升级
		new VersionManager(this, BuildConfig.VERSION_NAME).checkVersion();
		showLoadingDialog();
		RNPatchUpdateExecuteManager.launchInitFile(this);
		setupView();
		//二手车清除数据
		new UsedCarSPHelper(this).removeUsedCarData();
		//刷新token
//        refreshToken();
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				//小能
				ChatManager.getInstance().setDebug(BuildConfig.DEBUG).initChatSDK(getApplicationContext());
				ChatManager.getInstance().userLogin(getApplicationContext());
			}
		});

		//didi代驾
		DIOpenSDK.registerApp(this, BuildConfig.DiDiAPPID, BuildConfig.DiDiSecret);
		DIOpenSDK.setMapSdkType(DIOpenSDK.MapLocationType.GAODE);
//        newEnergyArea();

		// 获取4S店分站数据
		HomeSiteDataUtil.getSiteData(this);
		//开始补丁打包请求
		new TinkerAutoFixStartUtil().requestForPatch(MainActivity.this);
		RouteOpenActivityUtil.linkToActivity(this, getIntent());
	}

	/**
	 * 显示加载框
	 */
	private void showLoadingDialog() {
		final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
		if (!(MainActivity.this).isFinishing()) {
			loadingDialog.show();
		}

		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (loadingDialog.isShowing() && !(MainActivity.this).isFinishing()) {
					loadingDialog.cancel();
				}
			}
		}, 3000);
	}


	private void refreshToken() {
		boolean isFromLoginPage = false;
		if (getIntent().getExtras() != null) {
			isFromLoginPage = getIntent().getExtras().getInt(ActivitySwitchBase.TAB, Integer.MIN_VALUE) == 2;
		}
		if ((isFromLoginPage && mSharedPreferencesHelper.getTabChange())) {
			return;
		}
		UserInfoHelper.getInstance().refreshToken(getApplicationContext());
	}


	@Override
	public void onResume() {
		super.onResume();
		checkLocationPermission();
		loadUnPayOrderCount();
		//完善车辆信息弹窗操作
//        CompleteAutoDialogUtils.completeAutoInfoDialog(this);
	}


	/**
	 * 获取未付款订单
	 */
	private void loadUnPayOrderCount() {
		if (!UserInfoHelper.getInstance().isLogin(this)) {
			refreshUnPayMark(0);
			return;
		}
		new UserApiClient().assistantInfo(new BaseMallJsonHttpResponseHandler(this) {
			@Override
			public void onSuccess(String response) {
				SubjoinInfo mSubjoinInfo = JSONUtils.fromJson(response, SubjoinInfo.class);
				if (mSubjoinInfo != null) {
					refreshUnPayMark(mSubjoinInfo.mallOrderCount + mSubjoinInfo.shopOrderCount);
				}
			}


			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                refreshUnPayMark(0);
			}
		});
	}


	/**
	 * 更新订单角标
	 *
	 * @param unPayCount 数量
	 */
	public void refreshUnPayMark(int unPayCount) {
		DebugLog.d("refreshUnPayMark", "unPayCount:" + unPayCount);
//        if (unReadMark != null) {
		TextView unReadMark = (TextView) mTabHost.getTabWidget().getChildAt(2).findViewById(R.id.unread_mark);
		if (0 < unPayCount && unPayCount < 99) {
			unReadMark.setText(MessageFormat.format("{0}", unPayCount));
			unReadMark.setVisibility(View.VISIBLE);
		} else if (unPayCount > 99) {
			unReadMark.setText("99+");
			unReadMark.setVisibility(View.VISIBLE);
		} else {
			unReadMark.setVisibility(View.GONE);
			unReadMark.setText("");
		}
//        } else DebugLog.d("refreshUnPayMark", "unReadMark is null" + unPayCount);
	}


	private void setupView() {
		titleArray = this.getResources().getStringArray(R.array.main_tabbar_array);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.getTabWidget().setDividerDrawable(null);

		int count = fragmentArray.length;
		for (int i = 0; i < count; i++) {
			TabHost.TabSpec tabSpec = mTabHost.newTabSpec(titleArray[i]).setIndicator(getTabItemView(i));
			mTabHost.addTab(tabSpec, fragmentArray[i], null);


			mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
		}
		mTabHost.setOnTabChangedListener(this);
	}


	private View getTabItemView(int index) {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View view = layoutInflater.inflate(R.layout.view_main_bottom_tabbar, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.main_view_tabbar_icon);
		imageView.setImageResource(iconArray[index]);

		TextView textView = (TextView) view.findViewById(R.id.main_view_tabbar_text);
		textView.setText(titleArray[index]);

		return view;
	}


	@Override
	public void setSelectedFragment(BackHandledFragment selectedFragment) {
		this.mBackHandedFragment = selectedFragment;
	}


	//控制返回键
	@Override
	public void onBackPressed() {
		DebugLog.i("fragment", " onBackPressed " + activeBackAction + " = " + getTabItemView(2).getTag() + " () ");
		if (mBackHandedFragment instanceof ActiveEventFragment) {
			ActiveEventFragment fragment = (ActiveEventFragment) mBackHandedFragment;
			if (activeBackAction && fragment.mActiveEvent.canGoBack()) {
				fragment.mActiveEvent.goBack();
			} else {
//                super.onBackPressed();
				exitBy2Click();
			}
		} else {
			exitBy2Click();
//            super.onBackPressed();
		}
	}


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		changeCurrentTab();
	}


	/**
	 * 切换tab页
	 */
	private void changeCurrentTab() {
		if (mSharedPreferencesHelper.getTabChange()) {
			int mTab = getIntent().getIntExtra(ActivitySwitcher.TAB, 0);
			mTabHost.setCurrentTab(mTab);
			mSharedPreferencesHelper.setTabChange(false);
		}
	}


	/**
	 * 切换tab
	 *
	 * @param p
	 */
	public void changeCurrentTab(int p) {
		HomeToTabID = titleArray[p];
		mTabHost.setCurrentTab(p);
	}


	/***
	 * 切换到当前分类的资讯列表下
	 *
	 * @param type
	 */
	public void toAutoInfoList(String type) {
		DebugLog.w("js_test", "toAutoInfoList  :" + type);
		AutoInfoFragment_3.mType = type;
		HomeToTabID = titleArray[1];
		mTabHost.setCurrentTab(1);
//        AutoInfoFragment_3 fragment = (AutoInfoFragment_3) getSupportFragmentManager().findFragmentByTag(titleArray[1]);
//        if (fragment != null) {
//            DebugLog.w("js_test", "toAutoInfoList fragment inner :" + type);
//            fragment.toIndexTab(type);
//        } else {
//            autpInfoType = type;
//        }
	}


	private void exitBy2Click() {

		if (isFromHome) {
			changeCurrentTab(0);
			isFromHome = false;
			return;
		}

		Timer tExit;
		if (!isExit) {
			isExit = true; // 准备退出
			Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            ToastHelper.showRedToast(getApplicationContext(), "再按一次退出程序");
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000);

		} else {
			//关闭补丁打包请求
//            TinkerAutoFixStartUtil.cancelTimerAlarm(MainActivity.this);

			finish();
			//通知umeng记录
			MobclickAgent.onKillProcess(this);
			System.exit(0);
		}
	}


	@Override
	public void onTabChanged(String tabId) {
		if (tabId.equals(HomeToTabID)) {
			isFromHome = true;
		} else {
			HomeToTabID = "";
			isFromHome = false;
		}

		//监听活动页面返回
		activeBackAction = tabId.equals(titleArray[1]);
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(tabId);
		if (fragment != null) fragment.onResume();
	}


	public void setHomeKeyListener(HomeKeyListener homeKeyListener) {
		this.homeKeyListener = homeKeyListener;
	}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU && homeKeyListener != null) {
//            mReactInstanceManager.showDevOptionsDialog();
			homeKeyListener.onKeyUp(keyCode);

			return true;
		}
		return super.onKeyUp(keyCode, event);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		DebugLog.i("home_data", "MainActivity onActivityResult");
		if (requestCode == FetchForToolData.request_code && resultCode == 1) {
			if (getHomeNameListener != null) {
				getHomeNameListener.getName(data.getStringExtra("position"));
			}
		}
	}


	public void setGetHomeName(GetHomeNameListener getHomeName) {
		this.getHomeNameListener = getHomeName;
	}

	public interface HomeKeyListener {
		void onKeyUp(int code);
	}

	public interface GetHomeNameListener {
		void getName(String name);
	}
}
