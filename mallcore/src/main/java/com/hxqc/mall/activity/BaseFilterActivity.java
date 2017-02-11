package com.hxqc.mall.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.BaseFilterCoreFragment;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.views.MyCoordinatorLayout;
import com.hxqc.mall.core.views.MyRecyclerViewOnScrollListener;
import com.hxqc.mall.core.views.MyUltraPullRefreshHeaderHelper;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.util.DebugLog;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * Function: 所有筛选界面基类（上推列表可以隐藏筛选条件）
 *
 * @author 袁秉勇
 * @since 2016年02月18日
 */
public abstract class BaseFilterActivity extends NoBackActivity implements MyCoordinatorLayout.CallBack, OnRefreshHandler, BaseFilterCoreFragment.FilterMenuListener, BaseFilterCoreFragment.CallBack, View.OnClickListener {
    private final static String TAG = BaseFilterActivity.class.getSimpleName();
    private Context mContext;

    public HashMap< String, String > mFilterMap = new HashMap<>();
    protected BaseSharedPreferencesHelper baseSharedPreferencesHelper;
    protected BaseFilterCoreFragment baseFilterCoreFragment;
    protected RecyclerView.Adapter adapter;
    protected Toolbar toolbar;
    protected Fragment lastFragment = new Fragment();
    protected PtrFrameLayout mPtrFrameLayoutView;
    protected MyUltraPullRefreshHeaderHelper mPtrHelper;
    protected MyRecyclerViewOnScrollListener myRecyclerViewOnScrollListener;
    protected LatLng latLng = null;
    protected TextView mChangeCityView;         // 选择city
    protected EditText mSearchContentView;      // 搜索输入框
    protected LinearLayout mSearchButtonView;   // 搜索按钮
    protected OverlayDrawer mOverlayDrawer;
    protected ArrayList< Fragment > fragments = new ArrayList<>();
    protected MyCoordinatorLayout myCoordinatorLayoutView;
    protected RecyclerView mRecyclerView;
    protected RequestFailView mRequestFailView;
    protected int PER_PAGE = 20;
    protected int mPage = 1;
    protected int DEFAULT_PATE = 1;
    /** 加载数据相关 **/
    protected boolean hasMore = false;
    LinearLayout mTipView;


    /** 初始化筛选控制器中的HashMap **/
    protected abstract void initHashMap();

    /** 设置Activity的标题名 **/
    protected abstract void setTitle();

    /** 初始化第三方接口 **/
    protected abstract void initApiClient();

    /** 初始化RecyclerView的Adapter **/
    protected abstract void initAdapter();

    /** 初始化fragment **/
    protected abstract void initFragment();//初始化baseFilterCoreFragment

    /** 筛选条件不同，初始化不同的fragment **/
    protected abstract void initFragmentView();

    /** 初始化列表数据 **/
    protected abstract void initData();

    /** 刷新数据时调用方法 **/
    protected abstract void refreshData();

    /** 点击定位城市选择界面方法 **/
    protected abstract void toPositionChoose();

    /** 清空adapter中的数据 **/
    protected abstract void clearAdapterData();

    /** 是否显示Adapter中的距离视图 **/
    protected abstract void showDistanceInAdapter(boolean flag);//flag 为true时显示，false不显示


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugLog.e(TAG, " -------------> onCreate");
        setContentView(R.layout.activity_filter_core_view);
        getWindow().setBackgroundDrawable(null);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(this);
        initHashMap();
        initApiClient();
        initAdapter();

        mChangeCityView = (TextView) findViewById(R.id.change_city);

        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
        mOverlayDrawer.setSidewardCloseMenu(true);

        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.refresh_frame);
        myRecyclerViewOnScrollListener = new MyRecyclerViewOnScrollListener(this);
        mPtrHelper = new MyUltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView, myRecyclerViewOnScrollListener);
        mPtrHelper.setOnRefreshHandler(this);
        mPtrHelper.setCanDoPullRefresh(false);//禁止控件下拉显示“搜索”界面

        findViewById(R.id.tip_choose_view).setVisibility(View.GONE);
        mSearchContentView = (EditText) findViewById(R.id.search_tip);
        mSearchButtonView = (LinearLayout) findViewById(R.id.search);

        myCoordinatorLayoutView = (MyCoordinatorLayout) findViewById(R.id.coordinatorlayout);
        myCoordinatorLayoutView.setCallBack(this);
        myCoordinatorLayoutView.setInterceptMove(true);

        mTipView = (LinearLayout) findViewById(R.id.tip_view);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        initFragment();
        baseFilterCoreFragment.setCallBack(this);
        fragmentTransaction.add(R.id.collapse_view, baseFilterCoreFragment);
        fragmentTransaction.commit();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(null);

        mRequestFailView = (RequestFailView) findViewById(R.id.request_view);

        /** 获取历史定位城市数据并上传city */
        initLocationData();

        initData();

        setListener();
    }


    public void setListener() {
        mChangeCityView.setOnClickListener(this);
        mSearchButtonView.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        DebugLog.e(TAG, "------------ onStart");
        initFragmentView();
    }


    @Override
    public void onResume() {
        super.onResume();
        DebugLog.e(TAG, " -------------- onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
        DebugLog.e(TAG, " ----------- onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        DebugLog.e(TAG, " -------------- onStop");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DebugLog.e(TAG, "-------------- onDestroy");
    }


    /**
     * 初始化当前坐标
     * final String latitu, final String longitu, final String c
     */
    protected void initLocationData() {
        if (baseSharedPreferencesHelper == null) {
            baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(this);
        }

        LinkedList< String > historyCityList = baseSharedPreferencesHelper.getHistoryCity();
        if (historyCityList.isEmpty()) {
            showLocationIcon(false);
            mChangeCityView.setText("全国");
            mFilterMap.put("area", "全国");
            startSettingDialog(baseSharedPreferencesHelper.getCity());
        } else {
            String historyCity = historyCityList.getFirst();
            if (!TextUtils.isEmpty(historyCity)) {
                mChangeCityView.setText(historyCity);
                if (!"全国".equals(historyCity)) mFilterMap.put("area", mChangeCityView.getText().toString());
            } else {
                mChangeCityView.setText("全国");
            }

            String city = baseSharedPreferencesHelper.getCity();

            if (!city.equals(historyCity)) {
                showLocationIcon(false);
                startSettingDialog(city);
            } else {
                showLocationIcon(true);
                showDistanceInAdapter(true);
            }
        }

        //只有定位成功后才上传地理坐标
//        if (!TextUtils.isEmpty(latitu) && !TextUtils.isEmpty(longitu)) {
        try {
            if (!TextUtils.isEmpty(baseSharedPreferencesHelper.getLongitudeBD()) && !TextUtils.isEmpty(baseSharedPreferencesHelper.getLatitudeBD()))
                latLng = new LatLng(Double.valueOf(baseSharedPreferencesHelper.getLatitudeBD()), Double.valueOf(baseSharedPreferencesHelper.getLongitudeBD()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (latLng != null) {
            mFilterMap.put("latitude", Double.toString(latLng.latitude));
            mFilterMap.put("longitude", Double.toString(latLng.longitude));
        }
    }


    /**
     * @param city 当前城市
     * @return
     */
    private void startSettingDialog(final String city) {
        if (TextUtils.isEmpty(city) || baseSharedPreferencesHelper.getPositionTranslate()) {
            return;
        } else {
            baseSharedPreferencesHelper.setPositionTranslate(true);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialog);
        builder.setCancelable(false);
        builder.setTitle("提示").
                setMessage("您当前城市是【" + city + "】，是否需要进行数据切换？")//您当前城市是【%@】，需要切换吗？
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLocationIcon(true);
                        mChangeCityView.setText(city);
                        mFilterMap.put("area", city);
                        showDistanceInAdapter(true);
                        refreshData();
                        baseSharedPreferencesHelper.setHistoryCity(city);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).create().show();
    }


    @Override
    public boolean hasMore() {
        return hasMore;
    }


    @Override
    public void onRefresh() {

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.change_city) {
            toPositionChoose();
        }
    }


    /**
     * @param isNull 当前返回的是不是空或者ArrayList的长度等于0
     */
    protected void FoldJudgment(boolean isNull) {
        adapter = mRecyclerView.getAdapter();
        if (isNull) {
            if (adapter != null) {
                clearAdapterData();
                adapter.notifyDataSetChanged();
            }
            myCoordinatorLayoutView.setInterceptMove(true);
            mRequestFailView.setEmptyDescription("未找到结果");
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
            mRequestFailView.setVisibility(View.VISIBLE);
        } else {
            mRequestFailView.setVisibility(View.GONE);
            if (adapter != null) {
                myCoordinatorLayoutView.setInterceptMove(false);
                if (!isCanScrollVertically(mRecyclerView) && mTipView.getVisibility() == View.VISIBLE) mTipView.setVisibility(View.GONE);
            }
        }
        if (mPtrFrameLayoutView.isRefreshing()) {
            mPtrFrameLayoutView.refreshComplete();
            mRecyclerView.requestFocus();
        }
    }


    /** 判断RecyclerView是否铺满全屏，即是否可向上滑动 **/
    private boolean isCanScrollVertically(RecyclerView recyclerView) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(recyclerView, 1) || recyclerView.getScrollY() < recyclerView.getHeight();
        } else {
            return ViewCompat.canScrollVertically(recyclerView, 1);
        }
    }


    /** 展开侧滑Fragment **/
    @Override
    public void showFilterFactor(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragments.contains(fragment)) {
            fragments.add(fragment);
            transaction.add(R.id.mdMenu, fragment);
            if (fragments.size() > 0 && lastFragment != fragment) {
                transaction.hide(lastFragment);
            }
        } else {
            if (lastFragment != fragment) transaction.hide(lastFragment).show(fragment);
        }
        transaction.commitAllowingStateLoss();
        lastFragment = fragment;
        mOverlayDrawer.openMenu();
    }


    /** 关闭侧滑Fragment条件 **/
    @Override
    public void closeFilterFactor() {
        if (mOverlayDrawer.isMenuVisible()) mOverlayDrawer.closeMenu();
    }


    @Override
    public void setRecyclerViewPullCondition(int pullCondition) {
/** 设置EmptyView时RequestFailView的layoutParams **/
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, pullCondition, 0, 0);
        mRequestFailView.setLayoutParams(layoutParams);

        /** 设置下拉搜索的条件 **/
        mPtrHelper.setPullCondition(pullCondition);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        echo(mFilterMap);

        if (requestCode == 1 && resultCode == 1) {

            String position;

            if (!TextUtils.isEmpty(position = data.getStringExtra("position"))) {

                if (position.equals(mChangeCityView.getText().toString())) {
                    return;
                } else {
                    mChangeCityView.setText(data.getStringExtra("position"));
                }

                if (baseSharedPreferencesHelper.getCity().equals(position)) {
                    showLocationIcon(true);
                    showDistanceInAdapter(true);
                } else {
                    showLocationIcon(false);
                    showDistanceInAdapter(false);
                }
                refreshData();
            }
        }
    }


    public void showLocationIcon(boolean flag) {
        if (mChangeCityView == null) return;
        if (flag) {
            if ((mChangeCityView.getCompoundDrawables())[0] == null) mChangeCityView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.t_icon_button_location), null, null, null);
        } else {
            if ((mChangeCityView.getCompoundDrawables())[0] != null) {
                mChangeCityView.setCompoundDrawables(null, null, null, null);
            }
        }
    }


    /** 打印HashMap中的数据 **/
    public void echo(HashMap< String, String > map) {
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            DebugLog.e(TAG, "key : " + key + " ,  value : " + val);
        }
    }
}
