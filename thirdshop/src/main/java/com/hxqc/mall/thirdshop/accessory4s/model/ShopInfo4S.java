package com.hxqc.mall.thirdshop.accessory4s.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * 说明:店铺信息
 *
 * @author: 吕飞
 * @since: 2016-02-29
 * Copyright:恒信汽车电子商务有限公司
 */
public class ShopInfo4S implements Parcelable {
    @Expose
    public String shopID;
    @Expose
    public String address;
    @Expose
    public String shopTitle;
    @Expose
    public String tel;
    @Expose
    public String longitude;
    @Expose
    public String latitude;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopID);
        dest.writeString(this.address);
        dest.writeString(this.shopTitle);
        dest.writeString(this.tel);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
    }

    public ShopInfo4S() {
    }

    protected ShopInfo4S(Parcel in) {
        this.shopID = in.readString();
        this.address = in.readString();
        this.shopTitle = in.readString();
        this.tel = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
    }

    public static final Parcelable.Creator<ShopInfo4S> CREATOR = new Parcelable.Creator<ShopInfo4S>() {
        @Override
        public ShopInfo4S createFromParcel(Parcel source) {
            return new ShopInfo4S(source);
        }

        @Override
        public ShopInfo4S[] newArray(int size) {
            return new ShopInfo4S[size];
        }
    };
}
