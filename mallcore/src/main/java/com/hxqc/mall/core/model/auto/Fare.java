package com.hxqc.mall.core.model.auto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: 胡俊杰
 * Date: 2015-08-07
 * FIXME
 * Todo
 */
public class Fare implements Parcelable {
    //交强险
    float insurance;
    //上牌
    float plateTax;
    //购置税
    float purchaseTax;
    //车船税
    float travelTax;

    public float getInsurance() {
        return insurance;
    }

    public float getPlateTax() {
        return plateTax;
    }

    public float getPurchaseTax() {
        return purchaseTax;
    }

    public float getTravelTax() {
        return travelTax;
    }

    /**
     * 总费用
     * @return
     */
    public float getAllCost(){
//        return insurance+plateTax+purchaseTax+travelTax;
        return plateTax;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.insurance);
        dest.writeFloat(this.plateTax);
        dest.writeFloat(this.purchaseTax);
        dest.writeFloat(this.travelTax);
    }

    public Fare() {
    }

    protected Fare(Parcel in) {
        this.insurance = in.readFloat();
        this.plateTax = in.readFloat();
        this.purchaseTax = in.readFloat();
        this.travelTax = in.readFloat();
    }

    public static final Parcelable.Creator< Fare > CREATOR = new Parcelable.Creator< Fare >() {
        public Fare createFromParcel(Parcel source) {
            return new Fare(source);
        }

        public Fare[] newArray(int size) {
            return new Fare[size];
        }
    };
}
