package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * 说明:产品基本信息
 *
 * @author: 吕飞
 * @since: 2016-02-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class ProductInfo implements Parcelable {

    public static final Parcelable.Creator<ProductInfo> CREATOR = new Parcelable.Creator<ProductInfo>() {
        public ProductInfo createFromParcel(Parcel source) {
            return new ProductInfo(source);
        }

        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };
    /**
     * price : string,用品价格
     * smallPhoto : string,缩略图url
     * productNum : integer,商品数量
     * name : string,用品名称
     * productID : string,用品ID
     */
    @Expose
    public String price;
    @Expose
    public String smallPhoto;
    @Expose
    public String productNum;
    @Expose
    public String name;
    @Expose
    public String productID;
    @Expose
    public String model;
    public boolean isInPackage;
//    public boolean isFirstInShop;
    public boolean isFirstInPackage;
    public boolean isLastInPackage;
//    public boolean isLastInShop;
    public boolean isEdit;
    public boolean isSelected;
    public boolean isInvalid;
    public String shopName = "";
    public String shopID = "";
    public String packageNum = "";
    public String packageID = "";
    public String packageName = "";
    public String comboPrice = "0";

    public ProductInfo() {
    }

    //黄祎Test
    public ProductInfo(String price, String smallPhoto, String productNum, String name, String productID) {
        this.price = price;
        this.smallPhoto = smallPhoto;
        this.productNum = productNum;
        this.name = name;
        this.productID = productID;
    }

    protected ProductInfo(Parcel in) {
        this.price = in.readString();
        this.smallPhoto = in.readString();
        this.productNum = in.readString();
        this.name = in.readString();
        this.productID = in.readString();
    }

    public String getPackageNum() {
        return "套餐数量：" + packageNum;
    }

    public String getProductNum() {
        return "商品数量：" + productNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.price);
        dest.writeString(this.smallPhoto);
        dest.writeString(this.productNum);
        dest.writeString(this.name);
        dest.writeString(this.productID);
    }
}
