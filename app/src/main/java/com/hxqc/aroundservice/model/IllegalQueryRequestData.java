package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 12
 * FIXME
 * Todo
 */
public class IllegalQueryRequestData implements Parcelable{

//    http://apis.haoservice.com/weizhang/query?city=GZ_GuiYang&hphm=贵A19X37&hpzl=02&engineno=89541W&classno=&registno=&key=您申请的APPKEY
    public String cityCode;//城市
    public String hphm;//车牌号
    public String hpzl;//类型号
    public String engineno;
    public String classno;
    public String registno;
    public String handled;
    public String cityName;
    public String provinceName;

    public IllegalQueryRequestData() {
    }


    protected IllegalQueryRequestData(Parcel in) {
        cityCode = in.readString();
        hphm = in.readString();
        hpzl = in.readString();
        engineno = in.readString();
        classno = in.readString();
        registno = in.readString();
        handled = in.readString();
        cityName = in.readString();
        provinceName = in.readString();
    }

    public static final Creator<IllegalQueryRequestData> CREATOR = new Creator<IllegalQueryRequestData>() {
        @Override
        public IllegalQueryRequestData createFromParcel(Parcel in) {
            return new IllegalQueryRequestData(in);
        }

        @Override
        public IllegalQueryRequestData[] newArray(int size) {
            return new IllegalQueryRequestData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityCode);
        dest.writeString(hphm);
        dest.writeString(hpzl);
        dest.writeString(engineno);
        dest.writeString(classno);
        dest.writeString(registno);
        dest.writeString(handled);
        dest.writeString(cityName);
        dest.writeString(provinceName);
    }

    @Override
    public String toString() {
        return "IllegalQueryRequestData{" +
                "cityCode='" + cityCode + '\'' +
                ", hphm='" + hphm + '\'' +
                ", hpzl='" + hpzl + '\'' +
                ", engineno='" + engineno + '\'' +
                ", classno='" + classno + '\'' +
                ", registno='" + registno + '\'' +
                ", handled='" + handled + '\'' +
                ", cityName='" + cityName + '\'' +
                ", provinceName='" + provinceName + '\'' +
                '}';
    }
}
