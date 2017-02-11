package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 05
 * FIXME
 * Todo
 */
public class Licence implements Parcelable {

    /**
     * orederID : 订单号 string
     * username : 联系人姓名 string
     * phone : 联系人电话 string
     * serviceCharge : 服务费 number
     * paymentID : 付款方式 string
     * drivingLicenseFile1 : 行驶证正面 string
     * drivingLicenseFile2 : 行驶证反面 string
     * orderStatus : 订单状态 -10订单取消 10待受理 20待付款 30已付款 40完成 number
     * orderStatusText : 订单状态- 10订单取消 10待受理 20待付款 30已付款 40完成 string
     * refundStatus : 退款状态 10-待退款 20-退款中 30-退款完成 不用判断订单状态 number
     * refundStatusText : 退款状态 10-待退款 20-退款中 30-退款完成 string
     * refundType: 退款方式
     * refundNumber: 退款单号
     * orderCreateTime: 订单创建时间
     * tradeID : 交易单号 string
     * paidTime: 付款时间
     * IDCardFile1: 身份证正面 string
     * IDCardFile2: 身份证反面 string
     *  province: 省
     * city: 市
     * district: 区
     * address: 详细地址
     */

    public String orederID;
    public String username;
    public String phone;
    public String serviceCharge;
    public String paymentID;
    public String drivingLicenseFile1;
    public String drivingLicenseFile2;
    public String orderStatus;
    public String orderStatusText;
    public String refundStatus;
    public String refundStatusText;
    public String refundType;
    public String refundNumber;
    public String orderCreateTime;
    public String tradeID;
    public String paidTime;
    public String IDCardFile1;
    public String IDCardFile2;
    public String province;
    public String city;
    public String district;
    public String address;

    public Licence() {
    }

    protected Licence(Parcel in) {
        orederID = in.readString();
        username = in.readString();
        phone = in.readString();
        serviceCharge = in.readString();
        paymentID = in.readString();
        drivingLicenseFile1 = in.readString();
        drivingLicenseFile2 = in.readString();
        orderStatus = in.readString();
        orderStatusText = in.readString();
        refundStatus = in.readString();
        refundStatusText = in.readString();
        refundType = in.readString();
        refundNumber = in.readString();
        orderCreateTime = in.readString();
        tradeID = in.readString();
        paidTime = in.readString();
        IDCardFile1 = in.readString();
        IDCardFile2 = in.readString();
        province = in.readString();
        city = in.readString();
        district = in.readString();
        address = in.readString();
    }

    public static final Creator<Licence> CREATOR = new Creator<Licence>() {
        @Override
        public Licence createFromParcel(Parcel in) {
            return new Licence(in);
        }

        @Override
        public Licence[] newArray(int size) {
            return new Licence[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orederID);
        dest.writeString(username);
        dest.writeString(phone);
        dest.writeString(serviceCharge);
        dest.writeString(paymentID);
        dest.writeString(drivingLicenseFile1);
        dest.writeString(drivingLicenseFile2);
        dest.writeString(orderStatus);
        dest.writeString(orderStatusText);
        dest.writeString(refundStatus);
        dest.writeString(refundStatusText);
        dest.writeString(refundType);
        dest.writeString(refundNumber);
        dest.writeString(orderCreateTime);
        dest.writeString(tradeID);
        dest.writeString(paidTime);
        dest.writeString(IDCardFile1);
        dest.writeString(IDCardFile2);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(district);
        dest.writeString(address);
    }
}
