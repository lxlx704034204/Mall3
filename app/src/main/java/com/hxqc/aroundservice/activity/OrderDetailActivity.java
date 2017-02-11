package com.hxqc.aroundservice.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.fragment.IllegalOrderDetailFragment;
import com.hxqc.aroundservice.fragment.LicenseOrderDetailFragment;
import com.hxqc.aroundservice.fragment.VehiclesOrderDetailFragment;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.core.util.ActivitySwitchBase;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 25
 * Des: 订单详情
 * FIXME
 * Todo
 */
public class OrderDetailActivity extends BackActivity {

    private static final String TAG = AutoInfoContants.LOG_J;
    private IllegalOrderDetailFragment illegalOrderDetailFragment;//违章订单详情页面
    private LicenseOrderDetailFragment licenseOrderDetailFragment;//驾驶证换证详情页面
    private VehiclesOrderDetailFragment vehiclesOrderDetailFragment;//年检详情页面
    private String flagFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common_order_detail);

        initView();

        initData();

        initEvent();

    }

    private void initEvent() {

    }

    private void initData() {

    }

    private void initView() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (getIntent() != null) {
//            flagFragment = getIntent().getIntExtra("flagFragment", -1);
            Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            flagFragment = bundleExtra.getString("flagFragment","empty");

	        if (flagFragment.equals(OrderDetailContants.FRAGMENT_ILLEGAL_ORDER_DETAIL)) {
                if (illegalOrderDetailFragment == null) {
                    illegalOrderDetailFragment = new IllegalOrderDetailFragment();
                    fragmentTransaction.replace(R.id.order_detail_home, illegalOrderDetailFragment);
                }
            } else if (flagFragment.equals(OrderDetailContants.FRAGMENT_LICENSE_ORDER_DETAIL)) {
                if (licenseOrderDetailFragment == null) {
                    licenseOrderDetailFragment = new LicenseOrderDetailFragment();
                    fragmentTransaction.replace(R.id.order_detail_home, licenseOrderDetailFragment);
                }
            } else if (flagFragment.equals(OrderDetailContants.FRAGMENT_VEHICLES_ORDER_DETAIL)) {
                if (vehiclesOrderDetailFragment == null) {
                    vehiclesOrderDetailFragment = new VehiclesOrderDetailFragment();
                    fragmentTransaction.replace(R.id.order_detail_home, vehiclesOrderDetailFragment);
                }
            }
            fragmentTransaction.commit();
        }
    }

}
