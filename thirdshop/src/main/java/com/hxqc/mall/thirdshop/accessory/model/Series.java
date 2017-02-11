package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.core.util.OtherUtil;

/**
 * Function:车系model
 *
 * @author 袁秉勇
 * @since 2015年12月09日
 */
public class Series implements Parcelable {
    public static final Creator<Series> CREATOR = new Creator<Series>() {
        public Series createFromParcel(Parcel source) {
            return new Series(source);
        }

        public Series[] newArray(int size) {
            return new Series[size];
        }
    };
    public String seriesID;//车系ID
    public String seriesName;//车系名称
    public String seriesThumb;//车系图标url
    public String priceRange;//价格区间
    public boolean isChecked = false; //当前是否选中 hy

    public Series() {
    }

    protected Series(Parcel in) {
        this.seriesID = in.readString();
        this.seriesName = in.readString();
        this.seriesThumb = in.readString();
        this.priceRange = in.readString();
        this.isChecked = in.readByte() != 0;
    }

    public String getPriceRange() {
        return OtherUtil.formatPriceRange(priceRange);
    }

    public String getSeriesName() {
        return seriesName;
    }

    public String getSeriesThumb() {
        return seriesThumb;
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
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }
}
