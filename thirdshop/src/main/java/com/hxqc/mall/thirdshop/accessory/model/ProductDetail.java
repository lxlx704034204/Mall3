package com.hxqc.mall.thirdshop.accessory.model;

import com.hxqc.mall.thirdshop.accessory4s.model.ShopInfo4S;
import com.hxqc.socialshare.pojo.ShareContent;

import java.util.ArrayList;

/**
 * 商品详情
 * Created by huangyi on 16/2/25.
 */
public class ProductDetail {

    public String cover; //封面
    public int inventory; //库存 -1为已下架
    public String productIntroduce; //商品介绍url

    public ArrayList<Photo> photos;
    public ArrayList<SinglePackage> packages; //优惠套餐数据
    public ArrayList<ProductsForProperty> productsForProperty; //各种属性的商品组
    public ArrayList<ChoseCondition> choseCondition; //已选条件

    public ProductInfo productInfo;
    public ShareContent share;


    //新版 特用
    public String shopName;
    public ShopInfo4S shopInfo;
}
