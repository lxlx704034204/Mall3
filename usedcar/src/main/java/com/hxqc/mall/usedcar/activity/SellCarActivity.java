package com.hxqc.mall.usedcar.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.auto.view.NewPlateNumberLayout;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.WidgetController;
import com.hxqc.mall.core.views.dialog.ListDialog;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.manager.VWholeEditManager;
import com.hxqc.mall.core.views.vedit.tech.UsedCarPlateNumberValidator;
import com.hxqc.mall.core.views.vedit.tech.UsedCarPlateNumberValidator2;
import com.hxqc.mall.core.views.vedit.tech.UsedCarPlateNumberValidator3;
import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.SellCarAdapter;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.fragment.ChooseFragment;
import com.hxqc.mall.usedcar.fragment.ChooseGearboxFragment;
import com.hxqc.mall.usedcar.fragment.ChooseLevelFragment;
import com.hxqc.mall.usedcar.fragment.LookCarFragment;
import com.hxqc.mall.usedcar.fragment.SellCarAddFragment;
import com.hxqc.mall.usedcar.model.AreaModel;
import com.hxqc.mall.usedcar.model.CityModel;
import com.hxqc.mall.usedcar.model.InfoByModel;
import com.hxqc.mall.usedcar.model.RegionModel;
import com.hxqc.mall.usedcar.model.SellCarChoose;
import com.hxqc.mall.usedcar.model.SellCarDetailModel;
import com.hxqc.mall.usedcar.model.ValuationArgument;
import com.hxqc.mall.usedcar.utils.OtherUtil;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.mall.usedcar.utils.UsedCarSPHelper;
import com.hxqc.mall.usedcar.views.SellCar.SellCarBrand;
import com.hxqc.mall.usedcar.views.SellCar.SellCarColor;
import com.hxqc.mall.usedcar.views.SellCar.SellCarEditText;
import com.hxqc.mall.usedcar.views.SellCar.SellCarItem;
import com.hxqc.mall.usedcar.views.SellCar.SellCarItemChooseDate;
import com.hxqc.mall.usedcar.views.SellCar.SellCarReadme;
import com.hxqc.mall.usedcar.views.UsedCarDrawer;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.GridViewNoSlide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.nereo.multi_image_selector.MultiImageSelector;


/**
 * 说明:个人卖车
 *
 * @author: 吕飞
 * @since: 2016-05-20
 * Copyright:恒信汽车电子商务有限公司
 */
public class SellCarActivity extends NoBackActivity implements SellCarAddFragment.onConfirmListener, AdapterView.OnItemClickListener, View.OnClickListener, ChooseFragment.ChooseListener,
        SellCarBrand.OnSellCarBrandClickListener, ChooseGearboxFragment.ChooseGearboxListener, ChooseLevelFragment.ChooseLevelListener, SellCarItem.SellCarItemListener, LookCarFragment.ChooseAreaListener {
    public static final int README = 107;
    public boolean chosenCover = false;//是否选过封面
    public int cover = 0;//封面
    private VWholeEditManager mVWholeEditManager;
    private ChooseFragment mChooseFragment;
    ChooseLevelFragment mChooseLevelFragment;
    ChooseGearboxFragment mChooseGearboxFragment;
    SellCarAddFragment mSellCarAddFragment;
    LookCarFragment mLocationFragment, mLookCarFragment; //车牌，看车地点
    UsedCarApiClient mUsedCarApiClient;
    UsedCarSPHelper mUsedCarSPHelper;
    Toolbar mToolbar;
    UsedCarDrawer mDrawerLayoutView;
    FrameLayout mRightView;
    SellCarBrand mBrandView;
    SellCarColor mColorView;
    SellCarItem mLocationView, mLevelView, mGearboxView, mLookLocationView;
    SellCarEditText mDisplacementView, mMileView, mNewCarPriceView, mPriceView, mContactView, mMobileView;
    NewPlateNumberLayout mPlateNumView;
    SellCarReadme mReadmeView;
    GridViewNoSlide mGridView;
    LinearLayout mIDPhotoView;
    RelativeLayout mTextIDPhotoView;
    ImageView mDrivingView, mRegistrationView, mInvoiceView, mDrivingDeleteView, mRegistrationDeleteView, mInvoiceDeleteView, mArrowView;
    ImageView[] mLicenseViews;
    ImageView[] mDeleteViews;
    TextView mTvIDPhotoView;
    Button mSubmitView;
    SellCarItemChooseDate mOnCardView, mInspectionView, mStrongView, mCommercialView, mWarrantyView;
    ValuationArgument mValuationArgument; //估价详情传来的数据
    SellCarDetailModel mSellCarDetailModel; //卖车详情传来的数据
    List<ImageItem> mDataList = new ArrayList<>();
    SellCarAdapter mAdapter;
    ArrayList<ImageItem> imageItems;
    String carSourceNo = "", brandId = "", seriesId = "", modelId = "", addBrand = "", addSeries = "", addModel = "", color = "",
            province = "", city = "", range = "", firstOnCard = "", estimatePrice = "", provinceId = "", cityId = "", level, gearbox,
            displacement, license2_show = "", license1_show = "", coverName, contacts = "", phoneNumber = "", owners = "";
    String inspection_date = "";//年检有效期
    String sali_date = "";//交强险到期时间
    String location_province = "";//看车省
    String location_city = "";//看车市
    String location_region = "";//看车区
    String location_provinceId = "";//看车省
    String location_cityId = "";//看车市
    String location_regionId = "";//看车区
    String warranty_date = "";//质保到期时间
    String insurance_date = "";//商业险
    String new_car_price = "";//新车价
    String car_license_no = "";//车牌号
    String[] mLicenses = new String[]{"", "", ""};
    String license_show = "";//用来截取文件名
    String delete1 = ""; //adapter返回的delete
    String delete2 = ""; //证件照的删除文件名
    String delete = ""; //总的删除文件名
    ArrayList<AreaModel> areaModels_p;//省
    ArrayList<CityModel> areaModels_c;//市
    ArrayList<RegionModel> areaModels_s;//区
    SellCarChoose sellCarChoose;
    boolean isShowID = false;
    ArrayList<AreaModel> areaModels = new ArrayList<>();
    String mLookCarFragmentFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsedCarApiClient = new UsedCarApiClient();
        mUsedCarSPHelper = new UsedCarSPHelper(this);
        setContentView(R.layout.activity_sell_car);
        mVWholeEditManager = new VWholeEditManager(this);
        initView();
        initDate();
        try {
            mValuationArgument = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelable(UsedCarActivitySwitcher.VALUATION_ARGUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mSellCarDetailModel = (SellCarDetailModel) getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getSerializable("sellCarDetailModel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mValuationArgument != null) {
            fillValuationData();
            setModelInfo();
        } else if (mSellCarDetailModel != null) {
            fillDetailData();
        }
    }

    private void fillDetailData() {
        chosenCover = true;
        displacement = mSellCarDetailModel.displacement;
        level = mSellCarDetailModel.level;
        gearbox = mSellCarDetailModel.gearbox;
        new_car_price = mSellCarDetailModel.new_car_price;
        carSourceNo = mSellCarDetailModel.car_source_no;
        brandId = mSellCarDetailModel.brand_id;
        seriesId = mSellCarDetailModel.serie_id;
        modelId = mSellCarDetailModel.model_id;
        addBrand = mSellCarDetailModel.addbrand;
        addSeries = mSellCarDetailModel.addserie;
        addModel = mSellCarDetailModel.addmodel;
        color = mSellCarDetailModel.car_color;
        province = mSellCarDetailModel.province;
        provinceId = mSellCarDetailModel.province_id;
        city = mSellCarDetailModel.city;
        cityId = mSellCarDetailModel.city_id;
        range = mSellCarDetailModel.car_mileage;
        firstOnCard = mSellCarDetailModel.first_on_card;
        estimatePrice = mSellCarDetailModel.estimate_price;
        contacts = mSellCarDetailModel.contacts;
        phoneNumber = mSellCarDetailModel.phone_num;
        owners = mSellCarDetailModel.owners;
        inspection_date = mSellCarDetailModel.inspection_date;
        sali_date = mSellCarDetailModel.sali_date;
        warranty_date = mSellCarDetailModel.warranty_date;
        insurance_date = mSellCarDetailModel.insurance_date;
        location_province = mSellCarDetailModel.location_province;
        location_provinceId = mSellCarDetailModel.location_province_id;
        location_city = mSellCarDetailModel.location_city;
        location_cityId = mSellCarDetailModel.location_city_id;
        location_region = mSellCarDetailModel.location_region;
        location_regionId = mSellCarDetailModel.location_region_id;
        car_license_no = mSellCarDetailModel.car_license_no;
        if (!mSellCarDetailModel.brand.equals("") || !mSellCarDetailModel.serie.equals("")) {
            mBrandView.setBrandString(mSellCarDetailModel.brand + mSellCarDetailModel.serie + mSellCarDetailModel.model);
        } else {
            String addbrand = "";
            String addserie = "";
            String addmodel = "";
            if (!mSellCarDetailModel.addbrand.equals("")) {
                addbrand = mSellCarDetailModel.addbrand;
            }
            if (!mSellCarDetailModel.addserie.equals("")) {
                addserie = mSellCarDetailModel.addserie;
            }
            if (!mSellCarDetailModel.addmodel.equals("")) {
                addmodel = mSellCarDetailModel.addmodel;
            }
            mSellCarAddFragment.setDate(addbrand, addserie, addmodel);
            mBrandView.setBrandString(addbrand + " " + addserie + " " + addmodel);
        }
        mLevelView.setResult(mSellCarDetailModel.level);
        mLevelView.getChooseResultView().setEnabled(mSellCarDetailModel.level_editable.equals("1"));
        mGearboxView.setResult(mSellCarDetailModel.gearbox);
        mGearboxView.getChooseResultView().setEnabled(mSellCarDetailModel.gearbox_editable.equals("1"));
        mDisplacementView.setText(mSellCarDetailModel.displacement);
        mDisplacementView.getInputView().setEnabled(mSellCarDetailModel.displacement_editable.equals("1"));
        mNewCarPriceView.setText(mSellCarDetailModel.new_car_price);
        mNewCarPriceView.getInputView().setEnabled(mSellCarDetailModel.new_car_price_editable.equals("1"));
        mColorView.setResult(mSellCarDetailModel.car_color);
        mLocationView.setResult(mSellCarDetailModel.province + mSellCarDetailModel.city);
        mMileView.setText(mSellCarDetailModel.car_mileage);
        mOnCardView.setResult(mSellCarDetailModel.first_on_card);
        mPriceView.setText(mSellCarDetailModel.estimate_price);
        mContactView.setText(mSellCarDetailModel.contacts);
        mMobileView.setText(mSellCarDetailModel.phone_num);
        mReadmeView.setRemarkText(mSellCarDetailModel.owners);
        mInspectionView.setResult(mSellCarDetailModel.inspection_date);
        mStrongView.setResult(mSellCarDetailModel.sali_date);
        mCommercialView.setResult(mSellCarDetailModel.insurance_date);
        mWarrantyView.setResult(mSellCarDetailModel.warranty_date);
        mLookLocationView.setResult(location_province + location_city + location_region);
        mPlateNumView.setPlateNumber(mSellCarDetailModel.car_license_no, false);
        ArrayList<SellCarDetailModel.ImageEntity> imageUrls = mSellCarDetailModel.image;
        mDataList.clear();
        for (SellCarDetailModel.ImageEntity imageUrl : imageUrls) {
            ImageItem imageItem = new ImageItem("");
            imageItem.sourcePath = imageUrl.getPath();
            imageItem.thumbnailPath = imageUrl.getSmall_path();
            imageItem.setTitle(imageUrl.getName());
            mDataList.add(imageItem);
        }
        coverName = mSellCarDetailModel.cover;
        for (int i = 0; i < 5; i++) {
            if (!TextUtils.isEmpty(mDataList.get(i).getTitle()) && mDataList.get(i).getTitle().equals(coverName)) {
                cover = i;
                mDataList.get(i).isFigure = true;
                break;
            }
        }
        if (!mSellCarDetailModel.license.equals("")) {
            mDrivingDeleteView.setVisibility(View.VISIBLE);
            license_show = mSellCarDetailModel.license;
            ImageUtil.setImage(this, mDrivingView, mSellCarDetailModel.license);
        }
        if (!mSellCarDetailModel.license1.equals("")) {
            mRegistrationDeleteView.setVisibility(View.VISIBLE);
            license1_show = mSellCarDetailModel.license1;
            ImageUtil.setImage(this, mRegistrationView, mSellCarDetailModel.license1);
        }

        if (!mSellCarDetailModel.license2.equals("")) {
            mInvoiceDeleteView.setVisibility(View.VISIBLE);
            license2_show = mSellCarDetailModel.license2;
            ImageUtil.setImage(this, mInvoiceView, mSellCarDetailModel.license2);
        }
        if (!mSellCarDetailModel.license.equals("") || !mSellCarDetailModel.license1.equals("") || !mSellCarDetailModel.license2.equals("")) {
            isShowID = true;
            mIDPhotoView.setVisibility(View.VISIBLE);
            mTvIDPhotoView.setText("收起证件照");
            mArrowView.setImageResource(R.mipmap.ic_sellthecar_contraction);
        }
    }

    private void fillValuationData() {
        mBrandView.setBrandString(mValuationArgument.title);
        mMileView.setText(mValuationArgument.range);
        mOnCardView.setResult(mValuationArgument.licensing_date);
        mNewCarPriceView.setText(mValuationArgument.new_car_price);
        brandId = mValuationArgument.brandId;
        seriesId = mValuationArgument.seriesId;
        modelId = mValuationArgument.modelId;
        range = mValuationArgument.range;
        firstOnCard = mValuationArgument.licensing_date;
        new_car_price = mValuationArgument.new_car_price;
    }


    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(getResources().getString(R.string.activity_sellcar));
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
        mDrawerLayoutView = (UsedCarDrawer) findViewById(R.id.drawer_layout);
        mRightView = (FrameLayout) findViewById(R.id.right);
        mBrandView = (SellCarBrand) findViewById(R.id.item_brand);
        mColorView = (SellCarColor) findViewById(R.id.item_color);
        mLocationView = (SellCarItem) findViewById(R.id.item_location);
        mLevelView = (SellCarItem) findViewById(R.id.item_level);
        mGearboxView = (SellCarItem) findViewById(R.id.item_gearbox);
        mDisplacementView = (SellCarEditText) findViewById(R.id.item_displacement);
        mNewCarPriceView = (SellCarEditText) findViewById(R.id.item_new_car_price);
        mMileView = (SellCarEditText) findViewById(R.id.item_mile);
        mPlateNumView = (NewPlateNumberLayout) findViewById(R.id.item_plate_num);
        mPlateNumView.setScrollView((ScrollView) findViewById(R.id.scroll_view));
        mPlateNumView.setNextView(mNewCarPriceView);
        mPlateNumView.setPlateNumber("", false);
        mPlateNumView.getCityEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPlateNumView.getNumberEditText().clearValidators();
                if (s.length() == 0) {
                    mPlateNumView.getNumberEditText().addValidator(new UsedCarPlateNumberValidator("请输入正确的车牌号", "[A-Z_0-9]{5}$"));
                } else if (s.length() == 1) {
                    mPlateNumView.getNumberEditText().addValidator(new UsedCarPlateNumberValidator3("请输入正确的车牌号", ""));
                    mPlateNumView.initKeyboard(com.hxqc.mall.core.R.xml.licence_new_letter);
                } else if (s.length() == 2) {
                    mPlateNumView.getNumberEditText().addValidator(new UsedCarPlateNumberValidator2("请输入正确的车牌号", "[A-Z_0-9]{5}$"));
                    mPlateNumView.jumpNumKerboard();
                }
            }
        });

        mOnCardView = (SellCarItemChooseDate) findViewById(R.id.item_on_card);
        mPriceView = (SellCarEditText) findViewById(R.id.item_price);
        mContactView = (SellCarEditText) findViewById(R.id.item_contact);
        mMobileView = (SellCarEditText) findViewById(R.id.item_mobile);
        mLookLocationView = (SellCarItem) findViewById(R.id.item_look_location);
        mReadmeView = (SellCarReadme) findViewById(R.id.item_readme);
        mGridView = (GridViewNoSlide) findViewById(R.id.grid_view);
        mIDPhotoView = (LinearLayout) findViewById(R.id.ll_id_photo);
        mTextIDPhotoView = (RelativeLayout) findViewById(R.id.rl_id_photo);
        mDrivingView = (ImageView) findViewById(R.id.iv_driving_license);
        mRegistrationView = (ImageView) findViewById(R.id.iv_registration);
        mInvoiceView = (ImageView) findViewById(R.id.iv_invoice);
        mDrivingDeleteView = (ImageView) findViewById(R.id.iv_driving_license_delete);
        mRegistrationDeleteView = (ImageView) findViewById(R.id.iv_registration_delete);
        mInvoiceDeleteView = (ImageView) findViewById(R.id.iv_invoice_delete);
        mTvIDPhotoView = (TextView) findViewById(R.id.tv_id_photo);
        mArrowView = (ImageView) findViewById(R.id.iv_arrow);
        mSubmitView = (Button) findViewById(R.id.submit);
        mInspectionView = (SellCarItemChooseDate) findViewById(R.id.item_inspection);
        mStrongView = (SellCarItemChooseDate) findViewById(R.id.item_strong);
        mCommercialView = (SellCarItemChooseDate) findViewById(R.id.item_commercial);
        mWarrantyView = (SellCarItemChooseDate) findViewById(R.id.item_warranty);
        mDeleteViews = new ImageView[]{mDrivingDeleteView, mRegistrationDeleteView, mInvoiceDeleteView};
        mLicenseViews = new ImageView[]{mDrivingView, mRegistrationView, mInvoiceView};
        mChooseFragment = new ChooseFragment();
        mLookCarFragment = new LookCarFragment();
        mLocationFragment = new LookCarFragment();
        mChooseGearboxFragment = new ChooseGearboxFragment();
        mChooseLevelFragment = new ChooseLevelFragment();
        mSellCarAddFragment = new SellCarAddFragment(addBrand, addSeries, addModel);
        mBrandView.setOnSellCarBrandClickListener(this);
        mLocationFragment.setChooseAreaListener(this);
        mLookCarFragment.setChooseAreaListener(this);
        mChooseFragment.setListener(this);
        mReadmeView.setOnClickListener(this);
        mChooseGearboxFragment.setListener(this);
        mChooseLevelFragment.setListener(this);
        mLookLocationView.setSellCarItemListener(this);
        mLocationView.setSellCarItemListener(this);
        mColorView.setSellCarItemListener(this);
        mTextIDPhotoView.setOnClickListener(this);
        mDrivingView.setOnClickListener(this);
        mRegistrationView.setOnClickListener(this);
        mInvoiceView.setOnClickListener(this);
        mDrivingDeleteView.setOnClickListener(this);
        mRegistrationDeleteView.setOnClickListener(this);
        mInvoiceDeleteView.setOnClickListener(this);
        mSellCarAddFragment.setOnConfirmListener(this);
        mGridView.setOnItemClickListener(this);
        mSubmitView.setOnClickListener(this);
        mGearboxView.setSellCarItemListener(this);
        mLevelView.setSellCarItemListener(this);
        initRight(mChooseFragment);
        if (UserInfoHelper.getInstance().isLogin(this)) {
            mMobileView.getInputView().setText(UserInfoHelper.getInstance().getPhoneNumber(this));
            mMobileView.getInputView().setEnabled(false);
            UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
                @Override
                public void showUserInfo(User meData) {
                    if (meData != null && !TextUtils.isEmpty(meData.fullname)) {
                        mContactView.getInputView().setText(meData.fullname);
                    }
                }

                @Override
                public void onFinish() {

                }
            }, false);
        }
        mVWholeEditManager.addEditView(new EditTextValidatorView[]{mBrandView.getBrandView(), mColorView.getChooseResultView(), mLevelView.getChooseResultView(),
                mGearboxView.getChooseResultView(), mDisplacementView.getInputView(), mLocationView.getChooseResultView(), mMileView.getInputView(), mOnCardView.getChooseResultView(),
                mPlateNumView.getNumberEditText(), mPriceView.getInputView(), mContactView.getInputView(), mMobileView.getInputView(), mLookLocationView.getChooseResultView()});
    }

    @Override
    public boolean onSupportNavigateUp() {
        showExitDialog();
        return super.onSupportNavigateUp();
    }

    private void showExitDialog() {
        new NormalDialog(this, "你确定放弃编辑") {
            @Override
            protected void doNext() {
                finish();
            }
        }.show();
    }

    @Override
    public void onBackPressed() {
        if (mPlateNumView != null && mPlateNumView.mKeyboardWindow != null && mPlateNumView.mKeyboardWindow.isShowing()) {
            mPlateNumView.hideKeyboard();
        } else if (mDrawerLayoutView.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayoutView.closeDrawers();
        } else {
            showExitDialog();
        }
    }

    private void initRight(Fragment fragment) {
        mRightView.setLayoutParams(new DrawerLayout.LayoutParams(DisplayTools.getScreenWidth(this) * 4 / 5, DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.right, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        com.hxqc.mall.core.util.OtherUtil.closeSoftKeyBoard(this, v);
        if (i == R.id.item_readme) {
            UsedCarActivitySwitcher.toReadme(this, mReadmeView.getText());
        } else if (i == R.id.rl_id_photo) {
            isShowID = !isShowID;
            if (isShowID) {
                mIDPhotoView.setVisibility(View.VISIBLE);
                mTvIDPhotoView.setText("收起证件照");
                mArrowView.setImageResource(R.mipmap.ic_sellthecar_contraction);
            } else {
                mIDPhotoView.setVisibility(View.GONE);
                mTvIDPhotoView.setText("上传证件照，提升信息完整度（非必填）");
                mArrowView.setImageResource(R.mipmap.ic_sellthecar_open);
            }
        } else if (i == R.id.iv_driving_license) {
            upPhoto(10, mDrivingView, mLicenses[0]);

        } else if (i == R.id.iv_registration) {
            upPhoto(20, mRegistrationView, mLicenses[1]);

        } else if (i == R.id.iv_invoice) {
            upPhoto(30, mInvoiceView, mLicenses[2]);

        } else if (i == R.id.iv_driving_license_delete) {
            if (!license_show.equals("") && license_show.substring(0, 4).equals("http")) {
                delete2 = delete2 + OtherUtil.substringAfterLast(license_show, "/") + ",";
            }
            mLicenses[0] = "";
            mDrivingView.setImageResource(R.drawable.ic_common_pictures);
            mDrivingDeleteView.setVisibility(View.GONE);
            WidgetController.setLayoutHeight(mDrivingView, DisplayTools.dip2px(this, 24));
            WidgetController.setLayoutWidth(mDrivingView, DisplayTools.dip2px(this, 24));
        } else if (i == R.id.iv_registration_delete) {
            if (!license1_show.equals("") && license1_show.substring(0, 4).equals("http")) {
                delete2 = delete2 + OtherUtil.substringAfterLast(license1_show, "/") + ",";
            }
            mLicenses[1] = "";
            mRegistrationView.setImageResource(R.drawable.ic_common_pictures);
            mRegistrationDeleteView.setVisibility(View.GONE);
            WidgetController.setLayoutHeight(mRegistrationView, DisplayTools.dip2px(this, 24));
            WidgetController.setLayoutWidth(mRegistrationView, DisplayTools.dip2px(this, 24));
        } else if (i == R.id.iv_invoice_delete) {
            if (!license2_show.equals("") && license2_show.substring(0, 4).equals("http")) {
                delete2 = delete2 + OtherUtil.substringAfterLast(license2_show, "/") + ",";
            }
            mLicenses[2] = "";
            mInvoiceView.setImageResource(R.drawable.ic_common_pictures);
            mInvoiceDeleteView.setVisibility(View.GONE);
            WidgetController.setLayoutHeight(mInvoiceView, DisplayTools.dip2px(this, 24));
            WidgetController.setLayoutWidth(mInvoiceView, DisplayTools.dip2px(this, 24));
        } else if (i == R.id.submit) {
            UserInfoHelper.getInstance().loginAction(this, new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
                    if (mVWholeEditManager.validate()) {
                        submit();
                    }
                }
            });
        }
    }

    private void submit() {
        color = mColorView.getResult();
        displacement = mDisplacementView.getInputView().getText().toString().trim();
        range = mMileView.getInputView().getText().toString();
        firstOnCard = mOnCardView.getResult();
        car_license_no = mPlateNumView.getPlateNumber();
        new_car_price = mNewCarPriceView.getInputView().getText().toString();
        estimatePrice = mPriceView.getInputView().getText().toString();
        contacts = mContactView.getInputView().getText().toString();
        phoneNumber = mMobileView.getInputView().getText().toString();
        boolean hasPhoto = false;
        for (int j = 0; j < 5; j++) {
            if (!TextUtils.isEmpty(mDataList.get(j).sourcePath)) {
                hasPhoto = true;
                break;
            }
        }
        if (!hasPhoto) {
            ToastHelper.showYellowToast(getApplicationContext(), "请上传车辆图片");
            return;
        } else {
            imageItems = (ArrayList<ImageItem>) mDataList;
        }
        if (!TextUtils.isEmpty(mReadmeView.getText())) {
            owners = mReadmeView.getText();
        }
        delete1 = mAdapter.getDelete();
        delete = delete1 + delete2;
        if (!delete.equals("")) {
            delete = OtherUtil.substringBeforeLast(delete, ",");
        }
        inspection_date = mInspectionView.getResult();
        sali_date = mStrongView.getResult();
        warranty_date = mWarrantyView.getResult();
        insurance_date = mCommercialView.getResult();
        mSubmitView.setClickable(false);
        try {
            mUsedCarApiClient.sendSellCar(this, level, gearbox, displacement, new_car_price, carSourceNo, UserInfoHelper.getInstance().getPhoneNumber(this), brandId, seriesId, modelId,
                    addBrand, addSeries, addModel, color, provinceId, cityId, range, firstOnCard, estimatePrice, contacts, phoneNumber, owners, imageItems, delete, inspection_date, sali_date,
                    location_provinceId, location_cityId, location_regionId, warranty_date, insurance_date, car_license_no, mLicenses[0], mLicenses[1], mLicenses[2], cover, new LoadingAnimResponseHandler(this, false) {
                        @Override
                        public void onSuccess(String response) {
                            ToastHelper.showGreenToast(SellCarActivity.this, "提交成功,等待后台审核");
                            if (SellCarDetailInfoActivity.instance != null) {
                                SellCarDetailInfoActivity.instance.finish();
                            }
                            UsedCarActivitySwitcher.toSellCarInfo(SellCarActivity.this);
                            ToastHelper.toastFinish(SellCarActivity.this);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            mSubmitView.setClickable(true);
                        }
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mSubmitView.setClickable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == README) {
            try {
                mReadmeView.setText(data.getStringExtra(UsedCarActivitySwitcher.README));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initDate() {
        for (int i = 0; i < 5; i++) {
            mDataList.add(new ImageItem(""));
        }
        areaModels = mUsedCarSPHelper.getPCR();
        if (areaModels == null || areaModels.size() <= 0) {
            mUsedCarApiClient.getProvinceCity(new LoadingAnimResponseHandler(this) {
                @Override
                public void onSuccess(String response) {
                    areaModels = JSONUtils.fromJson(response, new TypeToken<ArrayList<AreaModel>>() {
                    });
                    mUsedCarSPHelper.savePCR(areaModels);
                }
            });
        }
        sellCarChoose = mUsedCarSPHelper.getSellCarChoose();
        if (sellCarChoose == null || sellCarChoose.color == null || sellCarChoose.color.size() == 0) {
            mUsedCarApiClient.getChoose(new LoadingAnimResponseHandler(this) {
                @Override
                public void onSuccess(String response) {
                    sellCarChoose = JSONUtils.fromJson(response, SellCarChoose.class);
                    mUsedCarSPHelper.saveSellCarChoose(sellCarChoose);
                    if (sellCarChoose != null && sellCarChoose.color != null && sellCarChoose.color.size() > 0) {
                        mColorView.initDate(sellCarChoose.color);
                    }
                }
            });
        } else {
            mColorView.initDate(sellCarChoose.color);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OtherUtil.removeTempFromPref(this);
        mDataList.clear();
    }


    @Override
    public void completeChoose(String brand, String series, String model, String brandId, String seriesId, String modelId) {
        initRight(mChooseFragment);
        mBrandView.setBrandString(brand + series + model);
        mDrawerLayoutView.closeDrawers();
        if (!(this.brandId + this.seriesId + this.modelId).equals(brandId + seriesId + modelId)) {
            this.brandId = brandId;
            this.seriesId = seriesId;
            this.modelId = modelId;
            addBrand = "";
            addSeries = "";
            addModel = "";
            mSellCarAddFragment.setDate("", "", "");
            setModelInfo();
        }
    }

    private void setDisplacementEnabled() {
        mDisplacementView.getInputView().setEnabled(true);
        mDisplacementView.setText("");
        displacement = "";
    }

    private void setGearboxEnabled() {
        mGearboxView.getChooseResultView().setEnabled(true);
        mGearboxView.setResult("");
        gearbox = "";
    }

    private void setLevelEnabled() {
        mLevelView.getChooseResultView().setEnabled(true);
        mLevelView.setResult("");
        level = "";
    }

    private void setNewCarPriceEnabled() {
        mNewCarPriceView.getInputView().setEnabled(true);
        mNewCarPriceView.setText("");
        new_car_price = "";
    }

    private void setModelInfo() {
        mUsedCarApiClient.getInfoByModel(modelId, new LoadingAnimResponseHandler(SellCarActivity.this) {
            @Override
            public void onSuccess(String response) {
                InfoByModel infoByModel = JSONUtils.fromJson(response, InfoByModel.class);
                if (infoByModel != null) {
                    if (!TextUtils.isEmpty(infoByModel.new_car_price)) {
                        new_car_price = infoByModel.new_car_price;
                        mNewCarPriceView.setText(infoByModel.new_car_price);
                        mNewCarPriceView.getInputView().setEnabled(false);
                    } else {
                        setNewCarPriceEnabled();
                    }
                    if (!TextUtils.isEmpty(infoByModel.level)) {
                        level = infoByModel.level;
                        mLevelView.setResult(infoByModel.level);
                        mLevelView.getChooseResultView().setEnabled(false);
                    } else {
                        setLevelEnabled();
                    }
                    if (!TextUtils.isEmpty(infoByModel.gearbox)) {
                        gearbox = infoByModel.gearbox;
                        mGearboxView.setResult(infoByModel.gearbox);
                        mGearboxView.getChooseResultView().setEnabled(false);
                    } else {
                        setGearboxEnabled();
                    }
                    if (!TextUtils.isEmpty(infoByModel.displacement)) {
                        displacement = infoByModel.displacement;
                        mDisplacementView.setText(infoByModel.displacement);
                        mDisplacementView.getInputView().setEnabled(false);
                    } else {
                        setDisplacementEnabled();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                setNewCarPriceEnabled();
                setLevelEnabled();
                setGearboxEnabled();
                setDisplacementEnabled();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        upPhoto(position, null, mDataList.get(position).sourcePath);
    }

    @Override
    public void onConfirmComplete(String s1, String s2, String s3) {
        initRight(mSellCarAddFragment);
        mBrandView.setBrandString(s1 + s2 + s3);
        addBrand = s1;
        addSeries = s2;
        addModel = s3;
        brandId = "";
        seriesId = "";
        modelId = "";
        mDrawerLayoutView.closeDrawers();
        if (!TextUtils.isEmpty(s1 + s2 + s3)) {
            setDisplacementEnabled();
            setGearboxEnabled();
            setLevelEnabled();
            setNewCarPriceEnabled();
        }
    }

    //证件照
    private void upPhoto(final int imagePosition, final ImageView imageView, final String path) {
        if (TextUtils.isEmpty(path)) {
            getPic(imagePosition, imageView);
        } else {
            if (imagePosition < 10) {
                try {
                    new ListDialog(this, "上传图片", new String[]{"设为封面", "查看大图", "拍照"}, new int[]{R.drawable.ic_pic_cover, R.drawable.ic_pic_enlarge, R.drawable.ic_pic_camera}) {
                        @Override
                        protected void doNext(int position) {
                            if (position == 0) {
                                chosenCover = true;
                                if (!mDataList.get(imagePosition).isFigure) {
                                    cover = imagePosition;
                                    for (int i = 0; i < mDataList.size(); i++) {
                                        mDataList.get(i).isFigure = false;
                                    }
                                    mDataList.get(imagePosition).isFigure = true;
                                    mAdapter.notifyDataSetChanged();
                                }
                            } else if (position == 1) {
                                ActivitySwitchBase.toViewLagerPic(0, new ImageModel("file://" + path, "file://" + path), SellCarActivity.this, null);
                            } else {
                                getPic(imagePosition, imageView);
                            }
                        }
                    }.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    new ListDialog(this, "上传图片", new String[]{"查看大图", "拍照"}, new int[]{R.drawable.ic_pic_enlarge, R.drawable.ic_pic_camera}) {
                        @Override
                        protected void doNext(int position) {
                            if (position == 0) {
                                ActivitySwitchBase.toViewLagerPic(0, new ImageModel(path, path), SellCarActivity.this, null);
                            } else if (position == 1) {
                                getPic(imagePosition, imageView);
                            }
                        }
                    }.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void getPic(final int position, final ImageView imageView) {
        MultiImageSelector selector = new MultiImageSelector(this);
        selector.showCamera(true);
        selector.start(this, new MultiImageSelector.MultiImageCallBack() {
            @Override
            public void multiSelectorImages(Collection<String> result) {
                if (result != null && result.size() > 0) {
                    String[] paths = new String[result.size()];
                    String imgPath = result.toArray(paths)[0];
                    if (position > 9) {
                        mLicenses[(position / 10) - 1] = imgPath;
                        ImageUtil.setImageResize(SellCarActivity.this, imageView, new File(imgPath), DisplayTools.dip2px(SellCarActivity.this, 79), DisplayTools.dip2px(SellCarActivity.this, 65));
                        mDeleteViews[(position / 10) - 1].setVisibility(View.VISIBLE);
                        WidgetController.setLayoutHeight(mLicenseViews[(position / 10) - 1], DisplayTools.dip2px(SellCarActivity.this, 65));
                        WidgetController.setLayoutWidth(mLicenseViews[(position / 10) - 1], DisplayTools.dip2px(SellCarActivity.this, 79));
                    } else {
                        ImageItem item = new ImageItem("");
                        item.sourcePath = imgPath;
                        mDataList.remove(position);
                        mDataList.add(position, item);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter = new SellCarAdapter();
        mAdapter.initAdapter(this, mDataList);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void completeChooseGearbox(String gearbox) {
        this.gearbox = gearbox;
        mGearboxView.setResult(gearbox);
        initRight(mChooseGearboxFragment);
    }

    @Override
    public void completeChooseLevel(String level) {
        this.level = level;
        mLevelView.setResult(level);
        initRight(mChooseLevelFragment);
    }

    @Override
    public void addBrand() {
        initRight(mSellCarAddFragment);
        mDrawerLayoutView.open(mBrandView.getBrandView());
    }

    @Override
    public void chooseBrand() {
        initRight(mChooseFragment);
        mDrawerLayoutView.open(mBrandView.getBrandView());
    }

    @Override
    public void sellCarItemClick(View view) {
        int i = view.getId();
        if (i == R.id.item_color) {
            mColorView.showPopWindow(mColorView, DisplayTools.dip2px(this, 100 + 16), -(int) getResources().getDimension(R.dimen.normal_padding_big_16));
        } else if (i == R.id.item_level) {
            mChooseLevelFragment.initCarLevel(sellCarChoose.level);
            initRight(mChooseLevelFragment);
            mDrawerLayoutView.open(mLevelView.getChooseResultView());
        } else if (i == R.id.item_gearbox) {
            mChooseGearboxFragment.initCarGearbox(sellCarChoose.gearbox);
            initRight(mChooseGearboxFragment);
            mDrawerLayoutView.open(mGearboxView.getChooseResultView());
        } else if (i == R.id.item_location) {
            mLookCarFragmentFlag = "2";
            initRight(mLocationFragment);
            if (!areaModels.isEmpty()) {
                mDrawerLayoutView.open(mLocationView.getChooseResultView());
                try {
                    mLocationFragment.initDate(location_province, location_city, location_region, areaModels, mLookCarFragmentFlag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (i == R.id.item_look_location) {
            mLookCarFragmentFlag = "1";
            initRight(mLookCarFragment);
            if (!areaModels.isEmpty()) {
                mDrawerLayoutView.open(mLookLocationView.getChooseResultView());
                try {
                    mLookCarFragment.initDate(province, city, "", areaModels, mLookCarFragmentFlag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onSubmitComplete(String province1, String city1, String region1, String provinceId, String provinceCode, String cityId, String cityCode, String regionId, ArrayList<AreaModel> areaModels_p1, ArrayList<CityModel> areaModels_c1, ArrayList<RegionModel> areaModels_s1) {
        if (mLookCarFragmentFlag.equals("1")) {
            initRight(mLookCarFragment);
            mLookLocationView.setResult(province1 + city1 + region1);
            province = province1;
            city = city1;
            provinceId = provinceCode;
            cityId = cityCode;
            areaModels_p = areaModels_p1;
            areaModels_c = areaModels_c1;
            areaModels_s = areaModels_s1;
            mDrawerLayoutView.closeDrawers();
        } else {
            initRight(mLocationFragment);
            location_province = province1;
            location_city = city1;
            location_region = region1;
            location_provinceId = provinceCode;
            location_cityId = cityCode;
            location_regionId = regionId;
            mLocationView.setResult(province1 + city1);
            areaModels_p = areaModels_p1;
            areaModels_c = areaModels_c1;
            areaModels_s = areaModels_s1;
            mDrawerLayoutView.closeDrawers();
        }
    }

    @Override
    public void initData() {
        if (mLookCarFragmentFlag.equals("1")) {
            mLookCarFragment.initDate(province, city, "", areaModels, mLookCarFragmentFlag);
        } else {
            mLocationFragment.initDate(location_province, location_city, location_region, areaModels, mLookCarFragmentFlag);
        }
    }
}
