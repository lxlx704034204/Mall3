package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-05-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class FourSShop extends ShopSearchShop{
    public String manageBrand;//经营品牌

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.manageBrand);
    }

    public FourSShop() {
    }

    protected FourSShop(Parcel in) {
        super(in);
        this.manageBrand = in.readString();
    }

    public static final Creator<FourSShop> CREATOR = new Creator<FourSShop>() {
        @Override
        public FourSShop createFromParcel(Parcel source) {
            return new FourSShop(source);
        }

        @Override
        public FourSShop[] newArray(int size) {
            return new FourSShop[size];
        }
    };
}
