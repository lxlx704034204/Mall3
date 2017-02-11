package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.FocusEditBackActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.view.NewPlateNumberLayout;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.Coupon;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.ValidatorTech;
import com.hxqc.mall.core.views.vedit.manager.VWholeEditManager;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
import com.hxqc.mall.thirdshop.maintenance.model.NewMaintenanceShop;
import com.hxqc.mall.thirdshop.maintenance.model.order.CreateOrder;
import com.hxqc.mall.thirdshop.maintenance.model.order.OrderPrepareN;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.thirdshop.maintenance.utils.SharedPreferencesHelper;
import com.hxqc.mall.thirdshop.maintenance.views.ConfirmOrderDetail;
import com.hxqc.mall.thirdshop.maintenance.views.ReserveDateDialog;
import com.hxqc.mall.thirdshop.model.InvoiceInfo;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * @Author : 钟学东
 * @Since : 2016-04-21
 * FIXME
 * Todo 4s店 快修店流程 确认订单
 */
public class FourSAndQuickConfirmOrderActivity extends FocusEditBackActivity implements View.OnClickListener, ReserveDateDialog.OnFinishClickListener, BaseSliderView.OnSliderClickListener {

    /************************
     * View
     *****************************/
    private SliderLayout mSliderView;
    private TextView mTitleView;
    private RelativeLayout mQuickShopView;
    private LinearLayout mQuickShopHideView;

    private EditTextValidatorView mContactView;
    private EditTextValidatorView mPhoneView;
    private LinearLayout mLLStoreView;
    private TextView mStoreView;
    private LinearLayout mLLTimeView;
    private TextView mTimeView;
//    private LinearLayout mLLConsultantView;
//    private TextView mConsultantView;
//    private LinearLayout mLLEngineerView;
//    private TextView mEngineerView;


    private RelativeLayout mQuickShopMoneyView;
    private TextView mAccessoryView; //配件总额
    private TextView mLaborHourView; //工时费
    private TextView mPayableView; //应付总额
    private TextView mPayView; //实付总额

    private RelativeLayout m4sMoneyView;
    private TextView mGoodsCostView; //商品总额
    private TextView mWorkCostView; //工时总额
    private TextView mAmoutMoneyView; //订单总额
    private TextView mDiscountScoreView; //积分折扣
    private TextView mDiscountCouponView; //优惠卷折扣
    private TextView mShouldPayView; //应付金额
    private TextView mTvScoreView;
    private TextView mTvCouponView;
    private TextView mPackageDiscountView; //套餐折扣
    private TextView mTvPackageDiscountView; //套餐text

    private TextView mTotalView;  //底部实付金额
    private Button mToMaintainView;
    private ConfirmOrderDetail confirmOrderDetail; //确认订单的订单详情View
    private LinearLayout mRlInvoiceView;
    private TextView mTvInvoiceView;
    private NewPlateNumberLayout commenPlateNumberView; //车牌号的view

    private LinearLayout mRlScoreView;  //积分
    private TextView mMaxScoreView;
    private TextView mMaxMoneyView;
    private EditText mUseScoreView;
    private TextView mUseMoneyView;
    private ImageView mSwitcherView;

    private LinearLayout mRlCouponView; //优惠卷
    private TextView mCouponCountView;
    private TextView mCouponView;

    private View view_1;
    private View view_2;
    private View view_3;
    private View view_4;


    private LinearLayout mParentView;
    private RequestFailView mFailView;
    private LinearLayout mPicHoldView; //占位View
    //时间dialog
    private ReserveDateDialog dateDialog;

    /***************************
     * 数据
     ********************************/
    private OrderPrepareN orderPrepareN;

    private String apppintmentDate; //推荐预约时间

    private MaintenanceClient maintenanceClient;

    private InvoiceInfo invoiceInfo;//用于发票信息的model

    private FourSAndQuickHelper fourSAndQuickHelper;

    private float upAmount;

    private String flag; // 1 为4s店 2 为 快修店
    private String shopID;
    private NewMaintenanceShop newMaintenanceShop;
    private MyAuto myAuto;
    private ArrayList<ImageModel> imageModels = new ArrayList<>(); //用于显示大图的list

    private DisplayMetrics metric;

    private String upAutoID; //上传的车辆ID
    private String tempPlateNumber; //临时存储的车牌号
    private String tempUpTime; //临时上传的时间

    /*******************************
     * 控制杂项
     ***********************************/
    private static int TO_COUPON_CODE = 1;
    private static int TO_CONSULTANT = 2;
    private static int TO_ENGINEER = 3;
    private static final int TO_INVOICE = 4;


    private boolean isSwitch = true;
    private boolean isSetText = true;

    private static final String HintKey = "Hint";
    private static final int HintMessage = 100;
    private static final int DelayedSearchTime = 1500;//延迟提示时间

    VWholeEditManager vWholeEditManager;
    private EditTextValidatorView mNumView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4s_quick_confirm_order);
        // 获取屏幕宽高
        metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
        flag = fourSAndQuickHelper.getFlag();
        invoiceInfo = new InvoiceInfo();
        vWholeEditManager = new VWholeEditManager(this);
        if (flag.equals("2")) {
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra(ActivitySwitchBase.KEY_DATA);
            newMaintenanceShop = bundle.getParcelable("newMaintenanceShop");
            shopID = newMaintenanceShop.shopID;
            fourSAndQuickHelper.setShopID(shopID);
        } else {
            shopID = fourSAndQuickHelper.getShopID();
            fourSAndQuickHelper.setScore(-1);
            fourSAndQuickHelper.setCouponID("-1");
        }
        myAuto = fourSAndQuickHelper.getMyAuto();
        upAutoID = myAuto.myAutoID;
        tempPlateNumber = myAuto.plateNumber;
        maintenanceClient = new MaintenanceClient();
        initView();
        mParentView.setVisibility(View.GONE);
        mPicHoldView.setVisibility(View.VISIBLE);
        mFailView.setVisibility(View.GONE);
        getDate();
        initEvent();
    }

    private void initEvent() {

        mLLStoreView.setOnClickListener(this);
        mLLTimeView.setOnClickListener(this);
//        mLLConsultantView.setOnClickListener(this);
//        mLLEngineerView.setOnClickListener(this);
        mToMaintainView.setOnClickListener(this);
        mRlInvoiceView.setOnClickListener(this);
        mSwitcherView.setOnClickListener(this);
        mRlCouponView.setOnClickListener(this);
        mUseScoreView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mUseScoreView.getText().toString().trim().equals("")) {
                    float score = Float.parseFloat(mUseScoreView.getText().toString().trim());
                    if (score > orderPrepareN.score.count) {
                        mUseScoreView.setText(((int) orderPrepareN.score.count) + "");
                        mUseMoneyView.setText(OtherUtil.amountFormat(orderPrepareN.score.count * orderPrepareN.score.unitPrice, true));
                    } else {
                        mUseMoneyView.setText(OtherUtil.amountFormat(score * orderPrepareN.score.unitPrice, true));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                DebugLog.i("TAG", isSetText + "");
                if (!isSetText) {
                    isSetText = true;
                    return;
                }
                toHintMessage(s.toString().trim());
                DebugLog.i("TAG", isSetText + "");
            }
        });

        if (myAuto.authenticated == 1) { //1为ERP认证的车不能改车牌号
            commenPlateNumberView.isIntercept(true);
//            commenPlateNumberView.setEnabled(true);
        } else { //不是ERP的车 改了车牌号后刷新界面
//            commenPlateNumberView.setEnabled(false);
//            commenPlateNumberView.setMatchPlateNumberListener(new NewPlateNumberLayout().OnMatchPlateNumberListener() {
//                @Override
//                public void matchPlateNumberListener() {
//                    if(commenPlateNumberView.getPlateNumber().equals(tempPlateNumber)){
//                        myAuto.myAutoID = upAutoID;
//                    }else {
//                        myAuto.myAutoID = "";
//                    }
//                    myAuto.plateNumber = commenPlateNumberView.getPlateNumber();
//                    fourSAndQuickHelper.setMyAuto(myAuto);
//                    getDate();
//                }
//            });
            commenPlateNumberView.setMatchPlateNumberListener(new NewPlateNumberLayout.OnMatchPlateNumberListener() {
                @Override
                public void matchPlateNumberListener() {
                    if (commenPlateNumberView.getPlateNumber().equals(tempPlateNumber)) {
                        myAuto.myAutoID = upAutoID;
                    } else {
                        myAuto.myAutoID = "";
                    }
                    myAuto.plateNumber = commenPlateNumberView.getPlateNumber();
                    fourSAndQuickHelper.setMyAuto(myAuto);
                    getDate();
                }
            });
        }

    }


    private void getDate() {
        fourSAndQuickHelper.prepareOrderN(this, new FourSAndQuickHelper.prepareOrderNHandle() {
            @Override
            public void onSuccess(OrderPrepareN orderPrepareN) {
                mParentView.setVisibility(View.VISIBLE);
                mPicHoldView.setVisibility(View.GONE);
                mFailView.setVisibility(View.GONE);
//                orderPrepareN.apppintmentDateNew.get(0).enable = 0;
                initDate(orderPrepareN);
                confirmOrderDetail.seMaintenanceItemNtDate(orderPrepareN.items, shopID, flag);
            }

            @Override
            public void onFailure() {
                showFailView();
            }
        });
    }

    //计算价格
    private void calculateMoney() {
        fourSAndQuickHelper.prepareOrderN(this, new FourSAndQuickHelper.prepareOrderNHandle() {
            @Override
            public void onSuccess(OrderPrepareN orderPrepareN) {

                mSwitcherView.setEnabled(true);
                initMoney(orderPrepareN);
            }

            @Override
            public void onFailure() {
                mSwitcherView.setEnabled(true);
            }
        });
    }

    private void initDate(OrderPrepareN orderPrepareN) {
        this.orderPrepareN = orderPrepareN;
        UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                if (!TextUtils.isEmpty(meData.fullname)) {
                    mContactView.setText(meData.fullname);
                }
                if (!TextUtils.isEmpty(meData.phoneNumber)) {
                    mPhoneView.setText(meData.phoneNumber);
                }
            }

            @Override
            public void onFinish() {

            }
        }, false);


        if (flag.equals("1") || flag.equals("3")) {// 1 是保养进入的4s流程  3是 4s店进入保养的流程
            mQuickShopView.setVisibility(View.GONE);
            mQuickShopHideView.setVisibility(View.VISIBLE);

            mStoreView.setText(orderPrepareN.shopIntroduce.shopTitle);
            if (TextUtils.isEmpty(tempUpTime)) {
                mTimeView.setText(orderPrepareN.recommendApppintmentDate);
            } else {
                mTimeView.setText(tempUpTime);
            }

            apppintmentDate = orderPrepareN.recommendApppintmentDate;
//            mConsultantView.setText(orderPrepareN.serviceAdviser.name);
//            mEngineerView.setText(orderPrepareN.mechanic.name);

            initMoney(orderPrepareN);

        } else if (flag.equals("2")) {//快修店 流程
            mQuickShopView.setVisibility(View.VISIBLE);
            mQuickShopHideView.setVisibility(View.GONE);

            shopID = newMaintenanceShop.shopID;
            fourSAndQuickHelper.setShopID(newMaintenanceShop.shopID);

            if (orderPrepareN.shopIntroduce.shopPhoto.size() > 0) {
                sliderAD(orderPrepareN.shopIntroduce.shopPhoto);
            } else {
                mSliderView.setSliderOnlyOneView("");
                mSliderView.setEnabled(false);
                mSliderView.setFocusableInTouchMode(false);
                mSliderView.setFilterTouchesWhenObscured(false);
                mSliderView.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            }

            for (String url : orderPrepareN.shopIntroduce.shopPhoto) {
                ImageModel imageModel = new ImageModel();
                imageModel.largeImage = url;
                imageModel.thumbImage = url;
                imageModels.add(imageModel);
            }
            mTitleView.setText(orderPrepareN.shopIntroduce.shopTitle);

            mAccessoryView.setText(OtherUtil.amountFormat(orderPrepareN.goodsAmount, true));
            mLaborHourView.setText(OtherUtil.amountFormat(orderPrepareN.workCostAmount, true));
            mPayableView.setText(OtherUtil.amountFormat(orderPrepareN.amount, true));
            mPayView.setText(OtherUtil.amountFormat(orderPrepareN.amount, true));

            upAmount = orderPrepareN.amount;
        }


    }

    private void initMoney(OrderPrepareN orderPrepareN) {
        this.orderPrepareN = orderPrepareN;

        //套餐折扣
        if (orderPrepareN.discount == 0) {
            mTvPackageDiscountView.setVisibility(View.GONE);
            mPackageDiscountView.setVisibility(View.GONE);
        } else {
            mTvPackageDiscountView.setVisibility(View.VISIBLE);
            mPackageDiscountView.setVisibility(View.VISIBLE);
        }

        //积分
        if (orderPrepareN.score == null || orderPrepareN.score.count <= 0) {
            mRlScoreView.setVisibility(View.GONE);
            mTvScoreView.setVisibility(View.GONE);
            mDiscountScoreView.setVisibility(View.GONE);
            mDiscountScoreView.setText(OtherUtil.amountFormat(0, true));
        } else {
            mRlScoreView.setVisibility(View.VISIBLE);
            mTvScoreView.setVisibility(View.VISIBLE);
            mDiscountScoreView.setVisibility(View.VISIBLE);
            mMaxScoreView.setText(((int) orderPrepareN.score.count) + "");
            mMaxMoneyView.setText(OtherUtil.amountFormat(orderPrepareN.score.count * orderPrepareN.score.unitPrice, true));
            isSetText = false;
            if (isSwitch) {
                mUseScoreView.setText(((int) orderPrepareN.score.useCount) + "");
                mUseMoneyView.setText(OtherUtil.amountFormat(orderPrepareN.score.usePrice, true));
                mUseScoreView.setSelection(mUseScoreView.getText().length());
            }
            if (orderPrepareN.score.useCount == 0) {
                mDiscountScoreView.setText(OtherUtil.amountFormat(0, true));
            } else {
                mDiscountScoreView.setText(OtherUtil.amountFormat(-(orderPrepareN.score.usePrice), true));
            }
        }
        //优惠卷
        if (orderPrepareN.coupon == null || orderPrepareN.coupon.isEmpty()) {
            mRlCouponView.setVisibility(View.GONE);
            mTvCouponView.setVisibility(View.GONE);
            mDiscountCouponView.setVisibility(View.GONE);
            mDiscountCouponView.setText(OtherUtil.amountFormat(0, true));
        } else {
            mRlCouponView.setVisibility(View.VISIBLE);
            mTvCouponView.setVisibility(View.VISIBLE);
            mDiscountCouponView.setVisibility(View.VISIBLE);
            mCouponCountView.setText(orderPrepareN.coupon.size() + "张可用");
            boolean isChoose = false;
            for (Coupon coupon : orderPrepareN.coupon) {
                if (coupon.isChoose == 1) {
                    isChoose = true;
                    mCouponView.setText("抵扣 " + OtherUtil.amountFormat(coupon.usePrice, true));
                    mDiscountCouponView.setText(OtherUtil.amountFormat(-(coupon.usePrice), true));
                    fourSAndQuickHelper.setCouponID(coupon.couponID);
                }
            }
            if (!isChoose) {
                mDiscountCouponView.setText(OtherUtil.amountFormat(-(0), true));
            }
        }

        if ((orderPrepareN.score == null || orderPrepareN.score.count <= 0) && (orderPrepareN.coupon == null || orderPrepareN.coupon.isEmpty()) && orderPrepareN.discount == 0) {
            view_1.setVisibility(View.GONE);
            view_2.setVisibility(View.GONE);
        }

        mWorkCostView.setText(OtherUtil.amountFormat(orderPrepareN.workCostAmount, true));
        mGoodsCostView.setText(OtherUtil.amountFormat(orderPrepareN.goodsAmount, true));
        mAmoutMoneyView.setText(OtherUtil.amountFormat(orderPrepareN.amount, true));
        mShouldPayView.setText(OtherUtil.amountFormat(orderPrepareN.shouldPay, true));
        mTotalView.setText(OtherUtil.amountFormat(orderPrepareN.shouldPay, true));
        mPackageDiscountView.setText(OtherUtil.amountFormat((-orderPrepareN.discount), true));

        fourSAndQuickHelper.setScore(orderPrepareN.score.useCount);
        upAmount = orderPrepareN.shouldPay;
    }


    private void initView() {
        mSliderView = (SliderLayout) findViewById(R.id.slider_home);
        ViewGroup.LayoutParams layoutParams = mSliderView.getLayoutParams();
        layoutParams.width = metric.widthPixels;
        layoutParams.height = metric.widthPixels * 9 / 16;
        mSliderView.setLayoutParams(layoutParams);

        mTitleView = (TextView) findViewById(R.id.title);
        mQuickShopView = (RelativeLayout) findViewById(R.id.rl_quick_shop);
        mQuickShopHideView = (LinearLayout) findViewById(R.id.quick_shop_hide_view);

        mContactView = (EditTextValidatorView) findViewById(R.id.et_contact);
        mContactView.addValidator(ValidatorTech.ContactMen);


        mPhoneView = (EditTextValidatorView) findViewById(R.id.et_phone);
        mPhoneView.addValidator(ValidatorTech.PhoneNumber);


        mLLStoreView = (LinearLayout) findViewById(R.id.ll_store);
        mStoreView = (TextView) findViewById(R.id.tv_store);
        mLLTimeView = (LinearLayout) findViewById(R.id.ll_time);
        mTimeView = (TextView) findViewById(R.id.tv_time);
//        mLLConsultantView = (LinearLayout) findViewById(R.id.ll_consultant);
//        mConsultantView = (TextView) findViewById(R.id.tv_consultant);
//        mLLEngineerView = (LinearLayout) findViewById(R.id.ll_engineer);
//        mEngineerView = (TextView) findViewById(R.id.tv_engineer);
        commenPlateNumberView = (NewPlateNumberLayout) findViewById(R.id.commen_auto_info_plate_number);
        mNumView = commenPlateNumberView.getNumberEditText();


        if (!TextUtils.isEmpty(myAuto.plateNumber)) {
            DebugLog.i("TAG", myAuto.plateNumber);
            commenPlateNumberView.setPlateNumber(myAuto.getPlateNumber(), true);
        } else {
            commenPlateNumberView.setPlateNumber("", true);
        }
//        commenPlateNumberView.checkPlateNumber(this);

        mQuickShopMoneyView = (RelativeLayout) findViewById(R.id.quick_shop_money);
        mAccessoryView = (TextView) findViewById(R.id.accessory);
        mLaborHourView = (TextView) findViewById(R.id.labor_hour);
        mPayableView = (TextView) findViewById(R.id.payable);
        mPayView = (TextView) findViewById(R.id.pay);

        m4sMoneyView = (RelativeLayout) findViewById(R.id.four_s_money);
        mGoodsCostView = (TextView) findViewById(R.id.goods_cost);
        mWorkCostView = (TextView) findViewById(R.id.work_cost);
        mAmoutMoneyView = (TextView) findViewById(R.id.amount_to_pay);
        mDiscountScoreView = (TextView) findViewById(R.id.score_discount_money);
        mDiscountCouponView = (TextView) findViewById(R.id.coupon_discount_money);
        mShouldPayView = (TextView) findViewById(R.id.should_pay);
        mTvScoreView = (TextView) findViewById(R.id.text_score);
        mTvCouponView = (TextView) findViewById(R.id.text_coupon);

        mTvPackageDiscountView = (TextView) findViewById(R.id.text_package_discount);
        mPackageDiscountView = (TextView) findViewById(R.id.package_discount_money);

        mTotalView = (TextView) findViewById(R.id.total_pay);
        mToMaintainView = (Button) findViewById(R.id.to_maintain);
        mToMaintainView.setText("提交订单");

        confirmOrderDetail = (ConfirmOrderDetail) findViewById(R.id.confirm_order);
        mRlInvoiceView = (LinearLayout) findViewById(R.id.rl_invoice);
        mTvInvoiceView = (TextView) findViewById(R.id.tv_invoice);

        mRlScoreView = (LinearLayout) findViewById(R.id.rl_scores);
        mMaxScoreView = (TextView) findViewById(R.id.max_score);
        mMaxMoneyView = (TextView) findViewById(R.id.max_money);
        mUseScoreView = (EditText) findViewById(R.id.et_score);
        mUseMoneyView = (TextView) findViewById(R.id.use_money);
        mSwitcherView = (ImageView) findViewById(R.id.switcher);

        view_1 = findViewById(R.id.view_1);
        view_1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        view_2 = findViewById(R.id.view_2);
        view_2.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        view_3 = findViewById(R.id.view_3);
        view_3.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        view_4 = findViewById(R.id.view_4);
        view_4.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mRlCouponView = (LinearLayout) findViewById(R.id.rl_coupon);
        mCouponCountView = (TextView) findViewById(R.id.coupon_count);
        mCouponView = (TextView) findViewById(R.id.tv_coupon);

        mParentView = (LinearLayout) findViewById(R.id.ll_parent);
        mFailView = (RequestFailView) findViewById(R.id.fail_view);
        mPicHoldView = (LinearLayout) findViewById(R.id.pic_hold);

        if (flag.equals("2")) {
            mQuickShopMoneyView.setVisibility(View.VISIBLE);
            m4sMoneyView.setVisibility(View.GONE);
            mRlScoreView.setVisibility(View.GONE);
            mRlInvoiceView.setVisibility(View.VISIBLE);
        } else {
            mQuickShopMoneyView.setVisibility(View.GONE);
            m4sMoneyView.setVisibility(View.VISIBLE);
            mRlScoreView.setVisibility(View.VISIBLE);
            mRlInvoiceView.setVisibility(View.GONE);
        }

        if (isSwitch) {
            mSwitcherView.setImageResource(R.drawable.maintain_switch_on);
            mUseMoneyView.setTextColor(getResources().getColor(R.color.text_color_orange));
            mUseScoreView.setTextColor(getResources().getColor(R.color.text_color_orange));
        } else {
            mSwitcherView.setImageResource(R.drawable.maintain_switch_off);
            mUseMoneyView.setTextColor(getResources().getColor(R.color.text_color_subheading));
            mUseScoreView.setTextColor(getResources().getColor(R.color.text_color_subheading));
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == HintMessage) {
                String s = msg.getData().getString(HintKey);
                if (s.equals("")) {
                    isSetText = false;
                    mUseScoreView.setText(0 + "");
                    mUseMoneyView.setText(OtherUtil.amountFormat(0, true));
                }
//                if(isSwitch){
                fourSAndQuickHelper.setScore(Float.parseFloat(mUseScoreView.getText().toString().trim()));
                calculateMoney();
//                }
            }
            return false;
        }
    });

    /**
     * 延迟获取数据
     *
     * @param s
     */
    private void toHintMessage(String s) {
        handler.removeMessages(HintMessage);
        Message message = new Message();
        message.what = HintMessage;
        message.getData().putString(HintKey, s);
        handler.sendMessageDelayed(message, DelayedSearchTime);
    }

    //快修店照片View
    private void sliderAD(ArrayList<String> thumb) {
        if (thumb.size() == 1) {
            mSliderView.setSliderOnlyOneView(thumb.get(0));
            mSliderView.setEnabled(false);
            mSliderView.setFocusableInTouchMode(false);
            mSliderView.setFilterTouchesWhenObscured(false);
            mSliderView.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            mSliderView.sliderOnlyOneView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showViewLagerPic(0);
                }
            });
        } else {
            for (int i = 0; i < thumb.size(); i++) {
                DefaultSliderView textSliderView = new DefaultSliderView(this);
                textSliderView.empty(R.drawable.pic_normal).error(R.drawable.pic_normal);

                textSliderView.description(i + "").image(thumb.get(i)).
                        setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);
                mSliderView.addSlider(textSliderView);
                textSliderView.getBundle().putInt("position", i);
            }
            mSliderView.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mSliderView.setCustomAnimation(new DescriptionAnimation());
            mSliderView.setDuration(5000);
        }
    }

    private void showViewLagerPic(int position) {
        int location[] = new int[2];
        mSliderView.getLocationOnScreen(location);
        Bundle bundle = new Bundle();
        bundle.putInt("locationX", location[0]);
        bundle.putInt("locationY", location[1]);
        bundle.putInt("width", mSliderView.getWidth());
        bundle.putInt("height", mSliderView.getHeight());
        ActivitySwitchBase.toViewLagerPic(position, imageModels, FourSAndQuickConfirmOrderActivity
                .this, bundle);
    }

    //错误界面的显示
    private void showFailView() {
        mParentView.setVisibility(View.GONE);
        mPicHoldView.setVisibility(View.GONE);
        mFailView.setVisibility(View.VISIBLE);
        mFailView.setEmptyDescription("网络连接失败");
        mFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
        mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == TO_CONSULTANT){
//            if(data != null){
//                orderPrepareN.serviceAdviser = data.getParcelableExtra("serviceAdviser");
//                if(orderPrepareN.serviceAdviser != null)
//                    mConsultantView.setText(orderPrepareN.serviceAdviser.name);
//            }
//
//        }else if(requestCode == TO_ENGINEER){
//            if(data != null){
//                orderPrepareN.mechanic = data.getParcelableExtra("mechanic");
//                if(orderPrepareN.mechanic != null)
//                    mEngineerView.setText(orderPrepareN.mechanic.name);
//            }
//
//        }else
        if (requestCode == TO_INVOICE) {
            if (data != null) {
                invoiceInfo = data.getParcelableExtra(ActivitySwitcherThirdPartShop.INVOICE_INFO);
                DebugLog.i("TAG", invoiceInfo.toString());
                if (invoiceInfo.invoiceContent.equals("-1")) {
                    mTvInvoiceView.setText("不开发票");
                } else if (invoiceInfo.invoiceContent.equals("0")) {
                    mTvInvoiceView.setText("明细");
                } else if (invoiceInfo.invoiceContent.equals("2")) {
                    mTvInvoiceView.setText("保养");
                }
            }
        } else if (requestCode == TO_COUPON_CODE) {
            if (data != null) {
                Coupon coupon = data.getParcelableExtra("coupon");
                if (coupon != null) {
                    fourSAndQuickHelper.setCouponID(coupon.couponID);
                } else {
                    mCouponView.setText("不使用优惠卷");
                    fourSAndQuickHelper.setCouponID("");
                }
                calculateMoney();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_store) {
            PickupPointT pickupPointT = new PickupPointT();
            pickupPointT.address = orderPrepareN.shopIntroduce.address;
            pickupPointT.latitude = orderPrepareN.shopIntroduce.latitude + "";
            pickupPointT.longitude = orderPrepareN.shopIntroduce.longitude + "";
            pickupPointT.tel = orderPrepareN.shopIntroduce.tel;

            ActivitySwitchBase.toAMapNvai(this, pickupPointT);

        } else if (i == R.id.ll_time) {
            dateDialog = new ReserveDateDialog(this);
            dateDialog.setOnFinishClickListener(this);
            dateDialog.build();
//            dateDialog.setData(orderPrepareN.apppintmentDate,orderPrepareN.recommendApppintmentDate,tempUpTime);
            dateDialog.setDataNew(orderPrepareN.apppintmentDateNew, orderPrepareN.recommendApppintmentDate, tempUpTime);
//            dateDialog.analysisDateNew(orderPrepareN.apppintmentDateNew);
            dateDialog.create();

        }
//        else if (i == R.id.ll_consultant) {
//
//            ActivitySwitcherMaintenance.toServiceAdviserForResult(FourSAndQuickConfirmOrderActivity.this, shopID, TO_CONSULTANT);
//
//        } else if (i == R.id.ll_engineer) {
//
//            ActivitySwitcherMaintenance.toServiceMechanicForResult(FourSAndQuickConfirmOrderActivity.this, shopID, TO_ENGINEER);
//
//        }
        else if (i == R.id.to_maintain) {
            ToMaintain();
        } else if (i == R.id.rl_coupon) {
            ActivitySwitcherMaintenance.toAvailableCouponList(this, orderPrepareN.coupon, TO_COUPON_CODE);
        } else if (i == R.id.rl_invoice) {
            ActivitySwitcherThirdPartShop.toInvoice(false, invoiceInfo, this);
        } else if (i == R.id.switcher) {
            if (isSwitch) {
                isSwitch = false;
                mSwitcherView.setImageResource(R.drawable.maintain_switch_off);
                mUseMoneyView.setTextColor(getResources().getColor(R.color.text_color_subheading));
                mUseScoreView.setTextColor(getResources().getColor(R.color.text_color_subheading));

                mUseScoreView.setEnabled(false);
                fourSAndQuickHelper.setScore(0);

            } else {
                isSwitch = true;
                mSwitcherView.setImageResource(R.drawable.maintain_switch_on);
                mUseMoneyView.setTextColor(getResources().getColor(R.color.text_color_orange));
                mUseScoreView.setTextColor(getResources().getColor(R.color.text_color_orange));
                int score = Integer.parseInt(mUseScoreView.getText().toString().trim());
                mUseScoreView.setEnabled(true);
                fourSAndQuickHelper.setScore(score);
            }
            mSwitcherView.setEnabled(false);
            calculateMoney();
        }
    }

    private void ToMaintain() {
        vWholeEditManager.addEditView(mNumView);
        vWholeEditManager.addEditView(mContactView);
        vWholeEditManager.addEditView(mPhoneView);
//        vWholeEditManager.autoAddVViews();
        String name = mContactView.getText().toString().trim();
        String phone = mPhoneView.getText().toString().trim();
        String serviceAdviserID = orderPrepareN.serviceAdviser.serviceAdviserID;
        final String mechanicID = orderPrepareN.mechanic.mechanicID;

//		int phoneCode = FormatCheck.checkPhoneNumber(mPhoneView.getText().toString().trim(), this);
//
//		if (!FormatCheck.checkName(mContactView.getText().toString().trim(), this)) {
//			return;
//		}
//
//		if (phoneCode == 8) {
//			return;
//		} else if (phoneCode == 10) {
//			return;
//		}

        if (vWholeEditManager.validate()) {
            String choose;
            String shopType;
            if (flag.equals("2")) {
                shopType = "20";
            } else {
                shopType = "10";
            }
            choose = fourSAndQuickHelper.getItemIDFor4S();

            maintenanceClient.createdN(shopID, name, phone, serviceAdviserID, mechanicID, apppintmentDate, choose, shopType, myAuto.brandID,
                    myAuto.seriesID, myAuto.autoModelID, JSONUtils.toJson(invoiceInfo), myAuto.drivingDistance, commenPlateNumberView.getPlateNumber(), fourSAndQuickHelper.getScore(),
                    fourSAndQuickHelper.getCouponID(), myAuto.myAutoID, new LoadingAnimResponseHandler(this) {
                        @Override
                        public void onSuccess(String response) {
                            myAuto.plateNumber = commenPlateNumberView.getPlateNumber();
                            AutoInfoControl.getInstance().checkAutoNetWork(FourSAndQuickConfirmOrderActivity.this, myAuto, new AutoInfoControl.CheckDataCallBack() {
                                @Override
                                public void checkData(boolean isCheck) {
                                    DebugLog.i(AutoInfoContants.LOG_J, isCheck + "isCheck");
                                    if (!isCheck) {
                                        AutoInfoControl.getInstance().addAutoInfo(FourSAndQuickConfirmOrderActivity.this, myAuto, new CallBackControl.CallBack<String>() {
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

                            new SharedPreferencesHelper(FourSAndQuickConfirmOrderActivity.this).setOrderChange(true);
                            fourSAndQuickHelper.destroy();
                            CreateOrder createOrder = JSONUtils.fromJson(response, new TypeToken<CreateOrder>() {
                            });
                            if (createOrder.orderAmount == 0 && createOrder.paymentType != null && createOrder.paymentType.equals("DISCOUNT")) {
                                ActivitySwitcherMaintenance.toShopFinishPay(FourSAndQuickConfirmOrderActivity.this, createOrder.orderID, "1");
                            } else {
                                createOrder.amount = upAmount;
                                ActivitySwitcherMaintenance.toPayChoice(FourSAndQuickConfirmOrderActivity.this, createOrder, shopID, "2");
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }
                    });
        }

//		if (!fourSAndQuickHelper.getFlag().equals("2") && commenPlateNumberView.getPlateNumber().equals("")) {
//			ToastHelper.showYellowToast(this, "请填写车牌号");
//			return;
//		}

//        if(TextUtils.isEmpty(apppintmentDate)){
//            ToastHelper.showYellowToast(this,"请选择保养时间");
//            return;
//        }


    }

    @Override
    public void onFinishClick(View v, String mDateATime) {
        apppintmentDate = mDateATime;
        mTimeView.setText(mDateATime);
        tempUpTime = mDateATime;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        int position = slider.getBundle().getInt("position");
        showViewLagerPic(position);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        commenPlateNumberView.onKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }
}
