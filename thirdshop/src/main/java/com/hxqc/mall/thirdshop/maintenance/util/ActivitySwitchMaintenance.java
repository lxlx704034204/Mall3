package com.hxqc.mall.thirdshop.maintenance.util;

import android.content.Context;
import android.content.Intent;

import com.hxqc.mall.thirdshop.maintenance.activity.MaintenancePromotionActivity;

/**
 * Author: wanghao
 * Date: 2016-03-31
 * FIXME
 * Todo
 */
public class ActivitySwitchMaintenance {


    public static final String PROMOTION_ID = "promotionID";

    /**
     * 保养促销
     */
    public static void toMaintenancePromotion(String promotionID, Context context) {
        Intent intent = new Intent(context, MaintenancePromotionActivity.class);
        intent.putExtra(PROMOTION_ID, promotionID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
