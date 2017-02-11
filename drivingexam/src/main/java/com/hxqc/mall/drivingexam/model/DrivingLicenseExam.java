package com.hxqc.mall.drivingexam.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zhaofan on 2016/8/2.
 */
public class DrivingLicenseExam implements Parcelable {
    public List<String> allQuestionID;   //全部题目id
    public List<QItems> QItems;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.allQuestionID);
        dest.writeTypedList(this.QItems);
    }

    public DrivingLicenseExam() {
    }

    protected DrivingLicenseExam(Parcel in) {
        this.allQuestionID = in.createStringArrayList();
        this.QItems = in.createTypedArrayList(com.hxqc.mall.drivingexam.model.QItems.CREATOR);
    }

    public static final Creator<DrivingLicenseExam> CREATOR = new Creator<DrivingLicenseExam>() {
        @Override
        public DrivingLicenseExam createFromParcel(Parcel source) {
            return new DrivingLicenseExam(source);
        }

        @Override
        public DrivingLicenseExam[] newArray(int size) {
            return new DrivingLicenseExam[size];
        }
    };
}
