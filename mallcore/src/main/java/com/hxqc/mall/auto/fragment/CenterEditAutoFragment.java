package com.hxqc.mall.auto.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.auto.util.CompleteAutoDialogUtils;
import com.hxqc.mall.auto.view.CommenPlateNumberView;
import com.hxqc.mall.auto.view.CommonEditInfoItemView;
import com.hxqc.mall.auto.view.NewPlateNumberLayout;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.manager.VWholeEditManager;
import com.hxqc.mall.core.views.vedit.tech.VMallDivNotNull;
import com.hxqc.util.DebugLog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import static android.R.attr.name;
import static com.hxqc.mall.core.R.id.phone;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 01
 * FIXME
 * Todo 保养不含车辆列表通用添加车辆信息
 */
public class CenterEditAutoFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = AutoInfoContants.LOG_J;
    private View rootView;
    //    private EditText mVINView;
    private EditTextValidatorView mAutoTypeView;
    private EditTextValidatorView mMileageView;
    private Button mFinishView;
    public OnFinishClickListener mOnFinishClickListener;
    public OnAutoTyperClickListener mOnAutoTyperClickListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AutoInfoContants.AUTO_TYPE_BACK_DATA) {
//                mAutoTypeView.setText(mMyAuto.brand + "," + mMyAuto.series + "," + mMyAuto.autoModel);
                mAutoTypeView.setText(mMyAuto.autoModel);
            }
        }
    };
    public MyAuto mMyAuto;
    private boolean isAdd = false;
    private CommonEditInfoItemView mRegisterDateView;
    private TextView mMaintainView;
    private TextView mQualityDateView;
    private LinearLayout mMaintainInfoView;
    private String shopID;
    private int flagActivity;
    private int flagAuto;
    private TextView mExplainContentView;
    private NewPlateNumberLayout mPlateNumberView;
    private EditTextValidatorView mNumView;
    private VWholeEditManager vWholeEditManager;

    public interface OnAutoTyperClickListener {
        void onAutoTyperClick(View v);
    }

    public interface OnFinishClickListener {
        void onFinishClick(View v, MyAuto myAuto, boolean isAdd, int flag);

    }

    public void setmOnAutoTyperClickListener(OnAutoTyperClickListener l) {
        this.mOnAutoTyperClickListener = l;
    }

    public void setOnFinishClickListener(OnFinishClickListener l) {
        this.mOnFinishClickListener = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_center_add_auto_info, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vWholeEditManager = new VWholeEditManager(getActivity());

        initView();

        initData();

        initEvent();
    }

    private ArrayList<MyAuto> autoLocal = null;

    /**
     * 初始化数据
     */
    private void initData() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (getActivity().getIntent() != null) {
            Bundle bundleExtra = getActivity().getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            if (bundleExtra != null) {
                int flagPage = bundleExtra.getInt("flagPage");
                mMyAuto = bundleExtra.getParcelable("myAuto");
                shopID = bundleExtra.getString("shopID");
                flagActivity = bundleExtra.getInt("flagActivity", -1);
                if (mMyAuto != null) {
                    DebugLog.i(TAG, mMyAuto.toString());
                }
                DebugLog.i(TAG, "shopID:" + shopID);
                DebugLog.i(TAG, "flagActivity:" + flagActivity);
                if (flagPage == AutoInfoContants.ADD_PAGE) {
                    actionBar.setTitle("添加车辆信息");
                    DebugLog.i(TAG, "ADD");
//                shopID = getActivity().getIntent().getStringExtra("shopID");
//                flagActivity = getActivity().getIntent().getIntExtra("flagActivity", -1);
                    shopID = bundleExtra.getString("shopID");
                    flagActivity = bundleExtra.getInt("flagActivity", -1);
                    DebugLog.i(TAG, "ADD shopID:" + shopID + ",ADD flagActivity:" + flagActivity);
                    mAutoTypeView.setText(mMyAuto.autoModel);
                /*if (flagActivity == AutoInfoContants.HOME_PAGE) {
                    //首页进入时
//                    mAutoInfoHeaderView.setExplainContentViewVisibility(true);
                    mExplainContentView.setVisibility(View.VISIBLE);
                    if (UserInfoHelper.getInstance().isLogin(getActivity())) {
                        if (checkLocalAutoInfo()) {
                            //判断网络车辆数据
                            AutoInfoControl.getInstance().requestAutoInfo(getActivity(), new CallBack<ArrayList<MyAuto>>() {
                                @Override
                                public void onSuccess(ArrayList<MyAuto> response) {
                                    if (response != null) {
                                        DebugLog.i(TAG, "登录后数据获取");
                                        if (!response.isEmpty()) {
                                            for (int i = 0; i < response.size(); i++) {
                                                if (!TextUtils.isEmpty(response.get(i).autoModel)) {
                                                    flagID = i;
                                                    break;
                                                }
                                            }
//                                    AutoHelper.getInstance().createAutoLocal(getActivity(), response, AutoHelper.AUTO_LOCAL_INFO);
                                            if (flagID == -1) {
//                                            dialogAutoInfoComplete(response.get(0));
                                                ActivitySwitchBase.toAutoInfo(getActivity(), "", AutoInfoContants.AUTO_DETAIL);
                                            } else {
//                AutoHelper.getInstance().createAutoLocal(getActivity(), response,UserInfoHelper.getInstance().getPhoneNumber(getActivity()));
                                                *//*Bundle bundle = new Bundle();
                                                bundle.putParcelable("myAuto", response.get(flagID));
                                                ActivitySwitchBase.toWhere(getActivity(), "com.hxqc.mall.thirdshop.maintenance.activity.ShopQuoteActivity", bundle);*//*
                                                ActivitySwitchAutoInfo.toShopQuoteActivity(getActivity(), response.get(flagID));
                                                getActivity().finish();
                                            }
                                        } else {
                                            //检验本地车辆
                                            checkDetailAutoInfo();
                                        }
                                    } else {
                                        //检验本地车辆
                                        checkDetailAutoInfo();
                                    }
                                }

                                @Override
                                public void onFailed(boolean offLine) {

                                }
                            });
                        }
                    } else {
                        //检验本地车辆
                        checkDetailAutoInfo();
                    }

                   *//* if (UserInfoHelper.getInstance().isLogin(getActivity())) {
                        autoLocal = AutoHelper.getInstance().getAutoLocal(getActivity(), AutoHelper.AUTO_LOCAL_INFO);
                    } else {
                        autoLocal = AutoHelper.getInstance().getAutoLocal(getActivity(), AutoHelper.AUTO_DETAIL_INFO);
                    }
                    if (!autoLocal.isEmpty()) {
                        DebugLog.i(TAG, "本地有数据");
                        for (int i = 0; i < autoLocal.size(); i++) {
                            if (!TextUtils.isEmpty(autoLocal.get(i).autoModel)) {
                                flagID = i;
                                break;
                            }
                        }
                        DebugLog.i(TAG, "flagID " + flagID);
                        if (flagID == -1) {
                            DebugLog.i(TAG, "无已完善的车");
                            dialogAutoInfoComplete(autoLocal.get(0));
                        } else {
                            DebugLog.i(TAG, "有已完善的车");
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("myAuto", autoLocal.get(flagID));
                            ActivitySwitchBase.toWhere(getActivity(), "com.hxqc.mall.thirdshop.maintenance.activity.ShopQuoteActivity", bundle);
                            getActivity().finish();
                        }
                    } else {
                        DebugLog.i(TAG, "本地无数据");
                        if (UserInfoHelper.getInstance().isLogin(getActivity())) {
                            AutoInfoControl.getInstance().requestAutoInfo(getActivity(), arrayListCallBack);
                        }
                    }*//*
                } else if (flagActivity == AutoInfoContants.AUTO_DETAIL) {
//                    mAutoInfoHeaderView.setExplainContentViewVisibility(true);
                    mExplainContentView.setVisibility(View.VISIBLE);
                }*/
                    if (shopID == null) {
                        shopID = "";
                        isAdd = true;
                    }
                    isAdd = true;
                    if (mMyAuto == null) {
                        mMyAuto = new MyAuto();
                    }
                    mPlateNumberView.initTextChangedListener();
                } else if (flagPage == AutoInfoContants.EDIT_PAGE) {
                    actionBar.setTitle("修改车辆信息");
                    mFinishView.setText("保存修改");
                    DebugLog.i(TAG, "EDIT");
                    mAutoTypeView.setText(mMyAuto.autoModel);
                    mMileageView.setText(mMyAuto.drivingDistance);
                    if (!TextUtils.isEmpty(mMyAuto.plateNumber)) {
                        try {
                            mPlateNumberView.setPlateNumber(mMyAuto.plateNumber,true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        /*if (mPlateNumberView.getPlateNumber().length() == 7) {
                            mNumView.hideKeyboard();
                            mPlateNumberView.clearFocus();
                        }*/
                    }
//                mVINView.setText(mMyAuto.VIN);
//                mVINView.setSelection(mMyAuto.VIN.length());

                /*if (TextUtils.isEmpty(mMyAuto.lastMaintenancDate) && TextUtils.isEmpty(mMyAuto.drivingDistance)) {
                    mMaintainInfoView.setVisibility(View.GONE);
                } else if (!TextUtils.isEmpty(mMyAuto.lastMaintenancDate) && !TextUtils.isEmpty(mMyAuto.drivingDistance)) {
                    mMaintainInfoView.setVisibility(View.VISIBLE);
                    mMaintainView.setVisibility(View.VISIBLE);
                    mMaintainView.setText("您上次保养的时间" + mMyAuto.lastMaintenancDate + "行驶里程" + mMyAuto.drivingDistance + "公里");
                } else if (!TextUtils.isEmpty(mMyAuto.lastMaintenancDate) && TextUtils.isEmpty(mMyAuto.drivingDistance)) {
                    mMaintainInfoView.setVisibility(View.VISIBLE);
                    mMaintainView.setVisibility(View.VISIBLE);
                    mMaintainView.setText("您上次保养的时间" + mMyAuto.lastMaintenancDate);
                } else if (TextUtils.isEmpty(mMyAuto.lastMaintenancDate) && !TextUtils.isEmpty(mMyAuto.drivingDistance)) {
                    mMaintainInfoView.setVisibility(View.VISIBLE);
                    mMaintainView.setVisibility(View.VISIBLE);
                    mMaintainView.setText("行驶里程" + mMyAuto.drivingDistance + "公里");
                }*/

//                    showOtherInfo(mMyAuto);

                    if (TextUtils.isEmpty(mMyAuto.guaranteePeriod)) {
//                ToastHelper.showYellowToast(getActivity(), "很抱歉，未能查到车辆质保日期");
                        mQualityDateView.setVisibility(View.GONE);
                    } else {
                        mQualityDateView.setVisibility(View.VISIBLE);
                        mQualityDateView.setText("质保到" + mMyAuto.guaranteePeriod);
                    }

//                mEditNameView.setContentText(mMyAuto.ownName);
//                mEditPhoneView.setContentText(mMyAuto.ownPhone);
//                mRegisterDateView.setContentText(mMyAuto.registerTime);
                    if (!TextUtils.isEmpty(mMyAuto.registerTime)) {
                        String[] date = mMyAuto.registerTime.split("-");
                        imYear = Integer.parseInt(date[0]);
                        imMonthOfYear = Integer.parseInt(date[1]) - 1;
                        imDayOfMonth = Integer.parseInt(date[2]);
                    }
                    DebugLog.i(TAG, imYear + "----" + imMonthOfYear + "----" + imDayOfMonth);
                    isAdd = false;
                /*if (TextUtils.isEmpty(mPlateNumberView.getPlateNumber())) {
                    mPlateNumberView.setState(!isAdd);
                } else {
                    mPlateNumberView.setState(isAdd);
                }*/
                }
            }
        }
    }

    /**
     * 检验本地车辆
     */
    private void checkDetailAutoInfo() {
        DebugLog.i(TAG, "检验本地数据");
        autoLocal = AutoHelper.getInstance().getAutoLocal(getActivity(), AutoHelper.AUTO_DETAIL_INFO);
        if (autoLocal != null && !autoLocal.isEmpty()) {
            DebugLog.i(TAG, "本地有数据");
            for (int i = 0; i < autoLocal.size(); i++) {
                if (!TextUtils.isEmpty(autoLocal.get(i).autoModel)) {
                    flagID = i;
                    break;
                }
            }
            DebugLog.i(TAG, "flagID " + flagID);
            if (flagID == -1) {
                DebugLog.i(TAG, "本地无已完善的车");
                CompleteAutoDialogUtils.dialogAutoInfoComplete(getActivity(), autoLocal.get(0), -1);
//                dialogAutoInfoComplete(autoLocal.get(0));
            } else {
                DebugLog.i(TAG, "本地有已完善的车");
                Bundle bundle = new Bundle();
                bundle.putParcelable("myAuto", autoLocal.get(flagID));
                ActivitySwitchBase.toWhere(getActivity(), "com.hxqc.mall.thirdshop.maintenance.activity.ShopQuoteActivity", bundle);
                getActivity().finish();
            }
        }
    }

    /**
     * 检验本地车辆
     */
    private boolean checkLocalAutoInfo() {
        DebugLog.i(TAG, "检验网络缓存数据");
        autoLocal = AutoHelper.getInstance().getAutoLocal(getActivity(), AutoHelper.AUTO_LOCAL_INFO);
        if (autoLocal != null && !autoLocal.isEmpty()) {
            DebugLog.i(TAG, "网络缓存本地有数据");
            for (int i = 0; i < autoLocal.size(); i++) {
                if (!TextUtils.isEmpty(autoLocal.get(i).autoModel)) {
                    flagID = i;
                    break;
                }
            }
            DebugLog.i(TAG, "flagID " + flagID);
            if (flagID == -1) {
                DebugLog.i(TAG, "网络缓存本地无已完善的车");
                CompleteAutoDialogUtils.dialogAutoInfoComplete(getActivity(), autoLocal.get(0), -1);
//                dialogAutoInfoComplete(autoLocal.get(0));
            } else {
                DebugLog.i(TAG, "网络缓存本地有已完善的车");
                Bundle bundle = new Bundle();
                bundle.putParcelable("myAuto", autoLocal.get(flagID));
                ActivitySwitchBase.toWhere(getActivity(), "com.hxqc.mall.thirdshop.maintenance.activity.ShopQuoteActivity", bundle);
                getActivity().finish();
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mFinishView.setOnClickListener(this);
//            mPlateNumberView.setCheckListener(checkListener);
        mAutoTypeView.setOnClickListener(this);

//        mMileageView.setOnFocusChangeListener(mileageFocusChangeListener);
        /*if (TextUtils.isEmpty(mPlateNumberView.getPlateNumber())) {
            mPlateNumberView.setCheckListener(checkListener);
        }*/
//        mLicenseNumView.addTextChangedListener(licenseNumberChangeListener);
//        mRegisterDateView.setOnContentClickListener(registerDateListener);
    }

    /**
     * 初始化控件
     */
    private void initView() {
//        mAutoInfoHeaderView = (CommenAutoInfoHeaderView) rootView.findViewById(R.id.add_auto_info_header);
//        mLicenseCityView = mAutoInfoHeaderView.getmLicenseCityView();
//        mLicenseCityView.setMode(PlateNumberTextView.MODE_CITY);
//        mLicenseNumView = mAutoInfoHeaderView.getmLicenseNumView();
//        mLicenseNumView.setMode(PlateNumberTextView.MODE_WORD);
//        mPlateNumberView = mAutoInfoHeaderView.getmPlateNumView();
//        LinearLayout mPlateNumberParentView = mAutoInfoHeaderView.getmPlateNumberParentView();
//        mVINView = mAutoInfoHeaderView.getmVINView();
//        mAutoTypeView = mAutoInfoHeaderView.getmAutoTypeView();
//        mMileageView = mAutoInfoHeaderView.getmMileageView();
//        mMaintainInfoView = mAutoInfoHeaderView.getmMaintainInfoView();
//        LinearLayout mMileageInfoView = mAutoInfoHeaderView.getmMileageInfoView();
//        mMaintainView = mAutoInfoHeaderView.getmMaintainView();
//        mQualityDateView = mAutoInfoHeaderView.getmQualityDateView();
        mExplainContentView = (TextView) rootView.findViewById(R.id.add_auto_info_explain_content);
        mAutoTypeView = (EditTextValidatorView) rootView.findViewById(R.id.add_auto_info_type);
        mMileageView = (EditTextValidatorView) rootView.findViewById(R.id.add_auto_info_mileage);
        mMaintainInfoView = (LinearLayout) rootView.findViewById(R.id.add_auto_info_maintain_info);
        mMaintainView = (TextView) rootView.findViewById(R.id.add_auto_info_maintain);
        mQualityDateView = (TextView) rootView.findViewById(R.id.add_auto_info_date);
        mPlateNumberView = (NewPlateNumberLayout) rootView.findViewById(R.id.add_auto_info_plate_number);
        mNumView = mPlateNumberView.getNumberEditText();
//        mEditNameView = (CommonEditInfoItemView) rootView.findViewById(R.id.add_auto_info_name);
//        mEditPhoneView = (CommonEditInfoItemView) rootView.findViewById(R.id.add_auto_info_phone);
//        mRegisterDateView = (CommonEditInfoItemView) rootView.findViewById(R.id.add_auto_info_register_date);

        mFinishView = (Button) rootView.findViewById(R.id.add_auto_info_btn);

//        mRegisterDateView.setVisibility(View.GONE);
//        mPlateNumberParentView.setVisibility(View.GONE);
        mMaintainInfoView.setVisibility(View.GONE);
//        mMileageInfoView.setVisibility(View.VISIBLE);

        mAutoTypeView.addValidator(new VMallDivNotNull("请选择车辆信息", ""));
//        mNumView.addValidator(ValidatorTech.PlateNumber);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlateNumberView.onResume();
    }

    /**
     * 设置车辆信息
     *
     * @param brand
     * @param brandID
     * @param brandThumb
     * @param series
     * @param seriesID
     * @param model
     * @param modelID
     */
    public void setAutoType(String brand, String brandID, String brandThumb, String seriesBrandName, String series, String seriesID, String model, String modelID) {
        Message msg = Message.obtain();
        msg.what = AutoInfoContants.AUTO_TYPE_BACK_DATA;
        mMyAuto.brand = brand;
        mMyAuto.brandID = brandID;
        mMyAuto.brandThumb = brandThumb;
        mMyAuto.series = series;
        mMyAuto.seriesID = seriesID;
        mMyAuto.autoModel = model;
        mMyAuto.autoModelID = modelID;
        mMyAuto.brandName = seriesBrandName;
        msg.obj = mMyAuto;
        mHandler.sendMessage(msg);
    }

    /**
     * 匹配请求监听
     */
    private CommenPlateNumberView.OnCheckListener checkListener = new CommenPlateNumberView.OnCheckListener() {
        @Override
        public void checkListener(String plateNumber) {
//            AutoInfoControl.getInstance().checkAutoInfo(getActivity(), "", plateNumber, checkAutoCallBack);
        }
    };

 /*   private TextWatcher licenseNumberChangeListener = new TextWatcher() {
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
                AutoInfoControl.getInstance().checkAutoInfo(getActivity(), "", plateNumber, checkAutoHandler);
            }
        }
    };*/

    /**
     * 匹配功能
     */
    /*private AutoInfoControl.CheckAutoHandler checkAutoHandler = new AutoInfoControl.CheckAutoHandler() {
        @Override
        public void onCheckSucceed(MyAuto myAuto) {

            if (TextUtils.isEmpty(mEditNameView.getContentText())) {
                mEditNameView.setContentText(myAuto.ownName);
            }
            if (TextUtils.isEmpty(mEditPhoneView.getContentText())) {
                mEditPhoneView.setContentText(myAuto.ownPhone);
            }
            if (TextUtils.isEmpty(myAuto.lastMaintenancDate) && TextUtils.isEmpty(myAuto.drivingDistance) && TextUtils.isEmpty(myAuto.guaranteePeriod)) {
//                mMaintainView.setVisibility(View.GONE);
//                mQualityDateView.setVisibility(View.GONE);
                mMaintainInfoView.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(myAuto.lastMaintenancDate) && !TextUtils.isEmpty(myAuto.drivingDistance) && !TextUtils.isEmpty(myAuto.guaranteePeriod)) {
//                mMaintainView.setVisibility(View.VISIBLE);
//                mQualityDateView.setVisibility(View.VISIBLE);
                mMaintainInfoView.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(mMileageView.getText())) {
                if (!TextUtils.isEmpty(myAuto.drivingDistance)) {
//                mMaintainView.setText("行驶里程" + myAuto.getDrivingDistance() + "公里");
                    mMileageView.setText(myAuto.drivingDistance);
//                mMileageView.setSelection(myAuto.getDrivingDistance().length());
                }
            }
            if (TextUtils.isEmpty(myAuto.lastMaintenancDate)) {
                mMaintainView.setVisibility(View.GONE);
            } else {
                mMaintainView.setVisibility(View.VISIBLE);
                mMaintainView.setText("您上次保养的时间" + myAuto.lastMaintenancDate);

            }
            if (TextUtils.isEmpty(myAuto.guaranteePeriod)) {
                ToastHelper.showYellowToast(getActivity(), "很抱歉，未能查到车辆质保日期");
                mQualityDateView.setVisibility(View.GONE);
            } else {
                mQualityDateView.setVisibility(View.VISIBLE);
                mQualityDateView.setText("质保到" + myAuto.guaranteePeriod);
            }
            if (!TextUtils.isEmpty(myAuto.autoModel)) {
                mAutoTypeView.setText(myAuto.autoModel);
            }

            mMyAuto.brand = myAuto.brand;
            mMyAuto.brandID = myAuto.brandID;
            mMyAuto.series = myAuto.series;
            mMyAuto.seriesID = myAuto.seriesID;
            mMyAuto.autoModel = myAuto.autoModel;
            mMyAuto.autoModelID = myAuto.autoModelID;
        }

        @Override
        public void onCheckFailed(boolean offLine) {
//            ToastHelper.showYellowToast(getActivity(), "未能找到此车的信息");
        }
    };*/

    //匹配请求回调
    private CallBackControl.CallBack<MyAuto> checkAutoCallBack = new CallBackControl.CallBack<MyAuto>() {
        @Override
        public void onSuccess(MyAuto response) {
            if (response != null) {
//                if (TextUtils.isEmpty(mEditNameView.getContentText())) {
//                    mEditNameView.setContentText(response.ownName);
//                }
//                if (TextUtils.isEmpty(mEditPhoneView.getContentText())) {
//                    mEditPhoneView.setContentText(response.ownPhone);
//                }
                if (TextUtils.isEmpty(mMileageView.getText())) {
                    if (!TextUtils.isEmpty(response.drivingDistance)) {
//                mMaintainView.setText("行驶里程" + myAuto.getDrivingDistance() + "公里");
                        mMileageView.setText(response.drivingDistance);
//                mMileageView.setSelection(myAuto.getDrivingDistance().length());
                    }
                }

//                showOtherInfo(response);

                if (!TextUtils.isEmpty(response.autoModel)) {
                    mAutoTypeView.setText(response.autoModel);
                }

                /*if (!TextUtils.isEmpty(response.registerTime)) {
                    mRegisterDateView.setContentText(response.registerTime);
                }*/

                mMyAuto.brand = response.brand;
                mMyAuto.brandID = response.brandID;
                mMyAuto.series = response.series;
                mMyAuto.seriesID = response.seriesID;
                mMyAuto.autoModel = response.autoModel;
                mMyAuto.autoModelID = response.autoModelID;
            }
        }

        @Override
        public void onFailed(boolean offLine) {
//            ToastHelper.showYellowToast(getActivity(), "未能找到此车的信息");
        }
    };

    /**
     * 显示其他信息逻辑方法
     *
     * @param response
     */
    private void showOtherInfo(MyAuto response) {
        if (TextUtils.isEmpty(response.lastMaintenanceDate) && TextUtils.isEmpty(response.drivingDistance) && TextUtils.isEmpty(response.guaranteePeriod)) {
//                mMaintainView.setVisibility(View.GONE);
//                mQualityDateView.setVisibility(View.GONE);
            mMaintainInfoView.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(response.lastMaintenanceDate) || !TextUtils.isEmpty(response.drivingDistance) || !TextUtils.isEmpty(response.guaranteePeriod)) {
//                mMaintainView.setVisibility(View.VISIBLE);
//                mQualityDateView.setVisibility(View.VISIBLE);
            mMaintainInfoView.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(response.lastMaintenanceDate) && TextUtils.isEmpty(response.drivingDistance)) {
            mMaintainView.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(response.lastMaintenanceDate) && !TextUtils.isEmpty(response.drivingDistance)) {
            mMaintainView.setVisibility(View.VISIBLE);
            mMaintainView.setText(getString(R.string.maintenance_time) + response.lastMaintenanceDate + getString(R.string.mileage) + response.drivingDistance + getString(R.string.kilometre));
        } else if (!TextUtils.isEmpty(response.lastMaintenanceDate) && TextUtils.isEmpty(response.drivingDistance)) {
            mMaintainView.setVisibility(View.VISIBLE);
            mMaintainView.setText(getString(R.string.maintenance_time) + response.lastMaintenanceDate);
        } else if (TextUtils.isEmpty(response.lastMaintenanceDate) && !TextUtils.isEmpty(response.drivingDistance)) {
            mMaintainView.setVisibility(View.VISIBLE);
            mMaintainView.setText(getString(R.string.mileage) + response.drivingDistance + getString(R.string.kilometre));
        }

        if (TextUtils.isEmpty(response.guaranteePeriod)) {
//                ToastHelper.showYellowToast(getActivity(), "很抱歉，未能查到车辆质保日期");
            mQualityDateView.setVisibility(View.GONE);
        } else {
            mQualityDateView.setVisibility(View.VISIBLE);
            mQualityDateView.setText(getString(R.string.warranty) + response.guaranteePeriod);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_auto_info_type) {

            mOnAutoTyperClickListener.onAutoTyperClick(v);

        } else if (v.getId() == R.id.add_auto_info_btn) {

            String plateNum = mPlateNumberView.getPlateNumber();
            String autoType = mAutoTypeView.getText().toString();
            String mileage = mMileageView.getText().toString();
            DebugLog.i(TAG, "1-----" + "1-----" + autoType + "1-----" + mileage + "1-----" + name + "1-----" + phone);

            vWholeEditManager.addEditView(mAutoTypeView);
            vWholeEditManager.addEditView(mNumView);

            if (TextUtils.isEmpty(autoType)) {
                ToastHelper.showYellowToast(getActivity(), "请选择车型");
                vWholeEditManager.validate();
                return;
            }

            if (TextUtils.isEmpty(plateNum)) {
                ToastHelper.showYellowToast(getActivity(), "请填写车牌号");
                vWholeEditManager.validate();
                return;
            } else if (plateNum.length() < 7) {
                ToastHelper.showYellowToast(getActivity(), "车牌号格式不对");
                vWholeEditManager.validate();
                return;
            }

            /*if (isAdd) {
                if (TextUtils.isEmpty(mileage)) {
                    ToastHelper.showYellowToast(getActivity(), "请输入行驶里程");
                    vWholeEditManager.validate();
                    return;
                }
            }*/

/*            if (!TextUtils.isEmpty(name)) {
                if (name.length() < 2 || name.length() > 30) {
                    ToastHelper.showYellowToast(getActivity(), "姓名格式不正确");
                    return;
                }
            }

            if (!TextUtils.isEmpty(phone)) {
                if (!FormatCheck.isPhoneNumber(phone)) {
                    ToastHelper.showYellowToast(getActivity(), R.string.me_phone_number_format_error);
                    return;
                }
            }*/
            if (vWholeEditManager.validate()) {
                mMyAuto.autoModel = autoType;
                mMyAuto.drivingDistance = mileage;
                mMyAuto.plateNumber = plateNum;
                if (flagActivity == AutoInfoContants.AUTO_DETAIL) {
                    loadShopBrands(v, mMyAuto, isAdd);
                } else {
                    mOnFinishClickListener.onFinishClick(v, mMyAuto, isAdd, flagActivity);
                }
            }
            /*if (vWholeEditManager.validate()) {
//            mMyAuto.plateNumber = plateNum;
//            myAuto.VIN = vin;
                mMyAuto.autoModel = autoType;
                mMyAuto.drivingDistance = mileage;
                mMyAuto.plateNumber = plateNum;
//            mMyAuto.ownName = name;
//            mMyAuto.ownPhone = phone;
//            mMyAuto.registerTime = mRegisterDateView.getContentText();
                if (flagActivity == AutoInfoContants.AUTO_DETAIL) {
                    loadShopBrands(v, mMyAuto, isAdd);
                } else {
                    vWholeEditManager.autoAddVViews();
                    if (vWholeEditManager.validate()) {
                        mOnFinishClickListener.onFinishClick(v, mMyAuto, isAdd, flagActivity);
                    }
                }
            }*/
        }
    }

    private int imYear = -1, imMonthOfYear = -1, imDayOfMonth = -1;
    private Calendar now = null;
    private DatePickerDialog dpd = null;
    private CommonEditInfoItemView.onContentClickListener registerDateListener = new CommonEditInfoItemView.onContentClickListener() {
        @Override
        public void onContentClick(View v) {
            now = Calendar.getInstance();
            dpd = DatePickerDialog.newInstance(
                    dateSetListener,
                    imYear != -1 ? imYear : now.get(Calendar.YEAR),
                    imMonthOfYear != -1 ? imMonthOfYear : now.get(Calendar.MONTH),
                    imDayOfMonth != -1 ? imDayOfMonth : now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.setThemeDark(true);
            dpd.vibrate(true);
            dpd.dismissOnPause(false);
            dpd.showYearPickerFirst(false);
            dpd.setAccentColor(ContextCompat.getColor(getActivity(), R.color.cursor_orange));
            dpd.show(getActivity().getFragmentManager(), "DatePickerDialog");
        }
    };

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            imYear = year;
            imMonthOfYear = monthOfYear;
            imDayOfMonth = dayOfMonth;
            if (monthOfYear < 9) {
                mRegisterDateView.setContentText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
            } else {
                mRegisterDateView.setContentText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }
    };

    private boolean isCheck = false;

    /**
     * 检验是否店铺品牌车辆
     */

    private void loadShopBrands(final View view, final MyAuto mMyAuto, final boolean isAdd) {
        AutoTypeControl.getInstance().requestBrand(getActivity(), shopID, new CallBackControl.CallBack<ArrayList<BrandGroup>>() {
            @Override
            public void onSuccess(ArrayList<BrandGroup> myAutoGroups) {
                for (int i = 0; i < myAutoGroups.size(); i++) {
                    for (int j = 0; j < myAutoGroups.get(i).group.size(); j++) {
                        if (myAutoGroups.get(i).group.get(j).brandName.equals(mMyAuto.brand)) {
                            isCheck = true;
//                            ToastHelper.showRedToast(getActivity(), "请选择本店的车辆信息");
//                            return;
                        }
                      /*  else {
                            mOnFinishClickListener.onFinishClick(view, mMyAuto, isAdd);
                            return;
                        }*/
                    }
                }
                if (isCheck) {
                    mOnFinishClickListener.onFinishClick(view, mMyAuto, isAdd, flagActivity);
                } else {
                    ToastHelper.showRedToast(getActivity(), "请选择本店的车辆信息");
                    return;
                }
            }

            @Override
            public void onFailed(boolean offLine) {
                DebugLog.i(TAG, "店铺品牌列表为空！");
            }
        });
    }

    private int flagID = -1;
    //车辆数据请求回调
    /*private CallBack<ArrayList<MyAuto>> arrayListCallBack = new CallBack<ArrayList<MyAuto>>() {
        @Override
        public void onSuccess(ArrayList<MyAuto> response) {
            if (response != null) {
                DebugLog.i(TAG, "登录后数据获取");
                if (!response.isEmpty()) {
                    for (int i = 0; i < response.size(); i++) {
                        if (!TextUtils.isEmpty(response.get(i).autoModel)) {
                            flagID = i;
                            break;
                        }
                    }
                    AutoHelper.getInstance().createAutoLocal(getActivity(), response, AutoHelper.AUTO_LOCAL_INFO);
                    if (flagID == -1) {
                        CompleteAutoDialogUtils.dialogAutoInfoComplete(getActivity(), autoLocal.get(0));
//                        dialogAutoInfoComplete(response.get(0));
                    } else {
//                AutoHelper.getInstance().createAutoLocal(getActivity(), response,UserInfoHelper.getInstance().getPhoneNumber(getActivity()));
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("myAuto", response.get(flagID));
                        ActivitySwitchBase.toWhere(getActivity(), "com.hxqc.mall.thirdshop.maintenance.activity.ShopQuoteActivity", bundle);
                        getActivity().finish();
                    }
                }
            }
        }

        @Override
        public void onFailed(boolean offLine) {

        }
    };*/

    /**
     * 完善车辆信息弹窗的方法
     */
    private void dialogAutoInfoComplete(final MyAuto myAuto) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_auto_info_complete, null);
        final AlertDialog imAlertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        imAlertDialog.setCanceledOnTouchOutside(false);
        imAlertDialog.show();
        ImageView cancelView = (ImageView) view.findViewById(R.id.dialog_cancel);
        Button completeView = (Button) view.findViewById(R.id.dialog_complete);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imAlertDialog.dismiss();

            }
        });

        completeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserInfoHelper.getInstance().isLogin(getActivity())) {
//                    ActivitySwitchAutoInfo.toAutoInfo(getActivity());
//                    ActivitySwitchBase.toAutoInfo(getActivity(), "", AutoInfoContants.AUTO_DETAIL);
                    ActivitySwitchAutoInfo.toChooseBrandActivity(getActivity(), myAuto, AutoInfoContants.AUTO_DETAIL, false);
                    imAlertDialog.dismiss();
                    getActivity().finish();
                }
            }
        });
    }

    private View.OnFocusChangeListener mileageFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            DebugLog.i(TAG, "hasFocus:" + hasFocus);
            if (!hasFocus) {
                if (TextUtils.isEmpty(mMileageView.getText())) {
                    ToastHelper.showRedToast(getActivity(), "请填写行驶里程");
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        if (dpd != null) {
            dpd.dismiss();
        }

        if (now != null) {
            now.clear();
            now = null;
        }
        super.onDestroy();
    }
}
