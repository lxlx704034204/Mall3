package com.hxqc.carcompare.model.comparegrade;

import java.util.List;

/**
 * 评分对比
 * Created by zhaofan on 2016/10/24.
 */
public class CarGrade {

    public String brand;

    public String model;

    public String series;

    public String shopCount;

    public List<GradeEntity> grade;

    public CarGrade(String brand, String model, String series, String shopCount, List<GradeEntity> autoNews) {
        this.brand = brand;
        this.model = model;
        this.series = series;
        this.shopCount = shopCount;
        this.grade = autoNews;
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

    public List<GradeEntity> getAutoNews() {
        return grade;
    }

    public void setAutoNews(List<GradeEntity> autoNews) {
        this.grade = autoNews;
    }

    @Override
    public String toString() {
        return "CarGrade{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", series='" + series + '\'' +
                ", shopCount='" + shopCount + '\'' +
                ", autoNews=" + grade +
                '}';
    }
}
