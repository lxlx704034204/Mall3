//package com.hxqc.mall.activity.order;
//
//import android.os.Bundle;
//
//import com.hxqc.mall.activity.AppBackActivity;
//import com.hxqc.mall.fragment.me.UserOrderForRecyclerFragment;
//
//import hxqc.mall.R;
//
///**
// * 说明:我的订单
// *
// * author: 吕飞
// * since: 2015-04-08
// * Copyright:恒信汽车电子商务有限公司
// */
//@Deprecated
//public class UserOrderActivity extends AppBackActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_only_has_fragment);
////        //手势关闭
////        SlidrConfig config = new SlidrConfig.Builder()
////                .primaryColor(getResources().getColor(R.color.primary))
////                .sensitivity(1f).build();
////        Slidr.attach(this, config);
//        String orderType = getIntent().getStringExtra(UserOrderForRecyclerFragment.OrderTypeFlag);
//        UserOrderForRecyclerFragment mUserOrderFragment = UserOrderForRecyclerFragment.newInstance(orderType);
//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mUserOrderFragment).commit();
//    }
//
//}
