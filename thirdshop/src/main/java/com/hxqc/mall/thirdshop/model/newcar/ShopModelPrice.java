package com.hxqc.mall.thirdshop.model.newcar;

import java.util.List;

/**
 * Created by zhaofan
 */
public class ShopModelPrice {


      public List<ModelInfo> modelList;

    public List<ShopInfoPriceBean> shopInfoPrice;


    public List<ShopInfoPriceBean> getShopInfoPrice() {
        return shopInfoPrice;
    }

    public void setShopInfoPrice(List<ShopInfoPriceBean> shopInfoPrice) {
        this.shopInfoPrice = shopInfoPrice;
    }


    public List<ModelInfo> getModelList() {
        return modelList;
    }

    public void setModelList(List<ModelInfo> modelList) {
        this.modelList = modelList;
    }

    public class ShopInfoPriceBean {
        public String brand;
        public String brandID;
        public String shopID;
        public String shopLogoThumb;
        public String shopName;
        public String shopTitle;
        public String shopTel;
        public String rescueTel;
        public String shopInstruction;
        public ShopLocationBean shopLocation;
        public String modelPrice;
        public String itemID;


        public ShopLocationBean getShopLocation() {
            return shopLocation;
        }

        public void setShopLocation(ShopLocationBean shopLocation) {
            this.shopLocation = shopLocation;
        }

        public class ShopLocationBean {
            public String name;
            public String address;
            public double latitude;
            public double longitude;
            public String tel;


        }
    }

}
