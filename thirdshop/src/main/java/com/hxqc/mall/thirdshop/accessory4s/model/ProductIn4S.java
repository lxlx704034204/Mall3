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
public class ProductIn4S {
    @Expose
    public String isPackage;
    @Expose
    public PackageInfo4S packageInfo;
    @Expose
    public ProductInfo4S productInfo;
    public ArrayList<ProductInfo4S> getProductInfos() {
        ArrayList<ProductInfo4S> productInfos = new ArrayList<>();
        if (isPackage.equals("1")) {
            for (int i = 0; i < packageInfo.productList.size(); i++) {
                packageInfo.productList.get(i).isInPackage = true;
                packageInfo.productList.get(i).packageNum = packageInfo.packageNum;
                packageInfo.productList.get(i).packageID = packageInfo.packageID;
                packageInfo.productList.get(i).packageName = packageInfo.packageName;
                packageInfo.productList.get(i).comboPrice = packageInfo.comboPrice;
                if (i == 0) {
                    packageInfo.productList.get(i).isFirstInPackage = true;
                } else if (i == packageInfo.productList.size() - 1) {
                    packageInfo.productList.get(i).isLastInPackage = true;
                }
                productInfos.add(packageInfo.productList.get(i));
            }
        }else {
            productInfos.add(productInfo);
        }
        return productInfos;
    }
}
