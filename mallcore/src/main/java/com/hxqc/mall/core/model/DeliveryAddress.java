package com.hxqc.mall.core.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Author: wanghao
 * Date: 2015-03-27
 * FIXME
 * 收货地址
 */
public class DeliveryAddress implements Parcelable {
    public DeliveryAddress() {
    }

    @Expose
    public String provinceID;
    @Expose
    public String cityID;
    @Expose
    public String districtID;
    @Expose
    public String phone;

    @Expose
    public String province;
    //id
    @Expose
    public String addressID;
    //md5编码
    @Expose
    public String addressMD5;

    @Expose
    public String city;

    @Expose
    public String consignee;

    @Expose
    public String detailedAddress;

    @Expose
    public String district;
    //是否默认收货地址 1是 0否
    @Expose
    public int isDefault;

    //获取省市区文本
    public String getAddress() {
        return province + "&nbsp;&nbsp;&nbsp;&nbsp;" + city + "&nbsp;&nbsp;&nbsp;&nbsp;" + district;
    }

    public String getPhone() {
        return phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7);
    }

    @Override
    public String toString() {
        return "DeliveryAddress{" +
                "provinceID='" + provinceID + '\'' +
                ", cityID='" + cityID + '\'' +
                ", districtID='" + districtID + '\'' +
                ", phone='" + phone + '\'' +
                ", province='" + province + '\'' +
                ", addressID='" + addressID + '\'' +
                ", addressMD5='" + addressMD5 + '\'' +
                ", city='" + city + '\'' +
                ", consignee='" + consignee + '\'' +
                ", detailedAddress='" + detailedAddress + '\'' +
                ", district='" + district + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.provinceID);
        dest.writeString(this.cityID);
        dest.writeString(this.districtID);
        dest.writeString(this.phone);
        dest.writeString(this.province);
        dest.writeString(this.addressID);
        dest.writeString(this.addressMD5);
        dest.writeString(this.city);
        dest.writeString(this.consignee);
        dest.writeString(this.detailedAddress);
        dest.writeString(this.district);
        dest.writeInt(this.isDefault);
    }

    private DeliveryAddress(Parcel in) {
        this.provinceID = in.readString();
        this.cityID = in.readString();
        this.districtID = in.readString();
        this.phone = in.readString();
        this.province = in.readString();
        this.addressID = in.readString();
        this.addressMD5 = in.readString();
        this.city = in.readString();
        this.consignee = in.readString();
        this.detailedAddress = in.readString();
        this.district = in.readString();
        this.isDefault = in.readInt();
    }

    public static final Creator<DeliveryAddress> CREATOR = new Creator<DeliveryAddress>() {
        public DeliveryAddress createFromParcel(Parcel source) {
            return new DeliveryAddress(source);
        }

        public DeliveryAddress[] newArray(int size) {
            return new DeliveryAddress[size];
        }
    };
}
