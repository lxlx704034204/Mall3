package com.hxqc.mall.thirdshop.maintenance.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 18
 * FIXME
 * Todo 车辆model
 */
@Deprecated
public class Auto implements Parcelable {

    public String brandID;    //品牌ID
    public String brandName;    //品牌名称
    public String brandThumb;    //对应图标的URL
    public String seriesID;//    车系ID string
    public String seriesName;//    车系名称 string
    public String seriesThumb;//    对应图标的URL string
    public String autoModel;    //车型名称 string
    public String autoModelID;
    public String autoModelThumb;

    public Auto(String brandID, String brandName, String seriesID, String seriesName, String autoModel, String autoModelID) {
        this.brandID = brandID;
        this.brandName = brandName;
        this.seriesID = seriesID;
        this.seriesName = seriesName;
        this.autoModel = autoModel;
        this.autoModelID = autoModelID;
    }

    protected Auto(Parcel in) {
        brandID = in.readString();
        brandName = in.readString();
        brandThumb = in.readString();
        seriesID = in.readString();
        seriesName = in.readString();
        seriesThumb = in.readString();
        autoModel = in.readString();
        autoModelID = in.readString();
        autoModelThumb = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brandID);
        dest.writeString(brandName);
        dest.writeString(brandThumb);
        dest.writeString(seriesID);
        dest.writeString(seriesName);
        dest.writeString(seriesThumb);
        dest.writeString(autoModel);
        dest.writeString(autoModelID);
        dest.writeString(autoModelThumb);
    }
}
