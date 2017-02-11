package com.hxqc.carcompare.model.comparedisc;

import com.hxqc.mall.thirdshop.model.newcar.UserDiscussDetail;

import java.util.List;

/**
 * 评论对比
 * Created by zhaofan on 2016/10/24.
 */
public class Discuss {

    public String brand;

    public String model;

    public String series;

    public String shopCount;

    public String userGradeCount; //  用户评价数量 number


    public List<UserDiscussDetail> userGrade;

    public Discuss(String brand, String model, String series, String shopCount, String userGradeCount, List<UserDiscussDetail> userGrade) {
        this.brand = brand;
        this.model = model;
        this.series = series;
        this.shopCount = shopCount;
        this.userGradeCount = userGradeCount;
        this.userGrade = userGrade;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getShopCount() {
        return shopCount;
    }

    public void setShopCount(String shopCount) {
        this.shopCount = shopCount;
    }

    public String getUserGradeCount() {
        return userGradeCount;
    }

    public void setUserGradeCount(String userGradeCount) {
        this.userGradeCount = userGradeCount;
    }

    public List<UserDiscussDetail> getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(List<UserDiscussDetail> userGrade) {
        this.userGrade = userGrade;
    }

    @Override
    public String toString() {
        return "Discuss{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", series='" + series + '\'' +
                ", shopCount='" + shopCount + '\'' +
                ", userGradeCount='" + userGradeCount + '\'' +
                ", autoNews=" + userGrade +
                '}';
    }
}
