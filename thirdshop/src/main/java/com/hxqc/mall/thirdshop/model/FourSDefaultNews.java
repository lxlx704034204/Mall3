package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-05-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class FourSDefaultNews implements Parcelable {
    public String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    public FourSDefaultNews() {
    }

    protected FourSDefaultNews(Parcel in) {
        this.url = in.readString();
    }

    public static final Parcelable.Creator<FourSDefaultNews> CREATOR = new Parcelable.Creator<FourSDefaultNews>() {
        @Override
        public FourSDefaultNews createFromParcel(Parcel source) {
            return new FourSDefaultNews(source);
        }

        @Override
        public FourSDefaultNews[] newArray(int size) {
            return new FourSDefaultNews[size];
        }
    };
}
