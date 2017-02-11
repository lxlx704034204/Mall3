package com.hxqc.mall.thirdshop.accessory4s.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 22
 * FIXME
 * Todo 分期购车车系
 */

public class InstallmentBuyingSeries implements Parcelable {

//    itemID:    车辆ID string
//    brand:    品牌 string
//    series:    车系名称 string
//    extID:    车系ID string
//    modelName:    车辆名称 string
//    itemThumb:    车辆缩略图 string
//    shopPrice:    店铺参考价 number
//    shopPriceRange:    店铺参考价 number
//    itemOrigPrice:    厂家指导价 number
//    shopID:    所卖的店铺ID string
//    shopTitle:    店铺简称 string
//    itemPrice:    店铺参考价 number
//    modelCount:    车型个数 number
//    installment:    分期计算

    public String itemID;
    public String brand;
    public String series;
    public String extID;
    public String modelName;
    public String itemThumb;
    public String shopPrice;
    public String shopPriceRange;
    public String itemOrigPrice;
    public String shopID;
    public String shopTitle;
    public String itemPrice;
    public int modelCount;
    public Installment installment;

    protected InstallmentBuyingSeries(Parcel in) {
        itemID = in.readString();
        brand = in.readString();
        series = in.readString();
        extID = in.readString();
        modelName = in.readString();
        itemThumb = in.readString();
        shopPrice = in.readString();
        shopPriceRange = in.readString();
        itemOrigPrice = in.readString();
        shopID = in.readString();
        shopTitle = in.readString();
        itemPrice = in.readString();
        modelCount = in.readInt();
        installment = in.readParcelable(Installment.class.getClassLoader());
    }

    public static final Creator<InstallmentBuyingSeries> CREATOR = new Creator<InstallmentBuyingSeries>() {
        @Override
        public InstallmentBuyingSeries createFromParcel(Parcel in) {
            return new InstallmentBuyingSeries(in);
        }

        @Override
        public InstallmentBuyingSeries[] newArray(int size) {
            return new InstallmentBuyingSeries[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemID);
        dest.writeString(brand);
        dest.writeString(series);
        dest.writeString(extID);
        dest.writeString(modelName);
        dest.writeString(itemThumb);
        dest.writeString(shopPrice);
        dest.writeString(shopPriceRange);
        dest.writeString(itemOrigPrice);
        dest.writeString(shopID);
        dest.writeString(shopTitle);
        dest.writeString(itemPrice);
        dest.writeInt(modelCount);
        dest.writeParcelable(installment, flags);
    }
}
