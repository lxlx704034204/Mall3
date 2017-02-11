package com.hxqc.mall.thirdshop.maintenance.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.SprayRepairContentAdapter;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 08
 * FIXME
 * Todo 钣喷
 */
@Deprecated
public class SprayRepairActivity extends BackActivity {

    private static final String TAG = "SprayRepairActivity";
    private RecyclerView mSprayContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spray_repair);

        initView();

        initEvent();
    }

    private void initEvent() {

        SprayRepairContentAdapter sprayRepairContentAdapter = new SprayRepairContentAdapter(SprayRepairActivity.this);
        mSprayContentView.setHasFixedSize(true);
        mSprayContentView.setLayoutManager(new GridLayoutManager(SprayRepairActivity.this, 3));
        mSprayContentView.setAdapter(sprayRepairContentAdapter);
    }

    private void initView() {
        mSprayContentView = (RecyclerView) findViewById(R.id.spray_repair_text_nine_content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_service_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.action_confirmation) {
            finish();
        }
        return false;
    }

}
