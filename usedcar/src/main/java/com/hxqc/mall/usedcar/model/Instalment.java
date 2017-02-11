package com.hxqc.mall.usedcar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 分期
 * Created by huangyi on 16/6/18.
 */
public class Instalment implements Parcelable {

    public static final Parcelable.Creator<Instalment> CREATOR = new Parcelable.Creator<Instalment>() {
        public Instalment createFromParcel(Parcel in) {
            return new Instalment(in);
        }

        public Instalment[] newArray(int size) {
            return new Instalment[size];
        }
    };

    public String limit;
    public String item_id;
    public String year;
    public String per_month;
    public boolean isChecked = false; //当前是否选中

    private Instalment(Parcel in) {
        limit = in.readString();
        item_id = in.readString();
        year = in.readString();
        per_month = in.readString();
        isChecked = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(limit);
        out.writeString(item_id);
        out.writeString(year);
        out.writeString(per_month);
        out.writeByte((byte) (isChecked ? 1 : 0));
    }

}
