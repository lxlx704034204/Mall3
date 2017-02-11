package com.hxqc.aroundservice.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.hxqc.aroundservice.control.TrafficControlHelper;
import com.hxqc.aroundservice.model.CityList;
import com.hxqc.aroundservice.model.TrafficCity;
import com.hxqc.aroundservice.model.TrafficControlRule;
import com.hxqc.aroundservice.model.TrafficControlRuleDate;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.RequestFailView;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * @Author : 钟学东
 * @Since : 2016-04-07
 * FIXME
 * Todo 今日限行
 */
public class TrafficControlActivity extends NoBackActivity implements TrafficControlHelper.LocationHandle {

    private Toolbar toolbar;
    private TextView mChangeCityView;
    private RelativeLayout mLocatingView;
    private RelativeLayout mErrorView;
    private TextView mTvErrorView;
    private ScrollView mParentView;
    private LinearLayout mLlRemarkView;
    private LinearLayout mLlForbidView;
    private LinearLayout mLlPenaltyView;
    private RequestFailView mFailView;

    private TrafficCity trafficCity;

    private String LocationCity = "";

    TrafficControlHelper trafficControlHelper;

    AMapLocation currentAMapLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_control);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        trafficControlHelper = TrafficControlHelper.getInstance();

        initView();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        trafficControlHelper.onDestory();
    }

    private void getCity() {

        trafficCity = trafficControlHelper.getTrafficCity();
        if (trafficCity == null) {
            trafficControlHelper.getCity(this,new TrafficControlHelper.getCityHandle() {
                @Override
                public void onSuccess(final TrafficCity trafficCity) {
                    TrafficControlActivity.this.trafficCity = trafficCity;
                    final ArrayList<CityList> result = trafficCity.result;
                    nextSearchCityOperate();
                    mChangeCityView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivitySwitchAround.toPositionActivity(TrafficControlActivity.this, result, 1, LocationCity);
                        }
                    });
                }

                @Override
                public void onFailure() {

                }
            });
        } else {
            final ArrayList<CityList> result = trafficCity.result;
            mChangeCityView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitySwitchAround.toPositionActivity(TrafficControlActivity.this, result, 1, LocationCity);
                }
            });
        }

    }


    private void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        trafficControlHelper.setLocationHandle(this);
        trafficControlHelper.startLocation(this);
    }


    private void initView() {
        mChangeCityView = (TextView) findViewById(R.id.change_city);
        mLocatingView = (RelativeLayout) findViewById(R.id.rl_locating);
        mErrorView = (RelativeLayout) findViewById(R.id.rl_error);
        mParentView = (ScrollView) findViewById(R.id.ll_parent);
        mLlRemarkView = (LinearLayout) findViewById(R.id.ll_remarks);
        mLlForbidView = (LinearLayout) findViewById(R.id.ll_forbid);
        mLlPenaltyView = (LinearLayout) findViewById(R.id.ll_penalty);
        mFailView = (RequestFailView) findViewById(R.id.fail_view);
        mTvErrorView = (TextView) findViewById(R.id.tv_error);
    }


    @Override
    public void onLocationChange(AMapLocation aMapLocation) {

        if (TextUtils.isEmpty(aMapLocation.getCity())) {
            showLocateFail();
        } else {
            currentAMapLocation = aMapLocation;
            getCity();
        }
    }

    private void nextSearchCityOperate() {
        mChangeCityView.setText(currentAMapLocation.getCity());
        String cityName = currentAMapLocation.getCity();
        LocationCity = currentAMapLocation.getCity();
        if (cityName.contains("市")) {
            cityName = cityName.replace("市", "");
        }
        CityList tempCity = null;
        for (CityList cityList : trafficCity.result) {
            if (cityList.cityname.equals(cityName)) {
                tempCity = cityList;
            }
        }
        if (tempCity != null) {
            getRule(tempCity.cityid);
        } else {
            mLocatingView.setVisibility(View.GONE);
            mParentView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.GONE);
            mFailView.setVisibility(View.VISIBLE);
            mTvErrorView.setText(String.format("抱歉，%s尚未开通限行查询", currentAMapLocation.getCity()));
        }
    }


    private void getRule(final String cityId) {
        trafficControlHelper.getRule(this,cityId, new TrafficControlHelper.getRuleHandle() {
            @Override
            public void onSuccess(TrafficControlRule controlRule) {
                mLocatingView.setVisibility(View.GONE);
                mParentView.setVisibility(View.VISIBLE);
                mErrorView.setVisibility(View.GONE);
                mFailView.setVisibility(View.GONE);
                initDate(controlRule.result.data);
            }

            @Override
            public void onFailure() {
                showFailView(cityId);
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void initDate(ArrayList<TrafficControlRuleDate> dates) {
        mLocatingView.setVisibility(View.GONE);
        mParentView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
        mFailView.setVisibility(View.GONE);
        mLlRemarkView.removeAllViews();
        mLlForbidView.removeAllViews();
        mLlPenaltyView.removeAllViews();
        for (int i = 0; i < dates.size(); i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 40);

            TextView mRemarksView = new TextView(this);
            mRemarksView.setTextColor(this.getResources().getColor(R.color.black));
            mRemarksView.setTextSize(16);
            mRemarksView.setText(dates.get(i).remarks);
            mRemarksView.setLayoutParams(layoutParams);
            mLlRemarkView.addView(mRemarksView);


            TextView mForbidView = new TextView(this);
            mForbidView.setTextColor(this.getResources().getColor(R.color.black));
            mForbidView.setTextSize(16);
            mForbidView.setText(dates.get(i).message);
            mForbidView.setLayoutParams(layoutParams);
            mLlForbidView.addView(mForbidView);

            TextView mPenaltyView = new TextView(this);
            mPenaltyView.setTextColor(this.getResources().getColor(R.color.black));
            mPenaltyView.setTextSize(16);
            mPenaltyView.setText(dates.get(i).penalty);
            mPenaltyView.setLayoutParams(layoutParams);
            mLlPenaltyView.addView(mPenaltyView);
        }
    }


    @Override
    public void onLocationFail(AMapLocation aMapLocation) {
        showLocateFail();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            String position;
            if (!TextUtils.isEmpty(position = data.getStringExtra("position"))) {
                if (position.equals(mChangeCityView.getText().toString())) {
                    return;
                }
                mChangeCityView.setText(position);
                if (TextUtils.isEmpty(data.getStringExtra("cityID")) && trafficCity != null) {
                    for (int i = 0; i < trafficCity.result.size(); i++) {
                        if (position.equals(trafficCity.result.get(i).cityname)) {
                            getRule(trafficCity.result.get(i).cityid);
                            break;
                        }
                    }
                    return;
                }
                getRule(data.getStringExtra("cityID"));
            }
        }
    }

    //错误界面的显示
    private void showFailView(final String cityId) {
        mLocatingView.setVisibility(View.GONE);
        mParentView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mFailView.setVisibility(View.VISIBLE);
        mFailView.setEmptyDescription("网络连接失败");
        mFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRule(cityId);
            }
        });
        mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }

    //定位失败
    private void showLocateFail() {
        mLocatingView.setVisibility(View.GONE);
        mParentView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mFailView.setVisibility(View.GONE);
        mTvErrorView.setText("定位失败");
        mChangeCityView.setText("定位失败");
    }
}
