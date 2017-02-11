/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hxqc.mall.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.config.push.PushMessageUtil;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.db.CoreDatabase;
import com.hxqc.mall.core.db.SQLCipherHelperImpl;
import com.hxqc.mall.photolibrary.util.CustomConstants;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;
import com.hxqc.tinker.Log.MyLogImp;
import com.hxqc.tinker.util.TinkerManager;
import com.hxqc.util.AssetsDatabaseManager;
import com.hxqc.util.DebugLog;
import com.mcxiaoke.packer.helper.PackerNg;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;
import com.raizlabs.android.dbflow.structure.database.OpenHelper;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.ApplicationLifeCycle;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.analytics.MobclickAgent;

import hxqc.mall.BuildConfig;

import static android.content.Context.MODE_PRIVATE;


/**
 * because you can not use any other class in your application, we need to
 * move your implement of Application to {@link ApplicationLifeCycle}
 * As Application, all its direct reference class should be in the main dex.
 * <p>
 * We use tinker-android-anno to make sure all your classes can be patched.
 * <p>
 * application: if it is start with '.', we will add SampleApplicationLifeCycle's package name
 * <p>
 * flags:
 * TINKER_ENABLE_ALL: support dex, lib and resource
 * TINKER_DEX_MASK: just support dex
 * TINKER_NATIVE_LIBRARY_MASK: just support lib
 * TINKER_RESOURCE_MASK: just support resource
 * <p>
 * loaderClass: define the tinker loader class, we can just use the default TinkerLoader
 * <p>
 * loadVerifyFlag: whether check files' md5 on the load time, defualt it is false.
 * <p>
 * Created by zhangshaowen on 16/3/17.
 */
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.hxqc.mall.application.Application",
		flags = ShareConstants.TINKER_ENABLE_ALL,
		loadVerifyFlag = false)
public class SampleApplicationLike extends DefaultApplicationLike {
	private static final String TAG = "Tinker.SampleApplicationLike";


	public SampleApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent, Resources[] resources, ClassLoader[] classLoader, AssetManager[] assetManager) {
		super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent, resources, classLoader, assetManager);
	}


	/**
	 * install multiDex before install tinker
	 * so we don't need to put the tinker lib classes in the main dex
	 *
	 * @param base
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onBaseContextAttached(Context base) {
		super.onBaseContextAttached(base);
		//you must install multiDex whatever tinker is installed!
		MultiDex.install(base);
		SampleApplicationContext.application = getApplication();
		SampleApplicationContext.context = getApplication();
		TinkerManager.setTinkerApplicationLike(this);

		TinkerManager.initFastCrashProtect();
		//should set before tinker is installed
		TinkerManager.setUpgradeRetryEnable(true);
		//optional set logIml, or you can use default debug log
		TinkerInstaller.setLogIml(new MyLogImp());
		//installTinker after load multiDex
		//or you can put com.tencent.tinker.** to main dex
		TinkerManager.installTinker(this);
		Tinker tinker = Tinker.with(getApplication());
	}


	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
		getApplication().registerActivityLifecycleCallbacks(callback);
	}


	@Override
	public void onCreate() {
		super.onCreate();
//		FreelineCore.init(SampleApplicationContext.application);

		init();
	}


	public void init() {
		DebugLog.setDebug(BuildConfig.showLog);
		ApiUtil.AccountHostURL = BuildConfig.AccountURL;
		ApiUtil.APPConfigHostURL = BuildConfig.AppConfigURL;
		ApiUtil.AccessoryHostURL = BuildConfig.AccessoryGoodsURL;
		ApiUtil.MaintainHostURL = BuildConfig.MaintainURL;
		ApiUtil.UsedCarHostURL = BuildConfig.UsedCarURL;
		ApiUtil.isDebug = BuildConfig.DEBUG;
		ApiUtil.ConfigAppKey = BuildConfig.ConfigAppKey;
		umengConfig();
		new SharedPreferencesHelper(SampleApplicationContext.context).prepareSite(BuildConfig.ShopSiteID);
		removeTempFromPref();
		//复制省市区 进入dbAdd
		AssetsDatabaseManager.initManager(SampleApplicationContext.context).copyDatabase("Core.db");
		initDbFlow();
//        LeakCanary.install(this);
		//Stetho工具初始化
		Stetho.initialize(Stetho.newInitializerBuilder(SampleApplicationContext.context).
				enableDumpapp(Stetho.defaultDumperPluginsProvider(SampleApplicationContext.context)).
				enableWebKitInspector(Stetho.defaultInspectorModulesProvider(SampleApplicationContext.context)).build());

	}

	//友盟配置
	private void umengConfig() {
		String appID = BuildConfig.UMENGAPPID;
		String channel = PackerNg.getMarket(SampleApplicationContext.context);
		MobclickAgent.startWithConfigure(
				new MobclickAgent.UMAnalyticsConfig(SampleApplicationContext.context, appID, channel));
		PushMessageUtil.getInstance().preparePushMessage(SampleApplicationContext.context, BuildConfig.DEBUG, channel);
	}

	//清除上传图片缓存
	private void removeTempFromPref() {
		SharedPreferences sp = getApplication().getSharedPreferences(CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
		sp.edit().remove(CustomConstants.PREF_TEMP_IMAGES).remove(CustomConstants.AVAILABLE_IMAGE_SIZE).apply();
	}


	private void initDbFlow() {
		//初始化数据库
		FlowManager.init(new FlowConfig.Builder(SampleApplicationContext.application).
				addDatabaseHolder(com.raizlabs.android.dbflow.config.DrivingexamGeneratedDatabaseHolder.class).
				addDatabaseHolder(com.raizlabs.android.dbflow.config.MallCoreGeneratedDatabaseHolder.class)
				.addDatabaseConfig(
						new DatabaseConfig.Builder(CoreDatabase.class)
								.openHelper(new DatabaseConfig.OpenHelperCreator() {
									@Override
									public OpenHelper createHelper(DatabaseDefinition databaseDefinition, DatabaseHelperListener helperListener) {
										return new SQLCipherHelperImpl(databaseDefinition, helperListener);
									}
								})
								.build()).build());
	}
}
