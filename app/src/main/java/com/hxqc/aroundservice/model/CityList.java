package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年04月13日
 */
public class CityList implements Parcelable {
    public static final Creator< CityList > CREATOR = new Creator< CityList >() {
        @Override
        public CityList createFromParcel(Parcel in) {
            return new CityList(in);
        }


        @Override
        public CityList[] newArray(int size) {
            return new CityList[size];
        }
    };
    public String cityid;
    public String cityname;


    protected CityList(Parcel in) {
        cityid = in.readString();
        cityname = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityid);
        dest.writeString(cityname);
    }
}
