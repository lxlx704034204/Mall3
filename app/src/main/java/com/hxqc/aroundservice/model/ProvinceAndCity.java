package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 09
 * FIXME
 * Todo
 */
public class ProvinceAndCity implements Parcelable{

    public static final Creator<ProvinceAndCity> CREATOR = new Creator<ProvinceAndCity>() {
        @Override
        public ProvinceAndCity createFromParcel(Parcel in) {
            return new ProvinceAndCity(in);
        }

        @Override
        public ProvinceAndCity[] newArray(int size) {
            return new ProvinceAndCity[size];
        }
    };
    public String province;
    public String city;
    public String hphm;
    public String hpzl;
    public ArrayList<IllegalQueryResultInfo> lists;

    protected ProvinceAndCity(Parcel in) {
        province = in.readString();
        city = in.readString();
        hphm = in.readString();
        hpzl = in.readString();
        lists = in.createTypedArrayList(IllegalQueryResultInfo.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(hphm);
        dest.writeString(hpzl);
        dest.writeTypedList(lists);
    }

    @Override
    public String toString() {
        return "ProvinceAndCity{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", hphm='" + hphm + '\'' +
                ", hpzl='" + hpzl + '\'' +
                ", lists=" + lists +
                '}';
    }
}
