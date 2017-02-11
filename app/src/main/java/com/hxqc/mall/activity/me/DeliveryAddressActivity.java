package com.hxqc.mall.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.RecyclerViewActivity;
import com.hxqc.mall.core.adapter.DeliveryAddressAdapter;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.DeliveryAddress;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

import hxqc.mall.R;


/**
 * 说明:收货地址
 *
 * author: 吕飞
 * since: 2015-03-27
 * Copyright:恒信汽车电子商务有限公司
 */
@Deprecated
public class DeliveryAddressActivity extends RecyclerViewActivity {
    public static boolean sHasChanged = false;//地址是否有变化
    public static String sDefaultID;//当前默认地址
    public static String sExDefaultID;//初始默认地址
    ArrayList<DeliveryAddress> mDeliveryAddresses;//地址信息
    int mFlag;

    //    恢复页面时，如有更新就刷新一次
    @Override
    public void onResume() {
        super.onResume();
        if (sHasChanged) {
            initData();
            sHasChanged = false;
        }
    }

    public void initData() {
        mDeliveryAddresses = new ArrayList<>();
        mApiClient.getAddress(new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                mDeliveryAddresses = JSONUtils.fromJson(response, new TypeToken<ArrayList<DeliveryAddress>>() {
                });
                if (mDeliveryAddresses != null && mDeliveryAddresses.size() > 0) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    for (int i = 0; i < mDeliveryAddresses.size(); i++) {
//                        记录默认地址
                        if (mDeliveryAddresses.get(i).isDefault == 1) {
                            sExDefaultID = mDeliveryAddresses.get(i).addressID;
                            sDefaultID = mDeliveryAddresses.get(i).addressID;
                        }
                    }
                    showList();
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showBlankPage(R.string.app_net_error);
                sHasChanged = true;
            }
        });
    }

    private void showList() {
        if (mFlag != PayConstant.CHOOSE_ADDRESS) {
            mAdapter = new DeliveryAddressAdapter(mDeliveryAddresses, this, DeliveryAddressActivity.this, true);
        } else {
            mAdapter = new DeliveryAddressAdapter(mDeliveryAddresses, this, DeliveryAddressActivity.this, false);
        }
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFlag = getIntent().getIntExtra(PayConstant.CHOOSE_ADDRESS_FLAG, 0);
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_delivery_address, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_add && canAddAddress()) {
            ActivitySwitcher.toAddAddress(this);
        }
        return false;
    }

    /**
     * 是否可以添加新地址（地址最多8个）
     */
    private boolean canAddAddress() {
        if (mDeliveryAddresses != null && mDeliveryAddresses.size() > 7) {
            ToastHelper.showYellowToast(this, R.string.me_max_addresses_hint);
            return false;
        } else {
            return true;
        }
    }

    //        默认地址改变就请求服务器
    public void changeDefaultAddress() {
        if (mFlag != PayConstant.CHOOSE_ADDRESS && mDeliveryAddresses != null && mDeliveryAddresses.size() > 0 && !sDefaultID.equals(sExDefaultID)) {
            mApiClient.editAddress( getDefaultAddress(), new BaseMallJsonHttpResponseHandler(this) {
                @Override
                public void onSuccess(String response) {
                }
            });
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    private DeliveryAddress getDefaultAddress() {
        DeliveryAddress mDefaultDeliveryAddress = null;
        for (int i = 0; i < mDeliveryAddresses.size(); i++) {
            if (mDeliveryAddresses.get(i).isDefault == 1) {
                mDefaultDeliveryAddress = mDeliveryAddresses.get(i);
            }
        }
        return mDefaultDeliveryAddress;
    }

    //    把地址传给订单
    private void addressToOrder() {
        Intent intent = new Intent();
        intent.putExtra("ADDRESS", getDefaultAddress());
        setResult(118, intent);
    }

    @Override
    public void finish() {
        if (mFlag == PayConstant.CHOOSE_ADDRESS) {
            addressToOrder();
            mFlag = 0;
        }
        super.finish();
    }
}
