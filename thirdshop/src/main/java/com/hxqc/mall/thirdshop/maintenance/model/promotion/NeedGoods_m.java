package com.hxqc.mall.thirdshop.maintenance.model.promotion;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Author: wanghao
 * Date: 2016-03-22
 * FIXME
 * Todo 所需配件
 */
public class NeedGoods_m implements Parcelable {
    @Expose
    public String name;
    @Expose
    public String price;
    @Expose
    public String count;
    @Expose
    public String goodsID;
    @Expose
    public String thumb;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.price);
        dest.writeString(this.count);
        dest.writeString(this.goodsID);
        dest.writeString(this.thumb);
    }

    public NeedGoods_m() {
    }

    protected NeedGoods_m(Parcel in) {
        this.name = in.readString();
        this.price = in.readString();
        this.count = in.readString();
        this.goodsID = in.readString();
        this.thumb = in.readString();
    }

    public static final Parcelable.Creator< NeedGoods_m > CREATOR = new Parcelable.Creator< NeedGoods_m >() {
        @Override
        public NeedGoods_m createFromParcel(Parcel source) {
            return new NeedGoods_m(source);
        }

        @Override
        public NeedGoods_m[] newArray(int size) {
            return new NeedGoods_m[size];
        }
    };
}
