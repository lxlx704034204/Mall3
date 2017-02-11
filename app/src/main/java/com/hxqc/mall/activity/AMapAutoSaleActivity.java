package com.hxqc.mall.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.amap.control.AmapManager;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.model.order.OrderModel;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.JSONUtils;

import hxqc.mall.R;

public class AMapAutoSaleActivity extends BackActivity {


    OrderModel orderDetail;

    PickupPointT pickupPoint;

    AmapManager amapManager;

    private TextView mNavi;//商城导航
    private TextView mCall;//商城打电话
    private TextView mShop;//商城店铺
    private TextView mMyLoc;//重定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap_auto_sale);
        orderDetail = JSONUtils.fromJson(getIntent().getStringExtra(ActivitySwitcher.ORDER_DETAIL), new TypeToken< OrderModel >() {
        });
//        int[] rPositions = new int[]{R.id.simple_route_map, R.id.tv_amap_distance};
        initView();
        MapView mapView = (MapView) findViewById(R.id.auto_sale_map);
        TextView distance = (TextView) findViewById(R.id.auto_sale_amap_distance);

        amapManager = AmapManager.getInstance();
        amapManager.onCreate(savedInstanceState,AMapAutoSaleActivity.this,mapView,distance);

        if (orderDetail != null) {
            if (orderDetail.PickupPoint != null) {
                pickupPoint = orderDetail.PickupPoint;
                amapManager.initModelData(orderDetail.PickupPoint);
                initData();
            }
        }
    }

    private void initView() {
        mNavi = (TextView) findViewById(R.id.tv_amap_navi);
        mCall = (TextView) findViewById(R.id.tv_amap_phone);
        mShop = (TextView) findViewById(R.id.tv_amap_shop);
        mMyLoc = (TextView) findViewById(R.id.tv_my_loc);
    }

    private void initData() {
        if (!TextUtils.isEmpty(pickupPoint.address)) {
            mShop.setText(pickupPoint.address);
        } else {
            mShop.setText("获取失败");
        }

        mMyLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amapManager.reLoc();
            }
        });

        mNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amapManager.clickToNavi();
            }
        });

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amapManager.shopCall();
            }
        });

    }

    //------------------生命周期重写函数---------------------------
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        amapManager.onSaveInstanceState(outState);
    }


    @Override
    public void onResume() {
        super.onResume();
        amapManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        amapManager.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        amapManager.onDestroy();
    }

    @Override
    public void finish() {
        amapManager.onDestroy();
        super.finish();
    }
//-------------------------------------------------------------------------------------------------

}
