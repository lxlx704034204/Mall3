package com.hxqc.autonews.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hxqc.autonews.adapter.FragmentPagerAdapter;
import com.hxqc.autonews.model.AutoEvent;
import com.hxqc.autonews.model.AutoInfoTypeModel;
import com.hxqc.autonews.model.AutoInformationModel;
import com.hxqc.autonews.model.pojos.AutoInfoHomeData;
import com.hxqc.autonews.model.pojos.InfoType;
import com.hxqc.autonews.presenter.Presenter;
import com.hxqc.autonews.util.ActivitySwitchAutoInformation;
import com.hxqc.autonews.view.AutoInfoHomeDataHandler;
import com.hxqc.autonews.view.RequestDataWithCacheHandler;
import com.hxqc.autonews.widget.IScrollSateHandler;
import com.hxqc.autonews.widget.StickyNavLayout;
import com.hxqc.autonews.widget.TopBanner;
import com.hxqc.mall.activity.MainActivity;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 汽车资讯
 */

public class AutoInfoFragment_3 extends FunctionFragment implements OnRefreshHandler,
        RequestDataWithCacheHandler<ArrayList<InfoType>>, AutoInfoHomeDataHandler, View.OnClickListener {
    public static final int REFRESH_CODE = 0x1000;//刷新
    public static final int REFRESH_COMPLETE_CODE = 0x2000;
    private Toolbar mToolbar;
    public static String mType = "最新";
    //    private SimpleViewPagerIndicator tabLayout;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    //    private AutoInfoPagerAdapter pagerAdapter;
    private RecommentAutoInfoListFragment fragment;
    private FragmentPagerAdapter adapter;
    private ArrayList<FunctionFragment> fragments;
    private ArrayList<InfoType> infoTypes = new ArrayList<>();

    //    private PtrFrameLayout mPtrFrameLayout;
//    private UltraPullRefreshHeaderHelper helper;
    private RequestFailView mRequestFailView;
    private Presenter mPresenter;
    private AutoInformationModel model;
    private AutoInfoTypeModel typeModel;
    private TopBanner mBanner;
    private StickyNavLayout stickyNavLayout;
    private MainActivity activity;
    private boolean hasLoad;
    private ImageButton fabAutoMatch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto_info_3, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabAutoMatch = (ImageButton) view.findViewById(R.id.fab_auto_match);
        fabAutoMatch.setOnClickListener(this);
        mRequestFailView = (RequestFailView) view.findViewById(R.id.request_fail_view);
        mRequestFailView.setEmptyButtonClick("暂无数据", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getAutoInfoTypes(AutoInfoFragment_3.this, typeModel);
            }
        });
        fragments = new ArrayList<>();
        initStickyNavLayout(view);
        initBanner(view);
        initViewPager(view);
        initToolBar(view);
        initTabLayout(view);
        model = new AutoInformationModel(mContext);
        typeModel = new AutoInfoTypeModel(mContext);
        mPresenter = new Presenter();
        mPresenter.getAutoInfoTypes(this, typeModel);
        EventBus.getDefault().register(this);
    }

    private void initStickyNavLayout(View view) {
        stickyNavLayout = (StickyNavLayout) view.findViewById(R.id.sticky_nav_layout);
        stickyNavLayout.setOnNestScrollListener(new StickyNavLayout.OnNestScrollListener() {
            @Override
            public void onNestedPreScroll() {
                //设置为false
                int currentItem = mViewPager.getCurrentItem();
                IScrollSateHandler handler = (IScrollSateHandler) fragments.get(currentItem);
                if (handler != null) {
                    handler.onNestedPreScroll();
                }
            }

            @Override
            public void onNestedScroll() {
                //设置为true
                int currentItem = mViewPager.getCurrentItem();
                IScrollSateHandler handler = (IScrollSateHandler) fragments.get(currentItem);
                if (handler != null) {
                    handler.onNestedScroll();
                }
            }
        });
    }

    private void initViewPager(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
    }


    @Override
    public void onResume() {
        super.onResume();
        toIndexTab();
    }

//    int bannerHeight;

    private void initTabLayout(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        adapter = new FragmentPagerAdapter(getChildFragmentManager(), fragments);
        adapter.setTitles(getTitles(infoTypes));
        mViewPager.setAdapter(adapter);

    }

    public String[] getTitles(ArrayList<InfoType> infoTypes) {
        String[] titles = new String[infoTypes.size()];
        for (int i = 0; i < infoTypes.size(); i++) {
            titles[i] = infoTypes.get(i).guideName;
        }
        return titles;
    }

    private void initBanner(View view) {
        mBanner = (TopBanner) view.findViewById(R.id.banner_slider);
    }

    private void initToolBar(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("汽车资讯");
        mToolbar.setTitleTextColor(Color.WHITE);
    }

    private void loadData() {
        DebugLog.d(getClass().getSimpleName(), "loadData");
        mPresenter.getAutoInfoHomeData(this, model);
    }

    @Subscribe
    public void onEvent(AutoEvent event) {
        if (event != null) {
            if (event.eventCode == REFRESH_CODE) {
                hasLoad = false;
                mPresenter.getAutoInfoTypes(this, typeModel);
            }
        }

    }

    private void setTabLayout(ArrayList<InfoType> obj) {
        infoTypes.clear();
        fragments.clear();
        stickyNavLayout.setVisibility(View.VISIBLE);
        mRequestFailView.setVisibility(View.GONE);
        //添加最新
        if (fragment == null)
            fragment = RecommentAutoInfoListFragment.newInstance();
        fragments.add(fragment);
        infoTypes.add(new InfoType("最新", "-1"));
        //添加其他类型
        for (int i = 0; i < obj.size(); i++) {
            InfoType infoType = obj.get(i);
            boolean equals = infoType.guideName.equals("最新");
            if (!equals) {
                AutoInfoListFragment fragment = AutoInfoListFragment.newInstance(infoType.guideCode);
                infoTypes.add(infoType);
                fragments.add(fragment);
            }
        }
//        infoTypes.addAll(obj);
        adapter.setTitles(getTitles(infoTypes));
        adapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setVisibility(View.VISIBLE);
        if (infoTypes.size() > 4) {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else
            tabLayout.setTabMode(TabLayout.GRAVITY_CENTER);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (infoTypes.size() > position)
                    mType = infoTypes.get(position).guideName;
            }
        });
        toIndexTab();
        if (!hasLoad) {
            hasLoad = true;
            loadData();
        }
    }

    public void onDataNull(String msg) {
        //类型的获取
        tabLayout.setVisibility(View.GONE);
        stickyNavLayout.setVisibility(View.GONE);
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        mRequestFailView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDataResponse(ArrayList<InfoType> obj) {
        //类型的获取
        setTabLayout(obj);
    }

    @Override
    public void onCacheDataBack(ArrayList<InfoType> obj) {
        //类型的获取
        setTabLayout(obj);
    }

    @Override
    public void onCacheDataNull() {
        //类型的获取
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBanner.destroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public String fragmentDescription() {
        return "汽车资讯";
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
    }


    @Override
    public void onHomeDataResponse(AutoInfoHomeData homeData) {
        //banner和推荐的第一页
        mBanner.bindData(homeData.banner);
        if (fragment != null) {
            fragment.addData(homeData.infoList);
        }
    }

    @Override
    public void onHomeDataNull(String msg) {
        //banner和推荐的第一页
    }

    public void toIndexTab() {
        for (int i = 0; i < infoTypes.size(); i++) {
            if (mType.equals(infoTypes.get(i).guideName)) {
                TabLayout.Tab tabAt = tabLayout.getTabAt(i);
                if (tabAt != null) {
                    tabAt.select();
                }
                mViewPager.setCurrentItem(i);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_auto_match:
                ActivitySwitchAutoInformation.toCarCompare(mContext, null, null, null);
                break;
        }
    }
}