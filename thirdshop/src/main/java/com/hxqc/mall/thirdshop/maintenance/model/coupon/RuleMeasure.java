package com.hxqc.mall.thirdshop.maintenance.model.coupon;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:李烽
 * Date:2016-02-24
 * FIXME
 * Todo 满减
 */
public class RuleMeasure implements Parcelable{
    public static final Creator<RuleMeasure> CREATOR = new Creator<RuleMeasure>() {
        @Override
        public RuleMeasure createFromParcel(Parcel in) {
            return new RuleMeasure(in);
        }

        @Override
        public RuleMeasure[] newArray(int size) {
            return new RuleMeasure[size];
        }
    };
    public float condition;    //    条件金额
    public float price;//    优惠金额

    protected RuleMeasure(Parcel in) {
        condition = in.readFloat();
        price = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(condition);
        dest.writeFloat(price);
    }
}
