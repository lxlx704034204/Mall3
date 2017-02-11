package com.hxqc.mall.thirdshop.maintenance.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:李烽
 * Date:2016-02-19
 * FIXME
 * Todo 接待顾问
 */
public class ServiceAdviser implements Parcelable {

    /*name:姓名 string
    avatar:头像 string
    serviceAdviserID:服务顾问ID string
    station:岗位名称 string
    info:个人介绍 string
    logos:工作格言 string
    skill:工作特长 string
    experience:工作经验 string*/

    public String avatar;//头像
    public String name;//姓名
    public String station;//职位
    public String info;//个人介绍
    public String logos;//格言
    public String skill;//特长
    public String experience;//经验
    public String serviceAdviserID;//id
    public String mobile;//电话

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeString(this.name);
        dest.writeString(this.station);
        dest.writeString(this.info);
        dest.writeString(this.logos);
        dest.writeString(this.skill);
        dest.writeString(this.experience);
        dest.writeString(this.serviceAdviserID);
        dest.writeString(this.mobile);
    }

    public ServiceAdviser() {
    }

    protected ServiceAdviser(Parcel in) {
        this.avatar = in.readString();
        this.name = in.readString();
        this.station = in.readString();
        this.info = in.readString();
        this.logos = in.readString();
        this.skill = in.readString();
        this.experience = in.readString();
        this.serviceAdviserID = in.readString();
        this.mobile = in.readString();
    }

    public static final Parcelable.Creator<ServiceAdviser> CREATOR = new Parcelable.Creator<ServiceAdviser>() {
        public ServiceAdviser createFromParcel(Parcel source) {
            return new ServiceAdviser(source);
        }

        public ServiceAdviser[] newArray(int size) {
            return new ServiceAdviser[size];
        }
    };
}
