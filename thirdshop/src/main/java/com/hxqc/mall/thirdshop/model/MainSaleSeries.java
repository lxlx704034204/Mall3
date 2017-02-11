package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:李烽
 * Date:2015-12-01
 * FIXME
 * Todo 主营系列
 */
@Deprecated
public class MainSaleSeries implements Parcelable {
    public String seriesName;
    public String seriesID;
    public String seriesThumb;
    public String priceRange;
    public MainSaleSeries() {

    }

    protected MainSaleSeries(Parcel in) {
        this.seriesID = in.readString();
        this.seriesName = in.readString();
        this.seriesThumb = in.readString();
        this.priceRange = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.seriesID);
        dest.writeString(this.seriesName);
        dest.writeString(this.seriesThumb);
        dest.writeString(this.priceRange);
    }

    public static final Parcelable.Creator<MainSaleSeries> CREATOR = new Parcelable.Creator<MainSaleSeries>() {
        @Override
        public MainSaleSeries createFromParcel(Parcel source) {
            return new MainSaleSeries(source);
        }

        @Override
        public MainSaleSeries[] newArray(int size) {
            return new MainSaleSeries[size];
        }
    };
}
