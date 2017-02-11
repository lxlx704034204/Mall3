package com.hxqc.mall.thirdshop.model.newcar;

import com.hxqc.mall.thirdshop.model.ColorInfo;
import com.hxqc.mall.thirdshop.model.PriceInfo;

import java.util.List;

/**
 * Created by zhaofan
 */
public class ModelInfo {
    private String brand;   //品牌 string
    private String itemPic;//车辆缩略图 string
    private String seriesName; //车系名称 string
    private String modelName; //车型名称 string
    public List<ParameterInfo> parameter;
    private String introduce;  //图文介绍 url string
    private PriceInfo priceInfo;
    private String priceRange;  //经销商参考价 string
    private String itemOrigPrice; //厂家指导价 string
    private String brandID;  //品牌ID string
    private List<ColorInfo> appearance; //车身颜色
    public String extID;//车型ID string
    public String tag;//分组标签

    public ModelInfo(String brand, String modelName, String extID, String tag) {
        this.brand = brand;
        this.modelName = modelName;
        this.extID = extID;
        this.tag = tag;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getItemPic() {
        return itemPic;
    }

    public void setItemPic(String itemPic) {
        this.itemPic = itemPic;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }



    /*    public List<ParameterInfo> getParameter() {
        return parameter;
    }

    public void setParameter(List<ParameterInfo> parameter) {
        this.parameter = parameter;
    }*/

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getExtID() {
        return extID;
    }

    public void setExtID(String extID) {
        this.extID = extID;
    }

    public PriceInfo getPriceInfo() {
        return priceInfo;
    }

    public void setPriceInfo(PriceInfo priceInfo) {
        this.priceInfo = priceInfo;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getItemOrigPrice() {
        return itemOrigPrice;
    }

    public void setItemOrigPrice(String itemOrigPrice) {
        this.itemOrigPrice = itemOrigPrice;
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public List<ColorInfo> getAppearance() {
        return appearance;
    }

    public void setAppearance(List<ColorInfo> appearance) {
        this.appearance = appearance;
    }


}
