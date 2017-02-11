package com.hxqc.mall.drivingexam.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zhaofan on 2016/8/1.
 */

public class QItems implements Parcelable {
    public Long id;
    public String question;        //题目
    public String mediaType;    //类型
    public List<Options> options;    //选项列表
    public String mediaUrl;  //url
    public String konwledge; //解析

    public QItems(String question, String mediaType, List<Options> options, String mediaUrl, String konwledge) {
        this.question = question;
        this.mediaType = mediaType;
        this.options = options;
        this.mediaUrl = mediaUrl;
        this.konwledge = konwledge;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getKonwledge() {
        return konwledge;
    }

    public void setKonwledge(String konwledge) {
        this.konwledge = konwledge;
    }

    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    @Override
    public String toString() {
        return "QItems{" +
                "question='" + question + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", options=" + options +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", konwledge='" + konwledge + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
        dest.writeString(this.mediaType);
        dest.writeTypedList(this.options);
        dest.writeString(this.mediaUrl);
    }

    public QItems() {
    }

    protected QItems(Parcel in) {
        this.question = in.readString();
        this.mediaType = in.readString();
        this.options = in.createTypedArrayList(Options.CREATOR);
        this.mediaUrl = in.readString();
    }

    public static final Creator<QItems> CREATOR = new Creator<QItems>() {
        @Override
        public QItems createFromParcel(Parcel source) {
            return new QItems(source);
        }

        @Override
        public QItems[] newArray(int size) {
            return new QItems[size];
        }
    };
}
