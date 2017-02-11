package com.hxqc.mall.thirdshop.maintenance.activity;

import com.hxqc.mall.payment.activity.PayFinishActivity;
import com.hxqc.mall.thirdshop.maintenance.utils.SharedPreferencesHelper;

/**
 * @Author : 钟学东
 * @Since : 2016-03-17
 * FIXME
 * Todo 支付完成页面
 */
public class FinishPayMaintenanceActivity extends PayFinishActivity{

//    String shopID;
//    String flag;
//    String orderID;
    @Override
    protected void onStart() {
        super.onStart();
//        Intent intent = getIntent();
//        shopID = intent.getStringExtra("shopID");
//        flag = intent.getStringExtra("flag");
//        orderID = intent.getStringExtra("orderID");
        getSupportActionBar().setTitle("支付成功");
    }

    @Override
    protected void finishPay() {
//        if(flag.equals("1")){ //1 正常流程
//            ActivitySwitcherThirdPartShop.toMaintenanHomeWithFlag(shopID,this);
//        }else if(flag.equals("2")){ // 2 首页订单
//            ActivitySwitchBase.toMain(FinishPayMaintenanceActivity.this, 2);
//        }else if(flag.equals("3")){ // 订单详情
//            ActivitySwitchBase.toMaintainOrderDetail(this,orderID);
//        }
        new SharedPreferencesHelper(FinishPayMaintenanceActivity.this).setOrderChange(true);
    }
}
