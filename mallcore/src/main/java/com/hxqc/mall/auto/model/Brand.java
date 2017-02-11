package com.hxqc.mall.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 17
 * FIXME
 * Todo 品牌
 */
public class Brand implements Parcelable {

    @Expose
    public String brandID;    //品牌ID
    @Expose
    public String brandName;    //品牌名称
    @Expose
    public String brandThumb;    //对应图标的URL

    protected Brand(Parcel in) {
        brandID = in.readString();
        brandName = in.readString();
        brandThumb = in.readString();
    }

    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
        @Override
        public Brand createFromParcel(Parcel in) {
            return new Brand(in);
        }

        @Override
        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.brandID);
        dest.writeString(this.brandName);
        dest.writeString(this.brandThumb);
    }

    @Override
    public String toString() {
        return "Brand{" +
                "brandID='" + brandID + '\'' +
                ", brandName='" + brandName + '\'' +
                ", brandThumb='" + brandThumb + '\'' +
                '}';
    }
}
