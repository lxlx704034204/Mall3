package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.event.FilterMaintenanceShopEvent;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.base.BaseActivity;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.AppointmentMaintenanceGridAdapter;
import com.hxqc.mall.thirdshop.maintenance.adapter.MyItemDecoration;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.control.FilterController;
import com.hxqc.mall.thirdshop.maintenance.model.MaintenanceShop;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Function: 新版修车预约界面
 *
 * @author 袁秉勇
 * @since 2017年01月13日
 */
public class FilterMaintenanceShopListActivity extends BaseActivity implements OnRefreshHandler, UserInfoHelper.OnLoginListener, FilterController.AppointmentMsgHandler {
    private final static String TAG = FilterMaintenanceShopListActivity.class.getSimpleName();
    protected int PER_PAGE = 20;
    protected int mPage = 1;
    protected int DEFAULT_PATE = 1;
    protected boolean hasMore = false;
    protected MyAuto myAuto;
    protected Toolbar toolbar;
    protected TextView mChangeCityView;
    protected ImageView carBrandImageView;
    protected TextView carBrandNameView;
    protected PtrFrameLayout mPtrFrameLayout;
    protected UltraPullRefreshHeaderHelper mPterHelper;
    protected RequestFailView mRequestFailView;
    protected RecyclerView mRecyclerView;
    protected LatLng latLng = null;
    protected AppointmentMaintenanceGridAdapter appointmentMaintenanceAdapter;
    protected ClickFrom clickFrom = null;
    private Context mContext;
    private MaintenanceShop itemData;
    private MaintenanceClient maintenanceClient;
    private FilterController filterController;
    private BaseSharedPreferencesHelper baseSharedPreferencesHelper;
    private HashMap< String, String > mFilterMap;
    private int flagActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
            myAuto = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelable("myAuto");
            flagActivity = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getInt("flagActivity");
            try {
                DebugLog.i(AutoInfoContants.LOG_J, myAuto.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (myAuto == null) {
            ActivitySwitchBase.toChooseBrandActivity(this, new MyAuto(), AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_LIST, "", false);
            return;
        } else {
            filterController = FilterController.getInstance();
            mFilterMap = filterController.mFilterMap;
            baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(this);

            mFilterMap.put("brand", myAuto.brandName);
            mFilterMap.put("series", myAuto.series);
            mFilterMap.put("myAutoID", myAuto.myAutoID);
        }

        setContentView(R.layout.activity_maintance_appointment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(getResources().getString(R.string.appointment_maintenance));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_HOME) {
                    ActivitySwitchBase.toMain(FilterMaintenanceShopListActivity.this, 0);
                }*/
                finish();
            }
        });

        mChangeCityView = (TextView) findViewById(R.id.change_city);
        mChangeCityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherMaintenance.toPositionActivity(FilterMaintenanceShopListActivity.this, 1, mChangeCityView.getText().toString());
            }
        });

        carBrandImageView = (ImageView) findViewById(R.id.car_brand_img);
        carBrandNameView = (TextView) findViewById(R.id.car_brand_txt);
        changeCarViewInfo(myAuto);

        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.refresh_frame);
        mPterHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayout, this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        MyItemDecoration myItemDecoration = new MyItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        mRecyclerView.addItemDecoration(myItemDecoration);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        mRecyclerView.setBackgroundColor(Color.parseColor("#ECECEC"));
        mRecyclerView.setItemAnimator(null);

        /** 初始化Adapter **/
        appointmentMaintenanceAdapter = new AppointmentMaintenanceGridAdapter() {
            @Override
            public AppointmentMaintenanceGridAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                final AppointmentMaintenanceGridAdapter.Holder holder = super.onCreateViewHolder(parent, viewType);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemData = getItemData(holder.getAdapterPosition());
                        checkLogin(ClickFrom.ITEM);
                    }
                });

                holder.maintenanceBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemData = getItemData(holder.getAdapterPosition());
                        checkLogin(ClickFrom.ITEM);
                    }
                });
                return holder;
            }
        };
        mRecyclerView.setAdapter(appointmentMaintenanceAdapter);

        mRequestFailView = (RequestFailView) findViewById(R.id.request_view);

        initLocationData();

        maintenanceClient = new MaintenanceClient();

        getData();

        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEventMainThread(FilterMaintenanceShopEvent event) {
        DebugLog.i(TAG, "店铺列表添加车辆成功");
        if (event == null || event.myAuto == null) {
            DebugLog.e(TAG, "empty");
            return;
        } else {
            DebugLog.e(TAG, event.myAuto.toString());
        }

        mPage = DEFAULT_PATE;
        changeCarViewInfo(event.myAuto);
        mFilterMap.put("brand", event.myAuto.brandName);
        mFilterMap.put("series", event.myAuto.series);
        mFilterMap.put("myAutoID", event.myAuto.myAutoID);
        onRefresh();
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
            if (!TextUtils.isEmpty(baseSharedPreferencesHelper.getLongitudeBD()) && !TextUtils.isEmpty(baseSharedPreferencesHelper.getLatitudeBD())) latLng = new LatLng(Double.valueOf(baseSharedPreferencesHelper.getLatitudeBD()), Double.valueOf(baseSharedPreferencesHelper.getLongitudeBD()));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this, com.hxqc.mall.core.R.style.MaterialDialog);
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
                        onRefresh();
                        baseSharedPreferencesHelper.setHistoryCity(city);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).create().show();
    }

    private void getData() {
        filterController.getAppointmentMaintenanceData(this, mPage, PER_PAGE, filterController.mFilterMap, this);
    }

    private void checkLogin(ClickFrom clickFrom) {
        // 标明点击来源 用于处理登录后的不同跳转
        this.clickFrom = clickFrom;

        if (UserInfoHelper.getInstance().isLogin(this)) {
            onLoginSuccess();
        } else {
            UserInfoHelper.getInstance().loginAction(this, 50, this);
        }
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    @Override
    public void onRefresh() {
        mPage = DEFAULT_PATE;

        getData();
    }

    @Override
    public void onLoadMore() {
        ++mPage;
        getData();
        hasMore = false;
    }

    @Override
    public void onLoginSuccess() {
        if (clickFrom == ClickFrom.ITEM) {
            if (itemData != null) {
                ActivitySwitcherMaintenance.toReserveMaintainAndHeadActivity(this, itemData.shopType, itemData.shopID, myAuto);
            }
        } else if (clickFrom == ClickFrom.CHANGECAR) {
            if (myAuto != null) {
//                ActivitySwitchBase.toMaintainAutoInfo(this, "", AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_LIST);
                AutoInfoControl.getInstance().chooseAuto(this, myAuto, "", AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_LIST,null);
            } else {
                ActivitySwitchBase.toChooseBrandActivity(this, new MyAuto(), AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_LIST, "", false);
            }
        }
    }

    @Override
    public void onGetAppointmentMsgSucceed(ArrayList< MaintenanceShop > maintenanceShops) {
        if (maintenanceShops != null && maintenanceShops.size() > 0) {
            hasMore = maintenanceShops.size() >= 20;
            if (mPage == DEFAULT_PATE && appointmentMaintenanceAdapter.list.size() > 0) {
                showEmptyView(false);
                appointmentMaintenanceAdapter.clearData();
            }
            appointmentMaintenanceAdapter.addData(maintenanceShops);

        } else {
            if (mPage == DEFAULT_PATE) {
                showEmptyView(true);
            }
            mPage = mPage > 1 ? mPage - 1 : 1;
        }
    }

    @Override
    public void onGetAppointmentMsgFailed() {
        mPage = mPage > 1 ? mPage - 1 : 1;
        if (mPage == DEFAULT_PATE) showEmptyView(true);
    }

    @Override
    public void onFinish() {
        if (mPtrFrameLayout.isRefreshing()) mPtrFrameLayout.refreshComplete();
    }

    private void showEmptyView(boolean show) {
        if (show) {
            mRequestFailView.setEmptyDescription("未找到结果");
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }
        if (mRequestFailView.isShown() != show) mRequestFailView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showLocationIcon(boolean flag) {
        if (mChangeCityView == null) return;
        if (flag) {
            if ((mChangeCityView.getCompoundDrawables())[0] == null) mChangeCityView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(com.hxqc.mall.core.R.drawable.t_icon_button_location), null, null, null);
        } else {
            if ((mChangeCityView.getCompoundDrawables())[0] != null) {
                mChangeCityView.setCompoundDrawables(null, null, null, null);
            }

        }
    }

    protected void showDistanceInAdapter(boolean flag) {
        if (appointmentMaintenanceAdapter != null) appointmentMaintenanceAdapter.setShowDistance(flag);
    }

    public void changeCar(View view) {
        checkLogin(ClickFrom.CHANGECAR);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DebugLog.d(TAG, "execute onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

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
                onRefresh();
            }
        } else if (requestCode == AutoInfoContants.GET_AUTO_DATA) {
            if (data != null && data.getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
                myAuto = data.getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelable("myAuto");

                DebugLog.e(TAG, myAuto.toString());

                changeCarViewInfo(myAuto);

                mFilterMap.put("brand", myAuto.brandName);
                mFilterMap.put("series", myAuto.series);
                mFilterMap.put("myAutoID", myAuto.myAutoID);
                onRefresh();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_HOME) {
            ActivitySwitchBase.toMain(this, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (filterController != null) filterController.destroy();

        EventBus.getDefault().unregister(this);
    }

    private void changeCarViewInfo(MyAuto myAuto) {
        if (TextUtils.isEmpty(myAuto.brandThumb)) {
            myAuto.brandThumb = "";
        }
        ImageUtil.setImage(this, carBrandImageView, myAuto.brandThumb);
	    carBrandNameView.setText(Html.fromHtml("<p><span style=\"font-size:%20.0fpx\">" + myAuto.autoModel.substring(0, myAuto.autoModel.indexOf(" ")) + "</span><br/>"
			    + myAuto.autoModel.substring(myAuto.autoModel.indexOf(" ")) + "</p>"));
//        carBrandNameView.setText(myAuto.brand + " " + myAuto.series + "\n" + myAuto.autoModel.substring(myAuto.autoModel.indexOf(" ")));
    }


    /**
     * 用于判断点击来自“列表”还是“选车”
     **/
    enum ClickFrom {
        ITEM, CHANGECAR
    }
}