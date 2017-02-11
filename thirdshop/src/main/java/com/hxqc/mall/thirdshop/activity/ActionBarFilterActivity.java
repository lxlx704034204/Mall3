package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hxqc.mall.activity.SingleFragmentActivity;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;

/**
 * Author:李烽
 * Date:2016-05-05
 * FIXME
 * Todo 带删选的按钮的activity
 */
public abstract class ActionBarFilterActivity extends SingleFragmentActivity {

    protected String areaID;

    protected ThirdPartShopClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        areaID = getAreaID();
        client = new ThirdPartShopClient();
    }

    protected abstract String getAreaID();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_filter_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_brand_filter) {
            toFilter();
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract void toFilter();
}
