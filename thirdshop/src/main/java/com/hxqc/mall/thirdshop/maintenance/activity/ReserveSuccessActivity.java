package com.hxqc.mall.thirdshop.maintenance.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.view.CommenTwoTextView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.control.ReservationMaintainControl;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ReservationSuccessInfo;
import com.hxqc.util.DebugLog;

/**
 * Author :胡仲俊
 * Date : 2016-03-09
 * FIXME
 * Todo 预约成功
 */
public class ReserveSuccessActivity extends NoBackActivity implements View.OnClickListener {

    private static final String TAG = "ReserveSuccessActivity";
    private Button mFinishView;
    private CommenTwoTextView mCustomerView;
    private CommenTwoTextView mPhoneNumView;
    private CommenTwoTextView mAutoInfoView;
    private CommenTwoTextView mStoreView;
    private CommenTwoTextView mServiceTypeView;
    private CommenTwoTextView mTimeView;
    //    private CommenTwoTextView mAdviserView;
//    private CommenTwoTextView mMechanicView;
    private ReservationSuccessInfo reservationSuccessInfo;
    private CommenTwoTextView mAutoPlateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reserve_success);

        initData();

        initView();

        initEvent();

    }

    private void initData() {
        if (getIntent() != null) {
            Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            reservationSuccessInfo = bundleExtra.getParcelable("reservationSuccessInfo");
//            reservationSuccessInfo = getIntent().getParcelableExtra("reservationSuccessInfo");
            if (reservationSuccessInfo != null) {
                if (reservationSuccessInfo.serviceTypeID.equals("-1")) {
                    DebugLog.i(TAG,"其他");
                    ReservationMaintainControl.getInstance().postReservationMaintain(
                            ReserveSuccessActivity.this,
                            reservationSuccessInfo.plateNumber,
                            reservationSuccessInfo.autoModelID,
                            reservationSuccessInfo.drivingDistance,
                            reservationSuccessInfo.name,
                            reservationSuccessInfo.phone,
                            reservationSuccessInfo.shopID,
                            reservationSuccessInfo.shopType,
                            reservationSuccessInfo.apppintmentDate,
                            reservationSuccessInfo.serviceTypeID,
                            reservationSuccessInfo.serviceAdviserID,
                            reservationSuccessInfo.mechanicID,
                            reservationSuccessInfo.VIN,
                            reservationSuccessInfo.remark,
                            postReservationMaintainCallBack
                    );
                } else {
                    DebugLog.i(TAG,"非其他");
                    ReservationMaintainControl.getInstance().postReservationMaintain(
                            ReserveSuccessActivity.this,
                            reservationSuccessInfo.plateNumber,
                            reservationSuccessInfo.autoModelID,
                            reservationSuccessInfo.drivingDistance,
                            reservationSuccessInfo.name,
                            reservationSuccessInfo.phone,
                            reservationSuccessInfo.shopID,
                            reservationSuccessInfo.shopType,
                            reservationSuccessInfo.apppintmentDate,
                            reservationSuccessInfo.serviceTypeID,
                            reservationSuccessInfo.serviceAdviserID,
                            reservationSuccessInfo.mechanicID,
                            reservationSuccessInfo.VIN,
                            postReservationMaintainCallBack
                    );
                }
            }
        }
    }

    private void initEvent() {
        mFinishView.setOnClickListener(this);
    }

    private void initView() {

        mCustomerView = (CommenTwoTextView) findViewById(R.id.reserve_success_customer);
        mPhoneNumView = (CommenTwoTextView) findViewById(R.id.reserve_success_phone_num);
        mAutoPlateView = (CommenTwoTextView) findViewById(R.id.reserve_success_auto_plate);
        mAutoInfoView = (CommenTwoTextView) findViewById(R.id.reserve_success_auto_info);
        mStoreView = (CommenTwoTextView) findViewById(R.id.reserve_success_store);
        mServiceTypeView = (CommenTwoTextView) findViewById(R.id.reserve_success_service_type);
        mTimeView = (CommenTwoTextView) findViewById(R.id.reserve_success_time);
//        mAdviserView = (CommenTwoTextView) findViewById(R.id.reserve_success_adviser);
//        mMechanicView = (CommenTwoTextView) findViewById(R.id.reserve_success_mechanic);

        mFinishView = (Button) findViewById(R.id.reserve_success_finish_btn);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reserve_success_finish_btn) {
            ActivitySwitchBase.toMain(this, 2);
            finish();
        }
    }

    /*@Override
    public void onPostReservationMaintainSucceed(String response) {
        setText();
    }

    @Override
    public void onPostReservationMaintainFailed(boolean offLine) {
    }*/

    private CallBackControl.CallBack<String> postReservationMaintainCallBack = new CallBackControl.CallBack<String>() {
        @Override
        public void onSuccess(String response) {
            setText();
        }

        @Override
        public void onFailed(boolean offLine) {

        }
    };

    private void setText() {
        mCustomerView.setTwoText(reservationSuccessInfo.name);
        mPhoneNumView.setTwoText(reservationSuccessInfo.phone);
        if (TextUtils.isEmpty(reservationSuccessInfo.plateNumber)) {
            mAutoPlateView.setVisibility(View.GONE);
        } else {
            mAutoPlateView.setVisibility(View.VISIBLE);
            mAutoPlateView.setTwoText(reservationSuccessInfo.plateNumber);
        }
        mAutoInfoView.setTwoText(reservationSuccessInfo.autoModel);
        mStoreView.setTwoText(reservationSuccessInfo.shop);
        mServiceTypeView.setTwoText(reservationSuccessInfo.serviceType);
        mTimeView.setTwoText(reservationSuccessInfo.apppintmentDate);
        /*if (TextUtils.isEmpty(reservationSuccessInfo.serviceAdviser)) {
            mAdviserView.setVisibility(View.GONE);
        } else {
            mAdviserView.setVisibility(View.VISIBLE);
            mAdviserView.setTwoText(reservationSuccessInfo.serviceAdviser);
        }

        if (TextUtils.isEmpty(reservationSuccessInfo.mechanic)) {
            mMechanicView.setVisibility(View.GONE);
        } else {
            mMechanicView.setTwoText(reservationSuccessInfo.mechanic);
        }*/

    }

    @Override
    protected void onDestroy() {
        ReservationMaintainControl.getInstance().killInstance();
        super.onDestroy();
    }
}
