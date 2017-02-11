package com.hxqc.mall.thirdshop.maintenance.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.hxqc.mall.activity.FocusEditNoBackActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.ServiceTypeAdapter;
import com.hxqc.mall.thirdshop.maintenance.control.ReservationMaintainControl;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ServiceType;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Author :胡仲俊
 * Date : 2016-03-08
 * Des: 服务类型
 * FIXME
 * Todo
 */
public class ServiceTypeActivity extends FocusEditNoBackActivity {

    private static final String TAG = AutoInfoContants.LOG_J;
    private RecyclerView mServiceTypeListView;
    private HashMap<Integer, String> serviceTypeList;
    private ServiceTypeAdapter serviceTypeAdapter;
    private RequestFailView mRequestFailView;
    private Button mServiceTypeCommitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type);

        initView();

        initEvent();

        initData();

    }

    private void initData() {
        Bundle bundleExtra = null;
        if (getIntent() != null) {
//            ArrayList<ServiceType> serviceTypes = getIntent().getParcelableArrayListExtra("serviceTypes");
            bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            ServiceType serviceType = bundleExtra.getParcelable("serviceType");
            ArrayList<ServiceType> serviceTypes = bundleExtra.getParcelableArrayList("serviceTypes");
            DebugLog.i(TAG, "serviceType: " + serviceType.toString());
            if (serviceType == null) {
                if (serviceTypes != null && !serviceTypes.isEmpty()) {
                    mRequestFailView.setVisibility(View.GONE);
                    initList(serviceTypes, serviceType);
                } else {
//                String shopID = getIntent().getStringExtra("shopID");
                    String shopID = bundleExtra.getString("shopID");
                    ReservationMaintainControl.getInstance().requestServiceType(this, shopID, serviceTypeCallBack);
                }
            } else {
                initList(serviceTypes, serviceType);
            }
        } else {
//         String shopID = getIntent().getStringExtra("shopID");
            bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            String shopID = bundleExtra.getString("shopID");
//          String shopID = "shop1585064992385057";
            ReservationMaintainControl.getInstance().requestServiceType(this, shopID, serviceTypeCallBack);
        }

    }

    private void initEvent() {

        mServiceTypeCommitView.setOnClickListener(commitClickListener);

    }

    private void initView() {

        mServiceTypeListView = (RecyclerView) findViewById(R.id.service_type_lists);

        mServiceTypeCommitView = (Button) findViewById(R.id.service_type_commit);

        mRequestFailView = (RequestFailView) findViewById(R.id.service_type_fail_view);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_service_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_confirmation) {
            if (serviceTypeAdapter != null) {
                if (serviceTypeAdapter.mServiceType != null) {
                    DebugLog.i(TAG, "remark: " + serviceTypeAdapter.getServiceTypeRemarkContent());
                    if (serviceTypeAdapter.mServiceType.serviceType.equals("-1")) {
                        if (TextUtils.isEmpty(serviceTypeAdapter.getServiceTypeRemarkContent())) {
                            ToastHelper.showRedToast(ServiceTypeActivity.this, "请填写备注补充说明");
                            serviceTypeAdapter.notifyBackground(true);
                            return false;
                        } else {
                            serviceTypeAdapter.mServiceType.remark = serviceTypeAdapter.getServiceTypeRemarkContent();
                        }
                    }
                    ActivitySwitcherMaintenance.toReserveMaintain(this, serviceTypeAdapter.mServiceType,serviceTypeAdapter.mServiceTypeGroup);
                    this.finish();
                }
            }

        }
        return false;
    }*/

    /*@Override
    public void onServiceTypeSucceed(ArrayList<ServiceType> serviceType) {
        mRequestFailView.setVisibility(View.GONE);
        initList(serviceType);
    }

    @Override
    public void onServiceTypeFailed(boolean offLine) {
        mRequestFailView.notShop(this, "无数据");
    }*/

    private CallBackControl.CallBack<ArrayList<ServiceType>> serviceTypeCallBack = new CallBackControl.CallBack<ArrayList<ServiceType>>() {
        @Override
        public void onSuccess(ArrayList<ServiceType> response) {
            mRequestFailView.setVisibility(View.GONE);
            initList(response, null);
        }

        @Override
        public void onFailed(boolean offLine) {
            mRequestFailView.notShop(ServiceTypeActivity.this, "无数据");
        }
    };

    private ServiceType serviceTypeOther;

    /**
     * 初始化列表
     *
     * @param serviceTypes
     */
    private void initList(ArrayList<ServiceType> serviceTypes, ServiceType serviceType) {

        if (!serviceTypes.get(serviceTypes.size() - 1).serviceType.equals("-1")) {
            if (serviceTypeOther == null) {
                DebugLog.i(TAG, "serviceTypeOther");
                serviceTypeOther = new ServiceType();
                serviceTypeOther.kindTitle = "其他";
                serviceTypeOther.serviceType = "-1";
                serviceTypeOther.remark = "";
                serviceTypeOther.items = new ArrayList<>();
                serviceTypes.add(serviceTypeOther);
            }
        }

        if (serviceTypeAdapter == null) {
            DebugLog.i(TAG, "ServiceTypeAdapter1");
            serviceTypeAdapter = new ServiceTypeAdapter(this, serviceTypes, serviceType);
            mServiceTypeListView.setLayoutManager(new LinearLayoutManager(this));
            mServiceTypeListView.setAdapter(serviceTypeAdapter);
        } else {
            DebugLog.i(TAG, "ServiceTypeAdapter2");
            serviceTypeAdapter.notifyData(serviceTypes, serviceType);
        }
    }

    private View.OnClickListener commitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (serviceTypeAdapter != null) {
                if (serviceTypeAdapter.mServiceType != null) {
                    DebugLog.i(TAG, "remark: " + serviceTypeAdapter.getServiceTypeRemarkContent());
                    if (serviceTypeAdapter.mServiceType.serviceType.equals("-1")) {
                        if (TextUtils.isEmpty(serviceTypeAdapter.getServiceTypeRemarkContent())) {
                            ToastHelper.showRedToast(ServiceTypeActivity.this, "请填写备注补充说明");
                            serviceTypeAdapter.notifyBackground(true);
                            return;
                        } else {
                            serviceTypeAdapter.mServiceType.remark = serviceTypeAdapter.getServiceTypeRemarkContent();
                        }
                    }
                    ActivitySwitcherMaintenance.toReserveMaintain(ServiceTypeActivity.this, serviceTypeAdapter.mServiceType, serviceTypeAdapter.mServiceTypeGroup);
                    finish();
                }
            }
        }
    };
}
