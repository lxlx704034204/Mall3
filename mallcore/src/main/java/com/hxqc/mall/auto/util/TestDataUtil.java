package com.hxqc.mall.auto.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 15
 * Des: 测试数据
 * FIXME
 * Todo
 */
public class TestDataUtil {

    private static final String TAG = "TestDataUtil";

    public static void testMyAutoData() {

        List<MyAuto> myAuto = new ArrayList<MyAuto>();

    }

    /**
     * @return
     */
    public static ArrayList<AutoSeriesGroup> testAutoSeries() {
        ArrayList<AutoSeriesGroup> autoSeriesGroups = new ArrayList<AutoSeriesGroup>();
        String[] strings = {"奥迪","奥迪RS","一汽-大众奥迪"};
        String[] strings1 = {"北京奔驰","奔驰(进口)","福建奔驰","梅塞德斯-AMG","梅塞德斯-迈巴赫","东风本田","广汽本田","别克(进口)","上汽通用别克"};

        ArrayList<Series> seriesArrayList = new ArrayList<Series>();
        ArrayList<Series> seriesArrayList1 = new ArrayList<Series>();

        for(int i = 0;i<strings.length;i++) {
            Series series = new Series();
            series.seriesName = strings[i];
            seriesArrayList.add(series);
        }

        AutoSeriesGroup autoSeriesGroup = new AutoSeriesGroup();
        autoSeriesGroup.groupTag = "A";
        autoSeriesGroup.group = seriesArrayList;
        autoSeriesGroups.add(autoSeriesGroup);

        for(int i = 0;i<strings1.length;i++) {
            Series series = new Series();
            series.seriesName = strings1[i];
            seriesArrayList1.add(series);
        }

        AutoSeriesGroup autoSeriesGroup1 = new AutoSeriesGroup();
        autoSeriesGroup1.groupTag = "B";
        autoSeriesGroup1.group = seriesArrayList1;
        autoSeriesGroups.add(autoSeriesGroup1);
        return autoSeriesGroups;
    }

    public static class AutoSeriesGroup{
        public String groupTag;
        public ArrayList< Series > group;
    }

    public static class Series implements Parcelable {
        public final Creator<Series> CREATOR = new Creator<Series>() {
            public Series createFromParcel(Parcel source) {
                return new Series(source);
            }

            public Series[] newArray(int size) {
                return new Series[size];
            }
        };
        public String seriesID;//车系ID
        public String seriesName;//车系名称
        public String seriesThumb;//车系图标url
        public String priceRange;//价格区间

        public Series() {
        }

        protected Series(Parcel in) {
            this.seriesID = in.readString();
            this.seriesName = in.readString();
            this.seriesThumb = in.readString();
            this.priceRange = in.readString();
        }

        public String getPriceRange() {
            return OtherUtil.formatPriceRange(priceRange);
        }

        public String getSeriesName() {
            return seriesName;
        }

        public String getSeriesThumb() {
            return seriesThumb;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.seriesID);
            dest.writeString(this.seriesName);
            dest.writeString(this.seriesThumb);
            dest.writeString(this.priceRange);
        }
    }


}
