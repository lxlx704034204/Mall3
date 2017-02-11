package com.hxqc.mall.usedcar.activity;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.MonitorScrollView;
import com.hxqc.mall.core.views.VoiceCaptchaView;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.ValidatorTech;
import com.hxqc.mall.core.views.vedit.manager.VWholeEditManager;
import com.hxqc.mall.launch.api.ApiClientAuthenticate;
import com.hxqc.mall.launch.view.CountdownButton;
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
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;


/**
 * @Author : 钟学东
 * @Since : 2015-09-21
 * FIXME
 * Todo
 */
public class PlatformSellCarActivity extends NoBackActivity implements ChooseFragment.ChooseListener, View.OnClickListener, LookCarFragment.ChooseAreaListener, SellCarItem.SellCarItemListener, MonitorScrollView.ScrollViewListener,VoiceCaptchaView.VoiceCaptchaListener {
    EditTextValidatorView mMobileView;
    Toolbar mToolbar;
    DrawerLayout mDrawerLayoutView;
    FrameLayout mRightView;//右边浮层
    ChooseFragment mChooseFragment;
    LookCarFragment mLookCarFragment;
    SellCarEditText mNameView;
    SellCarItem mCarView, mAreaView;
    RemarkView mOtherView;
    Button mSubmitView;
    TextView mPhoneView;
    CountdownButton mGetCaptchaView;
    SellCarEditText mCaptchaView;
    UsedCarApiClient mUsedCarApiClient;
    String brand = "", series = "", brandId = "", seriesId = "", province = "", city = "", provinceId = "", cityId = "";
    ArrayList<AreaModel> areaModels = new ArrayList<>();
    UsedCarSPHelper mUsedCarSPHelper;
    VWholeEditManager mVWholeEditManager;
    MonitorScrollView mScrollView;
    VoiceCaptchaView mVoiceCaptchaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_sell);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        mUsedCarApiClient = new UsedCarApiClient();
        mUsedCarSPHelper = new UsedCarSPHelper(this);
        areaModels = mUsedCarSPHelper.getPCR();
        mVWholeEditManager = new VWholeEditManager(this);
        initView();
        OtherUtil.setDrawerMode(mDrawerLayoutView);
    }

    private void initView() {
        mDrawerLayoutView = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRightView = (FrameLayout) findViewById(R.id.right);
        mNameView = (SellCarEditText) findViewById(R.id.item_name);
        mCaptchaView = (SellCarEditText) findViewById(R.id.item_captcha);
        mMobileView = (EditTextValidatorView) findViewById(R.id.mobile);
        mMobileView.addValidator(ValidatorTech.PhoneNumber);
        mCarView = (SellCarItem) findViewById(R.id.item_car);
        mScrollView = (MonitorScrollView) findViewById(R.id.scroll_view);
        mScrollView.setScrollViewListener(mToolbar,this);
        mScrollView.setHeight(DisplayTools.dip2px(this, 214));
        mGetCaptchaView = (CountdownButton) findViewById(R.id.get_captcha);
        mCarView.setSellCarItemListener(this);
        mAreaView = (SellCarItem) findViewById(R.id.item_area);
        mAreaView.setSellCarItemListener(this);
        mOtherView = (RemarkView) findViewById(R.id.other);
        mVoiceCaptchaView = (VoiceCaptchaView) findViewById(R.id.voice_captcha);
        mChooseFragment = new ChooseFragment();
        mChooseFragment.showModel(false);
        mChooseFragment.setListener(this);
        mLookCarFragment = new LookCarFragment();
        mLookCarFragment.setChooseAreaListener(this);
        mSubmitView = (Button) findViewById(R.id.bt_submit);
        mSubmitView.setOnClickListener(this);
        mPhoneView = (TextView) findViewById(R.id.tv_call_number);
        mPhoneView.setOnClickListener(this);
        mVoiceCaptchaView.setVoiceCaptchaListener(this);
        mGetCaptchaView.setOnClickListener(this);
        OtherUtil.setVisible(mCaptchaView, !UserInfoHelper.getInstance().isLogin(this));
        OtherUtil.setVisible(mGetCaptchaView, !UserInfoHelper.getInstance().isLogin(this));
        OtherUtil.setVisible(mVoiceCaptchaView, !UserInfoHelper.getInstance().isLogin(this));
        UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                if (meData != null && !TextUtils.isEmpty(meData.fullname)) {
                    mNameView.getInputView().setText(meData.fullname);
                }
            }

            @Override
            public void onFinish() {

            }
        }, false);
        if (UserInfoHelper.getInstance().isLogin(this)) {
            mMobileView.setText(UserInfoHelper.getInstance().getPhoneNumber(this));
            mMobileView.setEnabled(false);
            mVWholeEditManager.addEditView(new EditTextValidatorView[]{mNameView.getInputView()});
        } else {
//            mVoiceCaptchaView.setMobileView(mMobileView);
            mVWholeEditManager.addEditView(new EditTextValidatorView[]{mNameView.getInputView(), mMobileView, mCaptchaView.getInputView()});
        }
        mRightView.setLayoutParams(new DrawerLayout.LayoutParams(DisplayTools.getScreenWidth(this) * 4 / 5, DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT));
    }


    @Override
    public void completeChoose(String brand, String series, String model, String brandId, String seriesId, String modelId) {
        this.brand = brand;
        this.series = series;
        this.brandId = brandId;
        this.seriesId = seriesId;
        mCarView.setResult(brand + series);
        mDrawerLayoutView.closeDrawer(Gravity.RIGHT);
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_call_number) {
            OtherUtil.callHXService(this);
        } else if (i == R.id.get_captcha) {
            String mobile = mMobileView.getText().toString().trim();
            if (FormatCheck.checkPhoneNumber(mobile, this) == FormatCheck.CHECK_SUCCESS) {
                mGetCaptchaView.identifyingStart3(mobile, ApiClientAuthenticate.CAPTCHA_PLATFORM);
            }
        } else if (i == R.id.bt_submit) {
            if (mVWholeEditManager.validate()) {
                mSubmitView.setClickable(false);
                mUsedCarApiClient.platformSell(mMobileView.getText().toString().trim(), brandId, seriesId, provinceId, cityId, mOtherView.getRemarkView().getText().toString().trim(), mNameView.getInputView().getText().toString().trim(), mCaptchaView.getInputView().getText().toString().trim(), new LoadingAnimResponseHandler(PlatformSellCarActivity.this) {
                    @Override
                    public void onSuccess(String response) {
                        ToastHelper.showGreenToast(PlatformSellCarActivity.this, "提交预约成功");
                        ToastHelper.toastFinish(PlatformSellCarActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSubmitView.setClickable(true);
                    }
                });
            }
        }
    }

    private void chooseCity() {
        OtherUtil.closeSoftKeyBoard(this, mDrawerLayoutView);
        initRight(mLookCarFragment);
        mDrawerLayoutView.openDrawer(Gravity.RIGHT);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onBackPressed() {
        if (mDrawerLayoutView.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayoutView.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    private void initRight(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.right, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSubmitComplete(String province1, String city1, String region1, String provinceId, String provinceCode, String cityId, String cityCode, String regionId, ArrayList<AreaModel> areaModels_p1, ArrayList<CityModel> areaModels_c1, ArrayList<RegionModel> areaModels_s1) {
        province = province1;
        city = city1;
        this.provinceId = provinceCode;
        this.cityId = cityCode;
        mAreaView.setResult(province + city);
        mDrawerLayoutView.closeDrawer(Gravity.RIGHT);
    }

    @Override
    public void initData() {
        mLookCarFragment.initDate(areaModels, "2");
    }

    @Override
    public void sellCarItemClick(View view) {
        int i = view.getId();
        if (i == R.id.item_car) {
            initRight(mChooseFragment);
            OtherUtil.closeSoftKeyBoard(this, mDrawerLayoutView);
            mDrawerLayoutView.openDrawer(Gravity.RIGHT);
        } else if (i == R.id.item_area) {
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
    }

    @Override
    public void getVoiceCaptcha() {
        mUsedCarApiClient.getVoiceCaptcha(mMobileView.getText().toString().trim(), UsedCarApiClient.VC_PLATFORM, mVoiceCaptchaView.getVoiceCaptchaResponseHandler());
    }

    @Override
    public String getPhoneNumber() {
        return mMobileView.getText().toString().trim();
    }

    @Override
    public void onScrollChange(float f1) {
        mToolbar.getBackground().setAlpha((int) (f1 * 255));
    }

    @Override
    public void moveDown() {
    }

    @Override
    public void moveUp() {
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
