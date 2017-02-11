package com.hxqc.carcompare.model.comparebasic;

import java.util.List;

/**
 * 基本参数对比
 * Created by zhaofan on 2016/10/8.
 */
public class CompareParm {
    public String brand;

    public String model;

    public String series;

    public String extID;

    public String shopCount;

    public List<CompareGroupParm> parameters;

    public CompareParm(String brand, String model, String series, String shopCount, List<CompareGroupParm> parameters) {
        this.brand = brand;
        this.model = model;
        this.series = series;
        this.shopCount = shopCount;
        this.parameters = parameters;
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

    public String getExtID() {
        return extID;
    }

    public void setExtID(String extID) {
        this.extID = extID;
    }

    public String getShopCount() {
        return shopCount;
    }

    public void setShopCount(String shopCount) {
        this.shopCount = shopCount;
    }

    public List<CompareGroupParm> getParameters() {
        return parameters;
    }

    public void setParameters(List<CompareGroupParm> parameters) {
        this.parameters = parameters;
    }
}
