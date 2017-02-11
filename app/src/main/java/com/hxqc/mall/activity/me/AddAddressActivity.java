package com.hxqc.mall.activity.me;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hxqc.mall.activity.AppBackActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.fragment.CityChooseFragment;
import com.hxqc.mall.core.model.DeliveryAddress;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.views.ClearEditText;
import com.hxqc.util.DisplayTools;

import hxqc.mall.R;


/**
 * 说明:新增地址
 *
 * author: 吕飞
 * since: 2015-03-31
 * Copyright:恒信汽车电子商务有限公司
 */
@Deprecated
public class AddAddressActivity extends AppBackActivity implements CityChooseFragment.OnAreaChooseInteractionListener, View.OnClickListener {
    public boolean mIsEditAddressActivity;//是否是EditAddressActivity
    ClearEditText mConsigneeView;//收货人姓名
    ClearEditText mMobileView;
    MaterialEditText mAreaView;//选择区域
    MaterialEditText mDetailAddressView;
    Button mSaveView;
    DrawerLayout mDrawerLayoutView;
    DeliveryAddress mDeliveryAddress;
    FrameLayout mRightView;//右边浮层
    CityChooseFragment mCityChooseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        mDeliveryAddress = new DeliveryAddress();
        mConsigneeView = (ClearEditText) findViewById(R.id.consignee);
        mMobileView = (ClearEditText) findViewById(R.id.mobile);
        mAreaView = (MaterialEditText) findViewById(R.id.area);
        mDetailAddressView = (MaterialEditText) findViewById(R.id.detail_address);
        mSaveView = (Button) findViewById(R.id.save);
        mDrawerLayoutView = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRightView = (FrameLayout) findViewById(R.id.right);
        initEditText();
        mAreaView.setOnClickListener(this);
        mSaveView.setOnClickListener(this);
        initRight();
    }

    private void initEditText() {
        mConsigneeView.mEditTextView.setHint(R.string.me_consignee_hint);
        mConsigneeView.mEditTextView.setMaxEms(10);
        mMobileView.mEditTextView.setHint(R.string.me_mobile_hint);
        mMobileView.mEditTextView.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        mMobileView.mEditTextView.setMaxEms(11);
    }

    //    浮层初始化
    private void initRight() {
        mRightView.setLayoutParams(new DrawerLayout.LayoutParams(DisplayTools.getScreenWidth(this) * 8 / 9, DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.END));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mCityChooseFragment = new CityChooseFragment();
        mCityChooseFragment.setAreaChooseListener(this);
        fragmentTransaction.replace(R.id.right, mCityChooseFragment);
        fragmentTransaction.commit();
    }

    //省市区监听
    @Override
    public void OnAreaChooseInteraction(String provinces, String city, String district, String pID, String cID, String dID) {
        mDeliveryAddress.province = provinces;
        mDeliveryAddress.city = city;
        mDeliveryAddress.district = district;
        mDeliveryAddress.provinceID = pID;
        mDeliveryAddress.cityID = cID;
        mDeliveryAddress.districtID = dID;
        if (!TextUtils.isEmpty(provinces)) {
            mAreaView.setText(provinces + " " + city + " " + district);
            mAreaView.setTextColor(getResources().getColor(R.color.title_and_main_text));
        }
        mDrawerLayoutView.closeDrawers();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.area:
                mCityChooseFragment.setDefaultAreaData(mDeliveryAddress.province, mDeliveryAddress.city, mDeliveryAddress.district);
                mDrawerLayoutView.openDrawer(Gravity.RIGHT);
                break;
            case R.id.save:
                mDeliveryAddress.consignee = mConsigneeView.mEditTextView.getText().toString().trim();
                mDeliveryAddress.phone = mMobileView.mEditTextView.getText().toString().trim();
                mDeliveryAddress.detailedAddress = mDetailAddressView.getText().toString();
                if (canSave()) {
                    if (mIsEditAddressActivity) {
                        mApiClient.editAddress( mDeliveryAddress,
                                new DialogResponseHandler(this, true) {
                                    @Override
                                    public void onSuccess(String response) {
                                        DeliveryAddressActivity.sHasChanged = true;
                                        finish();
                                    }
                                });
                    } else {
                        mApiClient.addAddress( mDeliveryAddress, new DialogResponseHandler(this,
                                true) {
                            @Override
                            public void onSuccess(String response) {
                                DeliveryAddressActivity.sHasChanged = true;
                                finish();
                            }
                        });
                    }
                }
                break;
            default:
                break;
        }
    }

    //    验证是否能保存
    private boolean canSave() {
        return hasConsignee() && (FormatCheck.checkNickname(mDeliveryAddress.consignee, this)) && (FormatCheck.checkPhoneNumber(mDeliveryAddress.phone, this) == FormatCheck.CHECK_SUCCESS) && checkArea() && checkDetailAddress();
    }

    //验证收货人
    private boolean hasConsignee() {
        if (TextUtils.isEmpty(mDeliveryAddress.consignee)) {
            ToastHelper.showYellowToast(this, R.string.me_consignee_null);
            return false;
        } else {
            return true;
        }
    }

    //验证区域选择
    private boolean checkArea() {
        if (!TextUtils.isEmpty(mDeliveryAddress.district)) {
            return true;
        } else {
            ToastHelper.showYellowToast(this, R.string.me_area_hint);
            return false;
        }
    }

    //验证详细地址
    private boolean checkDetailAddress() {
        if (!TextUtils.isEmpty(mDeliveryAddress.detailedAddress)) {
            return true;
        } else {
            ToastHelper.showYellowToast(this, R.string.me_detail_address_null);
            return false;
        }
    }
}
