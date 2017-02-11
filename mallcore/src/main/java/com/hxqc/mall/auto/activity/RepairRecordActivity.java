package com.hxqc.mall.auto.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.adapter.RepairRecordAdapterV3;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.MaintenanceRecord;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.launch.util.ActivitySwitchAuthenticate;
import com.hxqc.util.DebugLog;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 10
 * Des: 保养记录
 * FIXME
 * Todo
 */
public class RepairRecordActivity extends BackActivity implements View.OnClickListener {

    private static final String TAG = AutoInfoContants.LOG_J;
    private RecyclerView mRepairRecordListView;
    private View mRepairRecordHeaderView;
    private Button mLookView;
    private RepairRecordAdapterV3 repairRecordAdapter;
    private ImageView mAutoLogView;
    private TextView mAutoTypeView;
    private TextView mLicenseNumView;

    //    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == AutoInfoContants.MY_AUTO_DATA) {
//                MyAuto myAutoV2 = (MyAuto) msg.obj;
//                showMyAutoData(myAutoV2);
//            }
//        }
//    };
    private RequestFailView mRequestFailView;
    private LinearLayout mNextMaintenanceDistanceView;
    private LinearLayout mMaintenanceItemsTotalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_record);

        if (!UserInfoHelper.getInstance().isLogin(this)) {
            ActivitySwitchAuthenticate.toCodeLogin(this, ActivitySwitchBase.ENTRANCE_ACCESSORYMAINTENANE);
        }

        initView();

        initData();

        initEvent();

    }

    private void initData() {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            MyAuto myAuto = bundle.getParcelable("myAuto");
            DebugLog.i(TAG, myAuto.toString());
            AutoInfoControl.getInstance().maintenancRecord(this, myAuto, maintenanceRecordCallBack);
        }
    }

    /*初始化事件*/
    private void initEvent() {
//        mLookView.setOnClickListener(this);
    }

    /*初始化控件*/
    private void initView() {

//        mRepairRecordHeaderView = LayoutInflater.from(this).inflate(R.layout.layout_repair_record_header, null);
//        mAutoLogView = (ImageView) mRepairRecordHeaderView.findViewById(R.id.repair_record_auto_bg);
//        mAutoTypeView = (TextView) mRepairRecordHeaderView.findViewById(R.id.repair_record_auto);
//        mLicenseNumView = (TextView) mRepairRecordHeaderView.findViewById(R.id.repair_record_license_number);
//
//        mNextMaintenanceDistanceView = (LinearLayout) mRepairRecordHeaderView.findViewById(R.id.repair_record_next_maintenance_distance);
//        mMaintenanceItemsTotalView = (LinearLayout) mRepairRecordHeaderView.findViewById(R.id.repair_record_maintenance_items_total);

        mRepairRecordListView = (RecyclerView) findViewById(R.id.repair_record_list);

//        mLookView = (Button) mRepairRecordHeaderView.findViewById(R.id.repair_record_look);

        mRequestFailView = (RequestFailView) findViewById(R.id.repair_record_fail_view);

    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.repair_record_look) {
//            DebugLog.i(TAG,"toMain");
//            ActivitySwitchBase.toMain(this, 0);
//        }
    }

    private CallBackControl.CallBack<MaintenanceRecord> maintenanceRecordCallBack = new CallBackControl.CallBack<MaintenanceRecord>() {
        @Override
        public void onSuccess(MaintenanceRecord response) {
            if (response != null) {
                if (response.record == null || response.record.isEmpty()) {
                    showRepairEmpty();
                } else {
                    if (repairRecordAdapter == null) {
                        mRequestFailView.setVisibility(View.GONE);
                      /*  Message msg = Message.obtain();
                        msg.what = AutoInfoContants.MY_AUTO_DATA;
                        msg.obj = maintenanceRecord.getMyAuto();
                        mHandler.sendMessage(msg);
                        repairRecordAdapter = new RepairRecordAdapterV2(this);
                        mRepairRecordListView.setLayoutManager(new LinearLayoutManager(this));
                        mRepairRecordListView.setAdapter(repairRecordAdapter);
                        repairRecordAdapter.addDatas(maintenanceRecord.getRecord());
                        repairRecordAdapter.setHeaderView(mRepairRecordHeaderView);*/

                        repairRecordAdapter = new RepairRecordAdapterV3(RepairRecordActivity.this, response.record, response.myAuto, response.nextMaintenance);
                        mRepairRecordListView.setLayoutManager(new LinearLayoutManager(RepairRecordActivity.this));
                        mRepairRecordListView.setAdapter(repairRecordAdapter);
                    } else {
                        repairRecordAdapter.notifyData(response.record, response.myAuto, response.nextMaintenance);
                    }
                }
            } else {
                showRepairEmpty();
            }
        }

        @Override
        public void onFailed(boolean offLine) {
            mRequestFailView.setVisibility(View.VISIBLE);
            showRepairFailed();
        }
    };

    /**
     * 显示错误页面
     */
    private void showRepairFailed() {
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }

    /**
     * 显示空数据页面
     */
    private void showRepairEmpty() {
        mRequestFailView.showEmptyPage("您目前没有保养记录", R.drawable.ic_empty_repair, false, "", null);
    }

    /*@Override
    public void onMaintenanceRecordSucceed(MaintenanceRecord maintenanceRecord) {
//        mRequestFailView.setVisibility(View.GONE);
        if (repairRecordAdapter == null) {
          *//*  Message msg = Message.obtain();
            msg.what = AutoInfoContants.MY_AUTO_DATA;
            msg.obj = maintenanceRecord.getMyAuto();
            mHandler.sendMessage(msg);
            repairRecordAdapter = new RepairRecordAdapterV2(this);
            mRepairRecordListView.setLayoutManager(new LinearLayoutManager(this));
            mRepairRecordListView.setAdapter(repairRecordAdapter);
            repairRecordAdapter.addDatas(maintenanceRecord.getRecord());
            repairRecordAdapter.setHeaderView(mRepairRecordHeaderView);*//*

            repairRecordAdapter = new RepairRecordAdapterV3(this, maintenanceRecord.record, maintenanceRecord.myAuto, maintenanceRecord.nextMaintenance);
            mRepairRecordListView.setLayoutManager(new LinearLayoutManager(this));
            mRepairRecordListView.setAdapter(repairRecordAdapter);

        } else {
            repairRecordAdapter.notifyData(maintenanceRecord.record, maintenanceRecord.myAuto,maintenanceRecord.nextMaintenance);
        }
    }

    @Override
    public void onMaintenanceRecordFailed(boolean offLine) {
//        mRequestFailView.notShop(this,"没有保养记录");
    }

//    private void showMyAutoData(MyAuto myAutoV2) {
//        mAutoTypeView.setText(myAutoV2.autoModel);
//        mLicenseNumView.setText(myAutoV2.plateNumber);
//        if (TextUtils.isEmpty(myAutoV2.brandThumb)) {
//            mAutoLogView.setImageResource(R.drawable.pic_normal);
//        } else {
//            Picasso.with(this).load(myAutoV2.brandThumb)
//                    .placeholder(R.drawable.pic_normal)
//                    .error(R.drawable.pic_normal).into(mAutoLogView);
//        }
//    }*/
}
