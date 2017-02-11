package com.hxqc.mall.usedcar.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.ValuationArgument;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;

/**
 * @Author : 钟学东
 * @Since : 2015-08-08
 * FIXME
 * Todo
 */
public class NewSellCarActivity extends NoBackActivity implements View.OnClickListener {

    Toolbar mToolbar;
    ValuationArgument mValuationArgument;
    Button mPlatformView;
    Button mPersonalView;
    TextView mPlatView;
    TextView mPersonView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sell_car);
        try {
            mValuationArgument = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelable(UsedCarActivitySwitcher.VALUATION_ARGUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("我要卖车");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
        mPlatformView = (Button) findViewById(R.id.bt_platform);
        mPersonalView = (Button) findViewById(R.id.bt_personal);
        mPlatView = (TextView) findViewById(R.id.plat_button);
        mPersonView = (TextView) findViewById(R.id.person_button);
        mPlatformView.setOnClickListener(this);
        mPersonalView.setOnClickListener(this);
        mPlatView.setOnClickListener(this);
        mPersonView.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_platform || i == R.id.plat_button) {
            UsedCarActivitySwitcher.toPlatformSell(NewSellCarActivity.this);
        } else if (i == R.id.bt_personal || i == R.id.person_button) {
            if (mValuationArgument == null) {
                UsedCarActivitySwitcher.toSellCar(NewSellCarActivity.this);
            } else {
                UsedCarActivitySwitcher.toSellCar(NewSellCarActivity.this, mValuationArgument);
            }
        }
    }
}
