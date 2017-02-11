package com.hxqc.newenergy.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明:
 * author: 何玉
 * since: 2016/3/22.
 * Copyright:恒信汽车电子商务有限公司
 */
public class EVNewenergyNewsListBean implements Parcelable{

    public   String newsID=null;
    public   String newsTitle=null;
    public  String newsThumb=null;
    public  String newsDate=null;
    public  String newsSummary=null;


    protected EVNewenergyNewsListBean(Parcel in) {
        newsID = in.readString();
        newsTitle = in.readString();
        newsThumb = in.readString();
        newsDate = in.readString();
        newsSummary = in.readString();
    }

    public static final Creator<EVNewenergyNewsListBean> CREATOR = new Creator<EVNewenergyNewsListBean>() {
        @Override
        public EVNewenergyNewsListBean createFromParcel(Parcel in) {
            return new EVNewenergyNewsListBean(in);
        }

        @Override
        public EVNewenergyNewsListBean[] newArray(int size) {
            return new EVNewenergyNewsListBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(newsID);
        dest.writeString(newsTitle);
        dest.writeString(newsThumb);
        dest.writeString(newsDate);
        dest.writeString(newsSummary);
    }
}
