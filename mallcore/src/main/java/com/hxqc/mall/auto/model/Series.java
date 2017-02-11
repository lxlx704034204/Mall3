package com.hxqc.mall.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 17
 * FIXME
 * Todo 车系
 */
public class Series extends Filter implements Parcelable {

    @Expose
    public String seriesID;//    车系ID string
    @Expose
    public String seriesName;//    车系名称 string
    @Expose
    public String seriesThumb;//    对应图标的URL string
    @Expose
    public String priceRange;
    @Expose
    public String itemOrigPrice;

    public Series() {
    }

    protected Series(Parcel in) {
        seriesID = in.readString();
        seriesName = in.readString();
        seriesThumb = in.readString();
        itemOrigPrice = in.readString();
        priceRange = in.readString();
    }

    public String getLabel() {
        return seriesName;
    }

    public static final Creator<Series> CREATOR = new Creator<Series>() {
        @Override
        public Series createFromParcel(Parcel in) {
            return new Series(in);
        }

        @Override
        public Series[] newArray(int size) {
            return new Series[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(seriesID);
        dest.writeString(seriesName);
        dest.writeString(seriesThumb);
        dest.writeString(priceRange);
        dest.writeString(itemOrigPrice);
    }

    @Override
    public String toString() {
        return "Series{" +
                "seriesID='" + seriesID + '\'' +
                ", seriesName='" + seriesName + '\'' +
                ", seriesThumb='" + seriesThumb + '\'' +
                '}';
    }
}
