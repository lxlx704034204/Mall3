package com.hxqc.mall.thirdshop.accessory4s.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-08-04
 * Copyright:恒信汽车电子商务有限公司
 */
public class PackageInfo4S implements Parcelable {
    @Expose
    public int packageNum;
    @Expose
    public String packagePrice;
    @Expose
    public String packageID;
    @Expose
    public String packageName;
    @Expose
    public String comboPrice;
    @Expose
    public String discountPrice;
    @Expose
    public ArrayList<ProductInfo4S> productList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.packageNum);
        dest.writeString(this.packagePrice);
        dest.writeString(this.packageID);
        dest.writeString(this.packageName);
        dest.writeString(this.comboPrice);
        dest.writeString(this.discountPrice);
        dest.writeTypedList(this.productList);
    }

    public PackageInfo4S() {
    }

    protected PackageInfo4S(Parcel in) {
        this.packageNum = in.readInt();
        this.packagePrice = in.readString();
        this.packageID = in.readString();
        this.packageName = in.readString();
        this.comboPrice = in.readString();
        this.discountPrice = in.readString();
        this.productList = in.createTypedArrayList(ProductInfo4S.CREATOR);
    }

    public static final Parcelable.Creator<PackageInfo4S> CREATOR = new Parcelable.Creator<PackageInfo4S>() {
        @Override
        public PackageInfo4S createFromParcel(Parcel source) {
            return new PackageInfo4S(source);
        }

        @Override
        public PackageInfo4S[] newArray(int size) {
            return new PackageInfo4S[size];
        }
    };
    public String getIdList() {
        ArrayList<String> idList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            idList.add(productList.get(i).productID);
        }
        return idList.toString().replace("[", "").replace("]", "").replace(" ", "");
    }
}
