package com.hxqc.mall.thirdshop.accessory4s.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 22
 * FIXME
 * Todo 车系
 */

public class Series implements Parcelable {

//    brand:    品牌 string
//    seriesID:    车系ID string
//    seriesName:    车系名称 string
//    seriesThumb:    对应图标的URL string
//    priceRange:            4s店参考价 价格区间(格式：60000.00-70000.00) string
//    itemOrigPrice:    厂家指导价 价格区间(格式：60000.00-70000.00) string
//    modelCount:    车型个数 number

    public String brand;
    public String seriesID;
    public String seriesName;
    public String seriesThumb;
    public String priceRange;
    public String itemOrigPrice;
    public int modelCount;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.brand);
        dest.writeString(this.seriesID);
        dest.writeString(this.seriesName);
        dest.writeString(this.seriesThumb);
        dest.writeString(this.priceRange);
        dest.writeString(this.itemOrigPrice);
        dest.writeInt(this.modelCount);
    }

    public Series() {
    }

    protected Series(Parcel in) {
        this.brand = in.readString();
        this.seriesID = in.readString();
        this.seriesName = in.readString();
        this.seriesThumb = in.readString();
        this.priceRange = in.readString();
        this.itemOrigPrice = in.readString();
        this.modelCount = in.readInt();
    }

    public static final Parcelable.Creator<Series> CREATOR = new Parcelable.Creator<Series>() {
        @Override
        public Series createFromParcel(Parcel source) {
            return new Series(source);
        }

        @Override
        public Series[] newArray(int size) {
            return new Series[size];
        }
    };
}
