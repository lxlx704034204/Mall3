package com.hxqc.mall.thirdshop.maintenance.model.coupon;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:李烽
 * Date:2016-02-24
 * FIXME
 * Todo 优惠券抵扣金额
 */
public class RulePrice implements Parcelable {
    public static final Creator<RulePrice> CREATOR = new Creator<RulePrice>() {
        @Override
        public RulePrice createFromParcel(Parcel in) {
            return new RulePrice(in);
        }

        @Override
        public RulePrice[] newArray(int size) {
            return new RulePrice[size];
        }
    };
    public float price;//    金额

    protected RulePrice(Parcel in) {
        price = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(price);
    }
}
