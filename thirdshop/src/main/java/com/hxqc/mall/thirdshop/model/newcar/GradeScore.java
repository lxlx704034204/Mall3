package com.hxqc.mall.thirdshop.model.newcar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 口碑评分
 * Created by huangyi on 16/10/24.
 */
public class GradeScore implements Parcelable {

    public static final Creator<GradeScore> CREATOR = new Creator<GradeScore>() {
        @Override
        public GradeScore createFromParcel(Parcel in) {
            return new GradeScore(in);
        }

        @Override
        public GradeScore[] newArray(int size) {
            return new GradeScore[size];
        }
    };

    public String comfort; //舒适性
    public String space; //空间
    public String power; //动力
    public String fuelConsumption; //油耗
    public String appearance; //外观
    public String interiorTrimming; //内饰
    public String average; //平均分

    protected GradeScore(Parcel in) {
        comfort = in.readString();
        space = in.readString();
        power = in.readString();
        fuelConsumption = in.readString();
        appearance = in.readString();
        interiorTrimming = in.readString();
        average = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comfort);
        dest.writeString(space);
        dest.writeString(power);
        dest.writeString(fuelConsumption);
        dest.writeString(appearance);
        dest.writeString(interiorTrimming);
        dest.writeString(average);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
