package com.hxqc.mall.core.model.auto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.hxqc.mall.auto.model.Filter;
import com.hxqc.mall.auto.model.Series;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:品牌
 * <p/>
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class Brand extends Filter implements Parcelable {
    @Expose
    public String brandID;//品牌id
    @Expose
    public String brandInitial;//品牌首字母
    @Expose
    public String brandName;//品牌名称
    @Expose
    public String brandThumb;//对应图标的URL。
    @Expose
    public String brandEName;//品牌拼音

    public int brandType=10;//品牌类型：10.汽车品牌（全部），20.新能源品牌
    public ArrayList<Series> seriesItem;//车系

    public Brand(String brandName) {
        this.brandName = brandName;
    }

    public String getLabel() {
        return brandName;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Brand) {
            return brandID.equals(((Brand) o).brandID);
        }
        return super.equals(o);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.brandID);
        dest.writeString(this.brandInitial);
        dest.writeString(this.brandName);
        dest.writeString(this.brandThumb);
        dest.writeString(this.brandEName);
        dest.writeList(this.seriesItem);
    }

    protected Brand(Parcel in) {
        this.brandID = in.readString();
        this.brandInitial = in.readString();
        this.brandName = in.readString();
        this.brandThumb = in.readString();
        this.brandEName = in.readString();
        this.seriesItem = new ArrayList<>();
        in.readList(this.seriesItem, List.class.getClassLoader());
    }

    public static final Creator< Brand > CREATOR = new Creator< Brand >() {
        public Brand createFromParcel(Parcel source) {
            return new Brand(source);
        }

        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };

    @Override
    public String toString() {
        return "Brand{" +
                "brandID='" + brandID + '\'' +
                ", brandInitial='" + brandInitial + '\'' +
                ", brandName='" + brandName + '\'' +
                ", brandThumb='" + brandThumb + '\'' +
                ", brandEName='" + brandEName + '\'' +
                ", seriesItem=" + seriesItem +
                '}';
    }
}
