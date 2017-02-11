package com.hxqc.mall.thirdshop.accessory.api;

import android.text.TextUtils;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.mall.thirdshop.model.InvoiceInfo;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;


/**
 * 说明:用品接口
 *
 * @author: 吕飞
 * @since: 2015-12-01
 * Copyright:恒信汽车电子商务有限公司
 */
public class AccessoryApiClient extends BaseApiClient {

    public AccessoryApiClient() {
        super();
    }


    @Override
    protected String completeUrl(String control) {
//        return ACCESSORY_API_HOST + "/accessory/" + API_VERSION + control;
        return ApiUtil.getAccessoryURL(control);
    }

    /**
     * 获取筛选品牌
     *
     * @param handler
     */
    public void requestFilterBrand(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/Brand");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 获取筛选车系
     *
     * @param handler
     */
    public void requestFilterSeries(String brandID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/Series");
        RequestParams requestParams = new RequestParams();
        requestParams.put("brandID", brandID);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 获取用品二级分类数据
     *
     * @param handler
     */
    public void requestAccessoryCategory(String shopID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/AccessoryClass");
        RequestParams requestParams = new RequestParams();
        requestParams.put("shopID", shopID);
        gGetUrl(url, handler);
    }

    /**
     * 用品预付订金
     */
    public void accessoryPayment(String orderID, String paymentID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appOrder/AccessoryPayment");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("paymentID", paymentID);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 余额支付
     */
    public void balancePay(String orderID, String paymentID, String password, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appPayment/balance");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("paymentID", paymentID);
        requestParams.put("password", password);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 获取支付列表
     */
    public void getPaymentList(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appPayment/listPayment");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取工时费
     */
    public void getLaborCost(String shopID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/LaborCost");
        RequestParams requestParams = new RequestParams();
        requestParams.put("shopID", shopID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 取消用品订单
     */
    public void cancelAccessoryOrder(String orderID, String reason, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appOrder/CancelAccessoryOrder");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("reason", reason);
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
     * 取消申请退款
     */
    public void cancelRefund(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appOrder/CancelRefund");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 删除用品订单
     */
    public void deleteAccessoryOrder(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appOrder/DeleteAccessoryOrder");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 用品订单详情
     */
    public void getAccessoryOrderDetail(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appOrder/AccessoryOrderDetail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 用品订单确认收货
     */
    public void confirmReceived(String orderID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appOrder/ConfirmReceived");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gPostUrl(url, requestParams, handler);
    }


    /**
     * 提交用品订单
     */
    public void submitAccessoryOrder(String contactName, String contactPhone, String shopID, String notes, String amount, String json, InvoiceInfo invoiceInfo, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appOrder/SubmitAccessoryOrder");
        RequestParams requestParams = new RequestParams();
        requestParams.put("contactName", contactName);
        requestParams.put("contactPhone", contactPhone);
        requestParams.put("shopID", shopID);
        if (!TextUtils.isEmpty(notes)) {
            requestParams.put("notes", notes);
        }
        requestParams.put("amount", amount);
        requestParams.put("json", json.replace(" ", ""));
        if (!invoiceInfo.getInvoiceContent().equals("-1")) {
            requestParams.put("invoice", JSONUtils.toJson(invoiceInfo));
        }
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 加入购物车
     */
    public void addShoppingCart(String productNum, String model, String productIDList, String packageID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/ambUserCart/AddShoppingCart");
        RequestParams requestParams = new RequestParams();
        requestParams.put("productNum", productNum);
        requestParams.put("productIDList", productIDList);
        if (!TextUtils.isEmpty(packageID)) {
            requestParams.put("packageID", packageID);
        }
        if (!TextUtils.isEmpty(model)) {
            requestParams.put("model", model);
        }
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 从购物车删除项目
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
     * 编辑购物车商品数量
     */
    public void editShoppingCart(String productIDList, String productNumList, String packageIDList, String packageNumList, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/ambUserCart/EditShoppingCart");
        RequestParams requestParams = new RequestParams();
        if (!TextUtils.isEmpty(productIDList)) {
            requestParams.put("productIDList", productIDList);
        }
        if (!TextUtils.isEmpty(packageIDList)) {
            requestParams.put("packageIDList", packageIDList.replace(" ", ""));
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
     * 获取用品购物车
     */
    public void getAccessoryShoppingCart(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/ambUserCart/AccessoryShoppingCart");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 去结算(用品购物车)
     */
    public void goToCheckout(String json, String totalAmount, String totalCount, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/ambUserCart/GoToCheckout");
        RequestParams requestParams = new RequestParams();
        requestParams.put("json", json);
        DebugLog.e("111", json);
        requestParams.put("totalAmount", totalAmount);
        requestParams.put("totalCount", totalCount);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 关注
     */
    public void collect(int collect, String productIDList, String packageIDList, int fromShoppingCart, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/Collect");
        RequestParams requestParams = new RequestParams();
        requestParams.put("collect", collect);
        if (!TextUtils.isEmpty(productIDList)) {
            requestParams.put("productIDList", productIDList);
        }
        if (!TextUtils.isEmpty(packageIDList)) {
            requestParams.put("packageIDList", packageIDList);
        }
        requestParams.put("fromShoppingCart", fromShoppingCart);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 获取分类
     */
    public void getClass(String shopID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/AccessoryClass");
        RequestParams requestParams = new RequestParams();
        if (!TextUtils.isEmpty(shopID)) {
            requestParams.put("shopID", shopID);
        }
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 商品详情
     */
    public void getProductDetail(String productID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/ProductDetail");
        RequestParams requestParams = new RequestParams();
        if (!TextUtils.isEmpty(productID)) {
            requestParams.put("productID", productID);
        }
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 根据商品ID获取适配车系或车型
     */
    public void getProductFitList(String productID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/ProductFitList");
        RequestParams requestParams = new RequestParams();
        if (!TextUtils.isEmpty(productID)) {
            requestParams.put("productID", productID);
        }
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 根据商品系列获取商品组
     */
    public void getProductFitList(String productSeriesID, String shopID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/ProductFitList");
        RequestParams requestParams = new RequestParams();
        requestParams.put("productGroupID", productSeriesID);
        requestParams.put("shopID", shopID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 用品系列组列表
     */
    public void getProductSeriesList(int page, String brandID, String seriesID, String modelID, String class1stID, String class2ndID, String shopID, String productBrandID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/ProductSeriesList");
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", page);
        if (!TextUtils.isEmpty(brandID)) {
            requestParams.put("brandID", brandID);
        }
        if (!TextUtils.isEmpty(seriesID)) {
            requestParams.put("seriesID", seriesID);
        }
        if (!TextUtils.isEmpty(modelID)) {
            requestParams.put("modelID", modelID);
        }
        if (!TextUtils.isEmpty(class1stID)) {
            requestParams.put("class1stID", class1stID);
        }
        if (!TextUtils.isEmpty(class2ndID)) {
            requestParams.put("class2ndID", class2ndID);
        }
        if (!TextUtils.isEmpty(shopID)) {
            requestParams.put("shopID", shopID);
        }
        if (!TextUtils.isEmpty(productBrandID)) {
            requestParams.put("productBrandID", productBrandID);
        }
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 新版用品列表
     */
    public void getProductList(int page, String class1stID, String class2ndID, String brandID, String seriesID, String priceOrder, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/ProductList");
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", page);
        if (!TextUtils.isEmpty(class1stID)) {
            requestParams.put("class1stID", class1stID);
        }
        if (!TextUtils.isEmpty(class2ndID)) {
            requestParams.put("class2ndID", class2ndID);
        }
        if (!TextUtils.isEmpty(brandID)) {
            requestParams.put("brandID", brandID);
        }
        if (!TextUtils.isEmpty(seriesID)) {
            requestParams.put("seriesID", seriesID);
        }
        if (!TextUtils.isEmpty(priceOrder)) {
            requestParams.put("priceOrder", priceOrder);
        }
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 新版用品列表筛选条件
     */
    public void getProductListFilter(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/ProductListFilter");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 用品系列组列表
     */
    public void getProductSeriesList(int page, int count, HashMap<String, String> searchCondition, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/ProductSeriesList");
        RequestParams requestParams = new RequestParams(searchCondition);
        requestParams.put("page", page);
        requestParams.put("count", count);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 根据品牌获取车系
     */
    public void getSeries(String brandID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/Series");
        RequestParams requestParams = new RequestParams();
        requestParams.put("brandID", brandID);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取店铺列表int
     */
    public void getShopList(int page, int conut, String latitude, String longitude, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/ShopList");
        RequestParams requestParams = new RequestParams();
        requestParams.put("page", page);
        requestParams.put("count", conut);
        if (!TextUtils.isEmpty(latitude)) {
            requestParams.put("latitude", latitude);
        }
        if (!TextUtils.isEmpty(longitude)) {
            requestParams.put("longitude", longitude);
        }
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 根据店铺获取车系车型
     */
    public void getSeriesModel(String shopID, String productID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/SeriesModel");
        RequestParams requestParams = new RequestParams();
        requestParams.put("shopID", shopID);
        if (!TextUtils.isEmpty(productID)) {
            requestParams.put("productID", productID);
        }
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 通过商品系列id返回店铺列表
     */
    public void getShopListByProductSeriesID(int page, int count, HashMap<String, String> searchCondition, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/appItem/ShopListByProductSeriesID");
        if (searchCondition.containsKey("area")) {
            searchCondition.put("city", searchCondition.get("area"));
        } else {
            searchCondition.remove("city");
        }
        RequestParams requestParams = new RequestParams(searchCondition);
        requestParams.put("page", page);
        requestParams.put("count", count);
        gGetUrl(url, requestParams, handler);
    }
}
