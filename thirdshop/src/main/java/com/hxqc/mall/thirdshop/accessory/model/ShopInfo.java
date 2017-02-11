package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.core.model.auto.PickupPointT;

/**
 * 说明:店铺信息
 *
 * @author: 吕飞
 * @since: 2016-02-29
 * Copyright:恒信汽车电子商务有限公司
 */
public class ShopInfo implements Parcelable {


    /**
     * shopID : string,店铺ID
     * shopLocation : {"address":"string,地址","tel":"string,电话","name":"string,位置名称","longitude":"string,经度","latitude":"string,维度"}
     */

    public String shopID;

    public PickupPointT shopLocation;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopID);
        dest.writeParcelable(this.shopLocation, 0);
    }

    public ShopInfo() {
    }

    protected ShopInfo(Parcel in) {
        this.shopID = in.readString();
        this.shopLocation = in.readParcelable(PickupPointT.class.getClassLoader());
    }

    public static final Parcelable.Creator<ShopInfo> CREATOR = new Parcelable.Creator<ShopInfo>() {
        public ShopInfo createFromParcel(Parcel source) {
            return new ShopInfo(source);
        }

        public ShopInfo[] newArray(int size) {
            return new ShopInfo[size];
        }
    };
}
