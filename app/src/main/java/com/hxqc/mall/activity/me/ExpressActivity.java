package com.hxqc.mall.activity.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.AppBackActivity;
import com.hxqc.mall.core.adapter.ExpressAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.ExpressDetail;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import hxqc.mall.R;

/**
 * 说明:物流追踪
 *
 * author: 吕飞
 * since: 2015-06-02
 * Copyright:恒信汽车电子商务有限公司
 */
@Deprecated
public class ExpressActivity extends AppBackActivity {
    RecyclerView mRecyclerView;
    String mOrderId;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ExpressDetail > mExpressDetails;
    TextView mNoDataView;
    TextView mOrderIdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderId = getIntent().getStringExtra(ActivitySwitcher.ORDER_ID);
        setContentView(R.layout.activity_express);
        //手势关闭
//        SlidrConfig config = new SlidrConfig.Builder()
//                .primaryColor(getResources().getColor(R.color.primary))
//                .sensitivity(1f).build();
//        Slidr.attach(this, config);
        mNoDataView = (TextView) findViewById(R.id.no_data);
        mOrderIdView = (TextView) findViewById(R.id.order_id);
        mOrderIdView.setText(getResources().getString(R.string.me_order_id) + mOrderId);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

    }

    protected void showBlankPage() {
        mNoDataView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mNoDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoDataView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                initData();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mApiClient.expressDetail(mOrderId, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                mExpressDetails = JSONUtils.fromJson(response, new TypeToken<ArrayList<ExpressDetail>>() {
                });
                if (mExpressDetails != null && mExpressDetails.size() > 0) {
                    showList();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showBlankPage();
            }
        });
    }

    private void showList() {
        mAdapter = new ExpressAdapter(mExpressDetails, this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
