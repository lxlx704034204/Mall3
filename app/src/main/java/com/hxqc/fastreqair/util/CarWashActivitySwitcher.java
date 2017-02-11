package com.hxqc.fastreqair.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hxqc.fastreqair.activity.CarWashSendCommentActivity;
import com.hxqc.fastreqair.activity.CarWashShopListActivity;
import com.hxqc.fastreqair.activity.CarWashShopListOnMapActivity;
import com.hxqc.fastreqair.activity.WashCarActivity;
import com.hxqc.fastreqair.activity.WashCarCommentListActivity;
import com.hxqc.fastreqair.activity.WashCarPayFinishActivity;
import com.hxqc.fastreqair.activity.WashCarShopActivity;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;

import java.util.HashMap;

/**
 * Created by CPR113 on 2016/5/23.
 */
public class CarWashActivitySwitcher  extends ActivitySwitchBase {
    public static final String ORDER_ID = "order_id";
    public static final String SHOP_PHOTO = "shop_photo";
    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_NAME = "shop_name";
    public static final String PAYMENT_AMOUNT = "payment_amount";//支付金额

    /**
     * 洗车评论
     */
    public static void CarWashSendComment(Context context,String orderID,String shopPhoto,String shopID,String shopName,String paymentAmount){
        Intent intent = new Intent(context, CarWashSendCommentActivity.class);
        intent.putExtra(ORDER_ID, orderID);
        intent.putExtra(SHOP_PHOTO, shopPhoto);
        intent.putExtra(SHOP_ID, shopID);
        intent.putExtra(SHOP_NAME, shopName);
        intent.putExtra(PAYMENT_AMOUNT, paymentAmount);
        context.startActivity(intent);
    }


    /**
     * 洗车店
     * @param context
     */
    public static void toWashCarShop(Context context,String shopID){
        Intent intent = new Intent(context, WashCarShopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("shopID",shopID);
        intent.putExtra(KEY_DATA,bundle);
        context.startActivity(intent);
    }

    /**
     * 洗车店评价列表页
     * @param context
     * @param shopID
     * carWashComments ,ArrayList<CarWashComment> carWashComments
     */
    public static void toWashCarCommentList(Context context,String shopID){
        Intent intent = new Intent(context, WashCarCommentListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("shopID",shopID);
        intent.putExtra(KEY_DATA,bundle);
        context.startActivity(intent);
    }

    /**
     * 洗车页面
     * @param context
     */
    public static void toWashCar(final Context context){
        UserInfoHelper.getInstance().loginAction(context, new UserInfoHelper.OnLoginListener() {
            @Override
            public void onLoginSuccess() {
                Intent intent = new Intent(context , WashCarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }


    /** 洗车店列表 **/
    public static void toCarWashShopList(Context context) {
        Intent intent = new Intent(context, CarWashShopListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /** 地图上选店 **/
    public static void toChooseShopOnMap(Context context, HashMap<String, String> hashMap) {
        Intent intent = new Intent(context, CarWashShopListOnMapActivity.class);
        intent.putExtra(CarWashShopListOnMapActivity.DATA, hashMap);
        context.startActivity(intent);
    }


    /**
     * 洗车付款完成页面
     * @param context
     * @param orderID
     */
    public static void toWashCarPayFinish(Context context ,String orderID){
        Intent intent = new Intent(context, WashCarPayFinishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("orderID",orderID);
        intent.putExtra(KEY_DATA,bundle);
        context.startActivity(intent);
    }
}
