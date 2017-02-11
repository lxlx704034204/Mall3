/*
package com.hxqc.mall.auto.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.adapter.AutoInfoAdapterV2;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.inter.CallBack;
import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenu;
import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenuCreator;
import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenuItem;
import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenuListView;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrFrameLayout;

*/
/**
 * Author:胡仲俊
 * Date : 2016-02-16
 * FIXME
 * Todo 车辆信息
 *//*

public class AutoInfoActivityV2 extends BackActivity implements SwipeMenuListView.OnMenuItemClickListener, AdapterView.OnItemClickListener, View.OnClickListener, OnRefreshHandler {

    private SwipeMenuListView mAutoInfoListView1;
    private AutoInfoAdapterV2 autoInfoAdapterV2;
    private MenuItem itemEdit;
    private MenuItem itemFinish;
    private LinearLayout mAddBtnView;
    private View mAutoInfoFootView;

    protected PtrFrameLayout mPtrFrameLayoutView;
    protected UltraPullRefreshHeaderHelper mPtrHelper;
    private boolean isEdit = false;
    private String shopID;
    private int flags;
    private ArrayList<MyAuto> autoGroups;
    private int editPostion;
    private AlertDialog imAlertDialog = null;
    private RequestFailView mRequestFailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_info_v2);

//        if (!UserInfoHelper.getInstance().isLogin(this)) {
//            ActivitySwitchAuthenticate.toLogin(this, null, ActivitySwitchBase.ENTRANCE_ACCESSORYMAINTENANE);
//        }

        initView();

        initData();

        initEvent();

    }

    */
/**
     * 初始化数据
     *//*

    private void initData() {
        if (UserInfoHelper.getInstance().isLogin(this)) {
            if (getIntent() != null) {
                Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
                shopID = bundleExtra.getString("shopID");
                flags = bundleExtra.getInt("flagActivity", -1);
//                shopID = getIntent().getStringExtra("shopID");
//                flags = getIntent().getIntExtra("flagActivity", -1);
                DebugLog.i(TAG, "shopID:" + shopID + ",flags:" + flags);
                if (!TextUtils.isEmpty(shopID)) {
                    DebugLog.i(TAG, "筛选车辆");
                    if (AutoInfoContants.getTime() == 0) {
                        AutoTypeControl.getInstance().requestBrand(this, shopID, brandGroupCallBack);
                        autoGroups = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                        if (autoGroups != null && !autoGroups.isEmpty()) {
                            refreshData(autoGroups);
                        }
                    } else {
                        autoGroups = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                        refreshData(autoGroups);
                    }
                } else {
                    if (flags == AutoInfoContants.AUTO_DETAIL) {
                        if (AutoInfoContants.getTime() == 0) {
                            DebugLog.i(TAG, "常规保养第一次");
                            AutoInfoControl.getInstance().requestAutoInfo(this, autoInfoCallBack);
                            autoGroups = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                            if (autoGroups != null && !autoGroups.isEmpty()) {
                                refreshData(autoGroups);
                            }
                        } else {
                            DebugLog.i(TAG, "常规保养");
                            autoGroups = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                            refreshData(autoGroups);
                        }
                    } else {
                        DebugLog.i(TAG, "网络车辆");
                        autoGroups = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                        if (autoGroups != null && !autoGroups.isEmpty()) {
                            refreshData(autoGroups);
                        } else {
                            NoneDataListView();
                        }
                        AutoInfoControl.getInstance().requestAutoInfo(this, autoInfoCallBack);
                        createMenu();
                    }
                }
            } else {
                DebugLog.i(TAG, "本地车辆");
//                ArrayList<MyAuto> autoDataLocal = MyAutoInfoHelper.getInstance(this).getAutoDataLocal();
                autoGroups = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                if (getIntent() != null) {
                    Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
                    shopID = bundleExtra.getString("shopID");
//                    shopID = getIntent().getStringExtra("shopID");
                    flags = getIntent().getFlags();
                    DebugLog.i(TAG, "shopID:" + shopID + ",flags:" + flags);
                }
                if (autoGroups != null && !autoGroups.isEmpty()) {
                    refreshData(autoGroups);
                } else {
                    NoneDataListView();
                }
            }
        } else {
            if (getIntent() != null) {
                Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
                shopID = bundleExtra.getString("shopID");
                flags = bundleExtra.getInt("flagActivity", -1);
//                shopID = getIntent().getStringExtra("shopID");`
//                flags = getIntent().getIntExtra("flagActivity", -1);
                DebugLog.i(TAG, "shopID:" + shopID + ",flags:" + flags);
                if (autoGroups != null && !autoGroups.isEmpty()) {
                    autoGroups = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                    refreshData(autoGroups);
                }
            }
        }
    }

    */
/**
     * 初始化事件
     *//*

    private void initEvent() {

        if (flags != AutoInfoContants.AUTO_DETAIL && flags != AutoInfoContants.HOME_PAGE) {
            mAutoInfoListView1.setOnMenuItemClickListener(this);
            mAutoInfoListView1.setOnMenuStateChangeListener(menuStateListener);
        }
        mAutoInfoListView1.setOnItemClickListener(this);
        mAddBtnView.setOnClickListener(this);
        mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView);
        mPtrHelper.setOnRefreshHandler(this);

    }

    */
/**
     * 初始化控件
     *//*

    private void initView() {
        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.auto_info_ptr);
        mAutoInfoFootView = LayoutInflater.from(this).inflate(R.layout.view_auto_info_foot, null);
        mAutoInfoListView1 = (SwipeMenuListView) findViewById(R.id.auto_info_list);
        mAddBtnView = (LinearLayout) mAutoInfoFootView.findViewById(R.id.auto_info_add_auto);

        mRequestFailView = (RequestFailView) findViewById(R.id.auto_info_result_no_data);

    }

    private SwipeMenuCreator creator = null;

    */
/**
     * 初始化条目侧滑菜单
     *//*

    private void createMenu() {
        //创建条目侧滑菜单
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //defaultItem
           */
/*     createSwipeMenuItem(
                        menu,
                        getApplicationContext(),
                        R.color.auto_info_default_bg,
                        160,
                        "设为默认",
                        16,
                        Color.WHITE,
                        R.drawable.ic_information_setup);*//*

                //modifyItem
                //车辆修改按钮
                */
/*createSwipeMenuItem(
                        menu,
                        getApplicationContext(),
                        R.color.auto_info_modify_bg,
                        160,
                        "修改",
                        16,
                        Color.WHITE,
                        R.drawable.ic_information_modify);*//*

                //defaultItem
                //车辆删除按钮
                createSwipeMenuItem(
                        menu,
                        getApplicationContext(),
                        R.color.auto_info_delete_bg,
                        160,
                        "删除",
                        16,
                        Color.WHITE,
                        R.drawable.ic_information_del);
            }
        };

        mAutoInfoListView1.setMenuCreator(creator);
    }

    */
/**
     * 创建条目侧滑菜单内容的封装
     *
     * @param menu
     * @param context
     * @param backgroundId
     * @param width
     * @param title
     * @param titleSize
     * @param titleColor
     * @param resId
     *//*

    private void createSwipeMenuItem(SwipeMenu menu, Context context, int backgroundId, int width, String title, int titleSize, int titleColor, int resId) {
        // create "open" item
        SwipeMenuItem defaultItem = new SwipeMenuItem(
                context);
        // set item background
        defaultItem.setBackground(backgroundId);
        // set item width
        defaultItem.setWidth(width);
        // set item title
        defaultItem.setTitle(title);
        // set item title fontsize
        defaultItem.setTitleSize(titleSize);
        // set item title font color
        defaultItem.setTitleColor(titleColor);
        //set icon
        defaultItem.setIcon(resId);
        // add to menu
        menu.addMenuItem(defaultItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //加载标题栏菜单
        getMenuInflater().inflate(R.menu.menu_auto_info, menu);
//        itemEdit = menu.findItem(R.id.action_edit);
//        itemFinish = menu.findItem(R.id.action_finish);
        if (flags != AutoInfoContants.AUTO_DETAIL && flags != AutoInfoContants.HOME_PAGE) {
            optionsMenuState(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        //编辑按钮
//        if (item.getItemId() == R.id.action_edit) {
//            if (autoInfoAdapterV2 != null) {
//                if (autoInfoAdapterV2.getCount() > 0) {
//                    //弹开第一行条目
//                    mAutoInfoListView1.smoothOpenMenu(0);
//                    optionsMenuState(true);
//                }
//            }
//            //完成按钮
//        } else if (item.getItemId() == R.id.action_finish) {
//            if (autoInfoAdapterV2.getCount() > 0) {
//                mAutoInfoListView1.smoothCloseMenu();
//                optionsMenuState(false);
//            }
//        }
        return false;
    }

    private int isDefault = 0;

    @Override
    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
        switch (index) {
         */
/*   case 0:
                optionsMenuState(false);
                autoInfoAdapterV2.getmAutoGroups().get(position).isDefault =20;
                AutoInfoControl.getInstance().editAutoInfo(this, autoInfoAdapterV2.getmAutoGroups().get(position), this);
                isDefault = position;
                break;*//*

            //修改车辆
            */
/*case 0:
                optionsMenuState(false);
                if (UserInfoHelper.getInstance().isLogin(this)) {
                    editPostion = position;
                    ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, autoGroups.get(position), shopID, flags,AutoInfoContants.EDIT_PAGE);
                } else {
//                    ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, MyAutoInfoHelper.getInstance(this).getAutoDataLocal().get(position), shopID, -1);
                    ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO).get(position), shopID, -1,AutoInfoContants.EDIT_PAGE);
                }
                break;*//*

            //删除车辆
            case 0:
                optionsMenuState(false);
                imAlertDialog = new AlertDialog.Builder(this, R.style.MaterialDialog)
                        .setTitle("删除信息后无法恢复,您确定删除吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onDelete(position);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                imAlertDialog.show();
                break;
            default:
                break;
        }
        return false;
    }

    private int dlPosition = 0;

    */
/**
     * 删除车辆
     *
     * @param position
     *//*

    private void onDelete(int position) {
        DebugLog.i("Tag", "删除开始  " + System.currentTimeMillis());
        if (UserInfoHelper.getInstance().isLogin(this)) {
            DebugLog.i(TAG, "登录本地车辆");
            DebugLog.i(TAG, autoGroups.toString() + "----------");
            DebugLog.i(TAG, "position:" + position);
            AutoInfoControl.getInstance().deleteAutoInfo(this, autoGroups.get(position).myAutoID,
                    deleteCallBack);
            this.dlPosition = position;
        } else {
            //获取本地车辆数据
//            ArrayList<MyAuto> myAutos = MyAutoInfoHelper.getInstance(this).deleteAutoDataLocal(position);
            DebugLog.i(TAG, "未登录本地车辆");
            ArrayList<MyAuto> myAutos = AutoHelper.getInstance().deleteAutoDataLocal(this, position);
            if (myAutos.size() > 0) {
                DebugLog.i(TAG, "有数据");
                DebugLog.i(TAG, myAutos.get(0).toString());
                refreshData(myAutos);
            } else {
                DebugLog.i(TAG, "无数据");
                NoneDataListView();
            }
        }

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
                    AutoInfoControl.getInstance().requestAutoInfo(AutoInfoActivityV2.this, autoInfoCallBack);
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
                    AutoInfoControl.getInstance().requestAutoInfo(AutoInfoActivityV2.this, autoInfoCallBack);
                } else {
                    //获取本地车辆数据
//                    ArrayList<MyAuto> autoDataLocal = MyAutoInfoHelper.getInstance(this).getAutoDataLocal();
                    ArrayList<MyAuto> autoDataLocal = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                    refreshData(autoDataLocal);
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        DebugLog.i(TAG, "position:" + position);
        if (flags == AutoInfoContants.AUTO_DETAIL || flags == AutoInfoContants.HOME_PAGE) {
            if (UserInfoHelper.getInstance().isLogin(this)) {
                DebugLog.i(TAG, "登录后");
                DebugLog.i(TAG, autoGroups.get(position).toString());
                ActivitySwitchBase.toAutoDetail(this, autoGroups.get(position));
                AutoHelper.getInstance().switchAutoLocal(this, autoGroups.get(position), position, AutoHelper.AUTO_LOCAL_INFO);
                AutoInfoContants.setTime();
                finish();
            } else {
                DebugLog.i(TAG, "登录前");
                AutoHelper.getInstance().switchAutoLocal(this, autoGroups.get(position), position, AutoHelper.AUTO_LOCAL_INFO);
//                ActivitySwitchBase.toAutoDetail(this, MyAutoInfoHelper.getInstance(this).getAutoDataLocal().get(position));
                ActivitySwitchBase.toAutoDetail(this, AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO).get(position));
                finish();
            }
        } else {
         */
/*   if (!isEdit) {
                if (AutoInfoControl.getInstance().getMyAutoGroup() != null) {
                    ActivitySwitchAutoInfo.toRepairRecord(this, AutoInfoControl.getInstance().getMyAutoGroup().get(position));
                }
            }*//*

            if (UserInfoHelper.getInstance().isLogin(this)) {
//                editPostion = position;
//                ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, autoGroups.get(position), shopID, flags,AutoInfoContants.EDIT_PAGE);
                ActivitySwitchAutoInfo.toAutoDetailActivity(this,autoGroups.get(position));
            } else {
//                    ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, MyAutoInfoHelper.getInstance(this).getAutoDataLocal().get(position), shopID, -1);
                ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO).get(position), shopID, -1);
            }
            AutoInfoContants.setTime();
        }

    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.auto_info_add_auto) {
            DebugLog.i(TAG, "Add myauto");
            ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, null, shopID, flags,AutoInfoContants.ADD_PAGE);
        }
    }

    private CallBack<ArrayList<MyAuto>> autoInfoCallBack = new CallBack<ArrayList<MyAuto>>() {
        @Override
        public void onSuccess(ArrayList<MyAuto> response) {
            mRequestFailView.setVisibility(View.GONE);
            if (response != null && !response.isEmpty()) {
                autoGroups = response;
                DebugLog.i(TAG, "onAutoInfoSucceed 有数据");
                DebugLog.i(TAG, response.get(0).toString());
                DebugLog.i(TAG, "size:" + response.size());
                ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(AutoInfoActivityV2.this, AutoHelper.AUTO_LOCAL_INFO);
//                ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(AutoInfoActivity.this, UserInfoHelper.getInstance().getPhoneNumber(AutoInfoActivity.this));
                if (autoLocal.isEmpty() || autoLocal.size() != autoGroups.size()) {
                    AutoHelper.getInstance().createAutoLocal(AutoInfoActivityV2.this, autoGroups, AutoHelper.AUTO_LOCAL_INFO);
//                            AutoHelper.getInstance().createAutoLocal(AutoInfoActivity.this, autoGroups,UserInfoHelper.getInstance().getPhoneNumber(AutoInfoActivity.this));
                }
                refreshData(response);
            } else {
                DebugLog.i(TAG, "onAutoInfoSucceed 无数据");
//                NoneDataListView();
                if (autoGroups == null) {
                    autoGroups = new ArrayList<MyAuto>();
                }
                refreshData(autoGroups);
            }
            //TODO 删除多余测试车辆
            */
/*if(response.size()>100) {
                for(int i=15;i<response.size();i++) {
                    onDelete(i);
                }
            }*//*

        }

        @Override
        public void onFailed(boolean offLine) {
            mRequestFailView.setVisibility(View.VISIBLE);
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);

           */
/* NoneDataListView();
            if (autoGroups == null) {
                autoGroups = new ArrayList<MyAuto>();
            }*//*

        }
    };

    */
/*@Override
    public void onAutoInfoSucceed(ArrayList<MyAuto> myAutoGroups) {
        if (myAutoGroups.size() > 0) {
            autoGroups = myAutoGroups;
            DebugLog.i(TAG, "onAutoInfoSucceed 有数据");
            DebugLog.i(TAG, myAutoGroups.get(0).toString());
            if (flags == AutoInfoContants.AUTO_DETAIL) {
                if (AutoInfoContants.getTime() == 0) {
                    AutoHelper.getInstance().createAutoLocal(AutoInfoActivity.this, autoGroups, AutoHelper.AUTO_DETAIL_INFO);
                }
            } else {
                AutoHelper.getInstance().createAutoLocal(AutoInfoActivity.this, autoGroups, AutoHelper.AUTO_DETAIL_INFO);
            }
            refreshData(myAutoGroups);
        } else {
            DebugLog.i(TAG, "onAutoInfoSucceed 无数据");
            NoneDataListView();
            autoGroups = new ArrayList<MyAuto>();
        }
    }

    @Override
    public void onAutoInfoFailed(boolean offLine) {

    }*//*


    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public void onRefresh() {
        DebugLog.i(TAG, "onRefresh");
        mPtrFrameLayoutView.refreshComplete();
        if (UserInfoHelper.getInstance().isLogin(this)) {
            if (flags == AutoInfoContants.AUTO_DETAIL) {
                AutoTypeControl.getInstance().requestBrand(this, shopID, brandGroupCallBack);
            } else {
                AutoInfoControl.getInstance().requestAutoInfo(this, autoInfoCallBack);
            }
        }
    }

    @Override
    public void onLoadMore() {
        DebugLog.i(TAG, "onLoadMore");
        mPtrFrameLayoutView.refreshComplete();
    }

    */
/*@Override
    public void onAESucceed(String response) {
        AutoInfoControl.getInstance().requestAutoInfo(this, this);

    }

    @Override
    public void onAEFailed(boolean offLine) {

    }*//*


    private CallBack<String> aeCallBack = new CallBack<String>() {
        @Override
        public void onSuccess(String response) {
            AutoInfoControl.getInstance().requestAutoInfo(AutoInfoActivityV2.this, autoInfoCallBack);
        }

        @Override
        public void onFailed(boolean offLine) {

        }
    };

    */
/**
     * 删除返回状态
     *//*

    private CallBack<String> deleteCallBack = new CallBack<String>() {
        @Override
        public void onSuccess(String response) {
            DebugLog.i(TAG, "dlPosition:" + dlPosition);
            autoGroups.remove(dlPosition);
            AutoHelper.getInstance().createAutoLocal(AutoInfoActivityV2.this, autoGroups, AutoHelper.AUTO_LOCAL_INFO);
            AutoInfoControl.getInstance().requestAutoInfo(AutoInfoActivityV2.this, autoInfoCallBack);
//        refreshData(autoGroups);
        }

        @Override
        public void onFailed(boolean offLine) {
            AutoInfoControl.getInstance().requestAutoInfo(AutoInfoActivityV2.this, autoInfoCallBack);
        }
    };

    */
/*@Override
    public void onDeleteSucceed(String response) {
        DebugLog.i(TAG, response + " 删除成功  --------------");
//        AutoHelper.getInstance().deleteMyAuto(this,autoGroups.get(dlPosition).plateNumber);
        autoGroups.remove(dlPosition);
        AutoInfoControl.getInstance().requestAutoInfo(this, this);
//        refreshData(autoGroups);

    }

    @Override
    public void onDeleteFailed(boolean offLine) {
        AutoInfoControl.getInstance().requestAutoInfo(this, this);
    }*//*


    private int isOpen = 0;
    private SwipeMenuListView.OnMenuStateChangeListener menuStateListener = new SwipeMenuListView.OnMenuStateChangeListener() {
        @Override
        public void onMenuOpen(int position) {
            DebugLog.i(TAG, "open:" + position);
            isOpen = 0;
            optionsMenuState(true);
        }

        @Override
        public void onMenuClose(int position) {
            DebugLog.i(TAG, "close:" + position);
            optionsMenuState(false);
        }
    };

    */
/**
     * ActionBar菜单的状态切换
     *
     * @param isOpen
     *//*

    private void optionsMenuState(boolean isOpen) {
        if (isOpen) {
            itemFinish.setVisible(true);
            itemEdit.setVisible(false);
            isEdit = true;
        } else {
            itemFinish.setVisible(false);
            itemEdit.setVisible(true);
            isEdit = false;
        }
    }

    */
/**
     * 没数据的车辆信息列表
     *//*

    private void NoneDataListView() {
        if (autoInfoAdapterV2 == null) {
            autoInfoAdapterV2 = new AutoInfoAdapterV2(AutoInfoActivityV2.this, new ArrayList<MyAuto>());
            mAutoInfoListView1.addFooterView(mAutoInfoFootView, null, false);
            mAutoInfoListView1.setAdapter(autoInfoAdapterV2);
        }
    }

    */
/**
     * 刷新车辆信息列表
     *
     * @param myAutos
     *//*

    private void refreshData(ArrayList<MyAuto> myAutos) {
        if (autoInfoAdapterV2 == null) {
            autoInfoAdapterV2 = new AutoInfoAdapterV2(AutoInfoActivityV2.this, myAutos);
            mAutoInfoListView1.addFooterView(mAutoInfoFootView, null, false);
            mAutoInfoListView1.setAdapter(autoInfoAdapterV2);
        } else {
            autoInfoAdapterV2.notifyData(myAutos);
        }
    }

    @Override
    protected void onDestroy() {
        if (flags != AutoInfoContants.AUTO_DETAIL) {
            AutoInfoContants.clearTime();
        }
        creator = null;
        if (imAlertDialog != null) {
            imAlertDialog.dismiss();
        }
        AutoInfoControl.getInstance().killInstance();
        AutoHelper.getInstance().killInstance(this);
        super.onDestroy();
    }

    private CallBack<ArrayList<BrandGroup>> brandGroupCallBack = new CallBack<ArrayList<BrandGroup>>() {
        @Override
        public void onSuccess(final ArrayList<BrandGroup> brandGroups) {
            AutoInfoControl.getInstance().requestAutoInfo(AutoInfoActivityV2.this, new CallBack<ArrayList<MyAuto>>() {
                @Override
                public void onSuccess(ArrayList<MyAuto> response) {
                    //检索是否与是店铺车辆
                    autoGroups = new ArrayList<MyAuto>();
                    ArrayList<Brand> brands = AutoTypeControl.getInstance().getBrands(brandGroups);
                    for (int i = 0; i < response.size(); i++) {
                        for (int j = 0; j < brands.size(); j++) {
                            if (response.get(i).brand.equals(brands.get(j).brandName)) {
                                autoGroups.add(response.get(i));
                            }
                        }
                    }
                    if (autoGroups.size() > 0) {
                        DebugLog.i(TAG, "有数据");
                        DebugLog.i(TAG, autoGroups.size() + "");
                        DebugLog.i(TAG, autoGroups.get(0).toString());
                        if (AutoInfoContants.getTime() == 0) {
                            AutoHelper.getInstance().createAutoLocal(AutoInfoActivityV2.this, autoGroups, AutoHelper.AUTO_LOCAL_INFO);
//                        AutoHelper.getInstance().createAutoLocal(AutoInfoActivity.this, autoGroups, UserInfoHelper.getInstance().getPhoneNumber(AutoInfoActivity.this));
                        }
                        refreshData(autoGroups);
                    } else {
                        DebugLog.i(TAG, "无数据");
                        NoneDataListView();
                    }
                }

                @Override
                public void onFailed(boolean offLine) {

                }
            });
        }

        @Override
        public void onFailed(boolean offLine) {

        }
    };

    */
/*@Override
    public void onBrandSucceed(final ArrayList<BrandGroup> brandGroups) {
        AutoInfoControl.getInstance().requestAutoInfo(this, new CallBack<ArrayList<MyAuto>>() {
            @Override
            public void onSuccess(ArrayList<MyAuto> response) {
                //检索是否与是店铺车辆
                autoGroups = new ArrayList<MyAuto>();
                ArrayList<Brand> brands = AutoTypeControl.getInstance().getBrands(brandGroups);
                for (int i = 0; i < response.size(); i++) {
                    for (int j = 0; j < brands.size(); j++) {
                        if (response.get(i).brand.equals(brands.get(j).brandName)) {
                            autoGroups.add(response.get(i));
                        }
                    }
                }
                if (autoGroups.size() > 0) {
                    DebugLog.i(TAG, "有数据");
                    DebugLog.i(TAG, autoGroups.size() + "");
                    DebugLog.i(TAG, autoGroups.get(0).toString());
                    if (AutoInfoContants.getTime() == 0) {
                        AutoHelper.getInstance().createAutoLocal(AutoInfoActivityV2.this, autoGroups, AutoHelper.AUTO_LOCAL_INFO);
//                        AutoHelper.getInstance().createAutoLocal(AutoInfoActivity.this, autoGroups, UserInfoHelper.getInstance().getPhoneNumber(AutoInfoActivity.this));
                    }
                    refreshData(autoGroups);
                } else {
                    DebugLog.i(TAG, "无数据");
                    NoneDataListView();
                }
            }

            @Override
            public void onFailed(boolean offLine) {

            }
        });
                *//*
*/
/*new AutoInfoControl.AutoInfoHandler() {
            @Override
            public void onAutoInfoSucceed(ArrayList<MyAuto> myAutos) {
                autoGroups = new ArrayList<MyAuto>();
                ArrayList<Brand> brands = AutoTypeControl.getInstance().getBrands(brandGroups);
                for (int i = 0; i < myAutos.size(); i++) {
                    for (int j = 0; j < brands.size(); j++) {
                        if (myAutos.get(i).brandID.equals(brands.get(j).brandID)) {
                            autoGroups.add(myAutos.get(i));
                        }
                    }
                }
                if (autoGroups.size() > 0) {
                    DebugLog.i(TAG, "有数据");
                    DebugLog.i(TAG, autoGroups.get(0).toString());
                    if (AutoInfoContants.getTime() == 0) {
                        AutoHelper.getInstance().createAutoLocal(AutoInfoActivity.this, autoGroups, AutoHelper.AUTO_DETAIL_INFO);
                    }
                    refreshData(autoGroups);
                } else {
                    DebugLog.i(TAG, "无数据");
                    NoneDataListView();
                }
            }

            @Override
            public void onAutoInfoFailed(boolean offLine) {

            }
        });*//*
*/
/*
    }

    @Override
    public void onBrandFailed(boolean offLine) {

    }*//*


}
*/
