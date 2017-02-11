package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 09
 * FIXME
 * Todo （号牌）种类
 */
public class AutoSpeciesType implements Parcelable {

    public static final Creator<AutoSpeciesType> CREATOR = new Creator<AutoSpeciesType>() {
        @Override
        public AutoSpeciesType createFromParcel(Parcel in) {
            return new AutoSpeciesType(in);
        }

        @Override
        public AutoSpeciesType[] newArray(int size) {
            return new AutoSpeciesType[size];
        }
    };
    public String car;
    public String id;

    protected AutoSpeciesType(Parcel in) {
        car = in.readString();
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(car);
        dest.writeString(id);
    }
}
