package com.hxqc.mall.thirdshop.maintenance.model.coupon;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-02-24
 * FIXME
 * Todo 可用item
 */
public class RuleItem implements Parcelable{
    public static final Creator<RuleItem> CREATOR = new Creator<RuleItem>() {
        @Override
        public RuleItem createFromParcel(Parcel in) {
            return new RuleItem(in);
        }

        @Override
        public RuleItem[] newArray(int size) {
            return new RuleItem[size];
        }
    };
    public String itemID;//项目id
    public ArrayList<String> goods;//    项目所需用品

    protected RuleItem(Parcel in) {
        itemID = in.readString();
        goods = in.createStringArrayList();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemID);
        dest.writeStringList(goods);
    }
}
