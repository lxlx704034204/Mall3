package com.hxqc.mall.auto.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.auto.view.CallBar;
import com.hxqc.mall.auto.view.CommonRelativeTextView;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * Author:胡仲俊
 * Date: 2016 - 07 - 28
 * FIXME
 * Todo 车辆详情
 */
public class AutoDetailActivity extends NoBackActivity {

    private final String TAG = AutoInfoContants.LOG_J;
    private ImageView autoImgView;
    private TextView autoModelView;
    private TextView integralTextView;
    private LinearLayout integralRuleView;
    private LinearLayout integralDetailView;
    private CommonRelativeTextView travelMileageView;
    private CommonRelativeTextView registerTimeView;
    private CommonRelativeTextView plateNumberView;
    private CommonRelativeTextView engineNumberView;
    private CommonRelativeTextView vehicleNumberView;
    private LinearLayout maintenanceRecordView;
    private LinearLayout queryIllegalView;
    private LinearLayout annualVehicleView;
    private LinearLayout mMaintainRecordView;
    private MyAuto hmMyAuto;
    private boolean isEr = false;
    private RelativeLayout infoView;
    private LinearLayout completeInfoView;
    private LinearLayout changeModelView;
    private Button completeInfoBtnView;
    private TextView couponTextView;
    private LinearLayout couponRuleView;
    private LinearLayout couponDetailView;
    private LinearLayout integralView;
    private LinearLayout couponView;
    private Toolbar mToolbarView;
    private TextView mLevelView;
    private ImageView mLevelImgView;

    private CommonRelativeTextView mExpirationOfPolicyView;
    private CommonRelativeTextView mExamineDateView;
    private CommonRelativeTextView mGuaranteePeriodView;
    private CommonRelativeTextView mNextMaintenanceDateView;
    private LinearLayout mTimeParentView;
    private TextView mPhoneNumView;
    private CommonRelativeTextView mNextMaintenanceDistanceView;
    private TextView mPlateNumberTopView;
    private ImageView mQueryIllegalIconView;
    private TextView mQueryIllegalTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auto_detail);

        initView();

        initData();

        initEvent();

        EventBus.getDefault().register(this);
    }

    private void initEvent() {
        integralRuleView.setOnClickListener(integralRuleClickListener);
        integralDetailView.setOnClickListener(integralDetailClickListener);
        maintenanceRecordView.setOnClickListener(maintenanceRecordClickListener);
        queryIllegalView.setOnClickListener(queryIllegalClickListener);
        annualVehicleView.setOnClickListener(annualVehicleClickListener);
//        mMaintainRecordView.setOnClickListener(driverLicenseClickListener);
        mMaintainRecordView.setOnClickListener(toMaintainClickListener);
        completeInfoBtnView.setOnClickListener(completeInfoLicenseClickListener);
        changeModelView.setOnClickListener(completeInfoLicenseClickListener);
        couponRuleView.setOnClickListener(couponRuleClickListener);
        couponDetailView.setOnClickListener(couponDetailClickListener);

        integralView.setOnClickListener(integralDetailClickListener);
        couponView.setOnClickListener(couponDetailClickListener);

        mPhoneNumView.setOnClickListener(phoneNumClickListener);
    }

    private void initData() {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            hmMyAuto = bundle.getParcelable("myAuto");
            DebugLog.i(TAG, hmMyAuto.toString());
            if (hmMyAuto != null) {
                if (AutoInfoContants.IS_TEST) {
                    if (TextUtils.isEmpty(hmMyAuto.autoModel)) {
                        completeInfoView.setVisibility(View.VISIBLE);
                        infoView.setVisibility(View.VISIBLE);
                    } else {
                        infoView.setVisibility(View.VISIBLE);
                        completeInfoView.setVisibility(View.VISIBLE);
                        ImageUtil.setImage(this, autoImgView, hmMyAuto.brandThumb);
                        autoModelView.setText(hmMyAuto.autoModel);
                    }
                } else {
                    if (TextUtils.isEmpty(hmMyAuto.autoModel)) {
                        completeInfoView.setVisibility(View.VISIBLE);
                        infoView.setVisibility(View.GONE);
                    } else {
                        completeInfoView.setVisibility(View.GONE);
                        infoView.setVisibility(View.VISIBLE);
                        ImageUtil.setImageSquareOfAutoInfo(this, autoImgView, hmMyAuto.brandThumb);
                        autoModelView.setText(hmMyAuto.autoModel);
                    }
                }
                if (TextUtils.isEmpty(hmMyAuto.score)) {
                    integralTextView.setText(0 + "分");
                } else {
                    integralTextView.setText(hmMyAuto.score + "分");
                }
                travelMileageView.setTwoText(hmMyAuto.drivingDistance + "km");
                if (!TextUtils.isEmpty(hmMyAuto.registerTime)) {
                    String[] split = hmMyAuto.registerTime.split(" ");
                    registerTimeView.setTwoText(split[0]);
                }
                mPlateNumberTopView.setText(hmMyAuto.plateNumber);
                plateNumberView.setTwoText(hmMyAuto.plateNumber);
                checkPlateNumber(hmMyAuto);

                if (TextUtils.isEmpty(hmMyAuto.engineNum)) {
                    engineNumberView.setTwoText("");
                } else {
                    engineNumberView.setTwoText(hmMyAuto.engineNum);
                    if (hmMyAuto.engineNum.length() >= 5) {
                        engineNumberView.setTwoText("***" + hmMyAuto.engineNum.substring(hmMyAuto.engineNum.length() - 5));
                    }
                }

                if (TextUtils.isEmpty(hmMyAuto.VIN)) {
                    vehicleNumberView.setTwoText("");
                } else {
                    vehicleNumberView.setTwoText(hmMyAuto.VIN);
                    if (hmMyAuto.VIN.length() >= 5) {
                        vehicleNumberView.setTwoText("***" + hmMyAuto.VIN.substring(hmMyAuto.VIN.length() - 5));
                    }
                }

                if (hmMyAuto.authenticated == 0) {
                    maintenanceRecordView.setVisibility(View.GONE);
                    engineNumberView.setTwoText("");
                    vehicleNumberView.setTwoText("");

                }

                if (TextUtils.isEmpty(hmMyAuto.couponCount)) {
                    couponTextView.setText("0" + "张");
                } else {
                    couponTextView.setText(hmMyAuto.couponCount + "张");
                }
                if (TextUtils.isEmpty(hmMyAuto.Level)) {
                    mLevelImgView.setVisibility(View.GONE);
                    mLevelView.setVisibility(View.GONE);
                } else {
                    mLevelView.setVisibility(View.GONE);
                    mLevelImgView.setVisibility(View.GONE);
//                    mLevelView.setText(hmMyAuto.LevelText);
//                    ImageUtil.setImage(this, mLevelImgView, hmMyAuto.LevelIcon);
                    /*if (hmMyAuto.Level.equals("1")) {
                        setDrawable(R.drawable.ic_ordinary);
                        mLevelView.setText("普通会员");
                    } else if (hmMyAuto.Level.equals("2")) {
                        setDrawable(R.drawable.ic_copper);
                        mLevelView.setText("铜牌会员");
                    } else if (hmMyAuto.Level.equals("3")) {
                        setDrawable(R.drawable.ic_silver);
                        mLevelView.setText("银牌会员");
                    } else if (hmMyAuto.Level.equals("4")) {
                        setDrawable(R.drawable.ic_gold);
                        mLevelView.setText("金牌会员");
                    } else {
                        mLevelView.setVisibility(View.GONE);
                    }*/
                }

                if (!TextUtils.isEmpty(hmMyAuto.expirationOfPolicy) || !TextUtils.isEmpty(hmMyAuto.examineDate) || !TextUtils.isEmpty(hmMyAuto.guaranteePeriod) || !TextUtils.isEmpty(hmMyAuto.nextMaintenanceDate) || (!TextUtils.isEmpty(hmMyAuto.nextMaintenanceDistance + "") && hmMyAuto.nextMaintenanceDistance != 0)) {
                    mTimeParentView.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(hmMyAuto.expirationOfPolicy) && !hmMyAuto.expirationOfPolicy.equals("")) {
                    mExpirationOfPolicyView.setVisibility(View.VISIBLE);
                    mExpirationOfPolicyView.setTwoText(hmMyAuto.expirationOfPolicy);
                }

                if (!TextUtils.isEmpty(hmMyAuto.examineDate) && !hmMyAuto.examineDate.equals("")) {
                    mExamineDateView.setVisibility(View.VISIBLE);
                    String[] split = hmMyAuto.examineDate.split("-");
                    mExamineDateView.setTwoText(split[0] + "-" + split[1]);
                }

                if (!TextUtils.isEmpty(hmMyAuto.guaranteePeriod) && !hmMyAuto.guaranteePeriod.equals("")) {
                    mGuaranteePeriodView.setVisibility(View.VISIBLE);
                    mGuaranteePeriodView.setTwoText(hmMyAuto.guaranteePeriod);
                }

                if (!TextUtils.isEmpty(hmMyAuto.nextMaintenanceDate) && !hmMyAuto.nextMaintenanceDate.equals("")) {
                    mNextMaintenanceDateView.setVisibility(View.VISIBLE);
                    String[] split = hmMyAuto.nextMaintenanceDate.split("-");
                    mNextMaintenanceDateView.setTwoText(split[0] + "-" + split[1]);
                }

                if (!TextUtils.isEmpty(hmMyAuto.nextMaintenanceDistance + "") && hmMyAuto.nextMaintenanceDistance != 0) {
                    mNextMaintenanceDistanceView.setVisibility(View.VISIBLE);
                    mNextMaintenanceDistanceView.setTwoText(hmMyAuto.nextMaintenanceDistance + "km");
                }
            }
        }
    }

    private void checkPlateNumber(MyAuto myAuto) {
        if (!TextUtils.isEmpty(myAuto.plateNumber)) {
            if (!myAuto.plateNumber.substring(0, 2).equals("鄂A")) {
//                annualVehicleView.setVisibility(View.GONE);
//                mQueryIllegalIconView.setImageResource(R.drawable.ic_illegal);
                mQueryIllegalTitleView.setText("违章查询");
                isEr = false;
            } else {
                isEr = true;
            }
        }
    }

    private void initView() {

        mToolbarView = (Toolbar) findViewById(R.id.auto_detail_toolbar);
        autoImgView = (ImageView) findViewById(R.id.auto_detail_auto_bg);
        autoModelView = (TextView) findViewById(R.id.auto_detail_auto_model);
        integralTextView = (TextView) findViewById(R.id.auto_detail_integral_text);
        integralRuleView = (LinearLayout) findViewById(R.id.auto_detail_integral_rule);
        integralDetailView = (LinearLayout) findViewById(R.id.auto_detail_integral_detail);
        travelMileageView = (CommonRelativeTextView) findViewById(R.id.auto_detail_travel_mileage);
        registerTimeView = (CommonRelativeTextView) findViewById(R.id.auto_detail_register_time);
        plateNumberView = (CommonRelativeTextView) findViewById(R.id.auto_detail_plate_number);
        engineNumberView = (CommonRelativeTextView) findViewById(R.id.auto_detail_engine_number);
        vehicleNumberView = (CommonRelativeTextView) findViewById(R.id.auto_detail_vehicle_number);

        maintenanceRecordView = (LinearLayout) findViewById(R.id.auto_detail_maintenance_record);
        queryIllegalView = (LinearLayout) findViewById(R.id.auto_detail_query_illegal);
        annualVehicleView = (LinearLayout) findViewById(R.id.auto_detail_annual_vehicle);
        mMaintainRecordView = (LinearLayout) findViewById(R.id.auto_detail_maintain_record);

        mQueryIllegalIconView = (ImageView) findViewById(R.id.auto_detail_query_illegal_icon);
        mQueryIllegalTitleView = (TextView) findViewById(R.id.auto_detail_query_illegal_title);

        infoView = (RelativeLayout) findViewById(R.id.auto_detail_info);
        completeInfoView = (LinearLayout) findViewById(R.id.auto_detail_complete_info);

        completeInfoBtnView = (Button) findViewById(R.id.auto_detail_complete_info_btn);

        changeModelView = (LinearLayout) findViewById(R.id.auto_detail_change_model);

        couponTextView = (TextView) findViewById(R.id.auto_detail_coupon_text);
        couponRuleView = (LinearLayout) findViewById(R.id.auto_detail_coupon_rule);
        couponDetailView = (LinearLayout) findViewById(R.id.auto_detail_coupon_detail);

        integralView = (LinearLayout) findViewById(R.id.auto_detail_integral_parent);
        couponView = (LinearLayout) findViewById(R.id.auto_detail_coupon_parent);

        mToolbarView.setTitleTextColor(Color.WHITE);
        mToolbarView.setTitle("车辆详情");
        setSupportActionBar(mToolbarView);
        mToolbarView.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mToolbarView.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLevelImgView = (ImageView) findViewById(R.id.auto_detail_auto_level_img);
        mLevelView = (TextView) findViewById(R.id.auto_detail_auto_level);

        mTimeParentView = (LinearLayout) findViewById(R.id.auto_detail_time_parent);
        mExpirationOfPolicyView = (CommonRelativeTextView) findViewById(R.id.auto_detail_expirationofpolicy);
        mExamineDateView = (CommonRelativeTextView) findViewById(R.id.auto_detail_examinedate);
        mGuaranteePeriodView = (CommonRelativeTextView) findViewById(R.id.auto_detail_guaranteeperiod);
        mNextMaintenanceDateView = (CommonRelativeTextView) findViewById(R.id.auto_detail_nextmaintenancedate);
        mNextMaintenanceDistanceView = (CommonRelativeTextView) findViewById(R.id.auto_detail_next_maintenance_distance);

        mPhoneNumView = (TextView) findViewById(R.id.auto_detail_phone_number);

        mPlateNumberTopView = (TextView) findViewById(R.id.auto_detail_plate_number_top);

    }

    @Subscribe
    public void onEventMainThread(MyAuto bean) {
        DebugLog.i(TAG, "修改车辆成功");
        DebugLog.i(TAG, bean.toString());
        if (TextUtils.isEmpty(bean.autoModel)) {
            completeInfoView.setVisibility(View.VISIBLE);
            infoView.setVisibility(View.GONE);
        } else {
            infoView.setVisibility(View.VISIBLE);
            completeInfoView.setVisibility(View.GONE);
            ImageUtil.setImage(this, autoImgView, bean.brandThumb);
            autoModelView.setText(bean.autoModel);
            if (bean.authenticated == 0) {
                mPlateNumberTopView.setText(bean.plateNumber);
                plateNumberView.setTwoText(bean.plateNumber);
                travelMileageView.setTwoText(bean.drivingDistance + "km");
                checkPlateNumber(bean);
            }
            hmMyAuto = bean;
        }
    }

    /**
     * 积分规则按钮实现-
     */
    private View.OnClickListener integralRuleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivitySwitchBase.toH5Activity(AutoDetailActivity.this, "积分规则", new UserApiClient().scoreRule());
        }
    };

    /**
     * 查看明细按钮实现
     */
    private View.OnClickListener integralDetailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivitySwitchAutoInfo.toMyBillList(AutoDetailActivity.this, false, hmMyAuto.myAutoID);
        }
    };

    /**
     * 使用规则按钮实现-优惠卷
     */
    private View.OnClickListener couponRuleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivitySwitchBase.toH5Activity(AutoDetailActivity.this, "使用规则", AutoInfoControl.getInstance().couponRules());
        }
    };

    /**
     * 查看明细按钮实现-优惠卷
     */
    private View.OnClickListener couponDetailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivitySwitchAutoInfo.toMyCoupon(AutoDetailActivity.this, hmMyAuto.myAutoID);
        }
    };

    /**
     * 保养记录按钮实现
     */
    private View.OnClickListener maintenanceRecordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivitySwitchAutoInfo.toRepairRecord(AutoDetailActivity.this, hmMyAuto);
        }
    };

    /**
     * 违章查缴按钮实现
     */
    private View.OnClickListener queryIllegalClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivitySwitchAutoInfo.toIllegalConfiscateActivity(AutoDetailActivity.this, hmMyAuto);
        }
    };

    /**
     * 车辆年检按钮实现(Old)
     * 修车预约
     */
    private View.OnClickListener annualVehicleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            ActivitySwitchBase.toWhere(AutoDetailActivity.this, "com.hxqc.aroundservice.activity.VehicleInspectionActivity");
//            ActivitySwitchAutoInfo.toVehicleInspectionActivity(AutoDetailActivity.this, hmMyAuto);
//            ActivitySwitchBase.toAppointmentMaintenance(AutoDetailActivity.this);
            DebugLog.i(TAG, "修车预约");
            if (!TextUtils.isEmpty(hmMyAuto.brandID) && !TextUtils.isEmpty(hmMyAuto.brand) && !TextUtils.isEmpty(hmMyAuto.seriesID) && !TextUtils.isEmpty(hmMyAuto.series) && !TextUtils.isEmpty(hmMyAuto.autoModelID) && !TextUtils.isEmpty(hmMyAuto.autoModel)) {
//                ActivitySwitchAutoInfo.toShopQuoteActivity(AutoDetailActivity.this, hmMyAuto);
                AutoInfoControl.getInstance().toActivityInter(AutoDetailActivity.this, AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_AUTO_DETAIL, hmMyAuto);
            } else {
                ActivitySwitchAutoInfo.toChooseBrandActivity(AutoDetailActivity.this, hmMyAuto, AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_LIST, false);
            }
        }
    };

    /**
     * 驾驶证换证按钮实现
     */
    private View.OnClickListener driverLicenseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivitySwitchBase.toWhere(AutoDetailActivity.this, "com.hxqc.aroundservice.activity.DriversLicenseChangeActivity");
        }
    };

    /**
     * 去保养
     */
    private View.OnClickListener toMaintainClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(hmMyAuto.brandID) && !TextUtils.isEmpty(hmMyAuto.brand) && !TextUtils.isEmpty(hmMyAuto.seriesID) && !TextUtils.isEmpty(hmMyAuto.series) && !TextUtils.isEmpty(hmMyAuto.autoModelID) && !TextUtils.isEmpty(hmMyAuto.autoModel)) {
                ActivitySwitchAutoInfo.toShopQuoteActivity(AutoDetailActivity.this, hmMyAuto);
            } else {
                ActivitySwitchAutoInfo.toChooseBrandActivity(AutoDetailActivity.this, hmMyAuto, AutoInfoContants.AUTO_DETAIL, false);
            }
        }
    };

    /**
     * 去完善车辆信息按钮实现
     */
    private View.OnClickListener completeInfoLicenseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (hmMyAuto.authenticated == 1) {
                ActivitySwitchAutoInfo.toChooseBrandActivity(AutoDetailActivity.this, hmMyAuto);
            } else {
                ActivitySwitchAutoInfo.toAddOrEditAutoInfo(AutoDetailActivity.this, hmMyAuto, "", AutoInfoContants.EDIT_PAGE, AutoInfoContants.EDIT_PAGE, CenterEditAutoActivity.class);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 设置textview的图标
     *
     * @param id
     */
    private void setDrawable(int id) {
        Drawable drawable = getResources().getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mLevelView.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 拨打电话
     */
    private View.OnClickListener phoneNumClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new CallBar.CallPhoneDialog(AutoDetailActivity.this, mPhoneNumView.getText().toString()).show();
        }
    };

}
