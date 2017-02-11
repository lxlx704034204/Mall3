package com.hxqc.mall.thirdshop.maintenance.model.promotion;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2016-03-22
 * FIXME
 * Todo  套餐项目
 */
public class Maintenance_package implements Parcelable {
    @Expose
    public String packageID;
    @Expose
    public String name;
    @Expose
    public String amount;
    @Expose
    public String discount;
    @Expose
    public ArrayList<Maintenance_item> items;
    @Expose
    public String suitable;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageID);
        dest.writeString(this.name);
        dest.writeString(this.amount);
        dest.writeString(this.discount);
        dest.writeTypedList(items);
        dest.writeString(this.suitable);
    }

    public Maintenance_package() {
    }

    protected Maintenance_package(Parcel in) {
        this.packageID = in.readString();
        this.name = in.readString();
        this.amount = in.readString();
        this.discount = in.readString();
        this.items = in.createTypedArrayList(Maintenance_item.CREATOR);
        this.suitable = in.readString();
    }

    public static final Creator< Maintenance_package > CREATOR = new Creator< Maintenance_package >() {
        @Override
        public Maintenance_package createFromParcel(Parcel source) {
            return new Maintenance_package(source);
        }

        @Override
        public Maintenance_package[] newArray(int size) {
            return new Maintenance_package[size];
        }
    };
}
