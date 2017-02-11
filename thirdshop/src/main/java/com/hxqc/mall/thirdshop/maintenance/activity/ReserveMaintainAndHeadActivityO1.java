package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.fragment.AutoTypeFragment;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.view.CommonEditInfoItemView;
import com.hxqc.mall.auto.view.NewPlateNumberLayout;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.manager.VWholeEditManager;
import com.hxqc.mall.core.views.vedit.tech.VMallDivNotNull;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.maintenance.config.MaintainContants;
import com.hxqc.mall.thirdshop.maintenance.control.ReservationMaintainControl;
import com.hxqc.mall.thirdshop.maintenance.model.MaintenanceShop;
import com.hxqc.mall.thirdshop.maintenance.model.Mechanic;
import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ReservationMaintainInfo;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ReservationSuccessInfo;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ServiceType;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.thirdshop.maintenance.views.ReserveDateDialog;
import com.hxqc.mall.thirdshop.model.ShopInfo;
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.mall.thirdshop.views.ShopDetailsHeadView;
import com.hxqc.util.DebugLog;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import java.util.ArrayList;

/**
 * Author :胡仲俊
 * Date : 2016-03-07
 * Des: 修车预约
 * FIXME
 * Todo
 */
public class ReserveMaintainAndHeadActivityO1 extends FoucsEditShopDetailActivity implements ReserveDateDialog.OnFinishClickListener, MenuDrawer.OnDrawerStateChangeListener, AutoTypeFragment.onBackData {

    private static final String TAG = AutoInfoContants.LOG_J;
    public static final String MAINTEANCESHOP = "mainteanceShop";
    //    private PlateNumberTextView mLicenseCityView; //车牌城市
//    private PlateNumberTextView mLicenseNumberView;//车牌字母
    private EditText mVINView;//VIN
    private CommonEditInfoItemView mSelectServiceTypeView;//选择服务类型
    private CommonEditInfoItemView mSelectReserveDateView; //预约时间
    //    private CommonEditInfoItemView mSelectCounselorView; //预约服务顾问
//    private CommonEditInfoItemView mSelectMechanicView; //预约服务技师
    private Button mReserveSuccessView; //提交
    //    private CommonAutoInfoHeaderViewV2 mCommonAutoInfoHeaderView;
    private EditTextValidatorView mAutoTypeView;//车辆类型
    private ReserveDateDialog reserveDateDialog;
    private OverlayDrawer mOverlayDrawer;
    private TextView mMaintainView;//保养时间
    private TextView mDriveDistanceView;//里程
    private TextView mQualityDateView;//质保期
    private CommonEditInfoItemView mCustomView;//联系人姓名
    private CommonEditInfoItemView mPhoneNumView;//手机号码
    private CommonEditInfoItemView mShopView;//服务门店
    private ReservationMaintainInfo reservationMaintainInfo;//提交数据
    private ServiceAdviser mServiceAdviser;
    private Mechanic mMechanic;
    private ServiceType serviceType;
    private RequestFailView mRequestFailView;

    private AutoTypeFragment autoTypeFragment;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MaintainContants.AUTO_TYPE_BACK_DATA) {
                imMyAuto = (MyAuto) msg.obj;
                mAutoTypeView.setText(imMyAuto.autoModel);
            } else if (msg.what == MaintainContants.RESERVATION_MAINTAIN_INFO_DATA) {
                reservationMaintainInfo = (ReservationMaintainInfo) msg.obj;
//                setData(reservationMaintainInfo);
            } else if (msg.what == MaintainContants.SERVCE_ADVISER_DATA) {
                mServiceAdviser = ((ServiceAdviser) msg.obj);
//                mSelectCounselorView.setContentText(mServiceAdviser.name);
            } else if (msg.what == MaintainContants.SERVCE_MECHANIC_DATA) {
                mMechanic = ((Mechanic) msg.obj);
//                mSelectMechanicView.setContentText(mMechanic.name);
            } else if (msg.what == MaintainContants.SERVICE_TYPE_DATA) {
                serviceType = (ServiceType) msg.obj;
                DebugLog.i(TAG, serviceType.toString());
                if (serviceType.serviceType.equals("-1")) {
                    if (serviceTypes.get(serviceTypes.size() - 1).serviceType.equals("-1")) {
                        serviceTypes.set(serviceTypes.size() - 1, serviceType);
                    } else {
                        serviceTypes.add(serviceType);
                    }
                }
                mSelectServiceTypeView.setContentText(serviceType.kindTitle);
            }
        }
    };

    private String shopType = "";
    private String shopID = "";
    private String shopTitle = "";
    private MaintenanceShop mainteanceShop;
    private LinearLayout mMaintainInfoView;
    private ScrollView mScrollView;
    private ShopDetailsHeadView mShopDetailsHeadView;
    private CallBar mCallBarView;
    private NewPlateNumberLayout mPlateNumberView;
    private ActionBar mActionBar;
    private ShopInfo shopInfo;
    //    private Auto auto;
    private MyAuto imMyAuto = null;
    private VWholeEditManager vWholeEditManager;
    private EditTextValidatorView mNumberEditTextView;
    private EditTextValidatorView mServiceTypeView;
    private EditTextValidatorView mAppointmentTimeView;
    private EditTextValidatorView mNameView;
    private EditTextValidatorView mPhoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reserve_maintain_info_menu_and_head);

        vWholeEditManager = new VWholeEditManager(this);

        initView();

        initData();

        initEvent();
    }

    //初始化数据
    private void initData() {

        if (getIntent() != null) {
            Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            mainteanceShop = bundleExtra.getParcelable(ReserveMaintainActivity.MAINTEANCESHOP);
//            mainteanceShop = getIntent().getParcelableExtra(ReserveMaintainActivity.MAINTEANCESHOP);
            if (mainteanceShop != null) {
                shopID = mainteanceShop.shopID;
                shopTitle = mainteanceShop.shopTitle;
                shopType = mainteanceShop.shopType;
                DebugLog.i(TAG, shopID + "-----11----" + shopTitle + "-----11----" + shopType);
            }
            /*if (!TextUtils.isEmpty(getIntent().getStringExtra(ShopDetailsController.SHOPID_KEY)) && getIntent().getStringExtra(ShopDetailsController.SHOPID_KEY) != null) {
                shopID = getIntent().getStringExtra(ShopDetailsController.SHOPID_KEY);
                DebugLog.i(TAG, shopID + "-----22----");
            }*/
            if (!TextUtils.isEmpty(bundleExtra.getString(ShopDetailsController.SHOPID_KEY))) {
                shopID = bundleExtra.getString(ShopDetailsController.SHOPID_KEY);
                shopType = bundleExtra.getString(ShopDetailsController.SHOP_TYPE);
                DebugLog.i(TAG, shopID + "-----22----" + shopType);
            }
            if (!TextUtils.isEmpty(shopID)) {
//        ReservationMaintainControl.getInstance().requestReservationMaintain(this, "1585067251822281", this);
                ReservationMaintainControl.getInstance().requestReservationMaintain(this, shopID, reservationMaintainCallBack);
            }
        }

/*
        if (getIntent() != null) {
            shopID = mShopDetailsController.getShopID();
            DebugLog.i(TAG, "shopID:" + shopID);
            if (shopID != null) {
                ReservationMaintainControl.getInstance().requestReservationMaintain(this, shopID, this);
                autoTypeFragment.setShopID(shopID);
            }
        }*/

//        ReservationMaintainControl.getInstance().requestReservationMaintain(this, shopID, this);
//        autoTypeFragment.setShopID(shopID);
    }

    //初始化控件
    private void initView() {
        mShopDetailsHeadView = (ShopDetailsHeadView) findViewById(R.id.reserve_maintain_header_info_shop_detail_head);
        mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_XCYY);

        mCallBarView = (CallBar) findViewById(R.id.reserve_maintain_header_call_bar);

        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.reserve_maintain_header_drawer);
        autoTypeFragment = (AutoTypeFragment) getSupportFragmentManager().findFragmentById(R.id.reserve_maintain_header_menu);
//        mCommonAutoInfoHeaderView = (CommonAutoInfoHeaderViewV2) findViewById(R.id.reserve_maintain_header_info_header);

//        mPlateNumberView = mCommonAutoInfoHeaderView.getmPlateNumView();
//        mVINView = mCommonAutoInfoHeaderView.getmVINView();
//        mAutoTypeView = mCommonAutoInfoHeaderView.getmAutoTypeView();
//        mMaintainView = mCommonAutoInfoHeaderView.getmMaintainView();
//        mDriveDistanceView = mCommonAutoInfoHeaderView.getmDriveDistanceView();
//        mQualityDateView = mCommonAutoInfoHeaderView.getmQualityDateView();
//        mMaintainInfoView = mCommonAutoInfoHeaderView.getmMaintainInfoView();
        mPlateNumberView = (NewPlateNumberLayout) findViewById(R.id.reserve_maintain_plate_number);
        mNumberEditTextView = mPlateNumberView.getNumberEditText();
//        mVINView = mCommonAutoInfoHeaderView.getmVINView();
        mAutoTypeView = (EditTextValidatorView) findViewById(R.id.reserve_maintain_type);
        mAutoTypeView.addValidator(new VMallDivNotNull("请选择车辆信息", ""));
        mMaintainView = (TextView) findViewById(R.id.reserve_maintain_maintain);
        mDriveDistanceView = (TextView) findViewById(R.id.reserve_maintain_driving_distance);
        mQualityDateView = (TextView) findViewById(R.id.reserve_maintain_date);
        mMaintainInfoView = (LinearLayout) findViewById(R.id.reserve_maintain_maintain_info);
//        mCommonAutoInfoHeaderView.setPlateNumTitleDrawLeft(R.drawable.bg_star);

        mScrollView = (ScrollView) findViewById(R.id.reserve_maintain_scroll);
        mSelectServiceTypeView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_header_select_service_type);
        mServiceTypeView = mSelectServiceTypeView.getmContentView();
        mSelectReserveDateView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_header_select_reserve_date);
        mAppointmentTimeView = mSelectReserveDateView.getmContentView();
//        mSelectCounselorView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_header_service_adviser);
//        mSelectMechanicView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_header_service_mechanic);
        mReserveSuccessView = (Button) findViewById(R.id.reserve_maintain_header_reserve_success);

        mCustomView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_header_custom);
        mNameView = mCustomView.getmContentView();
        mPhoneNumView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_header_phone_num);
        mPhoneView = mPhoneNumView.getmContentView();
        mShopView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_header_shop);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
        mOverlayDrawer.setSidewardCloseMenu(true);

        mActionBar = getSupportActionBar();

        mRequestFailView = (RequestFailView) findViewById(R.id.reserve_maintain_header_fail_view);

//        mNumberEditTextView.addValidator(new PlateNumberRegexpValidator("请输入正确的车牌号", "^[A-Z]{1}[A-Z_0-9]{5}$"));
        vWholeEditManager.addEditView(mNumberEditTextView);
        vWholeEditManager.addEditView(mAutoTypeView);
        vWholeEditManager.addEditView(mNameView);
        vWholeEditManager.addEditView(mPhoneView);
        vWholeEditManager.addEditView(mServiceTypeView);
        vWholeEditManager.addEditView(mAppointmentTimeView);
    }


    //初始化事件
    private void initEvent() {

//        mScrollView.setOnTouchListener(scrollTouchListener);

        mAutoTypeView.setOnClickListener(autoTypeClickListener);
        mOverlayDrawer.setOnDrawerStateChangeListener(this);
        autoTypeFragment.setOnBackDataListener(this);

        mShopView.setOnContentClickListener(shopClickListener);
        mSelectServiceTypeView.setOnContentClickListener(selectServiceTypeClickListener);
        mSelectReserveDateView.setOnContentClickListener(selectReserveDateClickListener);
//        mSelectCounselorView.setOnContentClickListener(selectCounselorClickListener);
//        mSelectMechanicView.setOnContentClickListener(selectMechanicClickListener);

        mReserveSuccessView.setOnClickListener(reserveSuccessClickListener);

//        mLicenseNumberView.addTextChangedListener(licenseNumberChangeListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mShopDetailsHeadView !=null)
            mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_XCYY);

        mPlateNumberView.onResume();
    }

    private int imServiceAdviserPosition = -1;
    private int imServiceMechanicPosition = -1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundleExtra = data.getBundleExtra(ActivitySwitchBase.KEY_DATA);
            if (resultCode == MaintainContants.SERVICE_TYPE_DATA_BACK) {
          /*      String i = "";
                HashMap<Integer, String> serviceTypeBackData = (HashMap<Integer, String>) data.getSerializableExtra("serviceTypeBackData");
                Iterator iter = serviceTypeBackData.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    i += entry.getValue();
                }*/
//                ServiceType serviceType = data.getParcelableExtra("serviceTypeBackData");
                ServiceType serviceType = bundleExtra.getParcelable("serviceTypeBackData");
                serviceTypes = bundleExtra.getParcelableArrayList("serviceTypeGroupBackData");
                Message msg = Message.obtain();
                msg.what = MaintainContants.SERVICE_TYPE_DATA;
                msg.obj = serviceType;
                mHandler.sendMessage(msg);
            } else if (resultCode == MaintainContants.SERVCE_ADVISER_OR_MECHANIC_BACK_DATA) {
                if (bundleExtra.getParcelable("serviceAdviser") != null) {
                    ServiceAdviser serviceAdviser = bundleExtra.getParcelable("serviceAdviser");
                    imServiceAdviserPosition = bundleExtra.getInt("position", -1);
                    Message msg = Message.obtain();
                    msg.what = MaintainContants.SERVCE_ADVISER_DATA;
                    msg.obj = serviceAdviser;
                    mHandler.sendMessage(msg);
                } else if (bundleExtra.getParcelable("mechanic") != null) {
                    Mechanic mechanic = bundleExtra.getParcelable("mechanic");
                    imServiceMechanicPosition = bundleExtra.getInt("position", -1);
                    Message msg = Message.obtain();
                    msg.what = MaintainContants.SERVCE_MECHANIC_DATA;
                    msg.obj = mechanic;
                    mHandler.sendMessage(msg);
                }
/*
                if (data.getParcelableExtra("serviceAdviser") != null) {
                    ServiceAdviser serviceAdviser = data.getParcelableExtra("serviceAdviser");
                    imServiceAdviserPosition = data.getIntExtra("position", -1);
                    Message msg = Message.obtain();
                    msg.what = MaintainContants.SERVCE_ADVISER_DATA;
                    msg.obj = serviceAdviser;
                    mHandler.sendMessage(msg);
                } else if (data.getParcelableExtra("mechanic") != null) {
                    Mechanic mechanic = data.getParcelableExtra("mechanic");
                    imServiceMechanicPosition = data.getIntExtra("position", -1);
                    Message msg = Message.obtain();
                    msg.what = MaintainContants.SERVCE_MECHANIC_DATA;
                    msg.obj = mechanic;
                    mHandler.sendMessage(msg);
                }
*/
            }
        }
    }

/*
    private TextWatcher licenseNumberChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            DebugLog.i(TAG, "beforeTextChanged:" + s.toString());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            DebugLog.i(TAG, "onTextChanged:" + s.toString());
        }


        @Override
        public void afterTextChanged(Editable s) {
            DebugLog.i(TAG, "afterTextChanged:" + s.toString());
            if (s.toString().length() == 6) {
                String plateNumber = mLicenseCityView.getText() + s.toString();
                DebugLog.i(TAG, "getInstance" + plateNumber);
                AutoInfoControl.getInstance().checkAutoInfo(ReserveMaintainAndHeadActivityO1.this, "", plateNumber, ReserveMaintainAndHeadActivityO1.this);
//                    mLicenseNumberView.setSelection(6);
            }
        }
    };
*/

/*    //滑动时隐藏键盘
    private View.OnTouchListener scrollTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                mLicenseCityView.dismissPopup();
//                mLicenseNumberView.dismissPopup();
                mPlateNumberView.dismissPopup();
            }
            return false;
        }
    };*/

    //车型选择侧滑
    private View.OnClickListener autoTypeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mOverlayDrawer.isMenuVisible()) mOverlayDrawer.openMenu();
        }
    };

    //店铺点击事件
    private CommonEditInfoItemView.onContentClickListener shopClickListener = new CommonEditInfoItemView.onContentClickListener() {
        @Override
        public void onContentClick(View v) {
            if (shopInfo != null) {
                PickupPointT shopLocation = shopInfo.getShopLocation();
                if (shopLocation != null) {
                    PickupPointT pickupPointT = new PickupPointT();
                    pickupPointT.address = shopLocation.address;
                    pickupPointT.latitude = shopLocation.latitude + "";
                    pickupPointT.longitude = shopLocation.longitude + "";
                    pickupPointT.tel = shopLocation.tel;
                    ActivitySwitchBase.toAMapNvai(ReserveMaintainAndHeadActivityO1.this, pickupPointT);
                }
            }
        }
    };

    //服务类型点击事件
    private CommonEditInfoItemView.onContentClickListener selectServiceTypeClickListener = new CommonEditInfoItemView.onContentClickListener() {
        @Override
        public void onContentClick(View v) {
            DebugLog.i(TAG, "shopID:" + shopID);
            ActivitySwitcherMaintenance.toServiceType(ReserveMaintainAndHeadActivityO1.this, MaintainContants.SERVICE_TYPE_DATA, shopID, serviceTypes, serviceType);
        }
    };

    //预约时间点击事件
    private CommonEditInfoItemView.onContentClickListener selectReserveDateClickListener = new CommonEditInfoItemView.onContentClickListener() {
        @Override
        public void onContentClick(View v) {
//            reserveDateDialog = new ReserveDateDialog(ReserveMaintainActivity.this, "");
            if (reservationMaintainInfo != null) {
                reserveDateDialog = new ReserveDateDialog(ReserveMaintainAndHeadActivityO1.this);
                reserveDateDialog.setOnFinishClickListener(ReserveMaintainAndHeadActivityO1.this);
                reserveDateDialog.build();
                reserveDateDialog.setData(reservationMaintainInfo.apppintmentDate, mSelectReserveDateView.getContentText());
                reserveDateDialog.create();

            }
        }
    };

    //顾问点击事件
    private CommonEditInfoItemView.onContentClickListener selectCounselorClickListener = new CommonEditInfoItemView.onContentClickListener() {
        @Override
        public void onContentClick(View v) {
            if (reservationMaintainInfo != null) {
                if (reservationMaintainInfo.getServiceAdviser() != null) {
                    ActivitySwitcherMaintenance.toServiceAdviser(ReserveMaintainAndHeadActivityO1.this, 10, shopID, reservationMaintainInfo.getServiceAdviser(), imServiceAdviserPosition);
                }
            }
        }
    };

    //技师点击事件
    private CommonEditInfoItemView.onContentClickListener selectMechanicClickListener = new CommonEditInfoItemView.onContentClickListener() {
        @Override
        public void onContentClick(View v) {
            if (reservationMaintainInfo != null) {
                if (reservationMaintainInfo.getMechanic() != null) {
                    ActivitySwitcherMaintenance.toServiceMechanic(ReserveMaintainAndHeadActivityO1.this, 11, shopID, reservationMaintainInfo.getMechanic(), imServiceMechanicPosition);
                }
            }
        }
    };

    //提交点击事件
    private View.OnClickListener reserveSuccessClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserInfoHelper.getInstance().loginAction(ReserveMaintainAndHeadActivityO1.this, new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
                    analysisReservationSuccessInfo();
                }
            });
        }
    };

    private void getFocusable(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean flag = false;

    /**
     * 提交前数据的校验
     */
    private void analysisReservationSuccessInfo() {

        if (TextUtils.isEmpty(mPlateNumberView.getPlateNumber())) {
            ToastHelper.showYellowToast(this, "请输入车牌号");
            vWholeEditManager.validate();
            return;
        } else if (mPlateNumberView.getPlateNumber().length() < 7) {
            ToastHelper.showYellowToast(this, "请输入正确的车牌号");
            vWholeEditManager.validate();
            return;
        }
        /*if (mPlateNumberView.getPlateNumber().length() > 0 && mPlateNumberView.getPlateNumber().length() < 7) {
            ToastHelper.showYellowToast(this, "车牌号格式不正确");
            return;
        }*/

        if (TextUtils.isEmpty(mAutoTypeView.getText())) {
            ToastHelper.showYellowToast(this, "请选择车辆信息");
            vWholeEditManager.validate();
            return;
        }
        if (TextUtils.isEmpty(mCustomView.getContentText())) {
            ToastHelper.showYellowToast(this, "请输入联系人姓名");
            vWholeEditManager.validate();
            return;
        }
        /*if (FormatCheck.checkName2(mCustomView.getContentText(), this) == FormatCheck.NO_REAL_NAME) {
            return;
        } else if (FormatCheck.checkName2(mCustomView.getContentText(), this) == FormatCheck.REAL_NAME_ERROR) {
            return;
        }

        if (!FormatCheck.checkRealName(mCustomView.getContentText(), this)) {
            return;
        }*/

        if (FormatCheck.checkPhoneNumber(mPhoneNumView.getContentText(), this) == FormatCheck.NO_PHONE_NUMBER) {
            vWholeEditManager.validate();
            return;
        } else if (FormatCheck.checkPhoneNumber(mPhoneNumView.getContentText(), this) == FormatCheck.PHONE_NUMBER_FORMAT_ERROR) {
            vWholeEditManager.validate();
            return;
        }

        if (TextUtils.isEmpty(mSelectServiceTypeView.getContentText())) {
            ToastHelper.showYellowToast(this, "请选择服务类型");
            vWholeEditManager.validate();
            return;
        }
        if (TextUtils.isEmpty(mSelectReserveDateView.getContentText())) {
            ToastHelper.showYellowToast(this, "请选择预约时间");
            vWholeEditManager.validate();
            return;
        }
        /*if (TextUtils.isEmpty(mSelectCounselorView.getContentText())) {
            ToastHelper.showYellowToast(this, "请选择服务顾问");
            return;
        }
        if (TextUtils.isEmpty(mSelectMechanicView.getContentText())) {
            ToastHelper.showYellowToast(this, "请选择服务技师");
            return;
        }*/

        if (vWholeEditManager.validate()) {
            final ReservationSuccessInfo reservationSuccessInfo = new ReservationSuccessInfo();
            reservationSuccessInfo.name = mCustomView.getContentText().toString();
            reservationSuccessInfo.phone = mPhoneNumView.getContentText().toString();
            reservationSuccessInfo.autoModel = imMyAuto.autoModel;
            reservationSuccessInfo.autoModelID = imMyAuto.autoModelID;
            reservationSuccessInfo.shop = shopTitle;
            reservationSuccessInfo.shopID = shopID;
            reservationSuccessInfo.shopType = shopType;
            reservationSuccessInfo.serviceType = serviceType.kindTitle;
            reservationSuccessInfo.serviceTypeID = serviceType.serviceType;
            reservationSuccessInfo.remark = serviceType.remark;
            reservationSuccessInfo.apppintmentDate = mSelectReserveDateView.getContentText().toString();

            if (mServiceAdviser != null) {
                reservationSuccessInfo.serviceAdviser = mServiceAdviser.name;
                reservationSuccessInfo.serviceAdviserID = mServiceAdviser.serviceAdviserID;
            }
            if (mMechanic != null) {
                reservationSuccessInfo.mechanic = mMechanic.name;
                reservationSuccessInfo.mechanicID = mMechanic.mechanicID;
            }
            reservationSuccessInfo.plateNumber = mPlateNumberView.getPlateNumber();
            if (reservationMaintainInfo.getMyAuto() != null) {
                reservationSuccessInfo.drivingDistance = reservationMaintainInfo.getMyAuto().drivingDistance;
            }
//            reservationSuccessInfo.VIN = mVINView.getText().toString();
            DebugLog.i(TAG, reservationSuccessInfo.toString());
            DebugLog.i(TAG, imMyAuto.toString());
            AutoTypeControl.getInstance().requestBrand(this, shopID, new CallBackControl.CallBack<ArrayList<BrandGroup>>() {
                @Override
                public void onSuccess(ArrayList<BrandGroup> myAutoGroups) {
                    if (myAutoGroups != null && !myAutoGroups.isEmpty()) {
                        for (int i = 0; i < myAutoGroups.size(); i++) {
                            for (int j = 0; j < myAutoGroups.get(i).group.size(); j++) {
                                if (imMyAuto.brand.equals(myAutoGroups.get(i).group.get(j).brandName)) {
                                    DebugLog.i(TAG, imMyAuto.brand + "----111" + myAutoGroups.get(i).group.get(j).brandName);
                                    flag = true;
                                } else {
                                    flag = false;
                                }
                                if (flag) {
                                    break;
                                }
                            }
                            if (flag) {
                                break;
                            }
                        }
                        DebugLog.i(TAG, "flag:" + flag);
                        if (flag) {
                            ActivitySwitcherMaintenance.toReserveSuccess(ReserveMaintainAndHeadActivityO1.this, reservationSuccessInfo);
                        } else {
                            ToastHelper.showYellowToast(ReserveMaintainAndHeadActivityO1.this, "请选择本店的车辆信息");
                            return;
                        }
                    }
                }

                @Override
                public void onFailed(boolean offLine) {

                }
            });
        }

        /*AutoTypeControl.getInstance().requestBrand(this, shopID, new CallBack<ArrayList<BrandGroup>>() {
            @Override
            public void onSuccess(ArrayList<BrandGroup> response) {
                if (response.size() > 0) {
                    for (int i = 0; i < response.size(); i++) {
                        for (int j = 0; j < response.get(i).group.size(); j++) {
                            if (imMyAuto.brand.equals(response.get(i).group.get(j).brandName)) {
                                DebugLog.i(TAG, imMyAuto.brand + "----111" + response.get(i).group.get(j).brandName);
                                flag = true;
                                break;
                            } else {
                                flag = false;
                            }
                        }
                    }
                    DebugLog.i(TAG,"flag:"+flag);
                    if (flag) {
                        ActivitySwitcherMaintenance.toReserveSuccess(ReserveMaintainAndHeadActivityO1.this, reservationSuccessInfo);
                    } else {
                        ToastHelper.showYellowToast(ReserveMaintainAndHeadActivityO1.this, "请选择本店的车辆信息");
                        return;
                    }
                }
            }

            @Override
            public void onFailed(boolean offLine) {

            }
        });*/

//        ActivitySwitcherMaintenance.toReserveSuccess(this, reservationSuccessInfo);

//        ActivitySwitcherMaintenance.toReserveSuccess(this, TestDataUtil.reserveTestData());
    }

    //展示数据
    @Deprecated
    private void setData(ReservationMaintainInfo reservationMaintainInfo) {
        if (reservationMaintainInfo.getMyAuto() != null) {
            if (!TextUtils.isEmpty(reservationMaintainInfo.getMyAuto().plateNumber)) {
                mPlateNumberView.setPlateNumber(reservationMaintainInfo.getMyAuto().plateNumber,true);
            }
            if (!TextUtils.isEmpty(reservationMaintainInfo.getMyAuto().autoModel)) {
                mAutoTypeView.setText(reservationMaintainInfo.getMyAuto().autoModel);
            }
            if (!TextUtils.isEmpty(reservationMaintainInfo.getMyAuto().lastMaintenanceDate)) {
                mMaintainView.setText(getString(R.string.maintenance_time) + reservationMaintainInfo.getMyAuto().lastMaintenanceDate);
            } else if (!TextUtils.isEmpty(reservationMaintainInfo.getMyAuto().drivingDistance) && Integer.parseInt(reservationMaintainInfo.getMyAuto().drivingDistance) > 0) {
                mDriveDistanceView.setText(getString(R.string.mileage) + reservationMaintainInfo.getMyAuto().drivingDistance + getString(R.string.kilometre));
            } else if (!TextUtils.isEmpty(reservationMaintainInfo.getMyAuto().lastMaintenanceDate) && !TextUtils.isEmpty(reservationMaintainInfo.getMyAuto().drivingDistance) && Integer.parseInt(reservationMaintainInfo.getMyAuto().drivingDistance) > 0) {
                mMaintainView.setText(getString(R.string.maintenance_time) + reservationMaintainInfo.getMyAuto().lastMaintenanceDate);
                mDriveDistanceView.setText(getString(R.string.mileage) + reservationMaintainInfo.getMyAuto().drivingDistance + getString(R.string.kilometre));
            }
            if (!TextUtils.isEmpty(reservationMaintainInfo.getMyAuto().guaranteePeriod)) {

                mQualityDateView.setText(getString(R.string.warranty) + reservationMaintainInfo.getMyAuto().guaranteePeriod);
            }

            if (!TextUtils.isEmpty(reservationMaintainInfo.getMyAuto().ownName)) {
                mCustomView.setContentText(reservationMaintainInfo.getMyAuto().ownName);
            }

            if (!TextUtils.isEmpty(reservationMaintainInfo.getMyAuto().ownPhone)) {
                mPhoneNumView.setContentText(reservationMaintainInfo.getMyAuto().ownPhone);
            }
            if (!TextUtils.isEmpty(shopTitle)) {
                mShopView.setContentText(shopTitle);
            }
            DebugLog.i(TAG, "shopTitle:" + shopTitle);
        }
    }

    @Override
    public void onFinishClick(View v, String dateAndTime) {
        DebugLog.i(TAG, dateAndTime);
        mSelectReserveDateView.setContentText(dateAndTime);
    }

    @Override
    public void onDrawerStateChange(int oldState, int newState) {
        if (newState == 8) {
            autoTypeFragment.setData(imMyAuto);
        }
    }

    @Override
    public void onDrawerSlide(float openRatio, int offsetPixels) {

    }

    @Override
    public void backData(String brand, String brandID, String brandThumb, String seriesBrandName, String series, String seriesID, String model, String modelID) {
        mOverlayDrawer.closeMenu();
        if (imMyAuto == null) {
            imMyAuto = new MyAuto();
        }
        imMyAuto.brand = brand;
        imMyAuto.brandID = brandID;
        imMyAuto.brandThumb = brandThumb;
        imMyAuto.series = series;
        imMyAuto.seriesID = seriesID;
        imMyAuto.autoModel = model;
        imMyAuto.autoModelID = modelID;
        imMyAuto.brandName = seriesBrandName;
        Message msg = Message.obtain();
        msg.what = MaintainContants.AUTO_TYPE_BACK_DATA;
        msg.obj = imMyAuto;
        mHandler.sendMessage(msg);
    }

    private CallBackControl.CallBack<ReservationMaintainInfo> reservationMaintainCallBack = new CallBackControl.CallBack<ReservationMaintainInfo>() {
        @Override
        public void onSuccess(ReservationMaintainInfo response) {
            mRequestFailView.setVisibility(View.GONE);
            Message msg = Message.obtain();
            msg.what = MaintainContants.RESERVATION_MAINTAIN_INFO_DATA;
            msg.obj = response;
            mHandler.sendMessage(msg);
//            AutoHelper.getInstance().checkLocationMyAuto(ReserveMaintainAndHeadActivityO1.this, shopID, checkLocalCallBack);
            ReservationMaintainControl.getInstance().requestServiceType(ReserveMaintainAndHeadActivityO1.this, shopID, serviceTypeCallBack);
            mShopDetailsController.requestThirdPartShop(ReserveMaintainAndHeadActivityO1.this, thirdPartShopCallBack);
            autoTypeFragment.setShopID(shopID);
        }

        @Override
        public void onFailed(boolean offLine) {
//        notShop();
            mRequestFailView.notShop(ReserveMaintainAndHeadActivityO1.this, "店铺不存在");
        }
    };

    /*@Override
    public void onReservationMaintainSucceed(ReservationMaintainInfo reservationMaintainInfo) {
        mRequestFailView.setVisibility(View.GONE);
        Message msg = Message.obtain();
        msg.what = MaintainContants.RESERVATION_MAINTAIN_INFO_DATA;
        msg.obj = reservationMaintainInfo;
        mHandler.sendMessage(msg);
        AutoHelper.getInstance().checkLocationMyAuto(this, shopID, this);
        ReservationMaintainControl.getInstance().requestServiceType(this, shopID, this);
        mShopDetailsController.requestThirdPartShop(ReserveMaintainAndHeadActivityO1.this, this);
        autoTypeFragment.setShopID(shopID);
    }

    @Override
    public void onReservationMaintainFailed(boolean offLine) {
//        notShop();
        mRequestFailView.notShop(this, "店铺不存在");
    }*/

    private CallBackControl.CallBack<MyAuto> checkCallBack = new CallBackControl.CallBack<MyAuto>() {
        @Override
        public void onSuccess(MyAuto response) {
            if (response != null) {
                if (TextUtils.isEmpty(mCustomView.getContentText())) {
                    if (!TextUtils.isEmpty(response.ownName)) {
                        mCustomView.setContentText(response.ownName);
                    }
                }
                if (TextUtils.isEmpty(mPhoneNumView.getContentText())) {
                    if (!TextUtils.isEmpty(response.ownPhone)) {
                        mPhoneNumView.setContentText(response.ownPhone);
                    }
                }

                if (TextUtils.isEmpty(mMaintainView.getText())) {
                    if (TextUtils.isEmpty(response.lastMaintenanceDate) && TextUtils.isEmpty(response.drivingDistance) && TextUtils.isEmpty(response.guaranteePeriod)) {
//            mMaintainView.setVisibility(View.GONE);
//            mQualityDateView.setVisibility(View.GONE);
                        mMaintainInfoView.setVisibility(View.GONE);
                    } else if (!TextUtils.isEmpty(response.lastMaintenanceDate) || !TextUtils.isEmpty(response.drivingDistance) || !TextUtils.isEmpty(response.guaranteePeriod)) {
//            mMaintainView.setVisibility(View.VISIBLE);
//            mQualityDateView.setVisibility(View.VISIBLE);
                        mMaintainInfoView.setVisibility(View.VISIBLE);
                    }
                   /*if (!TextUtils.isEmpty(response.lastMaintenanceDate) && !TextUtils.isEmpty(response.drivingDistance)) {
                        mMaintainView.setText(getString(R.string.maintenance_time) + response.lastMaintenanceDate + " " + getString(R.string.mileage) + response.drivingDistance + getString(R.string.kilometre));
                    } else if (!TextUtils.isEmpty(response.lastMaintenanceDate)) {
                        mMaintainView.setText(getString(R.string.maintenance_time) + response.lastMaintenanceDate);
                    } else if (!TextUtils.isEmpty(response.drivingDistance)) {
                        mMaintainView.setText(getString(R.string.mileage) + response.drivingDistance + getString(R.string.kilometre));
                    }*/
                    if (!TextUtils.isEmpty(response.lastMaintenanceDate)) {
                        mMaintainView.setVisibility(View.VISIBLE);
                        mMaintainView.setText(getString(R.string.maintenance_time) + response.lastMaintenanceDate);
                    } else {
                        mMaintainView.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(response.drivingDistance) && Integer.parseInt(response.drivingDistance) > 0) {
                        mDriveDistanceView.setVisibility(View.VISIBLE);
                        mDriveDistanceView.setText(getString(R.string.mileage) + response.drivingDistance + getString(R.string.kilometre));
                    } else {
                        mDriveDistanceView.setVisibility(View.GONE);
                    }
                }
                if (TextUtils.isEmpty(mQualityDateView.getText())) {
                    if (TextUtils.isEmpty(response.guaranteePeriod)) {
                        mQualityDateView.setVisibility(View.GONE);
                    } else {
                        mQualityDateView.setVisibility(View.VISIBLE);
                        mQualityDateView.setText(getString(R.string.warranty) + response.guaranteePeriod);
                    }
                }
                if (TextUtils.isEmpty(mAutoTypeView.getText())) {
                    if (!TextUtils.isEmpty(response.autoModel)) {
                        if (imMyAuto != null) {
                            imMyAuto.brand = response.brand;
                            imMyAuto.brandID = response.brandID;
                            imMyAuto.series = response.series;
                            imMyAuto.seriesID = response.seriesID;
                            imMyAuto.autoModel = response.autoModel;
                            imMyAuto.autoModelID = response.autoModelID;
                        }
                        mAutoTypeView.setText(response.autoModel);
                    }
                }
            }
        }

        @Override
        public void onFailed(boolean offLine) {
            mMaintainInfoView.setVisibility(View.GONE);
            mQualityDateView.setVisibility(View.GONE);
        }
    };

    /*@Override
    public void onCheckSucceed(MyAuto myAuto) {
        if (myAuto != null) {
            if (TextUtils.isEmpty(mCustomView.getContentText())) {
                if (!TextUtils.isEmpty(myAuto.ownName)) {
                    mCustomView.setContentText(myAuto.ownName);
                }
            }
            if (TextUtils.isEmpty(mPhoneNumView.getContentText())) {
                if (!TextUtils.isEmpty(myAuto.ownPhone)) {
                    mPhoneNumView.setContentText(myAuto.ownPhone);
                }
            }
            if(TextUtils.isEmpty(mMaintainView.getText())) {
            if (TextUtils.isEmpty(myAuto.lastMaintenancDate) && TextUtils.isEmpty(myAuto.drivingDistance) && TextUtils.isEmpty(myAuto.guaranteePeriod)) {
//            mMaintainView.setVisibility(View.GONE);
//            mQualityDateView.setVisibility(View.GONE);
                mMaintainInfoView.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(myAuto.lastMaintenancDate) || !TextUtils.isEmpty(myAuto.drivingDistance) || !TextUtils.isEmpty(myAuto.guaranteePeriod)) {
//            mMaintainView.setVisibility(View.VISIBLE);
//            mQualityDateView.setVisibility(View.VISIBLE);
                mMaintainInfoView.setVisibility(View.VISIBLE);
            }
                if (!TextUtils.isEmpty(myAuto.lastMaintenancDate) && !TextUtils.isEmpty(myAuto.drivingDistance)) {
                    mMaintainView.setText("您上次保养的时间" + myAuto.lastMaintenancDate + " " + "行驶里程" + myAuto.drivingDistance + "公里");
                } else if (!TextUtils.isEmpty(myAuto.lastMaintenancDate)) {
                    mMaintainView.setText("您上次保养的时间" + myAuto.lastMaintenancDate);
                } else if (!TextUtils.isEmpty(myAuto.drivingDistance)) {
                    mMaintainView.setText("行驶里程" + myAuto.drivingDistance + "公里");
                }
            }
            if(TextUtils.isEmpty(mQualityDateView.getText())) {
                if (TextUtils.isEmpty(myAuto.guaranteePeriod)) {
                    mQualityDateView.setVisibility(View.GONE);
                } else {
                    mQualityDateView.setVisibility(View.VISIBLE);
                    mQualityDateView.setText("质保到" + myAuto.guaranteePeriod);
                }
            }
            if(TextUtils.isEmpty(mAutoTypeView.getText())) {
                if (!TextUtils.isEmpty(myAuto.autoModel)) {
                    auto = new Auto(myAuto.brandID, myAuto.brand,
                            myAuto.seriesID, myAuto.series,
                            myAuto.autoModel, myAuto.autoModelID);
                    mAutoTypeView.setText(myAuto.autoModel);
                }
            }
        }
    }

    @Override
    public void onCheckFailed(boolean offLine) {
        mMaintainInfoView.setVisibility(View.GONE);
        mQualityDateView.setVisibility(View.GONE);
    }*/

    @Override
    protected void onDestroy() {
        DebugLog.i(TAG, "onDestroy");
        ReservationMaintainControl.getInstance().killInstance();
        super.onDestroy();
    }

    private CallBackControl.CallBack<ThirdPartShop> thirdPartShopCallBack = new CallBackControl.CallBack<ThirdPartShop>() {
        @Override
        public void onSuccess(ThirdPartShop response) {
            mRequestFailView.setVisibility(View.GONE);
            mShopDetailsHeadView.bindData(response.getShopInfo());
            shopInfo = response.getShopInfo();
            mCallBarView.setTitle("售后电话");
            mCallBarView.setNumber(shopInfo.serviceHotline);
            mActionBar.setTitle(shopInfo.shopTitle);
            mShopView.setContentText(shopInfo.shopTitle);
            shopTitle = shopInfo.shopTitle;
            DebugLog.i(TAG, "shopTitle:" + shopTitle);
                /*if (!TextUtils.isEmpty(UserInfoHelper.getInstance().getUser(this).fullname)) {
                    mCustomView.setContentText(UserInfoHelper.getInstance().getUser(this).fullname);
                }

                if (!TextUtils.isEmpty(UserInfoHelper.getInstance().getUser(this).phoneNumber)) {
                    mPhoneNumView.setContentText(UserInfoHelper.getInstance().getUser(this).phoneNumber);
                }*/
            mPlateNumberView.setPlateNumber("",true);
            if (TextUtils.isEmpty(mCustomView.getContentText())) {
                UserInfoHelper.getInstance().getUserInfo(ReserveMaintainAndHeadActivityO1.this, new UserInfoHelper.UserInfoAction() {
                    @Override
                    public void showUserInfo(User meData) {
                        mCustomView.setContentText(meData.fullname);
                        mPhoneNumView.setContentText(meData.phoneNumber);
                    }

                    @Override
                    public void onFinish() {

                    }
                }, true);
            }
//        mPlateNumberView.setCheckListener(checkListener);
//            mPlateNumberView.setCheckListener();
        }

        @Override
        public void onFailed(boolean offLine) {
//        notShop();
            mRequestFailView.notShop(ReserveMaintainAndHeadActivityO1.this, "店铺不存在");
        }
    };

    /*@Override
    public void onSucceed(ThirdPartShop thirdPartShop) {
        mRequestFailView.setVisibility(View.GONE);
        mShopDetailsHeadView.bindData(thirdPartShop.getShopInfo());
        shopInfo = thirdPartShop.getShopInfo();
        mCallBarView.setNumber(shopInfo.shopTel);
        mActionBar.setTitle(shopInfo.shopTitle);
        mShopView.setContentText(shopInfo.shopTitle);
        shopTitle = shopInfo.shopTitle;
    }

    @Override
    public void onFailed(boolean offLine) {
//        notShop();
        mRequestFailView.notShop(this, "店铺不存在");
    }*/

/*    public void notShop() {
        mRequestFailView.setEmptyDescription("店铺不存在");
        mRequestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        mRequestFailView.setVisibility(View.VISIBLE);
    }*/

    private ArrayList<ServiceType> serviceTypes;

    /*@Override
    public void onServiceTypeSucceed(ArrayList<ServiceType> serviceType) {
        serviceTypes = serviceType;
        if (serviceType != null && serviceType.size() > 0) {
            this.serviceType = serviceType.get(0);
            mSelectServiceTypeView.setContentText(serviceType.get(0).kindTitle);
        }
    }

    @Override
    public void onServiceTypeFailed(boolean offLine) {

    }*/

    private CallBackControl.CallBack<ArrayList<ServiceType>> serviceTypeCallBack = new CallBackControl.CallBack<ArrayList<ServiceType>>() {
        @Override
        public void onSuccess(ArrayList<ServiceType> response) {
            if (response != null && !response.isEmpty()) {
                DebugLog.i(TAG, "服务类型有值");
                serviceType = response.get(0);
                mSelectServiceTypeView.setContentText(response.get(0).kindTitle);
            }
            serviceTypes = response;
        }

        @Override
        public void onFailed(boolean offLine) {

        }
    };

    /**
     * 服务器返回的数据导入通用方法
     *
     * @param myAuto
     */
    @Deprecated
    private void fillData(MyAuto myAuto) {
        if (TextUtils.isEmpty(mPlateNumberView.getPlateNumber())) {
            if (!TextUtils.isEmpty(myAuto.plateNumber)) {
                mPlateNumberView.setPlateNumber(myAuto.plateNumber,true);
            }
        }
        if (TextUtils.isEmpty(mCustomView.getContentText())) {
            if (!TextUtils.isEmpty(myAuto.ownName)) {
                mCustomView.setContentText(myAuto.ownName);
            }
        }
        if (TextUtils.isEmpty(mPhoneNumView.getContentText())) {
            if (!TextUtils.isEmpty(myAuto.ownPhone)) {
                mPhoneNumView.setContentText(myAuto.ownPhone);
            }
        }
        if (TextUtils.isEmpty(myAuto.lastMaintenanceDate) && TextUtils.isEmpty(myAuto.drivingDistance) && TextUtils.isEmpty(myAuto.guaranteePeriod)) {
//            mMaintainView.setVisibility(View.GONE);
//            mQualityDateView.setVisibility(View.GONE);
            mMaintainInfoView.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(myAuto.lastMaintenanceDate) || !TextUtils.isEmpty(myAuto.drivingDistance) || !TextUtils.isEmpty(myAuto.guaranteePeriod)) {
//            mMaintainView.setVisibility(View.VISIBLE);
//            mQualityDateView.setVisibility(View.VISIBLE);
            mMaintainInfoView.setVisibility(View.VISIBLE);
        }
       /* if (!TextUtils.isEmpty(myAuto.lastMaintenanceDate)) {
            mMaintainView.setText(getString(R.string.mileage) + myAuto.drivingDistance + getString(R.string.kilometre));
        } else if (!TextUtils.isEmpty(myAuto.drivingDistance)) {
            mMaintainView.setText(getString(R.string.maintenance_time) + myAuto.lastMaintenanceDate);
        } else if (!TextUtils.isEmpty(myAuto.lastMaintenanceDate) && !TextUtils.isEmpty(myAuto.drivingDistance)) {
            mMaintainView.setText(getString(R.string.maintenance_time) + myAuto.lastMaintenanceDate + " " + getString(R.string.mileage) + myAuto.drivingDistance + getString(R.string.kilometre));
        }*/
        if (!TextUtils.isEmpty(myAuto.lastMaintenanceDate)) {
            mMaintainView.setVisibility(View.VISIBLE);
            mMaintainView.setText(getString(R.string.maintenance_time) + myAuto.lastMaintenanceDate);
        } else {
            mMaintainView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(myAuto.drivingDistance) && Integer.parseInt(myAuto.drivingDistance) > 0) {
            mDriveDistanceView.setVisibility(View.VISIBLE);
            mDriveDistanceView.setText(getString(R.string.mileage) + myAuto.drivingDistance + getString(R.string.kilometre));
        } else {
            mDriveDistanceView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(myAuto.guaranteePeriod)) {
            mQualityDateView.setVisibility(View.GONE);
        } else {
            mQualityDateView.setVisibility(View.VISIBLE);
            mQualityDateView.setText(getString(R.string.warranty) + myAuto.guaranteePeriod);
        }
        if (!TextUtils.isEmpty(myAuto.autoModel)) {
            mAutoTypeView.setText(myAuto.autoModel);
        }
    }

    /*@Override
    public void onCheckLocationSucceed(MyAuto myAuto) {
        DebugLog.i(TAG, "Location:" + myAuto.toString());
        setCheckData(myAuto);
    }

    @Override
    public void onCheckLocationFailed(MyAuto myAuto) {
        DebugLog.i(TAG, "本地无匹配");
        if (UserInfoHelper.getInstance().isLogin(this)) {
            AutoHelper.getInstance().checkNetWorkMyAuto(this, shopID, this);
        } else {
            mPlateNumberView.setCheckListener(checkListener);
        }
    }*/
    private CallBackControl.LocalCallBack<MyAuto> checkLocalCallBack = new CallBackControl.LocalCallBack<MyAuto>() {
        @Override
        public void onSuccess(MyAuto response) {
            DebugLog.i(TAG, "Location:" + response.toString());
            setCheckData(response);
        }

        @Override
        public void onFailed(MyAuto response) {
            DebugLog.i(TAG, "本地无匹配");
            if (UserInfoHelper.getInstance().isLogin(ReserveMaintainAndHeadActivityO1.this)) {
                DebugLog.i(TAG, "登录后");
                AutoHelper.getInstance().checkNetWorkMyAuto(ReserveMaintainAndHeadActivityO1.this, shopID, checkNetWorkCallBack);
            } else {
                DebugLog.i(TAG, "未登录");
//                mPlateNumberView.setCheckListener(checkListener);
                setCheckData(new MyAuto());
            }
        }
    };

    /*@Override
    public void onCheckNetWorkSucceed(MyAuto myAuto) {
        DebugLog.i(TAG, "Location:" + myAuto.toString());
        setCheckData(myAuto);
    }

    @Override
    public void onCheckNetWorkFailed(MyAuto myAuto) {
        DebugLog.i(TAG, "网络无匹配");
        mPlateNumberView.setCheckListener(checkListener);
    }*/

    private CallBackControl.LocalCallBack<MyAuto> checkNetWorkCallBack = new CallBackControl.LocalCallBack<MyAuto>() {
        @Override
        public void onSuccess(MyAuto response) {
            DebugLog.i(TAG, "Location:" + response.toString());
            setCheckData(response);
        }

        @Override
        public void onFailed(MyAuto response) {
            DebugLog.i(TAG, "网络无匹配");
//            mPlateNumberView.setCheckListener(checkListener);
            setCheckData(new MyAuto());
        }
    };

    /**
     * 展示数据
     *
     * @param checkMyAuto
     */
    private void setCheckData(MyAuto checkMyAuto) {
        if (checkMyAuto != null) {

            imMyAuto = checkMyAuto;
            if (!TextUtils.isEmpty(checkMyAuto.plateNumber)) {
                mPlateNumberView.setPlateNumber(checkMyAuto.plateNumber,true);
            }
            if (TextUtils.isEmpty(mAutoTypeView.getText())) {
                if (!TextUtils.isEmpty(checkMyAuto.autoModel)) {
                    mAutoTypeView.setText(checkMyAuto.autoModel);
                }
            }

            if (TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate) && TextUtils.isEmpty(checkMyAuto.drivingDistance) && TextUtils.isEmpty(checkMyAuto.guaranteePeriod)) {
                mMaintainInfoView.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(checkMyAuto.drivingDistance) && Integer.parseInt(checkMyAuto.drivingDistance) <= 0) {
                mMaintainInfoView.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate) || !TextUtils.isEmpty(checkMyAuto.drivingDistance) || !TextUtils.isEmpty(checkMyAuto.guaranteePeriod)) {
                mMaintainInfoView.setVisibility(View.VISIBLE);
            }
        /*    if (!TextUtils.isEmpty(checkMyAuto.lastMaintenancDate)) {
                mMaintainView.setText("您上次保养的时间" + checkMyAuto.lastMaintenancDate);
            } else if (!TextUtils.isEmpty(checkMyAuto.drivingDistance)) {
                mMaintainView.setText("行驶里程" + checkMyAuto.drivingDistance + "公里");
            } else if (!TextUtils.isEmpty(checkMyAuto.lastMaintenancDate) && !TextUtils.isEmpty(checkMyAuto.drivingDistance)) {
                mMaintainView.setText("您上次保养的时间" + checkMyAuto.lastMaintenancDate + " " + "行驶里程" + checkMyAuto.drivingDistance + "公里");
            }*/
            if (!TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate) && !TextUtils.isEmpty(checkMyAuto.drivingDistance)) {
                mMaintainView.setText(getString(R.string.maintenance_time) + checkMyAuto.lastMaintenanceDate + " " + getString(R.string.mileage) + checkMyAuto.drivingDistance + getString(R.string.kilometre));
            } else if (!TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate)) {
                mMaintainView.setText(getString(R.string.maintenance_time) + checkMyAuto.lastMaintenanceDate);
            } else if (!TextUtils.isEmpty(checkMyAuto.drivingDistance)) {
                mMaintainView.setText(getString(R.string.mileage) + checkMyAuto.drivingDistance + getString(R.string.kilometre));
            }
            if (!TextUtils.isEmpty(checkMyAuto.guaranteePeriod)) {
                mQualityDateView.setVisibility(View.VISIBLE);
                mQualityDateView.setText(getString(R.string.warranty) + checkMyAuto.guaranteePeriod);
            } else {
                mQualityDateView.setVisibility(View.GONE);
            }

            UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
                @Override
                public void showUserInfo(User meData) {
                    mCustomView.setContentText(meData.fullname);
                    mPhoneNumView.setContentText(meData.phoneNumber);
                }

                @Override
                public void onFinish() {

                }
            }, false);
      /*      if (!TextUtils.isEmpty(checkMyAuto.ownName)) {
                mCustomView.setContentText(checkMyAuto.ownName);
            }*/
   /*         if (!TextUtils.isEmpty(checkMyAuto.ownPhone)) {
                mPhoneNumView.setContentText(checkMyAuto.ownPhone);
            }*/
        }
        if (!TextUtils.isEmpty(shopTitle)) {
            mShopView.setContentText(shopTitle);
        }
        DebugLog.i(TAG, "shopTitle:" + shopTitle);
//        mPlateNumberView.setCheckListener(checkListener);
//        mPlateNumberView.setCheckListener();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mPlateNumberView.onKeyDown(keyCode,event);
        return super.onKeyDown(keyCode, event);
    }
}
