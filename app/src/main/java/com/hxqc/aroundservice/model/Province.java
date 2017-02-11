package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 09
 * FIXME
 * Todo 省份
 */
public class Province implements Parcelable{

    public static final Creator<Province> CREATOR = new Creator<Province>() {
        @Override
        public Province createFromParcel(Parcel in) {
            return new Province(in);
        }

        @Override
        public Province[] newArray(int size) {
            return new Province[size];
        }
    };
    public String province_code;
    public String province;
    public ArrayList<City> citys;//城市组

    protected Province(Parcel in) {
        province_code = in.readString();
        province = in.readString();
        citys = in.createTypedArrayList(City.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(province_code);
        dest.writeString(province);
        dest.writeTypedList(citys);
    }
}
