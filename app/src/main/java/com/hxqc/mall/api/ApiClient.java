package com.hxqc.mall.api;

import android.os.Build;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.mall.core.model.DeliveryAddress;
import com.hxqc.mall.core.model.RefundRequest;
import com.hxqc.mall.core.util.OtherUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 说明:接口访问
 * <p/>
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class ApiClient extends BaseApiClient {
    public ApiClient() {
        super();
    }

    @Override
    protected String completeUrl(String control) {
//        return HOST + "/Mall/" + API_VERSION + control;
        return ApiUtil.getMallUrl(control);
    }


    /**
     * 首页数据
     */
    public void getHome(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Home");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 意见反馈
     *
     * @param mobile
     * @param content
     * @param type
     */
    public void sendAdvice(String mobile, String content, int type, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Advice");
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", mobile);
        requestParams.put("content", content);
        requestParams.put("type", type);
        requestParams.put("os", "Android");
        requestParams.put("osVersion", "Android" + Build.VERSION.RELEASE);
        requestParams.put("deviceName", Build.MODEL);
        gPostUrl(url, requestParams, handler);
    }


    /**
     * 我的关注
     */
    public void wishList(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users/myCollect");
        gGetUrl(url, handler);
    }

    /**
     * 关注状态
     *
     * @param itemID
     * @param collectState
     * @param handler
     */
    public void collect(String itemID, boolean collectState, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users/collect");
        RequestParams requestParams = new RequestParams();
        requestParams.put("itemID", itemID);
        requestParams.put("collectState", OtherUtil.boolean2Int(collectState));
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 获取保险公司数据
     *
     * @param handler
     */
    public void insurancesData(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Insurances");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        gPostUrl(url, requestParams, handler);
    }


    /**
     * 获取收货地址列表
     *
     */
    @Deprecated
    public void getAddress(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users/address");
        gGetUrl(url, handler);
    }

    /**
     * 新增收货地址
     *
     */
    @Deprecated
    public void addAddress(DeliveryAddress deliveryAddress, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users/address");
        RequestParams requestParams = new RequestParams();
        requestParams.put("consignee", deliveryAddress.consignee);
        requestParams.put("phone", deliveryAddress.phone);
        requestParams.put("detailedAddress", deliveryAddress.detailedAddress);
        requestParams.put("provinceID", deliveryAddress.provinceID);
        requestParams.put("cityID", deliveryAddress.cityID);
        requestParams.put("districtID", deliveryAddress.districtID);
        requestParams.put("isDefault", deliveryAddress.isDefault);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 修改收货地址
     */
    @Deprecated
    public void editAddress(DeliveryAddress deliveryAddress, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users/address");
        RequestParams requestParams = new RequestParams();
        requestParams.put("consignee", deliveryAddress.consignee);
        requestParams.put("phone", deliveryAddress.phone);
        requestParams.put("detailedAddress", deliveryAddress.detailedAddress);
        requestParams.put("provinceID", deliveryAddress.provinceID);
        requestParams.put("cityID", deliveryAddress.cityID);
        requestParams.put("districtID", deliveryAddress.districtID);
        requestParams.put("isDefault", deliveryAddress.isDefault);
        requestParams.put("addressID", deliveryAddress.addressID);
        requestParams.put("addressMD5", deliveryAddress.addressMD5);
        gPutUrl(url, requestParams, handler);
    }

    /**
     * 删除收货地址
     *
     * @param token
     */
    @Deprecated
    public void deleteAddress(String token, DeliveryAddress deliveryAddress, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users/address");
        RequestParams requestParams = new RequestParams();
        requestParams.put("addressID", deliveryAddress.addressID);
        requestParams.put("token", token);
        requestParams.put("addressMD5", deliveryAddress.addressMD5);
        gDeleteUrl(url, requestParams, handler);
    }

    /**
     * 获取推荐活动列表
     *
     * @param token
     * @param page
     */
    public void getPromotionMessage(String token, int page, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Message/promotion");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("page", page);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取取消订单理由列表
     *
     * @param token
     */
    public void getCancelReason(String token, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/cancelReason");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 取消订单
     *
     * @param token
     * @param orderID
     * @param reasonID
     */
    public void cancelOrder(String token, String orderID, int reasonID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/cancel");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("orderID", orderID);
        requestParams.put("reasonID", reasonID);
        gPutUrl(url, requestParams, handler);
    }

    /**
     * 修改订单（支付方式）
     *
     * @param token
     */
    public void setPaymentType(String token, String orderID, String paymentType, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("orderID", orderID);
        requestParams.put("paymentType", paymentType);
        gPutUrl(url, requestParams, handler);
    }


    /**
     * 获取我的评论
     *
     * @param token
     * @param commentStatus
     * @param page
     */
    public void getMyComments(String token, String commentStatus, int page, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Comments/myComments");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("commentStatus", commentStatus);
        requestParams.put("page", page);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 获取我的订单/特卖订单
     *
     * @param token
     */
    public void getOrders(String token, int page, int count, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/orderList");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("page", page);
        requestParams.put("count", count);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取订单详情
     *
     * @param token
     * @param orderID
     */
    public void getOrderDetail(String token, String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/detail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取订单消息列表
     *
     * @param token
     * @param page
     */
    public void getOrderMessage(String token, int page, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Message/Order");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("page", page);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 设置消息状态
     *
     * @param token
     * @param messageID
     * @param isRead
     */
    public void setMessageStatus(String token, int messageType, int messageID, int isRead, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Message");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("messageType", messageType);
        requestParams.put("messageID", messageID);
        requestParams.put("isRead", isRead);
        gPutUrl(url, requestParams, handler);
    }


    /**
     * 订单追踪
     *
     * @param token
     * @param orderID
     */
    public void expressDetail(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/expressDetail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 退款原因
     */
    public void refundReason(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/refundReason");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
//        requestParams.put("token", sharePreHelp.getToken());
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 申请退款
     */
    public void applicationRefund(RefundRequest request, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/refund");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", request.orderID);
//        requestParams.put("token", sharePreHelp.getToken());
        requestParams.put("reasonID", request.reasonID);
        requestParams.put("memo", request.memo);
        gPutUrl(url, requestParams, handler);
    }


    /**
     * 我的消息
     *
     * @param handler
     */
    public void newest(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Message/newest");
        gGetUrl(url, handler);
    }

}
