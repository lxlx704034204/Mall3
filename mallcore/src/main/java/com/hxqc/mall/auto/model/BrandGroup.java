package com.hxqc.mall.auto.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 17
 * FIXME
 * Todo 品牌组
 */
public class BrandGroup implements Parcelable {

    @Expose
    public String groupTag; //品牌名称
    @Expose
    public ArrayList<Brand> group;

    protected BrandGroup(Parcel in) {
        groupTag = in.readString();
        group = in.createTypedArrayList(Brand.CREATOR);
    }

    public static final Creator<BrandGroup> CREATOR = new Creator<BrandGroup>() {
        @Override
        public BrandGroup createFromParcel(Parcel in) {
            return new BrandGroup(in);
        }

        @Override
        public BrandGroup[] newArray(int size) {
            return new BrandGroup[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupTag);
        dest.writeTypedList(group);
    }
}
