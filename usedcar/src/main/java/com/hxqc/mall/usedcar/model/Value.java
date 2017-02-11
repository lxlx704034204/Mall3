package com.hxqc.mall.usedcar.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 质保 查看详情 表格 价格表和明细
 * Created by huangyi on 16/6/21.
 */
public class Value implements Parcelable {

    public static final Parcelable.Creator<Value> CREATOR = new Parcelable.Creator<Value>() {
        public Value createFromParcel(Parcel in) {
            return new Value(in);
        }

        public Value[] newArray(int size) {
            return new Value[size];
        }
    };

    public String price;
    public ArrayList<Detail> detail;

    private Value(Parcel in) {
        price = in.readString();
        detail = in.createTypedArrayList(Detail.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(price);
        out.writeTypedList(detail);
    }

}
