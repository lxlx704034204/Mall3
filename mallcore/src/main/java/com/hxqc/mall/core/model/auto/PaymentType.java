package com.hxqc.mall.core.model.auto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡俊杰
 * Date: 2015/10/18
 * FIXME
 * Todo
 */
public class PaymentType implements Parcelable {
    public String paymentDesc;
    public String paymentTitle;
    public int paymentType;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.paymentDesc);
        dest.writeString(this.paymentTitle);
        dest.writeInt(this.paymentType);
    }

    public PaymentType() {
    }

    protected PaymentType(Parcel in) {
        this.paymentDesc = in.readString();
        this.paymentTitle = in.readString();
        this.paymentType = in.readInt();
    }

    public static final Parcelable.Creator< PaymentType > CREATOR = new Parcelable.Creator< PaymentType >() {
        public PaymentType createFromParcel(Parcel source) {
            return new PaymentType(source);
        }

        public PaymentType[] newArray(int size) {
            return new PaymentType[size];
        }
    };
}
