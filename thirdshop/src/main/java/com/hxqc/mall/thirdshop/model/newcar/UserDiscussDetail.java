package com.hxqc.mall.thirdshop.model.newcar;

import java.util.List;

public class UserDiscussDetail {
    public String status;//状态 code

    public String statusText;// 状态 string

    public String gradeID;// 评分ID string

    public List<String> images;//    图片列表[]

    public String content;//  评价内容 string

    public String time;// 评价时间 string

    public UserInfo userInfo;

    public GradeScore grade;


    public UserDiscussDetail(List<String> images, String content, String time, UserInfo userInfo, GradeScore grade) {
        this.images = images;
        this.content = content;
        this.time = time;
        this.userInfo = userInfo;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "UserDiscussDetail{" +
                "status='" + status + '\'' +
                ", statusText='" + statusText + '\'' +
                ", gradeID='" + gradeID + '\'' +
                ", images=" + images +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", userInfo=" + userInfo +
                ", grade=" + grade +
                '}';
    }
}