package com.hxqc.mall.config.update;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


/**
 * Author:胡俊杰
 * Date:2015-11-09
 * FIXME
 * Todo 版本管理
 */
public class VersionManager {

	String versionNum;
	private Context mContext;
	private UpdateApiClient apiClient;

	public VersionManager(Context context, String versionNum) {
		this.mContext = context;
		apiClient = new UpdateApiClient();
		this.versionNum = versionNum;
	}

	public VersionManager(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * 检查版本更新
	 */
	public void checkVersion() {
		apiClient.checkUpdate(new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				VersionPo version = JSONUtils.fromJson(responseString, new TypeToken<VersionPo>() {
				});
				if (null != version) {
					DebugLog.i("Tag", "ss  " + version.toString());
					if (TextUtils.isEmpty(version.versionNum))
						return;
					checkUpdate(version);
				}

			}
		});

	}

	/**
	 * 检查版本是否更新
	 */
	private void checkUpdate(final VersionPo version) {
		DebugLog.i("Tag", "更新  " + new UpdateSP(mContext).getIgnoreVersion() + "  version.versionNu " + version.versionNum);
		if (new UpdateSP(mContext).getIgnoreVersion().equals(version.versionNum)) {
			//忽略版本
			return;
		}
		if (!((Activity) mContext).isFinishing()) {
			boolean hasNewVersion = version.versionNum.compareTo(versionNum) > 0;//是否更新
			DebugLog.i("Tag", "更新  " + hasNewVersion + "  version.versionNum" + version.versionNum + "  versionNum " + versionNum);
			new UpdateSP(mContext.getApplicationContext()).setHasNewVersion(hasNewVersion);//保存是否有更新
			boolean forceUpdate = version.minVersion.compareTo(versionNum) > 0;//强制更新
			DebugLog.i("Tag", "强制  " + hasNewVersion + "  version.minVersion" + version.minVersion + "  versionNum " + versionNum);
			if (hasNewVersion) {
				new UpdateDialog(mContext, version, forceUpdate).show();
			}

		}
	}

	/**
	 * 是否有新版本
	 * @param context
	 */
	public boolean hasNewVersion(Context context) {
		return new UpdateSP(context.getApplicationContext()).isHasNewVersion();
	}
	/**
	 * 显示新版本对话框
	 */
	public void showUpdateDialog(Context ctx){
		UpdateSP updateSP=new UpdateSP(ctx.getApplicationContext());
		boolean hasNewVersion=updateSP.isHasNewVersion();
		if (hasNewVersion) {
			VersionPo version=updateSP.getVersion();
			new UpdateDialog(mContext, version).show();
		}
	}

}


