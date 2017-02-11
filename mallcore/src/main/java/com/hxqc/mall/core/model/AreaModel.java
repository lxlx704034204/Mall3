package com.hxqc.mall.core.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-03-26
 * FIXME
 * 省市区
 */
public class AreaModel implements Parcelable {

    public String title;
    public int areaID;
    public ArrayList< AreaModel > subAreas;


    public AreaModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.areaID);
        dest.writeTypedList(subAreas);
    }

    protected AreaModel(Parcel in) {
        this.title = in.readString();
        this.areaID = in.readInt();
        this.subAreas = in.createTypedArrayList(AreaModel.CREATOR);
    }

    public static final Creator< AreaModel > CREATOR = new Creator< AreaModel >() {
        public AreaModel createFromParcel(Parcel source) {
            return new AreaModel(source);
        }

        public AreaModel[] newArray(int size) {
            return new AreaModel[size];
        }
    };
}
