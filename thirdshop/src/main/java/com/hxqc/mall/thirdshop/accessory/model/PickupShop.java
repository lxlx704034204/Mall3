package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.thirdshop.maintenance.model.Payment;

import java.util.ArrayList;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-05-30
 * Copyright:恒信汽车电子商务有限公司
 */
public class PickupShop implements Parcelable {
    public String used;//是否为使用过的店铺，是：1，否：0 string
    public String shopID;//店铺ID string
    public String shopPhoto;//店铺照片（URL） string
    public String shopTel;// 店铺电话 string
    public String shopTitle;//店铺简称（城市+简称+品牌） string
    public String shopName;//店铺全称 string
    public double distance;//与自己的距离 (单位：米) number
    public String address;//地址 string
    public float latitude;//纬度 number
    public float longitude;// 经度 number
    public float score;//评分 string
    public ArrayList<Payment> paymentID;//到店付支持的支付方式[]
    public ArrayList<LaborCost> laborCost;//工时费用[]
    public String queryShop;//企业信息查询链接 string

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.used);
        dest.writeString(this.shopID);
        dest.writeString(this.shopPhoto);
        dest.writeString(this.shopTel);
        dest.writeString(this.shopTitle);
        dest.writeString(this.shopName);
        dest.writeDouble(this.distance);
        dest.writeString(this.address);
        dest.writeFloat(this.latitude);
        dest.writeFloat(this.longitude);
        dest.writeFloat(this.score);
        dest.writeList(this.paymentID);
        dest.writeString(this.queryShop);
    }

    public PickupShop() {
    }

    protected PickupShop(Parcel in) {
        this.used = in.readString();
        this.shopID = in.readString();
        this.shopPhoto = in.readString();
        this.shopTel = in.readString();
        this.shopTitle = in.readString();
        this.shopName = in.readString();
        this.distance = in.readFloat();
        this.address = in.readString();
        this.latitude = in.readFloat();
        this.longitude = in.readFloat();
        this.score = in.readFloat();
        this.paymentID = new ArrayList<>();
        in.readList(this.paymentID, Payment.class.getClassLoader());
        this.queryShop = in.readString();
    }

    public static final Parcelable.Creator<PickupShop> CREATOR = new Parcelable.Creator<PickupShop>() {
        @Override
        public PickupShop createFromParcel(Parcel source) {
            return new PickupShop(source);
        }

        @Override
        public PickupShop[] newArray(int size) {
            return new PickupShop[size];
        }
    };
}
