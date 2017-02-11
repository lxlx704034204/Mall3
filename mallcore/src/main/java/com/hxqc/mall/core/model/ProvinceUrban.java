package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-03-25
 * FIXME
 * 省市区
 */
public class ProvinceUrban implements Parcelable {

    //省
    public String province;
    //市
    public String city;
    //区
    public String subdivide;

    public ProvinceUrban() {
    }

    public ProvinceUrban(String province, String city, String subdivide) {
        this.province = province;
        this.city = city;
        this.subdivide = subdivide;
    }

    @Override
    public String toString() {
        return "ProvinceUrban{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", subdivide='" + subdivide + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.subdivide);

    }

    private ProvinceUrban(Parcel in) {
        this.province = in.readString();
        this.city = in.readString();
        this.subdivide = in.readString();
    }

    public static final Creator< ProvinceUrban > CREATOR = new Creator< ProvinceUrban >() {
        public ProvinceUrban createFromParcel(Parcel source) {
            return new ProvinceUrban(source);
        }

        public ProvinceUrban[] newArray(int size) {
            return new ProvinceUrban[size];
        }
    };

}
