package com.hxqc.newenergy.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年03月21日
 */
public class PriceAndMileage implements Parcelable {
    public String label;
    public String filterKey;
    public String filterValue;


    protected PriceAndMileage(Parcel in) {
        label = in.readString();
        filterKey = in.readString();
        filterValue = in.readString();
    }


    public static final Creator< PriceAndMileage > CREATOR = new Creator< PriceAndMileage >() {
        @Override
        public PriceAndMileage createFromParcel(Parcel in) {
            return new PriceAndMileage(in);
        }


        @Override
        public PriceAndMileage[] newArray(int size) {
            return new PriceAndMileage[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(filterKey);
        dest.writeString(filterValue);
    }
}
