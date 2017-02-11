package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.hxqc.mall.core.model.auto.BasePromotion;

/**
 * 说明:首页特卖
 *
 * author: 吕飞
 * since: 2015-06-17
 * Copyright:恒信汽车电子商务有限公司
 */
public class SpecialOfferForHome extends BasePromotion implements Cloneable, Parcelable {
    @Expose
    public String decrease;//降幅
    @Expose
    public String itemName;//特卖名称
    @Expose
    public String itemPic;//特卖图片
    @Expose
    public String itemPrice;// 特卖价
    @Expose
    public String stock;//库存

    //获取标题：有车名和降幅组成
    public String getTitle() {
        return "[直降" + decrease + "元] "+itemName;
    }
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.decrease);
        dest.writeString(this.itemName);
        dest.writeString(this.itemPic);
        dest.writeString(this.itemPrice);
        dest.writeString(this.stock);
    }

    public SpecialOfferForHome() {
    }

    protected SpecialOfferForHome(Parcel in) {
        this.decrease = in.readString();
        this.itemName = in.readString();
        this.itemPic = in.readString();
        this.itemPrice = in.readString();
        this.stock = in.readString();
    }

    public static final Parcelable.Creator<SpecialOfferForHome> CREATOR = new Parcelable.Creator<SpecialOfferForHome>() {
        public SpecialOfferForHome createFromParcel(Parcel source) {
            return new SpecialOfferForHome(source);
        }

        public SpecialOfferForHome[] newArray(int size) {
            return new SpecialOfferForHome[size];
        }
    };
}
