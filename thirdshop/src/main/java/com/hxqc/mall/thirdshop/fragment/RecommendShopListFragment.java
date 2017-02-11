package com.hxqc.mall.thirdshop.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.MyCoordinatorLayout;
import com.hxqc.mall.core.views.MyRecyclerViewOnScrollListener;
import com.hxqc.mall.core.views.MyUltraPullRefreshHeaderHelper;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.auto.activity.ControllerConstruct;
import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
import com.hxqc.mall.thirdshop.activity.auto.control.FilterControllerForSpecialCar;
import com.hxqc.mall.thirdshop.activity.auto.fragment.FilterThirdShopCoreFragment_2;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.Brand;
import com.hxqc.mall.thirdshop.model.CarType;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.model.ShopSearchAuto;
import com.hxqc.mall.thirdshop.model.ShopSearchShop;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
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

import static com.hxqc.mall.thirdshop.utils.HomeSiteDataUtil.appendWordToStr;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年11月21日
 */
public class RecommendShopListFragment extends FunctionFragment implements ControllerConstruct, FilterThirdShopCoreFragment_2.FilterMenuListener, FilterThirdShopCoreFragment_2.FilterThirdShopCoreFragmentCallBack, OnRefreshHandler, BaseFilterController.TCarHandler, BaseFilterController.TShopHandler, View.OnClickListener, MyCoordinatorLayout.CallBack {
    private final static String TAG = RecommendShopListFragment.class.getSimpleName();
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


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_filter_core_view, null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);

        ThirdPartShopClient = new ThirdPartShopClient();
        initSharedPreferences();
        initController();

        view.findViewById(R.id.toolbar).setVisibility(View.GONE);
        //城市选择View
        mChangeCityView = (TextView) view.findViewById(R.id.change_city);

        initAdapter();

        mOverlayDrawer = (OverlayDrawer) view.findViewById(R.id.drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
        mOverlayDrawer.setSidewardCloseMenu(true);

        mPtrFrameLayoutView = (PtrFrameLayout) view.findViewById(R.id.refresh_frame);
        myRecyclerViewOnScrollListener = new MyRecyclerViewOnScrollListener(this);
        mPtrHelper = new MyUltraPullRefreshHeaderHelper(mContext, mPtrFrameLayoutView, myRecyclerViewOnScrollListener);
        mPtrHelper.setOnRefreshHandler(this);

        /**搜索相关的view*/
        mChangeSearchTypeView = (TextView) view.findViewById(R.id.change_search_type);
        mSearchContentView = (EditText) view.findViewById(R.id.search_tip);
        mSearchButtonView = (LinearLayout) view.findViewById(R.id.search);

        myCoordinatorLayoutView = (MyCoordinatorLayout) view.findViewById(R.id.coordinatorlayout);
        myCoordinatorLayoutView.setCallBack(this);
        myCoordinatorLayoutView.setInterceptMove(true);
//        mCollapseView = (LinearLayout) view.findViewById(R.id.collapse_view);
        mTipView = (LinearLayout) view.findViewById(R.id.tip_view);
        mTipCarNameView = (TextView) view.findViewById(R.id.tip_car_name);
//        mTipFragmentView = (FrameLayout) view.findViewById(R.id.tip_fragment);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        filterThirdShopCoreFragment_2 = new FilterThirdShopCoreFragment_2();
        filterThirdShopCoreFragment_2.setFilterMenuListener(this);
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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(null);
        mRequestFailView = (RequestFailView) view.findViewById(R.id.request_view);

        /** 获取历史定位城市数据并上传city */
        initLocationData();

        getData(true);

        setListener();
    }


    public void setListener() {
        mChangeCityView.setOnClickListener(this);
        mChangeSearchTypeView.setOnClickListener(this);
        mSearchButtonView.setOnClickListener(this);
    }


    @Override
    public void onStart() {
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
                        ActivitySwitcherThirdPartShop.toCarDetail(itemData.autoInfo.itemID, itemData.shopInfo.shopID, itemData.shopInfo.shopTitle, mContext);
                    }
                });
                holder.car_dial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OtherUtil.callPhone(mContext, getItemData(holder.getAdapterPosition()).shopInfo.shopTel);
                    }
                });
                holder.ask_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopSearchAuto itemData = getItemData(holder.getAdapterPosition());
                        ActivitySwitcherThirdPartShop.toAskLeastPrice(mContext, itemData.shopInfo.shopID, itemData.autoInfo.itemID, itemData.autoInfo.itemName, itemData.shopInfo.shopTel, true, null);
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
                        ActivitySwitcherThirdPartShop.toShopDetails(mContext, itemData.shopID);
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
                        getData(true);
                    }
                } else {
                    if (baseFilterController.getBrand() == null) {
                        getData(true);
                    } else if (baseFilterController.getBrand() != null && !value.equals(baseFilterController.getBrand().brandName)) {
                        getData(true);
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
                    } else if (baseFilterController.getCarType() != null && !value.equals(baseFilterController.getCarType().modelName)) {
                        getData(true);
                    }
                } else {
                    requestType = 0;
                    if (baseFilterController.getCarType() != null) getData(true);
                }
                if (!TextUtils.isEmpty(value)) carName = seriesName + " " + value;
                break;

            case "area":
                if (!TextUtils.isEmpty(value)) {
                    mChangeCityView.setText(value);
                } else {
                    mChangeCityView.setText("全国");
                }
                mChangeCityView.setCompoundDrawables(null, null, null, null); // 手动选择地区,不要定位的图标
                if (!TextUtils.isEmpty(value)) {
//                    searchHashMap.put("area", value);
                } else {
//                    searchHashMap.remove("area");
                }

                echo(baseFilterController.mFilterMap);

                getData(true);
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
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
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
//        if (i == R.id.change_city) {
//            if (this.getClass() == ThirdShopFilterForNewAuto.class) {
//                ActivitySwitcherThirdPartShop.toPositionActivity(this, 1, ((TextView) v).getText().toString());
//            } else {
//                ActivitySwitcherThirdPartShop.toSpecialCarChoosePositon(this, 1, ((TextView) v).getText().toString());
//            }
//        } else

        if (i == R.id.change_search_type) {  /** 切换搜索类型，0为店铺，1为车型 */
            DebugLog.e(getClass().getName(), mChangeSearchTypeView.getWidth() + " " + mChangeSearchTypeView.getHeight());

            View rootView = View.inflate(mContext, R.layout.third_filter_type_popup, null);
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
                ToastHelper.showRedToast(mContext, "请输入搜索条件");
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


//    protected void showLocationIcon(boolean flag) {
//        if (mChangeCityView == null) return;
//        if (flag) {
//            if ((mChangeCityView.getCompoundDrawables())[0] == null) mChangeCityView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.t_icon_button_location), null, null, null);
//        } else {
//            if (mChangeCityView.getCompoundDrawables().length > 0) mChangeCityView.setCompoundDrawables(null, null, null, null);
//        }
//    }


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
                baseFilterController.getShopData(mContext, initPage ? mPage = DEFAULT_PATE : mPage, PER_PAGE, baseFilterController.mFilterMap, this);
            } else if (requestType == 1) {
                baseFilterController.getCarData(mContext, initPage ? mPage = DEFAULT_PATE : mPage, PER_PAGE, baseFilterController.mFilterMap, this);
            }
        } else if (RECENT_OPERATE_TYPE == 1) {
            if (searchType == 0) {
                baseFilterController.getShopData(mContext, initPage ? mPage = DEFAULT_PATE : mPage, PER_PAGE, baseFilterController.mFilterMap, this);
            } else if (searchType == 1) {
                baseFilterController.getCarData(mContext, initPage ? mPage = DEFAULT_PATE : mPage, PER_PAGE, baseFilterController.mFilterMap, this);
            }
        }
    }


    @Override
    public String fragmentDescription() {
        return "推荐店铺Fragment";
    }


    private String cityGroupID; // 分站站点的ID
    private String areaGroup; // 分站站点名称

    private AreaSiteUtil areaSiteUtil;


    @Override
    public void initController() {
        baseFilterController = FilterControllerForSpecialCar.getInstance();
    }


    @Override
    public BaseFilterController getController() {
        return baseFilterController;
    }


    @Override
    public void destroyController() {
        if (baseFilterController != null) baseFilterController.destroy();
    }


    protected void initSharedPreferences() {
        sharedPreferencesHelper = new SharedPreferencesHelper(mContext);
        areaSiteUtil = AreaSiteUtil.getInstance(mContext);

        if (sharedPreferencesHelper.getHistoryCityForSpecialCar().size() > 0 && !TextUtils.isEmpty(sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst())) {
            cityGroupID = areaSiteUtil.getCityGroupID(sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst());
        } else {
            cityGroupID = sharedPreferencesHelper.getDefaultSiteData().siteGroupID;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        areaSiteUtil.destroy();

        RECENT_OPERATE_TYPE = 0;
        baseFilterController.destroy();
        destroyController();
    }


    /**
     * 初始化当前坐标
     * final String latitu, final String longitu, final String c
     */
    protected void initLocationData() {
        String historyCity;
        String city = sharedPreferencesHelper.getCity();
        LinkedList< String > historyCityList = sharedPreferencesHelper.getHistoryCityForSpecialCar();
        if (!historyCityList.isEmpty() && !TextUtils.isEmpty(historyCity = historyCityList.getFirst())) {
            mChangeCityView.setText(historyCity);
            cityGroupID = areaSiteUtil.getCityGroupID(historyCity);
            baseFilterController.mFilterMap.put("siteID", cityGroupID);
            if (!areaSiteUtil.getCityGroup(city).equals(historyCity)) {
                startSettingDialog(city);
            }
            return;
        }

        mChangeCityView.setText(areaGroup = areaSiteUtil.getWHSiteName());
        sharedPreferencesHelper.setHistoryCityForSpecialCar(areaGroup);
        baseFilterController.mFilterMap.put("siteID", cityGroupID);

        if (areaSiteUtil.getCityGroup(city).endsWith(areaSiteUtil.getWHSiteName())) {
            sharedPreferencesHelper.setCityForSpecialCar(city);
            sharedPreferencesHelper.setHistoryProvinceForSpecialCar(sharedPreferencesHelper.getProvince());
        } else {
            startSettingDialog(city);
        }
    }


    /**
     * @param city 当前城市
     * @return
     */
    private void startSettingDialog(final String city) {
        if (TextUtils.isEmpty(city) || sharedPreferencesHelper.getPositionTranslateForSiteGroup()) {
            return;
        } else {
            sharedPreferencesHelper.setPositionTranslateForSiteGroup(true);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MaterialDialog);
        builder.setCancelable(false);
        builder.setTitle("提示");
        if (areaSiteUtil.adjustCity(city)) {
            builder.setMessage("您当前城市属于【" + (appendWordToStr(areaSiteUtil.getCityGroup(city))) + "】" + ",是否需要进行数据切换?"); // 您当前城市是【%@】，需要切换吗？
        } else {
            builder.setMessage("您当前城市是【" + city + "】,不在分站列表中，已为您切换到【" + (appendWordToStr(areaSiteUtil.getWHSiteName())) + "】"); // 您当前城市是【%@】，需要切换吗？
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (areaSiteUtil.adjustCity(city)) {
                    areaGroup = areaSiteUtil.getCityGroup(city);
                    sharedPreferencesHelper.setCityForSpecialCar(city);
                    sharedPreferencesHelper.setHistoryProvinceForSpecialCar(areaSiteUtil.getCityProvince(city));
                } else {
                    areaGroup = areaSiteUtil.getWHSiteName();
                    sharedPreferencesHelper.setCityForSpecialCar(sharedPreferencesHelper.getDefaultSiteData().siteAreaName);
                    sharedPreferencesHelper.setHistoryProvinceForSpecialCar(areaSiteUtil.getCityProvince("湖北省"));
                }
                cityGroupID = areaSiteUtil.getCityGroupID(areaGroup);
                baseFilterController.mFilterMap.put("siteID", cityGroupID);
                mChangeCityView.setText(areaGroup);
                sharedPreferencesHelper.setHistoryCityForSpecialCar(areaGroup);
                getData(true);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).create().show();
    }
}
