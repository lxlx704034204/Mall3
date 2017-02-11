package com.hxqc.mall.thirdshop.maintenance.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:
    到店付支持的支付方式[
        {
            payName:
                支付名称 string
            thumb:
                图标url string
        }
 *
 * @author 袁秉勇
 * @since 2016年05月24日
 */
public class Payment implements Parcelable {

    public String payName;

    public String thumb;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.payName);
        dest.writeString(this.thumb);
    }

    public Payment() {
    }

    protected Payment(Parcel in) {
        this.payName = in.readString();
        this.thumb = in.readString();
    }

    public static final Parcelable.Creator<Payment> CREATOR = new Parcelable.Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel source) {
            return new Payment(source);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };
}
