package com.hxqc.mall.thirdshop.accessory4s.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:确认订单的一个item
 *
 * @author: 吕飞
 * @since: 2016-02-24
 * Copyright:恒信汽车电子商务有限公司
 */
public class ConfirmOrderItem4S implements Parcelable {
    @Expose
    public String payAmount;
    @Expose
    public String packageDiscount;
    @Expose
    public String orderAmount;
    @Expose
    public int productCount;
    @Expose
    public ShopInfo4S shopInfo;
    @Expose
    public ArrayList<ProductIn4S> productList;

    public String getTotalCount() {
        return "共" + productCount + "件用品";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.payAmount);
        dest.writeString(this.packageDiscount);
        dest.writeString(this.orderAmount);
        dest.writeInt(this.productCount);
        dest.writeParcelable(this.shopInfo, flags);
        dest.writeList(this.productList);
    }

    public ConfirmOrderItem4S() {
    }

    protected ConfirmOrderItem4S(Parcel in) {
        this.payAmount = in.readString();
        this.packageDiscount = in.readString();
        this.orderAmount = in.readString();
        this.productCount = in.readInt();
        this.shopInfo = in.readParcelable(ShopInfo4S.class.getClassLoader());
        this.productList = new ArrayList<>();
        in.readList(this.productList, ProductIn4S.class.getClassLoader());
    }

    public static final Parcelable.Creator<ConfirmOrderItem4S> CREATOR = new Parcelable.Creator<ConfirmOrderItem4S>() {
        @Override
        public ConfirmOrderItem4S createFromParcel(Parcel source) {
            return new ConfirmOrderItem4S(source);
        }

        @Override
        public ConfirmOrderItem4S[] newArray(int size) {
            return new ConfirmOrderItem4S[size];
        }
    };
}
