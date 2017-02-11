package com.hxqc.mall.thirdshop.model.newcar;

import com.hxqc.mall.thirdshop.model.ShopInfo;

import java.util.List;

/**
 * Created by zhaofan
 */
public class SeriesIntroduce {
    public SeriesInfo seriesInfo;  //车系信息
    public List<ModelListInfo> modelList;//车型列表
    public List<ShopInfo> shopList; //店铺列表
    public String shopSiteFrom;  //店铺来源 10本地站点 20 周边站点 number
}
