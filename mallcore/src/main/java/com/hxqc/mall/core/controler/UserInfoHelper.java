package com.hxqc.mall.core.controler;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.model.Token;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.launch.api.ApiClientAuthenticate;
import com.hxqc.mall.launch.util.ActivitySwitchAuthenticate;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import static com.hxqc.mall.config.application.SampleApplicationContext.application;

/**
 * Author:胡俊杰
 * Date: 2015/9/25
 * FIXME
 * Todo  获取个人信息
 */
public class UserInfoHelper {

	private static UserInfoHelper ourInstance;
	public OnLoginListener mOnLoginListener;
	User mMeData;
	UserInfoSP mSpHelp;
	private String UserPhotoNum;

	private UserInfoHelper() {
	}

	public static UserInfoHelper getInstance() {
		if (ourInstance == null) {
			synchronized (UserInfoHelper.class) {
				if (ourInstance == null) {
					ourInstance = new UserInfoHelper();
				}
			}
		}
		return ourInstance;
	}

	public String getUserPhotoNum() {
		return UserPhotoNum;
	}

	public void setUserPhotoNum(String userPhotoNum) {
		UserPhotoNum = userPhotoNum;
	}

	public UserInfoSP getSharedPreferences() {
		if (mSpHelp == null)
			mSpHelp = new UserInfoSP(SampleApplicationContext.context);
		return mSpHelp;
	}

	public void getUserInfo(@NonNull Context context, UserInfoAction userInfoAction, boolean showAnim) {
		if (isLogin(context)) {
			if (getUser(context) == null) {
				refreshUserInfo(context, userInfoAction, showAnim);
			} else {
				userInfoAction.showUserInfo(mMeData);
			}
		}
	}

	public boolean hasPayPassword(Context context) {
		User user = getUser(context);
		return user != null && user.isRealNameAuthentication;
	}


	/**
	 * 移除登陆状态
	 */
	public void removeLoginStatus(Context context) {
		getSharedPreferences().saveBindDeviceState(false);
		removeUser(context);
		getSharedPreferences().removeToken(context);
		new BaseSharedPreferencesHelper(application).setOrderChange(false); //不刷新列表
		new BaseSharedPreferencesHelper(application).setClearOrder(true); //清理订单列表
	}

	/**
	 * 获取个人信息
	 */
	private User getUser(Context context) {
		mMeData = mMeData != null ? mMeData : getSharedPreferences().getUser(context);
		return mMeData;
	}

	/**
	 * 保存个人信息
	 */
	public void saveUser(Context context, User user) {
		mMeData = user;
		getSharedPreferences().saveUser(context, user);
	}

	/**
	 * 移除个人信息
	 */
	public void removeUser(Context context) {
		mMeData = null;
		getSharedPreferences().removeUser(context);
	}

	/**
	 * 判断是否登陆
	 * true 已登录
	 */
	public boolean isLogin(Context context) {
		return !TextUtils.isEmpty(getSharedPreferences().getToken(context));
	}


	/**
	 * 强制刷新
	 */
	public void refreshUserInfo(final Context context, final UserInfoAction userInfoAction, boolean showAnim) {
		String token = getSharedPreferences().getToken(context);
		if (TextUtils.isEmpty(token)) {
			return;
		}
		new UserApiClient().getUserInfo(token, new LoadingAnimResponseHandler(context, showAnim) {
			@Override
			public void onFinish() {
				super.onFinish();
				if (userInfoAction != null) {
					userInfoAction.onFinish();
				}
			}

			@Override
			public void onSuccess(String response) {
				mMeData = JSONUtils.fromJson(response, User.class);
				DebugLog.i("Tag", "请求用户信息");
				if (mMeData != null) {
					saveUser(context, mMeData);
					if (userInfoAction != null) {
						userInfoAction.showUserInfo(mMeData);
					}
				}
			}
		});
	}

	/**
	 * 退出登陆
	 */
	public void exit(final Context tContext, OnLogoutListener onLogoutListener) {
		//移除登陆
		if (UserInfoHelper.getInstance().isLogin(tContext)) {
			new ApiClientAuthenticate().exit(getDeviceToken(), new BaseMallJsonHttpResponseHandler(tContext) {
				@Override
				public void onSuccess(String response) {


				}
			});
			if (onLogoutListener != null) {
				onLogoutListener.onLogoutSuccess(tContext);
			}
		}

	}

	/**
	 * 登陆取消
	 */
	public void loginCancel() {
		if (mOnLoginListener != null) {
			mOnLoginListener = null;
		}
	}

	/**
	 * 判断是否登陆过，没登陆先跳转登陆页面，登陆成功，执行后续动作
	 *
	 * @param entrance        注册来源
	 * @param onLoginListener 登陆成功回调
	 */
	public void loginAction(Context context, int entrance, OnLoginListener onLoginListener) {
		if (!isLogin(application)) {
			this.mOnLoginListener = onLoginListener;
			ActivitySwitchAuthenticate.toCodeLogin(context, entrance);
		} else {
			onLoginListener.onLoginSuccess();
		}
	}

	/**
	 * 登陆 默认来源
	 *
	 * @param onLoginListener 登陆成功回调
	 */
	public void loginAction(Context context, OnLoginListener onLoginListener) {
		loginAction(context, 50, onLoginListener);
	}

	public void loginAction(final Context context, final String userName, String password,
	                        int loginType) {
		final Application application = SampleApplicationContext.application;
		setUserPhotoNum(userName);
		new ApiClientAuthenticate().login(userName, password, loginType, new DialogResponseHandler(context, application.getResources().getString(R.string.me_logining)) {
			@Override
			public void onSuccess(String response) {
				DebugLog.d("loginAction", "response=" + response);
				Token mToken = JSONUtils.fromJson(response, Token.class);
				if (mToken == null)//服务器返回BadGateway
					return;
				getSharedPreferences().saveToken(application, mToken.token, System.currentTimeMillis());
				getSharedPreferences().savePhoneNumber(application, userName);

				new BaseSharedPreferencesHelper(application).setOrderChange(true);
				refreshUserInfo(context, null, false);
				bindDevice(context);

				if (mOnLoginListener != null) {
					mOnLoginListener.onLoginSuccess();
					mOnLoginListener = null;
				}
				((Activity) context).finish();

			}
		});
	}

	/**
	 * 绑定设备
	 */
	public void bindDevice(final Context context) {
		if (!isLogin(context)) return;
		if (getSharedPreferences().isBindDevice()) return;
		String token = getSharedPreferences().getToken(context);
		String deviceToken = getSharedPreferences().getUMengDeviceToken();
		new ApiClientAuthenticate().bindDevice(token, deviceToken, new
				BaseMallJsonHttpResponseHandler(context) {
					@Override
					public void onSuccess(String response) {
						getSharedPreferences().saveBindDeviceState(true);
					}
				});
	}


	public String getDeviceToken() {
		return getSharedPreferences().getUMengDeviceToken();
	}


	public void refreshToken(final Context context) {
		if (isLogin(context) && (getSharedPreferences().getTokenTime(context) + 7 * 1000 * 60 * 60 < System.currentTimeMillis())) {
			String token = getSharedPreferences().getToken(context);
			new ApiClientAuthenticate().refreshToken(token, new BaseMallJsonHttpResponseHandler(context) {
				@Override
				public void onSuccess(String response) {
					Token mToken = JSONUtils.fromJson(response, Token.class);
					if (mToken == null) return;
					getSharedPreferences().saveToken(context, mToken.token, System.currentTimeMillis());
				}

				@Override
				public void onOtherFailure(int statusCode, Header[] headers, String responseString, com.hxqc.mall.core.model.Error throwable) {
					new LogoutAction().onLogoutSuccess(context);
				}
			});
		}
	}


	public long getTokenTime(Context context) {
		return getSharedPreferences().getTokenTime(context);
	}

	public String getToken(Context context) {
		return getSharedPreferences().getToken(context);
	}

	public String getPhoneNumber(Context context) {
		return getSharedPreferences().getPhoneNumber(context);
	}

	public interface UserInfoAction {
		void showUserInfo(User meData);

		void onFinish();
	}

	public interface OnLoginListener {
		void onLoginSuccess();
	}

	public interface OnLogoutListener {
		void onLogoutSuccess(Context context);
	}
}
