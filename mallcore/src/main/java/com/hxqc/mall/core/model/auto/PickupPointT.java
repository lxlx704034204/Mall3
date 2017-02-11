package com.hxqc.mall.core.model.auto;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.amap.inter.AmapDataModel;

/**
 * 说明:自提点
 * <p/>
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class PickupPointT implements Parcelable,AmapDataModel {

    public static final Creator<PickupPointT> CREATOR = new Creator<PickupPointT>() {
        public PickupPointT createFromParcel(Parcel source) {
            return new PickupPointT(source);
        }

        public PickupPointT[] newArray(int size) {
            return new PickupPointT[size];
        }
    };
    public String address;//地址
    public String id;//自提点ID
    public String latitude;//纬度
    public String longitude;//经度
    public String name;//自提点名称
    public String shopTitle;//
    public String tel;//电话
    public double distance;//距离

    public PickupPointT(String address, String latitude, String longitude, String tel) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tel = tel;
    }

    public PickupPointT() {
    }

    private PickupPointT(Parcel in) {
        this.address = in.readString();
        this.id = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.name = in.readString();
        this.shopTitle = in.readString();
        this.tel = in.readString();
        this.distance = in.readDouble();
    }

    @Override
    public String tipName() {
        return this.name;
    }

    @Override
    public String tipAddress() {
        return this.address;
    }

    @Override
    public String tipPhoneNumber() {
        return this.tel;
    }

    @Override
    public String tipID() {
        return this.id;
    }

    @Override
    public double tipLatitude() {
        double d = 0;
        try {
            d = Double.parseDouble(latitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    @Override
    public double tipLongitude() {
        double d = 0;
        try {
            d = Double.parseDouble(longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    @Override
    public String toString() {
        return "PickupPointT{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", distance=" + distance +
                '}';
    }

    public double getLatitude() {
        double d = 0;
        try {
            d = Double.parseDouble(latitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public double getLongitude() {
        double d = 0;
        try {
            d = Double.parseDouble(longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.id);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.name);
        dest.writeString(this.shopTitle);
        dest.writeString(this.tel);
        dest.writeDouble(this.distance);
    }

}
