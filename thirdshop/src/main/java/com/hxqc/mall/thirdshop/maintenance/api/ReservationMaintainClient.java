package com.hxqc.mall.thirdshop.maintenance.api;

import com.hxqc.mall.core.api.ApiUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 21
 * FIXME
 * Todo 在线预约
 */
public class ReservationMaintainClient extends MaintenanceClient {

//    private final String HOST = "http://10.0.14.201:8080";
//    private final String API_VERSION = "v1";

    @Override
    protected String completeUrl(String control) {
        return ApiUtil.getMaintainURL(control);
    }

    /**
     * 在线预约维修请求
     *
     * @param shopID
     * @param handler
     */

    public void requestReservationMaintain(String shopID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams("shopID", shopID);
        gGetUrl(completeUrl("/ReservationMaintain/prepare"), requestParams, handler);
    }

    /**
     * 服务类型请求
     *
     * @param shopID
     * @param handler
     */
    public void requestServiceType(String shopID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams("shopID", shopID);
        gGetUrl(completeUrl("/MaintenanceWiki"), requestParams, handler);
    }

    /**
     * 服务类型介绍
     *
     * @param shopID
     * @param itemID
     * @param handler
     */
    public void requestServiceTypeIntroduce(String shopID, String itemID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("shopID", shopID);
        requestParams.put("itemID", itemID);
        gGetUrl(completeUrl("/MaintenanceWiki/introduce"), requestParams, handler);
    }

    /**
     * 在线预约维修提交
     *
     * @param shopID
     * @param handler
     */
    public void postReservationMaintain(String plateNumber, String autoModel, String drivingDistance, String name, String phone, String shopID, String shopType, String apppintmentDate, String serviceType, String serviceAdviserID, String mechanicID, String VIN, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = paramsAddDeviceType(new RequestParams());
        requestParams.put("plateNumber", plateNumber);
        requestParams.put("autoModel", autoModel);
        requestParams.put("drivingDistance", drivingDistance);
        requestParams.put("name", name);
        requestParams.put("phone", phone);
        requestParams.put("shopID", shopID);
        requestParams.put("shopType", shopType);
        requestParams.put("apppintmentDate", apppintmentDate);
        requestParams.put("serviceType", serviceType);
        requestParams.put("serviceAdviserID", serviceAdviserID);
        requestParams.put("mechanicID", mechanicID);
        requestParams.put("VIN", VIN);
        gPostUrl(completeUrl("/ReservationMaintain/created"), requestParams, handler);
    }

    /**
     * 在线预约维修提交
     *
     * @param shopID
     * @param handler
     */
    public void postReservationMaintain(String plateNumber, String autoModel, String drivingDistance, String name, String phone, String shopID, String shopType, String apppintmentDate, String serviceType, String serviceAdviserID, String mechanicID, String VIN, String remark, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = paramsAddDeviceType(new RequestParams());
        requestParams.put("plateNumber", plateNumber);
        requestParams.put("autoModel", autoModel);
        requestParams.put("drivingDistance", drivingDistance);
        requestParams.put("name", name);
        requestParams.put("phone", phone);
        requestParams.put("shopID", shopID);
        requestParams.put("shopType", shopType);
        requestParams.put("apppintmentDate", apppintmentDate);
        requestParams.put("serviceType", serviceType);
        requestParams.put("serviceAdviserID", serviceAdviserID);
        requestParams.put("mechanicID", mechanicID);
        requestParams.put("VIN", VIN);
        requestParams.put("remark", remark);
        gPostUrl(completeUrl("/ReservationMaintain/created"), requestParams, handler);
    }

    /**
     * 预约维修详情
     *
     * @param orderID
     * @param handler
     */
    public void requestReservationMaintainInfo(String orderID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        gGetUrl(completeUrl("/Order/reservationMaintainDetail"), requestParams, handler);
    }

    /**
     * 服务顾问请求
     *
     * @param shopID
     * @param handler
     */
//    /ServiceAdviser
    public void requestServiceAdviser(String shopID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("shopID", shopID);
        gGetUrl(completeUrl("/ServiceAdviser"), requestParams, handler);
    }

    /**
     * 服务技师请求
     *
     * @param shopID
     * @param handler
     */
//    /Mechanic
    public void requestMechanic(String shopID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("shopID", shopID);
        gGetUrl(completeUrl("/Mechanic"), requestParams, handler);
    }


}
