package com.hxqc.mall.usedcar.activity;

import android.os.Bundle;
import android.view.View;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;

/**
 * 说明:置换入口
 *
 * @author: 吕飞
 * @since: 2016-09-29
 * Copyright:恒信汽车电子商务有限公司
 */

public class ExchangeEntranceActivity extends BackActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_entrance);
    }

    public void toExchange(View v) {
        UsedCarActivitySwitcher.toExchange(this);
    }
}
