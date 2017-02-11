package com.hxqc.mall.thirdshop.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明:4s店资讯
 *
 * @author: 吕飞
 * @since: 2016-05-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class FourSNews implements Parcelable {
    public String newsID;//新闻ID
    public String title;//标题
    public String summary;//简介
    public String publishDate;//发布时间
    public String thumb;//新闻图片URL
    public String thumbSmall;//新闻小图URL
    public int newsKind;// 新闻所属 10一般链接 20普通车 30特价车 40店内促销 50保养促销 60新闻
    public FourSDefaultNews defaultNews;//默认或者其他 newsKind 10
    public FourSNewAuto newAuto;//普通车辆 newsKind 20/30 { }
    public FourSPromotion promotion;//新闻/店内促销/保养促销 newsKind 40/50/60 { }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.newsID);
        dest.writeString(this.title);
        dest.writeString(this.summary);
        dest.writeString(this.publishDate);
        dest.writeString(this.thumb);
        dest.writeString(this.thumbSmall);
        dest.writeInt(this.newsKind);
        dest.writeParcelable(this.defaultNews, flags);
        dest.writeParcelable(this.newAuto, flags);
        dest.writeParcelable(this.promotion, flags);
    }

    public FourSNews() {
    }

    protected FourSNews(Parcel in) {
        this.newsID = in.readString();
        this.title = in.readString();
        this.summary = in.readString();
        this.publishDate = in.readString();
        this.thumb = in.readString();
        this.thumbSmall = in.readString();
        this.newsKind = in.readInt();
        this.defaultNews = in.readParcelable(FourSDefaultNews.class.getClassLoader());
        this.newAuto = in.readParcelable(FourSNewAuto.class.getClassLoader());
        this.promotion = in.readParcelable(FourSPromotion.class.getClassLoader());
    }

    public static final Parcelable.Creator<FourSNews> CREATOR = new Parcelable.Creator<FourSNews>() {
        @Override
        public FourSNews createFromParcel(Parcel source) {
            return new FourSNews(source);
        }

        @Override
        public FourSNews[] newArray(int size) {
            return new FourSNews[size];
        }
    };
}
