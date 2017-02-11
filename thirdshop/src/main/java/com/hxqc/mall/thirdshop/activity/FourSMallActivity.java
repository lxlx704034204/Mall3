package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.fragment.GrouponFragment;
import com.hxqc.mall.core.views.FragmentTabHost;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.fragment.ChooseCarFragment;
import com.hxqc.mall.thirdshop.fragment.FourSMallFragment;
import com.hxqc.mall.thirdshop.fragment.RecommendShopListFragment;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;
import com.hxqc.mall.usedcar.utils.OtherUtil;


/**
 * 说明:4s店网上商城（新版）
 *
 * @author: 吕飞
 * @since: 2016-05-05
 * Copyright:恒信汽车电子商务有限公司
 */
public class FourSMallActivity extends BaseSiteChooseActivity implements View.OnClickListener, TabHost.OnTabChangeListener {
    SharedPreferencesHelper mSharedPreferencesHelper;
    public FragmentTabHost mTabHost;
    private Class fragmentArray[] = {FourSMallFragment.class, ChooseCarFragment.class, RecommendShopListFragment.class, GrouponFragment.class};
    private int iconArray[] = {R.drawable.ic_4s_tabbar_mall, R.drawable.ic_4s_tabbar_choose, R.drawable.ic_4s_tabbar_shop, R.drawable.ic_4s_tabbar_groupon};
    private String[] titleArray;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4s_mall_2);
        mSharedPreferencesHelper = new SharedPreferencesHelper(this);
        titleArray = getResources().getStringArray(R.array.tabbar_4s_array);
        setupView();
        mTabHost.setOnTabChangedListener(this);
        initView();
        initLocationData();
    }


    @Override
    void onResultCallBack(Bundle bundle) {
        ((FourSMallFragment) (getSupportFragmentManager().findFragmentByTag(titleArray[0]))).getData();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle("网上4S店商城");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mChangeCityView = (TextView) findViewById(R.id.change_city);
        mChangeCityView.setOnClickListener(this);
    }

    private void setupView() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(titleArray[i]).setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    private View getTabItemView(int index) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.view_4s_bottom_tabbar, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.main_view_tabbar_icon);
        imageView.setImageResource(iconArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.main_view_tabbar_text);
        textView.setText(titleArray[index]);
        return view;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mSharedPreferencesHelper.get4STabChange()) {
            int mTab = getIntent().getIntExtra(ActivitySwitcherThirdPartShop.TAB_4S, 0);
            mTabHost.setCurrentTab(mTab);
            mSharedPreferencesHelper.set4STabChange(false);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.change_city) {
            ActivitySwitcherThirdPartShop.toSpecialCarChoosePositon(this, 1, ((TextView) v).getText().toString());
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        OtherUtil.setVisible(mChangeCityView, tabId.equals(titleArray[0]));
        if (tabId.equals(titleArray[0])) {
            toolbar.setTitle("网上4S店商城");
            mChangeCityView.setVisibility(View.VISIBLE);
            ((FourSMallFragment) (getSupportFragmentManager().findFragmentByTag(titleArray[0]))).getData();
        } else {
            toolbar.setTitle(tabId);
            mChangeCityView.setVisibility(View.INVISIBLE);
            if (tabId.equals(titleArray[1])) {

            } else if (tabId.equals(titleArray[2])) {

            } else if (tabId.equals(titleArray[3])) {
                GrouponFragment grouponFragment = (GrouponFragment) (getSupportFragmentManager().findFragmentByTag(titleArray[3]));
                if (grouponFragment != null) {
                    grouponFragment.getData();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mTabHost.getCurrentTab() == 3 && ((GrouponFragment) getSupportFragmentManager().findFragmentByTag(titleArray[3])).mWebView.canGoBack()) {
            ((GrouponFragment) getSupportFragmentManager().findFragmentByTag(titleArray[3])).mWebView.goBack();
        } else {
            finish();
        }
    }
}
