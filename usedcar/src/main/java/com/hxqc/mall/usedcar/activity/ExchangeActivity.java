package com.hxqc.mall.usedcar.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.views.dialog.NoCancelDialog;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.manager.VWholeEditManager;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.fragment.ChooseFragment;
import com.hxqc.mall.usedcar.fragment.LookCarFragment;
import com.hxqc.mall.usedcar.model.AreaModel;
import com.hxqc.mall.usedcar.model.CityModel;
import com.hxqc.mall.usedcar.model.RegionModel;
import com.hxqc.mall.usedcar.utils.OtherUtil;
import com.hxqc.mall.usedcar.utils.UsedCarSPHelper;
import com.hxqc.mall.usedcar.views.RemarkView;
import com.hxqc.mall.usedcar.views.SellCar.SellCarEditText;
import com.hxqc.mall.usedcar.views.SellCar.SellCarItem;
import com.hxqc.mall.usedcar.views.SellCar.SellCarItemChooseDate;
import com.hxqc.mall.usedcar.views.UsedCarDrawer;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 说明:旧车置换
 *
 * @author: 吕飞
 * @since: 2016-07-22
 * Copyright:恒信汽车电子商务有限公司
 */
public class ExchangeActivity extends NoBackActivity implements LookCarFragment.ChooseAreaListener, View.OnClickListener, ChooseFragment.ChooseListener, ChooseFragment.ChooseIntentListener, SellCarItem.SellCarItemListener {
    String mProvince = "";
    String mCity = "";
    String mProvinceId = "";
    String mCityId = "";
    String mSellBrand = "";
    String mSellSeries = "";
    String mIntentBrand = "";
    String mIntentSeries = "";
    String mIntentModel = "";
    ChooseFragment mSellBrandFragment;
    ChooseFragment mBuyBrandFragment;
    LookCarFragment mLookCarFragment;
    UsedCarApiClient mUsedCarApiClient;
    UsedCarSPHelper mUsedCarSPHelper;
    Toolbar mToolbar;
    UsedCarDrawer mUsedCarDrawerView;
    FrameLayout mRightView;
    SellCarItem mBuyCarBrandView, mSellBrandView, mAreaView;
    SellCarEditText mNameView, mMileView, mMobileView;
    RadioButton mMaleView, mFemaleView;
    SellCarItemChooseDate mOnCardView;
    RemarkView mRemark1View;
    RemarkView mRemark2View;
    Button mSubmitView;
    TextView mConsultView;
    ArrayList<AreaModel> areaModels = new ArrayList<>();
    VWholeEditManager mVWholeEditManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsedCarApiClient = new UsedCarApiClient();
        mUsedCarSPHelper = new UsedCarSPHelper(this);
        setContentView(R.layout.activity_exchange);
        areaModels = mUsedCarSPHelper.getPCR();
        mVWholeEditManager = new VWholeEditManager(this);
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("旧车置换");
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
        mUsedCarDrawerView = (UsedCarDrawer) findViewById(R.id.drawer_layout);
        mRightView = (FrameLayout) findViewById(R.id.right);
        mBuyCarBrandView = (SellCarItem) findViewById(R.id.buy_brand);
        mNameView = (SellCarEditText) findViewById(R.id.name);
        mMaleView = (RadioButton) findViewById(R.id.male);
        mFemaleView = (RadioButton) findViewById(R.id.female);
        mMobileView = (SellCarEditText) findViewById(R.id.mobile);
        mSellBrandView = (SellCarItem) findViewById(R.id.sell_brand);
        mAreaView = (SellCarItem) findViewById(R.id.area);
        mOnCardView = (SellCarItemChooseDate) findViewById(R.id.on_card);
        mMileView = (SellCarEditText) findViewById(R.id.mile);
        mRemark1View = (RemarkView) findViewById(R.id.remark_1);
        mRemark2View = (RemarkView) findViewById(R.id.remark_2);
        mSubmitView = (Button) findViewById(R.id.submit);
        mConsultView = (TextView) findViewById(R.id.consult);
        mVWholeEditManager.addEditView(new EditTextValidatorView[]{mNameView.getInputView(), mMobileView.getInputView(), mSellBrandView.getChooseResultView(), mAreaView.getChooseResultView(), mBuyCarBrandView.getChooseResultView()});
        mSellBrandFragment = new ChooseFragment();
        mSellBrandFragment.showModel(false);
        mSellBrandFragment.setListener(this);
        mBuyBrandFragment = new ChooseFragment();
        mBuyBrandFragment.setType(ChooseFragment.INTENT);
        mLookCarFragment = new LookCarFragment();
        mLookCarFragment.setChooseAreaListener(this);
        mBuyBrandFragment.setIntentListener(this);
        mSubmitView.setOnClickListener(this);
        mConsultView.setOnClickListener(this);
        mBuyCarBrandView.setSellCarItemListener(this);
        mSellBrandView.setSellCarItemListener(this);
        mAreaView.setSellCarItemListener(this);
        mRightView.setLayoutParams(new DrawerLayout.LayoutParams(DisplayTools.getScreenWidth(this) * 4 / 5, DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT));
        if (UserInfoHelper.getInstance().isLogin(this) && !TextUtils.isEmpty(UserInfoHelper.getInstance().getPhoneNumber(this))) {
            mMobileView.setText(UserInfoHelper.getInstance().getPhoneNumber(this));
            mMobileView.getInputView().setEnabled(false);
        }
        UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                if (meData != null) {
                    if (!TextUtils.isEmpty(meData.fullname)) {
                        mNameView.setText(meData.fullname);
                    }
                    mMaleView.setChecked(!meData.gender.equals("2"));
                    mFemaleView.setChecked(meData.gender.equals("2"));
                } else {
                    mMaleView.setChecked(true);
                }
            }

            @Override
            public void onFinish() {

            }
        }, false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void sellBrandChoose() {
        initRight(mSellBrandFragment);
        mUsedCarDrawerView.open(mSellBrandView.getChooseResultView());
    }

    private void areaChoose() {
        if (areaModels != null && areaModels.size() > 0) {
            chooseCity();
        } else {
            mUsedCarApiClient.getProvinceCity(new LoadingAnimResponseHandler(this) {
                @Override
                public void onSuccess(String response) {
                    areaModels = JSONUtils.fromJson(response, new TypeToken<ArrayList<AreaModel>>() {
                    });
                    if (areaModels != null && areaModels.size() > 0) {
                        mUsedCarSPHelper.savePCR(areaModels);
                        chooseCity();
                    }
                }
            });
        }
    }

    private void buyBrandChoose() {
        initRight(mBuyBrandFragment);
        mUsedCarDrawerView.open(mBuyCarBrandView.getChooseResultView());
    }

    @Override
    public void onClick(View v) {
        OtherUtil.closeSoftKeyBoard(this, v);
        int i = v.getId();
        if (i == R.id.submit) {
            if (mVWholeEditManager.validate()) {
                mUsedCarApiClient.exchange(mNameView.getInputView().getText().toString().trim(), getGender(), mMobileView.getInputView().getText().toString().trim(), mSellBrand, mSellSeries, mProvince, mProvinceId, mCity, mCityId, mOnCardView.getResult(), mMileView.getInputView().getText().toString().trim(), mRemark1View.getRemarkView().toString().trim(), mIntentBrand, mIntentSeries, mIntentModel, mRemark2View.getRemarkView().toString().trim(), new LoadingAnimResponseHandler(this) {
                    @Override
                    public void onSuccess(String response) {
                        new NoCancelDialog(ExchangeActivity.this, "提交成功", "您已提交置换申请，我们会尽快与您联系！") {
                            @Override
                            protected void doNext() {
                                finish();
                            }
                        }.show();
                    }
                });
            }
        } else if (i == R.id.consult) {
            OtherUtil.callHXService(this);
        }
    }

    private void chooseCity() {
        mUsedCarDrawerView.open(mAreaView.getChooseResultView());
        initRight(mLookCarFragment);
    }

    private String getGender() {
        if (mMaleView.isChecked()) {
            return "1";
        } else {
            return "2";
        }
    }

    @Override
    public void completeChoose(String brand, String series, String model, String brandId, String seriesId, String modelId) {
        if (!TextUtils.isEmpty(brand)) {
            mSellBrandView.setResult(brand + " " + series);
            mSellBrand = brand;
            mSellSeries = series;
        }
        mUsedCarDrawerView.closeDrawer(Gravity.RIGHT);
        mSellBrandView.getChooseResultView().validate();
    }

    private void initRight(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.right, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void completeIntentChoose(String brand, String series, String model) {
        if (!TextUtils.isEmpty(brand)) {
            mBuyCarBrandView.setResult(model);
            mIntentBrand = brand;
            mIntentSeries = series;
            mIntentModel = model;
        }
        mUsedCarDrawerView.closeDrawer(Gravity.RIGHT);
        mBuyCarBrandView.getChooseResultView().validate();
    }

    @Override
    public void onBackPressed() {
        if (mUsedCarDrawerView.isDrawerOpen(Gravity.RIGHT)) {
            mUsedCarDrawerView.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSubmitComplete(String province1, String city1, String region1, String provinceId, String provinceCode, String cityId, String cityCode, String regionId, ArrayList<AreaModel> areaModels_p1, ArrayList<CityModel> areaModels_c1, ArrayList<RegionModel> areaModels_s1) {
        mProvince = province1;
        mCity = city1;
        mProvinceId = provinceId;
        mCityId = cityId;
        mAreaView.setResult(province1 + city1);
        mUsedCarDrawerView.closeDrawer(Gravity.RIGHT);
        mAreaView.getChooseResultView().validate();
    }

    @Override
    public void initData() {
        mLookCarFragment.initDate(areaModels, "2");
    }

    @Override
    public void sellCarItemClick(View view) {
        int i = view.getId();
        if (i == R.id.sell_brand) {
            sellBrandChoose();
        } else if (i == R.id.area) {
            areaChoose();
        } else if (i == R.id.buy_brand) {
            buyBrandChoose();
        }
    }
}
