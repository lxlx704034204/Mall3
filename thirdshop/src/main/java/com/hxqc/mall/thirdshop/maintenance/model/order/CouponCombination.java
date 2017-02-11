package com.hxqc.mall.thirdshop.maintenance.model.order;

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
    public static final Parcelable.Creator<CouponCombination> CREATOR = new Parcelable.Creator<CouponCombination>() {
        public CouponCombination createFromParcel(Parcel source) {
            return new CouponCombination(source);
        }

        public CouponCombination[] newArray(int size) {
            return new CouponCombination[size];
        }
    };
    public float discountAmount;
    public ArrayList<String> couponIDs;

    public CouponCombination() {
    }

    protected CouponCombination(Parcel in) {
        this.discountAmount = in.readFloat();
        this.couponIDs = in.createStringArrayList();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.discountAmount);
        dest.writeStringList(this.couponIDs);
    }
}
