package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明:工时费
 *
 * @author: 吕飞
 * @since: 2016-05-30
 * Copyright:恒信汽车电子商务有限公司
 */
public class LaborCost implements Parcelable {
    public String itemName;//项目名称 string
    public String cost;//费用 string

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemName);
        dest.writeString(this.cost);
    }

    public LaborCost() {
    }

    protected LaborCost(Parcel in) {
        this.itemName = in.readString();
        this.cost = in.readString();
    }

    public static final Parcelable.Creator<LaborCost> CREATOR = new Parcelable.Creator<LaborCost>() {
        @Override
        public LaborCost createFromParcel(Parcel source) {
            return new LaborCost(source);
        }

        @Override
        public LaborCost[] newArray(int size) {
            return new LaborCost[size];
        }
    };
}
