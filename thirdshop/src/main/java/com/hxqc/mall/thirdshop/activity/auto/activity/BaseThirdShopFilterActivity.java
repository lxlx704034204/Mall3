package com.hxqc.mall.thirdshop.activity.auto.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.MyCoordinatorLayout;
import com.hxqc.mall.core.views.MyRecyclerViewOnScrollListener;
import com.hxqc.mall.core.views.MyUltraPullRefreshHeaderHelper;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
import com.hxqc.mall.thirdshop.activity.auto.fragment.FilterThirdShopCoreFragment_2;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.Brand;
import com.hxqc.mall.thirdshop.model.CarType;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.model.ShopSearchAuto;
import com.hxqc.mall.thirdshop.model.ShopSearchShop;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;
import com.hxqc.mall.thirdshop.views.adpter.SearchCarBrandsAdapter;
import com.hxqc.mall.thirdshop.views.adpter.SearchShopAdapter;
import com.hxqc.util.DebugLog;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年05月18日
 * private final static String TAG = BaseThirdShopFilterActivity.class.getSimpleName();
 * private Context mContext;
 */
public abstract class BaseThirdShopFilterActivity extends NoBackActivity implements ControllerConstruct, FilterThirdShopCoreFragment_2.FilterMenuListener, FilterThirdShopCoreFragment_2.FilterThirdShopCoreFragmentCallBack, OnRefreshHandler, BaseFilterController.TCarHandler, BaseFilterController.TShopHandler, View.OnClickListener, MyCoordinatorLayout.CallBack {
    private final static String TAG = BaseThirdShopFilterActivity.class.getSimpleName();
    private Context mContext;
    protected SharedPreferencesHelper sharedPreferencesHelper;

    static int RECENT_OPERATE_TYPE = 0;//最近一次操作 0为筛选，1为搜索

    public Fragment lastFragment = new Fragment();

    protected PtrFrameLayout mPtrFrameLayoutView;
    protected MyUltraPullRefreshHeaderHelper mPtrHelper;

    protected LatLng latLng = null;
    protected TextView mChangeCityView; // 选择city
    protected TextView mChangeSearchTypeView; // 选择搜索类型，店铺or车型
    protected EditText mSearchContentView; // 搜索输入框
    protected LinearLayout mSearchButtonView; // 搜索按钮

    protected OverlayDrawer mOverlayDrawer;
    protected FilterThirdShopCoreFragment_2 filterThirdShopCoreFragment_2;

    protected BaseFilterController baseFilterController;

    protected ArrayList< Fragment > fragments = new ArrayList<>();
    protected MyCoordinatorLayout myCoordinatorLayoutView;
    protected LinearLayout mTipView;
    protected RecyclerView mRecyclerView;
    protected RequestFailView mRequestFailView;

    protected TextView mTipCarNameView;
    protected String carName = "";

    protected SearchCarBrandsAdapter searchCarBrandsAdapter; // 车辆adapter

    protected SearchShopAdapter searchShopAdapter; // 店铺adapter

    protected ThirdPartShopClient ThirdPartShopClient;

    protected int searchType = 1; // 请求数据接口 0为店铺, 1为车型
    protected int requestType = 0; // 请求数据接口 0为店铺, 1为车型
    protected int PER_PAGE = 15;
    protected int mPage = 1;
    protected int DEFAULT_PATE = 1;
    /**
     * 加载数据相关
     */
    protected boolean hasMore = false;
    protected String brandName;
    protected String seriesName;
    protected Series series;
    protected String modelName;
    protected MyRecyclerViewOnScrollListener myRecyclerViewOnScrollListener;


    protected abstract void initSharedPreferences();

    protected abstract void initLocationData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThirdPartShopClient = new ThirdPartShopClient();
        initSharedPreferences();
        initController();

        setContentView(R.layout.activity_filter_core_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
//        toolbar.setTitle("网上4S店报价");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /** 来自新车的数据 */
        brandName = getIntent().getStringExtra(ActivitySwitcherThirdPartShop.BRAND);
        series = getIntent().getParcelableExtra(ActivitySwitcherThirdPartShop.SERIESNAME);
        modelName = getIntent().getStringExtra(ActivitySwitcherThirdPartShop.MODEL);


        //城市选择View
        mChangeCityView = (TextView) findViewById(R.id.change_city);
//        mChangeCityView.setText("定位中");//默认显示定位中
        //从SharedPreferences中拿历史定位数据
//        initLocationData(null, null, null);

//        /**开启定位*/
//        locationControl = AMapLocationControl.getInstance(this);
//        locationControl.setCoreLocationListener(this);
//        locationControl.requestLocation();

        initAdapter();

        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
        mOverlayDrawer.setSidewardCloseMenu(true);

        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.refresh_frame);
        myRecyclerViewOnScrollListener = new MyRecyclerViewOnScrollListener(this);
        mPtrHelper = new MyUltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView, myRecyclerViewOnScrollListener);
        mPtrHelper.setOnRefreshHandler(this);

        /**搜索相关的view*/
        mChangeSearchTypeView = (TextView) findViewById(R.id.change_search_type);
        mSearchContentView = (EditText) findViewById(R.id.search_tip);
        mSearchButtonView = (LinearLayout) findViewById(R.id.search);

        myCoordinatorLayoutView = (MyCoordinatorLayout) findViewById(R.id.coordinatorlayout);
        myCoordinatorLayoutView.setCallBack(this);
        myCoordinatorLayoutView.setInterceptMove(true);
//        mCollapseView = (LinearLayout) findViewById(R.id.collapse_view);
        mTipView = (LinearLayout) findViewById(R.id.tip_view);
        mTipCarNameView = (TextView) findViewById(R.id.tip_car_name);
//        mTipFragmentView = (FrameLayout) findViewById(R.id.tip_fragment);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        filterThirdShopCoreFragment_2 = new FilterThirdShopCoreFragment_2();
        filterThirdShopCoreFragment_2.setFilterThirdShopCoreFragmentCallBack(this);
        fragmentTransaction.add(R.id.collapse_view, filterThirdShopCoreFragment_2);
        fragmentTransaction.commit();

        /** 将新车数据填入到筛选视图中 */
        if (!TextUtils.isEmpty(brandName) && null != series && !TextUtils.isEmpty(modelName)) {
            requestType = 1;
            baseFilterController.mFilterMap.put("brand", brandName);
            baseFilterController.mFilterMap.put("serie", series.seriesName);
            baseFilterController.mFilterMap.put("model", modelName);

            baseFilterController.setBrand(new Brand(brandName));
            baseFilterController.setSeries(series);
            baseFilterController.setCarType(new CarType(modelName));

            this.seriesName = brandName + " " + series.seriesName;
            carName = brandName + " " + series.seriesName + " " + modelName;
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(null);
        mRequestFailView = (RequestFailView) findViewById(R.id.request_view);

        /** 获取历史定位城市数据并上传city */
        initLocationData();

        /** 如果是从新车进入该页就车辆数据，否则搜索店铺数据 */
//        if (!TextUtils.isEmpty(brandName) && null != series && !TextUtils.isEmpty(modelName)) {
//            baseFilterController.getCarData(this, mPage, PER_PAGE, baseFilterController.mFilterMap, this);
//        } else {
//            baseFilterController.getShopData(this, mPage, PER_PAGE, baseFilterController.mFilterMap, this);
//        }
        getData(true);

        setListener();
    }


    public void setListener() {
        mChangeCityView.setOnClickListener(this);
        mChangeSearchTypeView.setOnClickListener(this);
        mSearchButtonView.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mTipCarNameView.setText(carName);
    }


    /*** 初始化Adapter */
    private void initAdapter() {
        //初始车辆adapter
        searchCarBrandsAdapter = new SearchCarBrandsAdapter() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                final Holder holder = super.onCreateViewHolder(parent, viewType);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopSearchAuto itemData = getItemData(holder.getAdapterPosition());
                        ActivitySwitcherThirdPartShop.toCarDetail(itemData.autoInfo.itemID, itemData.shopInfo.shopID, itemData.shopInfo.shopTitle, BaseThirdShopFilterActivity.this);
                    }
                });
                holder.car_dial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OtherUtil.callPhone(BaseThirdShopFilterActivity.this, getItemData(holder.getAdapterPosition()).shopInfo.shopTel);
                    }
                });
                holder.ask_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopSearchAuto itemData = getItemData(holder.getAdapterPosition());
                        ActivitySwitcherThirdPartShop.toAskLeastPrice(BaseThirdShopFilterActivity.this, itemData.shopInfo.shopID, itemData.autoInfo.itemID, itemData.autoInfo.itemName, itemData.shopInfo.shopTel, true, null);
                    }
                });
                return holder;

            }
        };

        //初始店铺Adapter
        searchShopAdapter = new SearchShopAdapter() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                final Holder holder = super.onCreateViewHolder(parent, viewType);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopSearchShop itemData = getItemData(holder.getAdapterPosition());
                        ActivitySwitcherThirdPartShop.toShopDetails(BaseThirdShopFilterActivity.this, itemData.shopID);
                    }
                });
                return holder;
            }
        };
    }


    @Override
    public void onCallBack(String key, String value) {
        if (!TextUtils.isEmpty(value)) baseFilterController.putValue(key, value);
        switch (key) {
            // 搜店铺
            case "brand":
                requestType = 0;
                RECENT_OPERATE_TYPE = 0;
                if (baseFilterController.mFilterMap.containsKey("keyword")) baseFilterController.mFilterMap.remove("keyword");
                echo(baseFilterController.mFilterMap);
                if (TextUtils.isEmpty(value)) {
                    if (baseFilterController.getBrand() != null) {
//                        baseFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, baseFilterController.mFilterMap, this);
                        getData(true);
                    }
                } else {
                    if (baseFilterController.getBrand() == null) {
                        getData(true);
//                        baseFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, baseFilterController.mFilterMap, this);
                    } else if (baseFilterController.getBrand() != null && !value.equals(baseFilterController.getBrand().brandName)) {
                        getData(true);
//                        baseFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, baseFilterController.mFilterMap, this);
                    }
                }
                if (!TextUtils.isEmpty(value)) {
                    carName = value;
                    brandName = value;
                } else {
                    carName = "全部";
                }
                break;

            // 搜店铺
            case "serie":
                requestType = 0;
                RECENT_OPERATE_TYPE = 0;
                if (baseFilterController.mFilterMap.containsKey("keyword")) baseFilterController.mFilterMap.remove("keyword");
                echo(baseFilterController.mFilterMap);
                if (TextUtils.isEmpty(value)) {
                    if (baseFilterController.getSeries() != null) {
                        getData(true);
//                        baseFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, baseFilterController.mFilterMap, this);
                    }
                } else {
                    if (baseFilterController.getSeries() == null) {
                        getData(true);
//                        baseFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, baseFilterController.mFilterMap, this);
                    } else if (baseFilterController.getSeries() != null && !value.equals(baseFilterController.getSeries().seriesName)) {
                        getData(true);
//                        baseFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, baseFilterController.mFilterMap, this);
                    }
                }
                if (!TextUtils.isEmpty(value)) {
                    carName = brandName + " " + value;
                    seriesName = brandName + " " + value;
                }
                break;

            // 搜车辆
            case "model":
                RECENT_OPERATE_TYPE = 0;
                if (baseFilterController.mFilterMap.containsKey("keyword")) baseFilterController.mFilterMap.remove("keyword");
                echo(baseFilterController.mFilterMap);
                if (!TextUtils.isEmpty(value)) {
                    requestType = 1;
                    if (baseFilterController.getCarType() == null) {
                        getData(true);
//                        baseFilterController.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, baseFilterController.mFilterMap, this);
                    } else if (baseFilterController.getCarType() != null && !value.equals(baseFilterController.getCarType().modelName)) {
                        getData(true);
//                        baseFilterController.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, baseFilterController.mFilterMap, this);
                    }
                } else {
                    requestType = 0;
                    if (baseFilterController.getCarType() != null) getData(true);
//                        baseFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, baseFilterController.mFilterMap, this);
                }
                if (!TextUtils.isEmpty(value)) carName = seriesName + " " + value;
                break;

            case "area":
                if (!TextUtils.isEmpty(value)) {
                    mChangeCityView.setText(value);
                } else {
                    mChangeCityView.setText("全国");
                }
                mChangeCityView.setCompoundDrawables(null, null, null, null);//手动选择地区,不要定位的图标
                if (!TextUtils.isEmpty(value)) {
//                    searchHashMap.put("area", value);
                } else {
//                    searchHashMap.remove("area");
                }
                echo(baseFilterController.mFilterMap);
//                echo(searchHashMap);
//                if (RECENT_OPERATE_TYPE == 0) {
//                if (requestType == 0) {
//                    baseFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, baseFilterController.mFilterMap, this);
//                } else if (requestType == 1) {
//                    baseFilterController.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, baseFilterController.mFilterMap, this);
//                }
                getData(true);
//                } else if (RECENT_OPERATE_TYPE == 1) {
//                    if (searchType == 0) {
//                        baseFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, searchHashMap, this);
//                    } else if (searchType == 1) {
//                        baseFilterController.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, searchHashMap, this);
//                    }
//                }
                break;
        }
        mTipCarNameView.setText(carName);
    }


    public void echo(HashMap< String, String > map) {
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            DebugLog.e(TAG, "key : " + key + " ,  value : " + val);
        }
    }


    @Override
    public void finish() {
        super.finish();
        baseFilterController.destroy();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RECENT_OPERATE_TYPE = 0;
        destroyController();
    }


    /**
     * 车辆搜索成功回调
     *
     * @param shopSearchAutos
     */
    @Override
    public void onGetCarSucceed(ArrayList< ShopSearchAuto > shopSearchAutos) {
        if (shopSearchAutos == null) {
            if (mPage == DEFAULT_PATE) {
                FoldJudgment(true);
            }
            return;
        }
        hasMore = shopSearchAutos.size() >= PER_PAGE;
        if (mPage == DEFAULT_PATE) {
            searchCarBrandsAdapter.list.clear();
            if (shopSearchAutos.size() > 0) {
                mRecyclerView.setAdapter(searchCarBrandsAdapter);
            } else {
                FoldJudgment(true);
                return;
            }
        }
        searchCarBrandsAdapter.addDate(shopSearchAutos);
        FoldJudgment(false);
    }


    @Override
    public void onGetCarFailed() {
        FoldJudgment(true);
    }


    /**
     * 店铺搜索成功回调
     *
     * @param shopSearchShops
     */
    @Override
    public void onGetShopSucceed(ArrayList< ShopSearchShop > shopSearchShops) {
        if (shopSearchShops == null) {
            if (mPage == DEFAULT_PATE) {
                FoldJudgment(true);
            }
            return;
        }
        hasMore = shopSearchShops.size() >= PER_PAGE;
        if (mPage == DEFAULT_PATE) {
            searchShopAdapter.list.clear();
            if (shopSearchShops.size() > 0) {
                mRecyclerView.setAdapter(searchShopAdapter);
            } else {
                FoldJudgment(true);
                return;
            }
        }
        searchShopAdapter.addDate(shopSearchShops);
        FoldJudgment(false);
    }


    @Override
    public void onGetShopFailed() {
        FoldJudgment(true);
    }


    /**
     * @param isNull 当前返回的是不是空或者ArrayList的长度等于0
     */
    public void FoldJudgment(boolean isNull) {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (isNull) {
            if (adapter != null) {
                if (adapter instanceof SearchShopAdapter) {
                    ((SearchShopAdapter) adapter).list.clear();
                } else if (adapter instanceof SearchCarBrandsAdapter) {
                    ((SearchCarBrandsAdapter) adapter).list.clear();
                }
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
                if (!isCanScollVertically(mRecyclerView) && mTipView.getVisibility() == View.VISIBLE) mTipView.setVisibility(View.GONE);
            }
        }
        if (mPtrFrameLayoutView.isRefreshing()) {
            mPtrFrameLayoutView.refreshComplete();
            mRecyclerView.requestFocus();
        }
    }


    /**
     * 判断RecyclerView是否铺满全屏，即是否可向上滑动
     */
    private boolean isCanScollVertically(RecyclerView recyclerView) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(recyclerView, 1) || recyclerView.getScrollY() < recyclerView.getHeight();
        } else {
            return ViewCompat.canScrollVertically(recyclerView, 1);
        }
    }


    @Override
    public boolean hasMore() {
        return hasMore;
    }


    @Override
    public void onRefresh() {

    }


    @Override
    public void onLoadMore() {
        ++mPage;
        getData(false);
        hasMore = false;
    }


    /**
     * 管理侧滑fragment列表
     */
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


    /**
     * 关闭侧滑列表
     */
    @Override
    public void closeFilterFactor() {
        if (mOverlayDrawer.isMenuVisible()) mOverlayDrawer.closeMenu();
    }


    @Override
    public void onClick(final View v) {
        int i = v.getId();
        if (i == R.id.change_city) {
            if (this.getClass() == ThirdShopFilterForNewAuto.class) {
                ActivitySwitcherThirdPartShop.toPositionActivity(this, 1, ((TextView) v).getText().toString());
            } else {
                ActivitySwitcherThirdPartShop.toSpecialCarChoosePositon(this, 1, ((TextView) v).getText().toString());
            }
        } else if (i == R.id.change_search_type) {  /** 切换搜索类型，0为店铺，1为车型 */
            DebugLog.e(getClass().getName(), mChangeSearchTypeView.getWidth() + " " + mChangeSearchTypeView.getHeight());

            View rootView = View.inflate(this, R.layout.third_filter_type_popup, null);
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            final PopupWindow popupWindow = new PopupWindow(rootView, (mChangeSearchTypeView.getWidth() - size), ViewGroup.LayoutParams.WRAP_CONTENT, true);

            View typeShop = rootView.findViewById(R.id.type_shop);
            typeShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchType = 0;
                    mChangeSearchTypeView.setText("店铺");
                    popupWindow.dismiss();
                }
            });

            View typeCar = rootView.findViewById(R.id.type_car);
            typeCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchType = 1;
                    mChangeSearchTypeView.setText("车型");
                    popupWindow.dismiss();
                }
            });
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.showAsDropDown(mChangeSearchTypeView, size / 2, -size / 2);

            /** 根据搜索条件搜索车辆 */
        } else if (i == R.id.search) {
            if (TextUtils.isEmpty(mSearchContentView.getText())) {
                ToastHelper.showRedToast(this, "请输入搜索条件");
                return;
            }
            filterThirdShopCoreFragment_2.onSearchClearFilter();
            RECENT_OPERATE_TYPE = 1; // 最近一次操作改为搜索
            mTipCarNameView.setText(mSearchContentView.getText());

            baseFilterController.mFilterMap.put("keyword", mSearchContentView.getText().toString().trim());

            echo(baseFilterController.mFilterMap);

            getData(true);
//            switch (searchType) {
//                case 0:
////                        baseFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, searchHashMap, this);
//                    baseFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, baseFilterController.mFilterMap, this);
//                    break;
//
//                case 1:
////                        baseFilterController.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, searchHashMap, this);
//                    baseFilterController.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, baseFilterController.mFilterMap, this);
//                    break;
//            }
        }
    }


    protected void showLocationIcon(boolean flag) {
        if (mChangeCityView == null) return;
        if (flag) {
            if ((mChangeCityView.getCompoundDrawables())[0] == null) mChangeCityView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.t_icon_button_location), null, null, null);
        } else {
            if (mChangeCityView.getCompoundDrawables().length > 0) mChangeCityView.setCompoundDrawables(null, null, null, null);
        }
    }


    @Override
    public void setRecyclerViewPullCondition(int pullCondition) {
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, pullCondition, 0, 0);
        mRequestFailView.setLayoutParams(layoutParams);
        mPtrHelper.setPullCondition(pullCondition);
    }


    /** 获取数据 **/
    protected void getData(boolean initPage) {
        if (RECENT_OPERATE_TYPE == 0) {
            if (requestType == 0) {
                baseFilterController.getShopData(this, initPage ? mPage = DEFAULT_PATE : mPage, PER_PAGE, baseFilterController.mFilterMap, this);
            } else if (requestType == 1) {
                baseFilterController.getCarData(this, initPage ? mPage = DEFAULT_PATE : mPage, PER_PAGE, baseFilterController.mFilterMap, this);
            }
        } else if (RECENT_OPERATE_TYPE == 1) {
            if (searchType == 0) {
                baseFilterController.getShopData(this, initPage ? mPage = DEFAULT_PATE : mPage, PER_PAGE, baseFilterController.mFilterMap, this);
            } else if (searchType == 1) {
                baseFilterController.getCarData(this, initPage ? mPage = DEFAULT_PATE : mPage, PER_PAGE, baseFilterController.mFilterMap, this);
            }
        }
    }
}
