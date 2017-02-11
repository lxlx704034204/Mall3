package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.activity.BaseFilterActivity;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.AppointmentMaintenanceAdapter;
import com.hxqc.mall.thirdshop.maintenance.adapter.AppointmentMaintenanceGridAdapter;
import com.hxqc.mall.thirdshop.maintenance.adapter.MyItemDecoration;
import com.hxqc.mall.thirdshop.maintenance.adapter.NormalMaintenanceShopAdapter;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.control.FilterController;
import com.hxqc.mall.thirdshop.maintenance.fragment.FilterMaintenanceShopCoreFragment;
import com.hxqc.mall.thirdshop.maintenance.model.MaintenancePriceOrTitle;
import com.hxqc.mall.thirdshop.maintenance.model.MaintenanceShop;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * Function: 维修报价列表页面
 *
 * @author 袁秉勇
 * @since 2016年02月18日
 */
@Deprecated
public class FilterMaintenanceShopListActivity2 extends BaseFilterActivity implements FilterController.ShopHandler, FilterController.AppointmentMsgHandler, UserInfoHelper.OnLoginListener {
    private final static String TAG = FilterMaintenanceShopListActivity2.class.getSimpleName();
    private Context mContext;

    public static String ACTIVITY_TYPE = "type";
    public static int NORMAL_MAINTENANCE = 0;   // 表示当前页面是用做“常规保养”界面
    public static int APPOINTMENT_MAINTENANCE = 1;  // 表示当前页面是用作“预约维修”界面；
    NormalMaintenanceShopAdapter normalMaintenanceShopAdapter;
//    AppointmentMaintenanceAdapter appointmentMaintenanceAdapter;

    AppointmentMaintenanceGridAdapter appointmentMaintenanceAdapter;
    FilterController filterController;

    MaintenanceClient maintenanceClient;
    private int type = 0;

    private MaintenanceShop itemData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** 获取当前页面类型 常规保养 or 维修预约 **/
        if (getIntent().getBundleExtra("data") != null) {
            type = Integer.valueOf(getIntent().getBundleExtra("data").getString("type", "1"));
        }

        super.onCreate(savedInstanceState);

        if (type == APPOINTMENT_MAINTENANCE) {
            mChangeCityView.setVisibility(View.VISIBLE);

            MyItemDecoration myItemDecoration = new MyItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
            mRecyclerView.addItemDecoration(myItemDecoration);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            mRecyclerView.setBackgroundColor(Color.parseColor("#ECECEC"));
        }
    }


    @Override
    protected void initHashMap() {
        filterController = FilterController.getInstance();
        mFilterMap = filterController.mFilterMap;
    }


    @Override
    protected void setTitle() {
        if (type == NORMAL_MAINTENANCE) {
            toolbar.setTitle(getResources().getString(R.string.normal_maintenance));
        } else if (type == APPOINTMENT_MAINTENANCE) {
            toolbar.setTitle(getResources().getString(R.string.appointment_maintenance));
        }
    }


    @Override
    protected void initApiClient() {
        maintenanceClient = new MaintenanceClient();
    }


    @Override
    protected void initAdapter() {
        if (type == NORMAL_MAINTENANCE) {
            //初始店铺Adapter
            normalMaintenanceShopAdapter = new NormalMaintenanceShopAdapter(this) {
                @Override
                public NormalMaintenanceShopAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                    final NormalMaintenanceShopAdapter.Holder holder = super.onCreateViewHolder(parent, viewType);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MaintenanceShop itemData = getItemData(holder.getAdapterPosition());
                            ActivitySwitcherThirdPartShop.toMaintenanHome(itemData.shopID, FilterMaintenanceShopListActivity2.this);
                        }
                    });
                    return holder;
                }
            };
        } else if (type == APPOINTMENT_MAINTENANCE) {
            appointmentMaintenanceAdapter = new AppointmentMaintenanceGridAdapter() {
                @Override
                public AppointmentMaintenanceGridAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                    final AppointmentMaintenanceGridAdapter.Holder holder = super.onCreateViewHolder(parent, viewType);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemData = getItemData(holder.getAdapterPosition());
                            checkLogin();
                        }
                    });

                    holder.maintenanceBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemData = getItemData(holder.getAdapterPosition());
                            checkLogin();
                        }
                    });
                    return holder;
                }
            };
        }
    }


    private void checkLogin() {
        if (UserInfoHelper.getInstance().isLogin(this)) {
            onLoginSuccess();
        } else {
            UserInfoHelper.getInstance().loginAction(this, 50, this);
        }
    }


    @Override
    protected void initFragment() {
        DebugLog.e(TAG, " =============== initFragment");
        baseFilterCoreFragment = new FilterMaintenanceShopCoreFragment();
        baseFilterCoreFragment.setCallBack(this);
    }


    @Override
    protected void initFragmentView() {
        DebugLog.e(TAG, " =========== initFragmentView");
        if (baseFilterCoreFragment != null) {
            DebugLog.e(TAG, " I am showing ! ");
//            baseFilterCoreFragment.mGoodsCategoryView.setVisibility(View.GONE);
//            baseFilterCoreFragment.mAutoModelView.setVisibility(View.GONE);
        }
    }


    @Override
    protected void initData() {
        if (type == NORMAL_MAINTENANCE) {
            filterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
        } else if (type == APPOINTMENT_MAINTENANCE) {
            filterController.getAppointmentMaintenanceData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
        }
    }


    @Override
    protected void refreshData() {
        if (type == NORMAL_MAINTENANCE) {
            filterController.getShopData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
        } else if (type == APPOINTMENT_MAINTENANCE) {
            filterController.getAppointmentMaintenanceData(this, mPage = DEFAULT_PATE, PER_PAGE, filterController.mFilterMap, this);
        }
    }


    @Override
    public void onLoadMore() {
        ++mPage;
        if (type == NORMAL_MAINTENANCE) {
            filterController.getShopData(this, mPage, PER_PAGE, filterController.mFilterMap, this);
        } else if (type == APPOINTMENT_MAINTENANCE) {
            filterController.getAppointmentMaintenanceData(this, mPage, PER_PAGE, filterController.mFilterMap, this);
        }
    }


    @Override
    protected void toPositionChoose() {
        ActivitySwitcherMaintenance.toPositionActivity(this, 1, mChangeCityView.getText().toString());
    }


    @Override
    protected void clearAdapterData() {
        if (adapter instanceof NormalMaintenanceShopAdapter) {
            ((NormalMaintenanceShopAdapter) adapter).list.clear();
        } else if (adapter instanceof AppointmentMaintenanceAdapter) {
            ((AppointmentMaintenanceAdapter) adapter).list.clear();
        }
    }


    @Override
    protected void showDistanceInAdapter(boolean flag) {
        if (type == NORMAL_MAINTENANCE) {
            if (normalMaintenanceShopAdapter != null)
                normalMaintenanceShopAdapter.setShowDistance(flag);
        } else if (type == APPOINTMENT_MAINTENANCE) {
            if (appointmentMaintenanceAdapter != null)
                appointmentMaintenanceAdapter.setShowDistance(flag);
        }
    }


    @Override
    public void baseFilterCoreFragmentCallBack(String key, String value) {
        if (!TextUtils.isEmpty(value)) filterController.putValue(key, value);
        switch (key) {
            // 搜店铺
            case "brand":
                echo(filterController.mFilterMap);

                if (TextUtils.isEmpty(value)) {
                    if (filterController.getBrand() != null) {
                        if (type == NORMAL_MAINTENANCE && normalMaintenanceShopAdapter != null)
                            normalMaintenanceShopAdapter.setShowPrice(false);
                        initData();
                    }
                } else {
                    if (filterController.getBrand() == null) {
                        initData();
                    } else if (filterController.getBrand() != null && !value.equals(filterController.getBrand().brandName)) {
                        if (type == NORMAL_MAINTENANCE && normalMaintenanceShopAdapter != null)
                            normalMaintenanceShopAdapter.setShowPrice(false);
                        initData();
                    }
                }
                break;

            // 搜店铺
            case "series":
                echo(filterController.mFilterMap);

                if (TextUtils.isEmpty(value)) {
                    if (filterController.getSeries() != null) {
                        if (type == NORMAL_MAINTENANCE && normalMaintenanceShopAdapter != null)
                            normalMaintenanceShopAdapter.setShowPrice(false);
                        initData();
                    }
                } else {
                    if (type == NORMAL_MAINTENANCE && normalMaintenanceShopAdapter != null)
                        normalMaintenanceShopAdapter.setShowPrice(true);
                    if (filterController.getSeries() == null) {
                        initData();
                    } else if (filterController.getSeries() != null && !value.equals(filterController.getSeries().seriesName)) {
                        initData();
                    }
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterController.destroy();
    }


    private ArrayList<MaintenanceShop> constructData() {
        ArrayList<MaintenanceShop> maintenanceShopArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MaintenanceShop maintenanceShop = new MaintenanceShop();
            maintenanceShop.shopID = "shop1585067251822281";
            maintenanceShop.shopPhoto = "null";
            maintenanceShop.brandThumb = "null";
            maintenanceShop.shopTitle = "这是第 " + i + " 个ShopTitle";
            maintenanceShop.promotionTitle = "这是第 " + i + " 个PromotionTitle";
            maintenanceShop.distance = i * 100;
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
    public void onGetShopSucceed(ArrayList<MaintenanceShop> maintenanceShops) {
        if (maintenanceShops == null) {
            if (mPage == DEFAULT_PATE) {
                FoldJudgment(true);
            }
            return;
//            maintenanceShops = constructData();
        }
        hasMore = maintenanceShops.size() >= PER_PAGE;
        if (mPage == DEFAULT_PATE) {
            normalMaintenanceShopAdapter.list.clear();
            if (maintenanceShops.size() > 0) {
                mRecyclerView.setAdapter(normalMaintenanceShopAdapter);
            } else {
                FoldJudgment(true);
                return;
            }
        }
        normalMaintenanceShopAdapter.addData(maintenanceShops);
        FoldJudgment(false);
    }


    @Override
    public void onGetShopFailed() {
        FoldJudgment(true);
    }


    @Override
    public void onGetAppointmentMsgSucceed(ArrayList<MaintenanceShop> maintenanceShops) {
        if (maintenanceShops == null) {
            if (mPage == DEFAULT_PATE) {
                FoldJudgment(true);
            }
            return;
//            maintenanceShops = constructData();
        }
        hasMore = maintenanceShops.size() >= PER_PAGE;
        if (mPage == DEFAULT_PATE) {
            appointmentMaintenanceAdapter.list.clear();
            if (maintenanceShops.size() > 0) {
                mRecyclerView.setAdapter(appointmentMaintenanceAdapter);
            } else {
                FoldJudgment(true);
                return;
            }
        }
        appointmentMaintenanceAdapter.addData(maintenanceShops);
        FoldJudgment(false);
    }


    @Override
    public void onGetAppointmentMsgFailed() {
        FoldJudgment(true);
    }


    @Override
    public void onFinish() {

    }


    @Override
    public void onLoginSuccess() {
        if (itemData != null)
//            ActivitySwitcherMaintenance.toReserveMaintainAndHeadActivity(FilterMaintenanceShopListActivity2.this, itemData.shopType, itemData.shopID);
            ActivitySwitcherMaintenance.toReserveMaintainAndHeadActivity(FilterMaintenanceShopListActivity2.this, itemData.shopType, itemData.shopID,null);
    }
}
