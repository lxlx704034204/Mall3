package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-12-02
 * FIXME  参考价明细
 * Todo
 */
public class PriceInfo implements Parcelable {

    public float itemPrice;//裸车价
    public float purchaseTax;//购置税
    public float travelTax;//车船税
//    public float commercialInsurance;//商业险
    public float compulsoryInsurance;//交强险
//    public float insuranceCoupon;//保险优惠
    public float plateFare;//上牌费

    /**
     * 获得总价
     * @return
     */
    public float getTotalPrice(){
        return itemPrice+purchaseTax+travelTax+compulsoryInsurance+plateFare;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.itemPrice);
        dest.writeFloat(this.purchaseTax);
        dest.writeFloat(this.travelTax);
        dest.writeFloat(this.compulsoryInsurance);
        dest.writeFloat(this.plateFare);
    }

    public PriceInfo() {
    }

    protected PriceInfo(Parcel in) {
        this.itemPrice = in.readFloat();
        this.purchaseTax = in.readFloat();
        this.travelTax = in.readFloat();
        this.compulsoryInsurance = in.readFloat();
        this.plateFare = in.readFloat();
    }

    public static final Parcelable.Creator< PriceInfo > CREATOR = new Parcelable.Creator< PriceInfo >() {
        public PriceInfo createFromParcel(Parcel source) {
            return new PriceInfo(source);
        }

        public PriceInfo[] newArray(int size) {
            return new PriceInfo[size];
        }
    };
}
