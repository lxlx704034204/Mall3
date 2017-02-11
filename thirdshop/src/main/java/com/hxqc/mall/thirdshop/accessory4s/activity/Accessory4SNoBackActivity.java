package com.hxqc.mall.thirdshop.accessory4s.activity;

import android.os.Bundle;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.thirdshop.accessory4s.api.Accessory4SApiClient;

/**
 * 说明:
 *
 * author: 吕飞
 * since: 2015-05-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class Accessory4SNoBackActivity extends NoBackActivity {
    protected Accessory4SApiClient mAccessoryApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccessoryApiClient=new Accessory4SApiClient();
    }
}
