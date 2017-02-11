package com.hxqc.newenergy.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CPR193 on 2016/3/9.
 */
public class EVNewenergyAutoSample implements Parcelable{

    public   String itemID=null;  //商品id
    public  String itemName=null;//是商品名称
    public  String itemThumb=null;//商品图片
    public  float itemPrice=0;//商品价格
    public  float batteryLife=0;//续航里程
    public  float subsidyTotal=0;//补贴总额
    public  float itemTotalPrice=0;//指导价
    public  float reducedPrice=0;//直降价
    public  String series=null;//车系
    public  String brand=null;//品牌

    protected EVNewenergyAutoSample(Parcel in) {
        itemID = in.readString();
        itemName = in.readString();
        itemThumb = in.readString();
        itemPrice = in.readFloat();
        batteryLife = in.readFloat();
        subsidyTotal = in.readFloat();
        itemTotalPrice = in.readFloat();
        reducedPrice = in.readFloat();
        series = in.readString();
        brand = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemID);
        dest.writeString(itemName);
        dest.writeString(itemThumb);
        dest.writeFloat(itemPrice);
        dest.writeFloat(batteryLife);
        dest.writeFloat(subsidyTotal);
        dest.writeFloat(itemTotalPrice);
        dest.writeFloat(reducedPrice);
        dest.writeString(series);
        dest.writeString(brand);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EVNewenergyAutoSample> CREATOR = new Creator<EVNewenergyAutoSample>() {
        @Override
        public EVNewenergyAutoSample createFromParcel(Parcel in) {
            return new EVNewenergyAutoSample(in);
        }

        @Override
        public EVNewenergyAutoSample[] newArray(int size) {
            return new EVNewenergyAutoSample[size];
        }
    };
}
