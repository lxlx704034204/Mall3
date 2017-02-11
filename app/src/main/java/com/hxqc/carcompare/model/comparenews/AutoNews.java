package com.hxqc.carcompare.model.comparenews;

import java.util.List;

/**
 * 资讯对比
 * Created by zhaofan on 2016/10/24.
 */
public class AutoNews {

    public String brand;

    public String model;

    public String series;

    public String shopCount;

    public List<NewsEntity> autoNews;


    public AutoNews(String brand, String model, String series, String shopCount, List<NewsEntity> autoNews) {
        this.brand = brand;
        this.model = model;
        this.series = series;
        this.shopCount = shopCount;
        this.autoNews = autoNews;
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

    public List<NewsEntity> getAutoNews() {
        return autoNews;
    }

    public void setAutoNews(List<NewsEntity> autoNews) {
        this.autoNews = autoNews;
    }


    @Override
    public String toString() {
        return "AutoNews{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", series='" + series + '\'' +
                ", shopCount='" + shopCount + '\'' +
                ", autoNews=" + autoNews +
                '}';
    }
}
