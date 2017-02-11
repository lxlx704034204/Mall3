package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.controler.AutoSPControl;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.event.ReserveMaintainAndHeadActivityEvent;
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
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.maintenance.config.MaintainContants;
import com.hxqc.mall.thirdshop.maintenance.control.ReservationMaintainControl;
import com.hxqc.mall.thirdshop.maintenance.model.MaintenanceShop;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ReservationMaintainInfo;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ReservationSuccessInfo;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ServiceType;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.thirdshop.maintenance.views.ReserveDateDialog;
import com.hxqc.mall.thirdshop.maintenance.views.VehicleTypeStateLayout;
import com.hxqc.mall.thirdshop.model.ShopInfo;
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.mall.thirdshop.views.ShopDetailsHeadView;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Map;

/**
 * Author:胡仲俊
 * Date: 2016-03-07
 * Des: 修车预约V2
 * FIXME
 * Todo
 */

public class ReserveMaintainAndHeadActivity extends FoucsEditShopDetailActivity implements ReserveDateDialog.OnFinishClickListener {

    private static final String TAG = AutoInfoContants.LOG_J;
    private ScrollView mScrollView;
    private CallBar mCallBarView;
    private ActionBar mActionBar;
    private CommonEditInfoItemView mCustomView;//联系人姓名
    private CommonEditInfoItemView mPhoneNumView;//手机号码
    private CommonEditInfoItemView mShopView;//服务门店
    private CommonEditInfoItemView mSelectServiceTypeView;//选择服务类型
    private CommonEditInfoItemView mSelectReserveDateView; //预约时间
    private NewPlateNumberLayout mPlateNumberView;
    private EditTextValidatorView mNumberEditTextView;
    private EditTextValidatorView mServiceTypeView;
    private EditTextValidatorView mAppointmentTimeView;
    private EditTextValidatorView mNameView;
    private EditTextValidatorView mPhoneView;
    private Button mReserveSuccessView; //提交
    private RequestFailView mRequestFailView;

    private String shopType = "";
    private String shopID = "";
    private String shopTitle = "";
    private MaintenanceShop mainteanceShop;

    private ShopInfo shopInfo;

    private ServiceType serviceType;
    private ArrayList<ServiceType> serviceTypes;

    private ReservationMaintainInfo reservationMaintainInfo;//提交数据
    private ReserveDateDialog reserveDateDialog;

    private VWholeEditManager vWholeEditManager;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MaintainContants.RESERVATION_MAINTAIN_INFO_DATA) {
                reservationMaintainInfo = (ReservationMaintainInfo) msg.obj;
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
            } else if (msg.what == MaintainContants.AUTO_TYPE_BACK_DATA) {
                mMyAuto = (MyAuto) msg.obj;
                DebugLog.i(TAG, mMyAuto.toString());
                mShopDetailsHeadView.setMyAuto(mMyAuto);
                mVehicleTypeStateLayout.setLayoutChildState(false);
                mVehicleTypeStateLayout.setIsCompleteAutoContent(mMyAuto.brandThumb, mMyAuto.autoModel);
                if (!TextUtils.isEmpty(mMyAuto.plateNumber)) {
                    mVehicleTypeStateLayout.setFocusable(true);
                    mPlateNumberView.setPlateNumber(mMyAuto.plateNumber, true);
                }
                if (mMyAuto.authenticated == 1) {
                    mPlateNumberView.isIntercept(true);
                }

            }
        }
    };
    private VehicleTypeStateLayout mVehicleTypeStateLayout;
    private MyAuto mMyAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reserve_maintain_and_head_v2);

        vWholeEditManager = new VWholeEditManager(this);

        initView();

        initData();

        initEvent();

        EventBus.getDefault().register(this);
    }

    private void initEvent() {
        mShopView.setOnContentClickListener(shopClickListener);
        mSelectServiceTypeView.setOnContentClickListener(selectServiceTypeClickListener);
        mSelectReserveDateView.setOnContentClickListener(selectReserveDateClickListener);
        mReserveSuccessView.setOnClickListener(reserveSuccessClickListener);

        mVehicleTypeStateLayout.setVehicleTypeStateOnClickListener(vehicleTypeStateOnClickListener);
    }

    private void initData() {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            mainteanceShop = bundle.getParcelable(ReserveMaintainActivity.MAINTEANCESHOP);
            if (mainteanceShop != null) {
                shopID = mainteanceShop.shopID;
                shopTitle = mainteanceShop.shopTitle;
                shopType = mainteanceShop.shopType;
                DebugLog.i(TAG, shopID + "-----11----" + shopTitle + "-----11----" + shopType);
            }
            if (!TextUtils.isEmpty(bundle.getString(ShopDetailsController.SHOPID_KEY))) {
                shopID = bundle.getString(ShopDetailsController.SHOPID_KEY);
                shopType = bundle.getString(ShopDetailsController.SHOP_TYPE);
                DebugLog.i(TAG, shopID + "-----22----" + shopType);
            } else {
                Map<String, String> appointmentInfo = (Map<String, String>) AutoSPControl.getAppointmentInfo(this);
                shopID = appointmentInfo.get("shopID");
                shopType = appointmentInfo.get("shopType");
                DebugLog.i(TAG, shopID + "-----33----" + shopType);
            }
            mMyAuto = bundle.getParcelable("myAuto");
            if (mMyAuto != null) {
                DebugLog.i(TAG, mMyAuto.toString());
            }
            if (!TextUtils.isEmpty(shopID)) {
                ReservationMaintainControl.getInstance().requestReservationMaintain(this, shopID, reservationMaintainCallBack);
            }
        }
    }

    private void initView() {

        mShopDetailsHeadView = (ShopDetailsHeadView) findViewById(R.id.reserve_maintain_header_info_shop_detail_head);
        mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_XCYY);

        mVehicleTypeStateLayout = (VehicleTypeStateLayout) findViewById(R.id.reserve_maintain_header_vehicle_type_state);

        mCallBarView = (CallBar) findViewById(R.id.reserve_maintain_header_call_bar);

        mPlateNumberView = (NewPlateNumberLayout) findViewById(R.id.reserve_maintain_header_plate_number);
        mNumberEditTextView = mPlateNumberView.getNumberEditText();

        mScrollView = (ScrollView) findViewById(R.id.reserve_maintain_header_scroll);
        mSelectServiceTypeView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_header_select_service_type);
        mServiceTypeView = mSelectServiceTypeView.getmContentView();
        mSelectReserveDateView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_header_select_reserve_date);
        mAppointmentTimeView = mSelectReserveDateView.getmContentView();
        mReserveSuccessView = (Button) findViewById(R.id.reserve_maintain_header_reserve_success);

        mCustomView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_header_custom);
        mNameView = mCustomView.getmContentView();
        mPhoneNumView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_header_phone_num);
        mPhoneView = mPhoneNumView.getmContentView();
        mShopView = (CommonEditInfoItemView) findViewById(R.id.reserve_maintain_header_shop);

        mActionBar = getSupportActionBar();
        mRequestFailView = (RequestFailView) findViewById(R.id.reserve_maintain_header_fail_view);

        mCallBarView.setTitle("售后电话");

        vWholeEditManager.addEditView(mNumberEditTextView);
        vWholeEditManager.addEditView(mNameView);
        vWholeEditManager.addEditView(mPhoneView);
        vWholeEditManager.addEditView(mServiceTypeView);
        vWholeEditManager.addEditView(mAppointmentTimeView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getBundleExtra(ActivitySwitchBase.KEY_DATA);
            if (resultCode == MaintainContants.SERVICE_TYPE_DATA_BACK) {
          /*      String i = "";
                HashMap<Integer, String> serviceTypeBackData = (HashMap<Integer, String>) data.getSerializableExtra("serviceTypeBackData");
                Iterator iter = serviceTypeBackData.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    i += entry.getValue();
                }*/
//                ServiceType serviceType = data.getParcelableExtra("serviceTypeBackData");
                DebugLog.i(TAG, "SERVICE_TYPE_DATA_BACK");
                ServiceType serviceType = bundle.getParcelable("serviceTypeBackData");
                serviceTypes = bundle.getParcelableArrayList("serviceTypeGroupBackData");
                Message msg = Message.obtain();
                msg.what = MaintainContants.SERVICE_TYPE_DATA;
                msg.obj = serviceType;
                mHandler.sendMessage(msg);
            } else if (requestCode == AutoInfoContants.GET_AUTO_DATA) {
                DebugLog.i(TAG, "GET_AUTO_DATA");
                MyAuto myAuto = bundle.getParcelable("myAuto");
                Message msg = Message.obtain();
                msg.what = MaintainContants.AUTO_TYPE_BACK_DATA;
                msg.obj = myAuto;
                mHandler.sendMessage(msg);
            }
        }
    }

    @Subscribe
    public void onEventMainThread(ReserveMaintainAndHeadActivityEvent event) {
        DebugLog.i(TAG, "添加车辆成功");
        if (event != null && event.myAuto != null) {
            mVehicleTypeStateLayout.setLayoutChildState(false);
            mVehicleTypeStateLayout.setIsCompleteAutoContent(event.myAuto.brandThumb, event.myAuto.autoModel);
            mPlateNumberView.setPlateNumber(event.myAuto.plateNumber, true);
            mPlateNumberView.isIntercept(false);
            mShopDetailsHeadView.setMyAuto(event.myAuto);
            mMyAuto = event.myAuto;
        }

    }

    private CallBackControl.CallBack<ReservationMaintainInfo> reservationMaintainCallBack = new CallBackControl.CallBack<ReservationMaintainInfo>() {
        @Override
        public void onSuccess(ReservationMaintainInfo response) {
            mRequestFailView.setVisibility(View.GONE);
            Message msg = Message.obtain();
            msg.what = MaintainContants.RESERVATION_MAINTAIN_INFO_DATA;
            msg.obj = response;
            mHandler.sendMessage(msg);
            ReservationMaintainControl.getInstance().requestServiceType(ReserveMaintainAndHeadActivity.this, shopID, serviceTypeCallBack);
            mShopDetailsController.requestThirdPartShop(ReserveMaintainAndHeadActivity.this, thirdPartShopCallBack);
        }

        @Override
        public void onFailed(boolean offLine) {
//        notShop();
            mRequestFailView.notShop(ReserveMaintainAndHeadActivity.this, "店铺不存在");
        }
    };

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

    private CallBackControl.CallBack<ThirdPartShop> thirdPartShopCallBack = new CallBackControl.CallBack<ThirdPartShop>() {
        @Override
        public void onSuccess(ThirdPartShop response) {
            mRequestFailView.setVisibility(View.GONE);
            mShopDetailsHeadView.bindData(response.getShopInfo());
            mShopDetailsHeadView.setMyAuto(mMyAuto);
            shopInfo = response.getShopInfo();
            mCallBarView.setNumber(shopInfo.serviceHotline);
            mActionBar.setTitle(shopInfo.shopTitle);
            mShopView.setContentText(shopInfo.shopTitle);
            shopTitle = shopInfo.shopTitle;
            DebugLog.i(TAG, "shopTitle:" + shopTitle);
            if (mMyAuto != null) {
                if (mMyAuto.authenticated == 1) {
                    mPlateNumberView.setPlateNumber(mMyAuto.plateNumber, true);
                    mPlateNumberView.isIntercept(true);
                } else {
                    mPlateNumberView.setPlateNumber(mMyAuto.plateNumber, true);
                }

                if (!TextUtils.isEmpty(mMyAuto.brandID) && !TextUtils.isEmpty(mMyAuto.brand) && !TextUtils.isEmpty(mMyAuto.seriesID) && !TextUtils.isEmpty(mMyAuto.series) && !TextUtils.isEmpty(mMyAuto.autoModelID) && !TextUtils.isEmpty(mMyAuto.autoModel)) {
                    mVehicleTypeStateLayout.setLayoutChildState(false);
                    mVehicleTypeStateLayout.setIsCompleteAutoContent(mMyAuto.brandThumb, mMyAuto.autoModel);
                } else {
                    mVehicleTypeStateLayout.setLayoutChildState(true);
                }
            } else {
                mVehicleTypeStateLayout.setLayoutChildState(true);
                mPlateNumberView.setPlateNumber("", true);
            }
            if (TextUtils.isEmpty(mCustomView.getContentText())) {
                UserInfoHelper.getInstance().getUserInfo(ReserveMaintainAndHeadActivity.this, new UserInfoHelper.UserInfoAction() {
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
        public void onFailed(boolean offLine) {
//        notShop();
            mRequestFailView.notShop(ReserveMaintainAndHeadActivity.this, "店铺不存在");
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
                    ActivitySwitchBase.toAMapNvai(ReserveMaintainAndHeadActivity.this, pickupPointT);
                }
            }
        }
    };

    //服务类型点击事件
    private CommonEditInfoItemView.onContentClickListener selectServiceTypeClickListener = new CommonEditInfoItemView.onContentClickListener() {
        @Override
        public void onContentClick(View v) {
            DebugLog.i(TAG, "shopID:" + shopID);
            ActivitySwitcherMaintenance.toServiceType(ReserveMaintainAndHeadActivity.this, MaintainContants.SERVICE_TYPE_DATA, shopID, serviceTypes, serviceType);
        }
    };

    //预约时间点击事件
    private CommonEditInfoItemView.onContentClickListener selectReserveDateClickListener = new CommonEditInfoItemView.onContentClickListener() {
        @Override
        public void onContentClick(View v) {
            if (reservationMaintainInfo != null) {
                reserveDateDialog = new ReserveDateDialog(ReserveMaintainAndHeadActivity.this);
                reserveDateDialog.setOnFinishClickListener(ReserveMaintainAndHeadActivity.this);
                reserveDateDialog.build();
//                reserveDateDialog.setData(reservationMaintainInfo.apppintmentDate, mSelectReserveDateView.getContentText());
                reserveDateDialog.setDataNew(reservationMaintainInfo.apppintmentDateNew, mSelectReserveDateView.getContentText());
//                reserveDateDialog.analysisDateNew(reservationMaintainInfo.apppintmentDateNew);
                reserveDateDialog.create();

            }
        }
    };

    //提交点击事件
    private View.OnClickListener reserveSuccessClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserInfoHelper.getInstance().loginAction(ReserveMaintainAndHeadActivity.this, new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
                    analysisReservationSuccessInfo();
                }
            });
        }
    };

    @Override
    public void onFinishClick(View v, String dateAndTime) {
        mSelectReserveDateView.setContentText(dateAndTime);
    }

    private VehicleTypeStateLayout.OnClickListener vehicleTypeStateOnClickListener = new VehicleTypeStateLayout.OnClickListener() {
        @Override
        public void completeClickListener() {
            ActivitySwitchBase.toChooseBrandActivity(ReserveMaintainAndHeadActivity.this, new MyAuto(), AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_REPAIR_DETAIL, shopID, false);
        }

        @Override
        public void chooseAutoClickListener() {
            UserInfoHelper.getInstance().loginAction(ReserveMaintainAndHeadActivity.this, new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
                    AutoInfoControl.getInstance().chooseAuto(ReserveMaintainAndHeadActivity.this, mMyAuto, shopID, AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_REPAIR_DETAIL,null);
                }
            });

        }
    };

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

        if (TextUtils.isEmpty(mCustomView.getContentText())) {
            ToastHelper.showYellowToast(this, "请输入联系人姓名");
            vWholeEditManager.validate();
            return;
        }

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

        if (vWholeEditManager.validate()) {
            final ReservationSuccessInfo reservationSuccessInfo = new ReservationSuccessInfo();
            reservationSuccessInfo.name = mCustomView.getContentText().toString();
            reservationSuccessInfo.phone = mPhoneNumView.getContentText().toString();
            reservationSuccessInfo.autoModel = mMyAuto.autoModel;
            reservationSuccessInfo.autoModelID = mMyAuto.autoModelID;
            reservationSuccessInfo.shop = shopTitle;
            reservationSuccessInfo.shopID = shopID;
            reservationSuccessInfo.shopType = shopType;
            reservationSuccessInfo.serviceType = serviceType.kindTitle;
            reservationSuccessInfo.serviceTypeID = serviceType.serviceType;
            reservationSuccessInfo.remark = serviceType.remark;
            reservationSuccessInfo.apppintmentDate = mSelectReserveDateView.getContentText().toString();

            reservationSuccessInfo.plateNumber = mPlateNumberView.getPlateNumber();
            if (reservationMaintainInfo.getMyAuto() != null) {
                reservationSuccessInfo.drivingDistance = reservationMaintainInfo.getMyAuto().drivingDistance;
            }
//            reservationSuccessInfo.VIN = mVINView.getText().toString();
            DebugLog.i(TAG, reservationSuccessInfo.toString());
            mMyAuto.plateNumber = mPlateNumberView.getPlateNumber();
            DebugLog.i(TAG, mMyAuto.toString());
            AutoTypeControl.getInstance().requestBrand(this, shopID, new CallBackControl.CallBack<ArrayList<BrandGroup>>() {
                @Override
                public void onSuccess(ArrayList<BrandGroup> myAutoGroups) {
                    if (myAutoGroups != null && !myAutoGroups.isEmpty()) {
                        for (int i = 0; i < myAutoGroups.size(); i++) {
                            for (int j = 0; j < myAutoGroups.get(i).group.size(); j++) {
                                if (mMyAuto.brand.equals(myAutoGroups.get(i).group.get(j).brandName)) {
                                    DebugLog.i(TAG, mMyAuto.brand + "----111" + myAutoGroups.get(i).group.get(j).brandName);
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
                            ActivitySwitcherMaintenance.toReserveSuccess(ReserveMaintainAndHeadActivity.this, reservationSuccessInfo);
                        } else {
                            ToastHelper.showYellowToast(ReserveMaintainAndHeadActivity.this, "请选择本店的车辆信息");
                            return;
                        }
                    }
                }

                @Override
                public void onFailed(boolean offLine) {

                }
            });
            UserInfoHelper.getInstance().loginAction(ReserveMaintainAndHeadActivity.this, new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
                    AutoInfoControl.getInstance().checkAutoNetWork(ReserveMaintainAndHeadActivity.this, mMyAuto, new AutoInfoControl.CheckDataCallBack() {
                        @Override
                        public void checkData(boolean isCheck) {
                            DebugLog.i(TAG, isCheck + "isCheck");
                            if (!isCheck) {
                                AutoInfoControl.getInstance().addAutoInfo(ReserveMaintainAndHeadActivity.this, mMyAuto, new CallBackControl.CallBack<String>() {
                                    @Override
                                    public void onSuccess(String response) {

                                    }

                                    @Override
                                    public void onFailed(boolean offLine) {

                                    }
                                });
                            }
                        }
                    });
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
