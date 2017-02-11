package com.hxqc.mall.thirdshop.maintenance.model.order;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 07
 * FIXME
 * Todo
 */
public class OnlineOrderInfo {

    private String CarNumberCity;//车牌城市
    private String CarNumber;//车牌号
    private String VINId;//VIN码
    private String CarInfo;//车辆信息
    private String ContactName;//联系人姓名
    private String PhoneNumber;//手机号码
    private String ServiceStore;//服务门店
    private String ServiceType;//服务类型
    private String OrderDate;//预约时间
    private String OrderServiceCounselor;//预约服务顾问

    public String getCarNumberCity() {
        return CarNumberCity;
    }

    public void setCarNumberCity(String carNumberCity) {
        CarNumberCity = carNumberCity;
    }

    public String getCarNumber() {
        return CarNumber;
    }

    public void setCarNumber(String carNumber) {
        CarNumber = carNumber;
    }

    public String getVINId() {
        return VINId;
    }

    public void setVINId(String VINId) {
        this.VINId = VINId;
    }

    public String getCarInfo() {
        return CarInfo;
    }

    public void setCarInfo(String carInfo) {
        CarInfo = carInfo;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getServiceStore() {
        return ServiceStore;
    }

    public void setServiceStore(String serviceStore) {
        ServiceStore = serviceStore;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderServiceCounselor() {
        return OrderServiceCounselor;
    }

    public void setOrderServiceCounselor(String orderServiceCounselor) {
        OrderServiceCounselor = orderServiceCounselor;
    }
}
