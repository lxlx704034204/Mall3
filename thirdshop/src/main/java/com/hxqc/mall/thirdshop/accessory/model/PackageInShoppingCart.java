package com.hxqc.mall.thirdshop.accessory.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:购物车中的套餐信息，用于提交
 *
 * @author: 吕飞
 * @since: 2016-03-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class PackageInShoppingCart {
    @Expose
    String packageID;
    @Expose
    String productIDList;

    public PackageInShoppingCart(int position,ArrayList<ProductInfo> productInfos) {
        packageID = productInfos.get(position).packageID;
        productIDList = getProductIDList(position,productInfos);
    }
    public String getProductIDList(int position,ArrayList<ProductInfo> productInfos) {
        ArrayList<String> productIDList = new ArrayList<>();
        for (int i = position; i < productInfos.size(); i++) {
            productIDList.add(productInfos.get(i).productID);
            if (productInfos.get(i).isLastInPackage) {
                break;
            }
        }
        return productIDList.toString().replace("[", "").replace("]", "").replace(" ","");
    }
}
