package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.thirdshop.model.promotion.SalesPModel;

import java.util.ArrayList;

/**
 * Author :liukechong
 * Date : 2015-12-03
 * FIXME
 * Todo
 */
public class ShopSearchShop implements Parcelable {
    public String shopID;
    public String shopPhoto;
    public String brandThumb;
    public String shopTitle;
    public String distance;
    public ArrayList<SalesPModel> promotionList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopID);
        dest.writeString(this.shopPhoto);
        dest.writeString(this.brandThumb);
        dest.writeString(this.shopTitle);
        dest.writeString(this.distance);
        dest.writeTypedList(promotionList);
    }

    public ShopSearchShop() {
    }

    protected ShopSearchShop(Parcel in) {
        this.shopID = in.readString();
        this.shopPhoto = in.readString();
        this.brandThumb = in.readString();
        this.shopTitle = in.readString();
        this.distance = in.readString();
        this.promotionList = in.createTypedArrayList(SalesPModel.CREATOR);
    }

    public static final Parcelable.Creator<ShopSearchShop> CREATOR = new Parcelable.Creator<ShopSearchShop>() {
        @Override
        public ShopSearchShop createFromParcel(Parcel source) {
            return new ShopSearchShop(source);
        }

        @Override
        public ShopSearchShop[] newArray(int size) {
            return new ShopSearchShop[size];
        }
    };
}
