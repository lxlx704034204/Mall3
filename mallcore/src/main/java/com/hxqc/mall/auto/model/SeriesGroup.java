package com.hxqc.mall.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 17
 * FIXME
 * Todo 车系组
 */
public class SeriesGroup implements Parcelable {

    @Expose
    public String brandID;    //品牌ID
    @Expose
    public String brandName; //品牌名称
    @Expose
    public ArrayList<Series> series;

    public SeriesGroup() {
    }

    protected SeriesGroup(Parcel in) {
        brandID = in.readString();
        brandName = in.readString();
        series = in.createTypedArrayList(Series.CREATOR);
    }

    public static final Creator<SeriesGroup> CREATOR = new Creator<SeriesGroup>() {
        @Override
        public SeriesGroup createFromParcel(Parcel in) {
            return new SeriesGroup(in);
        }

        @Override
        public SeriesGroup[] newArray(int size) {
            return new SeriesGroup[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brandID);
        dest.writeString(brandName);
        dest.writeTypedList(series);
    }
}
