package com.hxqc.mall.core.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.util.DebugLog;
import com.mcxiaoke.packer.helper.PackerNg;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import hxqc.mall.BuildConfig;

/**
 * Created 胡俊杰
 * 2016/10/28.
 * Todo:  蒲公英环境强制更新
 */

public class PgyerUpdate {
	public static void update(final Activity context) {
			String umeng = PackerNg.getMarket(SampleApplicationContext.context);
			if (!TextUtils.isEmpty(umeng) && umeng.equals("pgyer")) {
				DebugLog.setDebug(true);
				DebugLog.i("Tag", "" + BuildConfig.VERSION_CODE);
				PgyUpdateManager.register(context, new UpdateManagerListener() {
					@Override
					public void onUpdateAvailable(final String result) {
						// 将新版本信息封装到AppBean中
						final AppBean appBean = getAppBeanFromString(result);
						new AlertDialog.Builder(context).setCancelable(false)
								.setTitle("版本更新")
								.setMessage(appBean.getReleaseNote())
								.setNegativeButton("确定",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												startDownloadTask(context,
														appBean.getDownloadURL());
											}
										}).show();
					}

					@Override
					public void onNoUpdateAvailable() {

					}
				});
			}
	}
}
