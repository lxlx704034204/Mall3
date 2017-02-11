package com.hxqc.mall.thirdshop.maintenance.control;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceChildGoodsN;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItem;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemGroup;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemN;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ShopQuote;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ShopQuoteItem;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.UpMaintainItemGroupID;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.UpMaintainItemID;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.UploadObject;
import com.hxqc.mall.thirdshop.maintenance.model.order.OrderPrepareN;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * @Author : 钟学东
 * @Since : 2016-04-20
 * FIXME
 * Todo 4s店 和 快修店 保养流程 帮助类
 */
public class FourSAndQuickHelper {


    private static FourSAndQuickHelper instance;
    private static MaintenanceClient client;

    //报价页面所返回的保养项目概述
    private ArrayList<ShopQuote> shopQuotes = new ArrayList<>();
    //符合车型所有保养项目
    private ArrayList<MaintenanceItemGroup> maintenanceItems = new ArrayList<>();
    //选中的保养项目 并用于显示的
    private ArrayList<MaintenanceItemGroup> maintenanceShowItems = new ArrayList<>();
    //用于4s店 首页进入的流程 筛选选中的items用于展示
    private ArrayList<MaintenanceItemGroup> chooseMaintenanceItems = new ArrayList<>();

    //符合车型所有保养项目 第三版的model(保养第四版) 为4s店的保养项目
    private ArrayList<MaintenanceItemGroup> itemsFor4S = new ArrayList<>();
    //平台推荐的保养项目 4s店
    private ArrayList<MaintenanceItemGroup> recommentItemsFor4S = new ArrayList<>();
    //自选的保养项目  4s店
    private ArrayList<MaintenanceItemGroup> optionalItemsFor4S = new ArrayList<>();

    //用于得到itemGroup的上传的参数
    private UploadObject uploadGroupObject = new UploadObject();

    //用于得到package item 的上传的参数
    private ArrayList<UpMaintainItemGroupID> upMaintainItemGroupIDs = new ArrayList<>();

    //准备订单
    private OrderPrepareN orderPrepareN;
    //店铺id
    private String shopID;
    //车
    private MyAuto myAuto;
    //标记符 1 4s店  2 快修店  3 4s店首页  默认值为1
    private String flag = "1";
    //标记符 1 用于4S店保养  2 用于 确认订单 4sHome页面
    private String maintenanceItemGroupFlag;

    private String couponID = "-1"; //优惠卷
    private float score = -1; //积分

    private String TAG = "TAG"; //log
    //记录 互斥的推荐项目
    private ArrayList<Integer> mutexRecommend = new ArrayList<>();
    //记录 互斥的自选项目
    private ArrayList<Integer> mutexOptional = new ArrayList<>();
    //记录 同时选中的推荐项目
    private ArrayList<Integer> bothRecommend = new ArrayList<>();
    //记录 同时选中的自选项目
    private ArrayList<Integer> bothOptional = new ArrayList<>();

    private final static String fakeShopQuote = "fakeShopQuote.txt";
    private final static String fakeItems = "baoyang.txt";
    private final static String fakePrepareN = "fakePrepareN.txt";

    private FourSAndQuickHelper() {
        if (client == null)
            client = new MaintenanceClient();
    }

    public static FourSAndQuickHelper getInstance() {
        if (instance == null)
            synchronized (FourSAndQuickHelper.class) {
                if (instance == null) {
                    instance = new FourSAndQuickHelper();
                }
            }
        return instance;
    }

    /**
     * 销毁
     */
    public void destroy() {
        if (instance != null)
            instance = null;
    }


    /**
     * 获取保养项目概述
     *
     * @param context
     */
    public void getItemsAllOverviewN(final Context context, final ItemsAllOverviewNHandle itemsAllOverviewNHandle) {

        client.itemsAllOverviewN(myAuto.autoModelID, myAuto.seriesID, myAuto.drivingDistance, myAuto.registerTime, new LoadingAnimResponseHandler(context) {
            @Override
            public void onSuccess(String response) {
                ArrayList<ShopQuote> newShopQuotes = JSONUtils.fromJson(response, new TypeToken<ArrayList<ShopQuote>>() {
                });

                ArrayList<ShopQuoteItem> recommendShopQuoteItem = new ArrayList<ShopQuoteItem>();
                ArrayList<ShopQuoteItem> optionalShopQuoteItem = new ArrayList<ShopQuoteItem>();

                if (newShopQuotes == null || newShopQuotes.size() == 0) {
                    itemsAllOverviewNHandle.onEmpty();
                } else {
                    for (int i = 0; i < newShopQuotes.size(); i++) {
                        for (int n = 0; n < newShopQuotes.get(i).items.size(); n++) {
                            if (newShopQuotes.get(i).items.get(n).choose == 1) {
                                newShopQuotes.get(i).items.get(n).isPlatformRecommend = true;
                                recommendShopQuoteItem.add(newShopQuotes.get(i).items.get(n));
                            } else {
                                newShopQuotes.get(i).items.get(n).isPlatformRecommend = false;
                                optionalShopQuoteItem.add(newShopQuotes.get(i).items.get(n));
                            }
                        }
                    }
                    shopQuotes.clear();
                    if (recommendShopQuoteItem.size() > 0) {
                        ShopQuote recommendShopQuote = new ShopQuote();
                        recommendShopQuote.groupTag = "推荐的项目";
                        recommendShopQuote.items = recommendShopQuoteItem;
                        shopQuotes.add(recommendShopQuote);
                    }
                    if (optionalShopQuoteItem.size() > 0) {
                        ShopQuote optionalShopQuote = new ShopQuote();
                        optionalShopQuote.groupTag = "可自行选择的项目";
                        optionalShopQuote.items = optionalShopQuoteItem;
                        shopQuotes.add(optionalShopQuote);
                    }

                    itemsAllOverviewNHandle.onSuccess(newShopQuotes);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                itemsAllOverviewNHandle.onFailure();
            }
        });
    }


    /**
     * 4s 店必选项目
     *
     * @return
     */
    public boolean mustSelect4S() {
        boolean mustSelect4S = true;
        for (ShopQuote shopQuote : shopQuotes) {
            for (int i = 0; i < shopQuote.items.size(); i++) {
                if (shopQuote.items.get(i).mustSelect4S == 1) {
                    if (!(shopQuote.items.get(i).choose == 1)) {
                        mustSelect4S = false;
                    }
                }
            }
        }
        return mustSelect4S;
    }

    public String get4SmustSelectName() {
        String name = "";
        for (ShopQuote shopQuote : shopQuotes) {
            for (int i = 0; i < shopQuote.items.size(); i++) {
                if (shopQuote.items.get(i).mustSelect4S == 1) {
                    name = name + shopQuote.items.get(i).name + "，";
                }
            }
        }
        return name.substring(0, name.length() - 1);
    }

    //拿本地json文件
    public String getFromAssets(String fileName, Context context) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");//你的文件的编码
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 符合车型所有保养项目 for4s
     *
     * @param context
     * @param itemsNHandleFor4S
     */
    public void getItemsNFor4S(final Context context, final ItemsNHandleFor4S itemsNHandleFor4S) {
        client.itemsFor4S(myAuto.autoModelID, myAuto.seriesID, myAuto.brandID, shopID, myAuto.drivingDistance, myAuto.registerTime, new LoadingAnimResponseHandler(context) {
            @Override
            public void onSuccess(String response) {
                ArrayList<MaintenanceItemGroup> tempItemsFor4S = JSONUtils.fromJson(response, new TypeToken<ArrayList<MaintenanceItemGroup>>() {
                });

                DebugLog.i("TAG", tempItemsFor4S.toString());
                itemsFor4S.clear();
                itemsFor4S = tempItemsFor4S;
                getShowItemsFor4S();

                if (tempItemsFor4S != null) {
                    if (flag.equals("3")) {
                        itemsNHandleFor4S.onSuccessFor4SHome(recommentItemsFor4S, optionalItemsFor4S.size());
                    } else {
                        itemsNHandleFor4S.onSuccess(recommentItemsFor4S, optionalItemsFor4S);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                itemsNHandleFor4S.onFailure();
            }
        });
    }


    /**
     * 得到保养初选互斥的项目
     *
     * @param mutexShopQuoteItem
     */
    public void getMutexItems(ShopQuoteItem mutexShopQuoteItem) {
        String[] mutexExclusionGroupID = mutexShopQuoteItem.mutualExclusionGroup.split(",");
        for (ShopQuote mShopQuote : shopQuotes) {
            for (ShopQuoteItem shopQuoteItem : mShopQuote.items) {
                for (String aMutexExclusionGroupID : mutexExclusionGroupID) {
                    DebugLog.i("Tag", "aa   " + aMutexExclusionGroupID);
                    if (!TextUtils.isEmpty(aMutexExclusionGroupID) && !mutexShopQuoteItem.itemGroupID.equals(shopQuoteItem.itemGroupID)
                            && shopQuoteItem.mutualExclusionGroup.contains(aMutexExclusionGroupID)) {
                        shopQuoteItem.choose = 0;
                        break;
                    }
                }
            }
        }
    }

    /**
     * 得到4s店互斥项目
     *
     * @param itemGroup
     */
    public void getMutexItemsFor4S(MaintenanceItemGroup itemGroup, boolean isCheck) {
        String[] mutexExclusionGroupID = itemGroup.mutualExclusionGroup.split(",");
        mutexRecommend.clear();
        for (int i = 0; i < recommentItemsFor4S.size(); i++) {
            MaintenanceItemGroup itemN = recommentItemsFor4S.get(i);
            for (String aMutexExclusionGroupID : mutexExclusionGroupID) {
                if (!TextUtils.isEmpty(aMutexExclusionGroupID) && !itemGroup.itemGroupID.equals(itemN.itemGroupID)
                        && itemN.mutualExclusionGroup.contains(aMutexExclusionGroupID)) {
                    itemN.isCheck = !isCheck;
                    mutexRecommend.add(i);
                    break;
                }
            }
        }
        DebugLog.i(TAG,"fourSHelper  mutexRecommend :" + mutexRecommend.size());

        mutexOptional.clear();
        for (int j = 0; j < optionalItemsFor4S.size(); j++) {
            MaintenanceItemGroup itemN = optionalItemsFor4S.get(j);
            for (String aMutexExclusionGroupID : mutexExclusionGroupID) {
                if (!TextUtils.isEmpty(aMutexExclusionGroupID) && !itemGroup.itemGroupID.equals(itemN.itemGroupID)
                        && itemN.mutualExclusionGroup.contains(aMutexExclusionGroupID)) {
                    itemN.isCheck = !isCheck;
                    mutexOptional.add(j);
                    break;
                }
            }
        }
        DebugLog.i(TAG,"fourSHelper  mutexOptional :" + mutexOptional.size());
    }

    /**
     * 获取4s店 同时选中的项目
     *
     * @param itemGroup
     */
    public void getBothItemsFor4s(MaintenanceItemGroup itemGroup, boolean isCheck) {
        String[] mBothGroupID = itemGroup.bothGroup.split(",");
        bothRecommend.clear();
        for (int i = 0; i < recommentItemsFor4S.size(); i++) {
            MaintenanceItemGroup itemN = recommentItemsFor4S.get(i);
            for (String aBothGroupID : mBothGroupID) {
                if (!TextUtils.isEmpty(aBothGroupID) && !itemGroup.itemGroupID.equals(itemN.itemGroupID)
                        && itemN.bothGroup.contains(aBothGroupID)) {
                    itemN.isCheck = isCheck;
                    bothRecommend.add(i);
                    break;
                }
            }
        }
        DebugLog.i(TAG,"fourSHelper  bothRecommend :" + bothRecommend.size());

        bothOptional.clear();
        for (int j = 0; j < optionalItemsFor4S.size(); j++) {
            MaintenanceItemGroup itemN = optionalItemsFor4S.get(j);
            for (String aBothGroupID : mBothGroupID) {
                if (!TextUtils.isEmpty(aBothGroupID) && !itemGroup.itemGroupID.equals(itemN.itemGroupID)
                        && itemN.bothGroup.contains(aBothGroupID)) {
                    itemN.isCheck = isCheck;
                    bothOptional.add(j);
                    break;
                }
            }
        }
        DebugLog.i(TAG,"fourSHelper   bothOptional :" + bothOptional.size());
    }

    /**
     * 得到保养初选同时选中的项目
     *
     * @param mutexShopQuoteItem
     */
    public void getBothItems(ShopQuoteItem mutexShopQuoteItem, int choose) {

        String[] bothExclusionGroupID = mutexShopQuoteItem.bothGroup.split(",");
        for (ShopQuote mShopQuote : shopQuotes) {
            for (ShopQuoteItem shopQuoteItem : mShopQuote.items) {
                for (String aBothExclusionGroupID : bothExclusionGroupID) {
                    if (!TextUtils.isEmpty(aBothExclusionGroupID) && !mutexShopQuoteItem.itemGroupID.equals(shopQuoteItem.itemGroupID)
                            && shopQuoteItem.bothGroup.contains(aBothExclusionGroupID)) {
                        shopQuoteItem.choose = choose;
                        break;
                    }
                }
            }
        }
    }


    //对比报价页面勾选的项目 来获取所有项目中的被勾选的项目 for4s
    private void getShowItemsFor4S() {
        recommentItemsFor4S.clear();
        optionalItemsFor4S.clear();

        if (!flag.equals("3")) {
            for (ShopQuote shopQuote : shopQuotes) {
                for (ShopQuoteItem shopQuoteItem : shopQuote.items) {
                    for (MaintenanceItemGroup maintenanceItemN : itemsFor4S) {
                        if (shopQuoteItem.choose == 1 && shopQuoteItem.itemGroupID.equals(maintenanceItemN.itemGroupID)) {
                            maintenanceItemN.isCheck = true;
                            recommentItemsFor4S.add(maintenanceItemN);
                        } else if (shopQuoteItem.choose == 0 && shopQuoteItem.itemGroupID.equals(maintenanceItemN.itemGroupID)) {
                            maintenanceItemN.isCheck = false;
                            optionalItemsFor4S.add(maintenanceItemN);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < itemsFor4S.size(); i++) {
                if (itemsFor4S.get(i).choose == 1) {
                    recommentItemsFor4S.add(itemsFor4S.get(i));
                } else {
                    optionalItemsFor4S.add(itemsFor4S.get(i));
                }
            }
            //给4s店首页进保养流程用的 默认推荐的都是勾选的
            for (int i = 0; i < recommentItemsFor4S.size(); i++) {
                recommentItemsFor4S.get(i).isCheck = true;
            }
        }
    }


    /**
     * 算所有项目的钱 for4s
     *
     * @param recommendItemsFor4S
     * @param optionalItemsFor4S
     */
    @hugo.weaving.DebugLog
    public void CalculateMoneyFor4S(ArrayList<MaintenanceItemGroup> recommendItemsFor4S, ArrayList<MaintenanceItemGroup> optionalItemsFor4S, CalculateMoneyFor4SHandle calculateMoneyFor4SHandle) {
        float amount = 0;
        float workCost = 0;
        float goodsCost = 0;
        float oldPrice = 0;

        for (MaintenanceItemGroup maintenanceItemN : recommendItemsFor4S) {
            if (maintenanceItemN.isCheck) {
                final float[] tempItemGroupAmount = new float[1];
                final float[] tempWorkCost = new float[1];
                final float[] tempGoodsAmount = new float[1];
                final float[] tempOldPrice = new float[1];
                CalculateItemMoneyFor4S(maintenanceItemN, new CalculateItemGroupMoneyFor4SHandle() {
                    @Override
                    public void onCalculateItemGroupMoney(float itemAmount, float workCoast, float goodsAmount, float oldPrice, boolean isDiscount) {
                        tempItemGroupAmount[0] = itemAmount;
                        tempWorkCost[0] = workCoast;
                        tempGoodsAmount[0] = goodsAmount;
                        tempOldPrice[0] = oldPrice;
                    }
                });
                amount = amount + tempItemGroupAmount[0];
                workCost = workCost + tempWorkCost[0];
                goodsCost = goodsCost + tempGoodsAmount[0];
                oldPrice = oldPrice + tempOldPrice[0];
            }
        }

        for (MaintenanceItemGroup maintenanceItemN : optionalItemsFor4S) {
            if (maintenanceItemN.isCheck) {
                final float[] tempItemGroupAmount = new float[1];
                final float[] tempWorkCost = new float[1];
                final float[] tempGoodsAmount = new float[1];
                final float[] tempOldPrice = new float[1];
                CalculateItemMoneyFor4S(maintenanceItemN, new CalculateItemGroupMoneyFor4SHandle() {
                    @Override
                    public void onCalculateItemGroupMoney(float itemAmount, float workCoast, float goodsAmount, float oldPrice, boolean isDiscount) {
                        tempItemGroupAmount[0] = itemAmount;
                        tempWorkCost[0] = workCoast;
                        tempGoodsAmount[0] = goodsAmount;
                        tempOldPrice[0] = oldPrice;
                    }
                });
                amount = amount + tempItemGroupAmount[0];
                workCost = workCost + tempWorkCost[0];
                goodsCost = goodsCost + tempGoodsAmount[0];
                oldPrice = oldPrice + tempOldPrice[0];
            }
        }
        calculateMoneyFor4SHandle.onSuccess(amount, workCost, goodsCost, oldPrice);
    }

    /**
     * 算一个项目组的钱 （项目组内所有项目的工时费-工时优惠+配件总价-配件优惠）
     *
     * @param maintenanceItemGroup
     */
    public void CalculateItemMoneyFor4S(MaintenanceItemGroup maintenanceItemGroup, CalculateItemGroupMoneyFor4SHandle calculateItemGroupMoneyFor4SHandle) {
        float totalWorkCost = 0; //工时总价
        float totalGoodsAmount = 0; //商品价格总和
        float itemGroupAmount = 0;  //折后总价

        float totalItemDiscount = 0; //项目总折扣
        float totalGoodsDiscount = 0; //商品总折扣
        float totalDiscount = 0; // 项目总折扣 + 商品总折扣 + maintenanceItemGroup.discount

        float oldPrice = 0; //折前价格

        boolean isDiscount = false; //有没有打折

        for (MaintenanceItem item : maintenanceItemGroup.items) {
            totalWorkCost = totalWorkCost + Float.parseFloat(item.workCost);
            totalItemDiscount = totalItemDiscount + item.discount;
            for (int i = 0; i < item.goods.size(); i++) {
                for (MaintenanceChildGoodsN childGoodsN : item.goods.get(i)) {
                    final float[] tempGoodsAmount = new float[1];
                    final float[] tempDiscountG = new float[1];
                    if (childGoodsN.choose == 1) {
                        CalculateGoodMoenyFor4S(childGoodsN, new CalculateGoodsMoneyFor4SHandle() {
                            @Override
                            public void onCalculateGoodsMoney(float goodsAmount, float discountG) {
                                tempGoodsAmount[0] = goodsAmount;
                                tempDiscountG[0] = discountG;
                            }
                        });
                        totalGoodsDiscount = totalGoodsDiscount + tempDiscountG[0];
                        totalGoodsAmount = totalGoodsAmount + tempGoodsAmount[0];
                    }
                }
            }
        }
        totalDiscount = totalDiscount + totalItemDiscount + totalGoodsDiscount + maintenanceItemGroup.discount;
        oldPrice = totalWorkCost + totalGoodsAmount;
        itemGroupAmount = oldPrice - totalDiscount;

        if (totalDiscount != 0) {
            isDiscount = true;
        }

        calculateItemGroupMoneyFor4SHandle.onCalculateItemGroupMoney(itemGroupAmount, totalWorkCost, totalGoodsAmount, oldPrice, isDiscount);
    }

    /**
     * 算一个商品的钱
     *
     * @param childGoodsN
     */
    public void CalculateGoodMoenyFor4S(MaintenanceChildGoodsN childGoodsN, CalculateGoodsMoneyFor4SHandle calculateGoodsMoneyFor4SHandle) {
        // 计算规则 （price-dicountG）*count）
        calculateGoodsMoneyFor4SHandle.onCalculateGoodsMoney((childGoodsN.price - childGoodsN.discountG) * childGoodsN.count, (childGoodsN.discountG * childGoodsN.count));
    }


    /**
     * 得到上传的参数 for 4s
     *
     * @return
     */
    public String getItemIDFor4S() {

        upMaintainItemGroupIDs.clear();

        for (MaintenanceItemGroup maintenanceItemN : recommentItemsFor4S) {
            if (maintenanceItemN.isCheck) {
                UpMaintainItemGroupID maintainItemGroupID = new UpMaintainItemGroupID();
                maintainItemGroupID.itemGroupID = maintenanceItemN.itemGroupID;
                for (MaintenanceItem item : maintenanceItemN.items) {
                    UpMaintainItemID upMaintainItemID = new UpMaintainItemID();
                    upMaintainItemID.itemID = item.itemID;
                    for (int i = 0; i < item.goods.size(); i++) {
                        for (MaintenanceChildGoodsN childGoodsN : item.goods.get(i)) {
                            if (childGoodsN.choose == 1) {
                                upMaintainItemID.goodsID.add(childGoodsN.goodsID + "_" + childGoodsN.count);
                            }
                        }
                    }
                    maintainItemGroupID.item.add(upMaintainItemID);
                }
                upMaintainItemGroupIDs.add(maintainItemGroupID);
            }
        }

        for (MaintenanceItemGroup maintenanceItemN : optionalItemsFor4S) {
            if (maintenanceItemN.isCheck) {
                UpMaintainItemGroupID maintainItemGroupID = new UpMaintainItemGroupID();
                maintainItemGroupID.itemGroupID = maintenanceItemN.itemGroupID;
                for (MaintenanceItem item : maintenanceItemN.items) {
                    UpMaintainItemID upMaintainItemID = new UpMaintainItemID();
                    upMaintainItemID.itemID = item.itemID;
                    for (int i = 0; i < item.goods.size(); i++) {
                        for (MaintenanceChildGoodsN childGoodsN : item.goods.get(i)) {
                            if (childGoodsN.choose == 1) {
                                upMaintainItemID.goodsID.add(childGoodsN.goodsID + "_" + childGoodsN.count);
                            }
                        }
                    }
                    maintainItemGroupID.item.add(upMaintainItemID);
                }
                upMaintainItemGroupIDs.add(maintainItemGroupID);
            }
        }
        return JSONUtils.toJson(upMaintainItemGroupIDs);
    }

    /**
     * 得到 shopQuote  itemGroupID 的上传参数
     *
     * @return
     */
    public String getGroupItem() {
        ArrayList<ShopQuoteItem> tempShopQuotes = new ArrayList<>();
        for (ShopQuote shopQuote : shopQuotes) {
            tempShopQuotes.addAll(shopQuote.items);
        }
        if (uploadGroupObject.itemGroupID != null) {
            uploadGroupObject.itemGroupID.clear();
        } else {
            uploadGroupObject.itemGroupID = new ArrayList<>();
        }
        if (tempShopQuotes.size() != 0) {
            for (int i = 0; i < tempShopQuotes.size(); i++) {
                if (tempShopQuotes.get(i).choose == 1) {
                    uploadGroupObject.itemGroupID.add(tempShopQuotes.get(i).itemGroupID);
                }
            }
        }
        return JSONUtils.toJson(uploadGroupObject);
    }

    /**
     * 准备订单
     *
     * @param context
     */
    public void prepareOrderN(final Context context, final prepareOrderNHandle prepareOrderNHandle) {
        String item;
        String shopType;
        if (flag.equals("2")) {
            shopType = "20";  //快修
        } else {
            shopType = "10"; //4s
        }
        item = getItemIDFor4S();
        int upScore = (int) score;

        client.prepareN(shopID, item, shopType, myAuto.brandID, myAuto.seriesID, myAuto.autoModelID, couponID, String.valueOf(upScore), myAuto.myAutoID, myAuto.getPlateNumber(), new LoadingAnimResponseHandler(context) {
            @Override
            public void onSuccess(String response) {
                orderPrepareN = JSONUtils.fromJson(response, new TypeToken<OrderPrepareN>() {
                });
                if (orderPrepareN != null && orderPrepareN.shopIntroduce != null) {
                    prepareOrderNHandle.onSuccess(orderPrepareN);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
//                orderPrepareN= JSONUtils.fromJson(getFromAssets(fakePrepareN,context),new TypeToken<OrderPrepareN>(){});
                prepareOrderNHandle.onFailure();
            }
        });
    }


//    public void deleteMaintenanceItem(MaintenanceItemN maintenanceItemN) {
//        for (int i = 0; i < maintenanceItems.size(); i++) {
//            if (maintenanceItemN.itemID.equals(maintenanceItems.get(i).itemID)) {
//                maintenanceItems.get(i).isCheck = false;
//            }
//        }
//    }


    /********************************************接口**************************************************************/

    /***********
     * 保养项目概述接口
     **********/
    public interface ItemsAllOverviewNHandle {
        void onSuccess(ArrayList<ShopQuote> shopQuotes);

        void onFailure();

        void onEmpty();
    }

    /***********
     * 符合车型所有保养项目接口
     **********/
    public interface ItemsNHandle {
        void onSuccess(ArrayList<MaintenanceItemN> maintenanceShowItems);

        void onSuccessFor4S(ArrayList<MaintenanceItemN> maintenanceItems);

        void onFailure();
    }

    /***********
     * 符合车型所有保养项目接口 For4S
     **********/
    public interface ItemsNHandleFor4S {
        void onSuccess(ArrayList<MaintenanceItemGroup> recommentItemsFor4S, ArrayList<MaintenanceItemGroup> optionalItemsFor4S);

        void onSuccessFor4SHome(ArrayList<MaintenanceItemGroup> recommentItemsFor4S, int optionSize);

        void onFailure();
    }

    /***********
     * 计算金钱的接口
     **********/
    public interface CalculateMoneyHandle {
        void onSuccess(float goodsAmount, String workCostAmount, String amount);
    }

    /***********
     * 计算金钱for4s的接口
     **********/
    public interface CalculateMoneyFor4SHandle {
        void onSuccess(float amount, float workCost, float goodsCost, float oldPrice);
    }

    /***********
     * 计算一个项目组的钱
     **********/
    public interface CalculateItemGroupMoneyFor4SHandle {
        void onCalculateItemGroupMoney(float itemAmount, float workCoast, float goodsAmount, float oldPrice, boolean isDiscount);
    }

    /***********
     * 计算一个商品的钱
     **********/
    public interface CalculateGoodsMoneyFor4SHandle {
        void onCalculateGoodsMoney(float goodsAmount, float goodsSale);
    }

    /***********
     * 准备订单的接口
     **********/
    public interface prepareOrderNHandle {
        void onSuccess(OrderPrepareN orderPrepareN);

        void onFailure();
    }

    /***********
     * 计算每个项目金钱的接口
     **********/
    public interface CalculateItemMoneyHandle {
        void onSuccess(float goodsAmount, String amount);
    }


    /**************************
     * get set
     *********************/

    public void setShopQuotes(ArrayList<ShopQuote> shopQuotes) {
        this.shopQuotes = shopQuotes;
    }

    public ArrayList<ShopQuote> getShopQuotes() {
        return shopQuotes;
    }

    public ArrayList<MaintenanceItemGroup> getMaintenanceItems() {
        return maintenanceItems;
    }

    public ArrayList<MaintenanceItemGroup> getMaintenanceShowItems() {
        return maintenanceShowItems;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public MyAuto getMyAuto() {
        return myAuto;
    }

    public void setMyAuto(MyAuto myAuto) {
        this.myAuto = myAuto;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public ArrayList<MaintenanceItemGroup> getChooseMaintenanceItems() {
        return chooseMaintenanceItems;
    }

    public ArrayList<MaintenanceItemGroup> getRecommentItemsFor4S() {
        return recommentItemsFor4S;
    }

    public void setRecommentItemsFor4S(ArrayList<MaintenanceItemGroup> recommentItemsFor4S) {
        this.recommentItemsFor4S = recommentItemsFor4S;
    }

    public ArrayList<MaintenanceItemGroup> getOptionalItemsFor4S() {
        return optionalItemsFor4S;
    }

    public void setOptionalItemsFor4S(ArrayList<MaintenanceItemGroup> optionalItemsFor4S) {
        this.optionalItemsFor4S = optionalItemsFor4S;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getCouponID() {
        return couponID;
    }

    public void setCouponID(String couponID) {
        this.couponID = couponID;
    }

    public String getMaintenanceItemGroupFlag() {
        return maintenanceItemGroupFlag;
    }

    public void setMaintenanceItemGroupFlag(String maintenanceItemGroupFlag) {
        this.maintenanceItemGroupFlag = maintenanceItemGroupFlag;
    }

    public ArrayList<Integer> getMutexRecommend() {
        return mutexRecommend;
    }

    public void setMutexRecommend(ArrayList<Integer> mutexRecommend) {
        this.mutexRecommend = mutexRecommend;
    }

    public ArrayList<Integer> getMutexOptional() {
        return mutexOptional;
    }

    public void setMutexOptional(ArrayList<Integer> mutexOptional) {
        this.mutexOptional = mutexOptional;
    }

    public ArrayList<Integer> getBothRecommend() {
        return bothRecommend;
    }

    public void setBothRecommend(ArrayList<Integer> bothRecommend) {
        this.bothRecommend = bothRecommend;
    }

    public ArrayList<Integer> getBothOptional() {
        return bothOptional;
    }

    public void setBothOptional(ArrayList<Integer> bothOptional) {
        this.bothOptional = bothOptional;
    }
}
