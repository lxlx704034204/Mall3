package com.hxqc.pay.api;

import android.text.TextUtils;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.OrderPayRequest;
import com.hxqc.mall.core.model.OrderRequest;
import com.hxqc.mall.core.model.loan.InstallmentInfo;
import com.hxqc.pay.model.CompleteInfoModelRequest;
import com.hxqc.util.DebugLog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Author: wanghao
 * Date: 2015-03-23
 * FIXME
 * 购车流程
 */
public class PayApiClient extends BaseApiClient {
    @Override
    protected String completeUrl(String control) {
//        return HOST + "/Mall/" + API_VERSION + control;
        return ApiUtil.getMallUrl(control);
    }

    /**
     * 获取自提点
     */
    public void getPickupPoint(String orderID,String itemID, String cityID, String provinceID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/PickupPoint");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("itemID", itemID);
        requestParams.put("cityID", cityID);
        requestParams.put("provinceID", provinceID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 拿到预览合同样本
     */
    public void getContractSample(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/contract");
        gGetUrl(url, handler);
    }

    /**
     * 建立普通订单
     */
    public void createOrderNormal(OrderPayRequest request, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/normal");

        RequestParams requestParams = new RequestParams();
        requestParams.put("isLicense", request.isLicense);
//        requestParams.put("insuranceFirm", request.insuranceFirm);
        requestParams.put("paymentType", request.paymentType);
        requestParams.put("itemID", request.itemID);
        requestParams.put("fullname", request.fullname);
        requestParams.put("cellphone", request.cellphone);
        requestParams.put("identifier", request.identifier);
        requestParams.put("province", request.province);
        requestParams.put("city", request.city);
        requestParams.put("district", request.district);
        requestParams.put("address", request.address);
        requestParams.put("packageID", request.packageID);
        requestParams.put("accessoryID", request.accessoryID);
        requestParams.put("insurance", request.insurance);
        requestParams.put("financeID", request.financeID);
//        String token = sharePreHelp.getToken();
        //        requestParams.put("token", token);
//        requestParams.put("area", EVSharePreferencesHelper.getLastHistoryCity(SampleApplicationContext.application));
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 建立特卖订单
     */
    public void createSeckillOrder(OrderRequest orderRequest, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/seckill");
        RequestParams requestParams = new RequestParams();
        requestParams.put("isLicense", orderRequest.isLicense);
//        requestParams.put("insuranceFirm", orderRequest.insuranceFirm);
        requestParams.put("insurance", orderRequest.insurance);
        requestParams.put("paymentType", orderRequest.paymentType);
        requestParams.put("itemID", orderRequest.itemID);
//        requestParams.put("token", sharePreHelp.getToken());
        requestParams.put("packageID", orderRequest.packageID);
        requestParams.put("accessoryID", orderRequest.accessoryID);
        requestParams.put("financeID", orderRequest.financeID);
        gPostUrl(url, requestParams, handler);

    }

    /**
     * 获取支付方式列表
     */
    public void listPayment(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Payment/listPayment");
        gGetUrl(url, handler);
    }

    /**
     * 获取应该支付的订金金额
     */
    public void getDepositAmount(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Payment/prepay");
        RequestParams requestParams = new RequestParams();
//        requestParams.put("token", sharePreHelp.getToken());
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取 支付时 总额 未支付 已支付
     */
    public void getPayHeadBarData(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/paidAmount");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
//        requestParams.put("token", sharePreHelp.getToken());
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 支付 付款
     */
    public void payOnline(String orderID, String paymentID, String money, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Payment/pay");
        RequestParams requestParams = new RequestParams();
//        requestParams.put("token", sharePreHelp.getToken());
        requestParams.put("orderID", orderID);
        requestParams.put("paymentID", paymentID);
        requestParams.put("money", money);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 订单详情
     */
    public void orderDetail(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/detail");
        RequestParams requestParams = new RequestParams();
//        requestParams.put("token", sharePreHelp.getToken());
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 完善信息
     */
    public void fillInformation(CompleteInfoModelRequest infoModelRequest, AsyncHttpResponseHandler handler) {

        String url = completeUrl("/Order/expressType");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", infoModelRequest.orderID);
        requestParams.put("expressType", infoModelRequest.expressType);
        requestParams.put("pickupPoint", infoModelRequest.pickupPoint);
        requestParams.put("token", UserInfoHelper.getInstance().getToken(SampleApplicationContext.application));
        if (!TextUtils.isEmpty(infoModelRequest.province)) {
            requestParams.put("province", infoModelRequest.province);
            requestParams.put("city", infoModelRequest.city);
            requestParams.put("district", infoModelRequest.district);
            requestParams.put("address", infoModelRequest.address);
            requestParams.put("fullname", infoModelRequest.fullname);
            requestParams.put("cellphone", infoModelRequest.cellphone);
            DebugLog.i("address", infoModelRequest.toString());
        }
        gPutUrl(url, requestParams, handler);
    }

    /**
     * 更新特卖订单信息
     */
    public void updateSeckillOrder(OrderPayRequest updateRequest, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/seckill");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", updateRequest.orderID);
        requestParams.put("fullname", updateRequest.fullname);
        requestParams.put("cellphone", updateRequest.cellphone);
        requestParams.put("identifier", updateRequest.identifier);
        requestParams.put("province", updateRequest.province);
        requestParams.put("city", updateRequest.city);
        requestParams.put("district", updateRequest.district);
        requestParams.put("address", updateRequest.address);
        requestParams.put("token", UserInfoHelper.getInstance().getToken(SampleApplicationContext.application));
        gPutUrl(url, requestParams, handler);

    }

    /**
     * 获取默认收货地址列表
     */
    public void getDefaultAddress(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Users/address");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", UserInfoHelper.getInstance().getToken(SampleApplicationContext.application));
        requestParams.put("default", 1);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取支付状态
     */
    public void getPayStatus(String paymentID, String orderID, String tradeID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/tradeStatus");
        RequestParams requestParams = new RequestParams();
        requestParams.put("token", UserInfoHelper.getInstance().getToken(SampleApplicationContext.context));
        requestParams.put("paymentID", paymentID);
        requestParams.put("orderID", orderID);
        requestParams.put("tradeID", tradeID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取正式合同
     */
    public void getFormalContract(OrderPayRequest request, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/contract");

        RequestParams requestParams = new RequestParams();
        requestParams.put("isSeckill", request.isSeckill);
        requestParams.put("orderID", request.orderID);
        requestParams.put("isLicense", request.isLicense);
        requestParams.put("insurance", request.insurance);
//        requestParams.put("insuranceType", request.insuranceType);
        requestParams.put("paymentType", request.paymentType);
        requestParams.put("itemID", request.itemID);
        requestParams.put("fullname", request.fullname);
        requestParams.put("cellphone", request.cellphone);
        requestParams.put("identifier", request.identifier);
        requestParams.put("province", request.province);
        requestParams.put("city", request.city);
        requestParams.put("district", request.district);
        requestParams.put("address", request.address);
        requestParams.put("packageID", request.packageID);
        requestParams.put("accessoryID", request.accessoryID);
        requestParams.put("financeID", request.financeID);

        gPostUrl(url, requestParams, handler);
    }

    /**
     * 上传图片
     *
     * @param handler
     * @throws FileNotFoundException
     */
    public void uploadImage(File imageFile, AsyncHttpResponseHandler handler) throws FileNotFoundException {

        String url = completeUrl("/Upload/image");
        DebugLog.d(TAG, "uploadImage() returned: " + imageFile);
        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("image", imageFile);
        client.post(url, mRequestParams, handler);
//        gPostUrl(url, mRequestParams, handler);
    }

    /**
     * 获取分期资料
     *
     * @param orderID
     * @param handler
     */
    public void getInstallmentInfo(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Installment/info");
        RequestParams mRequestParams = new RequestParams();
//        mRequestParams.put("orderID", "081571681198258051");
        mRequestParams.add("orderID", orderID);
        gGetUrl(url, mRequestParams, handler);
    }

    /**
     * 上传分期资料
     *
     * @param orderId
     * @param installmentInfo
     * @param handler
     */
    public void uploadInstallmentInfo(String orderId, InstallmentInfo installmentInfo,
                                      AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Installment/info");
        DebugLog.d(TAG, "uploadInstallmentInfo() called with: " + "orderId = [" + orderId + "], installmentInfo = [" +
                installmentInfo + "], handler = [" + handler + "]");
        RequestParams mRequestParams = new RequestParams();
//        mRequestParams.put("orderID", "081571681198258051");
        mRequestParams.add("orderID", orderId);
        mRequestParams.put("sex", installmentInfo.sex);
        mRequestParams.put("monthSalary", installmentInfo.monthSalary);
        mRequestParams.put("houseProperty", installmentInfo.houseProperty);
        mRequestParams.put("marriageStatus", installmentInfo.marriageStatus);
        mRequestParams.put("industry", installmentInfo.industry);
        mRequestParams.put("info1", installmentInfo.info1);
        mRequestParams.put("info2", installmentInfo.info2);
        mRequestParams.put("info3", installmentInfo.info3);
        mRequestParams.put("info4", installmentInfo.info4);
        mRequestParams.put("info5", installmentInfo.info5);
        gPostUrl(url, mRequestParams, handler);
    }


}
