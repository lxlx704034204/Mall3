package com.hxqc.fastreqair.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Function: 洗车店铺列表实体Bean
 *
 * @author 袁秉勇
 * @since 2016年05月18日
 */
public class CarWashShopListBean implements Parcelable {
    public String shopID;
    public boolean used;
    public ArrayList< String > shopPhoto;
    public String shopTel;
    public String brandThumb;
    public String shopTitle;
    public String shopName;
    public double distance;
    public String address;
    public double latitude;
    public double longitude;
    public String evaluate;
    public String amount;
    public String shopHours;
    public ArrayList<ChargeItem> charge;
    public float star;
    public long orderCount;
    public long workstatus;

    public CarWashShopListBean(){}


    protected CarWashShopListBean(Parcel in) {
        shopID = in.readString();
        used = in.readByte() != 0;
        shopPhoto = in.createStringArrayList();
        shopTel = in.readString();
        brandThumb = in.readString();
        shopTitle = in.readString();
        shopName = in.readString();
        distance = in.readDouble();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        evaluate = in.readString();
        amount = in.readString();
        shopHours = in.readString();
        charge = in.createTypedArrayList(ChargeItem.CREATOR);
        star = in.readFloat();
        orderCount = in.readLong();
        workstatus = in.readLong();
    }


    public static final Creator< CarWashShopListBean > CREATOR = new Creator< CarWashShopListBean >() {
        @Override
        public CarWashShopListBean createFromParcel(Parcel in) {
            return new CarWashShopListBean(in);
        }


        @Override
        public CarWashShopListBean[] newArray(int size) {
            return new CarWashShopListBean[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopID);
        dest.writeByte((byte) (used ? 1 : 0));
        dest.writeStringList(shopPhoto);
        dest.writeString(shopTel);
        dest.writeString(brandThumb);
        dest.writeString(shopTitle);
        dest.writeString(shopName);
        dest.writeDouble(distance);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(evaluate);
        dest.writeString(amount);
        dest.writeString(shopHours);
        dest.writeTypedList(charge);
        dest.writeFloat(star);
        dest.writeLong(orderCount);
        dest.writeLong(workstatus);
    }
}