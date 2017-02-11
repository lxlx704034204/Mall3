package com.hxqc.mall.thirdshop.model.newcar;


import java.util.List;

/**
 * Created by CPR193 on 2016/10/26.
 */
public class UserGrade {

    private int count;
    private GradeScore grade;
    private List<UserDiscussDetail> userGradeComment;


    public UserGrade(int count, GradeScore grade, List<UserDiscussDetail> userGradeComment) {
        this.count = count;
        this.grade = grade;
        this.userGradeComment = userGradeComment;
    }


    public GradeScore getGrade() {
        return grade;
    }

    public void setGrade(GradeScore grade) {
        this.grade = grade;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<UserDiscussDetail> getUserGradeComment() {
        return userGradeComment;
    }

    public void setUserGradeComment(List<UserDiscussDetail> userGradeComment) {
        this.userGradeComment = userGradeComment;
    }



}
