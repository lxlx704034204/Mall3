package com.hxqc.fastreqair.api;

import android.content.Context;
import android.graphics.Bitmap;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.photolibrary.util.ImageUtils;
import com.hxqc.util.BitmapCompress;
import com.hxqc.util.ScreenUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

/**
 * liaoguilong
 * Created by CPR113 on 2016/5/17.
 * 洗车接口
 */
public class CarWashApiClient extends BaseApiClient {

    @Override
    protected String completeUrl(String control) {
        return ApiUtil.getCarWashURL(control);
    }


    /**
     * 洗车订单详情
     *
     * @param orderID
     */
    public void getCarWashDetail(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/carWashDetail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 洗车退款
     *
     * @param orderID
     */
    public void carWashRefund(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/carWashRefund");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gDeleteUrl(url, requestParams, handler);
    }

//    public void sendComment(String orderID,String shopID,int score,
//                            String content,String tagsID,RequestParams requestParams,
//                            AsyncHttpResponseHandler handler) {
//       String url = completeUrl("/Comments/submit");
// //       String url ="http://10.0.15.203:8089/Carwash/V2/Comments/submit";
//        requestParams.put("orderID", orderID);
//        requestParams.put("shopID", shopID);
//        requestParams.put("score", score);
//        requestParams.put("content", content);
//        requestParams.put("tagsID", tagsID);
//        gPostUrl(url, requestParams, handler);
//    }


    /**
     * 发表评论
     */
    public void sendComment(Context context,String orderID,String shopID,int score, String content,String tagsID, List< ImageItem > pics, AsyncHttpResponseHandler handler) throws FileNotFoundException {
        String url = completeUrl("/Comments/submit");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("shopID", shopID);
        requestParams.put("score", score);
        requestParams.put("content", content);
        requestParams.put("tagsID", tagsID);
        requestParams = getDESRequestParams(url, requestParams);
        if (pics != null && pics.size() > 0) {
            int size = pics.size();
            for (int i = 0; i < size; i++) {
                try {

                    Bitmap bitmap = ImageUtils.compressAndGetImgBitmap(
                            pics.get(i).sourcePath,
                            ScreenUtil.getScreenWidth(context),
                            ScreenUtil.getScreenHeight(context));

                    @SuppressWarnings("ConstantConditions")
                    String path = context.getExternalCacheDir().getPath() + "/temp" + i + ".jpg";

                    BitmapCompress.compressImageToSpecifySizeAndSaveFile(bitmap, 640, 640, path, 96);
                    File file = new File(path);
                    requestParams.put("image" + (i + 1), file);
                    bitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        client.post(url, requestParams, handler);
    }


    /** 洗车列表 **/
    public void getCarWashShopListData(int page, int count, HashMap<String, String> hashMap, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams(hashMap);
        requestParams.put("page", page);
        requestParams.put("count", count);
        gGetUrl(completeUrl("/ShopList"), requestParams, handler);
    }

    /** 洗车列表筛选条件 **/
    public void getCarWashListFilterData(AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        gGetUrl(completeUrl("/CarwashCharge"), requestParams, handler);
    }

    /** 评论标签 **/
    public void getCarWashTags(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Comments/tags");
   //     String url ="http://10.0.15.203:8089/Carwash/V2/Comments/tags";
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 洗车店评论列表
     * @param page
     * @param count
     * @param shopID
     * @param handler
     */
    public void ShopCommentList(int page , int count, String shopID , AsyncHttpResponseHandler handler){
        String url = completeUrl("/ShopCommentList");
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", page);
        requestParams.put("count", count);
        requestParams.put("shopID", shopID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 洗车付款码
     * @param handler
     */
    public void CaptchaPay(AsyncHttpResponseHandler handler){
        String url = completeUrl("/CaptchaPay");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        gGetUrl(url, requestParams, handler);
    }

    /**
     *
     * @param shopID
     * @param handler
     */
    public void ShopDetail(String shopID,AsyncHttpResponseHandler handler){
        String url = completeUrl("/ShopDetail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        gGetUrl(url, requestParams, handler);
    }

}
