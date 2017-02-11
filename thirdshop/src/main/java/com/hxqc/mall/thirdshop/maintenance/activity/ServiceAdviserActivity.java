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
import com.hxqc.mall.thirdshop.maintenance.control.ReservationMaintainControl;
import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ReservationMaintainInfo;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * Author :胡仲俊
 * Date : 2016-03-09
 * FIXME
 * Todo 预约服务顾问
 */
public class ServiceAdviserActivity extends BackActivity implements SelectCounselorAdapter.SelectCounselorClickListener {

    private static final String TAG = "ServiceAdviserActivity";
    private RecyclerView mSelectCounselorListView;
    private SelectCounselorAdapter selectCounselorAdapter;
    private ArrayList<ServiceAdviser> serviceAdviser;
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
            Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            ArrayList<Parcelable> serviceAdviser = bundleExtra.getParcelableArrayList("serviceAdvisers");
//            ArrayList<Parcelable> serviceAdviser = getIntent().getParcelableArrayListExtra("serviceAdvisers");
            if (serviceAdviser != null && !serviceAdviser.isEmpty()) {
                DebugLog.i(TAG, "内存数据");
//                this.serviceAdviser = getIntent().getParcelableArrayListExtra("serviceAdvisers");
                this.serviceAdviser = bundleExtra.getParcelableArrayList("serviceAdvisers");
        /*        for (int i = 0; i < serviceAdviser.size(); i++) {
                    DebugLog.i(TAG, serviceAdviser.get(i).toString());
                }*/
            } else if (!TextUtils.isEmpty(bundleExtra.getString("shopID"))) {
                String shopID = bundleExtra.getString("shopID");
//                ReservationMaintainControl.getInstance().requestReservationMaintain(this, shopID, this);
                ReservationMaintainControl.getInstance().requestServiceAdviser(this, shopID, serviceAdvisersCallBack);
            }
/*
            else if (!TextUtils.isEmpty(getIntent().getStringExtra("shopID"))) {
                String shopID = getIntent().getStringExtra("shopID");
//                ReservationMaintainControl.getInstance().requestReservationMaintain(this, shopID, this);
                ReservationMaintainControl.getInstance().requestServiceAdviser(this, shopID, serviceAdvisersCallBack);
            }
*/
//            imPosition = getIntent().getIntExtra("position",-1);
            imPosition = bundleExtra.getInt("position",-1);
            DebugLog.i(TAG, "imPosition:" + imPosition);
        }
    }

    private void initEvent() {
        if (serviceAdviser != null) {
            if (selectCounselorAdapter == null) {
                DebugLog.i(TAG, "null");
                selectCounselorAdapter = new SelectCounselorAdapter(ServiceAdviserActivity.this, serviceAdviser);
                mSelectCounselorListView.setHasFixedSize(true);
                mSelectCounselorListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                mSelectCounselorListView.setAdapter(selectCounselorAdapter);
                selectCounselorAdapter.setOnItemClickListener(this);
                if (imPosition >= 0) {
                    selectCounselorAdapter.setIsChecked(imPosition);
                    selectCounselorAdapter.notifyDataSetChanged();
                }
            } else {
                DebugLog.i(TAG, "nonull");
                selectCounselorAdapter.notifyData(serviceAdviser);
            }
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
            if (serviceAdviser.size() > 0) {
                if (serviceAdviser.get(selectCounselorAdapter.getIsChecked()) != null) {
                    ActivitySwitcherMaintenance.toReserveMaintain(this, serviceAdviser.get(selectCounselorAdapter.getIsChecked()), selectCounselorAdapter.getIsChecked());
                    this.finish();
                } else {
                    ToastHelper.showYellowToast(this, "请选择顾问!");
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
            DebugLog.i(TAG, "position:" + position);
            selectCounselorAdapter.setIsChecked(position);
            selectCounselorAdapter.notifyDataSetChanged();
            imPosition = position;
        }
    }

    /*@Override
    public void onReservationMaintainSucceed(ReservationMaintainInfo reservationMaintainInfo) {
        if(reservationMaintainInfo!=null) {
            serviceAdviser = reservationMaintainInfo.getServiceAdviser();
            DebugLog.i(TAG, serviceAdviser.get(0).toString());
            initEvent();
        }
    }

    @Override
    public void onReservationMaintainFailed(boolean offLine) {
        mRequestFailView.notShop(this, "无数据");
    }*/

    private CallBackControl.CallBack<ReservationMaintainInfo> reservationMaintainInfoCallBack = new CallBackControl.CallBack<ReservationMaintainInfo>() {
        @Override
        public void onSuccess(ReservationMaintainInfo response) {
            if (response != null) {
                serviceAdviser = response.getServiceAdviser();
                DebugLog.i(TAG, serviceAdviser.get(0).toString());
                initEvent();
            }
        }

        @Override
        public void onFailed(boolean offLine) {
            mRequestFailView.notShop(ServiceAdviserActivity.this, "无数据");
        }
    };

    /*@Override
    public void onServiceAdviserSucceed(ArrayList<ServiceAdviser> serviceAdvisers) {
        mRequestFailView.setVisibility(View.GONE);
        serviceAdviser = serviceAdvisers;
        initEvent();
    }

    @Override
    public void onServiceAdviserFailed(boolean offLine) {
        mRequestFailView.notShop(this, "无数据");
    }*/

    private CallBackControl.CallBack<ArrayList<ServiceAdviser>> serviceAdvisersCallBack = new CallBackControl.CallBack<ArrayList<ServiceAdviser>>() {
        @Override
        public void onSuccess(ArrayList<ServiceAdviser> response) {
            mRequestFailView.setVisibility(View.GONE);
            serviceAdviser = response;
            initEvent();
        }

        @Override
        public void onFailed(boolean offLine) {
            mRequestFailView.notShop(ServiceAdviserActivity.this, "无数据");
        }
    };
}
