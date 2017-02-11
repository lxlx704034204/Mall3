package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 单个优惠套餐 -> 一组商品(同名商品 规格不一样)
 * Created by huangyi on 16/2/23.
 */
public class SinglePackageProducts implements Parcelable {

    public static final Parcelable.Creator<SinglePackageProducts> CREATOR = new Parcelable.Creator<SinglePackageProducts>() {
        public SinglePackageProducts createFromParcel(Parcel in) {
            return new SinglePackageProducts(in);
        }

        public SinglePackageProducts[] newArray(int size) {
            return new SinglePackageProducts[size];
        }
    };

    public String title; //规格描述

    public ProductInfo productInfo; //商品信息
    public ArrayList<SinglePackageProduct> products;

    public SinglePackageProducts(String title, ProductInfo productInfo, ArrayList<SinglePackageProduct> products) {
        this.title = title;
        this.productInfo = productInfo;
        this.products = products;
    }

    private SinglePackageProducts(Parcel in) {
        //读取
        title = in.readString();
        productInfo = in.readParcelable(ProductInfo.class.getClassLoader());
        products = in.createTypedArrayList(SinglePackageProduct.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        //写入
        out.writeString(title);
        out.writeParcelable(productInfo, flags);
        out.writeTypedList(products);
    }

}
