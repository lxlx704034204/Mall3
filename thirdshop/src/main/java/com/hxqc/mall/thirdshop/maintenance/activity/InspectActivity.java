package com.hxqc.mall.thirdshop.maintenance.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.InspectListAdapter;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 08
 * FIXME
 * Todo 检查故障
 */
@Deprecated
public class InspectActivity extends BackActivity{

    private RecyclerView mInspectListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inspect);

        initView();

        initEvent();
    }

    private void initEvent() {
        InspectListAdapter inspectListAdapter = new InspectListAdapter(InspectActivity.this);
        mInspectListView.setHasFixedSize(true);
        mInspectListView.setLayoutManager(new LinearLayoutManager(InspectActivity.this));
        mInspectListView.setAdapter(inspectListAdapter);
    }

    private void initView() {

        mInspectListView = (RecyclerView) findViewById(R.id.inspect_list);
    }
}
