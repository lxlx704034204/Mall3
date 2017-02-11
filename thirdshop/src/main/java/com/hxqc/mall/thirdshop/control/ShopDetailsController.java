package com.hxqc.mall.thirdshop.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.util.JSONUtils;

import org.apache.http.conn.HttpHostConnectException;

import cz.msebera.android.httpclient.Header;

/**
 * liaoguilong
 * Created by CPR113 on 2016/4/5.
 * 店铺详情controller
 */
public class ShopDetailsController {

    public final static String SHOPID_KEY = "shopID";

    public final static String SERIESID_KEY = "SeriesID";
    public final static String SHOP_TYPE = "shopType";
    public final static  String FROM_KEY="from";
    private static ShopDetailsController ourInstance;
    public ThirdPartShopClient ThirdPartShopClient;
    public int page=0;
    public  final static int FROM_4S=0; //4S店
    public final static int FROM_RESCUE=1; //救援


    private int from;
    private String shopID;
    private ThirdPartShop thirdPartShop;

    public ShopDetailsController() {
        ThirdPartShopClient = new ThirdPartShopClient();
    }

    public static ShopDetailsController getInstance() {
        if (ourInstance == null) {
            synchronized (TFilterController.class) {
                if (ourInstance == null) {
                    ourInstance = new ShopDetailsController();
                }
            }
        }
        return ourInstance;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public ThirdPartShop getThirdPartShop() {
        return thirdPartShop;
    }

    /**
     * 获取店铺信息
     * @param context
     * @param thirdPartShopHandler
     */
    public void requestThirdPartShop(Context context,@NonNull final ThirdPartShopHandler thirdPartShopHandler){
        if (thirdPartShop != null && thirdPartShopHandler != null) {
            thirdPartShopHandler.onSucceed(thirdPartShop);
            return;
        }
        new ThirdPartShopClient().shopInfo(shopID, new LoadingAnimResponseHandler(context, true,false) {

            @Override
            public void onSuccess(String response) {
                thirdPartShop = JSONUtils.fromJson(response, new TypeToken<ThirdPartShop>() {});
                thirdPartShopHandler.onSucceed(thirdPartShop);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    thirdPartShopHandler.onFailed(true);
                } else {
                    thirdPartShopHandler.onFailed(false);
                }
            }
        });
    }

    public void requestThirdPartShop(Context context, @NonNull final CallBackControl.CallBack<ThirdPartShop> callBack){
        if (thirdPartShop != null && callBack != null) {
            callBack.onSuccess(thirdPartShop);
            return;
        }
        new ThirdPartShopClient().shopInfo(shopID, new LoadingAnimResponseHandler(context, true,false) {

            @Override
            public void onSuccess(String response) {
                thirdPartShop = JSONUtils.fromJson(response, new TypeToken<ThirdPartShop>() {});
                callBack.onSuccess(thirdPartShop);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    callBack.onFailed(true);
                } else {
                    callBack.onFailed(false);
                }
            }
        });
    }

    /**
     * 回收Contoller
     */
    public void onDestroy(){
        if(page==0) {
            ourInstance = null;
        }
    }

    public interface ThirdPartShopHandler {
        void onSucceed(ThirdPartShop thirdPartShop);

        void onFailed(boolean offLine);
    }

}
