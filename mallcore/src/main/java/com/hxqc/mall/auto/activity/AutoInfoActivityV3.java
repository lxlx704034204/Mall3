package com.hxqc.mall.auto.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.adapter.AutoInfoAdapterV4;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.inter.CallBackControl;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * Author:胡仲俊
 * Date: 2016-02-16
 * Des: 车辆信息
 * FIXME
 * Todo
 */
public class AutoInfoActivityV3 extends BackActivity implements SwipeMenuListView.OnMenuItemClickListener, View.OnClickListener, OnRefreshHandler {

    private static final String TAG = AutoInfoContants.LOG_J;
    private SwipeMenuListView mAutoInfoListView1;
    //    private AutoInfoAdapterV2 autoInfoAdapterV2;
    private AutoInfoAdapterV4 mAutoInfoAdapter;
    private MenuItem itemAdd;
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
    private Button mNoDataBtnView;
    private View mNoDataParentView;
    private ArrayList<Brand> mBrands;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AutoInfoContants.GET_AUTO_DATA:
                    AutoInfoControl.getInstance().requestAutoInfo(AutoInfoActivityV3.this, autoInfoCallBack);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_info_v3);

        initView();

        initData();

        initEvent();

        EventBus.getDefault().register(this);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (UserInfoHelper.getInstance().isLogin(this)) {
            if (getIntent() != null) {
                Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
                shopID = bundleExtra.getString("shopID");
                flags = bundleExtra.getInt("flagActivity", -1);
                mBrands = bundleExtra.getParcelableArrayList("brands");

                DebugLog.i(TAG, "shopID:" + shopID + ",flags:" + flags);
                AutoInfoControl.getInstance().requestAutoInfo(this, autoInfoCallBack);
                createMenu();

                /*if (!TextUtils.isEmpty(shopID)) {
                    DebugLog.i(TAG, "筛选车辆");
                    *//*if (AutoInfoContants.getTime() == 0) {
                        AutoTypeControl.getInstance().requestBrand(this, shopID, this);
                        autoGroups = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                        if (autoGroups != null && !autoGroups.isEmpty()) {
                            refreshData(autoGroups);
                        }
                    } else {
                        autoGroups = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                        refreshData(autoGroups);
                    }*//*
                    AutoInfoControl.getInstance().requestAutoInfo(this, autoInfoCallBack);
                    mAutoInfoListView1.addFooterView(mAutoInfoFootView, null, false);
                    mAddBtnView.setOnClickListener(this);
                } else {
                    if (flags == AutoInfoContants.AUTO_DETAIL) {
                        AutoInfoControl.getInstance().requestAutoInfo(this, autoInfoCallBack);
                        mAutoInfoListView1.addFooterView(mAutoInfoFootView, null, false);
                        mAddBtnView.setOnClickListener(this);
                       *//* if (AutoInfoContants.getTime() == 0) {
                            DebugLog.i(TAG, "常规保养第一次");
                            AutoInfoControl.getInstance().requestAutoInfo(this, autoInfoCallBack);
                            *//**//*autoGroups = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                            if (autoGroups != null && !autoGroups.isEmpty()) {
                                refreshData(autoGroups);
                            }*//**//*
                            //常规保养下添加车辆按钮及监听
                            mAutoInfoListView1.addFooterView(mAutoInfoFootView, null, false);
                            mAddBtnView.setOnClickListener(this);
                        } else {
                            DebugLog.i(TAG, "常规保养");
//                            autoGroups = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
//                            refreshData(autoGroups);
                        }*//*
                    } else {
                        DebugLog.i(TAG, "网络车辆");
                        *//*autoGroups = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                        if (autoGroups != null && !autoGroups.isEmpty()) {
                            refreshData(autoGroups);
                        } else {
                            NoneDataListView();
                        }*//*
//                        NoneDataListView();
                        AutoInfoControl.getInstance().requestAutoInfo(this, autoInfoCallBack);
                        createMenu();
                    }
                }*/
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

    private HashMap<Integer, View> viewHashMap;

    /**
     * 初始化事件
     */
    private void initEvent() {

        mAutoInfoListView1.setOnMenuItemClickListener(this);
        mAutoInfoListView1.setOnMenuStateChangeListener(menuStateListener);
//        mAutoInfoListView1.setOnItemClickListener(this);
//        mAddBtnView.setOnClickListener(this);
        mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView);
        mPtrHelper.setOnRefreshHandler(this);

        /*new AutoInfoAdapterV4.EditClickListener() {
            @Override
            public void onEditClickListener(int position) {
                mAutoInfoListView1.smoothOpenMenu(position);
            }
        };*/

    }

    /**
     * 初始化控件
     */
    private void initView() {
        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.auto_info_ptr);
        mAutoInfoFootView = LayoutInflater.from(this).inflate(R.layout.view_auto_info_foot, null);
        mAutoInfoListView1 = (SwipeMenuListView) findViewById(R.id.auto_info_list);
        mAddBtnView = (LinearLayout) mAutoInfoFootView.findViewById(R.id.auto_info_add_auto);

        mRequestFailView = (RequestFailView) findViewById(R.id.auto_info_result_no_data);

        mNoDataParentView = findViewById(R.id.auto_no_data);
//        mNoDataParentView = (LinearLayout) view.findViewById(R.id.auto_no_data_parent);
        mNoDataBtnView = (Button) mNoDataParentView.findViewById(R.id.auto_no_data_btn);

    }

    private SwipeMenuCreator creator = null;

    /**
     * 初始化条目侧滑菜单
     */
    private void createMenu() {
        //创建条目侧滑菜单
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //defaultItem
           /*     createSwipeMenuItem(
                        menu,
                        getApplicationContext(),
                        R.color.auto_info_default_bg,
                        160,
                        "设为默认",
                        16,
                        Color.WHITE,
                        R.drawable.ic_information_setup);*/
                //modifyItem
                //车辆修改按钮
                createSwipeMenuItem(
                        menu,
                        getApplicationContext(),
                        R.color.auto_info_default_bg,
                        160,
                        "修改",
                        16,
                        Color.WHITE,
                        R.drawable.ic_information_modify);
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
     */
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
//        defaultItem.setIcon(resId);
        // add to menu
        menu.addMenuItem(defaultItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //加载标题栏菜单
        getMenuInflater().inflate(R.menu.menu_auto_info, menu);
        itemAdd = menu.findItem(R.id.action_add);
//        if (flags != AutoInfoContants.AUTO_DETAIL && flags != AutoInfoContants.HOME_PAGE) {
//            optionsMenuState(false);
//        }
        if (autoGroups != null && !autoGroups.isEmpty()) {
            itemAdd.setVisible(false);
        } else {
            itemAdd.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //添加按钮
        if (item.getItemId() == R.id.action_add) {
//            ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, null, "", flags, AutoInfoContants.ADD_PAGE, CenterEditAutoActivity.class);
            ActivitySwitchAutoInfo.toChooseBrandActivity(this, new MyAuto(), AutoInfoContants.MEMBER_CENTER, true);
            mAutoInfoListView1.smoothCloseMenu();
        }
        return false;
    }

    private int isDefault = 0;

    @Override
    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
        switch (index) {
         /*   case 0:
                optionsMenuState(false);
                autoInfoAdapterV2.getmAutoGroups().get(position).isDefault =20;
                AutoInfoControl.getInstance().editAutoInfo(this, autoInfoAdapterV2.getmAutoGroups().get(position), this);
                isDefault = position;
                break;*/
            //修改车辆
            case 0:
//                optionsMenuState(false);
//                mAutoInfoListView1.smoothCloseMenu();
                //                        if (UserInfoHelper.getInstance().isLogin(this)) {
//                            editPostion = position;
//                            ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, autoGroups.get(position), shopID, flags, AutoInfoContants.EDIT_PAGE, CenterEditAutoActivity.class);
//                        } else {
////                    ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, MyAutoInfoHelper.getInstance(this).getAutoDataLocal().get(position), shopID, -1);
//                            ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO).get(position), shopID, -1, AutoInfoContants.EDIT_PAGE, CenterEditAutoActivity.class);
//                        }
                editPostion = position;
                ActivitySwitchAutoInfo.toAddOrEditAutoInfo(AutoInfoActivityV3.this, autoGroups.get(position), shopID, flags, AutoInfoContants.EDIT_PAGE, CenterEditAutoActivity.class);
                break;
            //删除车辆
            case 1:
//                optionsMenuState(false);
//                mAutoInfoListView1.smoothCloseMenu();
                imAlertDialog = new AlertDialog.Builder(AutoInfoActivityV3.this, R.style.MaterialDialog)
                        .setTitle("您确定要删除这个车辆信息吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onDelete(position);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                imAlertDialog.show();
                /*mAutoInfoListView1.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 300);*/
                break;
            default:
                break;
        }
        return false;
    }

    private int dlPosition = 0;
    private MyAuto deleteAuto;

    /**
     * 删除车辆
     *
     * @param position
     */
    private void onDelete(int position) {
        DebugLog.i("Tag", "删除开始  " + System.currentTimeMillis());
        if (UserInfoHelper.getInstance().isLogin(this)) {
            DebugLog.i(TAG, "登录本地车辆");
            DebugLog.i(TAG, autoGroups.toString() + "----------");
            DebugLog.i(TAG, "position:" + position);
            deleteAuto = autoGroups.get(position);
            AutoInfoControl.getInstance().killInstance();
            AutoInfoControl.getInstance().deleteAutoInfo(this, autoGroups.get(position).myAutoID,
                    deleteCallBack);
            this.dlPosition = position;
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
                AutoInfoControl.getInstance().requestAutoInfo(AutoInfoActivityV3.this, autoInfoCallBack);
            } else if (resultCode == AutoInfoContants.EDIT_AUTO) {
                DebugLog.i(TAG, "修改数据");
//                AutoInfoControl.getInstance().requestAutoInfo(AutoInfoActivityV3.this, autoInfoCallBack);
                /*if (UserInfoHelper.getInstance().isLogin(this)) {
//                    MyAuto myAuto = data.getParcelableExtra("myAuto");
                    MyAuto myAuto = bundleExtra.getParcelable("myAuto");
                    DebugLog.i(TAG, "myAuto:" + myAuto.toString());
                    autoGroups.set(editPostion, myAuto);
//                    AutoInfoControl.getInstance().requestAutoInfo(this, this);
                    refreshData(autoGroups);
                    AutoInfoControl.getInstance().requestAutoInfo(AutoInfoActivityV3.this, autoInfoCallBack);
                } else {
                    //获取本地车辆数据
//                    ArrayList<MyAuto> autoDataLocal = MyAutoInfoHelper.getInstance(this).getAutoDataLocal();
                    ArrayList<MyAuto> autoDataLocal = AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO);
                    refreshData(autoDataLocal);
                }*/
            } else if (resultCode == AutoInfoContants.CHOOSE_SUCCESS) {
                DebugLog.i(TAG, "修改车辆成功");
            }
        }
    }

    @Subscribe
    public void onEventMainThread(MyAuto event) {
        DebugLog.i(TAG, "修改车辆成功" + event.toString());

        AutoInfoControl.getInstance().requestAutoInfo(this, autoInfoCallBack);
    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        DebugLog.i(TAG, "position:" + position);
        *//*if (flags == AutoInfoContants.AUTO_DETAIL || flags == AutoInfoContants.HOME_PAGE) {
            if (UserInfoHelper.getInstance().isLogin(this)) {
                DebugLog.i(TAG, "登录后");
                DebugLog.i(TAG, autoGroups.get(position).toString());
                if (TextUtils.isEmpty(autoGroups.get(position).autoModel)) {
                    CompleteAutoDialogUtils.dialogAutoInfoComplete(this);
                } else {
                    ActivitySwitchBase.toAutoDetail(this, autoGroups.get(position));
                    AutoHelper.getInstance().switchAutoLocal(this, autoGroups.get(position), position, AutoHelper.AUTO_LOCAL_INFO);
//                AutoInfoContants.setTime();
                    finish();
                }
            } else {
                DebugLog.i(TAG, "登录前");
//                AutoHelper.getInstance().switchAutoLocal(this, autoGroups.get(position), position, AutoHelper.AUTO_LOCAL_INFO);
//                ActivitySwitchBase.toAutoDetail(this, MyAutoInfoHelper.getInstance(this).getAutoDataLocal().get(position));
                ActivitySwitchBase.toAutoDetail(this, AutoHelper.getInstance().getAutoLocal(this, AutoHelper.AUTO_LOCAL_INFO).get(position));
                finish();
            }
        } else if (flags == AutoInfoContants.RESERVE_MAINTAIN) {
            if (TextUtils.isEmpty(autoGroups.get(position).autoModel)) {
                CompleteAutoDialogUtils.dialogAutoInfoComplete(this);
            } else {
                ActivitySwitchBase.toAutoDetail(this, autoGroups.get(position));
//                AutoHelper.getInstance().switchAutoLocal(this, autoGroups.get(position), position, AutoHelper.RESERVE_MAINTAIN_AUTO_INFO);
                finish();
            }
        } else {
            if (UserInfoHelper.getInstance().isLogin(this)) {
                DebugLog.i(TAG, autoGroups.get(position).toString());
                ActivitySwitchAutoInfo.toAutoDetailActivity(this, autoGroups.get(position));
            }
        }*//*
        mAutoInfoListView1.smoothCloseMenu();

        if (UserInfoHelper.getInstance().isLogin(this)) {
            DebugLog.i(TAG, autoGroups.get(position).toString());
            ActivitySwitchAutoInfo.toAutoDetailActivity(this, autoGroups.get(position));
        }

    }*/

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.auto_info_add_auto) {
            DebugLog.i(TAG, "Add myauto");
//            ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, null, shopID, flags, AutoInfoContants.ADD_PAGE, CenterEditAutoActivity.class);
            ActivitySwitchAutoInfo.toChooseBrandActivity(this, new MyAuto(), AutoInfoContants.MEMBER_CENTER, false);
        }
    }

    private CallBackControl.CallBack<ArrayList<MyAuto>> autoInfoCallBack = new CallBackControl.CallBack<ArrayList<MyAuto>>() {
        @Override
        public void onSuccess(ArrayList<MyAuto> response) {
            mRequestFailView.setVisibility(View.GONE);
            mNoDataParentView.setVisibility(View.GONE);
            if (response != null && !response.isEmpty()) {
                if (flags == AutoInfoContants.RESERVE_MAINTAIN) {
                    ArrayList<MyAuto> cacheMyAutos = new ArrayList<>();
                    for (int i = 0; i < response.size(); i++) {
                        for (int j = 0; j < mBrands.size(); j++) {
                            if (response.get(i).brand.equals(mBrands.get(j).brandName)) {
                                cacheMyAutos.add(response.get(i));
                            }
                        }
                    }
                    if (!cacheMyAutos.isEmpty()) {
                        autoGroups = cacheMyAutos;
                        refreshData(cacheMyAutos);
                    } else {
                        cacheMyAutos = null;
                        NoneDataListView();
                    }
                } else {
                    autoGroups = response;
//                    AutoHelper.getInstance().createAutoLocal(AutoInfoActivityV3.this, response, AutoHelper.AUTO_INFO);
                    DebugLog.i(TAG, "onAutoInfoSucceed 有数据");
                    DebugLog.i(TAG, "size:" + response.size());
//                ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(MaintainAutoInfoActivity.this, AutoHelper.AUTO_LOCAL_INFO);
//                ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(MaintainAutoInfoActivity.this, UserInfoHelper.getInstance().getPhoneNumber(MaintainAutoInfoActivity.this));
                /*if (response != null && !response.isEmpty()) {
                    DebugLog.i(TAG, "存储本地数据");
                    AutoHelper.getInstance().createAutoLocal(MaintainAutoInfoActivity.this, response, AutoHelper.AUTO_LOCAL_INFO);
//                            AutoHelper.getInstance().createAutoLocal(MaintainAutoInfoActivity.this, autoGroups,UserInfoHelper.getInstance().getPhoneNumber(MaintainAutoInfoActivity.this));
                }*/
                    refreshData(response);
                }
            } else {
                DebugLog.i(TAG, "onAutoInfoSucceed 无数据");
                NoneDataListView();
                /*if (autoGroups == null) {
                    autoGroups = new ArrayList<MyAuto>();
                }
                refreshData(autoGroups);*/
            }
        }

        @Override
        public void onFailed(boolean offLine) {
            DebugLog.i(TAG, "网络错误~~~");
            mNoDataParentView.setVisibility(View.GONE);
            mRequestFailView.setVisibility(View.VISIBLE);
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);

           /* NoneDataListView();
            if (autoGroups == null) {
                autoGroups = new ArrayList<MyAuto>();
            }*/
        }
    };

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

    private CallBackControl.CallBack<String> aeCallBack = new CallBackControl.CallBack<String>() {
        @Override
        public void onSuccess(String response) {
            AutoInfoControl.getInstance().requestAutoInfo(AutoInfoActivityV3.this, autoInfoCallBack);
        }

        @Override
        public void onFailed(boolean offLine) {

        }
    };

    /**
     * 删除返回状态
     */
    private CallBackControl.CallBack<String> deleteCallBack = new CallBackControl.CallBack<String>() {
        @Override
        public void onSuccess(String response) {
            DebugLog.i(TAG, "dlPosition:" + dlPosition);
            autoGroups.remove(dlPosition);

            AutoInfoControl.getInstance().checkAuto(AutoInfoActivityV3.this, deleteAuto, false);
            AutoHelper.getInstance().deleteSwitchAuto(AutoInfoActivityV3.this,deleteAuto,AutoHelper.SWITCH_AUTO);
            AutoInfoControl.getInstance().killInstance();
//            AutoHelper.getInstance().createAutoLocal(MaintainAutoInfoActivity.this, autoGroups, AutoHelper.AUTO_LOCAL_INFO);
            Message msg = Message.obtain();
            msg.what = AutoInfoContants.GET_AUTO_DATA;
            mHandler.sendMessage(msg);
//        refreshData(autoGroups);
        }

        @Override
        public void onFailed(boolean offLine) {
            AutoInfoControl.getInstance().killInstance();
            AutoInfoControl.getInstance().requestAutoInfo(AutoInfoActivityV3.this, autoInfoCallBack);
        }
    };

    private int isOpen = 0;
    private SwipeMenuListView.OnMenuStateChangeListener menuStateListener = new SwipeMenuListView.OnMenuStateChangeListener() {
        @Override
        public void onMenuOpen(int position) {
            DebugLog.i(TAG, "open:" + position);
//            mAutoInfoAdapter.setEditBnState(position,true);
            if (viewHashMap != null && viewHashMap.get(position) != null) {
                viewHashMap.get(position).setVisibility(View.GONE);
            }
            isOpen = 0;
        }

        @Override
        public void onMenuClose(int position) {
            DebugLog.i(TAG, "close:" + position);
//            mAutoInfoAdapter.setEditBnState(isOpen,false);
            if (viewHashMap != null && viewHashMap.get(position) != null) {
                viewHashMap.get(position).setVisibility(View.VISIBLE);
            }
//            mAutoInfoAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 没数据的车辆信息列表
     */
    private void NoneDataListView() {
        itemAdd.setVisible(false);
        if (flags == AutoInfoContants.AUTO_DETAIL) {
            DebugLog.i(TAG, "AUTO_DETAIL");
            mRequestFailView.setVisibility(View.VISIBLE);
            mNoDataParentView.setVisibility(View.GONE);
            mRequestFailView.setEmptyDescription("暂无车辆");
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
            mRequestFailView.setAddButtonClick().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ActivitySwitchAutoInfo.toAddOrEditAutoInfo(AutoInfoActivityV3.this, null, shopID, flags, AutoInfoContants.ADD_PAGE, CenterEditAutoActivity.class);
                    ActivitySwitchAutoInfo.toChooseBrandActivity(AutoInfoActivityV3.this, new MyAuto(), AutoInfoContants.MEMBER_CENTER, false);
                }
            });
        } else if (flags == AutoInfoContants.RESERVE_MAINTAIN) {
            DebugLog.i(TAG, "RESERVE_MAINTAIN");
            mRequestFailView.setVisibility(View.VISIBLE);
            mNoDataParentView.setVisibility(View.GONE);
            mRequestFailView.setEmptyDescription("您没有该品牌的车辆信息", R.drawable.ic_no_auto_data);
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        } else {
            DebugLog.i(TAG, "OHTER");
            mRequestFailView.setVisibility(View.GONE);
            mNoDataParentView.setVisibility(View.VISIBLE);
            mNoDataBtnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    DebugLog.i(TAG,"去保养");
//                    ActivitySwitchAutoInfo.toAddOrEditAutoInfo(AutoInfoActivityV3.this, null, "", flags, AutoInfoContants.ADD_PAGE, CenterEditAutoActivity.class);
                    ActivitySwitchAutoInfo.toChooseBrandActivity(AutoInfoActivityV3.this, new MyAuto(), AutoInfoContants.MEMBER_CENTER, false);
                }
            });
        }

        /*mRequestFailView.setEmptyButtonClick("请重新刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoInfoControl.getInstance().requestAutoInfo(MaintainAutoInfoActivity.this, autoInfoCallBack);
            }
        });*/
      /*  if (mAutoInfoAdapter == null) {
            mAutoInfoAdapter = new AutoInfoAdapterV3(MaintainAutoInfoActivity.this, new ArrayList<MyAuto>());
            mAutoInfoListView1.addFooterView(mAutoInfoFootView, null, false);
            mAutoInfoListView1.setAdapter(mAutoInfoAdapter);
        }*/
    }

    /**
     * 刷新车辆信息列表
     *
     * @param myAutos
     */
    private void refreshData(ArrayList<MyAuto> myAutos) {
        /*if (autoInfoAdapterV2 == null) {
            autoInfoAdapterV2 = new AutoInfoAdapterV2(MaintainAutoInfoActivity.this, myAutos);
//            mAutoInfoListView1.addFooterView(mAutoInfoFootView, null, false);
            mAutoInfoListView1.setAdapter(autoInfoAdapterV2);
        } else {
            autoInfoAdapterV2.notifyData(myAutos);
        }*/
        if (myAutos != null && !myAutos.isEmpty()) {
            itemAdd.setVisible(true);
        }
        if (mAutoInfoAdapter == null) {
            mAutoInfoAdapter = new AutoInfoAdapterV4(AutoInfoActivityV3.this, myAutos, mAutoInfoListView1);
//            mAutoInfoListView1.addFooterView(mAutoInfoFootView, null, false);
//            mAutoInfoListView1.setData(myAutos, true);
            mAutoInfoListView1.setAdapter(mAutoInfoAdapter);
            mAutoInfoAdapter.notifyData(myAutos);
            mAutoInfoAdapter.setOnCallBack(new AutoInfoAdapterV4.ViewStateCallBack() {
                @Override
                public void get(HashMap<Integer, View> map) {
                    viewHashMap = map;
                }
            });

        } else {
            mAutoInfoAdapter.notifyData(myAutos);
        }

    }

    @Override
    protected void onDestroy() {
        creator = null;
        if (imAlertDialog != null) {
            imAlertDialog.dismiss();
        }
        AutoInfoControl.getInstance().killInstance();
        AutoHelper.getInstance().killInstance(this);

        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private CallBackControl.CallBack<ArrayList<BrandGroup>> brandGroupCallBack = new CallBackControl.CallBack<ArrayList<BrandGroup>>() {
        @Override
        public void onSuccess(final ArrayList<BrandGroup> brandGroups) {
            AutoInfoControl.getInstance().requestAutoInfo(AutoInfoActivityV3.this, new CallBackControl.CallBack<ArrayList<MyAuto>>() {
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
                    /*if (AutoInfoContants.getTime() == 0) {
                        AutoHelper.getInstance().createAutoLocal(MaintainAutoInfoActivity.this, autoGroups, AutoHelper.AUTO_LOCAL_INFO);
//                        AutoHelper.getInstance().createAutoLocal(MaintainAutoInfoActivity.this, autoGroups, UserInfoHelper.getInstance().getPhoneNumber(MaintainAutoInfoActivity.this));
                    }*/
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

}
