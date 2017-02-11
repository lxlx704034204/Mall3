package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 单个优惠套餐
 * Created by huangyi on 16/2/23.
 */
public class SinglePackage implements Parcelable {

    public static final Parcelable.Creator<SinglePackage> CREATOR = new Parcelable.Creator<SinglePackage>() {
        public SinglePackage createFromParcel(Parcel in) {
            return new SinglePackage(in);
        }

        public SinglePackage[] newArray(int size) {
            return new SinglePackage[size];
        }
    };

    public String packageName; //套餐名
    public String packageID; //套餐id
    public String packageCut; //套餐折扣

    public ArrayList<SinglePackageProducts> productList;

    public SinglePackage(String packageName, String packageID, String packageCut, ArrayList<SinglePackageProducts> productList) {
        this.packageName = packageName;
        this.packageID = packageID;
        this.packageCut = packageCut;
        this.productList = productList;
    }

    private SinglePackage(Parcel in) {
        //读取
        packageName = in.readString();
        packageID = in.readString();
        packageCut = in.readString();
        productList = in.createTypedArrayList(SinglePackageProducts.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        //写入
        out.writeString(packageName);
        out.writeString(packageID);
        out.writeString(packageCut);
        out.writeTypedList(productList);
    }

}
