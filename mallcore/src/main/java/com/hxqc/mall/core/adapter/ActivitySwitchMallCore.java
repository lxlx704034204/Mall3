package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hxqc.mall.activity.coupon.CouponDetailActivity;
import com.hxqc.mall.core.model.Coupon;
import com.hxqc.mall.core.util.ActivitySwitchBase;

/**
 * Author:李烽
 * Date:2016-03-15
 * FIXME
 * Todo
 */
public class ActivitySwitchMallCore {

    /**
     * 查看优惠券详情
     *
     * @param context
     * @param coupon
     */
    public static void toCouponDetail(Context context, Coupon coupon) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CouponDetailActivity.COUPON, coupon);
        context.startActivity(new Intent(context, CouponDetailActivity.class)
                .putExtra(ActivitySwitchBase.KEY_DATA, bundle));
    }
}
