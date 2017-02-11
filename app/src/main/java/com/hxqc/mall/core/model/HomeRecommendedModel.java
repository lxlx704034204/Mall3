package com.hxqc.mall.core.model;

import android.os.Parcel;

import com.hxqc.mall.core.model.auto.AutoBaseInformation;
import com.hxqc.mall.core.util.OtherUtil;

/**
 * Author: wanghao
 * Date: 2015-04-20
 * FIXME
 * 首页 推荐车型
 */
public class HomeRecommendedModel extends AutoBaseInformation {

    public String price;
    public String thumb;
    public String title;


    @Override
    public String getItemDescription() {
        return title;
    }

    @Override
    public String getItemPriceU() {
        return OtherUtil.amountFormat(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.price);
        dest.writeString(this.thumb);
        dest.writeString(this.title);
    }

    public HomeRecommendedModel() {
    }

    protected HomeRecommendedModel(Parcel in) {
        super(in);
        this.price = in.readString();
        this.thumb = in.readString();
        this.title = in.readString();
    }

    public static final Creator<HomeRecommendedModel> CREATOR = new Creator<HomeRecommendedModel>() {
        @Override
        public HomeRecommendedModel createFromParcel(Parcel source) {
            return new HomeRecommendedModel(source);
        }

        @Override
        public HomeRecommendedModel[] newArray(int size) {
            return new HomeRecommendedModel[size];
        }
    };
}
