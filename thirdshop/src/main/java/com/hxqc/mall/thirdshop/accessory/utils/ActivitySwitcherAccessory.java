package com.hxqc.mall.thirdshop.accessory.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.accessory.activity.AccessoryPayFinishActivity;
import com.hxqc.mall.thirdshop.accessory.activity.AccessoryPriceListActivity;
import com.hxqc.mall.thirdshop.accessory.activity.AccessorySaleActivity;
import com.hxqc.mall.thirdshop.accessory.activity.AccessoryShopListActivity;
import com.hxqc.mall.thirdshop.accessory.activity.AccessoryShopListOnMapActivity;
import com.hxqc.mall.thirdshop.accessory.activity.AccessoryShopPayFinishActivity;
import com.hxqc.mall.thirdshop.accessory.activity.ConfirmOrderActivity;
import com.hxqc.mall.thirdshop.accessory.activity.PackageListActivity;
import com.hxqc.mall.thirdshop.accessory.activity.PaySubscriptionActivity;
import com.hxqc.mall.thirdshop.accessory.activity.PhotoActivity;
import com.hxqc.mall.thirdshop.accessory.activity.PositionActivity;
import com.hxqc.mall.thirdshop.accessory.activity.ProductDetailActivity;
import com.hxqc.mall.thirdshop.accessory.activity.RefundActivity;
import com.hxqc.mall.thirdshop.accessory.activity.ShoppingCartActivity;
import com.hxqc.mall.thirdshop.accessory.model.Brand;
import com.hxqc.mall.thirdshop.accessory.model.ConfirmOrderItem;
import com.hxqc.mall.thirdshop.accessory.model.SinglePackage;
import com.hxqc.mall.thirdshop.accessory.model.SubmitOrderInfo;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年02月22日
 */
public class ActivitySwitcherAccessory extends ActivitySwitchBase {
    public final static String CONFIRM_ORDER_ITEMS = "confirm_order_items";
    public final static String ORDER_ID = "orderID";
    public final static String SUBMIT_ORDER_INFO = "submit_order_info";
    public final static String AMOUNT = "amount";
    public final static String PRODUCT_ID = "product_id";
    public final static int TO_SHOP_LIST = 101;
    private final static String TAG = ActivitySwitcherAccessory.class.getSimpleName();

    /**
     * 用品销售 旧版
     **/
    public static void toAccessorySale(Context context) {
        Intent intent = new Intent(context, AccessorySaleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 商品详情 旧版
     */
    public static void toProductDetail(Context context, String productID) {
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_ID, productID);
        context.startActivity(new Intent(context, ProductDetailActivity.class).putExtra(KEY_DATA, bundle));
    }

    /**
     * 图集
     */
    public static void toPhoto(Context context, ArrayList<ImageModel> list) {
        Intent intent = new Intent(context, PhotoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(viewLargePics, list);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 优惠套餐列表
     */
    public static void toPackageList(Context context, ArrayList<SinglePackage> list) {
        Intent intent = new Intent(context, PackageListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PackageListActivity.PACKAGE_LIST, list);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
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
    public static void toShopDetails(Context context, HashMap<String, String> hashMap, String bigName, String samllName, String shopID, String productBrandID, String productBrandName) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.thirdshop.activity.ShopDetailsActivity");
        intent.putExtra("ShopID", shopID);
        intent.putExtra("HashMap", hashMap);
        intent.putExtra("bigName", bigName);
        intent.putExtra("smallName", samllName);
        intent.putExtra("productBrandID", productBrandID);
        intent.putExtra("productBrandName", productBrandName);
        intent.putExtra("DEFAULT", "1");
        context.startActivity(intent);
    }

    /**
     * 确认订单
     */
    public static void toConfirmOrder(Context context, ConfirmOrderItem confirmOrderItem) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CONFIRM_ORDER_ITEMS, JSONUtils.toJson(confirmOrderItem));
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 支付订金
     */
    public static void toPaySubscription(Context context, ArrayList<SubmitOrderInfo> submitOrderInfos, String amount) {
        Intent intent = new Intent(context, PaySubscriptionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SUBMIT_ORDER_INFO, submitOrderInfos);
        bundle.putString(AMOUNT, amount);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 订单详情
     */
    public static void toOrderDetail(Context context, String orderID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.setClassName(context, "com.hxqc.mall.activity.order.AccessoryOrderDetailActivity");
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
        Intent intent = new Intent(context, ShoppingCartActivity.class);
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
    public static void toShopPayFinish(Context context, String orderID) {
        Intent intent = new Intent(context, AccessoryShopPayFinishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID, orderID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 保养门店
     */
    public static void toShopListForResult(Activity activity) {
        Intent intent = new Intent(activity, AccessoryShopListActivity.class);
        activity.startActivityForResult(intent, TO_SHOP_LIST);
    }

    /**
     * 地图选店
     **/
    public static void toMapList(Activity activity) {
        Intent intent = new Intent(activity, AccessoryShopListOnMapActivity.class);
        activity.startActivityForResult(intent, TO_SHOP_LIST);
    }

}
