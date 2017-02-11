package com.hxqc.mall.thirdshop.accessory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:品牌
 * <p/>
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class Brand  implements Parcelable {
    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
        public Brand createFromParcel(Parcel source) {
            return new Brand(source);
        }

        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };
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
    public ArrayList< Series > seriesItem;//车系
    @Expose
    public String filterKey;//与服务器交互时，筛选条件key
    @Expose
    public String filterValue;//与服务器交互时，筛选条件选项值
    @Expose
    public String label;//选项名称
    @Expose
    public boolean isChecked = false; //当前是否选中 hy
    @Expose
    public ArrayList<Series> series; //车系 hy ---> 旧版用
    public ArrayList<Series> seriesList; //车系 hy ---> 新版用


    public Brand(String brandName) {
        this.brandName = brandName;
    }


    protected Brand(Parcel in) {
        this.brandID = in.readString();
        this.brandInitial = in.readString();
        this.brandName = in.readString();
        this.brandThumb = in.readString();
        this.brandEName = in.readString();
        this.seriesItem = in.createTypedArrayList(Series.CREATOR);
        this.filterKey = in.readString();
        this.filterValue = in.readString();
        this.label = in.readString();
        this.isChecked = in.readByte() != 0;
        this.series = in.createTypedArrayList(Series.CREATOR);
    }

    public String getLabel() {
        return brandName;
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
        dest.writeTypedList(seriesItem);
        dest.writeString(this.filterKey);
        dest.writeString(this.filterValue);
        dest.writeString(this.label);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeTypedList(series);
    }
}
