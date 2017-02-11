package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hxqc.mall.activity.FocusEditNoBackActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.fragment.AutoTypeFragment;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.view.CommonAutoInfoHeaderViewV2;
import com.hxqc.mall.auto.view.CommonEditInfoItemView;
import com.hxqc.mall.auto.view.PlateNumberEditText;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.thirdshop.R;
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
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.mall.thirdshop.views.ShopDetailsHeadView;
import com.hxqc.util.DebugLog;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import java.util.ArrayList;

/**
 * Author :胡仲俊
 * Date : 2016-03-07
 * Des: 在线预约
 * FIXME
 * Todo
 */
public class ReserveMaintainActivity extends FocusEditNoBackActivity implements ReserveDateDialog.OnFinishClickListener,
        MenuDrawer.OnDrawerStateChangeListener, AutoTypeFragment.onBackData {

    private static final String TAG = AutoInfoContants.LOG_J;
    public static final String MAINTEANCESHOP = "mainteanceShop";
    private EditText mVINView;//VIN
    private CommonEditInfoItemView mSelectServiceTypeView;//选择服务类型
    private CommonEditInfoItemView mSelectReserveDateView; //预约时间
    //    private CommonEditInfoItemView mSelectCounselorView; //预约服务顾问
//    private CommonEditInfoItemView mSelectMechanicView; //预约维修技师
    private Button mReserveSuccessView; //提交
    private CommonAutoInfoHeaderViewV2 mCommonAutoInfoHeaderView;
    private EditTextValidatorView mAutoTypeView;//车辆类型
    private ReserveDateDialog reserveDateDialog;
    private OverlayDrawer mOverlayDrawer;
    private TextView mMaintainView;//保养时间及里程
    private TextView mQualityDateView;//质保期
    private CommonEditInfoItemView mCustomView;//联系人姓名
    private CommonEditInfoItemView mPhoneNumView;//手机号码
    private CommonEditInfoItemView mShopView;//服务门店
    private ReservationMaintainInfo reservationMaintainInfo;//提交数据
    private ServiceAdviser mServiceAdviser;
    private Mechanic mMechanic;
    private ServiceType serviceType;
//    private Auto auto;

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
                DebugLog.i(TAG, "handler:" + serviceType.kindTitle);
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
    private PlateNumberEditText mPlateNumberView;
    private RequestFailView mRequestFailView;
    private PickupPointT shopLocation;
    private MyAuto imMyAuto = null;
    private TextView mDriveDistanceView;
    private Toolbar toolbar;
    private LinearLayout mChangeView;
    private LinearLayout mHomeView;
    private MyAuto chooseAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reserve_maintain_info_v2);

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
                if (!TextUtils.isEmpty(mainteanceShop.shopID)) {
                    ReservationMaintainControl.getInstance().requestReservationMaintain(this, shopID, reservationMaintainCallBack);
                }
            }
        }

//        ReservationMaintainControl.getInstance().requestReservationMaintain(this, shopID, this);
//        autoTypeFragment.setShopID(shopID);
    }

    //初始化控件
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mChangeView = (LinearLayout) findViewById(R.id.maintain_info_change);
        mHomeView = (LinearLayout) findViewById(R.id.maintain_info_home);
        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.reserve_maintain_drawer);
        autoTypeFragment = (AutoTypeFragment) getSupportFragmentManager().findFragmentById(R.id.reserve_maintain_menu);
        mCommonAutoInfoHeaderView = (CommonAutoInfoHeaderViewV2) findViewById(R.id.reserve_maintain_info_header);
        mPlateNumberView = mCommonAutoInfoHeaderView.getmPlateNumView();
        mVINView = mCommonAutoInfoHeaderView.getmVINView();
        mAutoTypeView = mCommonAutoInfoHeaderView.getmAutoTypeView();
        mMaintainView = mCommonAutoInfoHeaderView.getmMaintainView();
        mDriveDistanceView = mCommonAutoInfoHeaderView.getmDriveDistanceView();
        mQualityDateView = mCommonAutoInfoHeaderView.getmQualityDateView();
        mMaintainInfoView = mCommonAutoInfoHeaderView.getmMaintainInfoView();
//        mCommonAutoInfoHeaderView.setPlateNumTitleDrawLeft(R.drawable.bg_star);

        mScrollView = (ScrollView) findViewById(R.id.reserve_maintain_header_scroll);
        mSelectServiceTypeView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_select_service_type);
        mSelectReserveDateView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_select_reserve_date);
//        mSelectCounselorView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_service_adviser);
//        mSelectMechanicView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_service_mechanic);
        mReserveSuccessView = (Button) findViewById(R.id.reserve_maintain_reserve_success);

        mCustomView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_custom);
        mPhoneNumView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_phone_num);
        mShopView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_shop);

        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN_MENUOPEN);
        mOverlayDrawer.setSidewardCloseMenu(false);

        mRequestFailView = (RequestFailView) findViewById(R.id.reserve_maintain_fail_view);

    }

    //初始化事件
    private void initEvent() {

        mAutoTypeView.setOnClickListener(autoTypeClickListener);
        mOverlayDrawer.setOnDrawerStateChangeListener(this);
        autoTypeFragment.setOnBackDataListener(this);

        mShopView.setOnContentClickListener(shopClickListener);
        mSelectServiceTypeView.setOnContentClickListener(selectServiceTypeClickListener);
        mSelectReserveDateView.setOnContentClickListener(selectReserveDateClickListener);
//        mSelectCounselorView.setOnContentClickListener(selectCounselorClickListener);
//        mSelectMechanicView.setOnContentClickListener(selectMechanicClickListener);

        mReserveSuccessView.setOnClickListener(reserveSuccessClickListener);

        mChangeView.setOnClickListener(changeClickListener);
        mHomeView.setOnClickListener(homeClickListener);

//        mPlateNumberView.setCheckAutoListener(checkAutoListener);

    }

    private int imServiceAdviserPosition = -1;
    private int imServiceMechanicPosition = -1;

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(mCustomView.getContentText())) {
            UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundleExtra = data.getBundleExtra(ActivitySwitchBase.KEY_DATA);
            if (resultCode == MaintainContants.SERVICE_TYPE_DATA_BACK) {
        /*        String i = "";
                HashMap<Integer, String> serviceTypeBackData = (HashMap<Integer, String>) data.getSerializableExtra("serviceTypeBackData");
                Iterator iter = serviceTypeBackData.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    i += entry.getValue();
                }*/
//                ServiceType serviceType = data.getParcelableExtra("serviceTypeBackData");
                ServiceType serviceType = bundleExtra.getParcelable("serviceTypeBackData");
                DebugLog.i(TAG, "back:" + serviceType.toString());
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
            } else if (requestCode == AutoInfoContants.GET_AUTO_DATA) {
                chooseAuto = bundleExtra.getParcelable("myAuto");
                setCheckData(chooseAuto);
            }
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_tohome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_to_home) {
            ActivitySwitchBase.toMain(this, 0);
        } else if (item.getItemId() == R.id.action_to_change) {
            DebugLog.i(TAG, "换车");
            UserInfoHelper.getInstance().loginAction(ReserveMaintainActivity.this, new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
                    ActivitySwitchBase.toAutoInfo(ReserveMaintainActivity.this, "", AutoInfoContants.RESERVE_MAINTAIN);
                }
            });
        }

        return false;
    }*/

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
            if (shopLocation != null) {
                DebugLog.i(TAG, "mainteanceShop:" + shopLocation.toString());
                PickupPointT pickupPointT = new PickupPointT();
                pickupPointT.address = shopLocation.address;
                pickupPointT.latitude = shopLocation.latitude + "";
                pickupPointT.longitude = shopLocation.longitude + "";
                pickupPointT.tel = shopLocation.tel;
                ActivitySwitchBase.toAMapNvai(ReserveMaintainActivity.this, pickupPointT);
            }
        }
    };

    //服务类型点击事件
    private CommonEditInfoItemView.onContentClickListener selectServiceTypeClickListener = new CommonEditInfoItemView.onContentClickListener() {
        @Override
        public void onContentClick(View v) {
            DebugLog.i(TAG, "shopID:" + shopID);
            ActivitySwitcherMaintenance.toServiceType(ReserveMaintainActivity.this, MaintainContants.SERVICE_TYPE_DATA, shopID, serviceTypes, serviceType);
        }
    };

    //预约时间点击事件
    private CommonEditInfoItemView.onContentClickListener selectReserveDateClickListener = new CommonEditInfoItemView.onContentClickListener() {
        @Override
        public void onContentClick(View v) {
//            reserveDateDialog = new ReserveDateDialog(ReserveMaintainActivity.this, "");
            if (reservationMaintainInfo != null) {
                reserveDateDialog = new ReserveDateDialog(ReserveMaintainActivity.this);
                reserveDateDialog.setOnFinishClickListener(ReserveMaintainActivity.this);
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
                    ActivitySwitcherMaintenance.toServiceAdviser(ReserveMaintainActivity.this, 10, shopID, reservationMaintainInfo.getServiceAdviser(), imServiceAdviserPosition);
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
                    ActivitySwitcherMaintenance.toServiceMechanic(ReserveMaintainActivity.this, 11, shopID, reservationMaintainInfo.getMechanic(), imServiceMechanicPosition);
                }
            }
        }
    };

    //提交点击事件
    private View.OnClickListener reserveSuccessClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserInfoHelper.getInstance().loginAction(ReserveMaintainActivity.this, new UserInfoHelper.OnLoginListener() {
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

    //提交前数据的校验
    private void analysisReservationSuccessInfo() {

        final ReservationSuccessInfo reservationSuccessInfo = new ReservationSuccessInfo();
        if (TextUtils.isEmpty(mPlateNumberView.getPlateNumber())) {
            ToastHelper.showYellowToast(this, "请填写车牌号");
            return;
        } else if (mPlateNumberView.getPlateNumber().length() < 7) {
            ToastHelper.showYellowToast(this, "车牌号格式不正确");
            return;
        }

        if (TextUtils.isEmpty(mAutoTypeView.getText())) {
            ToastHelper.showYellowToast(ReserveMaintainActivity.this, "请选择车型");
            return;
        }
        /*if (TextUtils.isEmpty(mCustomView.getContentText())) {
            ToastHelper.showYellowToast(this, "请填写车主姓名");
            return;
        }*/
        /*if (FormatCheck.checkName2(mCustomView.getContentText(), this) == FormatCheck.NO_REAL_NAME) {
            return;
        } else if (FormatCheck.checkName2(mCustomView.getContentText(), this) == FormatCheck.REAL_NAME_ERROR) {
            return;
        }*/

        if (!FormatCheck.checkRealName(mCustomView.getContentText(), this)) {
            return;
        }

        if (FormatCheck.checkPhoneNumber(mPhoneNumView.getContentText(), this) == FormatCheck.NO_PHONE_NUMBER) {
            return;
        } else if (FormatCheck.checkPhoneNumber(mPhoneNumView.getContentText(), this) == FormatCheck.PHONE_NUMBER_FORMAT_ERROR) {
            return;
        }

    /*    if (TextUtils.isEmpty(mPhoneNumView.getContentText())) {
            ToastHelper.showYellowToast(this, "请填写手机号码");
            return;
        }*/
        if (TextUtils.isEmpty(mSelectServiceTypeView.getContentText())) {
            ToastHelper.showYellowToast(ReserveMaintainActivity.this, "请选择服务类型");
            return;
        }
        if (TextUtils.isEmpty(mSelectReserveDateView.getContentText())) {
            ToastHelper.showYellowToast(ReserveMaintainActivity.this, "请选择预约时间");
            return;
        }
  /*      if (TextUtils.isEmpty(mSelectCounselorView.getContentText())) {
            ToastHelper.showYellowToast(this, "请选择服务顾问");
            return;
        }
        if (TextUtils.isEmpty(mSelectMechanicView.getContentText())) {
            ToastHelper.showYellowToast(this, "请选择服务技师");
            return;
        }*/
        reservationSuccessInfo.name = mCustomView.getContentText();
        reservationSuccessInfo.phone = mPhoneNumView.getContentText();
        reservationSuccessInfo.autoModel = imMyAuto.autoModel;
        reservationSuccessInfo.autoModelID = imMyAuto.autoModelID;
        reservationSuccessInfo.shop = shopTitle;
        reservationSuccessInfo.shopID = shopID;
        reservationSuccessInfo.shopType = shopType;
        reservationSuccessInfo.serviceType = serviceType.kindTitle;
        reservationSuccessInfo.serviceTypeID = serviceType.serviceType;
        reservationSuccessInfo.remark = serviceType.remark;
        reservationSuccessInfo.apppintmentDate = mSelectReserveDateView.getContentText();
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
        reservationSuccessInfo.VIN = mVINView.getText().toString();

        AutoTypeControl.getInstance().requestBrand(this, shopID, new CallBackControl.CallBack<ArrayList<BrandGroup>>() {
            @Override
            public void onSuccess(ArrayList<BrandGroup> myAutoGroups) {
                DebugLog.i(TAG, reservationSuccessInfo.autoModel.substring(0, 1) + "----111" + myAutoGroups.get(0).group.get(0).brandName.substring(0, 1));
                if (myAutoGroups != null && !myAutoGroups.isEmpty()) {
                    for (int i = 0; i < myAutoGroups.size(); i++) {
                        for (int j = 0; j < myAutoGroups.get(i).group.size(); j++) {
                            if (imMyAuto.brand.equals(myAutoGroups.get(i).group.get(j).brandName)) {
                                DebugLog.i(TAG, imMyAuto.brand + "----111" + myAutoGroups.get(0).group.get(0).brandName);
                                flag = true;
                                break;
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
                    if (flag) {
                        ActivitySwitcherMaintenance.toReserveSuccess(ReserveMaintainActivity.this, reservationSuccessInfo);
                    } else {
                        ToastHelper.showYellowToast(ReserveMaintainActivity.this, "请选择本店的车辆信息");

                    }
                }
            }

            @Override
            public void onFailed(boolean offLine) {

            }
        });
//        ActivitySwitcherMaintenance.toReserveSuccess(this, reservationSuccessInfo);

//        ActivitySwitcherMaintenance.toReserveSuccess(this, TestDataUtil.reserveTestData());
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

            /*ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(ReserveMaintainActivity.this, AutoHelper.RESERVE_MAINTAIN_AUTO_INFO);
            if (!autoLocal.isEmpty()) {
                setCheckData(autoLocal.get(0));
            } else {
                AutoHelper.getInstance().checkLocationMyAuto(ReserveMaintainActivity.this, shopID, checkLocalCallBack);
            }*/
//            AutoHelper.getInstance().checkNetWorkMyAuto(ReserveMaintainActivity.this, shopID, checkNetWorkCallBack);
            checkNetWorkMyAuto();
//        ReservationMaintainControl.getInstance().requestReservationMaintain(this, "1585067251822281", this);
            ReservationMaintainControl.getInstance().requestServiceType(ReserveMaintainActivity.this, shopID, serviceTypeCallBack);
            ReservationMaintainControl.getInstance().requestThirdpartshop(ReserveMaintainActivity.this, shopID, thirdPartShopCallBack);
            autoTypeFragment.setShopID(shopID);
        }


        @Override
        public void onFailed(boolean offLine) {
//        notShop();
//        ReservationMaintainControl.getInstance().killInstance();
            mRequestFailView.notShop(ReserveMaintainActivity.this, "店铺不存在");
        }
    };

    private CallBackControl.CallBack<MyAuto> checkCallBack = new CallBackControl.CallBack<MyAuto>() {
        @Override
        public void onSuccess(MyAuto response) {
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

        @Override
        public void onFailed(boolean offLine) {
            mMaintainInfoView.setVisibility(View.GONE);
            mQualityDateView.setVisibility(View.GONE);
        }
    };

    /*@Override
    public void onCheckSucceed(MyAuto myAuto) {
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
        if (TextUtils.isEmpty(mMaintainView.getText())) {
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
        if (TextUtils.isEmpty(mQualityDateView.getText())) {
            if (TextUtils.isEmpty(myAuto.guaranteePeriod)) {
                mQualityDateView.setVisibility(View.GONE);
            } else {
                mQualityDateView.setVisibility(View.VISIBLE);
                mQualityDateView.setText("质保到" + myAuto.guaranteePeriod);
            }
        }
        if (TextUtils.isEmpty(mAutoTypeView.getText())) {
            if (!TextUtils.isEmpty(myAuto.autoModel)) {
                auto = new Auto(myAuto.brandID, myAuto.brand,
                        myAuto.seriesID, myAuto.series,
                        myAuto.autoModel, myAuto.autoModelID);
                mAutoTypeView.setText(myAuto.autoModel);
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
//        AutoHelper.getInstance().editAutoDataLocal(this, chooseAuto, AutoHelper.RESERVE_MAINTAIN_AUTO_INFO);
        ReservationMaintainControl.getInstance().killInstance();
        AutoHelper.getInstance().killInstance(this);
        super.onDestroy();
    }

    /*@Override
    public void onThirdpartshopSucceed(ThirdPartShop thirdPartShop) {
        shopLocation = thirdPartShop.getShopInfo().getShopLocation();
    }

    @Override
    public void onThirdpartshopFailed(boolean offLine) {

    }*/

    private CallBackControl.CallBack<ThirdPartShop> thirdPartShopCallBack = new CallBackControl.CallBack<ThirdPartShop>() {
        @Override
        public void onSuccess(ThirdPartShop response) {
            shopLocation = response.getShopInfo().getShopLocation();
            if (!TextUtils.isEmpty(shopTitle)) {
                mShopView.setContentText(shopTitle);
            }
            DebugLog.i(TAG, "shopTitle:" + shopTitle);
//        mPlateNumberView.setCheckListener(checkListener);
//            mPlateNumberView.setCheckListener();
//            mPlateNumberView.dismissPopup();
            mPlateNumberView.clearFocus();
        }

        @Override
        public void onFailed(boolean offLine) {
            mRequestFailView.notShop(ReserveMaintainActivity.this, "店铺不存在");
        }
    };

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
            serviceTypes = response;
            if (response != null && response.size() > 0) {
                serviceType = response.get(0);
                mSelectServiceTypeView.setContentText(response.get(0).kindTitle);
            }
        }

        @Override
        public void onFailed(boolean offLine) {

        }
    };

    private CallBackControl.LocalCallBack<MyAuto> checkLocalCallBack = new CallBackControl.LocalCallBack<MyAuto>() {
        @Override
        public void onSuccess(MyAuto response) {
            DebugLog.i(TAG, "Location:" + response.toString());
            setCheckData(response);
        }

        @Override
        public void onFailed(MyAuto response) {
            DebugLog.i(TAG, "本地无匹配");
            if (UserInfoHelper.getInstance().isLogin(ReserveMaintainActivity.this)) {
                AutoHelper.getInstance().checkNetWorkMyAuto(ReserveMaintainActivity.this, shopID, checkNetWorkCallBack);
            } else {
                setCheckData(response);
//                mPlateNumberView.setCheckListener(checkListener);
            }
        }
    };

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
            setCheckData(myAuto);
            mPlateNumberView.setCheckListener(checkListener);
        }
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
            setCheckData(new MyAuto());
//            mPlateNumberView.setCheckListener(checkListener);
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
        setCheckData(myAuto);
        mPlateNumberView.setCheckListener(checkListener);
    }*/

  /*  public void notShop() {
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

    /**
     * 展示数据
     *
     * @param checkMyAuto
     */
    private void setCheckData(MyAuto checkMyAuto) {
        DebugLog.i(TAG, "setCheckData");
        if (checkMyAuto != null) {
            imMyAuto = checkMyAuto;
            if (!TextUtils.isEmpty(checkMyAuto.plateNumber)) {
                mPlateNumberView.setPlateNumber(checkMyAuto.plateNumber);
            }
            if (TextUtils.isEmpty(mAutoTypeView.getText())) {
                if (!TextUtils.isEmpty(checkMyAuto.autoModel)) {
                    mAutoTypeView.setText(checkMyAuto.autoModel);
                }
            }
            if (!TextUtils.isEmpty(checkMyAuto.autoModel)) {
                mAutoTypeView.setText(checkMyAuto.autoModel);
            }

            if (TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate) && TextUtils.isEmpty(checkMyAuto.drivingDistance) && TextUtils.isEmpty(checkMyAuto.guaranteePeriod)) {
                mMaintainInfoView.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(checkMyAuto.drivingDistance) && Integer.parseInt(checkMyAuto.drivingDistance) <= 0) {
                mMaintainInfoView.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate) || !TextUtils.isEmpty(checkMyAuto.drivingDistance) || !TextUtils.isEmpty(checkMyAuto.guaranteePeriod)) {
                mMaintainInfoView.setVisibility(View.VISIBLE);
            }

            /*if (!TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate) && !TextUtils.isEmpty(checkMyAuto.drivingDistance)) {
                mMaintainView.setText(getString(R.string.maintenance_time) + checkMyAuto.lastMaintenanceDate + " " + getString(R.string.mileage) + checkMyAuto.drivingDistance + getString(R.string.kilometre));
            } else if (!TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate)) {
                mMaintainView.setText(getString(R.string.maintenance_time) + checkMyAuto.lastMaintenanceDate);
            } else if (!TextUtils.isEmpty(checkMyAuto.drivingDistance)) {
                mMaintainView.setText(getString(R.string.mileage) + checkMyAuto.drivingDistance + getString(R.string.kilometre));
            }*/
            if (!TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate)) {
                mMaintainView.setVisibility(View.VISIBLE);
                mMaintainView.setText(getString(R.string.maintenance_time) + checkMyAuto.lastMaintenanceDate);
            } else {
                mMaintainView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(checkMyAuto.drivingDistance) && Integer.parseInt(checkMyAuto.drivingDistance) > 0) {
                mDriveDistanceView.setVisibility(View.VISIBLE);
                mDriveDistanceView.setText(getString(R.string.mileage) + checkMyAuto.drivingDistance + getString(R.string.kilometre));
            } else {
                mDriveDistanceView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(checkMyAuto.guaranteePeriod)) {
                mQualityDateView.setVisibility(View.VISIBLE);
                mQualityDateView.setText(getString(R.string.warranty) + checkMyAuto.guaranteePeriod);
            } else {
                mQualityDateView.setVisibility(View.GONE);
            }

            /*if (UserInfoHelper.getInstance().getUser(this) != null) {
                if (!TextUtils.isEmpty(UserInfoHelper.getInstance().getUser(this).fullname)) {
                    mCustomView.setContentText(UserInfoHelper.getInstance().getUser(this).fullname);
                }

                if (!TextUtils.isEmpty(UserInfoHelper.getInstance().getUser(this).phoneNumber)) {
                    mPhoneNumView.setContentText(UserInfoHelper.getInstance().getUser(this).phoneNumber);
                }
            }*/
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
//        mPlateNumberView.dismissPopup();
        mPlateNumberView.clearFocus();
    }

    /**
     * 展示数据
     *
     * @param checkMyAuto
     */
    private void setCheckData2(MyAuto checkMyAuto) {
        DebugLog.i(TAG, "setCheckData2");
        if (checkMyAuto != null) {
            imMyAuto = checkMyAuto;
            /*if (!TextUtils.isEmpty(checkMyAuto.plateNumber)) {
                mPlateNumberView.setPlateNumber(checkMyAuto.plateNumber.substring(0, 1), checkMyAuto.plateNumber.substring(1, 7));
            }*/
            if (TextUtils.isEmpty(mAutoTypeView.getText())) {
                if (!TextUtils.isEmpty(checkMyAuto.autoModel)) {
                    mAutoTypeView.setText(checkMyAuto.autoModel);
                }
            }
            if (!TextUtils.isEmpty(checkMyAuto.autoModel)) {
                mAutoTypeView.setText(checkMyAuto.autoModel);
            }

            if (TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate) && TextUtils.isEmpty(checkMyAuto.drivingDistance) && TextUtils.isEmpty(checkMyAuto.guaranteePeriod)) {
                mMaintainInfoView.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(checkMyAuto.drivingDistance) && Integer.parseInt(checkMyAuto.drivingDistance) <= 0) {
                mMaintainInfoView.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate) || !TextUtils.isEmpty(checkMyAuto.drivingDistance) || !TextUtils.isEmpty(checkMyAuto.guaranteePeriod)) {
                mMaintainInfoView.setVisibility(View.VISIBLE);
            }

            /*if (!TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate) && !TextUtils.isEmpty(checkMyAuto.drivingDistance)) {
                mMaintainView.setText(getString(R.string.maintenance_time) + checkMyAuto.lastMaintenanceDate + " " + getString(R.string.mileage) + checkMyAuto.drivingDistance + getString(R.string.kilometre));
            } else if (!TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate)) {
                mMaintainView.setText(getString(R.string.maintenance_time) + checkMyAuto.lastMaintenanceDate);
            } else if (!TextUtils.isEmpty(checkMyAuto.drivingDistance)) {
                mMaintainView.setText(getString(R.string.mileage) + checkMyAuto.drivingDistance + getString(R.string.kilometre));
            }*/
            if (!TextUtils.isEmpty(checkMyAuto.lastMaintenanceDate)) {
                mMaintainView.setVisibility(View.VISIBLE);
                mMaintainView.setText(getString(R.string.maintenance_time) + checkMyAuto.lastMaintenanceDate);
            } else {
                mMaintainView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(checkMyAuto.drivingDistance) && Integer.parseInt(checkMyAuto.drivingDistance) > 0) {
                mDriveDistanceView.setVisibility(View.VISIBLE);
                mDriveDistanceView.setText(getString(R.string.mileage) + checkMyAuto.drivingDistance + getString(R.string.kilometre));
            } else {
                mDriveDistanceView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(checkMyAuto.guaranteePeriod)) {
                mQualityDateView.setVisibility(View.VISIBLE);
                mQualityDateView.setText(getString(R.string.warranty) + checkMyAuto.guaranteePeriod);
            } else {
                mQualityDateView.setVisibility(View.GONE);
            }

            /*if (UserInfoHelper.getInstance().getUser(this) != null) {
                if (!TextUtils.isEmpty(UserInfoHelper.getInstance().getUser(this).fullname)) {
                    mCustomView.setContentText(UserInfoHelper.getInstance().getUser(this).fullname);
                }

                if (!TextUtils.isEmpty(UserInfoHelper.getInstance().getUser(this).phoneNumber)) {
                    mPhoneNumView.setContentText(UserInfoHelper.getInstance().getUser(this).phoneNumber);
                }
            }*/
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
    }

    private View.OnClickListener changeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserInfoHelper.getInstance().loginAction(ReserveMaintainActivity.this, new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
//                    ActivitySwitchBase.toMaintainAutoInfo(ReserveMaintainActivity.this, "", AutoInfoContants.RESERVE_MAINTAIN, allBrands);
                    //TODO 跳转有问题
                    AutoInfoControl.getInstance().chooseAuto(ReserveMaintainActivity.this,null,"",AutoInfoContants.RESERVE_MAINTAIN,allBrands);
                }
            });
        }
    };

    private View.OnClickListener homeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivitySwitchBase.toMain(ReserveMaintainActivity.this, 0);
        }
    };

    private ArrayList<Brand> allBrands;

    /**
     * 检验网络车辆与店铺品牌是否对应方法
     */
    private void checkNetWorkMyAuto() {
        AutoTypeControl.getInstance().requestBrand(this, shopID, new CallBackControl.CallBack<ArrayList<BrandGroup>>() {

            @Override
            public void onSuccess(ArrayList<BrandGroup> response) {
                DebugLog.i(TAG, "检验网络车辆与店铺品牌");
                allBrands = AutoHelper.getInstance().getAllBrands(response);
                allBrands = distinctArrayList(allBrands);
                /*if (UserInfoHelper.getInstance().isLogin(ReserveMaintainActivity.this)) {
                    ArrayList<MyAuto> autoLocal = AutoHelper.getInstance().getAutoLocal(ReserveMaintainActivity.this, AutoHelper.AUTO_INFO);
                    if (autoLocal == null && autoLocal.isEmpty()) {
                        DebugLog.i(TAG, "本地无车辆缓存数据");
                        AutoInfoControl.getInstance().requestAutoInfo(ReserveMaintainActivity.this, new CallBack<ArrayList<MyAuto>>() {
                            @Override
                            public void onSuccess(ArrayList<MyAuto> response) {
                                matchingAutoResult(response);
                            }

                            @Override
                            public void onFailed(boolean offLine) {
                                setCheckData(new MyAuto());
                            }
                        });
                    } else {
                        DebugLog.i(TAG, "本地有车辆缓存数据");
                        matchingAutoResult(autoLocal);
                    }
                } else {
                    setCheckData(new MyAuto());
                }*/
            }

            @Override
            public void onFailed(boolean offLine) {

            }

        });
    }

    /**
     * @param brands
     * @return
     */
    private ArrayList<Brand> distinctArrayList(ArrayList<Brand> brands) {
        ArrayList<Brand> newBrands = new ArrayList<>();
        for (int i = 0; i < brands.size(); i++) {
            if (i == 0) {
                newBrands.add(brands.get(i));
            } else if (i > 0 && i < brands.size() - 1) {
                if (!brands.get(i).brandID.equals(brands.get(i + 1).brandID)) {
                    newBrands.add(brands.get(i));
                }
            } else if (i == brands.size() - 1) {
                for (int j = 0; j < brands.size(); j++) {
                    if (!brands.get(i).brandID.equals(brands.get(j).brandID)) {
                        newBrands.add(brands.get(i));
                    }
                }
            }
        }
        return newBrands;
    }

    /**
     * @param response
     */
    private void matchingAutoResult(ArrayList<MyAuto> response) {
        MyAuto myAuto = null;
        if (response != null && !response.isEmpty()) {
            for (int i = 0; i < response.size(); i++) {
                for (int j = 0; j < allBrands.size(); j++) {
                    if (response.get(i).brand.equals(allBrands.get(j).brandName)) {
                        myAuto = response.get(i);
                        break;
                    }
                }
                if (myAuto != null) {
                    break;
                }
            }
            if (myAuto != null) {
                setCheckData(myAuto);
            } else {
                setCheckData(new MyAuto());
            }
        } else {
            setCheckData(new MyAuto());
        }
    }

}
