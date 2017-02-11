package com.hxqc.mall.thirdshop.accessory4s.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-08-04
 * Copyright:恒信汽车电子商务有限公司
 */
public class ShopInShoppingCart {
    @Expose
    public ArrayList<ProductIn4S> productsInShop;
    @Expose
    public String shopID;//店铺id
    @Expose
    public String shopTitle;
}
