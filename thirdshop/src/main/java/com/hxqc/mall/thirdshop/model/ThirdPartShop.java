package com.hxqc.mall.thirdshop.model;

import com.hxqc.mall.thirdshop.model.promotion.SalesPModel;
import com.hxqc.socialshare.pojo.ShareContent;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2015-12-02
 * FIXME
 * Todo 第三方店铺
 */
public class ThirdPartShop {
    private ShopInfo shopInfo;
    private ArrayList<SalesPModel> shopPromotion;
    private ArrayList<AutoBaseInfoThirdShop > items;
    private ArrayList<ModelsQuote> series;
    private ArrayList<GoldenSeller> sellers;
    private ShareContent share; //分享

    public ShareContent getShare() {
        return share;
    }

    public void setShare(ShareContent share) {
        this.share = share;
    }

    public ArrayList<SalesPModel> getShopPromotion() {
        return shopPromotion;
    }

    public void setShopPromotion(ArrayList<SalesPModel> shopPromotion) {
        this.shopPromotion = shopPromotion;
    }

    public ArrayList<AutoBaseInfoThirdShop > getItems() {
        return items;
    }

    public void setItems(ArrayList<AutoBaseInfoThirdShop > items) {
        this.items = items;
    }

    public ArrayList<ModelsQuote> getSeries() {
        return series;
    }

    public void setSeries(ArrayList<ModelsQuote> series) {
        this.series = series;
    }

    public ArrayList<GoldenSeller> getSellers() {
        return sellers;
    }

    public void setSellers(ArrayList<GoldenSeller> sellers) {
        this.sellers = sellers;
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }




}
