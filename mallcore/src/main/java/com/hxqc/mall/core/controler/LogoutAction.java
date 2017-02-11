package com.hxqc.mall.core.controler;

import android.content.Context;

import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoSPControl;
import com.hxqc.mall.launch.util.ActivitySwitchAuthenticate;

/**
 * Created 胡俊杰
 * 2017/1/12.
 * Todo:  登出后状态修改
 */

public class LogoutAction implements UserInfoHelper.OnLogoutListener {
	@Override
	public void onLogoutSuccess(Context context) {

		//清除本地缓存车辆信息
		AutoHelper.getInstance().clearLocalDataAll(context);
		AutoSPControl.saveDialogCount(0);
		//删除本地用户信息
		UserInfoHelper.getInstance().removeLoginStatus(context);
		//跳转登陆页
		ActivitySwitchAuthenticate.toLogin(context);


	}
}
