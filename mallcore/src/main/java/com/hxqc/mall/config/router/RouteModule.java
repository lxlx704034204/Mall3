package com.hxqc.mall.config.router;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.db.CoreDatabase;
import com.hxqc.util.JSONUtils;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author:胡俊杰
 * Date: 2016/6/16
 * FIXME
 * Todo
 */

@Table(database = CoreDatabase.class)
public class RouteModule extends BaseModel {

	@Column
	@Unique
	public String module;
	@PrimaryKey()
	public String id;//ID
	@Column
	public String instruction;//备注信息
	@Column
	public String url;//示例
	Map<String, String> android_params;
	@Column
	private String dbAndroidParams;//保存数据库

	public String getClassName() {
		return getAndroid_params().get("className");
	}

	public Bundle getParamsBundle() {
		Bundle bundle = new Bundle();
		Set<String> keys = getAndroid_params().keySet();
		for (String k : keys) {
			bundle.putString(k, getAndroid_params().get(k));
		}
		return bundle;
	}

	public String getDbAndroidParams() {
		if (android_params != null)
			return JSONUtils.toJson(android_params);
		else return "";
	}

	public void setDbAndroidParams(String dbAndroidParams) {
		this.dbAndroidParams = dbAndroidParams;
	}

	public Map<String, String> getAndroid_params() {
		if (android_params != null) {
			return android_params;
		} else {
			if (!TextUtils.isEmpty(dbAndroidParams)) {
				return JSONUtils.fromJson(dbAndroidParams, new TypeToken<Map<String, String>>() {
				});
			}
			return new HashMap<>();
		}
	}

	@Override
	public String toString() {
		return "RouteModule{" +
				"module='" + module + '\'' +
				", android_params=" + android_params +
				", dbAndroidParams='" + dbAndroidParams + '\'' +
				", id='" + id + '\'' +
				", instruction='" + instruction + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}
