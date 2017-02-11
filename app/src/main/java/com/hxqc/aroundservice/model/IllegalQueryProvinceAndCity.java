package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 09
 * FIXME
 * Todo 省份城市查询
 */
public class IllegalQueryProvinceAndCity implements Parcelable{

    public static final Creator<IllegalQueryProvinceAndCity> CREATOR = new Creator<IllegalQueryProvinceAndCity>() {
        @Override
        public IllegalQueryProvinceAndCity createFromParcel(Parcel in) {
            return new IllegalQueryProvinceAndCity(in);
        }

        @Override
        public IllegalQueryProvinceAndCity[] newArray(int size) {
            return new IllegalQueryProvinceAndCity[size];
        }
    };
    public String error_code;
    public String reason;
//    public Province result;
    public ArrayList<Province> result;

    protected IllegalQueryProvinceAndCity(Parcel in) {
        error_code = in.readString();
        reason = in.readString();
        result = in.createTypedArrayList(Province.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(error_code);
        dest.writeString(reason);
        dest.writeTypedList(result);
    }

    @Override
    public String toString() {
        return "IllegalQueryProvinceAndCity{" +
                "error_code='" + error_code + '\'' +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
