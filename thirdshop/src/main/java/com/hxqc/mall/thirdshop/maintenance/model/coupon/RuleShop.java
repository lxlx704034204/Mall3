package com.hxqc.mall.thirdshop.maintenance.model.coupon;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:李烽
 * Date:2016-02-24
 * FIXME
 * Todo 可用店铺
 */
public class RuleShop implements Parcelable{
    public static final Creator<RuleShop> CREATOR = new Creator<RuleShop>() {
        @Override
        public RuleShop createFromParcel(Parcel in) {
            return new RuleShop(in);
        }

        @Override
        public RuleShop[] newArray(int size) {
            return new RuleShop[size];
        }
    };
    public String shopName;
    public String shopID;

    protected RuleShop(Parcel in) {
        shopName = in.readString();
        shopID = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopName);
        dest.writeString(shopID);
    }
}
