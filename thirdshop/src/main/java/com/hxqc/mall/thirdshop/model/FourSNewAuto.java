package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-05-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class FourSNewAuto implements Parcelable {
    public String  itemID;//车辆ID string
    public String shopID;//店铺ID string

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemID);
        dest.writeString(this.shopID);
    }

    public FourSNewAuto() {
    }

    protected FourSNewAuto(Parcel in) {
        this.itemID = in.readString();
        this.shopID = in.readString();
    }

    public static final Parcelable.Creator<FourSNewAuto> CREATOR = new Parcelable.Creator<FourSNewAuto>() {
        @Override
        public FourSNewAuto createFromParcel(Parcel source) {
            return new FourSNewAuto(source);
        }

        @Override
        public FourSNewAuto[] newArray(int size) {
            return new FourSNewAuto[size];
        }
    };
}
