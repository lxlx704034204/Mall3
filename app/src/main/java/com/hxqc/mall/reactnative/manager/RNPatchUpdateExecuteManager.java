package com.hxqc.mall.reactnative.manager;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.bsdiff.BSFileUtil;
import com.hxqc.mall.core.bsdiff.BsdiffUtilManager;
import com.hxqc.mall.core.bsdiff.MD5Util;
import com.hxqc.mall.reactnative.RNFileFullQuantityUpdateManager;
import com.hxqc.mall.reactnative.api.RNPatchApiClient;
import com.hxqc.mall.reactnative.model.BsdiffPatchResponseModel;
import com.hxqc.mall.reactnative.model.UnZipFileModel;
import com.hxqc.mall.reactnative.util.RNConfigUtil;
import com.hxqc.mall.reactnative.util.RNFileUpdateUtil;
import com.hxqc.mall.reactnative.util.RNVersionVerdifyUtil;
import com.hxqc.mall.reactnative.util.ZipExtractorAsyncTask;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

import java.io.File;

/**
 * Author:  wh
 * Date:  2016/8/18
 * FIXME
 * Todo  rn 文件增量更新管理
 */
public class RNPatchUpdateExecuteManager implements BsdiffUtilManager.OnBsdiffExecuteListener {
	public static String Tag = "bsdiff_rn";
	private Context ctx;
	private BsdiffPatchResponseModel bsdiffPatchResponseModel;//缓存请求获取的当前服务器更新信息
	private BsdiffUtilManager bsdiffUtilManager;
	private UnZipFileModel currentZipInnerFileModel;

	private String newBundelName;//新bundle文件名
	private String patch;//补丁文件路径
	private String oldP;//旧文件路径

	private static RNPatchUpdateExecuteManager mInstance;


	protected RNPatchUpdateExecuteManager(Context context) {
		this.ctx = context;
		bsdiffUtilManager = new BsdiffUtilManager();
		bsdiffUtilManager.setOnBsdiffExecuteListener(this);
	}

	public static RNPatchUpdateExecuteManager getInstance(Context context) {
		if (mInstance == null) {
			synchronized (RNPatchUpdateExecuteManager.class) {
				if (mInstance == null) {
					mInstance = new RNPatchUpdateExecuteManager(context);
				}
			}
		}
		return mInstance;
	}

	/**
	 * 启动程序初始化
	 */
	@hugo.weaving.DebugLog
	public static void launchInitFile(Context context) {

		DebugLog.i(Tag, "launchInitFile");

		//判断所需加载bundle文件是否存在
		if (RNFileUpdateUtil.isFileExist(RNConfigUtil.finalLoadBundleFilePath))
			return;

		RNVersionVerdifyUtil rnVersionVerdifyUtil = new RNVersionVerdifyUtil(context);

		//判断所存bundle名是否为空
		if (TextUtils.isEmpty(rnVersionVerdifyUtil.getBundleNameFromPrf())) {
			rnVersionVerdifyUtil.saveBundleNameToPrf(RNConfigUtil.localEncodeFileName);
		}

		//获取当前bundle名
		String localEncodeBunldeName = rnVersionVerdifyUtil.getBundleNameFromPrf();
		//判断最新bundle名文件是否存在
		boolean fileExist = RNFileUpdateUtil.isFileExist(RNConfigUtil.path + "/" + localEncodeBunldeName);
		//判断本地版本号
		if (rnVersionVerdifyUtil.verdifyLocalVersion()) {
			//如果版本号是最新  如果文件不存在从 asset中获取文件并包存在本地
			if (!fileExist)
				BSFileUtil.saveFileFAsset(context, RNConfigUtil.assetPathName, RNConfigUtil.path + "/" + localEncodeBunldeName);
		} else {
			//如果版本号不是最新  删除之前本地文件  从asset中获取文件并包存在本地
			RNFileUpdateUtil.deleteExistsFile(RNConfigUtil.path + "/" + localEncodeBunldeName);
			BSFileUtil.saveFileFAsset(context, RNConfigUtil.assetPathName, RNConfigUtil.path + "/" + localEncodeBunldeName);
		}

		//解密本地加密bundle文件
		RNFileUpdateUtil.localLoadDeCodeAndSave(context, RNConfigUtil.path + "/" + localEncodeBunldeName, RNConfigUtil.path + "/" + RNConfigUtil.saveName);
	}

	/**
	 * 执行更新操作
	 */
	public void executeRNFileUpdate() {
		DebugLog.i(Tag, "executeRNFileUpdate");
		verdifyMD5WithServer();
	}


	/**
	 * 1.读取本地 bundle   md5值
	 */
	private String getFileMD5() {
		RNVersionVerdifyUtil rnVersionVerdifyUtil = new RNVersionVerdifyUtil(ctx);
		return MD5Util.getFileMD5(RNConfigUtil.path + "/" + rnVersionVerdifyUtil.getBundleNameFromPrf());
	}

	/**
	 * 2.与网络上的md5作比较
	 */
	private void verdifyMD5WithServer() {
		/**
		 * 有更新 下载补丁包
		 *
		 * 无更新  不作为
		 */
		String md5Str = getFileMD5();

		BaseMallJsonHttpResponseHandler responseHandler = new BaseMallJsonHttpResponseHandler(ctx) {
			@Override
			public void onSuccess(String response) {
				DebugLog.i(Tag, response);
				bsdiffPatchResponseModel = JSONUtils.fromJson(response, new TypeToken<BsdiffPatchResponseModel>() {
				});

				if (bsdiffPatchResponseModel != null && !TextUtils.isEmpty(bsdiffPatchResponseModel.code)) {
					DebugLog.i(Tag, "生成更新信息");
					if (bsdiffPatchResponseModel.code.equals("2")) {
						DebugLog.i(Tag, "如果  返回码 是2 开始下载补丁包");
						//下载补丁文件
						downloadPatchZip(bsdiffPatchResponseModel.patchFileURL);
					} else if (bsdiffPatchResponseModel.code.equals("0")) {
						DebugLog.i(Tag, "code 为 0 开始全量更新");
						//    TODO        开始全量更新
						downloadWholeUpdateFile();
					} else {
						DebugLog.i(Tag, "已为最新版本 无需更新~~~");
					}
				}
			}

		};
		new RNPatchApiClient().verdifyMD5WithServer(md5Str, responseHandler);

	}


	private int tryDownloadPatchZipTimeNum = 0;//尝试重复数

	/**
	 * 下载增量更新补丁包
	 */
	private void downloadPatchZip(String downloadURL) {

		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("accept", "*/*");
		DebugLog.i(Tag, "开始下载补丁: " + downloadURL);

		downloadURL = downloadURL.replaceAll("\\\\", "/");

		client.get(downloadURL, new FileAsyncHttpResponseHandler(ctx) {
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, File file) {
				/**
				 * 下载补丁包成功   解压
				 */
				DebugLog.i(Tag, "下载补丁成功");
				RNFileUpdateUtil.deleteExistsFile(RNConfigUtil.finalLoadBundleFilePath);
				RNFileUpdateUtil.deleteExistsFile(RNConfigUtil.path + "/" + RNConfigUtil.saveNameZip);
				if (file != null && file.length() > 0) {
					DebugLog.i(Tag, "保存补丁");
					DebugLog.i(Tag, "保存补丁的文件大小：" + file.length() / 1024 + " kb");
					RNFileUpdateUtil.saveFileZip(file, RNConfigUtil.path + "/" + RNConfigUtil.saveNameZip);
					DebugLog.i(Tag, "保存补丁完成开始进行文件完整性校验");
					boolean b = MD5Util.verdictMD5isEqual(bsdiffPatchResponseModel.patchMD5, RNConfigUtil.path + "/" + RNConfigUtil.saveNameZip);
					if (b) {
						DebugLog.i(Tag, "文件校验成功 开始解压");
						unZipFile();
					} else {
						DebugLog.i(Tag, "文件校验失败 删除zip补丁包");
						RNFileUpdateUtil.deleteExistsFile(RNConfigUtil.path + "/" + RNConfigUtil.saveNameZip);
						if (tryDownloadPatchZipTimeNum < 1) {
							DebugLog.i(Tag, "文件校验失败 重新下载: tryDownloadPatchZipTimeNum: " + tryDownloadPatchZipTimeNum);
							tryDownloadPatchZipTimeNum++;
							downloadPatchZip(bsdiffPatchResponseModel.patchFileURL);
						} else {
							DebugLog.i(Tag, "文件校验失败 开始全量更新");
							//    TODO        开始全量更新
							downloadWholeUpdateFile();
						}
					}
				}
			}
		});
	}

	/**
	 * 文件解压
	 */
	private void unZipFile() {
		DebugLog.i(Tag, "unZipFile");
		RNFileUpdateUtil.deleteExistsFile(RNConfigUtil.path + "/" + RNConfigUtil.innerZipPatchFileName);
		ZipExtractorAsyncTask task = new ZipExtractorAsyncTask(
				false,
				RNConfigUtil.path + "/" + RNConfigUtil.saveNameZip,
				RNConfigUtil.path + "/" + RNConfigUtil.innerZipPatchFileName,
				ctx, true);
		task.setUnZipFinishListener(new ZipExtractorAsyncTask.OnUnZipFinishListener() {
			@Override
			public void unZipFinish(UnZipFileModel model) {
				DebugLog.i(Tag, "unZipFile 解压 unZipFinish");
				currentZipInnerFileModel = model;
				/**
				 * 解压完成   操作文件  合成补丁
				 */
				RNFileUpdateUtil.deleteExistsFile(RNConfigUtil.path + "/" + RNConfigUtil.saveNameZip);
				patchCombine();
			}
		});
		task.execute();
	}

	/**
	 * 补丁合成
	 * 合成成功 生成新bundle
	 * 合成失败 下载全量更新
	 */
	private void patchCombine() {

		if (currentZipInnerFileModel == null) {
			DebugLog.i(Tag, "patchCombine 解压 内部文件为空");
//    TODO        开始全量更新
			downloadWholeUpdateFile();
			return;
		}

		if (!currentZipInnerFileModel.isHasFile()) {
			DebugLog.i(Tag, "patchCombine 解压 内部文件不存在");
//    TODO        开始全量更新
			downloadWholeUpdateFile();
			return;
		}

		patch = currentZipInnerFileModel.getPatchFilePath();
		DebugLog.i(Tag, "patchCombine 合并 patch: " + patch);
		RNVersionVerdifyUtil rnVersionVerdifyUtil = new RNVersionVerdifyUtil(ctx);
		oldP = RNConfigUtil.path + "/" + rnVersionVerdifyUtil.getBundleNameFromPrf();
		DebugLog.i(Tag, "patchCombine 合并 oldP: " + oldP);
		newBundelName = "newHome" + System.currentTimeMillis() + ".js";
		String newP = RNConfigUtil.path + "/" + newBundelName;
		DebugLog.i(Tag, "patchCombine 合并 newP: " + newP);
		bsdiffUtilManager.startCombine(oldP, newP, patch);
	}

	/**
	 * 补丁合成成功
	 */
	@Override
	public void onBspatchSuccess() {
		DebugLog.i(Tag, "补丁合成成功");
		BSFileUtil.deleteExistsFile(patch);
		BSFileUtil.deleteExistsFile(oldP);
		updateVersionFile();
	}

	/**
	 * 补丁合成失败
	 */
	@Override
	public void onBspatchFail() {
		DebugLog.i(Tag, "补丁合成失败");
		BSFileUtil.deleteExistsFile(patch);
		downloadWholeUpdateFile();
	}

	/**
	 * 合成失败 下载全量更新
	 */
	private void downloadWholeUpdateFile() {
		RNFileFullQuantityUpdateManager m = new RNFileFullQuantityUpdateManager();
		if (bsdiffPatchResponseModel != null)
			m.doStartFullQUpdate(bsdiffPatchResponseModel.wholeFileURL, bsdiffPatchResponseModel.bundleMD5,ctx);
	}


	/**
	 * 最后 文件生成成功
	 * 改变 bundle 本地存储信息
	 */
	private void updateVersionFile() {
		RNVersionVerdifyUtil rnVersionVerdifyUtil = new RNVersionVerdifyUtil(ctx);
		rnVersionVerdifyUtil.saveBundleNameToPrf(newBundelName);
	}


}
