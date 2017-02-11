package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 09
 * FIXME
 * Todo 城市
 */
public class City implements Parcelable {

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
    public String city_name;
    public String city_code;
    public String abbr;
    public String engine;
    public String engineno;
    public String classa;
    public String classno;
    public String regist;
    public String registno;
    public String ChannelType;

    protected City(Parcel in) {
        city_name = in.readString();
        city_code = in.readString();
        abbr = in.readString();
        engine = in.readString();
        engineno = in.readString();
        classa = in.readString();
        classno = in.readString();
        regist = in.readString();
        registno = in.readString();
        ChannelType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city_name);
        dest.writeString(city_code);
        dest.writeString(abbr);
        dest.writeString(engine);
        dest.writeString(engineno);
        dest.writeString(classa);
        dest.writeString(classno);
        dest.writeString(regist);
        dest.writeString(registno);
        dest.writeString(ChannelType);
    }

    @Override
    public String toString() {
        return "City{" +
                "city_name='" + city_name + '\'' +
                ", city_code='" + city_code + '\'' +
                ", abbr='" + abbr + '\'' +
                ", engine='" + engine + '\'' +
                ", engineno='" + engineno + '\'' +
                ", classa='" + classa + '\'' +
                ", classno='" + classno + '\'' +
                ", regist='" + regist + '\'' +
                ", registno='" + registno + '\'' +
                ", ChannelType='" + ChannelType + '\'' +
                '}';
    }
}
