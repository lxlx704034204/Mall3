package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.core.util.OtherUtil;

/**
 * Author:李烽
 * Date:2015-12-01
 * FIXME
 * Todo 主推车型
 */
public class AutoBaseInfoThirdShop implements Parcelable {
    public String itemThumb;
    public String itemID ; //车辆ID
    public String itemName ; //车辆简称
    public float itemPrice; //价格
    public float itemOrigPrice;//厂家指导价
    public float itemFall;//降幅
    public String itemDescription;//车辆类型描述
    public int isInPromotion;//是否为促销商品（0否，1是）

    public String getItemFall() {
        return "¥" +OtherUtil.amountFormat(itemFall);
    }

    public String getItemPriceString() {
        return "¥" +OtherUtil.amountFormat(itemPrice);
    }

    public String getItemOrigPriceString() {
        return "¥" +OtherUtil.amountFormat(itemOrigPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemThumb);
        dest.writeString(this.itemID);
        dest.writeString(this.itemName);
        dest.writeFloat(this.itemPrice);
        dest.writeFloat(this.itemOrigPrice);
        dest.writeFloat(this.itemFall);
        dest.writeString(this.itemDescription);
        dest.writeInt(this.isInPromotion);
    }

    public AutoBaseInfoThirdShop() {
    }

    protected AutoBaseInfoThirdShop(Parcel in) {
        this.itemThumb = in.readString();
        this.itemID = in.readString();
        this.itemName = in.readString();
        this.itemPrice = in.readFloat();
        this.itemOrigPrice = in.readFloat();
        this.itemFall = in.readFloat();
        this.itemDescription = in.readString();
        this.isInPromotion = in.readInt();
    }

    public static final Parcelable.Creator<AutoBaseInfoThirdShop> CREATOR = new Parcelable.Creator<AutoBaseInfoThirdShop>() {
        public AutoBaseInfoThirdShop createFromParcel(Parcel source) {
            return new AutoBaseInfoThirdShop(source);
        }

        public AutoBaseInfoThirdShop[] newArray(int size) {
            return new AutoBaseInfoThirdShop[size];
        }
    };
}
