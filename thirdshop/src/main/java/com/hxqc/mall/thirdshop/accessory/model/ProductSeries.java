package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年03月04日
 */
public class ProductSeries implements Parcelable {
    public static final Creator< ProductSeries > CREATOR = new Creator< ProductSeries >() {
        @Override
        public ProductSeries createFromParcel(Parcel in) {
            return new ProductSeries(in);
        }


        @Override
        public ProductSeries[] newArray(int size) {
            return new ProductSeries[size];
        }
    };
    public String priceRange;
    public String smallPhoto;
    public String name;
    public String productSeriesID;

    public ProductSeries() {
    }

    public ProductSeries(String priceRange, String smallPhoto, String name, String productSeriesID) {
        this.priceRange = priceRange;
        this.smallPhoto = smallPhoto;
        this.name = name;
        this.productSeriesID = productSeriesID;
    }


    protected ProductSeries(Parcel in) {
        priceRange = in.readString();
        smallPhoto = in.readString();
        name = in.readString();
        productSeriesID = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(priceRange);
        dest.writeString(smallPhoto);
        dest.writeString(name);
        dest.writeString(productSeriesID);
    }
}
