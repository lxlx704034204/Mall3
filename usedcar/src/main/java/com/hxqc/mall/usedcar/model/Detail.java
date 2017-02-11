package com.hxqc.mall.usedcar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 质保 查看详情 表格 价格表
 * Created by huangyi on 16/6/21.
 */
public class Detail implements Parcelable {

    public static final Parcelable.Creator<Detail> CREATOR = new Parcelable.Creator<Detail>() {
        public Detail createFromParcel(Parcel in) {
            return new Detail(in);
        }

        public Detail[] newArray(int size) {
            return new Detail[size];
        }
    };

    public String detail_text;

    private Detail(Parcel in) {
        detail_text = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(detail_text);
    }

}
