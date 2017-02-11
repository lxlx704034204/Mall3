package com.hxqc.mall.thirdshop.maintenance.model.coupon;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-02-24
 * FIXME
 * Todo 优惠券使用规则
 */

public class CouponRule implements Parcelable{
    public static final Creator<CouponRule> CREATOR = new Creator<CouponRule>() {
        @Override
        public CouponRule createFromParcel(Parcel in) {
            return new CouponRule(in);
        }

        @Override
        public CouponRule[] newArray(int size) {
            return new CouponRule[size];
        }
    };
    public ArrayList<RuleShop> ruleShop;
    public ArrayList<RuleItem> ruleItem;
    public RuleMeasure ruleMeasure;
    public RulePrice rulePrice;

    protected CouponRule(Parcel in) {
        ruleShop = in.createTypedArrayList(RuleShop.CREATOR);
        ruleItem = in.createTypedArrayList(RuleItem.CREATOR);
        ruleMeasure = in.readParcelable(RuleMeasure.class.getClassLoader());
        rulePrice = in.readParcelable(RulePrice.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(ruleShop);
        dest.writeTypedList(ruleItem);
        dest.writeParcelable(ruleMeasure, flags);
        dest.writeParcelable(rulePrice, flags);
    }
}
