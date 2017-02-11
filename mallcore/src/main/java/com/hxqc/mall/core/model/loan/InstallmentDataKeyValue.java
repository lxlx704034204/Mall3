package com.hxqc.mall.core.model.loan;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: liukechong
 * Date: 2015-10-16
 * FIXME key value
 * Todo
 */
public class InstallmentDataKeyValue implements Parcelable {
    public int key;
    public String value;

    @Override
    public String toString() {
        return "InstallmentDataKeyValue{" +
                "key=" + key +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.key);
        dest.writeString(this.value);
    }

    public InstallmentDataKeyValue() {
    }

    protected InstallmentDataKeyValue(Parcel in) {
        this.key = in.readInt();
        this.value = in.readString();
    }

    public static final Creator<InstallmentDataKeyValue> CREATOR = new Creator<InstallmentDataKeyValue>() {
        public InstallmentDataKeyValue createFromParcel(Parcel source) {
            return new InstallmentDataKeyValue(source);
        }

        public InstallmentDataKeyValue[] newArray(int size) {
            return new InstallmentDataKeyValue[size];
        }
    };
}
