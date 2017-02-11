package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 05
 * FIXME
 * Todo 车辆年检model
 */
public class AnnualInspection implements Parcelable {
    /**
     * orederID : 订单号 string
     * plateNumber : 车牌号 string
     * username : 联系人姓名 string
     * phone : 联系人电话 string
     * amount : 总额 number
     * drivingLicenseFile1 : 行驶证正面 string
     * drivingLicenseFile2 : 行驶证反面 string
     * orderStatus : 订单状态 -10订单取消 10待受理 20待付款 30已付款 40完成 number
     * orderStatusText : 订单状态- 10订单取消 10待受理 20待付款 30已付款 40完成 string
     * refundStatus : 退款状态 10-待退款 20-退款中 30-退款完成 不用判断订单状态 number
     * refundStatusText : 退款状态 10-待退款 20-退款中 30-退款完成 string
     * registrDate : 车辆注册时间 string
     * paymentID: 支付方式
     * exemption:	是否免检期间 boolean
     *  refundType: 退款方式 支付宝 微信 线下 string
     * refundNumber: 退款单号 string
     * orderCreateTime: 订单创建时间
     * tradeID : 交易单号 string
     * paidTime: 付款时间
     * province: 省
     * city: 市
     * district: 区
     * address: 详细地址
     */

    public String orederID;
    public String plateNumber;
    public String username;
    public String phone;
    public String amount;
    public String drivingLicenseFile1;
    public String drivingLicenseFile2;
    public String orderStatus;
    public String orderStatusText;
    public String refundStatus;
    public String refundStatusText;
    public String registrDate;
    public String paymentID;
    public String exemption;
    public String refundType;
    public String refundNumber;
    public String orderCreateTime;
    public String tradeID;
    public String paidTime;
    public String province;
    public String city;
    public String district;
    public String address;


    protected AnnualInspection(Parcel in) {
        orederID = in.readString();
        plateNumber = in.readString();
        username = in.readString();
        phone = in.readString();
        amount = in.readString();
        drivingLicenseFile1 = in.readString();
        drivingLicenseFile2 = in.readString();
        orderStatus = in.readString();
        orderStatusText = in.readString();
        refundStatus = in.readString();
        refundStatusText = in.readString();
        registrDate = in.readString();
        paymentID = in.readString();
        exemption = in.readString();
        refundType = in.readString();
        refundNumber = in.readString();
        orderCreateTime = in.readString();
        tradeID = in.readString();
        paidTime = in.readString();
    }

    public static final Creator<AnnualInspection> CREATOR = new Creator<AnnualInspection>() {
        @Override
        public AnnualInspection createFromParcel(Parcel in) {
            return new AnnualInspection(in);
        }

        @Override
        public AnnualInspection[] newArray(int size) {
            return new AnnualInspection[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orederID);
        dest.writeString(plateNumber);
        dest.writeString(username);
        dest.writeString(phone);
        dest.writeString(amount);
        dest.writeString(drivingLicenseFile1);
        dest.writeString(drivingLicenseFile2);
        dest.writeString(orderStatus);
        dest.writeString(orderStatusText);
        dest.writeString(refundStatus);
        dest.writeString(refundStatusText);
        dest.writeString(registrDate);
        dest.writeString(paymentID);
        dest.writeString(exemption);
        dest.writeString(refundType);
        dest.writeString(refundNumber);
        dest.writeString(orderCreateTime);
        dest.writeString(tradeID);
        dest.writeString(paidTime);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(district);
        dest.writeString(address);
    }
}
