package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function: 网上4S店报价列表bean
 *
 * @author 袁秉勇
 * @since 2016年02月29日
 */
public class AccessoryShop implements Parcelable {
    public static final Creator< AccessoryShop > CREATOR = new Creator< AccessoryShop >() {
        @Override
        public AccessoryShop createFromParcel(Parcel in) {
            return new AccessoryShop(in);
        }


        @Override
        public AccessoryShop[] newArray(int size) {
            return new AccessoryShop[size];
        }
    };
    public String distance;
    public String shopPhoto;
    public String priceRange;
    public String shopID;
    public String shopTitle;
    public String brandThumb;
    public String shopPhone;


    public AccessoryShop() {
    }


    protected AccessoryShop(Parcel in) {
        distance = in.readString();
        shopPhoto = in.readString();
        priceRange = in.readString();
        shopID = in.readString();
        shopTitle = in.readString();
        brandThumb = in.readString();
        shopPhone = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(distance);
        dest.writeString(shopPhoto);
        dest.writeString(priceRange);
        dest.writeString(shopID);
        dest.writeString(shopTitle);
        dest.writeString(brandThumb);
        dest.writeString(shopPhone);
    }
}
