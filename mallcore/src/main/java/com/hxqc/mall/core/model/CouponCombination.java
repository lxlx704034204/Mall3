package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-05
 * FIXME
 * Todo 	 可用优惠卷组合
 */
public class CouponCombination implements Parcelable {
    public static final Creator<CouponCombination> CREATOR = new Creator<CouponCombination>() {
        @Override
        public CouponCombination createFromParcel(Parcel in) {
            return new CouponCombination(in);
        }

        @Override
        public CouponCombination[] newArray(int size) {
            return new CouponCombination[size];
        }
    };
    public float discountAmount;
    public ArrayList<String> couponIDs;

    public CouponCombination() {
    }

    protected CouponCombination(Parcel in) {
        discountAmount = in.readFloat();
        couponIDs = in.createStringArrayList();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(discountAmount);
        dest.writeStringList(couponIDs);
    }
}
