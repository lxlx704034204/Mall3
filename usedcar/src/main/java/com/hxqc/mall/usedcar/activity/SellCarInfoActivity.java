package com.hxqc.mall.usedcar.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.fragment.NewSellCarInfoFragment;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;

/**
 * Author : 钟学东
 * Date : 2015-08-04
 * FIXME
 * Todo
 */
public class SellCarInfoActivity extends BackActivity {

    public FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellcarinfo);
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fl, new NewSellCarInfoFragment()).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_sellinfo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_release) {
            UsedCarActivitySwitcher.toSellCarFromHome(this);
        }
        return false;
    }
}
