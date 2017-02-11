package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-03-31
 * FIXME
 * 创建订单的 请求 model
 */
public class OrderPayRequest extends OrderRequest implements Parcelable {

    //购车人
    public String fullname;
    //联系方式
    public String cellphone;
    //证件号码
    public String identifier;
    //省
    public String province;
    //市
    public String city;
    //区
    public String district;
    //详细地址
    public String address;

    @Override
    public String toString() {
        return "OrderPayRequest{" +
                "fullname='" + fullname + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", identifier='" + identifier + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public OrderPayRequest() {
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void setIsLicense(boolean isLicense) {
        if (isLicense) {
            this.isLicense = "1";
            return;
        }
        this.isLicense = "0";
    }

    public boolean isLicense() {
        switch (isLicense) {
            case "1":
                return true;
            case "0":
                return false;
            default:
                return false;
        }
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

//    public void setInsuranceFirm(String insuranceFirm) {
//        this.insuranceFirm = insuranceFirm;
//    }
//
//    public String getInsuranceType() {
//        return insuranceType;
//    }
//
//    public void setInsuranceType(String insuranceType) {
//        this.insuranceType = insuranceType;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.fullname);
        dest.writeString(this.cellphone);
        dest.writeString(this.identifier);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.address);
    }

    protected OrderPayRequest(Parcel in) {
        super(in);
        this.fullname = in.readString();
        this.cellphone = in.readString();
        this.identifier = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.address = in.readString();
    }

    public static final Creator< OrderPayRequest > CREATOR = new Creator< OrderPayRequest >() {
        public OrderPayRequest createFromParcel(Parcel source) {
            return new OrderPayRequest(source);
        }

        public OrderPayRequest[] newArray(int size) {
            return new OrderPayRequest[size];
        }
    };
}
