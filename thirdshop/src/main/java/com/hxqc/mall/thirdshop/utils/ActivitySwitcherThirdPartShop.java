package com.hxqc.mall.thirdshop.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hxqc.mall.activity.WebActivity;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.advisory.Advisory;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.photolibrary.activity.ImagePagerActivity;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.AdvisoryListActivity;
import com.hxqc.mall.thirdshop.activity.AskLeastMoneyActivity;
import com.hxqc.mall.thirdshop.activity.AskLeastMoneyActivity2;
import com.hxqc.mall.thirdshop.activity.ConfirmSpecialCarOrderActivity;
import com.hxqc.mall.thirdshop.activity.DepositFinishActivity;
import com.hxqc.mall.thirdshop.activity.FavorableCarDetailActivity;
import com.hxqc.mall.thirdshop.activity.FilterCarModelActivity;
import com.hxqc.mall.thirdshop.activity.FilterSeckillIndefiniteActivity;
import com.hxqc.mall.thirdshop.activity.FlashSaleListActivity;
import com.hxqc.mall.thirdshop.activity.FourSMallActivity2;
import com.hxqc.mall.thirdshop.activity.FourSMallActivity;
import com.hxqc.mall.thirdshop.activity.FourSShopListActivity;
import com.hxqc.mall.thirdshop.activity.GroupBuyMergeActivity;
import com.hxqc.mall.thirdshop.activity.InvoiceActivity;
import com.hxqc.mall.thirdshop.activity.NewCarModelDetailActivity;
import com.hxqc.mall.thirdshop.activity.NewCarModelListActivity;
import com.hxqc.mall.thirdshop.activity.NewCarModelListActivity2;
import com.hxqc.mall.thirdshop.activity.NewCarSeriesListActivity;
import com.hxqc.mall.thirdshop.activity.NewsImageInfoActivity;
import com.hxqc.mall.thirdshop.activity.PositionActivity;
import com.hxqc.mall.thirdshop.activity.PositionActivityForSpecialCar;
import com.hxqc.mall.thirdshop.activity.SalesDepositPayActivity;
import com.hxqc.mall.thirdshop.activity.SalesNewsDetailActivity;
import com.hxqc.mall.thirdshop.activity.SalesOrderConfirmActivity;
import com.hxqc.mall.thirdshop.activity.SalesPDetailActivity;
import com.hxqc.mall.thirdshop.activity.ShopAutoModelActivity;
import com.hxqc.mall.thirdshop.activity.ShopBrandActivity;
import com.hxqc.mall.thirdshop.activity.ShopInfoOfNewCarListActivity;
import com.hxqc.mall.thirdshop.activity.ShopProFileWebActivity;
import com.hxqc.mall.thirdshop.activity.SiteNewsListActivity;
import com.hxqc.mall.thirdshop.activity.SpecialCarDetailActivity;
import com.hxqc.mall.thirdshop.activity.SpecialCarDetailMainActivity;
import com.hxqc.mall.thirdshop.activity.TestDriveActivity;
import com.hxqc.mall.thirdshop.activity.ThirdShopAMapActivity;
import com.hxqc.mall.thirdshop.activity.ThirdShopAltasActivity;
import com.hxqc.mall.thirdshop.activity.ThirdShopOrderCancelActivity;
import com.hxqc.mall.thirdshop.activity.ThirdShopOrderDetailActivity;
import com.hxqc.mall.thirdshop.activity.ThirdShopOrderRefundActivity;
import com.hxqc.mall.thirdshop.activity.auto.activity.ThirdShopFilterForNewAuto;
import com.hxqc.mall.thirdshop.activity.auto.activity.ThirdShopFilterForSpecialAuto;
import com.hxqc.mall.thirdshop.activity.shop.InstallmentBuyCarActivity;
import com.hxqc.mall.thirdshop.activity.shop.MaintenanceHomeActivity_1;
import com.hxqc.mall.thirdshop.activity.shop.ModelsOfferActivity;
import com.hxqc.mall.thirdshop.activity.shop.ShopFlashSaleListActivity;
import com.hxqc.mall.thirdshop.activity.shop.ShopHomeActivity;
import com.hxqc.mall.thirdshop.activity.shop.ShopSalesPromotionActivity;
import com.hxqc.mall.thirdshop.constant.FilterResultKey;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.maintenance.activity.MaintenancePromotionActivity;
import com.hxqc.mall.thirdshop.model.InvoiceInfo;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.model.promotion.SalesDetail;
import com.hxqc.mall.thirdshop.model.promotion.SalesItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Author:胡俊杰
 * Date: 2015/11/30
 * FIXME
 * Todo
 */
public class ActivitySwitcherThirdPartShop extends ActivitySwitchBase {
    //促销详情id tag
    public static final String PROMOTION_ID = "promotionID";
    //新闻资讯id tag
    public static final String NEWS_ID = "newsID";
    //确认订单data tag
    public static final String CONFIRM_ORDER_DATA_TAG = "confirm_order_data_tag";
    //订单id传递
    public static final String SHOP_ORDER_ID = "shop_order_id";
    //订单支付金额
    public static final String DEPOSIT_AMOUNT = "deposit_amount";
    //店铺电话
    public static final String SHOP_TEL = "shop_tel";

    public static final String FROMCERTAINCAR = "certainCar";
    //询问底价、试乘试驾中的意向车型
    public static final String INTENT_TYPE = "intent_type";
    //新车跳第三方店铺用到的数据
    public static final String BRAND = "brand";
    public static final String SERIESNAME = "series_name";
    public static final String MODEL = "model";
    public static final String SITE_ID = "site_id";
    public static final String SERIES = "series";
    public static final String ACCESSORY = "1";
    public static final String MAINTENANCE = "2";
    public static final String INVOICE_INFO = "invoice_info";
    public static final String FROM = "from";
    public static final int TO_INVOICE = 10;
    public static final String TAB_4S = "tab_4S";
    public static final int REQUEST_CODE = 100;
    /**
     * 开启高德导航
     */
    final public static String MAP_OPERATOR = "map_operator";
    final public static String SHOP_AMAP = "shop_amap";


    /**
     * 筛选列表
     *
     * @param context
     */
    public static void toFilterAllShopBrand(Context context, boolean carCompare) {
        EventBus.getDefault().post(ShopBrandActivity.FINISH);
        Intent intent = new Intent(context, ShopBrandActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ShopBrandActivity.FROM_4S_HOME, true);
        bundle.putBoolean(ShopBrandActivity.FROM_CAR_COMPARE, carCompare);
        context.startActivity(intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle));
    }


    /**
     * 新车车辆列表跳第三方店铺
     *
     * @param context
     * @param brandName 品牌名称
     * @param series    车系实例
     * @param modelName 车型名称
     */
    public static void toFilterThirdShopActivity(Context context, String brandName, Series series, String modelName) {
        Intent intent = new Intent(context, ThirdShopFilterForNewAuto.class);
        intent.putExtra(BRAND, brandName);
        intent.putExtra(SERIESNAME, series);
        intent.putExtra(MODEL, modelName);

        context.startActivity(intent);
    }


    /**
     * 4S店特价车筛选
     *
     * @param context
     * @param brandName 品牌名称
     * @param series    车系实例
     * @param modelName 车型名称
     */
    public static void toFilterThirdSpecialActivity(Context context, String brandName, Series series, String modelName) {
        Intent intent = new Intent(context, ThirdShopFilterForSpecialAuto.class);
        intent.putExtra(BRAND, brandName);
        intent.putExtra(SERIESNAME, series);
        intent.putExtra(MODEL, modelName);
        context.startActivity(intent);
    }


    /**
     * 特价车详情中的车辆详情
     **/
    public static void toSpecialCarDetail(Context context, boolean isStarted, String saleArea, String imgUrl, String carName, String itemID, String introduce) {
        Intent intent = new Intent(context, SpecialCarDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SpecialCarDetailActivity.SALEAREA, saleArea);
        bundle.putBoolean(SpecialCarDetailActivity.ISSTARTED, isStarted);
        bundle.putString(SpecialCarDetailActivity.IMGURL, imgUrl);
        bundle.putString(SpecialCarDetailActivity.CARNAME, carName);
        bundle.putString(SpecialCarDetailActivity.INTRODUCE, introduce);
        bundle.putString(SpecialCarDetailActivity.ITEMID, itemID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

//    /**
//     * 品牌列表
//     **/
//    public static void toFourSBrand(Context context, String siteID) {
//        Intent intent = new Intent(context, FourSBrandActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(SITE_ID, siteID);
//        intent.putExtra(KEY_DATA, bundle);
//        context.startActivity(intent);
//    }


    /**
     * 跳转到促销详情
     */
    public static void toSalesItemDetail(String promotionID, Context context) {
        Intent intent = new Intent(context, SalesPDetailActivity.class);
        intent.putExtra(PROMOTION_ID, promotionID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 取消订单
     */
    public static void toThirShopOrderCancel(String OrderID, Context context) {
        Intent intent = new Intent(context, ThirdShopOrderCancelActivity.class);
        intent.putExtra(ThirdShopOrderCancelActivity.ORDER_ID, OrderID);
        context.startActivity(intent);
    }


    /**
     * 申请退款
     */
    public static void toThirShopOrderRefund(String OrderID, Context context) {
        Intent intent = new Intent(context, ThirdShopOrderRefundActivity.class);
        intent.putExtra(ThirdShopOrderRefundActivity.ORDER_ID, OrderID);
        context.startActivity(intent);
    }
    /**
     * 支付宝申请退款提示页
     */
    public static void toAlipayRefundInfo(Context context) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.activity.order.OrderRefundInfoActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 跳转到保养促销详情
     */
    public static void toMaintenancePDetail(String promotionID, Context context) {
        Intent intent = new Intent(context, MaintenancePromotionActivity.class);
        intent.putExtra(PROMOTION_ID, promotionID);
        context.startActivity(intent);
    }


    /**
     * 新闻资讯详情
     */
    public static void toSalesNewsDetail(String newsID, Context context) {
        Intent intent = new Intent(context, SalesNewsDetailActivity.class);
        intent.putExtra(NEWS_ID, newsID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 店铺首页
     *
     * @param shopID
     * @param context
     */
    public static void toShopHome(String shopID, Context context) {
        Intent intent = new Intent(context, ShopHomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ShopDetailsController.SHOPID_KEY, shopID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 车型报价
     *
     * @param shopID
     * @param context
     */
    public static void toModelsOffer(String shopID, Context context) {
        Intent intent = new Intent(context, ModelsOfferActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ShopDetailsController.SHOPID_KEY, shopID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 车型报价
     *
     * @param seriesID 滑动到指定车型
     * @param context
     */
    public static void toModelsOfferBySericesID(String seriesID, Context context) {
        Intent intent = new Intent(context, ModelsOfferActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ShopDetailsController.SERIESID_KEY, seriesID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 促销列表
     */
    public static void toShopPromotionList(String shopID, Context context) {
        Intent intent = new Intent(context, ShopSalesPromotionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ShopDetailsController.SHOPID_KEY, shopID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 维修首页
     *
     * @param shopID
     * @param context
     */
    public static void toMaintenanHome(String shopID, Context context) {
        Intent intent = new Intent(context, MaintenanceHomeActivity_1.class);
        Bundle bundle = new Bundle();
        bundle.putString(ShopDetailsController.SHOPID_KEY, shopID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 分期购车
     *
     * @param shopID
     * @param context
     */
    public static void toInstallmentBuyCar(String shopID, Context context) {
        Intent intent = new Intent(context, InstallmentBuyCarActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ShopDetailsController.SHOPID_KEY, shopID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 店铺特卖列表
     *
     * @param shopID
     * @param context
     */
    public static void toShopFlashSaleList(String shopID, Context context) {

        Intent intent = new Intent(context, ShopFlashSaleListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ShopDetailsController.SHOPID_KEY, shopID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
//        context.startActivity(new Intent(context, ShopFlashSaleListActivity.class).putExtra(ShopDetailsController.SHOPID_KEY, shopID));
    }


    public static void toMaintenanHomeWithFlag(String shopID, Context context) {
        context.startActivity(new Intent(context, MaintenanceHomeActivity_1.class).putExtra(ShopDetailsController.SHOPID_KEY, shopID).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }


    /**
     * 新闻资讯查看大图
     */
    public static void toViewNewsImage(Context context) {
        Intent intent = new Intent(context, NewsImageInfoActivity.class);
        context.startActivity(intent);
    }


    /**
     * 车辆详情页
     *
     * @param itemID
     * @param shopID
     * @param context
     */
    public static void toCarDetail(String itemID, String shopID, String shopTitle, Context context) {
        Intent intent = new Intent(context, FavorableCarDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FavorableCarDetailActivity.SHOPID, shopID);
        bundle.putString(FavorableCarDetailActivity.ITEMID, itemID);
        bundle.putString(FavorableCarDetailActivity.SHOPTITLE, shopTitle);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 跳转套试乘试驾
     *
     * @param context
     * @param shopID           店铺ID
     * @param carTypeName      车型名称  如果是从活动页跳转，请默认填写活动车型中的第一个车型名称
     * @param itemID           车辆id值，如果来自活动，请默认填写第一个车辆ID值
     * @param isFromCertainCar 是否来某一确定车型，来自确定车型填true，其他填false，例如来自某个活动页则填true
     * @param salesItems       活动中的车型列表 如果来自某一确定车型，请填null（如果isFromCertainCar为true则此处填null，否则填具体ArrayList）
     */
    public static void toTestDrive(Context context, String shopID, String itemID, String carTypeName, String shopTel, boolean isFromCertainCar, ArrayList< SalesItem > salesItems) {
        Intent intent = new Intent(context, TestDriveActivity.class);
        intent.putExtra("shopID", shopID);
        intent.putExtra("carTypeName", carTypeName);
        intent.putExtra("shopTel", shopTel);
        intent.putExtra("itemID", itemID);
        intent.putExtra(ActivitySwitcherThirdPartShop.FROMCERTAINCAR, isFromCertainCar);
        if (!isFromCertainCar) {
            intent.putParcelableArrayListExtra(ActivitySwitcherThirdPartShop.INTENT_TYPE, salesItems);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    /**
     * 跳转到询问底价
     *
     * @param context
     * @param shopID           店铺ID
     * @param carTypeName      车型名称 如果是从活动页跳转，请默认填写活动车型中的第一个车型名称
     * @param itemID           车辆id值，如果来自活动，请默认填写第一个车辆ID值
     * @param isFromCertainCar 是否来某一确定车型，来自确定车型填true，其他填false，例如来自某个活动页则填true
     * @param salesItems       活动中的车型列表 如果来自某一确定车型，请填null （如果isFromCertainCar为true则此处填null，否则填具体ArrayList）
     */
    public static void toAskLeastPrice(Context context, String shopID, String itemID, String carTypeName, String shopTEl, boolean isFromCertainCar, ArrayList< SalesItem > salesItems) {
        Intent intent = new Intent(context, AskLeastMoneyActivity.class);
        intent.putExtra("shopID", shopID);
        intent.putExtra("carTypeName", carTypeName);
        intent.putExtra("shopTel", shopTEl);
        intent.putExtra("itemID", itemID);
        intent.putExtra(ActivitySwitcherThirdPartShop.FROMCERTAINCAR, isFromCertainCar);
        if (!isFromCertainCar) {
            intent.putParcelableArrayListExtra(ActivitySwitcherThirdPartShop.INTENT_TYPE, salesItems);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    /**
     * 跳转到协议页面
     *
     * @param context
     */
    public static void toDeclares(Context context) {
        String url = ApiUtil.AccountHostURL + "/Shop/Article/Index/declares";
        Intent intent = new Intent(context, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, context.getString(R.string.third_title_activity_clause_detail));
        bundle.putString(WebActivity.URL, url);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 订单详情
     *
     * @param context
     * @param orderID 订单ID值
     */
    public static void toOrderDetail(Context context, String orderID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        intent.putExtra(KEY_DATA, bundle);
        intent.setClass(context, ThirdShopOrderDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 促销 确认订单信息
     *
     * @param context
     */
    public static void toConfirmOrder(SalesDetail salesDetail, Context context) {
        Intent intent = new Intent(context, SalesOrderConfirmActivity.class);
        intent.putExtra(CONFIRM_ORDER_DATA_TAG, salesDetail);
        context.startActivity(intent);
    }


    /**
     * 促销  预付订金
     */
    public static void toPayDeposit(String amount, String orderID, String shopTel, Context context) {
        Intent intent = new Intent(context, SalesDepositPayActivity.class);
        intent.putExtra(SHOP_ORDER_ID, orderID);
        intent.putExtra(DEPOSIT_AMOUNT, amount);
        intent.putExtra(SHOP_TEL, shopTel);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, 1);
        } else {
            context.startActivity(intent);
        }
    }


    /**
     * 发票
     */
    public static void toInvoice(boolean accessory, InvoiceInfo invoiceInfo, Activity activity) {
        Intent intent = new Intent(activity, InvoiceActivity.class);
        Bundle bundle = new Bundle();
        if (accessory) {
            bundle.putString(FROM, ACCESSORY);
        } else {
            bundle.putString(FROM, MAINTENANCE);
        }
        bundle.putParcelable(INVOICE_INFO, invoiceInfo);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivityForResult(intent, TO_INVOICE);
    }


    /**
     * 支付完成
     */
    public static void finishPay(Context context) {
        Intent intent = new Intent(context, DepositFinishActivity.class);
        context.startActivity(intent);
    }


    public static void toAMapNvai(Context context, int ot, PickupPointT shopLocation) {
        //因代码调整暂时注释
        Intent intent = new Intent(context, ThirdShopAMapActivity.class);
        intent.putExtra(SHOP_AMAP, shopLocation);
        intent.putExtra(MAP_OPERATOR, ot);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 店铺详情
     *
     * @param context
     * @param shopID  店铺ID
     */
    public static void toShopDetailsRescue(Context context, String shopID) {
        Intent intent = new Intent(context, ShopHomeActivity.class);
//        intent.putExtra(ShopDetailsController.SHOPID_KEY, shopID);
        Bundle bundle = new Bundle();
        bundle.putString(ShopDetailsController.SHOPID_KEY, shopID);
        bundle.putInt(ShopDetailsController.FROM_KEY, ShopDetailsController.FROM_RESCUE);//跳转来源 0:4s店，1：紧急救援
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 店铺详情
     *
     * @param context
     * @param shopID  店铺ID
     */
    public static void toShopDetails(Context context, String shopID) {
        Intent intent = new Intent(context, ShopHomeActivity.class);
//        intent.putExtra(ShopDetailsController.SHOPID_KEY, shopID);
        Bundle bundle = new Bundle();
        bundle.putString(ShopDetailsController.SHOPID_KEY, shopID);
        bundle.putInt(ShopDetailsController.FROM_KEY, ShopDetailsController.FROM_4S); //默认 4S店进入
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 跳转店铺简介H5
     *
     * @param context
     * @param bundle
     */
    public static void toShopProFileH5(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ShopProFileWebActivity.class);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 图集
     */
    public static void toAtlas(Context context, String itemID) {
        Intent intent = new Intent(context, ThirdShopAltasActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("item_id", itemID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    public static void toAtlasExtID(Context context, String itemID) {
        Intent intent = new Intent(context, ThirdShopAltasActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isExtID", true);
        bundle.putString("item_id", itemID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 店铺列表
     */
    public static void to4SShopList(Context context, String siteID) {
        Intent intent = new Intent(context, FourSShopListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SITE_ID, siteID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 车型列表  由新车销售选择后进入
     * 胡俊杰
     *
     * @param context
     * @param brandName  品牌名称
     * @param seriesName 车系名称
     */
    public static void toAutoModel(Context context, String brandName, String seriesName, Series series) {
        Intent intent = new Intent(context, ShopAutoModelActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BRAND, brandName);
        bundle.putString(SERIESNAME, seriesName);
        bundle.putParcelable(SERIES, series);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 参数详情
     *
     * @param context
     * @param extID
     */
    public static void toParameter(Context context, String extID) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.activity.auto.ParameterActivity");
        Bundle bundle = new Bundle();
        bundle.putString("data", extID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    public static void toParameter(boolean isExtID, Context context, String extID) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.activity.auto.ParameterActivity");
        Bundle bundle = new Bundle();
        bundle.putBoolean("isExtID", isExtID);
        bundle.putString("data", extID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 定位选择
     */
    public static void toPositionActivity(Context context, int requestCode, String position) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(context, PositionActivity.class);
        intent.setComponent(componentName);
        Bundle bundle = new Bundle();
        bundle.putString("position", position);
        intent.putExtra(KEY_DATA, bundle);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }


    /**
     * 查询整车质保
     *
     * @param context
     */
    public static void toQualityGuaranteeActivity(Context context) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.activity.auto.QualityGuaranteeActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 新车销售列表
     *
     * @param context
     */
    public static void toNewCarSaleList(Context context, String areaID) {
        Intent intent = new Intent(context, NewCarSeriesListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(NewCarSeriesListActivity.AREAID, areaID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 限时特卖销售列表
     *
     * @param context
     */
    public static void toFlashSaleList(Context context, String areaID, boolean isIndexApi) {
        Intent intent = new Intent(context, FlashSaleListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FlashSaleListActivity.AREAID, areaID);
        bundle.putBoolean(FlashSaleListActivity.IS_INDEX_API, isIndexApi);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 特价车筛选
     *
     * @param activity
     * @param areaID
     * @param REQUEST_CODE
     */
    public static void toFilterFlashSaleCar(Activity activity, String areaID, int REQUEST_CODE) {
        Intent intent = new Intent(activity, FilterSeckillIndefiniteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FilterSeckillIndefiniteActivity.SITEID, areaID);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }


    /**
     * 咨询列表
     *
     * @param context
     * @param advisories
     */
    public static void toAdvisoryList(Context context, ArrayList< Advisory > advisories) {
        Intent intent = new Intent(context, AdvisoryListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AdvisoryListActivity.ITEMS, advisories);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 跳特卖车详情页
     **/
    public static void toSpecialCarDetail(Context context, String itemID) {
        Intent intent = new Intent(context, SpecialCarDetailMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SpecialCarDetailMainActivity.ITEMID, itemID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 分站新闻列表
     *
     * @param context
     * @param siteID
     */
    public static void toSiteNewsList(Context context, String siteID) {
        Intent intent = new Intent(context, SiteNewsListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SiteNewsListActivity.SITE_ID, siteID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 跳特卖车订单信息确认界面
     **/
    public static void toConfirmSpecialCarOrder(Context context, HashMap< String, String > hashMap, String shopTel) {
        Intent intent = new Intent(context, ConfirmSpecialCarOrderActivity.class);
        intent.putExtra(ConfirmSpecialCarOrderActivity.SHOPTEL, shopTel);
        intent.putExtra(ConfirmSpecialCarOrderActivity.DATA, hashMap);
        ((Activity) context).startActivityForResult(intent, 1);
    }


    /**
     * 特卖车分站选择
     */
    public static void toSpecialCarChoosePositon(Context context, int requestCode, String position) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(context, PositionActivityForSpecialCar.class);
        intent.setComponent(componentName);
        intent.putExtra("position", position);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }


//    /**
//     * 4s首页
//     */
//    @Deprecated
//    public static void to4S(Context context) {
//        Intent intent = new Intent(context, FourSMallActivity2.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }


    /**
     * 4s首页
     */
    public static void to4S(Context context, int tab) {
        Intent intent = new Intent(context, FourSMallActivity.class);
        intent.putExtra(TAB_4S, tab);
        new SharedPreferencesHelper(context).set4STabChange(true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 车型列表
     *
     * @param context
     * @param areaID
     * @param series
     */
    public static void toNewCarModelList(Context context, String areaID, String brandName, Series series, boolean from4S) {
        Intent intent = new Intent(context, NewCarModelListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(NewCarModelListActivity.BRAND, brandName);
        bundle.putString(NewCarModelListActivity.AREAID, areaID);
        bundle.putParcelable(NewCarModelListActivity.SERIES, series);
        bundle.putBoolean(NewCarModelListActivity.FROM4SMENU, from4S);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 店铺列表
     *
     * @param context
     * @param areaID
     * @param series
     * @param modelName
     */
    public static void toShopInfoOfNewCarList(Context context, String areaID, String series, String modelName) {
        Intent intent = new Intent(context, ShopInfoOfNewCarListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ShopInfoOfNewCarListActivity.AREAID, areaID);
        bundle.putString(ShopInfoOfNewCarListActivity.SERIES, series);
        bundle.putString(ShopInfoOfNewCarListActivity.MODEL, modelName);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 车型列表
     *
     * @param context
     * @param areaID
     * @param series
     */
    public static void toNewCarModelList2(Context context, String areaID, String brand, String series) {
        Intent intent = new Intent(context, NewCarModelListActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putString(NewCarModelListActivity2.AREAID, areaID);
        bundle.putString(NewCarModelListActivity2.SERIES, series);
        bundle.putString(NewCarModelListActivity2.BRAND, brand);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * 车型详情
     *
     * @param context
     * @param areaID
     * @param model
     */
    public static void toNewCarModelDetail(Context context, String areaID, String extID, String brand, String model) {
        Bundle bundle = new Bundle();
        bundle.putString("extID", extID);
        bundle.putString(NewCarModelDetailActivity.MODEL_NAME, model);
        bundle.putString(NewCarModelDetailActivity.AREA_ID, areaID);
        bundle.putString(NewCarModelDetailActivity.BRAND, brand);
        Intent intent = new Intent(context, NewCarModelDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * 跳转到询问底价
     *
     * @param context
     * @param carTypeName 车型名称 如果是从活动页跳转，请默认填写活动车型中的第一个车型名称
     * @param extID       车辆id值，如果来自活动，请默认填写第一个车辆ID值
     */
    public static void toAskLeastPrice2(Context context, String siteId, String shopSiteFrom, String extID, String brand, Series mSeries, String carTypeName) {
        Intent intent = new Intent(context, AskLeastMoneyActivity2.class);
        intent.putExtra("carTypeName", carTypeName);
        intent.putExtra("extID", extID);
        intent.putExtra("shopSiteFrom", shopSiteFrom);
        intent.putExtra("siteId", siteId);
        intent.putExtra("brand", brand);
        intent.putExtra("series", mSeries);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    /**
     * 车型筛选
     *
     * @param context
     * @param brandName
     * @param seriesName
     */
    public static void toFilterCarModel(Context context, String brandName, String seriesName) {
        Intent intent = new Intent(context, FilterCarModelActivity.class);
        intent.putExtra("brandName", brandName);
        intent.putExtra("seriesName", seriesName);
        context.startActivity(intent);
    }


    /**
     * 大图
     *
     * @param context
     * @param url
     * @param pos
     */
    public static void toPhotoView(Context context, String[] url, int pos) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.photolibrary.activity.ImagePagerActivity");
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, pos);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, url);
        context.startActivity(intent);
    }


    /**
     * 汽车资讯图文
     *
     * @param context
     * @param infoID
     */
    public static void toAutoInfoDetail(Context context, String infoID) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.autonews.activities.AutoInfoDetailActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("infoID", infoID);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        context.startActivity(intent);
    }
    /**
     * 去新车日历界面
     *
     * @param context
     */
    public static void toNewAutoCalendar(Context context) {
        Intent intent = new Intent();
        intent.setClassName(context,"com.hxqc.autonews.activities.NewAutoCalendarActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 全部评论
     *
     * @param context
     * @param extID
     * @param brand
     * @param series
     */
    public static void toPublicCommentList2(Context context, String extID, String brand, String series) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.carcompare.ui.discuss.UserDiscussActivity");
        Bundle bundle = new Bundle();
        bundle.putString("extID", extID);
        bundle.putString("brand", brand);
        bundle.putString("series", series);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * 发表 口碑评价
     *
     * @param context
     * @param extID   车型ID (no)
     * @param brand   品牌(yes)
     * @param series  车系(no)
     */
    public static void toSendPublicComment(final Activity context, final String extID, final String brand, final String series, final String image) {
        UserInfoHelper.getInstance().loginAction(context, new UserInfoHelper.OnLoginListener() {
            @Override
            public void onLoginSuccess() {
                Intent intent = new Intent();
                intent.setClassName(context, "com.hxqc.autonews.activities.SendCommentActivity");
                Bundle bundle = new Bundle();
                bundle.putString(FilterResultKey.EXT_ID, extID);
                bundle.putString(FilterResultKey.BRAND_KEY, brand);
                bundle.putString(FilterResultKey.SERIES_KEY, series);
                bundle.putString(FilterResultKey.IMG_KEY, image);
                intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
                context.startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }


    /**
     * 车型对比 添加对比车型界面
     *
     * @param context
     */
    public static void toCarCompare(Context context, String className) {
        Intent intent = new Intent();
        intent.setClassName(context, className);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 团购汇
     *
     * @param bundle 此处bundle目前只用传递是显示 “返回首页” 按钮 还是 “分站” 按钮的布尔值
     */
    public static void toGroupBuyMerge(Context context, Bundle bundle) {
        Intent intent = new Intent(context, GroupBuyMergeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        context.startActivity(intent);
    }
}
