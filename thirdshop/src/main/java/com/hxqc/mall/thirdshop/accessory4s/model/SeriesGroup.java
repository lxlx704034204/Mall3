package com.hxqc.mall.thirdshop.accessory4s.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 22
 * FIXME
 * Todo 车系组
 */

public class SeriesGroup implements Parcelable {

    public String brand;
    public ArrayList<Series> series;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.brand);
        dest.writeTypedList(this.series);
    }

    public SeriesGroup() {
    }

    protected SeriesGroup(Parcel in) {
        this.brand = in.readString();
        this.series = in.createTypedArrayList(Series.CREATOR);
    }

    public static final Parcelable.Creator<SeriesGroup> CREATOR = new Parcelable.Creator<SeriesGroup>() {
        @Override
        public SeriesGroup createFromParcel(Parcel source) {
            return new SeriesGroup(source);
        }

        @Override
        public SeriesGroup[] newArray(int size) {
            return new SeriesGroup[size];
        }
    };
}
