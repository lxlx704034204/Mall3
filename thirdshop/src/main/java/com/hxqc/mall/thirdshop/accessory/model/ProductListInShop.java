package com.hxqc.mall.thirdshop.accessory.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:每个店的用品列表
 *
 * @author: 吕飞
 * @since: 2016-02-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class ProductListInShop implements Parcelable {

    public static final Parcelable.Creator<ProductListInShop> CREATOR = new Parcelable.Creator<ProductListInShop>() {
        public ProductListInShop createFromParcel(Parcel source) {
            return new ProductListInShop(source);
        }

        public ProductListInShop[] newArray(int size) {
            return new ProductListInShop[size];
        }
    };
    /**
     * shopID : string,4s店ID
     * shopName : string,4s店全称
     */
//    @Expose
//    public String shopID;
//    @Expose
//    public String shopName;
    @Expose
    public ArrayList<ShoppingCartItem> productListInShop;

    public ProductListInShop() {
    }

    protected ProductListInShop(Parcel in) {
        this.productListInShop = new ArrayList<>();
        in.readList(this.productListInShop, List.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.productListInShop);
    }
}
