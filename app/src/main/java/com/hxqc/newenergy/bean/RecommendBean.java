package com.hxqc.newenergy.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by CPR193 on 2016/3/23.
 */
public class RecommendBean implements Parcelable{

    public  String title=null;
    public  String recommendType=null;
    public ArrayList<EVNewenergyAutoSample>recommend;


    protected RecommendBean(Parcel in) {
        title = in.readString();
        recommendType = in.readString();
        recommend = in.createTypedArrayList(EVNewenergyAutoSample.CREATOR);
    }

    public static final Creator<RecommendBean> CREATOR = new Creator<RecommendBean>() {
        @Override
        public RecommendBean createFromParcel(Parcel in) {
            return new RecommendBean(in);
        }

        @Override
        public RecommendBean[] newArray(int size) {
            return new RecommendBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(recommendType);
        dest.writeTypedList(recommend);
    }
}
