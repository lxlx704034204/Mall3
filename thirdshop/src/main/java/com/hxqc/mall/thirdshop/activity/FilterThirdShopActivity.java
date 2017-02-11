package com.hxqc.mall.thirdshop.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import com.hxqc.mall.core.util.MapUtils;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.MyCoordinatorLayout;
import com.hxqc.mall.core.views.MyRecyclerViewOnScrollListener;
import com.hxqc.mall.core.views.MyUltraPullRefreshHeaderHelper;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.control.TFilterController;
import com.hxqc.mall.thirdshop.activity.auto.fragment.FilterThirdShopCoreFragment_2;
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
import java.util.LinkedList;
import java.util.Map;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Function:筛选界面第二版
 *
 * @author 袁秉勇
 * @since 2015年12月17日
 */
@Deprecated
public class FilterThirdShopActivity extends NoBackActivity implements FilterThirdShopCoreFragment_2.FilterMenuListener, FilterThirdShopCoreFragment_2.FilterThirdShopCoreFragmentCallBack,
//        AMapLocationControl.onCoreLocationListener,
        OnRefreshHandler, TFilterController.TCarHandler, TFilterController.TShopHandler, View.OnClickListener, MyCoordinatorLayout.CallBack {
    private final static String TAG = FilterThirdShopActivity.class.getSimpleName();
    private Context mContext;
    SharedPreferencesHelper sharedPreferencesHelper;

    static int RECENT_OPERATE_TYPE = 0;//最近一次操作 0为筛选，1为搜索

    public Fragment lastFragment = new Fragment();

    protected PtrFrameLayout mPtrFrameLayoutView;
    protected MyUltraPullRefreshHeaderHelper mPtrHelper;

    LatLng latLng = null;
    TextView mChangeCityView;//选择city
    TextView mChangeSearchTypeView;//选择搜索类型，店铺or车型
    EditText mSearchContentView;//搜索输入框
    LinearLayout mSearchButtonView;//搜索按钮
    //    AMapLocationControl locationControl;

    OverlayDrawer mOverlayDrawer;
    FilterThirdShopCoreFragment_2 filterThirdShopCoreFragment_2;
    FilterThirdShopCoreFragment_2 filterThirdShopCoreFragment_2_copy;
    TFilterController tFilterController;
    ArrayList< Fragment > fragments = new ArrayList<>();
    MyCoordinatorLayout myCoordinatorLayoutView;
    //    LinearLayout mCollapseView;
    LinearLayout mTipView;
    //    FrameLayout mTipFragmentView;
    RecyclerView mRecyclerView;
    RequestFailView mRequestFailView;

    TextView mTipCarNameView;
    String carName = "";
//    HashMap<String, String> searchHashMap = new HashMap<>();

    SearchCarBrandsAdapter searchCarBrandsAdapter;//车辆adapter

    SearchShopAdapter searchShopAdapter;//店铺adapter

    ThirdPartShopClient ThirdPartShopClient;
    //    CoordinatorLayout.LayoutParams recyclerViewLayoutParams;
//    AppBarLayout.ScrollingViewBehavior behavior;
    int searchType = 1;//请求数据接口 0为店铺, 1为车型
    int requestType = 0;//请求数据接口 0为店铺, 1为车型
    int PER_PAGE = 15;
    int mPage = 1;
    int DEFAULT_PATE = 1;
    /**
     * 加载数据相关
     */
    boolean hasMore = false;
    String brandName;
    String seriesName;
    Series series;
    String modelName;
    private MyRecyclerViewOnScrollListener myRecyclerViewOnScrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        tFilterController = TFilterController.getInstance();

        //城市选择View
        mChangeCityView = (TextView) findViewById(R.id.change_city);
//        mChangeCityView.setText("定位中");//默认显示定位中
        //从SharedPreferences中拿历史定位数据
//        initLocationData(null, null, null);

//        /**开启定位*/
//        locationControl = AMapLocationControl.getInstance(this);
//        locationControl.setCoreLocationListener(this);
//        locationControl.requestLocation();

        ThirdPartShopClient = new ThirdPartShopClient();
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
//        filterThirdShopCoreFragment_2 = (FilterThirdShopCoreFragment_2) getSupportFragmentManager().findFragmentById(R.id.third_filter_fragment);

        /** 将新车数据填入到筛选视图中 */
        if (!TextUtils.isEmpty(brandName) && null != series && !TextUtils.isEmpty(modelName)) {
            requestType = 1;
            tFilterController.mFilterMap.put("brand", brandName);
            tFilterController.mFilterMap.put("serie", series.seriesName);
            tFilterController.mFilterMap.put("model", modelName);

            tFilterController.setBrand(new Brand(brandName));
            tFilterController.setSeries(series);
            tFilterController.setCarType(new CarType(modelName));

//            filterThirdShopCoreFragment_2.initData(brandName, series, modelName);
            this.seriesName = brandName + " " + series.seriesName;
            carName = brandName + " " + series.seriesName + " " + modelName;
        }

//        filterThirdShopCoreFragment_2_copy = new FilterThirdShopCoreFragment_2();
//        filterThirdShopCoreFragment_2_copy.setFilterThirdShopCoreFragmentCallBack(this);
//        getSupportFragmentManager().beginTransaction().add(R.id.tip_fragment, filterThirdShopCoreFragment_2_copy)
// .commit();


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(null);
        mRequestFailView = (RequestFailView) findViewById(R.id.request_view);

//        recyclerViewLayoutParams = (CoordinatorLayout.LayoutParams) mRecyclerView.getLayoutParams();
//        behavior = (AppBarLayout.ScrollingViewBehavior) recyclerViewLayoutParams.getBehavior();

        /** 获取历史定位城市数据并上传city */
        initLocationData();

        /** 如果是从新车进入该页就车辆数据，否则搜索店铺数据 */
        if (!TextUtils.isEmpty(brandName) && null != series && !TextUtils.isEmpty(modelName)) {
            tFilterController.getCarData(this, mPage, PER_PAGE, 2, tFilterController.mFilterMap, this);
        } else {
            tFilterController.getShopData(this, mPage, PER_PAGE, 2, tFilterController.mFilterMap, this);
        }

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
                        ActivitySwitcherThirdPartShop.toCarDetail(itemData.autoInfo.itemID, itemData.shopInfo.shopID, itemData.shopInfo.shopTitle, FilterThirdShopActivity.this);
                    }
                });
                holder.car_dial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        com.hxqc.mall.core.util.OtherUtil.callPhone(FilterThirdShopActivity.this, getItemData(holder.getAdapterPosition()).shopInfo.shopTel);
                    }
                });
                holder.ask_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopSearchAuto itemData = getItemData(holder.getAdapterPosition());
                        ActivitySwitcherThirdPartShop.toAskLeastPrice(FilterThirdShopActivity.this, itemData.shopInfo.shopID, itemData.autoInfo.itemID, itemData.autoInfo.itemName, itemData.shopInfo.shopTel, true, null);
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
                        ActivitySwitcherThirdPartShop.toShopDetails(FilterThirdShopActivity.this, itemData.shopID);
                    }
                });
                return holder;
            }
        };
    }


    @Override
    public void onCallBack(String key, String value) {
        if (!TextUtils.isEmpty(value)) tFilterController.putValue(key, value);
        switch (key) {
            // 搜店铺
            case "brand":
                requestType = 0;
                RECENT_OPERATE_TYPE = 0;
                if (tFilterController.mFilterMap.containsKey("keyword")) tFilterController.mFilterMap.remove("keyword");
                echo(tFilterController.mFilterMap);
                if (TextUtils.isEmpty(value)) {
                    if (tFilterController.getBrand() != null) tFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                } else {
                    if (tFilterController.getBrand() == null) {
                        tFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                    } else if (tFilterController.getBrand() != null && !value.equals(tFilterController.getBrand().brandName)) {
                        tFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
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
                if (tFilterController.mFilterMap.containsKey("keyword")) tFilterController.mFilterMap.remove("keyword");
                echo(tFilterController.mFilterMap);
                if (TextUtils.isEmpty(value)) {
                    if (tFilterController.getSeries() != null) tFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                } else {
                    if (tFilterController.getSeries() == null) {
                        tFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                    } else if (tFilterController.getSeries() != null && !value.equals(tFilterController.getSeries().seriesName)) {
                        tFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
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
                if (tFilterController.mFilterMap.containsKey("keyword")) tFilterController.mFilterMap.remove("keyword");
                echo(tFilterController.mFilterMap);
                if (!TextUtils.isEmpty(value)) {
                    requestType = 1;
                    if (tFilterController.getCarType() == null) {
                        tFilterController.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                    } else if (tFilterController.getCarType() != null && !value.equals(tFilterController.getCarType().modelName)) {
                        tFilterController.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                    }
                } else {
                    requestType = 0;
                    if (tFilterController.getCarType() != null) tFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
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
                echo(tFilterController.mFilterMap);
//                echo(searchHashMap);
//                if (RECENT_OPERATE_TYPE == 0) {
                if (requestType == 0) {
                    tFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                } else if (requestType == 1) {
                    tFilterController.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                }
//                } else if (RECENT_OPERATE_TYPE == 1) {
//                    if (searchType == 0) {
//                        tFilterController_2.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, searchHashMap, this);
//                    } else if (searchType == 1) {
//                        tFilterController_2.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, searchHashMap, this);
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
        tFilterController.destroy();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        tFilterController.destroy();
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
        if (RECENT_OPERATE_TYPE == 0) {
            if (requestType == 0) {
                tFilterController.getShopData(this, mPage, PER_PAGE, 2, tFilterController.mFilterMap, this);
            } else if (requestType == 1) {
                tFilterController.getCarData(this, mPage, PER_PAGE, 2, tFilterController.mFilterMap, this);
            }
        } else if (RECENT_OPERATE_TYPE == 1) {
            if (searchType == 0) {
                tFilterController.getShopData(this, mPage, PER_PAGE, 2, tFilterController.mFilterMap, this);
            } else if (searchType == 1) {
                tFilterController.getCarData(this, mPage, PER_PAGE, 2, tFilterController.mFilterMap, this);
            }
        }
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
//            filterThirdShopCoreFragment_2.mCityView.performClick();
            ActivitySwitcherThirdPartShop.toPositionActivity(this, 1, ((TextView) v).getText().toString());

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
            RECENT_OPERATE_TYPE = 1;//最近一次操作改为搜索
            mTipCarNameView.setText(mSearchContentView.getText());
//                searchHashMap.put("keyword", mSearchContentView.getText().toString().trim());
//                echo(searchHashMap);
            tFilterController.mFilterMap.put("keyword", mSearchContentView.getText().toString().trim());
            echo(tFilterController.mFilterMap);
            switch (searchType) {
                case 0:
//                        tFilterController_2.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, searchHashMap, this);
                    tFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                    break;

                case 1:
//                        tFilterController_2.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, searchHashMap, this);
                    tFilterController.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                    break;
            }
        }
    }

//    /**
//     * 获取当前地理位置
//     *
//     * @param aMapLocation
//     */
//    @Override
//    public void onLocationChange(AMapLocation aMapLocation) {
//        double latitude = aMapLocation.getLatitude();//经度
//        double longitude = aMapLocation.getLongitude();//维度
//        String province = aMapLocation.getProvince();
//        String city = aMapLocation.getCity();
//        if (!TextUtils.isEmpty(city)) {//如果没有定位图标加载定位图标
//            mChangeCityView.setText(city);
//            if (mChangeCityView.getCompoundDrawables().length == 0) {
//                mChangeCityView.setCompoundDrawables(getResources().getDrawable(R.drawable.t_icon_button_location),
// null, null, null);
//            }
//        }
//        initLocationData(latitude + "", longitude + "", city);
//        if (sharedPreferencesHelper == null) sharedPreferencesHelper = new SharedPreferencesHelper(this);
////        sharedPreferencesHelper.setLatitude(latitude + "");
////        sharedPreferencesHelper.setLongitude(longitude + "");
//        sharedPreferencesHelper.setCity(city);
//        sharedPreferencesHelper.setProvince(province);
//        locationControl.onDestroy();
//    }


    /**
     * 初始化当前坐标
     * final String latitu, final String longitu, final String c
     */
    private void initLocationData() {
        if (sharedPreferencesHelper == null) {
            sharedPreferencesHelper = new SharedPreferencesHelper(this);
        }
        String city = sharedPreferencesHelper.getCity();
        LinkedList< String > historyCityList = sharedPreferencesHelper.getHistoryCity();
        if (historyCityList.isEmpty()) {
            showLocationIcon(false);
            mChangeCityView.setText("全国");
            tFilterController.mFilterMap.put("area", "全国");
            startSettingDialog(sharedPreferencesHelper.getCity());
        } else {
            String historyCity = historyCityList.getFirst();
            if (!TextUtils.isEmpty(historyCity)) {
                mChangeCityView.setText(historyCity);
            } else {
                mChangeCityView.setText("全国");
            }
            tFilterController.mFilterMap.put("area", mChangeCityView.getText().toString());
            if (!city.equals(historyCity)) {
                showLocationIcon(false);
                startSettingDialog(city);
            } else {
                showLocationIcon(true);
                if (searchShopAdapter != null) searchShopAdapter.setShowDistance(true);
                tFilterController.mFilterMap.remove("area");
            }
        }

        //只有定位成功后才上传地理坐标
//        if (!TextUtils.isEmpty(latitu) && !TextUtils.isEmpty(longitu)) {
        try {
            if (!TextUtils.isEmpty(sharedPreferencesHelper.getLatitude()) && !TextUtils.isEmpty(sharedPreferencesHelper.getLongitude())) latLng = MapUtils.bd_encrypt(Double.parseDouble(sharedPreferencesHelper.getLatitude()), Double.parseDouble(sharedPreferencesHelper.getLongitude()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (latLng != null) {
            tFilterController.mFilterMap.put("latitude", Double.toString(latLng.latitude));
            tFilterController.mFilterMap.put("longitude", Double.toString(latLng.longitude));
//            searchHashMap.put("latitude", Double.toString(latLng.latitude));
//            searchHashMap.put("longitude", Double.toString(latLng.longitude));
//                tFilterController_2.mFilterMap.put("latitude", Double.toString(latLng.latitude));
//                tFilterController_2.mFilterMap.put("longitude", Double.toString(latLng.longitude));
        }
    }

    //来自定位后的执行代码
//        if (!TextUtils.isEmpty(c) && !city.equals(c)) {
//            InfoSubmitSuccessConfirmDialog infoSubmitSuccessConfirmDialog = new InfoSubmitSuccessConfirmDialog
// (this, "提示：", "定位成功，是否切换到当前定位城市，并重新获取数据？", new InfoSubmitSuccessConfirmDialog.ConfirmListener() {
//                @Override
//                public void confirm() {
    //应要求,不再从SP文件中获取上次定位的位置
//                        if (!TextUtils.isEmpty(c)) {
//                            mChangeCityView.setText(c);
//                              mChangeCityView.setCompoundDrawables(null, null, null, null);//手动选择地区,不要定位的图标
//                        }

//                    if (!TextUtils.isEmpty(c)) {
//                        tFilterController_2.mFilterMap.put("area", c);
//                        searchHashMap.put("area", c);
//                    }

//                        if (latLng != null) {
//                            sharedPreferencesHelper.setLatitude(latitu + "");
//                            sharedPreferencesHelper.setLongitude(longitu + "");
//                            tFilterController_2.mFilterMap.put("latitude", Double.toString(latLng.latitude));
//                            tFilterController_2.mFilterMap.put("longitude", Double.toString(latLng.longitude));
//                            searchHashMap.put("latitude", Double.toString(latLng.latitude));
//                            searchHashMap.put("longitude", Double.toString(latLng.longitude));
//                        }

    //定位成功后重新刷新当前数据
//                    if (RECENT_OPERATE_TYPE == 0) {
//                        if (requestType == 0) {
//                            tFilterController_2.getShopData(FilterThirdShopActivity.this, mPage = DEFAULT_PATE,
// PER_PAGE, 2, tFilterController_2.mFilterMap, FilterThirdShopActivity.this);
//                        } else if (requestType == 1) {
//                            tFilterController_2.getCarData(FilterThirdShopActivity.this, mPage = DEFAULT_PATE,
// PER_PAGE, 2, tFilterController_2.mFilterMap, FilterThirdShopActivity.this);
//                        }
//                    } else if (RECENT_OPERATE_TYPE == 1) {
//                        if (searchType == 0) {
//                            tFilterController_2.getShopData(FilterThirdShopActivity.this, mPage = DEFAULT_PATE,
// PER_PAGE, 2, searchHashMap, FilterThirdShopActivity.this);
//                        } else if (searchType == 1) {
//                            tFilterController_2.getCarData(FilterThirdShopActivity.this, mPage = DEFAULT_PATE,
// PER_PAGE, 2, searchHashMap, FilterThirdShopActivity.this);
//                        }
//                    }
//                }
//            });
//            infoSubmitSuccessConfirmDialog.mOffView.setVisibility(View.VISIBLE);
//            infoSubmitSuccessConfirmDialog.show();
//        }
//    }


    /**
     * @param city 当前城市
     * @return
     */
    private void startSettingDialog(final String city) {
//        android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar

        if (TextUtils.isEmpty(city) || sharedPreferencesHelper.getPositionTranslate()) {
            return;
        } else {
            sharedPreferencesHelper.setPositionTranslate(true);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialog);
        builder.setCancelable(false);
        builder.setTitle("提示").
                setMessage("您当前城市是【" + city + "】,需要切换吗？")//您当前城市是【%@】，需要切换吗？
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLocationIcon(true);
                        mChangeCityView.setText(city);
//                                tFilterController_2.mFilterMap.put("area", city);
//                                searchHashMap.put("area", city);
                        tFilterController.mFilterMap.remove("area");
                        if (searchShopAdapter != null) searchShopAdapter.setShowDistance(true);
//
//                                /** 如果是从新车进入该页就车辆数据，否则搜索店铺数据 */
                        if (!TextUtils.isEmpty(brandName) && null != series && !TextUtils.isEmpty(modelName)) {
                            tFilterController.getCarData(FilterThirdShopActivity.this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, FilterThirdShopActivity.this);
                        } else {
                            tFilterController.getShopData(FilterThirdShopActivity.this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, FilterThirdShopActivity.this);
                        }
////                                tFilterController_2.getShopData(FilterThirdShopActivity.this, mPage =
// DEFAULT_PATE, PER_PAGE, 2, tFilterController_2.mFilterMap, FilterThirdShopActivity.this);
                        sharedPreferencesHelper.setHistoryCity(city);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        echo(tFilterController.mFilterMap);

        if (requestCode == 1 && resultCode == 1) {
            String position;
            if (!TextUtils.isEmpty(position = data.getStringExtra("position"))) {
                if (position.equals(mChangeCityView.getText().toString())) {
                    return;
                } else {
                    mChangeCityView.setText(position);
                }

                if (position.equals(sharedPreferencesHelper.getCity())) {
                    showLocationIcon(true);
                    if (searchShopAdapter != null) searchShopAdapter.setShowDistance(true);
                    tFilterController.mFilterMap.remove("area");
                } else {
                    showLocationIcon(false);
                    if (searchShopAdapter != null) searchShopAdapter.setShowDistance(false);
                }

                if (RECENT_OPERATE_TYPE == 0) {
                    if (requestType == 0) {
                        tFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                    } else if (requestType == 1) {
                        tFilterController.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                    }
                } else {
                    if (searchType == 0) {
                        tFilterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                    } else if (searchType == 1) {
                        tFilterController.getCarData(this, mPage = DEFAULT_PATE, PER_PAGE, 2, tFilterController.mFilterMap, this);
                    }
                }
            }
        }
    }


    public void showLocationIcon(boolean flag) {
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
}
