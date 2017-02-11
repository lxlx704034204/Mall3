package com.hxqc.mall.reactnative;

import android.content.Context;
import android.text.TextUtils;

import com.hxqc.mall.core.bsdiff.MD5Util;
import com.hxqc.mall.reactnative.model.UnZipFileModel;
import com.hxqc.mall.reactnative.util.RNConfigUtil;
import com.hxqc.mall.reactnative.util.RNFileUpdateUtil;
import com.hxqc.mall.reactnative.util.ZipExtractorAsyncTask;
import com.hxqc.mall.reactnative.util.RNVersionVerdifyUtil;
import com.hxqc.util.DebugLog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

import java.io.File;

/**
 * Author: wanghao
 * Date: 2016-03-17
 * FIXME
 * Todo  react-native    bundle 全量更新工具
 */
public class RNFileFullQuantityUpdateManager {
    public static String Tag = "bsdiff_rn";
    /**
     * 开始更新
     */
    public void doStartFullQUpdate(String url,String bundleMD5,Context context) {
        if (!TextUtils.isEmpty(url))
            startUpdateRNFile( url, bundleMD5, context);
    }

    private void startUpdateRNFile(String fullUpdateURL, final String originalFileMD5, final Context ctx) {
        DebugLog.i(Tag, " startUpdateRNFile : ");
        requestForJSBundle(fullUpdateURL,new FileAsyncHttpResponseHandler(ctx) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                DebugLog.i(Tag, " startUpdateRNFile : 文件下载失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                DebugLog.i(Tag, " startUpdateRNFile : onSuccess");
                if (file != null && file.length() > 0) {
                    DebugLog.i(Tag, " startUpdateRNFile : 文件下载成功  保存文件");
                    DebugLog.i(Tag, "startUpdateRNFile 文件下载成功 文件大小："+file.length()/1024+" kb");
                    RNFileUpdateUtil.saveFileZip(file, RNConfigUtil.path + "/" + RNConfigUtil.saveNameZip);
                    boolean b = MD5Util.verdictMD5isEqual(originalFileMD5, RNConfigUtil.path + "/" + RNConfigUtil.saveNameZip);
                    if (b){
                        DebugLog.i(Tag, " startUpdateRNFile : 文件下校验成功 开始解压");
                        unZipFile(ctx);
                    }else {
                        DebugLog.i(Tag, " startUpdateRNFile : 文件下校验失败 删除更新包");
                        RNFileUpdateUtil.deleteExistsFile(RNConfigUtil.path + "/" + RNConfigUtil.saveNameZip);
                    }
                }
            }
        });
    }

    private void requestForJSBundle(String fullUpdateURL,AsyncHttpResponseHandler handler) {
        DebugLog.i(Tag, " requestForJSBundle : 文件下载路径: "+fullUpdateURL);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("accept", "*/*");
        client.get(fullUpdateURL, handler);
    }

    /**
     * 文件解压
     */
    private void unZipFile(final Context ctx) {
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
                /**
                 * 解压完成   操作文件  合成补丁
                 */
                RNFileUpdateUtil.deleteExistsFile(RNConfigUtil.path + "/" + RNConfigUtil.saveNameZip);
                exchangeBundleFile(model,ctx);
            }
        });
        task.execute();
    }

    /**
     * 替换完整bundle
     */
    private void exchangeBundleFile(UnZipFileModel currentZipInnerFileModel,Context ctx) {
        RNVersionVerdifyUtil rnVersionVerdifyUtil = new RNVersionVerdifyUtil(ctx);
        DebugLog.e(Tag, "更改 bundle 文件");

        String oldP = RNConfigUtil.path + "/" + rnVersionVerdifyUtil.getBundleNameFromPrf();
        String newP = currentZipInnerFileModel.getPatchFilePath();
        String newBundelName = "newHome" + System.currentTimeMillis() + ".js";
        String newSaveP = RNConfigUtil.path + "/" + newBundelName;

        if (fileReplace(newP, newSaveP)){
            DebugLog.e(Tag, "更改成功  删除旧文件 更新bundle名称");
            RNFileUpdateUtil.deleteExistsFile(oldP);
            RNFileUpdateUtil.deleteExistsFile(newP);
            rnVersionVerdifyUtil.saveBundleNameToPrf(newBundelName);
        }else {
            DebugLog.e(Tag, "更改失败  不处理");
        }
    }

    /**
     * 替换文件路径
     */
    private boolean fileReplace(String filePath,String newPath) {
        boolean b = false;
        try {
             b = new File(filePath).renameTo(new File(newPath));
        } catch (Exception e) {
            DebugLog.e(Tag, "更改失败");
        }
        return b;
    }

}
