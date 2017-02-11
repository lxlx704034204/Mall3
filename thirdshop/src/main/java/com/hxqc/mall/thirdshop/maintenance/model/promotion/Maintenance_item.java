package com.hxqc.mall.thirdshop.maintenance.model.promotion;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2016-03-22
 * FIXME
 * Todo  保养项目
 */
public class Maintenance_item implements Parcelable {

    @Expose
    public  String name;
    @Expose
    public String itemID;
    @Expose
    public String amount;
    @Expose
    public String workCost;
    @Expose
    public String discount;
    @Expose
    public String deduction;
    @Expose
    public String summary;
    @Expose
    public ArrayList<NeedGoods_m> goods;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.itemID);
        dest.writeString(this.amount);
        dest.writeString(this.workCost);
        dest.writeString(this.discount);
        dest.writeString(this.deduction);
        dest.writeString(this.summary);
        dest.writeTypedList(goods);
    }

    public Maintenance_item() {
    }

    protected Maintenance_item(Parcel in) {
        this.name = in.readString();
        this.itemID = in.readString();
        this.amount = in.readString();
        this.workCost = in.readString();
        this.discount = in.readString();
        this.deduction = in.readString();
        this.summary = in.readString();
        this.goods = in.createTypedArrayList(NeedGoods_m.CREATOR);
    }

    public static final Parcelable.Creator< Maintenance_item > CREATOR = new Parcelable.Creator< Maintenance_item >() {
        @Override
        public Maintenance_item createFromParcel(Parcel source) {
            return new Maintenance_item(source);
        }

        @Override
        public Maintenance_item[] newArray(int size) {
            return new Maintenance_item[size];
        }
    };
}
