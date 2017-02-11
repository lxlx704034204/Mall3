package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * 说明:金融（首页）
 *
 * author: 吕飞
 * since: 2015-06-17
 * Copyright:恒信汽车电子商务有限公司
 */
public class FinanceForHome implements Parcelable {
    @Expose
    public String itemPic;//对应图片
    @Expose
    public String url;//对应URL

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemPic);
        dest.writeString(this.url);
    }

    public FinanceForHome() {
    }

    protected FinanceForHome(Parcel in) {
        this.itemPic = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<FinanceForHome> CREATOR = new Parcelable.Creator<FinanceForHome>() {
        public FinanceForHome createFromParcel(Parcel source) {
            return new FinanceForHome(source);
        }

        public FinanceForHome[] newArray(int size) {
            return new FinanceForHome[size];
        }
    };
}
