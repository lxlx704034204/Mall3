package com.hxqc.mall.thirdshop.model;

import com.hxqc.mall.thirdshop.model.newcar.ModelInfo;

import java.util.List;

/**
 * Created by zhaofan on 2016/10/27.
 */
public class ModelByYear {
    public String year;

    public List<ModelInfo> model;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<ModelInfo> getModel() {
        return model;
    }

    public void setModel(List<ModelInfo> model) {
        this.model = model;
    }
}
