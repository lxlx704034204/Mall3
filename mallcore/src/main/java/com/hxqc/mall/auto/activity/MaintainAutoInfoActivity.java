package com.hxqc.mall.auto.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.adapter.AutoInfoAdapterV3;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.event.FilterMaintenanceShopEvent;
import com.hxqc.mall.auto.event.ReserveMaintainAndHeadActivityEvent;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenuListView;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * Author:胡仲俊
 * Date: 2016-02-16
 * Des: 保养车辆信息列表
 * FIXME
 * Todo
 */
public class MaintainAutoInfoActivity extends BackActivity implements AdapterView.OnItemClickListener, View.OnClickListener, OnRefreshHandler {

    private static final String TAG = AutoInfoContants.LOG_J;
    private SwipeMenuListView mAutoInfoListView1;
    private AutoInfoAdapterV3 autoInfoAdapterV3;
    //    private MenuItem itemAdd;
    private LinearLayout mAddBtnView;
//    private View mAutoInfoFootView;

    protected PtrFrameLayout mPtrFrameLayoutView;
    protected UltraPullRefreshHeaderHelper mPtrHelper;
    private boolean isEdit = false;
    private String shopID;
    private int flagActivity;
    private ArrayList<MyAuto> autoGroups;
    private int editPostion;
    private AlertDialog imAlertDialog = null;
    private RequestFailView mRequestFailView;
    private Button mNoDataBtnView;
    private View mNoDataParentView;
    private ArrayList<Brand> mBrands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_info_v2);

        initView();

        initData();

        initEvent();

        EventBus.getDefault().register(this);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (getIntent() != null) {
            Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            shopID = bundleExtra.getString("shopID");
            flagActivity = bundleExtra.getInt("flagActivity", -1);
            mBrands = bundleExtra.getParcelableArrayList("brands");

            ArrayList<MyAuto> myAutos = bundleExtra.getParcelableArrayList("myAutos");

            DebugLog.i(TAG, "shopID:" + shopID + ",flags:" + flagActivity);
            if (myAutos != null) {
                showAutoList(myAutos);
            } else {
                mNoDataParentView.setVisibility(View.GONE);
                mRequestFailView.setVisibility(View.VISIBLE);
                mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
            }
//            AutoInfoControl.getInstance().requestAutoInfo(this, autoInfoCallBack);
        } else {
            DebugLog.i(TAG, "本地车辆");
//                ArrayList<MyAuto> autoDataLocal = MyAutoInfoHelper.getInstance(this).getAutoDataLocal();
            autoGroups = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
            if (getIntent() != null) {
                Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
                shopID = bundleExtra.getString("shopID");
//                    shopID = getIntent().getStringExtra("shopID");
                flagActivity = getIntent().getFlags();
                DebugLog.i(TAG, "shopID:" + shopID + ",flags:" + flagActivity);
            }
            if (autoGroups != null && !autoGroups.isEmpty()) {
                refreshData(autoGroups);
            } else {
                NoneDataListView();
            }
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {

        mAutoInfoListView1.setOnItemClickListener(this);
        mAddBtnView.setOnClickListener(this);
        mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView);
        mPtrHelper.setOnRefreshHandler(this);

    }

    /**
     * 初始化控件
     */
    private void initView() {
        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.auto_info_ptr);
        mAutoInfoListView1 = (SwipeMenuListView) findViewById(R.id.auto_info_list);
//        mAutoInfoFootView = LayoutInflater.from(this).inflate(R.layout.view_auto_info_foot, null);
//        mAddBtnView = (LinearLayout) mAutoInfoFootView.findViewById(R.id.auto_info_add_auto);
        mAddBtnView = (LinearLayout) findViewById(R.id.auto_info_add_auto);
        mRequestFailView = (RequestFailView) findViewById(R.id.auto_info_result_no_data);

        mNoDataParentView = findViewById(R.id.auto_no_data);
//        mNoDataParentView = (LinearLayout) view.findViewById(R.id.auto_no_data_parent);
        mNoDataBtnView = (Button) mNoDataParentView.findViewById(R.id.auto_no_data_btn);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //加载标题栏菜单
//        getMenuInflater().inflate(R.menu.menu_auto_info, menu);
//        itemAdd = menu.findItem(R.id.action_add);
//        if (flags != AutoInfoContants.AUTO_DETAIL && flags != AutoInfoContants.HOME_PAGE) {
//            optionsMenuState(false);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //编辑按钮
        /*if (item.getItemId() == R.id.action_add) {

            ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, null, shopID, flags, AutoInfoContants.ADD_PAGE, MaintainEditAutoActivity.class);
        }*/
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundleExtra = data.getBundleExtra(ActivitySwitchBase.KEY_DATA);
            //添加车辆成功刷新列表
            if (resultCode == AutoInfoContants.ADD_AUTO) {
                DebugLog.i(TAG, "添加数据");
                if (UserInfoHelper.getInstance().isLogin(this)) {
//                    MyAuto myAuto = data.getParcelableExtra("myAuto");
                    MyAuto myAuto = bundleExtra.getParcelable("myAuto");
                    DebugLog.i(TAG, "myAuto:" + myAuto.toString());
//                    autoGroups.set(editPostion, myAuto);
                    autoGroups.add(myAuto);
//                    AutoInfoControl.getInstance().requestAutoInfo(this, this);
                    refreshData(autoGroups);
                    AutoInfoControl.getInstance().requestAutoInfo(MaintainAutoInfoActivity.this, autoInfoCallBack);
                } else {
                    //获取本地车辆数据
//                    ArrayList<MyAuto> autoDataLocal = MyAutoInfoHelper.getInstance(this).getAutoDataLocal();
                    ArrayList<MyAuto> autoDataLocal = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                    refreshData(autoDataLocal);
                }
            } else if (resultCode == AutoInfoContants.EDIT_AUTO) {
                DebugLog.i(TAG, "修改数据");
                if (UserInfoHelper.getInstance().isLogin(this)) {
//                    MyAuto myAuto = data.getParcelableExtra("myAuto");
                    MyAuto myAuto = bundleExtra.getParcelable("myAuto");
                    DebugLog.i(TAG, "myAuto:" + myAuto.toString());
                    autoGroups.set(editPostion, myAuto);
//                    AutoInfoControl.getInstance().requestAutoInfo(this, this);
                    refreshData(autoGroups);
                    AutoInfoControl.getInstance().requestAutoInfo(MaintainAutoInfoActivity.this, autoInfoCallBack);
                } else {
                    //获取本地车辆数据
//                    ArrayList<MyAuto> autoDataLocal = MyAutoInfoHelper.getInstance(this).getAutoDataLocal();
                    ArrayList<MyAuto> autoDataLocal = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                    refreshData(autoDataLocal);
                }
            } else if (resultCode == AutoInfoContants.CHOOSE_SUCCESS) {
                DebugLog.i(TAG, "修改车辆成功");
            }
        }
    }

    @Subscribe
    public void onEventMainThread(MyAuto event) {
        DebugLog.i(TAG, "修改车辆成功");
        AutoInfoControl.getInstance().requestAutoInfo(this, autoInfoCallBack);
    }

    @Subscribe
    public void onEventMainThread(FilterMaintenanceShopEvent event) {
        DebugLog.i(TAG, "FilterMaintenanceShopEvent修改车辆成功");
        this.finish();
    }

    @Subscribe
    public void onEventMainThread(ReserveMaintainAndHeadActivityEvent event) {
        DebugLog.i(TAG, "ReserveMaintainAndHeadActivityEvent修改车辆成功");
        this.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        DebugLog.i(TAG, "position:" + position);
        DebugLog.i(TAG, autoGroups.get(position).toString());
        if (TextUtils.isEmpty(autoGroups.get(position).autoModel)) {
            ActivitySwitchAutoInfo.toChooseBrandActivity(this, autoGroups.get(position), flagActivity, false);
        } else {
            if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_LIST) {
                ActivitySwitchBase.toBackAutoData(this, autoGroups.get(position), "com.hxqc.mall.thirdshop.maintenance.activity.FilterMaintenanceShopListActivity", Activity.RESULT_OK);
            } else if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_REPAIR_DETAIL) {
                ActivitySwitchBase.toBackAutoData(this, autoGroups.get(position), "com.hxqc.mall.thirdshop.maintenance.activity.ReserveMaintainAndHeadActivity", Activity.RESULT_OK);
            } else {
                ActivitySwitchBase.toBackAutoData(this, autoGroups.get(position), "", Activity.RESULT_OK);
            }
            AutoHelper.getInstance().switchAutoLocal(this, autoGroups.get(position), position, AutoHelper.AUTO_LOCAL_INFO);
            AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(this, autoGroups.get(position), AutoHelper.SWITCH_AUTO);
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.auto_info_add_auto) {
            DebugLog.i(TAG, "Add myauto");
            if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_MAINTAIN_SHOP_QUOTE || flagActivity == AutoInfoContants.FOURS_SHOP) {
                ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, null, shopID, flagActivity, AutoInfoContants.ADD_PAGE, MaintainEditAutoActivity.class);
            } else if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_LIST || flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_REPAIR_DETAIL) {
                ActivitySwitchBase.toChooseBrandActivity(this, new MyAuto(), flagActivity, shopID, false);
            }
        }
    }

    private CallBackControl.CallBack<ArrayList<MyAuto>> autoInfoCallBack = new CallBackControl.CallBack<ArrayList<MyAuto>>() {
        @Override
        public void onSuccess(final ArrayList<MyAuto> response) {
            showAutoList(response);
        }

        @Override
        public void onFailed(boolean offLine) {
            DebugLog.i(TAG, "网络错误~~~");
            mNoDataParentView.setVisibility(View.GONE);
            mRequestFailView.setVisibility(View.VISIBLE);
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);

        }
    };

    /**
     * @param response
     */
    private void showAutoList(final ArrayList<MyAuto> response) {
        mRequestFailView.setVisibility(View.GONE);
        mNoDataParentView.setVisibility(View.GONE);
        if (response != null && !response.isEmpty()) {
            if (flagActivity == AutoInfoContants.FOURS_SHOP) {
                DebugLog.i(TAG, "FOURS_SHOP");
                autoGroups = AutoInfoControl.getInstance().getMatchAutoList(mBrands, response);
                if (autoGroups != null && !autoGroups.isEmpty()) {
                    AutoHelper.getInstance().createAutoLocal(MaintainAutoInfoActivity.this, autoGroups, AutoHelper.AUTO_INFO);
                    DebugLog.i(TAG, "onAutoInfoSucceed 有数据");
                    DebugLog.i(TAG, response.get(0).toString());
                    DebugLog.i(TAG, "size:" + response.size());
                    refreshData(autoGroups);
                }
            } else if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_REPAIR_DETAIL) {
                AutoTypeControl.getInstance().requestBrand(MaintainAutoInfoActivity.this, shopID, new CallBackControl.CallBack<ArrayList<BrandGroup>>() {
                    @Override
                    public void onSuccess(ArrayList<BrandGroup> brandGroups) {
                        DebugLog.i(TAG, "FLAG_ACTIVITY_APPOINTMENT");
                        ArrayList<Brand> allBrands = AutoHelper.getInstance().getAllBrands(brandGroups);
                        allBrands = AutoHelper.getInstance().distinctArrayList(allBrands);
                        autoGroups = AutoInfoControl.getInstance().getMatchAutoList(allBrands, response);
                        if (autoGroups != null && !autoGroups.isEmpty()) {
                            AutoHelper.getInstance().createAutoLocal(MaintainAutoInfoActivity.this, autoGroups, AutoHelper.AUTO_INFO);
                            DebugLog.i(TAG, "onSuccess 有数据");
                            DebugLog.i(TAG, response.get(0).toString());
                            DebugLog.i(TAG, "size:" + response.size());
                            refreshData(autoGroups);
                        }
                    }

                    @Override
                    public void onFailed(boolean offLine) {

                    }
                });
            } else if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_LIST) {
                ArrayList<MyAuto> isCompleteAutos = AutoInfoControl.getInstance().getIsCompleteAuto(response);
                if (!isCompleteAutos.isEmpty()) {
                    autoGroups = isCompleteAutos;
                    AutoHelper.getInstance().createAutoLocal(MaintainAutoInfoActivity.this, isCompleteAutos, AutoHelper.AUTO_INFO);
                    DebugLog.i(TAG, "onAutoInfoSucceed 有数据");
                    DebugLog.i(TAG, response.get(0).toString());
                    DebugLog.i(TAG, "size:" + response.size());
                    refreshData(isCompleteAutos);
                } else {
                    NoneDataListView();
                }
            } else {
                autoGroups = response;
                AutoHelper.getInstance().createAutoLocal(MaintainAutoInfoActivity.this, response, AutoHelper.AUTO_INFO);
                DebugLog.i(TAG, "onAutoInfoSucceed 有数据");
                DebugLog.i(TAG, response.get(0).toString());
                DebugLog.i(TAG, "size:" + response.size());
                refreshData(response);
            }
        } else {
            DebugLog.i(TAG, "onAutoInfoSucceed 无数据");
            if (flagActivity != AutoInfoContants.FOURS_SHOP) {
                NoneDataListView();
            }
        }
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public void onRefresh() {
        DebugLog.i(TAG, "onRefresh");
        mPtrFrameLayoutView.refreshComplete();
        if (UserInfoHelper.getInstance().isLogin(this)) {
            AutoInfoControl.getInstance().requestAutoInfo(this, autoInfoCallBack);
        }
    }

    @Override
    public void onLoadMore() {
        DebugLog.i(TAG, "onLoadMore");
        mPtrFrameLayoutView.refreshComplete();
    }

    /**
     * 没数据的车辆信息列表
     */
    private void NoneDataListView() {

        mRequestFailView.setVisibility(View.GONE);
        mNoDataParentView.setVisibility(View.VISIBLE);
        mNoDataBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    DebugLog.i(TAG,"去保养");
                if (flagActivity == AutoInfoContants.FOURS_SHOP) {

                } else {
                    ActivitySwitchAutoInfo.toFlowMaintain(MaintainAutoInfoActivity.this);
                }
            }
        });

    }

    /**
     * 刷新车辆信息列表
     *
     * @param myAutos
     */
    private void refreshData(ArrayList<MyAuto> myAutos) {

        if (autoInfoAdapterV3 == null) {
            autoInfoAdapterV3 = new AutoInfoAdapterV3(MaintainAutoInfoActivity.this, myAutos);
            mAutoInfoListView1.setAdapter(autoInfoAdapterV3);
        } else {
            autoInfoAdapterV3.notifyData(myAutos);
        }

    }

    @Override
    protected void onDestroy() {
        if (imAlertDialog != null) {
            imAlertDialog.dismiss();
        }
        AutoInfoControl.getInstance().killInstance();
        AutoHelper.getInstance().killInstance(this);

        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
