package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by CPR062 on 2015/2/4.
 */
public class AlipayResponse implements Parcelable{

    @Expose
    public String partner;

    @Expose
    public String seller;

    @Expose
    public String trade_no;

    @Expose
    public String product_name;

    @Expose
    public String product_description;

    @Expose
    public String amount;

    @Expose
    public String notify_url;

    @Expose
    public String name;

    @Expose
    public String phone_number;

    @Expose
    public String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.partner);
        dest.writeString(this.seller);
        dest.writeString(this.trade_no);
        dest.writeString(this.product_name);
        dest.writeString(this.product_description);
        dest.writeString(this.amount);
        dest.writeString(this.notify_url);
        dest.writeString(this.name);
        dest.writeString(this.phone_number);
        dest.writeString(this.url);
    }

    public AlipayResponse() {
    }

    private AlipayResponse(Parcel in) {
        this.partner = in.readString();
        this.seller = in.readString();
        this.trade_no = in.readString();
        this.product_name = in.readString();
        this.product_description = in.readString();
        this.amount = in.readString();
        this.notify_url = in.readString();
        this.name = in.readString();
        this.phone_number = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<AlipayResponse > CREATOR = new Parcelable.Creator<AlipayResponse >() {
        public AlipayResponse createFromParcel(Parcel source) {
            return new AlipayResponse(source);
        }

        public AlipayResponse[] newArray(int size) {
            return new AlipayResponse[size];
        }
    };




}























