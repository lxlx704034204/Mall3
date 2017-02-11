package com.hxqc.mall.thirdshop.model.newcar;

import com.hxqc.mall.thirdshop.model.ShopInfo;

import java.util.List;

/**
 * Created by zhaofan
 */
public class ModelIntroduce {
    public ModelInfo modelInfo;

    public List<ShopInfo> shopList;
    public String shopSiteFrom;  //店铺来源 10本地站点 20 周边站点 number

    public ModelInfo getModelInfo() {
        return modelInfo;
    }

    public void setModelInfo(ModelInfo modelInfo) {
        this.modelInfo = modelInfo;
    }

    public List<ShopInfo> getShopList() {
        return shopList;
    }

    public void setShopList(List<ShopInfo> shopList) {
        this.shopList = shopList;
    }

    @Override
    public String toString() {
        return "ModelIntroduce{" +
                "modelInfo=" + modelInfo +
                ", shopList=" + shopList +
                '}';
    }
}
