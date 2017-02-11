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
public class FourSPromotion implements Parcelable {
    public String promotionID;//促销ID string
    public String shopID;//店铺ID string

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.promotionID);
        dest.writeString(this.shopID);
    }

    public FourSPromotion() {
    }

    protected FourSPromotion(Parcel in) {
        this.promotionID = in.readString();
        this.shopID = in.readString();
    }

    public static final Parcelable.Creator<FourSPromotion> CREATOR = new Parcelable.Creator<FourSPromotion>() {
        @Override
        public FourSPromotion createFromParcel(Parcel source) {
            return new FourSPromotion(source);
        }

        @Override
        public FourSPromotion[] newArray(int size) {
            return new FourSPromotion[size];
        }
    };
}
