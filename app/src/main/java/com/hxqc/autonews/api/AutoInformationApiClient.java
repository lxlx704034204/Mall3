package com.hxqc.autonews.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.mall.photolibrary.util.ImageUtils;
import com.hxqc.util.BitmapCompress;
import com.hxqc.util.ScreenUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.IOException;

/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 新车资讯web请求集合
 */
public class AutoInformationApiClient extends BaseApiClient {
    @Override
    protected String completeUrl(String control) {
        return ApiUtil.getAutoInfoURL(control);
    }

    /**
     * 汽车资讯列表首页
     *
     * @param count
     * @param page
     * @param handler
     */
    public void requestAutoInformation(int count, int page, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Index");
        RequestParams requestParams = new RequestParams();
        requestParams.put("count", count);
        requestParams.put("page", page);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 汽车资讯 分类列表
     *
     * @param count
     * @param page
     * @param guideCode
     * @param handler
     */
    public void requestAutoInformationByType(int count, int page, String guideCode, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Index/guideInfo");
        RequestParams requestParams = new RequestParams();
        requestParams.put("count", count);
        requestParams.put("page", page);
        requestParams.put("guideCode", guideCode);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取资讯的分类栏目
     *
     * @param handler
     */
    public void requestInfoType(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Index/guide");
        gGetUrl(url, new RequestParams(), handler);
    }

    /**
     * 资讯详情
     *
     * @param infoID
     * @param handler
     */
    public void requestAutoInformationDetail(String infoID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Index/detail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("infoID", infoID);
        gGetUrl(url, requestParams, handler);


    }

    /**
     * 发布评论
     *
     * @param infoID
     * @param content
     * @param handler
     */
    public void sendAutoInfoComment(String infoID, String content, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/AutoInfoComment/send");
        RequestParams requestParams = new RequestParams();
        requestParams.put("infoID", infoID);
        requestParams.put("content", content);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 发表 口碑评价
     *
     * @param extID
     * @param brand
     * @param series
     * @param content
     * @param image1
     * @param image2
     * @param image3
     * @param image4
     * @param image5
     * @param image6
     * @param comfort
     * @param space
     * @param power
     * @param fuelConsumption
     * @param appearance
     * @param interiorTrimming
     * @param handler
     */
    public void sendAutoGrade(Context context, String extID, String brand, String series, String content, String image1, String image2, String image3,
                              String image4, String image5, String image6, int comfort, int space, int power, int fuelConsumption,
                              int appearance, int interiorTrimming, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/AutoGrade/send");
        RequestParams requestParams = new RequestParams();
        requestParams.put("extID", extID);
        requestParams.put("brand", brand);
        requestParams.put("series", series);
        requestParams.put("content", content);
        requestParams.put("comfort", "" + comfort);
        requestParams.put("space", "" + space);
        requestParams.put("power", "" + power);
        requestParams.put("fuelConsumption", "" + fuelConsumption);
        requestParams.put("appearance", "" + appearance);
        requestParams.put("interiorTrimming", "" + interiorTrimming);
        requestParams = getDESRequestParams(url, requestParams);

        if (!TextUtils.isEmpty(image1)) {
            try {
                requestParams.put("image1", getFile(context, image1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(image2)) {
            try {
                requestParams.put("image2", getFile(context, image2));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(image3)) {
            try {
                requestParams.put("image3", getFile(context, image3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(image4)) {
            try {
                requestParams.put("image4", getFile(context, image4));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(image5)) {
            try {
                requestParams.put("image5", getFile(context, image5));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(image6)) {
            try {
                requestParams.put("image6", getFile(context, image6));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        client.post(url, requestParams, handler);
    }

    /**
     * 删除 口碑评价
     *
     * @param gradeID
     * @param handler
     */
    public void deleteAutoGrade(String gradeID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/AutoGrade/delete");
        RequestParams requestParams = new RequestParams();
        requestParams.put("gradeID", gradeID);
        gDeleteUrl(url, requestParams, handler);
    }

    /**
     * 口碑评价 列表
     *
     * @param extID
     * @param brand
     * @param series
     * @param page
     * @param handler
     */
    public void requestAutoGrade(String extID, String brand, String series, int page, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/AutoGrade");
        RequestParams requestParams = new RequestParams();
        requestParams.put("extID", extID);
        requestParams.put("brand", brand);
        requestParams.put("series", series);
        requestParams.put("page", "" + page);
        requestParams.put("count", "15");
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 我的评价 口碑评价
     *
     * @param page
     * @param handler
     */
    public void requestMyGrades(int page, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/AutoGrade/myGrades");
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", "" + page);
        requestParams.put("count", "15");
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 发表 资讯评价
     *
     * @param infoID
     * @param pCommentID
     * @param cCommentID
     * @param content
     * @param handler
     */
    public void sendAutoInfoComment(String infoID, String pCommentID, String cCommentID, String content, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/AutoInfoComment/send");
        RequestParams requestParams = new RequestParams();
        requestParams.put("infoID", infoID);
        requestParams.put("pCommentID", pCommentID);
        requestParams.put("cCommentID", cCommentID);
        requestParams.put("content", content);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 删除 资讯评价
     *
     * @param commentID
     * @param handler
     */
    public void deleteAutoInfoComment(String commentID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/AutoInfoComment/delete");
        RequestParams requestParams = new RequestParams();
        requestParams.put("commentID", commentID);
        gDeleteUrl(url, requestParams, handler);
    }

    /**
     * 资讯评价 列表
     *
     * @param infoID
     * @param page
     * @param handler
     */
    public void requestAutoInfoCommentP(String infoID, int page, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/AutoInfoComment/parentlist");
        RequestParams requestParams = new RequestParams();
        requestParams.put("infoID", infoID);
        requestParams.put("page", "" + page);
        requestParams.put("count", "15");
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 资讯评价 单条详情
     *
     * @param infoID
     * @param pCommentID
     * @param page
     * @param handler
     */
    public void requestAutoInfoCommentC(String infoID, String pCommentID, int page, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/AutoInfoComment/chilelist");
        RequestParams requestParams = new RequestParams();
        requestParams.put("infoID", infoID);
        requestParams.put("pCommentID", pCommentID);
        requestParams.put("page", "" + page);
        requestParams.put("count", "15");
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 我的评价 资讯评价
     *
     * @param page
     * @param handler
     */
    public void requestMyComments(int page, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/AutoInfoComment/myComments");
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", "" + page);
        requestParams.put("count", "15");
        gGetUrl(url, requestParams, handler);
    }

    public File getFile(Context context, String sourcePath) throws IOException {
        Bitmap bitmap = ImageUtils.compressAndGetImgBitmap(sourcePath,
                ScreenUtil.getScreenWidth(context),
                ScreenUtil.getScreenHeight(context));

        @SuppressWarnings("ConstantConditions")
        String path = context.getExternalCacheDir().getPath() + "/temp" + System.currentTimeMillis() / 1000 + (int) (Math.random() * 9000 + 1000) + ".jpg";

        BitmapCompress.compressImageToSpecifySizeAndSaveFile(bitmap, 640, 640, path, 96);
        File file = new File(path);
        bitmap.recycle();
        return file;
    }

    public void getAutoCalendarYears(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/AutoCalendar/year");

        gGetUrl(url, handler);
    }

    public void getAutoCalendar(String year, AsyncHttpResponseHandler handler) {

        String url = completeUrl("/AutoCalendar");
        RequestParams params = new RequestParams("year", year);
        gGetUrl(url, params, handler);
    }
}
