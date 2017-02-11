package com.hxqc.mall.launch.api;

import android.content.Context;
import android.os.Build;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.mall.core.controler.UserInfoSP;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


/**
 * Author:胡俊杰
 * Date: 2016/1/6
 * FIXME
 * Todo
 */
public class ApiClientAuthenticate extends BaseApiClient {
	public static final int CAPTCHA_PLATFORM = 10;//平台帮卖验证码
	public static final int CAPTCHA_RESERVE = 20;//预约看车验证码
	public ApiClientAuthenticate() {
		super();
	}

	/**
	 * 注册协议
	 */
	public static String getRegisterAgreement() {
		return ApiUtil.getAccountURL("/Html/register");
	}

	@Override
	protected String completeUrl(String control) {
//        return HOST + "/Account/" + API_VERSION + control;
		return ApiUtil.getAccountURL(control);
	}

	/**
	 * 登陆
	 *
	 * @param username
	 * @param password
	 */
	public void login(String username, String password, int loginType, AsyncHttpResponseHandler handler) {

		RequestParams requestParams = new RequestParams();
		switch (loginType) {
			case 1:
				requestParams.put("username", username);
				requestParams.put("password", password);
				break;
			case 2:
				requestParams.put("username", username);
				requestParams.put("captcha", password);
				break;
		}
		String url = completeUrl("/Authenticate");
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * 获取登录验证码
	 *
	 * @param username
	 * @param handler
	 */
	public void authenticate(String username, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Captcha/authenticate");
		RequestParams requestParams = new RequestParams();
		requestParams.put("username", username);
		gGetUrl(url, requestParams, handler);
	}

	/**
	 * 获取语音登录验证码
	 *
	 * @param username
	 * @param handler
	 */
	public void authenticate20(String username, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Captcha/authenticate");
		RequestParams requestParams = new RequestParams();
		requestParams.put("username", username);
		requestParams.put("sendType", 20);
		gGetUrl(url, requestParams, handler);
	}

	/**
	 * 检验号码是否已注册,并获取注册验证码
	 *
	 * @param username
	 */
	public void canRegister(String username, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Captcha/register");
		RequestParams requestParams = new RequestParams();
		requestParams.put("username", username);
		gGetUrl(url, requestParams, handler);
	}
	/**
	 * 二手车获取验证码
	 *
	 * @param username
	 * @param handler
	 */
	public void getUsedCarCaptcha(String username, int userType, AsyncHttpResponseHandler handler) {
		String url = ApiUtil.getUsedCarURL("/captcha");
		RequestParams requestParams = new RequestParams();
		requestParams.put("username", username);
		requestParams.put("useType", userType);
		requestParams.put("sendType", 10);
		gGetUrl(url, requestParams, handler);
	}
	/**
	 *  注册  语音验证码
	 *
	 * @param username
	 */
	public void canRegister2(String username, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Captcha/register");
		RequestParams requestParams = new RequestParams();
		requestParams.put("username", username);
		requestParams.put("sendType", 20);
		gGetUrl(url, requestParams, handler);
	}

	/**
	 * 检验号码是否未注册,并获取注册验证码
	 *
	 * @param username
	 */
	public void isRegistered(String username, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Captcha/passwordForgot");
		RequestParams requestParams = new RequestParams();
		requestParams.put("username", username);
		gGetUrl(url, requestParams, handler);
	}

	/**
	 * 忘记密码 语音验证码
	 *
	 * @param username
	 */
	public void isRegistered2(String username, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Captcha/passwordForgot");
		RequestParams requestParams = new RequestParams();
		requestParams.put("username", username);
		requestParams.put("sendType", 20);
		gGetUrl(url, requestParams, handler);
	}

	/**
	 * 注册
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @param captcha  验证码
	 */
	@Deprecated
	public void register(String username, String password, String captcha, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Users");
		RequestParams requestParams = new RequestParams();
		requestParams.put("username", username);
		requestParams.put("password", password);
		requestParams.put("captcha", captcha);
		gPostUrl(url, requestParams, handler);
	}

	/**
	 * * 注册
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @param captcha  验证码
	 * @param entrance 注册入口 10：新车 | 20：4s店，默认为新车
	 */
	public void register(String username, String password, String captcha, int entrance,
						 AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Users");
		RequestParams requestParams = new RequestParams();
		requestParams.put("username", username);
		requestParams.put("password", password);
		requestParams.put("captcha", captcha);
		requestParams.put("entrance", entrance);
		gPostUrl(url, requestParams, handler);
	}

	/**
	 * 退出登录
	 *
	 */
	public void exit(String deviceToken, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Authenticate/logout");
		RequestParams requestParams = new RequestParams();
		requestParams.put("deviceToken", deviceToken);
		gDeleteUrl(url, requestParams, handler);
	}

	/**
	 * 改密码
	 *
	 * @param username
	 * @param captcha
	 */
	public void sendCode(String username, String captcha, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Captcha/passwordForgot2");
		RequestParams requestParams = new RequestParams();
		requestParams.put("username", username);
		requestParams.put("captcha", captcha);
		gGetUrl(url, requestParams, handler);
	}

	/**
	 * 改密码2
	 *
	 * @param username
	 * @param captcha
	 * @param password
	 */
	public void setPassword(String username, String captcha, String password,
	                        String deviceToken, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Users/resetPassword");
		RequestParams requestParams = new RequestParams();
		requestParams.put("username", username);
		requestParams.put("captcha", captcha);
		requestParams.put("password", password);
		requestParams.put("deviceToken", deviceToken);
		gPutUrl(url, requestParams, handler);
	}

	/**
	 * 绑定设备
	 *
	 * @param token
	 */
	public void bindDevice(String token, String deviceToken, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Device");
		RequestParams requestParams = new RequestParams();
		requestParams.put("token", token);
		requestParams.put("deviceToken", deviceToken);
		requestParams.put("os", "Android");
		requestParams.put("osVersion", "Android" + Build.VERSION.RELEASE);
		requestParams.put("deviceName", Build.MODEL);
		gPostUrl(url, requestParams, handler);
	}

	/**
	 * 解绑设备
	 *
	 * @param context
	 */
	public void unbindDevice(Context context, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Device");
		RequestParams requestParams = new RequestParams();
		requestParams.put("deviceToken", new UserInfoSP(context).getUMengDeviceToken());
		gPutUrl(url, requestParams, handler);
	}

	/**
	 * 刷新token
	 *
	 * @param token
	 * @param handler
	 */
	public void refreshToken(String token, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Authenticate/refresh");
		RequestParams requestParams = new RequestParams();
		requestParams.put("token", token);
		gGetUrl(url, requestParams, handler);
	}

}
