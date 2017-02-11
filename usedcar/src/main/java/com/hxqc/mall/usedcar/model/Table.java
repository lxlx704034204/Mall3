package com.hxqc.mall.usedcar.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 质保 查看详情 表格
 * Created by huangyi on 16/6/21.
 */
public class Table implements Parcelable {

    public static final Parcelable.Creator<Table> CREATOR = new Parcelable.Creator<Table>() {
        public Table createFromParcel(Parcel in) {
            return new Table(in);
        }

        public Table[] newArray(int size) {
            return new Table[size];
        }
    };

    public String key;
    public ArrayList<Value> values;

    private Table(Parcel in) {
        key = in.readString();
        values = in.createTypedArrayList(Value.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(key);
        out.writeTypedList(values);
    }

}
