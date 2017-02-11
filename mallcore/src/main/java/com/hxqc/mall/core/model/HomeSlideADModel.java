package com.hxqc.mall.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: wanghao
 * Date: 2015-04-22
 * FIXME
 * 主页上半部分 切换的  活动
 */
public class HomeSlideADModel implements Cloneable, Parcelable {

    public String routerUrl;
    public String slide;
    public String type;
    public String url;

    //是否活动
    public boolean isPromotion() {
        return type.equals("1");
    }

    //是否特卖
    public boolean isSeckill() {
        return url.split("/")[2].equalsIgnoreCase("Seckill");
    }

    //获取ID
    public String getID() {
        return url.split("/")[3];
    }

    //获取商品名字
    public String getProductName() {
        return url.split("/")[4];
    }

    @Override
    public String toString() {
        return "HomeSlideADModel{" +
                "slide='" + slide + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.routerUrl);
        dest.writeString(this.slide);
        dest.writeString(this.type);
        dest.writeString(this.url);
    }

    public HomeSlideADModel() {
    }

    protected HomeSlideADModel(Parcel in) {
        this.routerUrl = in.readString();
        this.slide = in.readString();
        this.type = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<HomeSlideADModel> CREATOR = new Parcelable.Creator<HomeSlideADModel>() {
        @Override
        public HomeSlideADModel createFromParcel(Parcel source) {
            return new HomeSlideADModel(source);
        }

        @Override
        public HomeSlideADModel[] newArray(int size) {
            return new HomeSlideADModel[size];
        }
    };
}
