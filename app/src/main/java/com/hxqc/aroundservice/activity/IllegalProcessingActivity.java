package com.hxqc.aroundservice.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.control.IllegalQueryControl;
import com.hxqc.aroundservice.control.IllegalSharedPreferencesHelper;
import com.hxqc.aroundservice.fragment.QueryProcessingPhotoFragmentV2;
import com.hxqc.aroundservice.model.IllegalOrderDetail;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.mall.activity.FocusEditBackActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.view.CommonEditInfoItemView;
import com.hxqc.mall.auto.view.CommonTwoTextView;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.util.DebugLog;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 23
 * FIXME
 * Todo 订单处理页面
 */
public class IllegalProcessingActivity extends FocusEditBackActivity {

    private static final String TAG = AutoInfoContants.LOG_J;
    private CommonTwoTextView mPlateNumberView;
    private CommonEditInfoItemView mNameView;
    private CommonEditInfoItemView mPhoneView;
    private Button mProcessingView;
    private QueryProcessingPhotoFragmentV2 mOrigianlFragment;
    private QueryProcessingPhotoFragmentV2 mCopyFragment;
    private String plateNumber;
    private String choseWZID;
    private IllegalOrderDetail illegalOrderDetail;
    private String cityCode;
    private String cityName;
    private String provinceName;
    private String hpzl;
    private String engineno;
    private String classno;
    private IllegalSharedPreferencesHelper imIllegalSharedPreferencesHelper;
    private RequestFailView mFailView;
    private LinearLayout mParentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_illegal_processing);

        initView();

        initData();

        initEvent();

    }

    private void initEvent() {
        mProcessingView.setOnClickListener(clickProcessingListener);

//        mOrigianlFragment.setClickSampleListener(this,R.drawable.ic_sample_license_origianl);
//        mCopyFragment.setClickSampleListener(this,R.drawable.ic_sample_license_copy);
        mOrigianlFragment.setClickSampleTitleListener(this, R.drawable.ic_sample_licence_origianl);
        mCopyFragment.setClickSampleTitleListener(this, R.drawable.ic_sample_licence_copy);
    }

    private void initData() {

        if (getIntent() != null) {
            imIllegalSharedPreferencesHelper = new IllegalSharedPreferencesHelper(this);
            Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            plateNumber = bundleExtra.getString("plateNumber");
            choseWZID = bundleExtra.getString("choseWZID");
//            plateNumber = getIntent().getStringExtra("plateNumber");
//            choseWZID = getIntent().getStringExtra("choseWZID");
            cityCode = imIllegalSharedPreferencesHelper.getCityCode();
            cityName = imIllegalSharedPreferencesHelper.getCityName();
            provinceName = imIllegalSharedPreferencesHelper.getProvinceName();
            hpzl = imIllegalSharedPreferencesHelper.getHPZL();
            engineno = imIllegalSharedPreferencesHelper.getEngineno();
            classno = imIllegalSharedPreferencesHelper.getClassno();
            DebugLog.i(TAG, "plateNumber:" + plateNumber + "----choseWZID:" + choseWZID + "----cityCode:" + cityCode + "----cityName:" + provinceName + "----provinceName:" + cityCode + "----hpzl:" + hpzl);
        }
        mPlateNumberView.setTwoText(plateNumber);
        UserInfoHelper.getInstance().refreshUserInfo(IllegalProcessingActivity.this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                DebugLog.i(TAG, "showUserInfo");
                DebugLog.i(TAG, "fullname:" + meData.fullname);
                DebugLog.i(TAG, "phoneNumber:" + meData.phoneNumber);
                mNameView.setContentText(meData.fullname);
                mPhoneView.setContentText(meData.phoneNumber);
//                AutoInfoControl.getInstance().requestAutoInfo(IllegalProcessingActivity.this, autoInfoCallBack);
            }

            @Override
            public void onFinish() {
                DebugLog.i(TAG, "onFinish");
            }
        }, false);

    }

    private void initView() {
        mPlateNumberView = (CommonTwoTextView) findViewById(R.id.illegal_processing_plate_number);
        mPlateNumberView.setFirstDrawableLeft(R.drawable.bg_star);
        mNameView = (CommonEditInfoItemView) findViewById(R.id.illegal_processing_name);
        mPhoneView = (CommonEditInfoItemView) findViewById(R.id.illegal_processing_phone);
        mProcessingView = (Button) findViewById(R.id.illegal_result_v2_processing);

        mOrigianlFragment = (QueryProcessingPhotoFragmentV2) getSupportFragmentManager().findFragmentById(R.id.illegal_processing_original);
        mCopyFragment = (QueryProcessingPhotoFragmentV2) getSupportFragmentManager().findFragmentById(R.id.illegal_processing_copy);
//        mOrigianlFragment.setTitle("行驶证正本");
//        mCopyFragment.setTitle("行驶证副本");
        mOrigianlFragment.setTitle("行驶证正本正面照");
        mCopyFragment.setTitle("行驶证副本正面照");
        mOrigianlFragment.setAactivityType(OrderDetailContants.DRIVING);
        mCopyFragment.setAactivityType(OrderDetailContants.DRIVING);
        mOrigianlFragment.setSampleTitleViewVisibility(true);
        mCopyFragment.setSampleTitleViewVisibility(true);
        mOrigianlFragment.setClickAreaListener(true);
        mCopyFragment.setClickAreaListener(false);

        mParentView = (LinearLayout) findViewById(R.id.illegal_parent);
        mFailView = (RequestFailView) findViewById(R.id.illegal_no_data);
    }

    private View.OnClickListener clickProcessingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /*if (FormatCheck.checkName2(mNameView.getContentText(), IllegalProcessingActivity.this) == FormatCheck.NO_REAL_NAME) {
                return;
            } else if (FormatCheck.checkName2(mNameView.getContentText(), IllegalProcessingActivity.this) == FormatCheck.REAL_NAME_ERROR) {
                return;
            }*/

            if (!FormatCheck.checkRealName(mNameView.getContentText(), IllegalProcessingActivity.this)) {
                return;
            }

            if (FormatCheck.checkPhoneNumber(mPhoneView.getContentText(), IllegalProcessingActivity.this) == FormatCheck.NO_PHONE_NUMBER) {
                return;
            } else if (FormatCheck.checkPhoneNumber(mPhoneView.getContentText(), IllegalProcessingActivity.this) == FormatCheck.PHONE_NUMBER_FORMAT_ERROR) {
                return;
            }

            if (TextUtils.isEmpty(mOrigianlFragment.getmFilePath())) {
                ToastHelper.showYellowToast(IllegalProcessingActivity.this, "请上传行驶证正本");
                return;
            }

            if (TextUtils.isEmpty(mCopyFragment.getmFilePath())) {
                ToastHelper.showYellowToast(IllegalProcessingActivity.this, "请上传行驶证副本");
                return;
            }

            illegalOrderDetail = new IllegalOrderDetail();
            illegalOrderDetail.plateNumber = mPlateNumberView.getTwoText();
            illegalOrderDetail.username = mNameView.getContentText();
            illegalOrderDetail.phone = mPhoneView.getContentText();
            illegalOrderDetail.city = cityCode;
            illegalOrderDetail.cityName = cityName;
            illegalOrderDetail.provinceName = provinceName;
            illegalOrderDetail.hpzl = hpzl;
            illegalOrderDetail.engineno = engineno;
            illegalOrderDetail.classno = classno;
            if (!TextUtils.isEmpty(mOrigianlFragment.getmFilePath())) {
                illegalOrderDetail.drivingLicenseFile1 = mOrigianlFragment.getmFilePath().substring(7);
            }

            if (!TextUtils.isEmpty(mCopyFragment.getmFilePath())) {
                illegalOrderDetail.drivingLicenseFile2 = mCopyFragment.getmFilePath().substring(7);
            }
            DebugLog.i(TAG, "illegalOrderDetail:" + illegalOrderDetail.toString());
            DebugLog.i(TAG, "=================");
            DebugLog.i(TAG, "choseWZID:" + choseWZID);
            IllegalQueryControl.getInstance().postIllegalOrder(IllegalProcessingActivity.this, illegalOrderCallBack, illegalOrderDetail, choseWZID);
//            ActivitySwitchAround.toMyIllegalOrderActivity(IllegalProcessingActivity.this);
        }
    };

    /*private IllegalQueryControl.IllegalOrderHandler illegalOrderHandler = new IllegalQueryControl.IllegalOrderHandler() {
        @Override
        public void onIllegalOrderSucceed(String response) {
            ActivitySwitchAround.toIllegalProcessingSuccessActivity(IllegalProcessingActivity.this);
        }

        @Override
        public void onIllegalOrderFailed(boolean offLine) {
            ToastHelper.showRedToast(IllegalProcessingActivity.this, "订单提交失败!");
        }
    };*/

    private CallBackControl.CallBack<String> illegalOrderCallBack = new CallBackControl.CallBack<String>() {
        @Override
        public void onSuccess(String response) {
            DebugLog.i(TAG, "success");
            ActivitySwitchAround.toIllegalProcessingSuccessActivity(IllegalProcessingActivity.this);
        }

        @Override
        public void onFailed(boolean offLine) {
//            ToastHelper.showRedToast(IllegalProcessingActivity.this, "订单提交失败!");
            DebugLog.i(TAG, "failed");
            mParentView.setVisibility(View.GONE);
            mFailView.setVisibility(View.VISIBLE);
            mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
            mFailView.showFailedPage("重试", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mParentView.setVisibility(View.VISIBLE);
                    mFailView.setVisibility(View.GONE);
                    IllegalQueryControl.getInstance().postIllegalOrder(IllegalProcessingActivity.this, illegalOrderCallBack, illegalOrderDetail, choseWZID);
                }
            });
        }
    };

    private MyAuto imMyAuto = null;
    /*private CallBack<ArrayList<MyAuto>> autoInfoCallBack = new CallBack<ArrayList<MyAuto>>() {
        @Override
        public void onSuccess(ArrayList<MyAuto> response) {
            if (response != null && !response.isEmpty()) {
                int size = response.size();
                for (int i = 0; i < size; i++) {
                    if (plateNumber.equals(response.get(i).plateNumber)) {
                        imMyAuto = response.get(i);
                        return;
                    }
                }
              *//*  for (MyAuto myAuto : response) {
                    if (plateNumber.equals(myAuto.plateNumber)) {
                        imMyAuto = myAuto;
                        return;
                    }
                }*//*
            }
            if (imMyAuto != null) {
                DebugLog.i(TAG, "有值");
                if (!TextUtils.isEmpty(imMyAuto.ownName)) {
                    mNameView.setContentText(imMyAuto.ownName);
                } else {
                    mNameView.setContentText(UserInfoHelper.getInstance().getUser(IllegalProcessingActivity.this).fullname);
                }
                if (!TextUtils.isEmpty(imMyAuto.ownPhone)) {
                    mPhoneView.setContentText(imMyAuto.ownPhone);
                    mPhoneView.setContentText(UserInfoHelper.getInstance().getUser(IllegalProcessingActivity.this).phoneNumber);
                }
            } else {
                DebugLog.i(TAG, "fullname:" + UserInfoHelper.getInstance().getUser(IllegalProcessingActivity.this).fullname);
                DebugLog.i(TAG, "phoneNumber:" + UserInfoHelper.getInstance().getUser(IllegalProcessingActivity.this).phoneNumber);
                mNameView.setContentText(UserInfoHelper.getInstance().getUser(IllegalProcessingActivity.this).fullname);
                mPhoneView.setContentText(UserInfoHelper.getInstance().getUser(IllegalProcessingActivity.this).phoneNumber);
            }
        }

        @Override
        public void onFailed(boolean offLine) {

        }
    };*/

    /*private AutoInfoControl.AutoInfoHandler autoInfoHandler = new AutoInfoControl.AutoInfoHandler() {
        @Override
        public void onAutoInfoSucceed(ArrayList<MyAuto> myAutos) {
            if (myAutos != null && !myAutos.isEmpty()) {
                int size = myAutos.size();
                for (int i = 0; i < size; i++) {
                    if (plateNumber.equals(myAutos.get(i).plateNumber)) {
                        imMyAuto = myAutos.get(i);
                        return;
                    }
                }
            }
            if (imMyAuto != null) {
                if (!TextUtils.isEmpty(imMyAuto.ownName)) {
                    mNameView.setContentText(imMyAuto.ownName);
                } else {
                    mNameView.setContentText(UserInfoHelper.getInstance().getUser(IllegalProcessingActivity.this).fullname);
                }

                if (!TextUtils.isEmpty(imMyAuto.ownPhone)) {
                    mPhoneView.setContentText(imMyAuto.ownPhone);
                    mPhoneView.setContentText(UserInfoHelper.getInstance().getUser(IllegalProcessingActivity.this).phoneNumber);
                }
            } else {
                mNameView.setContentText(UserInfoHelper.getInstance().getUser(IllegalProcessingActivity.this).fullname);
                mPhoneView.setContentText(UserInfoHelper.getInstance().getUser(IllegalProcessingActivity.this).phoneNumber);
            }
        }

        @Override
        public void onAutoInfoFailed(boolean offLine) {

        }
    };*/

    @Override
    protected void onDestroy() {
        illegalOrderDetail = null;
        if (imIllegalSharedPreferencesHelper != null) {
            imIllegalSharedPreferencesHelper = null;
        }
        AutoInfoControl.getInstance().killInstance();
        IllegalQueryControl.getInstance().killInstance();
        super.onDestroy();
    }
}
