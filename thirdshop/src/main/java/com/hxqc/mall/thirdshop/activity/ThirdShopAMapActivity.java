package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.amap.control.AmapManager;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.views.ThirdFunctionButton;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

public class ThirdShopAMapActivity extends BackActivity {

    PickupPointT pickupPoint;

    AmapManager amapManager;

    //第三方店铺
    private TextView mShop;//商城店铺
    private ThirdFunctionButton mShopMyLoc;
    private ThirdFunctionButton mShopCall;
    private ThirdFunctionButton mShopNavi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_shop_amap);
        int functionActiveState = getIntent().getIntExtra(ActivitySwitcherThirdPartShop.MAP_OPERATOR, 0);
        PickupPointT shopLocation = null;

        Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
        if ( bundleExtra!= null) {
            functionActiveState = 0;
            String address =  bundleExtra.getString("address");
            String tel = bundleExtra.getString("tel");
            String latitude =  bundleExtra.getString("latitude");
            String longitude = bundleExtra.getString("longitude");
            shopLocation = new PickupPointT(address, latitude, longitude, tel);
        } else {
            shopLocation = getIntent().getParcelableExtra(ActivitySwitcherThirdPartShop.SHOP_AMAP);
        }

        initView();
        MapView mapView = (MapView) findViewById(R.id.third_shop_map);
        TextView distance = (TextView) findViewById(R.id.third_shop_amap_distance);
        amapManager = AmapManager.getInstance();
        amapManager.onCreate(savedInstanceState, ThirdShopAMapActivity.this, mapView, distance);

        if (shopLocation != null) {
            pickupPoint = shopLocation;
            amapManager.setFunctionActiveState(functionActiveState);
            amapManager.initModelData(shopLocation);
            initData();
        }
    }

    private void initView() {

        mShop = (TextView) findViewById(R.id.tv_amap_shop);
        //第三方店铺底部
        mShopCall = (ThirdFunctionButton) findViewById(R.id.shop_amap_phone);
        mShopMyLoc = (ThirdFunctionButton) findViewById(R.id.shop_my_loc);
        mShopNavi = (ThirdFunctionButton) findViewById(R.id.shop_amap_navi);
    }

    private void initData() {
        if (!TextUtils.isEmpty(pickupPoint.address)) {
            mShop.setText(pickupPoint.address);
        } else {
            mShop.setText("获取失败");
        }

        mShopMyLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amapManager.reLoc();
            }
        });

        mShopNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amapManager.clickToNavi();
            }
        });

        mShopCall.setOnClickListener(new View.OnClickListener() {
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
