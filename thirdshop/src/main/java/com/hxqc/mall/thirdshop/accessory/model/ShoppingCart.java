package com.hxqc.mall.thirdshop.accessory.model;

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
public class ShoppingCart {
    @Expose
    public ArrayList<ShoppingCartItem> productList;
    @Expose
    public ArrayList<ShoppingCartItem> invalidList = new ArrayList<>();
    public int count = 0;//总数
    public int shopNum = 0;//店铺数量
    public float amount = 0f;//总额
    public ArrayList<ProductInfo> mProductInfos = new ArrayList<>();

    public void initProductInfos() {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).isPackage.equals("1")) {
                for (int k = 0; k < productList.get(i).packageInfo.productList.size(); k++) {
                    productList.get(i).packageInfo.productList.get(k).isInPackage = true;
                    productList.get(i).packageInfo.productList.get(k).packageNum = productList.get(i).packageInfo.packageNum;
                    productList.get(i).packageInfo.productList.get(k).packageID = productList.get(i).packageInfo.packageID;
                    productList.get(i).packageInfo.productList.get(k).packageName = productList.get(i).packageInfo.packageName;
                    productList.get(i).packageInfo.productList.get(k).comboPrice = productList.get(i).packageInfo.comboPrice;
                    if (k == 0) {
                        productList.get(i).packageInfo.productList.get(k).isFirstInPackage = true;
                    }
                    if (k == productList.get(i).packageInfo.productList.size() - 1) {
                        productList.get(i).packageInfo.productList.get(k).isLastInPackage = true;
                    }
                    mProductInfos.add(productList.get(i).packageInfo.productList.get(k));
                }
            } else {
                mProductInfos.add(productList.get(i).productInfo);
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
        ArrayList<PackageInShoppingCart> packageInShoppingCarts = new ArrayList<>();
        ArrayList<String> packageNumList = new ArrayList<>();
        for (int i = 0; i < mProductInfos.size(); i++) {
            if (mProductInfos.get(i).isInvalid) {
                break;
            }
            if (!mProductInfos.get(i).isInPackage) {
                productIDList.add(mProductInfos.get(i).productID);
                productNumList.add(mProductInfos.get(i).productNum);
            } else if (mProductInfos.get(i).isFirstInPackage) {
                packageInShoppingCarts.add(new PackageInShoppingCart(i, mProductInfos));
                packageNumList.add(mProductInfos.get(i).packageNum);
            }
        }
        IDNumList[0] = productIDList.toString().replace("[", "").replace("]", "").replace(" ", "");
        IDNumList[1] = productNumList.toString().replace("[", "").replace("]", "").replace(" ", "");
        IDNumList[2] = JSONUtils.toJson(packageInShoppingCarts).replace(" ", "");
        IDNumList[3] = packageNumList.toString().replace("[", "").replace("]", "").replace(" ", "");
        return IDNumList;
    }

    //获取提交类
    public ArrayList<ShoppingCartSubmitObject> getSubmitObjects() {
        ArrayList<ShoppingCartSubmitObject> submitObjects = new ArrayList<>();
        if (!isAllUnSelected()) {
            for (int i = 0; i < mProductInfos.size(); i++) {
                if (mProductInfos.get(i).isSelected) {
                    if (submitObjects.size() == 0 || !mProductInfos.get(i).shopID.equals(submitObjects.get(submitObjects.size() - 1).shopID)) {
                        ShoppingCartSubmitObject submitObject = new ShoppingCartSubmitObject();
                        submitObject.shopID = mProductInfos.get(i).shopID;
                        submitObject.shopName = mProductInfos.get(i).shopName;
                        submitObject.itemList = new ArrayList<>();
                        SubmitProduct submitProduct = new SubmitProduct();
                        if (mProductInfos.get(i).isInPackage) {
                            submitProduct.model = "";
                            submitProduct.itemNum = mProductInfos.get(i).packageNum;
                            submitProduct.isPackage = "1";
                            submitProduct.itemID = mProductInfos.get(i).packageID;
                            submitProduct.idList = mProductInfos.get(i).productID + ",";
                        } else {
                            submitProduct.itemNum = mProductInfos.get(i).productNum;
                            submitProduct.isPackage = "0";
                            submitProduct.itemID = mProductInfos.get(i).productID;
                            submitProduct.idList = "";
                            submitProduct.model = mProductInfos.get(i).model;
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
                            SubmitProduct submitProduct = new SubmitProduct();
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

    //数量
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
                            packageNum = Integer.parseInt(mProductInfos.get(i).packageNum);
                            try {
                                tempAmount = tempAmount + Integer.parseInt(mProductInfos.get(i).packageNum) * Float.parseFloat(mProductInfos.get(i).comboPrice);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        tempCount = tempCount + Integer.parseInt(mProductInfos.get(i).productNum) * packageNum;
                    } else {
                        tempCount = tempCount + Integer.parseInt(mProductInfos.get(i).productNum);
                        tempAmount = tempAmount + Integer.parseInt(mProductInfos.get(i).productNum) * Float.parseFloat(mProductInfos.get(i).price);
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
        ArrayList<ProductInfo> deleteList = new ArrayList<>();
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
        ArrayList<PackageInShoppingCart> packageInShoppingCarts = new ArrayList<>();
        for (int i = 0; i < mProductInfos.size(); i++) {
            if (mProductInfos.get(i).isInvalid) {
                break;
            } else {
                if (mProductInfos.get(i).isSelected && mProductInfos.get(i).isFirstInPackage) {
                    packageInShoppingCarts.add(new PackageInShoppingCart(i, mProductInfos));
                }
            }
        }
        return JSONUtils.toJson(packageInShoppingCarts).replace(" ", "");
    }
}
