package com.hxqc.mall.activity.coupon;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.model.Coupon;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.util.DebugLog;

/**
 * Author:李烽
 * Date:2016-03-10
 * FIXME
 * Todo 优惠券详情
 */
public class CouponDetailActivity extends BackActivity {
    public static final String COUPON
            = "com.hxqc.mall.activity.coupon.CouponDetailActivity_coupon";
    private TextView coupon_description, coupon_rule, coupon_number, end_date, use_shop, user_tip, mark;
    private LinearLayout coupon_description_layout, coupon_rule_layout, coupon_number_layout, end_date_layout, use_shop_layout, mark_layout;
    private Coupon coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail_activity);
        coupon = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelable(COUPON);
        coupon_description = (TextView) findViewById(R.id.coupon_description);
        end_date = (TextView) findViewById(R.id.end_date);
        use_shop = (TextView) findViewById(R.id.use_shop);
        user_tip = (TextView) findViewById(R.id.user_tip);
        coupon_rule = (TextView) findViewById(R.id.coupon_rule);
        coupon_number = (TextView) findViewById(R.id.coupon_number);
        mark = (TextView) findViewById(R.id.coupon_detail_mark);
        coupon_description_layout = (LinearLayout) findViewById(R.id.coupon_description_layout);
        end_date_layout = (LinearLayout) findViewById(R.id.end_date_layout);
        use_shop_layout = (LinearLayout) findViewById(R.id.use_shop_layout);
        coupon_rule_layout = (LinearLayout) findViewById(R.id.coupon_rule_layout);
        coupon_number_layout = (LinearLayout) findViewById(R.id.coupon_number_layout);
        mark_layout = (LinearLayout) findViewById(R.id.coupon_detail_mark_layout);
        if (coupon != null)
            bindData();
    }

    private void bindData() {
        //优惠面额
        if (!TextUtils.isEmpty(coupon.price)) {
            coupon_description.setText(OtherUtil.amountFormat(coupon.price, true));
            coupon_description_layout.setVisibility(View.VISIBLE);
        }
//        适用范围
        if (!TextUtils.isEmpty(coupon.kind)) {
//            coupon_rule.setText(String.format("仅限%s使用", coupon.kind));
            coupon_rule.setText(coupon.kind);
            coupon_rule_layout.setVisibility(View.VISIBLE);
        }
//        券编号
        if (!TextUtils.isEmpty(coupon.couponID)) {
            coupon_number.setText(coupon.couponID);
            coupon_number_layout.setVisibility(View.VISIBLE);
        }
//        有效日期
        if (!TextUtils.isEmpty(coupon.endDate)) {
            end_date.setText(String.format("%s-%s", coupon.startDate, coupon.endDate));
            end_date_layout.setVisibility(View.VISIBLE);
        }
//        使用门店
        if (!TextUtils.isEmpty(coupon.shopName)) {
//            use_shop.setText(coupon.shopName);
            use_shop.setText(getShopName(coupon.shopName));
            use_shop_layout.setVisibility(View.VISIBLE);
        }
//        详细信息
        if (!TextUtils.isEmpty(coupon.description)) {
            mark.setText(coupon.description);
            mark_layout.setVisibility(View.VISIBLE);
        }
    }

    private String getShopName(String shopName) {
        shopName = shopName.replace(",", "\n");
        DebugLog.d(getClass().getSimpleName(), shopName);
        return shopName;
    }

}
