package com.hxqc.mall.thirdshop.accessory4s.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.accessory.activity.AccessoryPayFinishActivity;
import com.hxqc.mall.thirdshop.accessory.activity.AccessoryPriceListActivity;
import com.hxqc.mall.thirdshop.accessory.activity.PhotoActivity;
import com.hxqc.mall.thirdshop.accessory.activity.PositionActivity;
import com.hxqc.mall.thirdshop.accessory.activity.RefundActivity;
import com.hxqc.mall.thirdshop.accessory.model.Brand;
import com.hxqc.mall.thirdshop.accessory.model.SinglePackage;
import com.hxqc.mall.thirdshop.accessory4s.activity.AccessorySaleFrom4SActivity;
import com.hxqc.mall.thirdshop.accessory4s.activity.AccessorySaleFromHomeActivity;
import com.hxqc.mall.thirdshop.accessory4s.activity.AccessoryShopPayFinish4SActivity;
import com.hxqc.mall.thirdshop.accessory4s.activity.ConfirmOrder4SActivity;
import com.hxqc.mall.thirdshop.accessory4s.activity.InstallmentBuyingModelActivity;
import com.hxqc.mall.thirdshop.accessory4s.activity.InstallmentBuyingSeriesActivity;
import com.hxqc.mall.thirdshop.accessory4s.activity.PackageListNewActivity;
import com.hxqc.mall.thirdshop.accessory4s.activity.PayActivity;
import com.hxqc.mall.thirdshop.accessory4s.activity.ProductDetailNewActivity;
import com.hxqc.mall.thirdshop.accessory4s.activity.ShoppingCart4SActivity;
import com.hxqc.mall.thirdshop.accessory4s.model.ConfirmOrderItem4S;
import com.hxqc.mall.thirdshop.accessory4s.model.SubmitOrderInfo4S;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年02月22日
 */
public class ActivitySwitcherAccessory4S extends ActivitySwitchBase {
    public final static String CONFIRM_ORDER_ITEMS = "confirm_order_items";
    public final static String ORDER_ID = "orderID";
    public final static String SUBMIT_ORDER_INFO_4S = "submit_order_info_4s";
    public final static String AMOUNT = "amount";
    public final static String PRODUCT_ID = "product_id";
    public final static int TO_SHOP_LIST = 101;
    private final static String TAG = ActivitySwitcherAccessory4S.class.getSimpleName();

    /**
     * 用品销售 从首页进
     **/
    public static void toAccessorySaleFromHome(Context context) {
        Intent intent = new Intent(context, AccessorySaleFromHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 用品销售 从4S店进
     **/
    public static void toAccessorySaleFrom4S(Context context, String shopID) {
        Bundle bundle = new Bundle();
        bundle.putString(ShopDetailsController.SHOPID_KEY, shopID);
        context.startActivity(new Intent(context, AccessorySaleFrom4SActivity.class).putExtra(KEY_DATA, bundle));
    }

    /**
     * 商品详情
     */
    public static void toProductDetail(Context context, String productID, String productGroupID) {
        Bundle bundle = new Bundle();
        bundle.putString(ProductDetailNewActivity.PRODUCT_ID, productID);
        bundle.putString(ProductDetailNewActivity.PRODUCT_GROUP_ID, productGroupID);
        context.startActivity(new Intent(context, ProductDetailNewActivity.class).putExtra(KEY_DATA, bundle));
    }

    /**
     * 图集
     */
    public static void toPhoto(Context context, ArrayList<ImageModel> list) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(viewLargePics, list);
        context.startActivity(new Intent(context, PhotoActivity.class).putExtra(KEY_DATA, bundle));
    }

    /**
     * 优惠套餐列表
     */
    public static void toPackageList(Context context, ArrayList<SinglePackage> list, String shopName, String shopID) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PackageListNewActivity.PACKAGE_LIST, list);
        bundle.putString(PackageListNewActivity.SHOP_NAME, shopName);
        bundle.putString(PackageListNewActivity.SHOP_ID, shopID);
        context.startActivity(new Intent(context, PackageListNewActivity.class).putExtra(KEY_DATA, bundle));
    }

    /**
     * 定位选择
     **/
    public static void toPositionActivity(Context context, int requestCode, String position) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(context, PositionActivity.class);
        intent.setComponent(componentName);
        intent.putExtra("position", position);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 网上4S店报价
     **/
    public static void toAccessoryPriceActivity(Context context, String imgUrl, String accessoryName, Brand brand, String productBrandID, String productBrandName) {
        Intent intent = new Intent(context, AccessoryPriceListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(AccessoryPriceListActivity.IMGURL, imgUrl);
        bundle.putString(AccessoryPriceListActivity.ACCESSORYNAME, accessoryName);
        bundle.putParcelable(AccessoryPriceListActivity.BRAND, brand);
        bundle.putString(AccessoryPriceListActivity.PRODUCTBRANDID, productBrandID);
        bundle.putString(AccessoryPriceListActivity.PRODUCTBRANDNAME, productBrandName);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 店铺详情
     *
     * @param context
     * @param shopID  店铺ID
     */
    public static void toShopDetails(Context context, HashMap<String, String> hashMap, String bigName, String smallName, String shopID, String productBrandID, String productBrandName) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.thirdshop.activity.ShopDetailsActivity");
        intent.putExtra("ShopID", shopID);
        intent.putExtra("HashMap", hashMap);
        intent.putExtra("bigName", bigName);
        intent.putExtra("smallName", smallName);
        intent.putExtra("productBrandID", productBrandID);
        intent.putExtra("productBrandName", productBrandName);
        intent.putExtra("DEFAULT", "1");
        context.startActivity(intent);
    }

    /**
     * 确认订单
     */
    public static void toConfirmOrder(Context context, ArrayList<ConfirmOrderItem4S> confirmOrderItem) {
        Intent intent = new Intent(context, ConfirmOrder4SActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CONFIRM_ORDER_ITEMS, JSONUtils.toJson(confirmOrderItem));
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 支付订金
     */
    public static void toPay(Context context, SubmitOrderInfo4S submitOrderInfo4S) {
        Intent intent = new Intent(context, PayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUBMIT_ORDER_INFO_4S, submitOrderInfo4S);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 订单详情
     */
    public static void toOrderDetail(Context context, String orderID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.setClassName(context, "com.hxqc.mall.activity.order.Accessory4SShopOrderDetailActivity");
        bundle.putString(ORDER_ID, orderID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 申请退款
     */
    public static void toRefund(Context context, String orderID) {
        Intent intent = new Intent(context, RefundActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID, orderID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 购物车
     */
    public static void toShoppingCart(Context context) {
        Intent intent = new Intent(context, ShoppingCart4SActivity.class);
        context.startActivity(intent);
    }

    /**
     * 支付完成
     */
    public static void toPayFinish(Context context) {
        Intent intent = new Intent(context, AccessoryPayFinishActivity.class);
        context.startActivity(intent);
    }

    /**
     * 到店支付提交完成
     */
    public static void toShopPayFinish(Context context, String orderID,String amount) {
        Intent intent = new Intent(context, AccessoryShopPayFinish4SActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID, orderID);
        bundle.putString(AMOUNT, amount);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 分期购车-车系
     * @param context
     */
    public static void toInstallmentBuyingSeries(Context context) {
        Intent intent = new Intent(context, InstallmentBuyingSeriesActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 分期购车-车型
     * @param context
     */
    public static void toInstallmentBuyingModel(Context context) {
        Intent intent = new Intent(context, InstallmentBuyingModelActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }
}
