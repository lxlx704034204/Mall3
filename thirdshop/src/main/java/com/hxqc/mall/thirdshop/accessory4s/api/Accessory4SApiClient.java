package com.hxqc.mall.thirdshop.accessory4s.api;

import android.text.TextUtils;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


/**
 * 说明:用品接口
 *
 * @author: 吕飞
 * @since: 2015-12-01
 * Copyright:恒信汽车电子商务有限公司
 */
public class Accessory4SApiClient extends BaseApiClient {

    public Accessory4SApiClient() {
        super();
    }


    @Override
    protected String completeUrl(String control) {
        return ApiUtil.getAccessory4SURL(control);
    }

    /**
     * 加入购物车
     *
     * @param handler
     */
    public void addShoppingCart(String productIDList, String productNum, String shopName, String shopID, String packageID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/ambUserCart/AddShoppingCart");
        RequestParams requestParams = new RequestParams();
        requestParams.put("productIDList", productIDList);
        requestParams.put("productNum", productNum);
        requestParams.put("shopName", shopName);
        requestParams.put("shopID", shopID);
        if (!TextUtils.isEmpty(packageID)) {
            requestParams.put("packageID", packageID);
        }
        gPostUrl(url, requestParams, handler);
    }


    /**
     * 余额支付
     *
     * @param handler
     */
    public void balance(String orderID, String paymentID, String password, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appPayment/balance");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("paymentID", paymentID);
        requestParams.put("password", password);
        gPostUrl(url, requestParams, handler);
    }


    /**
     * 取消订单
     *
     * @param handler
     */
    public void cancelOrder(String reason, String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appOrder/CancelAccessoryOrder");
        RequestParams requestParams = new RequestParams();
        requestParams.put("reason", reason);
        requestParams.put("orderID", orderID);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 确认收货
     */
    public void confirmReceived(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/ConfirmReceived");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 从购物车中删除
     */
    public void deleteFromShoppingCart(String productIDList, String packageIDList, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/ambUserCart/DeleteFromShoppingCart");
        RequestParams requestParams = new RequestParams();
        if (!TextUtils.isEmpty(productIDList)) {
            requestParams.put("productIDList", productIDList);
        }
        if (!TextUtils.isEmpty(packageIDList)) {
            requestParams.put("packageIDList", packageIDList);
        }
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 删除订单
     */
    public void deleteOrder(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/DeleteOrder");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 编辑购物车
     */
    public void editShoppingCart(String productIDList, String packageIDList, String productNumList, String packageNumList, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/ambUserCart/EditShoppingCart");
        RequestParams requestParams = new RequestParams();
        if (!TextUtils.isEmpty(productIDList)) {
            requestParams.put("productIDList", productIDList);
        }
        if (!TextUtils.isEmpty(packageIDList)) {
            requestParams.put("packageIDList", packageIDList);
        }
        if (!TextUtils.isEmpty(productNumList)) {
            requestParams.put("productNumList", productNumList);
        }
        if (!TextUtils.isEmpty(packageNumList)) {
            requestParams.put("packageNumList", packageNumList);
        }
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 筛选条件
     */
    public void filter(String siteID, String shopID, String latitude, String longitude, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Filter");
        RequestParams requestParams = new RequestParams();
        requestParams.put("siteID", siteID);
        if (!TextUtils.isEmpty(shopID)) {
            requestParams.put("shopID", shopID);
        }
        if (!TextUtils.isEmpty(latitude)) {
            requestParams.put("latitude", latitude);
        }
        if (!TextUtils.isEmpty(longitude)) {
            requestParams.put("longitude", longitude);
        }
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 去结算
     */
    public void goToCheckout(String json, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/ambUserCart/GoToCheckout");
        RequestParams requestParams = new RequestParams();
        requestParams.put("json", json);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 支付方式列表
     */
    public void listPayment(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appPayment/listPayment");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 订单详情
     */
    public void orderDetail(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/ambOrder/orderDetail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取购物车数据
     */
    public void shoppingCart(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/ambUserCart/AccessoryShoppingCart");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 付款
     */
    public void payment(String orderID, String paymentID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appOrder/AccessoryPayment");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("paymentID", paymentID);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 提交订单
     */
    public void submitOrder(String amount, String json, String contactName, String contactPhone, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appOrder/SubmitAccessoryOrder");
        RequestParams requestParams = new RequestParams();
        requestParams.put("amount", amount);
        requestParams.put("json", json.replace(" ", ""));
        requestParams.put("contactName", contactName);
        requestParams.put("contactPhone", contactPhone);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 申请退款
     */
    public void refund(String orderID, String reason, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appOrder/Refund");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("reason", reason);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 商品详情
     */
    public void productDetail(String productID, String productGroupID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/ProductDetail");
        RequestParams requestParams = new RequestParams();
        if (!TextUtils.isEmpty(productID)) {
            requestParams.put("productID", productID);
        }
        if (!TextUtils.isEmpty(productGroupID)) {
            requestParams.put("productGroupID", productGroupID);
        }
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 用品组列表
     */
    public void productList(String page, String brand, String series, String latitude, String longitude, String siteID, String shopID, String class1stID, String class2ndID, String priceOrder, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/ProductList");
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", page);
        if (!TextUtils.isEmpty(brand)) {
            requestParams.put("brand", brand);
        }
        if (!TextUtils.isEmpty(series)) {
            requestParams.put("series", series);
        }
        if (!TextUtils.isEmpty(latitude)) {
            requestParams.put("latitude", latitude);
        }
        if (!TextUtils.isEmpty(longitude)) {
            requestParams.put("longitude", longitude);
        }
        if (!TextUtils.isEmpty(siteID)) {
            requestParams.put("siteID", siteID);
        }
        if (!TextUtils.isEmpty(shopID)) {
            requestParams.put("shopID", shopID);
        }
        if (!TextUtils.isEmpty(class1stID)) {
            requestParams.put("class1stID", class1stID);
        }
        if (!TextUtils.isEmpty(class2ndID)) {
            requestParams.put("class2ndID", class2ndID);
        }
        if (!TextUtils.isEmpty(priceOrder)) {
            requestParams.put("priceOrder", priceOrder);
        }
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 分期 车系列表接口
     *
     * @param shopID
     * @param brand
     * @param serie
     * @param handler
     */
    public void searchAutoSericeInstallmentList(String shopID, String brand, String serie, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Site/searchAutoSericeInstallmentList");
        RequestParams requestParams = new RequestParams();
        requestParams.put("shopID", shopID);
        requestParams.put("brand", brand);
        requestParams.put("serie", serie);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 店铺 车系分期列表接口
     *
     * @param shopID
     * @param handler
     */
    public void autoSericesInstallment(String shopID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Shop/autoSericesInstallment");
        RequestParams requestParams = new RequestParams();
        requestParams.put("shopID", shopID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 店铺 车型分期列表接口
     *
     * @param shopID
     * @param brand
     * @param serie
     * @param handler
     */
    public void autoModelInstallment(String shopID, String brand, String serie, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Shop/autoModelInstallment");
        RequestParams requestParams = new RequestParams();
        requestParams.put("shopID", shopID);
        requestParams.put("brand", brand);
        requestParams.put("serie", serie);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 分期 筛选条件 品牌列表
     *
     * @param siteID
     * @param handler
     */
    public void filterAutoBrandInstallment(String siteID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Filter/filterAutoBrandInstallment");
        RequestParams requestParams = new RequestParams();
        requestParams.put("siteID", siteID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 分期 筛选条件 车系列表
     *
     * @param siteID
     * @param handler
     */
    public void filterAutoSeriesInstallment(String brand, String siteID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Filter/filterAutoSeriesInstallment");
        RequestParams requestParams = new RequestParams();
        requestParams.put("brand", brand);
        requestParams.put("siteID", siteID);
        gGetUrl(url, requestParams, handler);
    }
}
