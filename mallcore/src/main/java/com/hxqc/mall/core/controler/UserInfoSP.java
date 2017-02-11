package com.hxqc.mall.core.controler;

import android.content.Context;

import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

/**
 * Created 胡俊杰
 * 2016/11/2.
 * Todo:
 */

public class UserInfoSP extends BaseSharedPreferencesHelper {
	private static final String TOKEN_STRING = "token";
	private static final String PHONE_NUMBER = "PhoneNumber";
	private static final String USER = "User";
	private static final String TOKEN_TIME = "TokenTime";

	public UserInfoSP(Context context) {
		super(context);
	}


	void saveBindDeviceState(boolean bind) {
		shared.edit().putBoolean("bindDeviceToken", bind).apply();
	}


	boolean isBindDevice() {
		return shared.getBoolean("bindDeviceToken", false);
	}

	/**
	 * 保存友盟deviceToken
	 */
	public void saveUMengDeviceToken(String deviceToken) {
		shared.edit().putString("umengDeviceToken", deviceToken).apply();
	}


	/**
	 * 获取友盟DeviceTokend
	 */
	public String getUMengDeviceToken() {
		return shared.getString("umengDeviceToken", "");
	}

	/**
	 * 保存token时间
	 */
	private void saveTokenTime(Context context, long tokenTime) {
		shared.edit().putLong(TOKEN_TIME, tokenTime).apply();
	}

	long getTokenTime(Context context) {
		return shared.getLong(TOKEN_TIME, 0);
	}

	protected synchronized String getToken(Context context) {
		return shared.getString(TOKEN_STRING, "");
	}

	synchronized void saveToken(Context context, String token, long tokenTime) {
		shared.edit().putString(TOKEN_STRING, token).apply();
		saveTokenTime(context, tokenTime);
	}

	synchronized void removeToken(Context context) {
		shared.edit().remove(TOKEN_STRING).apply();
	}

	/**
	 * 获取个人信息
	 */
	User getUser(Context context) {
		DebugLog.i("Tag", "从sp取");
		return JSONUtils.fromJson(shared.getString(USER, null), User.class);
	}

	/**
	 * 保存个人信息
	 */
	void saveUser(Context context, User user) {
		shared.edit().putString(USER, JSONUtils.toJson(user)).apply();
	}

	/**
	 * 移除个人信息
	 */
	void removeUser(Context context) {
		shared.edit().remove(USER).apply();
	}


	/**
	 * 保存手机号
	 */
	void savePhoneNumber(Context context, String phoneNumber) {
		shared.edit().putString(PHONE_NUMBER, phoneNumber).apply();
	}

	/**
	 * 获取手机号
	 */
	public String getPhoneNumber(Context context) {
		return shared.getString(PHONE_NUMBER, "");
	}
}
