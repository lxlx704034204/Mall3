package com.hxqc.mall.thirdshop.accessory4s.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * 说明:4s店用品基本信息
 *
 * @author: 吕飞
 * @since: 2016-08-04
 * Copyright:恒信汽车电子商务有限公司
 */
public class ProductInfo4S implements Parcelable {
    @Expose
    public String price;
    @Expose
    public String smallPhoto;
    @Expose
    public int productNum;
    @Expose
    public String name;
    @Expose
    public String productID;
    @Expose
    public String shopTitle;
    public boolean isInPackage;
    public boolean isFirstInShop;
    public boolean isFirstInPackage;
    public boolean isLastInPackage;
    public boolean isLastInShop;
    public boolean isEdit;
    public boolean isSelected;
    public boolean isInvalid;
    public String shopID = "";
    public int packageNum = 0;
    public String packageID = "";
    public String packageName = "";
    public String comboPrice = "0";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.price);
        dest.writeString(this.smallPhoto);
        dest.writeInt(this.productNum);
        dest.writeString(this.name);
        dest.writeString(this.productID);
        dest.writeString(this.shopTitle);
    }

    public ProductInfo4S() {
    }

    protected ProductInfo4S(Parcel in) {
        this.price = in.readString();
        this.smallPhoto = in.readString();
        this.productNum = in.readInt();
        this.name = in.readString();
        this.productID = in.readString();
        this.shopTitle = in.readString();
    }

    public static final Parcelable.Creator<ProductInfo4S> CREATOR = new Parcelable.Creator<ProductInfo4S>() {
        @Override
        public ProductInfo4S createFromParcel(Parcel source) {
            return new ProductInfo4S(source);
        }

        @Override
        public ProductInfo4S[] newArray(int size) {
            return new ProductInfo4S[size];
        }
    };
    public String getPackageNum() {
        return "套餐数量：" + packageNum;
    }

    public String getProductNum() {
        return "商品数量：" + productNum;
    }
}
