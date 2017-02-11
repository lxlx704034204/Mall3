package com.hxqc.autonews.model.pojos;

import com.google.gson.annotations.Expose;

/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 车型或车系信息（汽车资讯部分）
 */
public class AutoInfoData {
    @Expose
    public String dataType = "";//20车系 10车型
    @Expose
    public String name;
    @Expose
    public String thumb;
    @Expose
    public String itemOrigPrice;
    @Expose
    public String seriesID;
    @Expose
    public String seriesName;
    @Expose
    public String autoModelID;
    @Expose
    public String modelName;
    @Expose
    public String brandName;
    @Expose
    public String brandID;
    @Expose
    public String extID;
    @Expose
    public String truebrand;

    public boolean isAutoSerise() {
        return dataType.equals("10");
    }
}
