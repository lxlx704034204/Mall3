package com.hxqc.mall.thirdshop.model.promotion;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.thirdshop.model.AutoDetailThirdShop;
import com.hxqc.mall.thirdshop.model.PriceInfo;

/**
 * Author: wanghao
 * Date: 2015-12-02
 * FIXME   促销车型
 * Todo
 */
public class SalesItem implements Parcelable {

    public AutoDetailThirdShop baseInfo;
    public PriceInfo priceInfo;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseInfo, 0);
        dest.writeParcelable(this.priceInfo, 0);
    }

    public SalesItem() {
    }

    protected SalesItem(Parcel in) {
        this.baseInfo = in.readParcelable(AutoDetailThirdShop.class.getClassLoader());
        this.priceInfo = in.readParcelable(PriceInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator< SalesItem > CREATOR = new Parcelable.Creator< SalesItem >() {
        public SalesItem createFromParcel(Parcel source) {
            return new SalesItem(source);
        }

        public SalesItem[] newArray(int size) {
            return new SalesItem[size];
        }
    };
}
