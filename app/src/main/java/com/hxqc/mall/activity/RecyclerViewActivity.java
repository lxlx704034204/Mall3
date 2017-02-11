package com.hxqc.mall.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hxqc.mall.core.api.RequestFailViewUtil;

import hxqc.mall.R;


/**
 * 说明:
 *
 * author: 吕飞
 * since: 2015-03-30
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class RecyclerViewActivity extends AppBackActivity {
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public View mBlankView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //手势关闭
//        SlidrConfig config = new SlidrConfig.Builder()
//                .primaryColor(getResources().getColor(R.color.primary))
//                .sensitivity(1f).build();
//        Slidr.attach(this, config);
        initView();
    }

    private void initView() {
        setContentView(R.layout.layout_only_recycler_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    protected void showBlankPage(int resId) {
        mBlankView = new RequestFailViewUtil().getEmptyView(this, getResources().getString(resId));
        setContentView(mBlankView);
        mBlankView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initView();
                onResume();
            }
        });
    }
}
