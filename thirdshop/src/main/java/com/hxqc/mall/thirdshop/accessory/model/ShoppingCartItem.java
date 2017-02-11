package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:购物车中的一栏
 *
 * @author: 吕飞
 * @since: 2016-02-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class ShoppingCartItem implements Parcelable {
    public static final Parcelable.Creator<ShoppingCartItem> CREATOR = new Parcelable.Creator<ShoppingCartItem>() {
        public ShoppingCartItem createFromParcel(Parcel source) {
            return new ShoppingCartItem(source);
        }

        public ShoppingCartItem[] newArray(int size) {
            return new ShoppingCartItem[size];
        }
    };
    /**
     * isPackage : integer,是否是套餐，是：1，否：0
     * packageInfo : {"packageNum":"integer,套餐数量","comboPrice":"string,套餐价格","productList":[{"price":"string,用品价格","smallPhoto":"string,缩略图url","productNum":"integer,商品数量","name":"string,用品名称","productID":"string,用品ID"}],"packageID":"string,套餐ID"}
     * productInfo : {"price":"string,用品价格","smallPhoto":"string,缩略图url","productNum":"integer,商品数量","name":"string,用品名称","productID":"string,用品ID"}
     */
    @Expose
    public String isPackage;
    @Expose
    public PackageInfo packageInfo;
    @Expose
    public ProductInfo productInfo;

    public ShoppingCartItem() {
    }

    protected ShoppingCartItem(Parcel in) {
        this.isPackage = in.readString();
        this.packageInfo = in.readParcelable(PackageInfo.class.getClassLoader());
        this.productInfo = in.readParcelable(ProductInfo.class.getClassLoader());
    }

    public ArrayList<ProductInfo> getProductInfos() {
        ArrayList<ProductInfo> productInfos = new ArrayList<>();
        if (isPackage.equals("1")) {
            for (int i = 0; i < packageInfo.productList.size(); i++) {
                packageInfo.productList.get(i).isInPackage = true;
                packageInfo.productList.get(i).packageNum = packageInfo.packageNum;
                packageInfo.productList.get(i).packageID = packageInfo.packageID;
                packageInfo.productList.get(i).packageName = packageInfo.packageName;
                packageInfo.productList.get(i).comboPrice = packageInfo.comboPrice;
                if (i == 0) {
                    packageInfo.productList.get(i).isFirstInPackage = true;
                } else if (i == packageInfo.productList.size() - 1) {
                    packageInfo.productList.get(i).isLastInPackage = true;
                }
                productInfos.add(packageInfo.productList.get(i));
            }
        }else {
            productInfos.add(productInfo);
        }
        return productInfos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.isPackage);
        dest.writeParcelable(this.packageInfo, flags);
        dest.writeParcelable(this.productInfo, flags);
    }
}
