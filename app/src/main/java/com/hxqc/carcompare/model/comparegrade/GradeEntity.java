package com.hxqc.carcompare.model.comparegrade;

/**
 * 评分对比
 * Created by zhaofan on 2016/10/24.
 */
public class GradeEntity {

    public String label;

    public String value;

    public GradeEntity(String label, String value) {
        this.label = label;
        this.value = value;
    }

/* public String comfort;  //舒适性

    public String space;  //空间

    public String power;  //动力

    public String fuelConsumption;  // 油耗

    public String appearance;   //外观

    public String interiorTrimming; //内饰

    public String average;  //平均分

    public GradeEntity(String comfort, String space, String power, String fuelConsumption, String appearance, String interiorTrimming, String average) {
        this.comfort = comfort;
        this.space = space;
        this.power = power;
        this.fuelConsumption = fuelConsumption;
        this.appearance = appearance;
        this.interiorTrimming = interiorTrimming;
        this.average = average;
    }

    @Override
    public String toString() {
        return "GradeEntity{" +
                "comfort='" + comfort + '\'' +
                ", space='" + space + '\'' +
                ", power='" + power + '\'' +
                ", fuelConsumption='" + fuelConsumption + '\'' +
                ", appearance='" + appearance + '\'' +
                ", interiorTrimming='" + interiorTrimming + '\'' +
                ", average='" + average + '\'' +
                '}';
    }*/
}
