package com.hxqc.autonews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 口碑评分
 * Created by huangyi on 16/10/24.
 */
public class Grade implements Parcelable {

    public static final Creator<Grade> CREATOR = new Creator<Grade>() {
        @Override
        public Grade createFromParcel(Parcel in) {
            return new Grade(in);
        }

        @Override
        public Grade[] newArray(int size) {
            return new Grade[size];
        }
    };

    public double comfort; //舒适性
    public double space; //空间
    public double power; //动力
    public double fuelConsumption; //油耗
    public double appearance; //外观
    public double interiorTrimming; //内饰
    public double average; //平均分

    protected Grade(Parcel in) {
        comfort = in.readDouble();
        space = in.readDouble();
        power = in.readDouble();
        fuelConsumption = in.readDouble();
        appearance = in.readDouble();
        interiorTrimming = in.readDouble();
        average = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(comfort);
        dest.writeDouble(space);
        dest.writeDouble(power);
        dest.writeDouble(fuelConsumption);
        dest.writeDouble(appearance);
        dest.writeDouble(interiorTrimming);
        dest.writeDouble(average);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
