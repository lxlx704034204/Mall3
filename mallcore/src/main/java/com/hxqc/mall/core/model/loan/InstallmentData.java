package com.hxqc.mall.core.model.loan;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author: liukechong
 * Date: 2015-10-16
 * FIXME 分期资料各种键值对
 * Todo
 */
public class InstallmentData implements Parcelable{
    /**
     * 房屋性质键值对
     */
    public ArrayList<InstallmentDataKeyValue> houseProperty;
    /**
     * 经营行业键值对
     */
    public ArrayList<InstallmentDataKeyValue> industry;
    /**
     * 婚姻状况键值对
     */
    public ArrayList<InstallmentDataKeyValue> marriageStatus;
    /**
     * 性别键值对
     */
    public ArrayList<InstallmentDataKeyValue> sex;

    @Override
    public String toString() {
        return "InstallmentData{" +
                "houseProperty=" + houseProperty +
                ", industry=" + industry +
                ", marriageStatus=" + marriageStatus +
                ", sex=" + sex +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(houseProperty);
        dest.writeTypedList(industry);
        dest.writeTypedList(marriageStatus);
        dest.writeTypedList(sex);
    }

    public InstallmentData() {
    }

    protected InstallmentData(Parcel in) {
        this.houseProperty = in.createTypedArrayList(InstallmentDataKeyValue.CREATOR);
        this.industry = in.createTypedArrayList(InstallmentDataKeyValue.CREATOR);
        this.marriageStatus = in.createTypedArrayList(InstallmentDataKeyValue.CREATOR);
        this.sex = in.createTypedArrayList(InstallmentDataKeyValue.CREATOR);
    }

    public static final Creator<InstallmentData> CREATOR = new Creator<InstallmentData>() {
        public InstallmentData createFromParcel(Parcel source) {
            return new InstallmentData(source);
        }

        public InstallmentData[] newArray(int size) {
            return new InstallmentData[size];
        }
    };
}
