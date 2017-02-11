package com.hxqc.mall.thirdshop.accessory.activity;

import android.os.Bundle;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;

/**
 * 说明:
 *
 * author: 吕飞
 * since: 2015-05-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class AccessoryBackActivity extends BackActivity {
    protected AccessoryApiClient mAccessoryApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccessoryApiClient=new AccessoryApiClient();
    }
}
