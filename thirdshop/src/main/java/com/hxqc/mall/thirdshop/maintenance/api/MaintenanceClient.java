package com.hxqc.mall.thirdshop.maintenance.api;

import android.content.Context;
import android.graphics.Bitmap;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.photolibrary.util.ImageUtils;
import com.hxqc.util.BitmapCompress;
import com.hxqc.util.DebugLog;
import com.hxqc.util.ScreenUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年02月16日
 */
public class MaintenanceClient extends BaseApiClient {


    @Override
    protected String completeUrl(String control) {
//        return HOST + "/maintain/" + API_VERSION + control;
        return ApiUtil.getMaintainURL(control);
    }


    /**
     * 获取筛选品牌
     *
     * @param handler
     */
    public void requestFilterBrand(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Filter/brand");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 获取筛选车系
     *
     * @param handler
     */
    public void requestFilterSeries(String brandName, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Filter/series");
        RequestParams requestParams = new RequestParams();
        requestParams.put("brand", brandName);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 新维修保养接口
     *
     * @param page
     * @param count
     * @param brandID
     * @param autoModelID
     * @param shopType
     * @param searchConditionMap
     * @param handler            sort 排序方式 10综合 20价格降序 30价格升序 40满意度降序 50 距离升序
     */
    public void requireNewMaintenanceData(int page, int count, String brandID, String seriesID, String autoModelID, String myAutoID, String items, int shopType, HashMap<String, String> searchConditionMap, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Filter/shopN2");
        RequestParams requestParams = new RequestParams(searchConditionMap);
        requestParams.put("page", page);
        requestParams.put("count", count);
        requestParams.put("brandID", brandID);
        requestParams.put("seriesID", seriesID);
        requestParams.put("autoModelID", autoModelID);
        requestParams.put("myAutoID", myAutoID);
        requestParams.put("shopType", shopType);
        requestParams.put("items", items);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 维修保养接口
     *
     * @param page  分页页数，不传时默认为1
     * @param count 分页每页条目数，不传时默认为15
     */
    public void requireMaintenanceData(int page, int count, HashMap<String, String> searchConditionMap, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Filter/maintenanceShop");
        RequestParams requestParams = new RequestParams(searchConditionMap);
        requestParams.put("page", page);
        requestParams.put("count", count);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 维修预约接口
     *
     * @param page  分页页数，不传时默认为1
     * @param count 分页每页条目数，不传时默认为15
     */
    public void requireAppointmentData(int page, int count, HashMap<String, String> searchCondition, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Filter/reservationShop");
        RequestParams requestParams = new RequestParams(searchCondition);
        requestParams.put("page", page);
        requestParams.put("count", count);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 紧急救援接口
     *
     * @param page
     * @param count
     * @param searchCondition
     * @param handler
     */
    public void requireRescueData(int page, int count, HashMap<String, String> searchCondition, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Filter/rescueShop");
        RequestParams requestParams = new RequestParams(searchCondition);
        requestParams.put("page", page);
        requestParams.put("count", count);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 获取首页的数据
     *
     * @param autoModel
     * @param drivingDistance
     * @param plateNumber
     * @param shopID          必填
     * @param handler
     */
    public void home(String autoModel, String autoModelID, String drivingDistance, String plateNumber, String shopID, AsyncHttpResponseHandler handler) {
        DebugLog.d(TAG, "home() called with:shopID=[" + shopID + "] ");
        String url = completeUrl("/Home");
        RequestParams requestParams = new RequestParams();
        requestParams.put("shopID", shopID);
        requestParams.put("autoModel", autoModel);
        requestParams.put("autoModelID", autoModelID);
        requestParams.put("drivingDistance", drivingDistance);
        requestParams.put("plateNumber", plateNumber);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 优惠套餐列表
     *
     * @param shopID
     * @param autoModelID
     * @param type        套餐类型 10 优惠套餐     20 基本套餐 默认 10
     * @param handler
     */
    public void packageList(String shopID, String autoModelID, String type, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("shopID", shopID);
        requestParams.put("autoModelID", autoModelID);
        requestParams.put("type", type);
        gGetUrl(completeUrl("/Maintenance/packages"), requestParams, handler);
    }


    /**
     * 获取首页的客服数据
     *
     * @param shopID
     * @param handler
     */
    public void serviceAdvisers(String shopID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/ServiceAdviser");
        RequestParams requestParams = new RequestParams();
        requestParams.put("shopID", shopID);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 获取首页的技师数据
     *
     * @param shopID
     * @param handler
     */
    public void mechanics(String shopID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Mechanic");
        RequestParams requestParams = new RequestParams();
        requestParams.put("shopID", shopID);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 保养手册
     *
     * @param modelID
     * @param handler
     */
    public void manual(String modelID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("modelID", modelID);
        gGetUrl(completeUrl("/Maintenance/manual"), requestParams, handler);
    }


    /**
     * 保养订单详情
     * @param orderID
     * @param handler
     */
    public void orderMaintenanceDetail( String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/detail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 4s店保养订单详情
     * @param orderID
     * @param handler
     */
    public void orderMaintenance4SShopDetail( String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/detail4SShop");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }
    /**
     * 4s店保养订单详情2
     * @param orderID
     * @param handler
     */
    public void orderMaintenance4SShopDetail2( String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Maintenance/orderDetail4SShop2");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 维修订单详情
     *
     * @param token
     * @param orderID
     * @param handler
     */
    public void orderRepairDetail(String token, String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/reservationMaintainDetail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("token", token);
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 取消维修预约
     *
     * @param orderID
     * @param reason
     * @param handler
     */
    public void reservationMaintainCancel(String orderID, String reason, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/reservationMaintainCancel");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("reason", reason);
        gPostUrl(url, requestParams, handler);
    }


    /**
     * 推荐保养方案
     *
     * @param shopID
     * @param packageID
     */
    public void recommendProgram(String shopID, String packageID, String autoModelID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Maintenance/recommendProgram");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("autoModelID", autoModelID);
        requestParams.put("packageID", packageID);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 保养项目
     *
     * @param shopID
     * @param autoModelID
     */
    public void maintenanceItems(String shopID, String autoModelID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Maintenance/items");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("autoModelID", autoModelID);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 保养配件介绍
     *
     * @param shopID
     * @param goodsID
     * @param handler
     */
    public void goodsIntroduce(String shopID, String goodsID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Maintenance/goodsIntroduce");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("goodsID", goodsID);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 保养项目介绍
     *
     * @param itemId
     * @param handler
     */
    public void itemIntroduce(String itemId, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Maintenance/itemIntroduce");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("itemId", itemId);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 检查套餐
     *
     * @param shopID
     * @param items
     * @param handler
     */
    public void checkPackage(String shopID, String items, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Maintenance/checkPackage");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
//        requestParams.put("autoModelID",autoModelID);
        requestParams.put("items", items);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 准备订单
     *
     * @param shopID
     * @param items
     * @param handler
     */
    public void prepareOrder(String shopID, String items, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/prepare");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
//        requestParams.put("autoModelID",autoModelID);
        requestParams.put("items", items);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 确认订单价格
     *
     * @param shopID
     * @param score
     * @param couponID
     * @param choose
     * @param handler
     */
    public void verifyOrder(String shopID, String score, String couponID, String choose, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/verify");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("score", score);
        requestParams.put("couponID", couponID);
        requestParams.put("choose", choose);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 确认订单（修车保养）
     */
    public void verifyComplete(String orderID,AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/verifyComplete");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }



    /**
     * 创建保养订单
     *
     * @param shopID
     * @param name
     * @param phone
     * @param serviceAdviserID
     * @param mechanicID
     * @param apppintmentDate
     * @param score
     * @param couponID
     * @param choose
     * @param handler
     */
    public void createdOrder(String shopID, String name, String phone, String serviceAdviserID, String mechanicID, String apppintmentDate, String score, String couponID, String choose, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/created");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("name", name);
        requestParams.put("phone", phone);
        requestParams.put("serviceAdviserID", serviceAdviserID);
        requestParams.put("mechanicID", mechanicID);
        requestParams.put("apppintmentDate", apppintmentDate);
        requestParams.put("score", score);
        requestParams.put("couponID", couponID);
        requestParams.put("choose", choose);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 付款方式
     *
     * @param handler
     */
    public void listPayment(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Payment/listPayment");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 获取店铺保养促销详情
     */
    public void getMaintenancePromotionDetail(String promotionID, LoadingAnimResponseHandler handler) {
        String url = completeUrl("/Promotion");
        RequestParams requestParams = new RequestParams();
        requestParams.put("promotionID", promotionID);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 保养取消订单
     *
     * @param orderID
     * @param reason
     * @param handler
     */
    public void cancelOrder( String orderID, String reason, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/cancel");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("reason", reason);
        gPostUrl(url, requestParams, handler);
    }


    /**
     * 保养申请退款
     *
     * @param orderID
     * @param reason
     * @param handler
     */
    public void refundOrder(String orderID, String reason, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/refundApply");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("reason", reason);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 取消申请退款
     *
     * @param orderID
     * @param handler
     */
    public void cancaelRefundOrder(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/cancelRefundApply");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gPostUrl(url, requestParams, handler);
    }



    /**
     * 发表评论
     *
     * @param context          上下文
     * @param orderID          订单ID
     * @param technologyScore  技术评分
     * @param serveScore       //服务评分
     * @param environmentScore //环境评分
     * @param content          //评论内容
     * @param pics             //用户晒图集合
     * @param handler
     * @throws FileNotFoundException
     */
    public void sendComment(Context context, String orderID, int technologyScore, int serveScore, int environmentScore, String content, List<ImageItem> pics, AsyncHttpResponseHandler handler) throws FileNotFoundException {
        String url = completeUrl("/Comments");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("technologyScore", technologyScore);
        requestParams.put("serveScore", serveScore);
        requestParams.put("environmentScore", environmentScore);
        requestParams.put("content", content);
        requestParams = getDESRequestParams(url, requestParams);
        if (pics != null && pics.size() > 0) {
            int size = pics.size();
            for (int i = 0; i < size; i++) {
                try {

                    Bitmap bitmap = ImageUtils.compressAndGetImgBitmap(pics.get(i).sourcePath, ScreenUtil.getScreenWidth(context), ScreenUtil.getScreenHeight(context));

                    @SuppressWarnings("ConstantConditions") String path = context.getExternalCacheDir().getPath() + "/temp" + i + ".jpg";
                    BitmapCompress.compressImageToSpecifySizeAndSaveFile(bitmap, 640, 640, path, 96);
                    File file = new File(path);
                    requestParams.put("image" + (i + 1), file);
                    bitmap.recycle();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        gPostUrl(url, requestParams, handler);
    }


    /**
     * 支付款项
     *
     * @param orderID
     * @param paymentID
     * @param money
     * @param handler
     */
    public void pay(String orderID, String paymentID, String money, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Payment/pay");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("orderID", orderID);
        requestParams.put("paymentID", paymentID);
        requestParams.put("money", money);
        gPostUrl(url, requestParams, handler);
    }


    /**
     * 余额支付
     *a
     */
    public void balance(String orderID, String paymentID, String password, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Pay/blance");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("orderID", orderID);
        requestParams.put("paymentID", paymentID);
        requestParams.put("password", password);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 保养项目概述
     *
     * @param autoModelID
     * @param drivingDistance
     * @param handler
     */
    public void itemsAllOverviewN(String autoModelID, String seriesID,String drivingDistance, String registerTime,AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Maintenance/itemsAllOverviewN");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("autoModelID", autoModelID);
        requestParams.put("seriesID", seriesID);
        requestParams.put("drivingDistance", drivingDistance);
//        requestParams.put("registerTime", registerTime);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 符合车型所有保养项目
     *
     * @param autoModelID
     * @param shopID
     * @param handler
     */
    public void itemsN(String autoModelID,String seriesID,String brandlID, String shopID,String drivingDistance, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Maintenance/itemsN");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("seriesID", seriesID);
        requestParams.put("brandlID", brandlID);
        requestParams.put("autoModelID", autoModelID);
        requestParams.put("drivingDistance", drivingDistance);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 符合车型所有保养项目 for4S
     * @param autoModelID
     * @param seriesID
     * @param brandID
     * @param shopID
     * @param drivingDistance
     * @param handler
     */
    public void itemsFor4S(String autoModelID,String seriesID,String brandID, String shopID,String drivingDistance ,String registerTime, AsyncHttpResponseHandler handler){
        String url = completeUrl("/Maintenance/itemsFor4S2");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("seriesID", seriesID);
        requestParams.put("brandID", brandID);
        requestParams.put("autoModelID", autoModelID);
        requestParams.put("drivingDistance", drivingDistance);
        requestParams.put("registerTime", registerTime);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 准备订单
     *
     * @param shopID
     * @param items
     * @param handler
     */
    public void prepareN(String shopID, String items,String shopType,String brandID,String seriesID,String autoModelID,String couponID, String score ,String myAutoID,String plateNumber,AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Maintenance/prepareN2");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("items", items);
        requestParams.put("brandID", brandID);
        requestParams.put("seriesID", seriesID);
        requestParams.put("autoModelID", autoModelID);
        requestParams.put("shopType", shopType);
        requestParams.put("couponID", couponID);
        requestParams.put("score", score);
        requestParams.put("myAutoID", myAutoID);
        requestParams.put("plateNumber", plateNumber);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 创建订单
     *
     * @param shopID
     * @param name
     * @param phone
     * @param serviceAdviserID
     * @param mechanicID
     * @param apppintmentDate
     * @param handler
     */
    public  void createdN(String shopID, String name, String phone, String serviceAdviserID, String mechanicID,
                          String apppintmentDate,String choose,String shopType ,String brandlID,String seriesID,String autoModelID,String invoice,String drivingDistance,String plateNumber,float score,
                          String couponID,String myAutoID,AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Maintenance/orderCreatedN2");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("name", name);
        requestParams.put("phone", phone);
        requestParams.put("serviceAdviserID", serviceAdviserID);
        requestParams.put("mechanicID", mechanicID);
        requestParams.put("apppintmentDate", apppintmentDate);
        requestParams.put("choose", choose);
        requestParams.put("shopType", shopType);
        requestParams.put("brandlID", brandlID);
        requestParams.put("seriesID", seriesID);
        requestParams.put("autoModelID", autoModelID);
        requestParams.put("invoice", invoice);
        requestParams.put("drivingDistance", drivingDistance);
        requestParams.put("plateNumber", plateNumber);
        requestParams.put("score", score);
        requestParams.put("couponID", couponID);
        requestParams.put("myAutoID", myAutoID);
        gPostUrl(url, requestParams, handler);
    }


    //保养检测的URL
    public  String getMaintainCheckURL(){
        return completeUrl("/explain.html");
    }

}
