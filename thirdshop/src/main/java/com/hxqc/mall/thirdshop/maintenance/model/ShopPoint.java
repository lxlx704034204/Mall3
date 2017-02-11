package com.hxqc.mall.thirdshop.maintenance.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.amap.inter.AmapDataModel;

/**
 * @Author : 钟学东
 * @Since : 2016-03-14
 * FIXME
 * Todo 店铺地址
 */
public class ShopPoint implements Parcelable, AmapDataModel {
    public String id; //店铺ID string
    public String shopName ;//店铺名称 string
    public String address; //地址 string
    public String tel; //电话 string
    public double latitude; //纬度 number
    public double longitude; //经度 number

    protected ShopPoint(Parcel in) {
        id = in.readString();
        shopName = in.readString();
        address = in.readString();
        tel = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }


    public static final Creator< ShopPoint > CREATOR = new Creator< ShopPoint >() {
        @Override
        public ShopPoint createFromParcel(Parcel in) {
            return new ShopPoint(in);
        }


        @Override
        public ShopPoint[] newArray(int size) {
            return new ShopPoint[size];
        }
    };


    @Override
    public String tipName() {
        return this.shopName;
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
            d = Double.parseDouble(latitude +"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    @Override
    public double tipLongitude() {
        double d = 0;
        try {
            d = Double.parseDouble(longitude + "");
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
        dest.writeString(id);
        dest.writeString(shopName);
        dest.writeString(address);
        dest.writeString(tel);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}

