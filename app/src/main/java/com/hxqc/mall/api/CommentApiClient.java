package com.hxqc.mall.api;

import android.content.Context;
import android.graphics.Bitmap;

import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.photolibrary.util.ImageUtils;
import com.hxqc.util.BitmapCompress;
import com.hxqc.util.DebugLog;
import com.hxqc.util.ScreenUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Author: wanghao
 * Date: 2015-03-23
 * FIXME
 * 评论列表
 */
public class CommentApiClient extends ApiClient {
    /**
     * 获取该评论列表
     */
    public void getComments(String seriesID, String itemID, int sortType, int page, int count,
                            AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Comments");
        RequestParams requestParams = new RequestParams();
        requestParams.put("itemID", itemID);
        requestParams.put("sortType", sortType);
        requestParams.put("page", page);
        requestParams.put("count", count);
        requestParams.put("seriesID", seriesID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 发表评论
     */
    public void sendComment(Context context, String itemID, String orderID, int score, String content,
                            List< ImageItem > pics, AsyncHttpResponseHandler handler) throws FileNotFoundException {
        String url = completeUrl("/Comments");
//        String token = "03kRebhH7PchYVtqhFSE9vliar3SQ54i";
//        String token = sharePreHelp.getToken();
        RequestParams requestParams = new RequestParams();
        requestParams.put("itemID", itemID);
        requestParams.put("orderID", orderID);
        requestParams.put("score", score);
        requestParams.put("content", content);
//        requestParams.put("token", token);
        requestParams = getDESRequestParams(url, requestParams);
        DebugLog.i("pick", "sendComment:");
        if (pics != null && pics.size() > 0) {
            int size = pics.size();
            for (int i = 0; i < size; i++) {
//                DebugLog.i("pick", "sendComment_pic:" + "file://" + pics.get(i).sourcePath);
                try {

//                    Bitmap bitmap = ImageUtils.getBitmapByDecodeFile(pics.get(i).sourcePath);
                    Bitmap bitmap = ImageUtils.compressAndGetImgBitmap(
                            pics.get(i).sourcePath,
                            ScreenUtil.getScreenWidth(context),
                            ScreenUtil.getScreenHeight(context));

                    @SuppressWarnings("ConstantConditions")
                    String path = context.getExternalCacheDir().getPath() + "/temp" + i + ".jpg";

                    BitmapCompress.compressImageToSpecifySizeAndSaveFile(bitmap, 640, 640, path, 96);
                    File file = new File(path);
                    requestParams.put("image" + (i + 1), file);
//                    DebugLog.i("pick", file.getAbsolutePath());
                    bitmap.recycle();

                } catch (Exception e) {
                    e.printStackTrace();
//                    DebugLog.e("pick", e.toString());
                }

            }
        }
//        gPostUrl(url, requestParams, handler);
        client.post(url, requestParams, handler);
    }

    /**
     * 拿到当前评论详情
     */
    public void getThisCommentDetail(String sku, String commentID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Comments/detail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("commentID", commentID);
        requestParams.put("sku", sku);
//        requestParams.put("token", sharePreHelp.getToken());
        gGetUrl(url, requestParams, handler);
    }


}
