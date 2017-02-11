package com.hxqc.mall.core.model.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.hxqc.mall.core.model.auto.AutoPackage;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 说明:订单基类
 *
 * author: 吕飞
 * since: 2015-06-05
 * Copyright:恒信汽车电子商务有限公司
 */
public class BaseOrder implements Parcelable {
    @Expose
    public String itemCount;//商品数量
    @Expose
    public String itemPrice;//商品单价
    @Expose
    public String itemThumb;//商品缩略图的URL
    @Expose
    public String itemColor;//车辆颜色
    @Expose
    public String itemInterior;//车辆内饰颜色
    @Expose
    public String itemName;//产品名称
    @Expose
    public ArrayList<AutoPackage> packages;//套餐

    public ArrayList<AutoPackage> getPackages() {
        Comparator<AutoPackage> comparator = new Comparator<AutoPackage>() {
            @Override
            public int compare(AutoPackage lhs, AutoPackage rhs) {
                return Integer.parseInt(rhs.status) - Integer.parseInt(lhs.status);
            }
        };
        Collections.sort(packages, comparator);
        return packages;
    }

    public static final Creator<BaseOrder> CREATOR = new Creator<BaseOrder>() {
        @Override
        public BaseOrder createFromParcel(Parcel in) {
            return new BaseOrder(in);
        }

        @Override
        public BaseOrder[] newArray(int size) {
            return new BaseOrder[size];
        }
    };

    public String[] getItemColor() {
        String after = itemColor.replace(";", "");
        return after.split(",");
    }

    public String[] getItemInterior() {
        String after = itemInterior.replaceAll(";", "");
        return after.split(",");
    }

    public String getItemName() {
        return itemName;
    }


    public String getItemPrice() {
        return "¥" + OtherUtil.amountFormat(itemPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemCount);
        dest.writeString(this.itemPrice);
        dest.writeString(this.itemThumb);
        dest.writeString(this.itemColor);
        dest.writeString(this.itemInterior);
        dest.writeString(this.itemName);
        dest.writeTypedList(packages);
    }

    public BaseOrder() {
    }

    protected BaseOrder(Parcel in) {
        this.itemCount = in.readString();
        this.itemPrice = in.readString();
        this.itemThumb = in.readString();
        this.itemColor = in.readString();
        this.itemInterior = in.readString();
        this.itemName = in.readString();
        this.packages = in.createTypedArrayList(AutoPackage.CREATOR);
    }

}
