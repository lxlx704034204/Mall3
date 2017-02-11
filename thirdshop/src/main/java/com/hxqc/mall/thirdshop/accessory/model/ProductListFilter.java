package com.hxqc.mall.thirdshop.accessory.model;

import com.hxqc.mall.thirdshop.accessory4s.model.ShopList;

import java.util.ArrayList;

/**
 * 用品销售 筛选条件
 * Created by huangyi on 16/5/30.
 */
public class ProductListFilter {

    public ArrayList<BrandGroup> brandSeries; //品牌 ---> 旧版用
    public ArrayList<BrandGroup> brandList; //品牌 ---> 新版用
    public ArrayList<ShopList> shopList; //店铺
    public ArrayList<AccessoryBigCategory> classification; //品类
}
