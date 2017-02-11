package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function: 用品信息bean
 *
 * @author 袁秉勇
 * @since 2016年02月29日
 */
public class AccessoryInfo implements Parcelable {
    public static final Creator< AccessoryInfo > CREATOR = new Creator< AccessoryInfo >() {
        @Override
        public AccessoryInfo createFromParcel(Parcel in) {
            return new AccessoryInfo(in);
        }


        @Override
        public AccessoryInfo[] newArray(int size) {
            return new AccessoryInfo[size];
        }
    };
    public String productBrandName;
    public String productBrandID;
    public ProductSeries productSeries;

    public AccessoryInfo() {
    }

    public AccessoryInfo(String productBrandName, String productBrandID, ProductSeries productSeries) {
        this.productBrandName = productBrandName;
        this.productBrandID = productBrandID;
        this.productSeries = productSeries;
    }


    protected AccessoryInfo(Parcel in) {
        productBrandName = in.readString();
        productBrandID = in.readString();
        productSeries = in.readParcelable(ProductSeries.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productBrandName);
        dest.writeString(productBrandID);
        dest.writeParcelable(productSeries, flags);
    }
}
