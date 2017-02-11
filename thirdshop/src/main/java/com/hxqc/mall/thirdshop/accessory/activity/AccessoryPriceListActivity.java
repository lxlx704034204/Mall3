package com.hxqc.mall.thirdshop.accessory.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.MapUtils;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.FilterFactorView;
import com.hxqc.mall.core.views.MyRecyclerViewOnScrollListener;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.adapter.AccessoryShopAdapter;
import com.hxqc.mall.thirdshop.accessory.control.FilterController;
import com.hxqc.mall.thirdshop.accessory.fragment.Filter.FilterBrandFragment;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryShop;
import com.hxqc.mall.thirdshop.accessory.model.Brand;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;
import com.hxqc.util.DebugLog;

import net.simonvt.menudrawer.OverlayDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Function: 网上4S店报价
 *
 * @author 袁秉勇
 * @since 2016年02月25日
 */
public class AccessoryPriceListActivity extends NoBackActivity implements View.OnClickListener, FilterBrandFragment.FilterBrandFragmentCallBack, FilterController.AccessoryShopHandler, OnRefreshHandler {
    private final static String TAG = AccessoryPriceListActivity.class.getSimpleName();
    public static String IMGURL = "imgUrl";
    public static String ACCESSORYNAME = "accessoryName";
    public static String BRAND = "brand";
    public static String PRODUCTBRANDID = "productBrandID";
    public static String PRODUCTBRANDNAME = "productBrandName";
    protected LatLng latLng = null;
    protected int PER_PAGE = 15;
    protected int mPage = 1;
    protected int DEFAULT_PATE = 1;
    /** 加载数据相关 **/
    protected boolean hasMore = false;
    protected String productBrandID;
    protected String productBrandName;
    private Context mContext;
    private Toolbar toolbar;
    private TextView mChangeCityView;
    private OverlayDrawer mOverlayDrawerView;
    private ImageView mAccessoryIMGView;
    private TextView mAccessoryNameView;
    private FilterFactorView mFilterBrandView;
    private RecyclerView recyclerView;
    private RequestFailView requestFailView;
    private MyRecyclerViewOnScrollListener myRecyclerViewOnScrollListener;
    private FilterController filterController;
    private BaseSharedPreferencesHelper baseSharedPreferencesHelper;
    private AccessoryShopAdapter accessoryShopAdapter;
    private FilterBrandFragment filterBrandFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_accessory_price);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        filterController = FilterController.getInstance();
        baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(this);
        myRecyclerViewOnScrollListener = new MyRecyclerViewOnScrollListener(this);

        mChangeCityView = (TextView) findViewById(R.id.change_city);
        mOverlayDrawerView = (OverlayDrawer) findViewById(R.id.drawer);
        mAccessoryIMGView = (ImageView) findViewById(R.id.accessory_img);
        mAccessoryNameView = (TextView) findViewById(R.id.accessory_name);
        mFilterBrandView = (FilterFactorView) findViewById(R.id.filter_brand);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setOnScrollListener(myRecyclerViewOnScrollListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);

        requestFailView = (RequestFailView) findViewById(R.id.request_view);

        initAdapter();

        setListener();
        initFragment();
        /** 获取历史定位城市数据并上传city */
        initLocationData();

        initView();
        initData();

        productBrandID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(PRODUCTBRANDID);
        productBrandName = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(PRODUCTBRANDNAME);
    }


    protected void initAdapter() {
        accessoryShopAdapter = new AccessoryShopAdapter(this) {
            @Override
            public AccessoryShopAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                final AccessoryShopAdapter.Holder holder = super.onCreateViewHolder(parent, viewType);

                holder.callView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AccessoryShop accessoryShop = getItemData(holder.getAdapterPosition());
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MaterialDialog);
                        builder.setTitle("拨打电话").setMessage(accessoryShop.shopPhone).setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                Uri uri = Uri.parse("tel:" + accessoryShop.shopPhone);
                                intent.setData(uri);
                                mContext.startActivity(intent);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
                    }
                });

                holder.buyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String bigName = "";
                        String smallName = "";
                        if (filterController.getAccessoryBigCategory() != null) {
                            bigName = TextUtils.isEmpty(filterController.getAccessoryBigCategory().class1stName) ? "" : filterController.getAccessoryBigCategory().class1stName;
                            if (filterController.getAccessoryBigCategory().class2nd != null && filterController.getAccessoryBigCategory().class2nd.size() > 0) {
                                smallName = TextUtils.isEmpty(filterController.getAccessoryBigCategory().class2nd.get(0).class2ndName) ? "" : filterController.getAccessoryBigCategory().class2nd.get(0).class2ndName;
                            }
                        }

                        AccessoryShop accessoryShop = getItemData(holder.getAdapterPosition());
//                        ActivitySwitcherAccessory.toShopDetails(mContext, filterController.mFilterMap, bigName, smallName, accessoryShop.shopID, productBrandID, productBrandName);
                        //ActivitySwitcherAccessory.toAccessorySaleActivity(mContext, "", "", filterController.mFilterMap.get("seriesID"), filterController.mFilterMap.get("brandID"), filterController.mFilterMap.get("class1stID"), bigName, filterController.mFilterMap.get("class2ndID"), smallName, accessoryShop.shopID, productBrandID, productBrandName);
                    }
                });

                holder.contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String bigName = "";
                        String smallName = "";
                        if (filterController.getAccessoryBigCategory() != null) {
                            bigName = TextUtils.isEmpty(filterController.getAccessoryBigCategory().class1stName) ? "" : filterController.getAccessoryBigCategory().class1stName;
                            if (filterController.getAccessoryBigCategory().class2nd != null && filterController.getAccessoryBigCategory().class2nd.size() > 0) {
                                smallName = TextUtils.isEmpty(filterController.getAccessoryBigCategory().class2nd.get(0).class2ndName) ? "" : filterController.getAccessoryBigCategory().class2nd.get(0).class2ndName;
                            }
                        }

                        AccessoryShop accessoryShop = getItemData(holder.getAdapterPosition());
//                        ActivitySwitcherAccessory.toShopDetails(mContext, filterController.mFilterMap, bigName, smallName, accessoryShop.shopID, productBrandID, productBrandName);
                        //ActivitySwitcherAccessory.toAccessorySaleActivity(mContext, "", "", filterController.mFilterMap.get("seriesID"), filterController.mFilterMap.get("brandID"), filterController.mFilterMap.get("class1stID"), bigName, filterController.mFilterMap.get("class2ndID"), smallName, accessoryShop.shopID, productBrandID, productBrandName);
                    }
                });
                return holder;
            }
        };
    }


    private void setListener() {
        mChangeCityView.setOnClickListener(this);
        mFilterBrandView.setOnClickListener(this);
    }


    private void initFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        filterBrandFragment = new FilterBrandFragment();
        filterBrandFragment.setFilterBrandFragmentCallBack(this);
        fragmentTransaction.add(R.id.mdMenu, filterBrandFragment);
        fragmentTransaction.commit();
    }


    private void initView() {
        ImageUtil.setImageSquare(this, mAccessoryIMGView, getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(AccessoryPriceListActivity.IMGURL));
        mAccessoryNameView.setText(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(AccessoryPriceListActivity.ACCESSORYNAME));
        Brand brand = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelable(AccessoryPriceListActivity.BRAND);
        if (brand != null) {
            mFilterBrandView.setTagString(brand.brandName, true);
            filterController.mFilterMap.put("brandID", brand.brandID);
        }
        echo(filterController.mFilterMap);
    }


    private void initData() {
        filterController.getAccessoryShopData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.filter_brand) {
            if (filterBrandFragment == null) initFragment();
            mOverlayDrawerView.openMenu();
        } else if (i == R.id.change_city) {
            ActivitySwitcherAccessory.toPositionActivity(this, 1, mChangeCityView.getText().toString());
        }
    }


    @Override
    public void onFilterBrandCallback(Brand brand) {
        mOverlayDrawerView.closeMenu();

        filterController.mFilterMap.remove("seriesID");
        if (brand == null) {
            if (filterController.mFilterMap.containsKey("brandID")) {
                filterController.mFilterMap.remove("brandID");
                filterController.getAccessoryShopData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
            }
            mFilterBrandView.setTagString("不限", false);
            filterController.setBrand(null);
        } else {
            if (TextUtils.isEmpty(brand.brandID)) {
                ToastHelper.showRedToast(this, "品牌名称不存在");
                mOverlayDrawerView.closeMenu();
                return;
            }
            mFilterBrandView.setTagString(brand.brandName, true);

            boolean repeat = brand.brandID.equals(filterController.mFilterMap.get("brandID"));

            filterController.mFilterMap.put("brandID", brand.brandID);

            if (!repeat) {
                filterController.getAccessoryShopData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
            }

            filterController.setBrand(brand);
        }
    }


    private ArrayList< AccessoryShop > constructData() {
        ArrayList< AccessoryShop > accessoryShopArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AccessoryShop accessoryShop = new AccessoryShop();
            accessoryShop.shopID = "shop1585067251822281";
            accessoryShop.shopPhoto = "null";
            accessoryShop.brandThumb = "null";
            accessoryShop.shopTitle = "这是第 " + i + " 个ShopTitle";
            accessoryShop.distance = i * 100.00 + "";
            accessoryShop.priceRange = (i * 100) + " — " + (i * 200);
            accessoryShopArrayList.add(accessoryShop);
        }

        return accessoryShopArrayList;
    }


    @Override
    public void onGetShopSucceed(ArrayList< AccessoryShop > accessoryShops) {
        if (accessoryShops == null) {
            if (mPage == DEFAULT_PATE) showEmptyOrFailView();
            return;
//            accessoryShops = constructData();
        }

        hasMore = accessoryShops.size() >= PER_PAGE;

        if (mPage == DEFAULT_PATE) {
            accessoryShopAdapter.list.clear();
            if (accessoryShops.size() > 0) {
                recyclerView.setAdapter(accessoryShopAdapter);
            } else {
                showEmptyOrFailView();
                return;
            }
        }
        if (requestFailView.getVisibility() == View.VISIBLE) requestFailView.setVisibility(View.GONE);
        accessoryShopAdapter.addData(accessoryShops);
    }


    @Override
    public void onGetShopFailed() {
        showEmptyOrFailView();
    }


    private void showEmptyOrFailView() {
        requestFailView.setEmptyDescription("未找到结果");
        requestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        requestFailView.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, " --------------> onStart");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, " --------------> onResume");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, " --------------> onStop");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, " --------------> onDestroy");
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, " --------------> onPause");
    }


    /**
     * 初始化当前坐标
     * final String latitu, final String longitu, final String c
     */
    private void initLocationData() {
        if (baseSharedPreferencesHelper == null) {
            baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(this);
        }
        String city = baseSharedPreferencesHelper.getCity();
        LinkedList< String > historyCityList = baseSharedPreferencesHelper.getHistoryCity();
        if (historyCityList.isEmpty()) {
            showLocationIcon(false);
            mChangeCityView.setText("全国");
            filterController.mFilterMap.put("area", "全国");
            startSettingDialog(baseSharedPreferencesHelper.getCity());
        } else {
            String historyCity = historyCityList.getFirst();
            if (!TextUtils.isEmpty(historyCity)) {
                mChangeCityView.setText(historyCity);
                if (!"全国".equals(historyCity)) filterController.mFilterMap.put("area", mChangeCityView.getText().toString());
            } else {
                mChangeCityView.setText("全国");
            }

            if (!city.equals(historyCity)) {
                showLocationIcon(false);
                startSettingDialog(city);
            } else {
                showLocationIcon(true);
                accessoryShopAdapter.setShowDistance(true);
            }
        }

        //只有定位成功后才上传地理坐标
//        if (!TextUtils.isEmpty(latitu) && !TextUtils.isEmpty(longitu)) {
        try {
            if (!TextUtils.isEmpty(baseSharedPreferencesHelper.getLatitude()) && !TextUtils.isEmpty(baseSharedPreferencesHelper.getLongitude())) latLng = MapUtils.bd_encrypt(Double.parseDouble(baseSharedPreferencesHelper.getLatitude()), Double.parseDouble(baseSharedPreferencesHelper.getLongitude()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (latLng != null) {
            filterController.mFilterMap.put("latitude", Double.toString(latLng.latitude));
            filterController.mFilterMap.put("longitude", Double.toString(latLng.longitude));
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
                setMessage("您当前城市是【" + city + "】,需要切换吗？")//您当前城市是【%@】，需要切换吗？
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLocationIcon(true);
                        mChangeCityView.setText(city);
                        filterController.mFilterMap.put("area", city);
                        if (accessoryShopAdapter != null) accessoryShopAdapter.setShowDistance(true);
                        initData();
                        baseSharedPreferencesHelper.setHistoryCity(city);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1) {
            String position;
            if (!TextUtils.isEmpty(position = data.getStringExtra("position"))) {
                if (position.equals(mChangeCityView.getText().toString())) return;

                mChangeCityView.setText(position);

                if (baseSharedPreferencesHelper.getCity().equals(position)) {
                    showLocationIcon(true);
                    if (accessoryShopAdapter != null) accessoryShopAdapter.setShowDistance(true);
//                    filterController.mFilterMap.remove("area");
                } else {
                    showLocationIcon(false);
                    if (accessoryShopAdapter != null) accessoryShopAdapter.setShowDistance(false);
                }
                initData();
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
        filterController.getAccessoryShopData(this, mPage, PER_PAGE, filterController.mFilterMap, this);
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


    public void onClickTest(View view) {
        DebugLog.e(TAG, "测试点击事件是否会穿透!");
    }
}
