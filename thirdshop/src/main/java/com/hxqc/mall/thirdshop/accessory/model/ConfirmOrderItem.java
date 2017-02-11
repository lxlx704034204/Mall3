package com.hxqc.mall.thirdshop.accessory.model;

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
public class ConfirmOrderItem implements Parcelable {

    /**
     * subscription : string,支付定金
     * totalCount : integer,该店铺总计商品数量
     * actualPaymentAmount : string,应付总金额
     * packageDiscount : string,套餐折扣
     * onShopPaymentAmount : string,到店支付金额
     * itemListInShop : {"shopID":"string,4s店ID","shopName":"string,4s店全称","itemList":[{"isPackage":"integer,是否是套餐，是：1，否：0","packageInfo":{"packageNum":"integer,套餐数量","packageName":"string,套餐名字","packagePrice":"string,套餐价格","productList":[{"price":"string,用品价格","smallPhoto":"string,缩略图url","productNum":"integer,商品数量","name":"string,用品名称","productID":"string,用品ID"}],"packageID":"string,套餐ID"},"productInfo":{"price":"string,用品价格","smallPhoto":"string,缩略图url","productNum":"integer,商品数量","name":"string,用品名称","productID":"string,用品ID"}}]}
     * orderTotalAmount : string,订单总金额
     * shopLocation : {"address":"string,地址","tel":"string,电话","name":"string,位置名称","longitude":"string,经度","latitude":"string,维度"}
     */
    @Expose
    public String subscription;
    @Expose
    public int totalCount;
    @Expose
    public String actualPaymentAmount;
    @Expose
    public String packageDiscount;
    @Expose
    public String orderTotalAmount;
    @Expose
    public ArrayList<ShoppingCartItem> itemList;

    public String getTotalCount() {
        return "共" + totalCount + "件用品";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subscription);
        dest.writeInt(this.totalCount);
        dest.writeString(this.actualPaymentAmount);
        dest.writeString(this.packageDiscount);
        dest.writeString(this.orderTotalAmount);
        dest.writeTypedList(this.itemList);
    }

    public ConfirmOrderItem() {
    }

    protected ConfirmOrderItem(Parcel in) {
        this.subscription = in.readString();
        this.totalCount = in.readInt();
        this.actualPaymentAmount = in.readString();
        this.packageDiscount = in.readString();
        this.orderTotalAmount = in.readString();
        this.itemList = in.createTypedArrayList(ShoppingCartItem.CREATOR);
    }

    public static final Parcelable.Creator<ConfirmOrderItem> CREATOR = new Parcelable.Creator<ConfirmOrderItem>() {
        @Override
        public ConfirmOrderItem createFromParcel(Parcel source) {
            return new ConfirmOrderItem(source);
        }

        @Override
        public ConfirmOrderItem[] newArray(int size) {
            return new ConfirmOrderItem[size];
        }
    };
}
