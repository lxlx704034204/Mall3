package com.hxqc.aroundservice.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.control.ChangeLicenseControl;
import com.hxqc.aroundservice.fragment.QueryProcessingPhotoFragmentV2;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.fragment.CityChooseFragment;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.usedcar.utils.OtherUtil;
import com.hxqc.util.DisplayTools;

import hxqc.mall.R;

/**
 * 驾驶证换证
 * Created by huangyi on 16/4/29.
 */
public class DriversLicenseChangeActivity extends BackActivity implements View.OnClickListener, CityChooseFragment.OnAreaChooseInteractionListener {

    DrawerLayout mDrawerView;
    MaterialEditText mNameView, mPhoneView, mAddressView, mAddressDetailView;
    String mProvince, mCity, mRegion;
    CityChooseFragment mAddressFragment;
    QueryProcessingPhotoFragmentV2 mOriginalFragment, mCopyFragment, mFrontFragment, mBackFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_license_change);

        mDrawerView = (DrawerLayout) findViewById(R.id.change_drawer);
        OtherUtil.setDrawerMode(mDrawerView);
        mNameView = (MaterialEditText) findViewById(R.id.change_name);
        mPhoneView = (MaterialEditText) findViewById(R.id.change_phone);
        mAddressView = (MaterialEditText) findViewById(R.id.change_address);
        mAddressView.setOnClickListener(this);
        mAddressDetailView = (MaterialEditText) findViewById(R.id.change_address_detail);
        findViewById(R.id.change_right).setLayoutParams(new DrawerLayout.LayoutParams(DisplayTools.getScreenWidth(this) / 5 * 4, DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT));
        mAddressFragment = new CityChooseFragment();
        mAddressFragment.setAreaChooseListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.change_right, mAddressFragment).commit();

        mOriginalFragment = (QueryProcessingPhotoFragmentV2) getSupportFragmentManager().findFragmentById(R.id.change_drivers_license_original);
        mCopyFragment = (QueryProcessingPhotoFragmentV2) getSupportFragmentManager().findFragmentById(R.id.change_drivers_license_copy);
        mFrontFragment = (QueryProcessingPhotoFragmentV2) getSupportFragmentManager().findFragmentById(R.id.change_id_card_front);
        mBackFragment = (QueryProcessingPhotoFragmentV2) getSupportFragmentManager().findFragmentById(R.id.change_id_card_back);

        mOriginalFragment.setTitle("驾驶证正本正面照");
        mOriginalFragment.setAactivityType(OrderDetailContants.DRIVER);
        mOriginalFragment.setClickAreaListener(true);
        mOriginalFragment.setSampleTitleViewVisibility(true);
        mOriginalFragment.setClickSampleTitleListener(this, R.drawable.ic_sample_license_origianl);
        mCopyFragment.setTitle("驾驶证副本正面照");
        mCopyFragment.setAactivityType(OrderDetailContants.DRIVER);
        mCopyFragment.setClickAreaListener(false);
        mCopyFragment.setSampleTitleViewVisibility(true);
        mCopyFragment.setClickSampleTitleListener(this, R.drawable.ic_sample_license_copy);
        mFrontFragment.setTitle("身份证正面照");
        mFrontFragment.setAactivityType(OrderDetailContants.ID);
        mFrontFragment.setClickAreaListener(true);
        mFrontFragment.setSampleTitleViewVisibility(true);
        mFrontFragment.changeImageView(true);
        mFrontFragment.setClickSampleTitleListener(this, R.drawable.ic_sample_identity_front);
        mBackFragment.setTitle("身份证反面照");
        mBackFragment.setAactivityType(OrderDetailContants.ID);
        mBackFragment.setClickAreaListener(false);
        mBackFragment.setSampleTitleViewVisibility(true);
        mBackFragment.changeImageView(true);
        mBackFragment.setClickSampleTitleListener(this, R.drawable.ic_sample_identity_back);
        findViewById(R.id.change_submit).setOnClickListener(this);

        //个人信息回显
        UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                mNameView.setText(meData.fullname);
                mPhoneView.setText(meData.phoneNumber);
            }

            @Override
            public void onFinish() {

            }
        }, false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.change_address) {
            mDrawerView.openDrawer(Gravity.RIGHT);

        } else if (id == R.id.change_submit) {
            String name = mNameView.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                ToastHelper.showYellowToast(this, "请输入正确的姓名");
                return;
            }

            String phone = mPhoneView.getText().toString().trim();
            if (FormatCheck.checkPhoneNumber(phone, this) != FormatCheck.CHECK_SUCCESS) return;

            if (TextUtils.isEmpty(mProvince) || TextUtils.isEmpty(mCity) || TextUtils.isEmpty(mRegion)) {
                ToastHelper.showYellowToast(this, "请选择省市区");
                return;
            }

            String detail = mAddressDetailView.getText().toString().trim();
            if (TextUtils.isEmpty(detail)) {
                ToastHelper.showYellowToast(this, "请输入详细地址");
                return;
            }

            String original = mOriginalFragment.getmFilePath();
            if (TextUtils.isEmpty(original)) {
                ToastHelper.showYellowToast(this, "请上传驾驶证正本正面照");
                return;
            } else {
                original = original.substring(7);
            }

            String copy = mCopyFragment.getmFilePath();
            if (TextUtils.isEmpty(copy)) {
                ToastHelper.showYellowToast(this, "请上传驾驶证副本正面照");
                return;
            } else {
                copy = copy.substring(7);
            }

            String front = mFrontFragment.getmFilePath();
            if (TextUtils.isEmpty(front)) {
                ToastHelper.showYellowToast(this, "请上传身份证正面照");
                return;
            } else {
                front = front.substring(7);
            }

            String back = mBackFragment.getmFilePath();
            if (TextUtils.isEmpty(back)) {
                ToastHelper.showYellowToast(this, "请上传身份证反面照");
                return;
            } else {
                back = back.substring(7);
            }

            submit(name, phone, detail, original, copy, front, back, view);
        }
    }


    private void submit(final String name, final String phone, final String address, final String path1, final String path2, final String path3, final String path4, final View view) {
        UserInfoHelper.getInstance().loginAction(this, new UserInfoHelper.OnLoginListener() {
            @Override
            public void onLoginSuccess() {
                /*new AroundServiceApiClient().postLicenceSubmit(DriversLicenseChangeActivity.this,
                        name, phone, mProvince, mCity, mRegion, address, path1, path2, path3, path4,
                        new LoadingAnimResponseHandler(DriversLicenseChangeActivity.this, true) {

                    @Override
                    public void onStart() {
                        super.onStart();
                        view.setEnabled(false);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        view.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(String response) {
                        ActivitySwitchAround.toIllegalProcessingSuccessActivity(DriversLicenseChangeActivity.this);
                    }
                });*/
                ChangeLicenseControl.getInstance().commitLicence(DriversLicenseChangeActivity.this, name, phone, mProvince, mCity, mRegion, address, path1, path2, path3, path4, new CallBackControl.CallBack<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ActivitySwitchAround.toIllegalProcessingSuccessActivity(DriversLicenseChangeActivity.this);
                    }

                    @Override
                    public void onFailed(boolean offLine) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_introduce, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_introduce) {
            ActivitySwitchBase.toH5Activity(this, "服务说明", ApiUtil.getAroundURL("/Servicedeclar/licence.html"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnAreaChooseInteraction(String provinces, String city, String district, String pID, String cID, String dID) {
        this.mProvince = provinces;
        this.mCity = city;
        this.mRegion = district;
        mAddressView.setText(mProvince + mCity + mRegion);
        mDrawerView.closeDrawer(Gravity.RIGHT);
    }
}
