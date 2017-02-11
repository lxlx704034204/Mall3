package com.hxqc.tinker.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.bsdiff.MD5Util;
import com.hxqc.mall.reactnative.util.RNFileUpdateUtil;
import com.hxqc.tinker.api.TinkerApiClient;
import com.hxqc.tinker.model.AppPatchItemModel;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Author:  wh
 * Date:  2016/12/29
 * FIXME
 * Todo   自动补丁开启工具
 */

public class TinkerAutoFixStartUtil {

    public static String TFS_Tag = "TFS_Tag";
    public String SavePatchFilePath = SampleApplicationContext.application.getFilesDir().getAbsolutePath() + "/";
    public String SavePatchFileName = "THXQCA_Patch";

    public static long intervalMillis = 3 * 3600 * 1000;//定时间隔3小时
    public static int afterDelay = 5;//五秒后开始请求app补丁

    public void requestForPatch(Context context) {
        DebugLog.w(TFS_Tag, "requestForPatch......................: " + System.currentTimeMillis());
//        Log.w(TFS_Tag, "requestForPatch......................: " + System.currentTimeMillis());
        fetchForPatch(context);
    }

    /**
     * 开启自动补丁更新
     */
    public static void startAutoFix(Context mC) {
        DebugLog.w(TFS_Tag, "startAutoFix......................: " + System.currentTimeMillis());
//        AlarmManager am = (AlarmManager) mC.getSystemService(ALARM_SERVICE);
//        Intent intent = new Intent(mC, TinkerFixReceiver.class);
//        PendingIntent sender = PendingIntent.getBroadcast(mC, 0, intent, 0);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.add(Calendar.SECOND, afterDelay);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, sender);
    }

    /**
     * 关闭定时器
     */
    public static void cancelTimerAlarm(Context mC) {
        DebugLog.w(TFS_Tag, "cancelTimerAlarm......................: " + System.currentTimeMillis());
//        Intent intent = new Intent(mC, TinkerFixReceiver.class);
//        PendingIntent sender = PendingIntent.getBroadcast(mC, 0, intent, 0);
//        AlarmManager am = (AlarmManager) mC.getSystemService(ALARM_SERVICE);
//        am.cancel(sender);
    }

    /**
     * 开始补丁更新
     */
    public void doFix(String path, Context mC) {
        DebugLog.w(TFS_Tag, "doFix......................: " + System.currentTimeMillis());
        TinkerInstaller.onReceiveUpgradePatch(mC, path);
    }

    private void fetchForPatch(final Context context) {
        TinkerApiClient apiClient = new TinkerApiClient();
        apiClient.fetchForAppPatch(new LoadingAnimResponseHandler(context, false) {
            @Override
            public void onSuccess(String response) {
                DebugLog.i(TinkerAutoFixStartUtil.TFS_Tag, "fetchForPatch......: " + response);
//                Log.i(TinkerAutoFixStartUtil.TFS_Tag, "fetchForPatch......: " + response);
                if (!TextUtils.isEmpty(response)) {
                    operateResponse(response, context);
                }
            }
        });
    }

    private void operateResponse(String json, Context context) {
//        Log.i(TinkerAutoFixStartUtil.TFS_Tag, "operateResponse......: " + json);
        DebugLog.i(TinkerAutoFixStartUtil.TFS_Tag, "operateResponse......: " + json);

        ArrayList<AppPatchItemModel> models = JSONUtils.fromJson(json, new TypeToken<ArrayList<AppPatchItemModel>>() {
        });
        if (models != null && models.size() > 0) {
            AppPatchItemModel model = models.get(models.size() - 1);

            if (model == null)
                return;

//            Log.i(TinkerAutoFixStartUtil.TFS_Tag, "AppPatchItemModel......: " + model.toString());
            DebugLog.i(TinkerAutoFixStartUtil.TFS_Tag, "AppPatchItemModel......: " + model.toString());
            downloadPatchFile(model.md5, model.url, context);
        }
    }

    /**
     * 下载app更新包
     * 并校验
     * 成功就开始 进行补丁安装
     * 失败 删除当前包  .....TODO
     */
    private void downloadPatchFile(final String fileMD5, String downloadURL, final Context context) {

        DebugLog.i(TFS_Tag, "开始删除原补丁包");
        RNFileUpdateUtil.deleteExistsFile(SavePatchFilePath + SavePatchFileName);

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("accept", "*/*");
        DebugLog.i(TFS_Tag, "开始下载补丁: " + downloadURL);
//        Log.i(TFS_Tag, "开始下载补丁: " + downloadURL);

        downloadURL = downloadURL.replaceAll("\\\\", "/");

        client.get(downloadURL, new FileAsyncHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                DebugLog.i(TFS_Tag, "下载补丁成功");
//                Log.i(TFS_Tag, "下载补丁成功");
                if (file != null && file.length() > 0) {
                    DebugLog.i(TFS_Tag, "保存文件大小：" + file.length() / 1024 + " kb");
//                    Log.i(TFS_Tag, "保存文件大小：" + file.length() / 1024 + " kb");
                    RNFileUpdateUtil.saveFile(SavePatchFilePath, SavePatchFileName, file);
                    DebugLog.i(TFS_Tag, "保存文件完成开始进行文件完整性校验");
//                    Log.i(TFS_Tag, "保存文件完成开始进行文件完整性校验: "+SavePatchFilePath+SavePatchFileName);
                    boolean b = MD5Util.verdictMD5isEqual(fileMD5, SavePatchFilePath  + SavePatchFileName);
                    if (b) {
                        DebugLog.i(TFS_Tag, "文件校验成功 ");
//                        Log.i(TFS_Tag, "文件校验成功 开始更新");
                        doFix(SavePatchFilePath  + SavePatchFileName,context);
                    } else {
                        DebugLog.i(TFS_Tag, "文件校验失败 删除文件 重新请求===");
//                        Log.i(TFS_Tag, "文件校验失败 删除文件 重新请求===");
                        RNFileUpdateUtil.deleteExistsFile(SavePatchFilePath + SavePatchFileName);
                    }
                }
            }
        });
    }
}
