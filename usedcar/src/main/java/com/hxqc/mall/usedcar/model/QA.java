package com.hxqc.mall.usedcar.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 质保
 * Created by huangyi on 16/6/18.
 */
public class QA implements Parcelable {

    public static final Parcelable.Creator<QA> CREATOR = new Parcelable.Creator<QA>() {
        public QA createFromParcel(Parcel in) {
            return new QA(in);
        }

        public QA[] newArray(int size) {
            return new QA[size];
        }
    };

    public String item_price;
    public String item_name;
    public String item_id;

    public ArrayList<QADetail> item_detail;
    public boolean isChecked = false;

    private QA(Parcel in) {
        item_price = in.readString();
        item_name = in.readString();
        item_id = in.readString();
        item_detail = in.createTypedArrayList(QADetail.CREATOR);
        isChecked = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(item_price);
        out.writeString(item_name);
        out.writeString(item_id);
        out.writeTypedList(item_detail);
        out.writeByte((byte) (isChecked ? 1 : 0));
    }

}
