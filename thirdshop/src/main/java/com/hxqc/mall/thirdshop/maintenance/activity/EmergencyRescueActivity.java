package com.hxqc.mall.thirdshop.maintenance.activity;

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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.AMapLocationControl;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.MapUtils;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.FilterFactorView;
import com.hxqc.mall.core.views.MyRecyclerViewOnScrollListener;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.EmergencyRescueAdapter;
import com.hxqc.mall.thirdshop.maintenance.api.EmergencyRescueApi;
import com.hxqc.mall.thirdshop.maintenance.control.FilterController;
import com.hxqc.mall.thirdshop.maintenance.fragment.Filter.FilterBrandFragment;
import com.hxqc.mall.thirdshop.maintenance.model.Brand;
import com.hxqc.mall.thirdshop.maintenance.model.MaintenancePriceOrTitle;
import com.hxqc.mall.thirdshop.maintenance.model.MaintenanceShop;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.util.DebugLog;

import net.simonvt.menudrawer.OverlayDrawer;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Function: 紧急救援Activity
 *
 * @author 袁秉勇
 * @since 2016年02月25日
 */
public class EmergencyRescueActivity extends NoBackActivity implements View.OnClickListener, FilterBrandFragment.FilterBrandFragmentCallBack, FilterController.RescueHandler, OnRefreshHandler, AMapLocationControl.onCoreLocationListener, AMapLocationControl.OnGetLocationFailCallBack {
    private final static String TAG = EmergencyRescueActivity.class.getSimpleName();
    private Context mContext;

    protected LatLng latLng = null;
    protected int PER_PAGE = 20;
    protected int mPage = 1;
    protected int DEFAULT_PATE = 1;
    /** 加载数据相关 **/
    protected boolean hasMore = false;

    private Toolbar toolbar;
    private TextView mChangeCityView;
    private TextView mAddressView;
    private OverlayDrawer mOverlayDrawerView;
    private FilterFactorView mFilterBrandView;
    private RecyclerView recyclerView;
    private RequestFailView requestFailView;
    private MyRecyclerViewOnScrollListener myRecyclerViewOnScrollListener;
    private FilterController filterController;
    private BaseSharedPreferencesHelper baseSharedPreferencesHelper;
    private EmergencyRescueAdapter emergencyRescueAdapter;

    private FilterBrandFragment filterBrandFragment;

    private AMapLocationControl aMapLocationControl;

    private EmergencyRescueApi emergencyRescueApi;

    private AMapLocation aMapLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_rescue);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAddressView = (TextView) findViewById(R.id.location_address);

        /** 当前位置定位 **/
        aMapLocationControl = AMapLocationControl.getInstance().setUpLocation(this);
        aMapLocationControl.setCoreLocationListener(this);
        aMapLocationControl.setOnGetLocationFailCallBack(this);
        aMapLocationControl.requestLocation();

        filterController = FilterController.getInstance();
        baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(this);
        myRecyclerViewOnScrollListener = new MyRecyclerViewOnScrollListener(this);

        mChangeCityView = (TextView) findViewById(R.id.change_city);
        mOverlayDrawerView = (OverlayDrawer) findViewById(R.id.drawer);
        mFilterBrandView = (FilterFactorView) findViewById(R.id.filter_brand);
        if (mFilterBrandView.getChildAt(0) instanceof RelativeLayout) {
            mFilterBrandView.getChildAt(0).getLayoutParams().height = (int) OtherUtil.dp2px(45, getResources().getDisplayMetrics());
            mFilterBrandView.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            mFilterBrandView.getChildAt(0).requestLayout();
//            mFilterBrandView.getChildAt(0).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) OtherUtil.dp2px(45, getResources().getDisplayMetrics())));
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setOnScrollListener(myRecyclerViewOnScrollListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        MyItemDecoration myItemDecoration = new MyItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        recyclerView.setItemAnimator(null);

        requestFailView = (RequestFailView) findViewById(R.id.request_view);

        emergencyRescueAdapter = new EmergencyRescueAdapter(this);

        setListener();
        initFragment();
        /** 获取历史定位城市数据并上传city */
        initLocationData();

        initData();
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


    private void initData() {
        filterController.getRescueData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.filter_brand) {
            if (filterBrandFragment == null) initFragment();
            mOverlayDrawerView.openMenu();
        } else if (i == R.id.change_city) {
            ActivitySwitcherMaintenance.toPositionActivity(this, 1, mChangeCityView.getText().toString());
        }
    }


    @Override
    public void onFilterBrandCallback(Brand brand) {
        mOverlayDrawerView.closeMenu();

        if (brand == null) {    // 点击"不限"时
            if (filterController.mFilterMap.containsKey("brand")) {
                filterController.mFilterMap.remove("brand");
                filterController.getRescueData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
                mFilterBrandView.setTagString("不限", false);
                filterController.setBrand(null);
            }
        } else {
            if (brand.brandName == null) {
                ToastHelper.showRedToast(this, "该品牌不存在");
                mOverlayDrawerView.closeMenu();
                return;
            }
            mFilterBrandView.setTagString(brand.brandName, true);

            boolean repeat = brand.brandName.equals(filterController.mFilterMap.get("brand"));


            if (!repeat) {
                filterController.mFilterMap.put("brand", brand.brandName);

                filterController.getRescueData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
            }

            filterController.setBrand(brand);
        }
    }


    private void showEmptyOrFailView() {
        requestFailView.setEmptyDescription("未找到结果");
        requestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        requestFailView.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterController.destroy();
        if (aMapLocationControl != null) {
            aMapLocationControl.unregistListener();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
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
            startSettingDialog(city);
        } else {
            String historyCity = historyCityList.getFirst();
//            searchHashMap.put("area", historyCity);
            if (!TextUtils.isEmpty(historyCity)) {
                mChangeCityView.setText(historyCity);
            } else {
                mChangeCityView.setText("全国");
            }
            filterController.mFilterMap.put("area", mChangeCityView.getText().toString());
            if (!city.equals(historyCity)) {
                showLocationIcon(false);
                startSettingDialog(city);
            } else {
                showLocationIcon(true);
                emergencyRescueAdapter.setShowDistance(true);
            }
        }

        //只有定位成功后才上传地理坐标
//        if (!TextUtils.isEmpty(latitu) && !TextUtils.isEmpty(longitu)) {
        try {
            if (!TextUtils.isEmpty(baseSharedPreferencesHelper.getLongitudeBD()) && !TextUtils.isEmpty(baseSharedPreferencesHelper.getLatitudeBD())) latLng = new LatLng(Double.valueOf(baseSharedPreferencesHelper.getLatitudeBD()), Double.valueOf(baseSharedPreferencesHelper.getLongitudeBD()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (latLng != null) {
            filterController.mFilterMap.put("latitude", Double.toString(latLng.latitude));
            filterController.mFilterMap.put("longitude", Double.toString(latLng.longitude));
//            searchHashMap.put("latitude", Double.toString(latLng.latitude));
//            searchHashMap.put("longitude", Double.toString(latLng.longitude));
//                tFilterController.mFilterMap.put("latitude", Double.toString(latLng.latitude));
//                tFilterController.mFilterMap.put("longitude", Double.toString(latLng.longitude));
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
                setMessage("您当前所在城市是【" + city + "】,是否需要进行切换？")//您当前城市是【%@】，需要切换吗？
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLocationIcon(true);
                        mChangeCityView.setText(city);
                        filterController.mFilterMap.put("area", city);
//                        filterController.mFilterMap.remove("area");
                        if (emergencyRescueAdapter != null) emergencyRescueAdapter.setShowDistance(true);
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
                if (position.equals(mChangeCityView.getText().toString())) {
                    return;
                } else {
                    mChangeCityView.setText(position);
                }

                if (position.equals(baseSharedPreferencesHelper.getCity())) {
                    showLocationIcon(true);
                    if (emergencyRescueAdapter != null) emergencyRescueAdapter.setShowDistance(true);
//                filterController.mFilterMap.remove("area");
                } else {
                    showLocationIcon(false);
                    if (emergencyRescueAdapter != null) {
                        emergencyRescueAdapter.setShowDistance(false);
                    }
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
        filterController.getRescueData(this, mPage, PER_PAGE, filterController.mFilterMap, this);
    }


    @Override
    public void onLocationChange(AMapLocation aMapLocation) {
//        mAddressView.setTextColor(getResources().getColor(R.color.black));
        this.aMapLocation = aMapLocation;
        if (baseSharedPreferencesHelper == null) {
            baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(this);
        }

        if (aMapLocation != null) {
            DebugLog.e("Location", aMapLocation.getErrorCode() + "  " + aMapLocation.toString());
            double latitude = aMapLocation.getLatitude();
            double longitude = aMapLocation.getLongitude();
            baseSharedPreferencesHelper.setLatitude(latitude + "");
            baseSharedPreferencesHelper.setLongitude(longitude + "");
            LatLng latLng = MapUtils.bd_encrypt(latitude, longitude);
            baseSharedPreferencesHelper.setLatitudeBD(latLng.latitude + "");
            baseSharedPreferencesHelper.setLongitudeBD(latLng.longitude + "");
        } else {
            DebugLog.e("Location", "定位失败");
        }
        if (mAddressView != null) mAddressView.setText(aMapLocation.getAddress());
    }


    private ArrayList< MaintenanceShop > constructData() {
        ArrayList< MaintenanceShop > maintenanceShopArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MaintenanceShop maintenanceShop = new MaintenanceShop();
            maintenanceShop.shopID = "shop1585067251822281";
            maintenanceShop.shopPhoto = "null";
            maintenanceShop.brandThumb = "null";
            maintenanceShop.shopTitle = "这是第 " + i + " 个ShopTitle";
            maintenanceShop.promotionTitle = "这是第 " + i + " 个PromotionTitle";
            maintenanceShop.distance = i * 100.00;
            maintenanceShop.maintenanceList = new ArrayList<>();
            maintenanceShop.promotionList = new ArrayList<>();

            for (int j = 0; j < 2; j++) {
                MaintenancePriceOrTitle maintenancePriceOrTitle = new MaintenancePriceOrTitle();
                maintenancePriceOrTitle.price = i * 10000.00;
                maintenancePriceOrTitle.title = "这是第 " + j + " 个Title";
                maintenanceShop.maintenanceList.add(maintenancePriceOrTitle);
            }
            for (int j = 0; j < 2; j++) {
                MaintenancePriceOrTitle maintenancePriceOrTitle = new MaintenancePriceOrTitle();
                maintenancePriceOrTitle.price = i * 10000.00;
                maintenancePriceOrTitle.title = "这是第 " + j + " 个Title";
                maintenanceShop.promotionList.add(maintenancePriceOrTitle);
            }
            maintenanceShopArrayList.add(maintenanceShop);
        }

        return maintenanceShopArrayList;
    }


    @Override
    public void onGetPriceSucceed(ArrayList< MaintenanceShop > maintenanceShops) {
        if (maintenanceShops == null) {
            if (mPage == DEFAULT_PATE) showEmptyOrFailView();
            return;
//            maintenanceShops = constructData();
        }

        hasMore = maintenanceShops.size() >= PER_PAGE;

        if (mPage == DEFAULT_PATE) {
            emergencyRescueAdapter.list.clear();
            if (maintenanceShops.size() > 0) {
                recyclerView.setAdapter(emergencyRescueAdapter);
            } else {
                showEmptyOrFailView();
                return;
            }
        }
        if (requestFailView.getVisibility() == View.VISIBLE) requestFailView.setVisibility(View.GONE);
        emergencyRescueAdapter.addData(maintenanceShops);
    }


    @Override
    public void onGetPriceFailed() {
        showEmptyOrFailView();
    }


    @Override
    public void onLocationFail(AMapLocation aMapLocation) {
        DebugLog.e("Location", aMapLocation.getErrorCode() + " " + aMapLocation.toString());
        if (mAddressView != null) mAddressView.setText("定位失败，请打开定位服务");
    }


    private AlertDialog sendPhoneNumDialog;


    public void sendMyPos(View view) {
        if (aMapLocation == null || aMapLocation.getErrorCode() != AMapLocation.LOCATION_SUCCESS) {
//                    ToastHelper.showRedToast(EmergencyRescueActivity.this, "当前定位无效，请重新开启本页面");
            phoneCall(null);
            return;
        }
        
        if (sendPhoneNumDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View emergencyPhoneDialogView = getLayoutInflater().inflate(R.layout.layout_emergency_phone_dialog, null);

            initPhoneDialogView(emergencyPhoneDialogView);

            builder.setView(emergencyPhoneDialogView);
            builder.setCancelable(true);
            sendPhoneNumDialog = builder.create();
            sendPhoneNumDialog.show();
        } else {
            sendPhoneNumDialog.show();
        }
    }


    private EditText mPhoneNum;
    private View mButton;


    private void initPhoneDialogView(View view) {
        view.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPhoneNumDialog.dismiss();
            }
        });

        mPhoneNum = (EditText) view.findViewById(R.id.phone_number);
        mButton = view.findViewById(R.id.submit);

        mPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mPhoneNum.getText().toString().length() == 11) {
                    mButton.setEnabled(true);
                } else {
                    mButton.setEnabled(false);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (UserInfoHelper.getInstance().isLogin(this)) mPhoneNum.setText(UserInfoHelper.getInstance().getPhoneNumber(this));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/9/30
                if (mPhoneNum.isFocused()) {
                    mButton.requestFocus();
                }

                if (FormatCheck.checkPhoneNumber(mPhoneNum.getText().toString().trim(), EmergencyRescueActivity.this) != 0) {
                    return;
                }

                sendPhoneNumDialog.dismiss();

                if (emergencyRescueApi == null) emergencyRescueApi = new EmergencyRescueApi();

                int index = aMapLocation.getAddress().indexOf("区") == -1 ? 0 : aMapLocation.getAddress().indexOf("区");

                String street = aMapLocation.getAddress().substring(index + 1);

                DebugLog.e(TAG, index + "  -------------  " + street);
                emergencyRescueApi.sendPos(mPhoneNum.getText().toString(), aMapLocation.getProvince(), aMapLocation.getCity(), aMapLocation.getDistrict(), street, aMapLocation.getAddress(), aMapLocation.getLongitude(), aMapLocation.getLatitude(), new LoadingAnimResponseHandler(EmergencyRescueActivity.this) {
                    @Override
                    public void onSuccess(String response) {
                        ToastHelper.showGreenToast(mContext, "信息提交成功");
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        ToastHelper.showRedToast(mContext, "信息提交失败");
                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                        phoneCall(null);
                    }
                });
            }
        });
    }


    public void phoneCall(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyRescueActivity.this, R.style.MaterialDialog);
        builder.setTitle("拨打电话").setMessage("400-1868-555").setPositiveButton("拨打", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri uri = Uri.parse("tel:" + "400-1868-555");
                intent.setData(uri);
                EmergencyRescueActivity.this.startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }
}