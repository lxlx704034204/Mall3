package com.hxqc.pay.model;

/**
 * Author: wanghao
 * Date: 2015-04-08
 * FIXME
 * 完善信息请求
 */
public class CompleteInfoModelRequest {

    //订单id
    public String orderID;
    //收货方式：1.自提点提车，2.送车上门
    public String expressType;
    //自提点id
    public String pickupPoint;
    public String province;
    public String city;
    public String district;
    public String address;
    public String cellphone;
    public String fullname;

    @Override
    public String toString() {
        return "CompleteInfoModelRequest{" +
                "orderID='" + orderID + '\'' +
                ", expressType='" + expressType + '\'' +
                ", pickupPoint='" + pickupPoint + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", fullname='" + fullname + '\'' +
                '}';
    }
}
