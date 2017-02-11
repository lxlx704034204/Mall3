package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:套餐基本信息
 *
 * @author: 吕飞
 * @since: 2016-02-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class PackageInfo implements Parcelable {

    public static final Parcelable.Creator<PackageInfo> CREATOR = new Parcelable.Creator<PackageInfo>() {
        public PackageInfo createFromParcel(Parcel source) {
            return new PackageInfo(source);
        }

        public PackageInfo[] newArray(int size) {
            return new PackageInfo[size];
        }
    };
    /**
     * packageNum : integer,套餐数量
     * packagePrice : string,套餐价格
     * packageName : string,套餐名字
     * productList : [{"price":"string,用品价格","smallPhoto":"string,缩略图url","productNum":"integer,商品数量","name":"string,用品名称","productID":"string,用品ID"}]
     * packageID : string,套餐ID
     */
    @Expose
    public String packageNum;
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
    public ArrayList<ProductInfo> productList;

    public PackageInfo() {
    }

    protected PackageInfo(Parcel in) {
        this.packageNum = in.readString();
        this.packagePrice = in.readString();
        this.packageID = in.readString();
        this.packageName = in.readString();
        this.comboPrice = in.readString();
        this.discountPrice = in.readString();
        this.productList = new ArrayList<>();
        in.readList(this.productList, ProductInfo.class.getClassLoader());
    }

    //    初始化套餐里面的商品
    public void initPackage() {
        for (int i = 0; i < productList.size(); i++) {
            productList.get(i).isInPackage = true;
            if (i == 0) {
                productList.get(i).isFirstInPackage = true;
                productList.get(i).packageName = packageName;
                productList.get(i).packageID = packageID;
                productList.get(i).packageNum = packageNum;
                productList.get(i).comboPrice = comboPrice;
            }
        }
    }

    public String getIdList() {
        ArrayList<String> idList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            idList.add(productList.get(i).productID);
        }
        return idList.toString().replace("[", "").replace("]", "").replace(" ", "");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageNum);
        dest.writeString(this.packagePrice);
        dest.writeString(this.packageID);
        dest.writeString(this.packageName);
        dest.writeString(this.discountPrice);
        dest.writeString(this.comboPrice);
        dest.writeList(this.productList);
    }
}
