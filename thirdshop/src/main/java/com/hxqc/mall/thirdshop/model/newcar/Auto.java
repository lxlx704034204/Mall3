package com.hxqc.mall.thirdshop.model.newcar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 车辆信息
 * Created by huangyi on 16/10/24.
 */
public class Auto implements Parcelable {

    public static final Creator<Auto> CREATOR = new Creator<Auto>() {
        @Override
        public Auto createFromParcel(Parcel in) {
            return new Auto(in);
        }

        @Override
        public Auto[] newArray(int size) {
            return new Auto[size];
        }
    };

    public String brand; //品牌
    public String series; //车系
    public String model; //车型
    public String extID; //车型id
    public String photo; //车辆图片

    protected Auto(Parcel in) {
        brand = in.readString();
        series = in.readString();
        model = in.readString();
        extID = in.readString();
        photo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brand);
        dest.writeString(series);
        dest.writeString(model);
        dest.writeString(extID);
        dest.writeString(photo);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
