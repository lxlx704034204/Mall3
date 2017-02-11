package com.hxqc.mall.thirdshop.maintenance.inter;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.control.T_NewImageViewControl;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DebugLog;

/**
 * Author: wanghao
 * Date: 2016-03-23
 * FIXME   保养促销 html    js  回调
 * Todo
 */
public class TPSMaintenancePJSInterface {

    public final static String PACKAGE_ITEM = "PACKAGE_ITEM";
    public final static String IMAGE_ITEM = "IMAGE_ITEM";

    private Context context;
    private T_NewImageViewControl control;
    private String shopID = "";

    public TPSMaintenancePJSInterface(String shopID,Context context, T_NewImageViewControl control) {
        this.context = context;
        this.control = control;
        this.shopID = shopID;
    }

    @JavascriptInterface
    public void showResponseView(String success,String type) {
        DebugLog.i("test_html_js", success + "");
        if (type.equals(PACKAGE_ITEM)){
            String packageID = success.split("\\|")[0];
            String title = success.split("\\|")[1];

            DebugLog.i("test_html_js", success + " : package id   PACKAGE_ITEM  shop id : "+shopID);
            DebugLog.i("test_html_js","packageID : "+packageID+" title: "+ title + " shop id : "+shopID);

            ToastHelper.showRedToast(context,"套餐不可用");
//            ActivitySwitcherMaintenance.toSmartMaintenance(context,title,shopID,packageID);

        }else if (type.equals(IMAGE_ITEM)){
            if (control!=null){
                control.setPosition(Integer.parseInt(success));
                ActivitySwitcherThirdPartShop.toViewNewsImage(context);
            }
        }
    }

}
