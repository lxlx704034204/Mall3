package com.hxqc.mall.thirdshop.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.views.ErrorView;
import com.hxqc.mall.core.views.sticklistviewbyzf.StickyListHeadersListView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.InfoApiClient;
import com.hxqc.mall.thirdshop.model.ModelByYear;
import com.hxqc.mall.thirdshop.model.newcar.ModelInfo;
import com.hxqc.mall.thirdshop.views.adpter.FilterCarModelAdapter;
import com.hxqc.util.ScreenUtil;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaofan.
 * 车型筛选列表
 */
public class FilterCarModelActivity extends NoBackActivity {
    private StickyListHeadersListView lv;
    FilterCarModelAdapter adapter;
    private List<ModelInfo> mList;//车型列表

    private InfoApiClient apiClient;
    private String brandName, seriesName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_carmodel);

        bindView();
        initResource();
        getData();

    }

    private void bindView() {
        lv = (StickyListHeadersListView) findViewById(R.id.listview);
    }

    private void initResource() {
        apiClient = new InfoApiClient();
        brandName = getIntent().getStringExtra("brandName");
        seriesName = getIntent().getStringExtra("seriesName");
        setHeaderTitle("选择车型");
        adapter = new FilterCarModelAdapter(this);
        lv.setAdapter(adapter);
    }


    private void getData() {
        apiClient.requestFilterModel(brandName, seriesName, new LoadingAnimResponseHandler(this, true, false) {
            @Override
            public void onSuccess(String response) {
                //    forTest1(response);
                List<ModelByYear> data = new Gson().fromJson(response, new TypeToken<List<ModelByYear>>() {
                }.getType());

                if (data == null || data.isEmpty()) {
                    empty();
                    return;
                }
                mList = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    for (int j = 0; j < data.get(i).getModel().size(); j++) {
                        List<ModelInfo> list = data.get(i).getModel();
                        mList.add(new ModelInfo(brandName, list.get(j).getModelName(), list.get(j).getExtID(), data.get(i).getYear()));
                    }
                }

                adapter.setData(mList);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                empty();
            }
        });

    }


    public void setHeaderTitle(String title) {
        RelativeLayout heardView = new RelativeLayout(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(this);
        int padding = ScreenUtil.dip2px(this, 16);
        tv.setPadding(padding, padding / 2, padding, padding / 2);
        tv.setTextColor(Color.parseColor("#333333"));
        tv.setText(title);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextSize(16);
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        heardView.setBackgroundColor(Color.parseColor("#ffffff"));
        heardView.addView(tv, layoutParams);
        lv.addHeaderView(heardView);
    }


    private void empty() {
        ErrorView.builder(this).showCustom("没有符合条件的车辆", "", R.drawable.ic_no_auto_data, false, null);
    }


}
