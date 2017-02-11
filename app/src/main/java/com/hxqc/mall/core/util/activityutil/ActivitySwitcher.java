package com.hxqc.mall.core.util.activityutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hxqc.aroundservice.activity.TrafficControlActivity;
import com.hxqc.fastreqair.activity.CarWashOrderDetailsActivity;
import com.hxqc.mall.activity.AMapAutoSaleActivity;
import com.hxqc.mall.activity.MainActivity;
import com.hxqc.mall.activity.WebActivity;
import com.hxqc.mall.activity.auto.AtlasActivity;
import com.hxqc.mall.activity.auto.AutoItemDetailActivity;
import com.hxqc.mall.activity.auto.AutoItemDetailCommonActivity;
import com.hxqc.mall.activity.auto.AutoItemDetailPromotionActivity;
import com.hxqc.mall.activity.auto.AutoListActivity;
import com.hxqc.mall.activity.auto.AutoPackageChooseActivity;
import com.hxqc.mall.activity.auto.BrandActivity;
import com.hxqc.mall.activity.auto.EventDetailActivity;
import com.hxqc.mall.activity.auto.FilterAutoActivity;
import com.hxqc.mall.activity.auto.ParameterActivity;
import com.hxqc.mall.activity.auto.PromotionQueueWaitActivity;
import com.hxqc.mall.activity.auto.SearchActivity;
import com.hxqc.mall.activity.auto.SpecialOfferActivity;
import com.hxqc.mall.activity.coupon.CouponDetailActivity;
import com.hxqc.mall.activity.coupon.MyCouponActivity;
import com.hxqc.mall.activity.me.AboutUsActivity;
import com.hxqc.mall.activity.me.AddAddressActivity;
import com.hxqc.mall.activity.me.AdviceActivity;
import com.hxqc.mall.activity.me.ComplaintsActivity2;
import com.hxqc.mall.activity.me.ComplaintsActivity;
import com.hxqc.mall.activity.me.DeliveryAddressActivity;
import com.hxqc.mall.activity.me.EditAddressActivity;
import com.hxqc.mall.activity.me.ExpressActivity;
import com.hxqc.mall.activity.me.MyWalletActivity;
import com.hxqc.mall.activity.me.PersonalInfoActivity;
import com.hxqc.mall.activity.me.ResetUserInfoActivity;
import com.hxqc.mall.activity.me.SettingsActivity;
import com.hxqc.mall.activity.me.UserCommentActivity;
import com.hxqc.mall.activity.me.WishListActivity;
import com.hxqc.mall.activity.me.bill.BillDetailActivity;
import com.hxqc.mall.activity.me.bill.MyBillListActivity;
import com.hxqc.mall.activity.me.message.MessageListActivity;
import com.hxqc.mall.activity.me.message.MyMessageActivity;
import com.hxqc.mall.activity.me.password.ForgetPWDActivity;
import com.hxqc.mall.activity.me.password.ForgetPayPWDStep2Activity;
import com.hxqc.mall.activity.me.password.ModifierPaidPWDActivity;
import com.hxqc.mall.activity.me.password.ModifierPayPWDStep2Activity;
import com.hxqc.mall.activity.me.password.RealNameAuthenticationActivity;
import com.hxqc.mall.activity.order.Accessory4SShopOrderCancelActivity;
import com.hxqc.mall.activity.order.Accessory4SShopOrderRefundActivity;
import com.hxqc.mall.activity.order.AccessoryOrderCancelActivity;
import com.hxqc.mall.activity.order.ApplicationRefundActivity;
import com.hxqc.mall.activity.order.InvoiceInfoActivity;
import com.hxqc.mall.activity.order.LoanBankChooseActivity;
import com.hxqc.mall.activity.order.Maintain4SShopOrderDetailsActivity;
import com.hxqc.mall.activity.order.MaintainOrderCancelActivity;
import com.hxqc.mall.activity.order.MaintainOrderDetailsActivity;
import com.hxqc.mall.activity.order.MaintainOrderRefundActivity;
import com.hxqc.mall.activity.order.MaintainSendCommentActivity;
import com.hxqc.mall.activity.order.MyOrderActivity;
import com.hxqc.mall.activity.order.OrderRefundInfoActivity;
import com.hxqc.mall.activity.order.RepairOrderCancelActivity;
import com.hxqc.mall.activity.order.RepairOrderDetailsActivity;
import com.hxqc.mall.activity.order.SeckillOrderCancelActivity;
import com.hxqc.mall.activity.order.SeckillOrderDetailActivity;
import com.hxqc.mall.activity.order.SeckillOrderRefundActivity;
import com.hxqc.mall.activity.recharge.PaymentWayListActivity;
import com.hxqc.mall.activity.recharge.RechargeActivity;
import com.hxqc.mall.activity.recharge.RechargePayActivity;
import com.hxqc.mall.activity.recharge.RechargeSuccessActivity;
import com.hxqc.mall.control.BillTypeHelper;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.model.Brand;
import com.hxqc.mall.core.model.DeliveryAddress;
import com.hxqc.mall.core.model.InvoiceModel;
import com.hxqc.mall.core.model.Series;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.model.recharge.RechargeRequest;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.fragment.auto.AutoBuyVerifyFragment;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.thirdshop.maintenance.activity.EmergencyRescueActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.FilterMaintenanceShopListActivity2;
import com.hxqc.mall.thirdshop.maintenance.model.coupon.Coupon;
import com.hxqc.util.JSONUtils;
import com.sdu.didi.openapi.DiDiWebActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hxqc.mall.R;



/**
 * 说明:界面切换
 * <p/>
 * author: 吕飞
 * since: 2015-03-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class ActivitySwitcher extends ActivitySwitchBase {
    public static final String DELIVERY_ADDRESS = "delivery_address";
    public static final String ORDER_ID = "order_id";
    public static final String SHOP_PHOTO = "shop_photo";
    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_NAME = "shop_name";
    public static final String TYPE = "type";
    public static final String WROTE_NUM = "wrote_num";
    public static final String PAYMENT_STATUS = "payment_status";
    public static final String ITEM_ID = "item_id";
    public static final String SERIES_ID = "series_id";
    public static final String INVOICEINFO = "invoice_info";


    //---------------商品列表
    public static final String KEYWORD = "keyword";
    public static final String SERIES = "seriesName";
    public static final String FILTER = "filter";
    public static final String TITLE = "title";
    public static final String REGISTER_AGREEMENT = "register_agreement";
    /**
     * 开启高德导航
     */
    public static final String ORDER_DETAIL = "order_detail";


    /**
     * didi代驾
     */
    public static void toDiDi(Context context) {
        HashMap< String, String > map = new HashMap();
        map.put("biz", "5"); //代驾参数
        DiDiWebActivity.showDDPage(context, map);
    }


    /**
     * 主界面
     *
     * @param context
     */
    public static void homeToPromotion(Context context) {
        toMain(context, 1);
        ((MainActivity) context).overridePendingTransition(R.anim.fragment_right_in, R.anim.fragment_left_out);
    }


    /**
     * 帮助中心
     *
     * @param context http://www.hxqc.com/Help/Help.html
     */
    public static void toHelpCenter(Context context) {
        Intent intent = new Intent(context, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, context.getString(R.string.title_activity_me_helpCenter));
        bundle.putString(WebActivity.URL, "http://www.hxqc.com/Help/Help.html");
        intent.putExtras(bundle);
        context.startActivity(intent);

    }


    /**
     * 关于我们
     *
     * @param context
     */
    public static void toAboutUs(Context context) {
        Intent intent = new Intent(context, AboutUsActivity.class);
//        intent.setAction(context.getResources().getString(R.string.action_about_us));
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 电商自营
     *
     * @param context
     */
    public static void toSpecialOffer(Context context) {
        Intent intent = new Intent(context, SpecialOfferActivity.class);
//        intent.setAction("MOD_SECKILL");
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 电商自营
     *
     * @param context
     */
    public static void toBrand(Context context) {
        Intent intent = new Intent(context, BrandActivity.class);
        context.startActivity(intent);
    }


    public static void toBrand(Context context, Brand brand) {
        Intent intent = new Intent(context, BrandActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("itemCategory", String.valueOf(brand.brandType));
        bundle.putParcelable("brand", brand);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 个人资料
     *
     * @param context
     */
    public static void toPersonalInfo(Context context) {
        Intent intent = new Intent(context, PersonalInfoActivity.class);
//        intent.setAction(context.getResources().getString(R.string.action_personal_info));
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    public static void toResetUserInfo(Context context) {
        context.startActivity(new Intent(context, ResetUserInfoActivity.class));
    }




    /**
     * @param context
     */
    public static void toOfficialWebsite(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse("http://www.hxqc.com/"));
        context.startActivity(intent);
    }


    /**
     * 恒信官方网站
     *
     * @param context
     */
    public static void toSettings(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 我的关注
     *
     * @param context
     */
    public static void toWishList(Context context) {
        Intent intent = new Intent(context, WishListActivity.class);
        context.startActivity(intent);
    }


    /**
     * 消息管理
     *
     * @param context
     */
    public static void toMessage(Context context, MessageListActivity.MessageListTypeEnum messageType) {
        Intent intent = new Intent(context, MessageListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(MessageListActivity.MESSAGE_TYPE, messageType.value());
        intent.putExtra(KEY_DATA, bundle);
//        intent.putExtra(MessageListActivity.MESSAGE_TYPE, messageType.value());
//        intent.setAction(context.getResources().getString(R.string.action_message));
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 意见反馈
     *
     * @param context
     */
    public static void toAdvice(Context context) {
        Intent intent = new Intent(context, AdviceActivity.class);
//        intent.setAction(context.getResources().getString(R.string.action_advice));
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 首页上部到导航
     *
     * @param context
     * @param key
     */
    public static void toModule(Context context, String key) {
        Intent intent = new Intent();
        intent.setAction(key);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 收货地址
     *
     * @param context
     */
    public static void toDeliveryAddress(Context context) {
        Intent intent = new Intent(context, DeliveryAddressActivity.class);
//        intent.setAction(context.getResources().getString(R.string.action_delivery_address));
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 新增地址
     *
     * @param context
     */
    public static void toAddAddress(Context context) {
        Intent intent = new Intent(context, AddAddressActivity.class);
//        intent.setAction(context.getResources().getString(R.string.action_add_address));
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 我的评论
     *
     * @param context
     */
    public static void toUserComment(Context context) {
        Intent intent = new Intent(context, UserCommentActivity.class);
//        intent.setAction(context.getResources().getString(R.string.action_user_comment));
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 发表评论
     *
     * @param context
     */
    public static void toSendComment(Context context, String itemID, String orderID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, itemID);
        bundle.putString(ORDER_ID, orderID);
        intent.putExtras(bundle);
        intent.setClassName(context, "com.hxqc.mall.activity.comment.SendCommentActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 订单追踪
     *
     * @param context
     */
    public static void toExpress(Context context, String orderID) {
        Intent intent = new Intent(context, ExpressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID, orderID);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 查看评论
     *
     * @param context
     */
    public static void toComment(Context context, String itemID, String seriesID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, itemID);
        bundle.putString(SERIES_ID, seriesID);
        intent.putExtras(bundle);
        intent.setClassName(context, "com.hxqc.mall.activity.comment.CommentActivity");
//        intent.setAction(context.getResources().getString(R.string.action_comment));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 浏览历史
     *
     * @param context
     */
//    public static void toHistory(Context context) {
//        Intent intent = new Intent(context, HistoryActivity.class);
////        intent.setAction(context.getResources().getString(R.string.action_history));
////        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(intent);
//    }


    /**
     * 支付
     *
     * @param context
     * @param flag
     */
    public static void toPayMain(final Context context, final int flag, final String orderID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(PayConstant.PAY_STATUS_FLAG, flag);
        bundle.putString(ORDER_ID, orderID);
        intent.putExtras(bundle);
//        intent.setAction(context.getResources().getString(R.string.action_pay_main));
        intent.setClassName(context, "com.hxqc.pay.activity.PayMainActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 支付余款
     *
     * @param context
     * @param flag
     */
    public static void toPaySpareMoney(final Context context, final int flag, final String orderID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(PayConstant.PAY_STATUS_FLAG, flag);
        bundle.putString(ORDER_ID, orderID);
        intent.putExtras(bundle);
//        intent.setAction(context.getResources().getString(R.string.action_pay_spare_money));
        intent.setClassName(context, "com.hxqc.pay.activity.PaySpareMoneyActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 支付订金
     *
     * @param context
     * @param flag
     */
    public static void toDeposit(final Context context, final int flag, final String orderID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(PayConstant.PAY_STATUS_FLAG, flag);
        bundle.putString(ORDER_ID, orderID);
        intent.putExtras(bundle);
//        intent.setAction(context.getResources().getString(R.string.action_deposit));
        intent.setClassName(context, "com.hxqc.pay.activity.DepositActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 编辑地址
     *
     * @param context
     */
    public static void toEditAddress(Context context, DeliveryAddress deliveryAddress) {
        Intent intent = new Intent(context, EditAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DELIVERY_ADDRESS, deliveryAddress);
        intent.putExtra(TYPE, DELIVERY_ADDRESS);
        intent.putExtras(bundle);
//        intent.setAction(context.getResources().getString(R.string.action_edit_address));
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 车辆详情
     *
     * @param context
     * @param itemType
     * @param ITEM_ID
     * @param title
     */
    public static void toAutoItemDetail(Context context, String itemType, String ITEM_ID, String title) {
        switch (itemType) {
            case AutoItem.AUTO_COMMON:
                toCommonItemDetail(context, ITEM_ID, title, AutoItem.CATEGORY_AUTOMOBILE);
                break;
            case AutoItem.AUTO_PROMOTION:
                toPromotionItemDetail(context, ITEM_ID, title, AutoItem.CATEGORY_AUTOMOBILE);
                break;
        }
    }


    /**
     * 车辆详情 新能源
     *
     * @param context
     * @param itemType
     * @param ITEM_ID
     * @param title
     * @param itemCategory 20
     */
    public static void toAutoItemDetail(Context context, String itemType, String ITEM_ID, String title, String itemCategory) {
        switch (itemType) {
            case AutoItem.AUTO_COMMON:
                toCommonItemDetail(context, ITEM_ID, title, AutoItem.CATEGORY_NEW_ENERGY);
                break;
            case AutoItem.AUTO_PROMOTION:
                toPromotionItemDetail(context, ITEM_ID, title, AutoItem.CATEGORY_NEW_ENERGY);
                break;
        }
    }


    /**
     * 车辆详情
     */
    static void toCommonItemDetail(Context context, String ITEM_ID, String title, int itemCategory) {
        Intent intent = new Intent(context, AutoItemDetailCommonActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_DATA, ITEM_ID);
        intent.putExtra(AutoItem.ItemCategory, itemCategory);
        intent.putExtra(TITLE, title);
        context.startActivity(intent);
    }


    /**
     * 电商自营
     */
    static void toPromotionItemDetail(Context context, String ITEM_ID, String title, int itemCategory) {
        Intent intent = new Intent(context, AutoItemDetailPromotionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_DATA, ITEM_ID);
        intent.putExtra(AutoItem.ItemCategory, itemCategory);
        intent.putExtra(TITLE, title);
        context.startActivity(intent);
    }


    /**
     * 找车
     *
     * @param context
     */
    public static void toAutoFilter(Context context, String itemCategory) {
        Intent intent = new Intent(context, FilterAutoActivity.class);
        intent.putExtra(AutoItem.ItemCategory, itemCategory);
        context.startActivity(intent);
    }


    /**
     * 车辆列表
     *
     * @param context
     */
    public static void toAutoList(Context context, Series autoSeries, int itemCategory) {
        Bundle bundle = new Bundle();
        bundle.putString(SERIES, autoSeries.getSeriesName());
        bundle.putInt(AutoItem.ItemCategory, itemCategory);
        toAutoList(context, bundle);
    }


    public static void toAutoList(Context context, String keyword) {
        Bundle bundle = new Bundle();
        bundle.putString(KEYWORD, keyword);
        toAutoList(context, bundle);
    }


    public static void toAutoList(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AutoListActivity.class);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    public static void toAutoList(Context context, Map< String, String > FilterMap) {
        Bundle bundle = new Bundle();
        bundle.putString(FILTER, JSONUtils.toJson(FilterMap));
        toAutoList(context, bundle);
    }


    public static void toAutoList(Context context) {
        Intent intent = new Intent(context, AutoListActivity.class);
        context.startActivity(intent);
    }


    /**
     * 图集
     */
    public static void toAtlas(Context context, String itemID) {
        Intent intent = new Intent(context, AtlasActivity.class);
        intent.putExtra("item_id", itemID);
        context.startActivity(intent);
    }


    /**
     * 参数详情
     *
     * @param context
     * @param itemID
     */
    public static void toParameter(Context context, String itemID) {
        Intent intent = new Intent(context, ParameterActivity.class);
        intent.putExtra("data", itemID);
        context.startActivity(intent);
    }


    /**
     * 搜索
     */
    public static void toSearch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 搜索
     */
    public static void toQueueWait(Context context) {
        Intent intent = new Intent(context, PromotionQueueWaitActivity.class);
        context.startActivity(intent);
    }


    /**
     * 我的订单
     */
    public static void toMyOrder(Context context) {
        Intent intent = new Intent(context, MyOrderActivity.class);
        context.startActivity(intent);
    }


    /**
     * 活动详情
     */
    public static void toEventDetail(Context context, String url) {
        Intent intent = new Intent(context, EventDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, "活动");
        bundle.putString(WebActivity.URL, url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * 申请退款
     */
    public static void applicationRefund(String order_id, Context context) {
        Intent intent = new Intent(context, ApplicationRefundActivity.class);
        intent.putExtra("order_id", order_id);
        context.startActivity(intent);
    }


    public static void toAMapNvai(Context context, String order_detail) {
        Intent intent = new Intent(context, AMapAutoSaleActivity.class);
        intent.putExtra(ORDER_DETAIL, order_detail);
        context.startActivity(intent);
    }


    /**
     * 套餐选择页面
     *
     * @param context
     */
    public static void toAutoPackageChoose(Context context, int position) {
        Intent intent = new Intent(context, AutoPackageChooseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_DATA, position);
        intent.putExtra(KEY_DATA, bundle);
//        intent.putExtra(KEY_DATA, position);
        ((Activity) context).startActivityForResult(intent, AutoItemDetailActivity.PACKAGE_RESULT);
    }


    /**
     * 贷款机构选择
     * 王颢
     */
    public static void chooseLoanBank(Context context, Fragment fragment, String financeID) {
        Intent intent = new Intent(context, LoanBankChooseActivity.class);
        intent.putExtra(KEY_DATA, financeID);
        fragment.startActivityForResult(intent, AutoBuyVerifyFragment.LOAN_BANK_RESULT);
    }


    /**
     * 保险试算
     * 胡俊杰
     */
    public static void toInsuranceCounter(Context context, String itemID) {
        String url = ApiUtil.AccountHostURL + "/Mall/App/Insurance/insuranceCounter?itemID=" + itemID;
        Intent intent = new Intent(context, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, context.getString(R.string.title_activity_insurance_counter));
        bundle.putString(WebActivity.URL, url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * QA界面
     *
     * @param context
     */
    public static void toQAPage(Context context) {
        Intent intent = new Intent(context, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, context.getString(R.string.title_activity_QA));
        intent.putExtra(WebActivity.URL, "file:///android_asset/QA.html");
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 我的消息界面
     *
     * @param context
     */
    public static void toMyMessageActivity(Context context) {
        Intent intent = new Intent(context, MyMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 充值界面
     *
     * @param context
     */
    public static void toRecharge(Context context) {
        context.startActivity(new Intent(context, RechargeActivity.class));
    }



    /**
     * 我的钱包
     *
     * @param context
     */
    public static void toMyWallet(Context context) {
        Intent intent = new Intent(context, MyWalletActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 我的优惠券列表
     *
     * @param context
     */
//    @Deprecated
//    public static void toMyCoupon(Context context) {
//        toAvailableCouponList(context, null);
//
//    }

    /**
     * 可用优惠券列表
     *
     * @param context
     * @param combinations 优惠券组合
     */
//    @Deprecated
//    public static void toAvailableCouponList(Context context, ArrayList<CouponCombination> combinations) {
//        Intent intent = new Intent(context, MyCouponActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(MyCouponActivity.COUPON_COMBINATION, combinations);
//        intent.putExtra(KEY_DATA, bundle);
//        context.startActivity(intent);
//    }


    /**
     * 我的优惠券列表
     *
     * @param context
     */
    public static void toMyCoupon(Context context, String myAutoID) {
        Intent intent = new Intent(context, MyCouponActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(MyCouponActivity.COUPONS, coupons);
        bundle.putString(MyCouponActivity.MY_AUTO_ID, myAutoID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 可用优惠券列表
     *
     * @param context
     * @param coupons 优惠券组合
     */
    public static void toAvailableCouponList(Activity context, ArrayList< Coupon > coupons, int requestCode) {
        Intent intent = new Intent(context, MyCouponActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MyCouponActivity.COUPONS, coupons);
//        bundle.putString(MyCouponActivity.MY_AUTO_ID, myAutoID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivityForResult(intent, requestCode);
    }


    /**
     * 我的账单
     *
     * @param context
     * @param isBalance 是否是现金余额账单
     */
    public static void toMyBillList(Context context, boolean isBalance) {
        Intent intent = new Intent(context, MyBillListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(MyBillListActivity.TYPE, isBalance);
        intent.putExtra(KEY_DATA, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
//        context.startActivity(new Intent(context, MyBillListActivity.class).putExtra(MyBillListActivity.TYPE, isBalance));
    }


    /**
     * 我的现金账单
     *
     * @param context
     */
    public static void toMyBalanceBillList(Context context) {
        Intent intent = new Intent(context, MyBillListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(MyBillListActivity.TYPE, true);
        intent.putExtra(KEY_DATA, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
//        context.startActivity(new Intent(context, MyBillListActivity.class).putExtra(MyBillListActivity.TYPE, isBalance));
    }


    /**
     * 我的积分账单
     *
     * @param context
     */
    public static void toMyScoreBillList(Context context) {
        Intent intent = new Intent(context, MyBillListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(MyBillListActivity.TYPE, false);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
//        context.startActivity(new Intent(context, MyBillListActivity.class).putExtra(MyBillListActivity.TYPE, isBalance));
    }


    /**
     * 积分详情
     *
     * @param context
     * @param billID  订单的id
     */
    public static void toScoreBillDetail(Context context, String billID) {
        Intent intent = new Intent(context, BillDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BillDetailActivity.TYPE, BillTypeHelper.TYPE_SCORE);
        bundle.putString(BillDetailActivity.ID, billID);
        intent.putExtra(KEY_DATA, bundle);
//        intent.putExtra(BillDetailActivity.TYPE, BillTypeHelper.TYPE_SCORE);
//        intent.putExtra(BillDetailActivity.ID, billID);
        context.startActivity(intent);
    }


    /**
     * 账单详情
     *
     * @param context
     */
    public static void toMoneyBillDetail(Context context, String billID) {
        Intent intent = new Intent(context, BillDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BillDetailActivity.TYPE, BillTypeHelper.TYPE_COMSUMPTION);
        bundle.putString(BillDetailActivity.ID, billID);
        intent.putExtra(KEY_DATA, bundle);
//        intent.putExtra(BillDetailActivity.TYPE, BillTypeHelper.TYPE_SCORE);
//        intent.putExtra(BillDetailActivity.ID, billID);
        context.startActivity(intent);

//        context.startActivity(new Intent(context, BillDetailActivity.class)
//                .putExtra(BillDetailActivity.ID, billID)
//                .putExtra(BillDetailActivity.TYPE, BillTypeHelper.TYPE_COMSUMPTION)
//        );
    }


    /**
     * 充值方式
     *
     * @param context
     * @param rechargeRequest
     */
    public static void toPaymentWayList(Context context, RechargeRequest rechargeRequest) {
        Intent intent = new Intent(context, PaymentWayListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PaymentWayListActivity.DATA_TAG, rechargeRequest);
        intent.putExtra(KEY_DATA, bundle);
//        intent.putExtra(PaymentWayListActivity.DATA_TAG, rechargeRequest);
        context.startActivity(intent);
    }


    /**
     * 充值方式
     *
     * @param context
     * @param rechargeRequest
     */
    public static void toRechargePayList(Context context, RechargeRequest rechargeRequest) {
        Intent intent = new Intent(context, RechargePayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(RechargePayActivity.DATA_TAG, rechargeRequest);
        intent.putExtra(KEY_DATA, bundle);
//        intent.putExtra(PaymentWayListActivity.DATA_TAG, rechargeRequest);
        context.startActivity(intent);
    }


    /**
     * 发票信息
     *
     * @param context
     */
    public static void toInVoiceInfo(Context context, InvoiceModel invoiceModel) {
        Intent intent = new Intent(context, InvoiceInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(INVOICEINFO, invoiceModel);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 充值成功
     *
     * @param context
     * @param orderID
     */
    public static void toReChargeSuccess(Context context, String orderID) {
        Intent intent = new Intent(context, RechargeSuccessActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(RechargeSuccessActivity.DATA_TAG, orderID);
        intent.putExtra(KEY_DATA, bundle);
//        intent.putExtra(RechargeSuccessActivity.DATA_TAG, rechargeRequest);
        context.startActivity(intent);
    }


    /**
     * 查看优惠券详情
     *
     * @param context
     * @param coupon
     */
    public static void toCouponDetail(Context context, Coupon coupon) {
        Intent intent = new Intent(context, CouponDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CouponDetailActivity.COUPON, coupon);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
//        context.startActivity(new Intent(context, CouponDetailActivity.class)
//                .putExtra(CouponDetailActivity.COUPON, coupon));
    }


    /**
     * 保养订单详情
     *
     * @param context
     * @param orderID
     */
    public static void toMaintainOrderDetail(Context context, String orderID) {
        Intent intent = new Intent(context, MaintainOrderDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MaintainOrderDetailsActivity.ORDERID, orderID);
        intent.putExtra(KEY_DATA, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 4s店保养订单详情
     *
     * @param context
     * @param orderID
     */
    public static void toMaintain4SShopOrderDetail(Context context, String orderID) {
        Intent intent = new Intent(context, Maintain4SShopOrderDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MaintainOrderDetailsActivity.ORDERID, orderID);
        intent.putExtra(KEY_DATA, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 订单详情（预约维修）
     *
     * @param context
     * @param orderID
     */
    public static void toRepairOrderDetail(Context context, String orderID) {
        Intent intent = new Intent(context, RepairOrderDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(RepairOrderDetailsActivity.ORDERID, orderID);
        intent.putExtra(KEY_DATA, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 保养发表评论
     *
     * @param context
     * @param orderID
     */
    public static void toOrderSendComment(Context context, String orderID, String shopPhoto, String shopID) {
        context.startActivity(new Intent(context, MaintainSendCommentActivity.class).putExtra(ORDER_ID, orderID).putExtra(SHOP_ID, shopID).putExtra(SHOP_PHOTO, shopPhoto));
    }


    /**
     * 实名认证
     */
    public static void toRealNameAuthentication(Context context) {
        Intent intent = new Intent(context, RealNameAuthenticationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 修改支付密码
     */
    public static void modifierPaidPWD(Context context) {
        Intent intent = new Intent(context, ModifierPaidPWDActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 忘记支付密码
     */
    public static void forgetPaidPWD(Context context) {
        Intent intent = new Intent(context, ForgetPWDActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 重设密码    忘记
     * captcha
     * realName
     * IDNumber
     */
    public static void resetForgetPaidPWD(String captcha, String realName, String IDNumber, Context context) {
        Intent intent = new Intent(context, ForgetPayPWDStep2Activity.class);
        intent.putExtra("captcha", captcha);
        intent.putExtra("realName", realName);
        intent.putExtra("IDNumber", IDNumber);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 重设密码    修改
     * captcha
     * oldPayPassword
     */
    public static void resetModifyPaidPWD(String captcha, String oldPayPassword, Context context) {
        Intent intent = new Intent(context, ModifierPayPWDStep2Activity.class);
        intent.putExtra("captcha", captcha);
        intent.putExtra("oldPayPassword", oldPayPassword);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 紧急救援
     **/
    public static void toEmergencyRescue(Context context) {
        Intent intent = new Intent(context, EmergencyRescueActivity.class);
        context.startActivity(intent);
    }


    /**
     * 到常规保养界面
     **/
    public static void toNormalMaintenance(Context context) {
        Intent intent = new Intent(context, FilterMaintenanceShopListActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putString(FilterMaintenanceShopListActivity2.ACTIVITY_TYPE, FilterMaintenanceShopListActivity2.NORMAL_MAINTENANCE + "");
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 到维修预约界面
     **/
    public static void toAppointmentMaintenance(Context context) {
        Intent intent = new Intent(context, FilterMaintenanceShopListActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putString(FilterMaintenanceShopListActivity2.ACTIVITY_TYPE, FilterMaintenanceShopListActivity2.APPOINTMENT_MAINTENANCE + "");
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 保养申请退款
     */
    public static void toOrderRefund(Context context, String orderID) {
        Intent intent = new Intent(context, MaintainOrderRefundActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ORDER_ID, orderID);
        context.startActivity(intent);
    }


    /**
     * 特价车申请退款
     */
    public static void toSeckillOrderRefund(Context context, String orderID) {
        Intent intent = new Intent(context, SeckillOrderRefundActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ORDER_ID, orderID);
        context.startActivity(intent);
    }

    /**
     * 支付宝退款提示页
     */
    public static void toAlipayRefundInfo(Context context) {
        Intent intent = new Intent(context, OrderRefundInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 保养取消订单
     */
    public static void toOrderCancel(Context context, String orderID) {
        Intent intent = new Intent(context, MaintainOrderCancelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ORDER_ID, orderID);
        context.startActivity(intent);
    }


    /**
     * 维修取消订单
     */
    public static void toRepairOrderCancel(Context context, String orderID) {
        Intent intent = new Intent(context, RepairOrderCancelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ORDER_ID, orderID);
        context.startActivity(intent);
    }


    /**
     * 用品取消订单
     */
    public static void toAccessoryOrderCancel(Context context, String orderID) {
        Intent intent = new Intent(context, AccessoryOrderCancelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ORDER_ID, orderID);
        context.startActivity(intent);
    }


    /**
     * 4S用品取消订单
     */
    public static void toAccessory4SOrderCancel(Context context, String orderID) {
        Intent intent = new Intent(context, Accessory4SShopOrderCancelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ORDER_ID, orderID);
        context.startActivity(intent);
    }


    /**
     * 4S用品申请退款
     */
    public static void toAccessory4SOrderRefund(Context context, String orderID) {
        Intent intent = new Intent(context, Accessory4SShopOrderRefundActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ORDER_ID, orderID);
        context.startActivity(intent);
    }


    /**
     * 特价车取消订单
     */
    public static void toSeckillOrderCancel(Context context, String orderID) {
        Intent intent = new Intent(context, SeckillOrderCancelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ORDER_ID, orderID);
        context.startActivity(intent);
    }


    /**
     * 特价车订单详情
     */
    public static void toSeckillOrderDetail(Context context, String orderID) {
        Intent intent = new Intent(context, SeckillOrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SeckillOrderDetailActivity.ORDERID, orderID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 洗车订单详情
     */
    public static void toCarWashOrderDetail(Context context, String orderID) {
        Intent intent = new Intent(context, CarWashOrderDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CarWashOrderDetailsActivity.ORDERID, orderID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 今日限行
     *
     * @param context
     */
    public static void toTrafficControl(Context context) {
        Intent intent = new Intent(context, TrafficControlActivity.class);
        context.startActivity(intent);
    }


    /**
     * 客户投诉
     *
     * @param context
     */
    public static void toComplaints(Context context) {
        Intent intent = new Intent(context, ComplaintsActivity2.class);
        context.startActivity(intent);
    }


    /**
     * 客户投诉
     *
     * @param context
     */
    public static void toComplaints2(Context context) {
        Intent intent = new Intent(context, ComplaintsActivity.class);
        context.startActivity(intent);
    }

//    /**
//     * 首页城市选择
//     */
//    public static void toPositionActivity(Context context, int requestCode, String position) {
//        Intent intent = new Intent(context, PositiosnActivity.class);
//        intent.putExtra("position", position);
//        ((Activity) context).startActivityForResult(intent, requestCode);
//    }
}
