package com.hxqc.mall.thirdshop.model.newcar;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 用户评价分数
 * Created by huangyi on 16/10/24.
 */
public class UserGradeComment implements Parcelable {

    public static final Creator<UserGradeComment> CREATOR = new Creator<UserGradeComment>() {
        @Override
        public UserGradeComment createFromParcel(Parcel in) {
            return new UserGradeComment(in);
        }

        @Override
        public UserGradeComment[] newArray(int size) {
            return new UserGradeComment[size];
        }
    };

    public int status; //状态 10:待审核 20:审核通过 30:审核不通过
    public String statusText; //状态
    public String gradeID; //评分id
    public GradeScore grade;
    public List<String> images; //图片列表
    public String content; //评价内容
    public String time; //评价时间
    public UserInfo userInfo;
    public Auto auto;

    public UserGradeComment(UserInfo userInfo, String content, String time, List<String> images, GradeScore grade) {
        this.userInfo = userInfo;
        this.content = content;
        this.time = time;
        this.images = images;
        this.grade = grade;
    }

    protected UserGradeComment(Parcel in) {
        status = in.readInt();
        statusText = in.readString();
        gradeID = in.readString();
        grade = in.readParcelable(GradeScore.class.getClassLoader());
        images = in.createStringArrayList();
        content = in.readString();
        time = in.readString();
        userInfo = in.readParcelable(UserInfo.class.getClassLoader());
        auto = in.readParcelable(Auto.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeString(statusText);
        dest.writeString(gradeID);
        dest.writeParcelable(grade, flags);
        dest.writeStringList(images);
        dest.writeString(content);
        dest.writeString(time);
        dest.writeParcelable(userInfo, flags);
        dest.writeParcelable(auto, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
