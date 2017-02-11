package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 单个优惠套餐 -> 一组商品(同名商品 规格不一样) -> 单个商品
 * Created by huangyi on 16/2/23.
 */
public class SinglePackageProduct implements Parcelable {

    public static final Parcelable.Creator<SinglePackageProduct> CREATOR = new Parcelable.Creator<SinglePackageProduct>() {
        public SinglePackageProduct createFromParcel(Parcel in) {
            return new SinglePackageProduct(in);
        }

        public SinglePackageProduct[] newArray(int size) {
            return new SinglePackageProduct[size];
        }
    };

    public String title; //规格描述
    public ProductInfo productInfo; //商品信息
    public boolean isChecked = false; //当前是否选中
    public boolean isTempChecked = false; //当前是否临时选中 用于 优惠套餐列表Dialog弹窗ListView

    public SinglePackageProduct(String title, ProductInfo productInfo) {
        this.title = title;
        this.productInfo = productInfo;
    }

    private SinglePackageProduct(Parcel in) {
        //读取
        title = in.readString();
        productInfo = in.readParcelable(ProductInfo.class.getClassLoader());
        isChecked = in.readByte() != 0; //isChecked == true if byte != 0
        isTempChecked = in.readByte() != 0;
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
        out.writeByte((byte) (isChecked ? 1 : 0)); //if isChecked == true, byte == 1
        out.writeByte((byte) (isTempChecked ? 1 : 0));
    }

}
