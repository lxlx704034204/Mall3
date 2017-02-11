package com.hxqc.autonews.model.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.hxqc.autonews.model.AutoInformationTyper;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 汽车资讯数据列表单个数据
 */

public class AutoInformation implements Parcelable {
    @Expose
    public String infoID;
    @Expose
    public int form_source;//来源 10资讯 20 4s店
    @Expose
    public String pageType;//页面样式form_source为10的时候判断 10图文混排 20图集
    @Expose
    public String newsType;//新闻类型form_source为20的时候判断 10优惠促销 20新车信息 30新闻资讯 40维修保养 50店内活动 60保养促销
    @Expose
    public String title;
    @Expose
    public String date;
    @Expose
    public String kind;//汽车资讯，导购推荐等等

    @Expose
    public String kindCode;
    @Expose
    public ArrayList<String> thumbImage;
    @Expose
    public List<InfoTag> tags;
    @Expose
    public int isRead;//标记是否已读，1为已读，0为未读

    public AutoInformation() {
    }

    public boolean isRead() {
        return isRead == 1;
    }

    public boolean equals(AutoInformation information) {
        return infoID.equals(information.infoID);
    }

    protected AutoInformation(Parcel in) {
        infoID = in.readString();
        form_source = in.readInt();
        pageType = in.readString();
        newsType = in.readString();
        title = in.readString();
        date = in.readString();
        kind = in.readString();
        kindCode = in.readString();
        tags = in.createTypedArrayList(InfoTag.CREATOR);
        thumbImage = in.createStringArrayList();
    }

    public static final Creator<AutoInformation> CREATOR = new Creator<AutoInformation>() {
        @Override
        public AutoInformation createFromParcel(Parcel in) {
            return new AutoInformation(in);
        }

        @Override
        public AutoInformation[] newArray(int size) {
            return new AutoInformation[size];
        }
    };

    /**
     * 获取类型
     *
     * @return
     */
    public Type getType() {
        return AutoInformationTyper.getType(form_source, pageType, newsType);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(infoID);
        dest.writeInt(form_source);
        dest.writeString(pageType);
        dest.writeString(newsType);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(kind);
        dest.writeString(kindCode);
        dest.writeStringList(thumbImage);
        dest.writeTypedList(tags);
    }

    public enum Type {
        promotion,//优惠促销
        autoInformation, //新车信息
        news, //新闻资讯
        maintenance, //维修保养
        shopActivity,//店内活动
        maintenancePromotion,//保养促销
        textAndImage,//图文
        Images,//图集
        autoCalendar//新车日历

        //。。。待添加
    }
}
