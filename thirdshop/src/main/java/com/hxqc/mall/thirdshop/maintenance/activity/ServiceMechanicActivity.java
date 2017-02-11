package com.hxqc.mall.thirdshop.maintenance.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.SelectCounselorAdapter;
import com.hxqc.mall.thirdshop.maintenance.adapter.ServiceMechanicAdapter;
import com.hxqc.mall.thirdshop.maintenance.control.ReservationMaintainControl;
import com.hxqc.mall.thirdshop.maintenance.model.Mechanic;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 15
 * FIXME
 * Todo 预约服务技师
 */
public class ServiceMechanicActivity extends BackActivity implements SelectCounselorAdapter.SelectCounselorClickListener, ServiceMechanicAdapter.SelectCounselorClickListener {

    private static final String TAG = "ServiceMechanicActivity";
    private RecyclerView mSelectCounselorListView;
    private ServiceMechanicAdapter selectCounselorAdapter;
    private ArrayList<Mechanic> serviceMechanics;
    private RequestFailView mRequestFailView;
    private int imPosition = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_counselor);

        initView();

        initData();

        initEvent();

    }

    private void initData() {
        if (getIntent() != null) {
//            ArrayList<Parcelable> mechanics = getIntent().getParcelableArrayListExtra("mechanics");
            Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            ArrayList<Parcelable> mechanics = bundleExtra.getParcelableArrayList("mechanics");
            if (mechanics != null && !mechanics.isEmpty()) {
                DebugLog.i(TAG, "内存数据");
//                serviceMechanics = getIntent().getParcelableArrayListExtra("mechanics");
                serviceMechanics = bundleExtra.getParcelableArrayList("mechanics");
/*                for (int i = 0; i < serviceMechanics.size(); i++) {
                    DebugLog.i(TAG, serviceMechanics.get(i).toString());
                }*/
            } else if (!TextUtils.isEmpty(bundleExtra.getString("shopID"))) {
                String shopID = bundleExtra.getString("shopID");
//                ReservationMaintainControl.getInstance().requestReservationMaintain(this, shopID, this);
                ReservationMaintainControl.getInstance().requestMechanic(this, shopID, mechanicsCallBack);
            }
/*
            else if (!TextUtils.isEmpty(getIntent().getStringExtra("shopID"))) {
                String shopID = getIntent().getStringExtra("shopID");
//                ReservationMaintainControl.getInstance().requestReservationMaintain(this, shopID, this);
                ReservationMaintainControl.getInstance().requestMechanic(this, shopID, mechanicsCallBack);
            }
*/
//            imPosition = getIntent().getIntExtra("position", -1);
            imPosition = bundleExtra.getInt("position", -1);
            DebugLog.i(TAG, "imPosition:" + imPosition);
        }
    }

    private void initEvent() {
        if (serviceMechanics != null) {
            if (selectCounselorAdapter == null) {
                selectCounselorAdapter = new ServiceMechanicAdapter(ServiceMechanicActivity.this, serviceMechanics);
                mSelectCounselorListView.setHasFixedSize(true);
                mSelectCounselorListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                mSelectCounselorListView.setAdapter(selectCounselorAdapter);
                selectCounselorAdapter.setOnItemClickListener(this);
                if (imPosition >= 0) {
                    selectCounselorAdapter.setIsChecked(imPosition);
                    selectCounselorAdapter.notifyDataSetChanged();
                }
            }
            selectCounselorAdapter.notifyData(serviceMechanics);
        }
    }

    private void initView() {
        mSelectCounselorListView = (RecyclerView) findViewById(R.id.select_counselor_list);
        mRequestFailView = (RequestFailView) findViewById(R.id.select_counselor_fail_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_service_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_confirmation) {
            if (serviceMechanics.size() > 0) {
                if (serviceMechanics.get(selectCounselorAdapter.getIsChecked()) != null) {
                    ActivitySwitcherMaintenance.toReserveMaintain(this, serviceMechanics.get(selectCounselorAdapter.getIsChecked()), selectCounselorAdapter.getIsChecked());
                    this.finish();
                } else {
                    ToastHelper.showYellowToast(this, "请选择技师!");
                }
            }
        }
        return false;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.isSelected()) {
//            view.setSelected(false);
        } else {
            selectCounselorAdapter.setIsChecked(position);
            selectCounselorAdapter.notifyDataSetChanged();
            imPosition = position;
        }
    }

    /*@Override
    public void onReservationMaintainSucceed(ReservationMaintainInfo reservationMaintainInfo) {
        if (reservationMaintainInfo != null) {
            serviceMechanics = reservationMaintainInfo.getMechanic();
            initEvent();
        }
    }

    @Override
    public void onReservationMaintainFailed(boolean offLine) {
        mRequestFailView.notShop(this, "无数据");
    }*/

    /*@Override
    public void onMechanicSucceed(ArrayList<Mechanic> mechanics) {
        mRequestFailView.setVisibility(View.GONE);
        serviceMechanics = mechanics;
        initEvent();
    }

    @Override
    public void onMechanicFailed(boolean offLine) {
        mRequestFailView.notShop(this, "无数据");
    }*/

    private CallBackControl.CallBack<ArrayList<Mechanic>> mechanicsCallBack = new CallBackControl.CallBack<ArrayList<Mechanic>>() {
        @Override
        public void onSuccess(ArrayList<Mechanic> response) {
            mRequestFailView.setVisibility(View.GONE);
            serviceMechanics = response;
            initEvent();
        }

        @Override
        public void onFailed(boolean offLine) {
            mRequestFailView.notShop(ServiceMechanicActivity.this, "无数据");
        }
    };
}
