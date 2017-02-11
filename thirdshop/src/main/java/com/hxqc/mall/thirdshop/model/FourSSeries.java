package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-05-05
 * Copyright:恒信汽车电子商务有限公司
 */
public class FourSSeries extends Series {
    public String itemOrigPrice;//厂家指导价

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.itemOrigPrice);
    }

    public FourSSeries() {
    }

    protected FourSSeries(Parcel in) {
        super(in);
        this.itemOrigPrice = in.readString();
    }

    public static final Creator<FourSSeries> CREATOR = new Creator<FourSSeries>() {
        @Override
        public FourSSeries createFromParcel(Parcel source) {
            return new FourSSeries(source);
        }

        @Override
        public FourSSeries[] newArray(int size) {
            return new FourSSeries[size];
        }
    };
}
