package com.hxqc.mall.config.update;

import android.content.Context;

import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.util.JSONUtils;

/**
 * Created 胡俊杰
 * 2016/9/23.
 * Todo:
 */

public class UpdateSP extends BaseSharedPreferencesHelper {
	public UpdateSP(Context context) {
		super(context);
	}

	/**
	 * 获取忽略的版本号
	 */
	public String getIgnoreVersion() {
		return shared.getString("IgnoreVersionName", "");
	}

	/**
	 * 设置忽略的版本号
	 */
	public void saveIgnoreVersionName(String versionName) {
		shared.edit().putString("IgnoreVersionName", versionName).apply();
	}

	/**
	 * 保存是否有新版本
	 * @param newVersion
	 */
	public void setHasNewVersion(boolean newVersion) {
		shared.edit().putBoolean("hasNewVersion", newVersion).apply();
	}

	/**
	 * 是否有新版本
	 */
	public boolean isHasNewVersion() {
		return shared.getBoolean("hasNewVersion", false);
	}

	public void saveVersion(VersionPo version){
		shared.edit().putString("version", JSONUtils.toJson(version)).apply();
	}
	public VersionPo getVersion(){
		return JSONUtils.fromJson(shared.getString("version",""),VersionPo.class);
	}
}
