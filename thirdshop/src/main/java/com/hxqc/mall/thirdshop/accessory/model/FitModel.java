package com.hxqc.mall.thirdshop.accessory.model;

import java.util.ArrayList;

/**
 * 适配车系
 * Created by huangyi on 16/2/25.
 */
public class FitModel {

    public String seriesName; //车系名字
    public String seriesID; //车系id

    public ArrayList<Model> model;

    public FitModel(String seriesName, String seriesID, ArrayList<Model> model) {
        this.seriesName = seriesName;
        this.seriesID = seriesID;
        this.model = model;
    }
}
