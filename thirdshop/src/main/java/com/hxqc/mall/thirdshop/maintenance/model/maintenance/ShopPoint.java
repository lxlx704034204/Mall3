package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 21
 * FIXME
 * Todo
 */
public class ShopPoint implements Parcelable{

    public String id; //店铺ID string
    public String shopName; //店铺名称 string
    public String address; //地址 string
    public String tel; //电话 string
    public String latitude; //纬度 number
    public String longitude; //经度 number

    protected ShopPoint(Parcel in) {
        id = in.readString();
        shopName = in.readString();
        address = in.readString();
        tel = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public static final Creator<ShopPoint> CREATOR = new Creator<ShopPoint>() {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(shopName);
        dest.writeString(address);
        dest.writeString(tel);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}
