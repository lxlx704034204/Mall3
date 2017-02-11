package com.hxqc.mall.thirdshop.accessory4s.model;

import com.google.gson.annotations.Expose;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 说明:购物车
 *
 * @author: 吕飞
 * @since: 2016-02-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class ShoppingCart4S {
    @Expose
    public ArrayList<ShopInShoppingCart> productList= new ArrayList<>();
    @Expose
    public ArrayList<ProductIn4S> invalidList = new ArrayList<>();
    public int count = 0;//总数
    public int shopNum = 0;//店铺数量
    public float amount = 0f;//总额
    public ArrayList<ProductInfo4S> mProductInfos = new ArrayList<>();

    public void initProductInfos() {
        for (int i = 0; i < productList.size(); i++) {
            for (int j = 0; j < productList.get(i).productsInShop.size(); j++) {
                if (productList.get(i).productsInShop.get(j).isPackage.equals("1")) {
                    for (int k = 0; k < productList.get(i).productsInShop.get(j).packageInfo.productList.size(); k++) {
                        productList.get(i).productsInShop.get(j).packageInfo.productList.get(k).packageID = productList.get(i).productsInShop.get(j).packageInfo.packageID;
                        productList.get(i).productsInShop.get(j).packageInfo.productList.get(k).isInPackage = true;
                        productList.get(i).productsInShop.get(j).packageInfo.productList.get(k).comboPrice = productList.get(i).productsInShop.get(j).packageInfo.comboPrice;
                        productList.get(i).productsInShop.get(j).packageInfo.productList.get(k).packageNum = productList.get(i).productsInShop.get(j).packageInfo.packageNum;
                        productList.get(i).productsInShop.get(j).packageInfo.productList.get(k).shopID = productList.get(i).shopID;
                        productList.get(i).productsInShop.get(j).packageInfo.productList.get(k).shopTitle = productList.get(i).shopTitle;
                        if (k == 0) {
                            productList.get(i).productsInShop.get(j).packageInfo.productList.get(k).isFirstInPackage = true;
                            if (j == 0) {
                                productList.get(i).productsInShop.get(j).packageInfo.productList.get(k).isFirstInShop = true;
                            }
                        }
                        if (k == productList.get(i).productsInShop.get(j).packageInfo.productList.size() - 1) {
                            productList.get(i).productsInShop.get(j).packageInfo.productList.get(k).isLastInPackage = true;
                            if (j == productList.get(i).productsInShop.size() - 1) {
                                productList.get(i).productsInShop.get(j).packageInfo.productList.get(k).isLastInShop = true;
                            }
                        }
                        mProductInfos.add(productList.get(i).productsInShop.get(j).packageInfo.productList.get(k));
                    }
                } else {
                    productList.get(i).productsInShop.get(j).productInfo.shopID = productList.get(i).shopID;
                    productList.get(i).productsInShop.get(j).productInfo.shopTitle = productList.get(i).shopTitle;
                    if (j == 0) {
                        productList.get(i).productsInShop.get(j).productInfo.isFirstInShop = true;
                    }
                    if (j == productList.get(i).productsInShop.size() - 1) {
                        productList.get(i).productsInShop.get(j).productInfo.isLastInShop = true;
                    }
                    mProductInfos.add(productList.get(i).productsInShop.get(j).productInfo);
                }
            }
        }
        for (int i = 0; i < invalidList.size(); i++) {
            if (invalidList.get(i).isPackage.equals("1")) {
                for (int j = 0; j < invalidList.get(i).packageInfo.productList.size(); j++) {
                    invalidList.get(i).packageInfo.productList.get(j).isInPackage = true;
                    invalidList.get(i).packageInfo.productList.get(j).isInvalid = true;
                    if (j == 0) {
                        invalidList.get(i).packageInfo.productList.get(j).isFirstInPackage = true;
                    }
                    if (j == invalidList.get(i).packageInfo.productList.size() - 1) {
                        invalidList.get(i).packageInfo.productList.get(j).isLastInPackage = true;
                    }
                    mProductInfos.add(invalidList.get(i).packageInfo.productList.get(j));
                }
            } else {
                invalidList.get(i).productInfo.isInvalid = true;
                mProductInfos.add(invalidList.get(i).productInfo);
            }
        }
    }


    //    是否全是失效
    public boolean isAllInvalid() {
        return getInvalidNum() == mProductInfos.size() && getInvalidNum() != 0;
    }

    //    失效数量
    public int getInvalidNum() {
        int invalid = 0;
        for (int i = 0; i < mProductInfos.size(); i++) {
            if (mProductInfos.get(i).isInvalid) {
                invalid++;
            }
        }
        return invalid;
    }

    //    是否为空
    public boolean isNull() {
        return !(mProductInfos.size() > 0);
    }

    //    是否全选
    public boolean isAllSelected() {
        int selected = 0;
        for (int i = 0; i < mProductInfos.size(); i++) {
            if (mProductInfos.get(i).isInvalid) {
                break;
            } else if (mProductInfos.get(i).isSelected) {
                selected++;
            }
        }
        return selected == mProductInfos.size() - getInvalidNum();
    }

    //    是否不全选
    public boolean isAllUnSelected() {
        int selected = 0;
        for (int i = 0; i < mProductInfos.size(); i++) {
            if (mProductInfos.get(i).isInvalid) {
                break;
            } else if (mProductInfos.get(i).isSelected) {
                selected++;
            }
        }
        return selected == 0;
    }

    //    设置编辑状态
    public void setEditStatus(boolean edit) {
        for (int i = 0; i < mProductInfos.size(); i++) {
            mProductInfos.get(i).isEdit = edit;
        }
    }

    //    获取productIDList, productNumList, packageIDList, packageNumList
    public String[] getIDNumList() {
        String[] IDNumList = {"", "", "", ""};
        ArrayList<String> productIDList = new ArrayList<>();
        ArrayList<String> productNumList = new ArrayList<>();
        ArrayList<PackageInShoppingCart4S> packageInShoppingCarts = new ArrayList<>();
        ArrayList<String> packageNumList = new ArrayList<>();
        for (int i = 0; i < mProductInfos.size(); i++) {
            if (mProductInfos.get(i).isInvalid) {
                break;
            }
            if (!mProductInfos.get(i).isInPackage) {
                productIDList.add(mProductInfos.get(i).productID);
                productNumList.add(mProductInfos.get(i).productNum + "");
            } else if (mProductInfos.get(i).isFirstInPackage) {
                packageInShoppingCarts.add(new PackageInShoppingCart4S(i, mProductInfos));
                packageNumList.add(mProductInfos.get(i).packageNum + "");
            }
        }
        IDNumList[0] = productIDList.toString().replace("[", "").replace("]", "").replace(" ", "");
        IDNumList[1] = productNumList.toString().replace("[", "").replace("]", "").replace(" ", "");
        IDNumList[2] = JSONUtils.toJson(packageInShoppingCarts).replace(" ", "");
        IDNumList[3] = packageNumList.toString().replace("[", "").replace("]", "").replace(" ", "");
        return IDNumList;
    }

    //获取提交类
    public ArrayList<ShoppingCartSubmitObject4S> getSubmitObjects() {
        ArrayList<ShoppingCartSubmitObject4S> submitObjects = new ArrayList<>();
        if (!isAllUnSelected()) {
            for (int i = 0; i < mProductInfos.size(); i++) {
                if (mProductInfos.get(i).isSelected) {
                    if (submitObjects.size() == 0 || !mProductInfos.get(i).shopID.equals(submitObjects.get(submitObjects.size() - 1).shopID)) {
                        ShoppingCartSubmitObject4S submitObject = new ShoppingCartSubmitObject4S();
                        submitObject.shopID = mProductInfos.get(i).shopID;
                        submitObject.itemList = new ArrayList<>();
                        SubmitProduct4S submitProduct = new SubmitProduct4S();
                        if (mProductInfos.get(i).isInPackage) {
                            submitProduct.itemNum = mProductInfos.get(i).packageNum;
                            submitProduct.isPackage = "1";
                            submitProduct.itemID = mProductInfos.get(i).packageID;
                            submitProduct.idList = mProductInfos.get(i).productID + ",";
                        } else {
                            submitProduct.itemNum = mProductInfos.get(i).productNum;
                            submitProduct.isPackage = "0";
                            submitProduct.itemID = mProductInfos.get(i).productID;
                            submitProduct.idList = "";
                        }
                        submitObject.itemList.add(submitProduct);
                        submitObjects.add(submitObject);
                    } else if (mProductInfos.get(i).shopID.equals(mProductInfos.get(i - 1).shopID)) {
                        if (mProductInfos.get(i).isInPackage && !mProductInfos.get(i).isFirstInPackage) {
                            if (mProductInfos.get(i).isLastInPackage) {
                                submitObjects.get(submitObjects.size() - 1).itemList.get(submitObjects.get(submitObjects.size() - 1).itemList.size() - 1).idList += mProductInfos.get(i).productID;
                            } else {
                                submitObjects.get(submitObjects.size() - 1).itemList.get(submitObjects.get(submitObjects.size() - 1).itemList.size() - 1).idList += mProductInfos.get(i).productID + ",";
                            }
                        } else {
                            SubmitProduct4S submitProduct = new SubmitProduct4S();
                            if (mProductInfos.get(i).isInPackage) {
                                submitProduct.itemNum = mProductInfos.get(i).packageNum;
                                submitProduct.isPackage = "1";
                                submitProduct.itemID = mProductInfos.get(i).packageID;
                                submitProduct.idList = mProductInfos.get(i).productID + ",";
                            } else {
                                submitProduct.itemNum = mProductInfos.get(i).productNum;
                                submitProduct.isPackage = "0";
                                submitProduct.itemID = mProductInfos.get(i).productID;
                                submitProduct.idList = "";
                            }
                            submitObjects.get(submitObjects.size() - 1).itemList.add(submitProduct);
                        }
                    }
                }
            }
        }
        return submitObjects;
    }

    //    获取提交json
    public String getSubmitJson() {
        return JSONUtils.toJson(getSubmitObjects()).replace(" ", "");
    }

    //数量金额
    public void setCountAmount() {
        int packageNum = 1;
        if (!isAllInvalid()) {
            int tempCount = 0;
            ArrayList<String> shopIDs = new ArrayList<>();
            float tempAmount = 0f;
            for (int i = 0; i < mProductInfos.size(); i++) {
                if (mProductInfos.get(i).isInvalid) {
                    break;
                } else if (mProductInfos.get(i).isSelected) {
                    if (shopIDs.size() == 0 || !mProductInfos.get(i).shopID.equals(mProductInfos.get(i - 1).shopID)) {
                        shopIDs.add(mProductInfos.get(i).shopID);
                    }
                    if (mProductInfos.get(i).isInPackage) {
                        if (mProductInfos.get(i).isFirstInPackage) {
                            packageNum = mProductInfos.get(i).packageNum;
                            try {
                                tempAmount = tempAmount + mProductInfos.get(i).packageNum * Float.parseFloat(mProductInfos.get(i).comboPrice);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        tempCount = tempCount + mProductInfos.get(i).productNum * packageNum;
                    } else {
                        tempCount = tempCount + mProductInfos.get(i).productNum;
                        tempAmount = tempAmount + mProductInfos.get(i).productNum * Float.parseFloat(mProductInfos.get(i).price);
                    }
                }
            }
            shopNum = shopIDs.size();
            count = tempCount;
            amount = tempAmount;
        }
    }

    //删除所选
    public void deleteItem() {
        ArrayList<ProductInfo4S> deleteList = new ArrayList<>();
        for (int i = 0; i < mProductInfos.size(); i++) {
            if (mProductInfos.get(i).isInvalid) {
                break;
            } else if (mProductInfos.get(i).isSelected) {
                deleteList.add(mProductInfos.get(i));
            }
        }
        mProductInfos.removeAll(deleteList);
    }

    //全体选择
    public void selectAll(boolean select) {
        for (int i = 0; i < mProductInfos.size(); i++) {
            if (!mProductInfos.get(i).isInvalid) {
                mProductInfos.get(i).isSelected = select;
            }
        }
        if (!select) {
            count = 0;
            amount = 0f;
            shopNum = 0;
        } else {
            setCountAmount();
        }
    }

    //获取选择商品id的列表，不包括套餐内商品
    public String getSelectProductIDList() {
        ArrayList<String> selectProductIDList = new ArrayList<>();
        for (int i = 0; i < mProductInfos.size(); i++) {
            if (mProductInfos.get(i).isInvalid) {
                break;
            } else {
                if (mProductInfos.get(i).isSelected && !mProductInfos.get(i).isInPackage) {
                    selectProductIDList.add(mProductInfos.get(i).productID);
                }
            }
        }
        return selectProductIDList.toString().replace("[", "").replace("]", "").replace(" ", "");
    }

    //获取选择套餐id的列表
    public String getSelectedPackageIDList() {
        ArrayList<PackageInShoppingCart4S> packageInShoppingCarts = new ArrayList<>();
        for (int i = 0; i < mProductInfos.size(); i++) {
            if (mProductInfos.get(i).isInvalid) {
                break;
            } else {
                if (mProductInfos.get(i).isSelected && mProductInfos.get(i).isFirstInPackage) {
                    packageInShoppingCarts.add(new PackageInShoppingCart4S(i, mProductInfos));
                }
            }
        }
        return JSONUtils.toJson(packageInShoppingCarts).replace(" ", "");
    }
}
