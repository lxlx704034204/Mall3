package com.hxqc.mall.amap.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 07
 * FIXME
 * Todo 品牌种类
 */
public class AutoType implements Parcelable{

    public static final Creator<AutoType> CREATOR = new Creator<AutoType>() {
        @Override
        public AutoType createFromParcel(Parcel in) {
            return new AutoType(in);
        }

        @Override
        public AutoType[] newArray(int size) {
            return new AutoType[size];
        }
    };
    public String car;//车辆类型
    public String id;//车辆类型编号

    protected AutoType(Parcel in) {
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
