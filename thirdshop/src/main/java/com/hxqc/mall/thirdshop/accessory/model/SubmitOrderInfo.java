package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明:提交订单的信息
 *
 * @author: 吕飞
 * @since: 2016-02-29
 * Copyright:恒信汽车电子商务有限公司
 */
public class SubmitOrderInfo implements Parcelable {

    public static final Parcelable.Creator<SubmitOrderInfo> CREATOR = new Parcelable.Creator<SubmitOrderInfo>() {
        public SubmitOrderInfo createFromParcel(Parcel source) {
            return new SubmitOrderInfo(source);
        }

        public SubmitOrderInfo[] newArray(int size) {
            return new SubmitOrderInfo[size];
        }
    };
    /**
     * orderID : string,用品订单id
     * shopName : string,4s店铺名称
     */

    public String orderID;
    public String shopName;

    public SubmitOrderInfo(String orderID, String shopName) {
        this.orderID = orderID;
        this.shopName = shopName;
    }

    public SubmitOrderInfo() {
    }

    protected SubmitOrderInfo(Parcel in) {
        this.orderID = in.readString();
        this.shopName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderID);
        dest.writeString(this.shopName);
    }
}
