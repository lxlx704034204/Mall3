package com.hxqc.mall.thirdshop.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.model.order.BaseOrder;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.ColorSquare;
import com.hxqc.mall.core.views.VariousShowView;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.fragment.CarDetailBuyParamFragment;
import com.hxqc.mall.thirdshop.model.BaseInfo;
import com.hxqc.mall.thirdshop.model.SpecialAutoInfo;
import com.hxqc.mall.thirdshop.model.SpecialCarDetailBean;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.ThirdAutoDetailIntroduce;
import com.hxqc.mall.thirdshop.views.TimeCounterView;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.ListViewNoSlide;

import net.simonvt.menudrawer.MenuDrawer;

import cz.msebera.android.httpclient.Header;
import org.apache.http.conn.HttpHostConnectException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Function: 网上4S店 店铺详情Activity
 *
 * @author 袁秉勇
 * @since 2016年05月04日
 */
public class SpecialCarDetailMainActivity extends BackActivity implements UserInfoHelper.OnLoginListener, TimeCounterView.CallBack {
    public static final String ITEMID = "seckillID";
    private final static String TAG = SpecialCarDetailMainActivity.class.getSimpleName();
    public static long TIMER = 0;
    protected AlertDialog mAppointmentAlertDialog;
    private Context mContext;
    private UserInfoHelper userInfoHelper = UserInfoHelper.getInstance();
    private ViewGroup mAppointmentAlertView;
    private ImageView mAppointmentCloseView;
    private MaterialEditText mUserNameView;
    private MaterialEditText mUserPhoneView;
    private RadioGroup mRadioGroupView;
    private Button mAppointmentSubmitView;
    private TextView mAppointTipView;

    private AlertDialog mAskAlertDailog;
    private ViewGroup mAskAlertView;
    private ImageView mAskCloseView;
    private EditText mMessageView;
    private Button mAskSubmitView;
    private TextView mValidityView;

    private int sex = 10; // 10为男， 20为女

    private RelativeLayout mContentView;
    private RequestFailView mRequestFailView;

    private MenuDrawer mMenuDrawer;
    private ImageView mSaleAreaView, mCarImageView;
    private TimeCounterView mTimeCountView;
    private TextView mCarNameView, mCarPriceView, mFactoryPriceView, mPriceSaleView, mCarDepositView, mBuyCarView;
    private VariousShowView mCarPicturesView;

    private ColorSquare mAppearanceView;//车身颜色
    private ColorSquare mInteriorView;//内饰颜色

    private ListViewNoSlide mListView;

    private ThirdAutoDetailIntroduce mThirdAutoDetailIntroduceView;

    private ThirdPartShopClient ThirdPartShopClient;

    private SpecialCarDetailBean specialCarDetailBean;

    private String itemID;

    private CarDetailBuyParamFragment carDetailBuyParamFragment;

    private HashMap< String, String > hashMap = new HashMap<>();

    private ArrayList< ImageModel > imageModels;

    private boolean isStarted = false;
    private boolean updateFlag = false;
    private boolean hasSaled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ThirdPartShopClient = new ThirdPartShopClient();

        setTitle("");

        setContentView(R.layout.activity_third_shop_car_detail);

        mMenuDrawer = (MenuDrawer) findViewById(R.id.drawer);
        mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);

        mContentView = (RelativeLayout) findViewById(R.id.detail_content_layout);
        mRequestFailView = (RequestFailView) findViewById(R.id.refresh_fail_view);

        /** 头部图片相关 **/
        mSaleAreaView = (ImageView) findViewById(R.id.sale_area);
        mCarImageView = (ImageView) findViewById(R.id.banner);
        mTimeCountView = (TimeCounterView) findViewById(R.id.time_counter);
        mTimeCountView.setCallBack(this);
        mCarNameView = (TextView) findViewById(R.id.auto_descriptions);

        /** 车辆价格相关 **/
        mCarPriceView = (TextView) findViewById(R.id.car_price);
        mFactoryPriceView = (TextView) findViewById(R.id.factory_price);
        mPriceSaleView = (TextView) findViewById(R.id.price_sale);
        mCarDepositView = (TextView) findViewById(R.id.car_deposit);

        /** 车身颜色相关 **/
        mAppearanceView = (ColorSquare) findViewById(R.id.color_square_appearance);
        mInteriorView = (ColorSquare) findViewById(R.id.color_square_interior);

        mCarPicturesView = (VariousShowView) findViewById(R.id.car_pics);
//        mListView = (ListViewNoSlide) findViewById(R.id.listview);

        /** 图文介绍相关 **/
        mThirdAutoDetailIntroduceView = (ThirdAutoDetailIntroduce) findViewById(R.id.introduce);
//        mThirdAutoDetailIntroduceView.post(new Runnable() {
//            @Override
//            public void run() {
//                mThirdAutoDetailIntroduceView.findViewById(R.id.auto_tab_group).performClick();
//            }
//        });

        mBuyCarView = (TextView) findViewById(R.id.buy_car);

        carDetailBuyParamFragment = (CarDetailBuyParamFragment) getSupportFragmentManager().findFragmentById(R.id.special_car_param_fragment);

        Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);

        if (TextUtils.isEmpty(itemID = bundle.getString(ITEMID))) {
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
            mRequestFailView.setVisibility(View.VISIBLE);
            return;
        }

        getData();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            mMenuDrawer.closeMenu();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeCountView.isRun()) mTimeCountView.stopCount();
    }


    /** 获取页面数据 **/
    private void getData() {
        ThirdPartShopClient.getSeckillDetail(itemID, new LoadingAnimResponseHandler(mContext) {
            @Override
            public void onSuccess(String response) {
                specialCarDetailBean = JSONUtils.fromJson(response, SpecialCarDetailBean.class);
                if (specialCarDetailBean != null) {
                    if (mContentView.getVisibility() == View.VISIBLE && updateFlag) {
                        updateFlag = false;
                        if (specialCarDetailBean.autoInfo != null) updateTimer(specialCarDetailBean.autoInfo);
                        return;
                    }
                    try {
                        initData(specialCarDetailBean);
                        mContentView.setVisibility(View.VISIBLE);
                    } catch (Exception e) {

                    }
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                if (throwable instanceof HttpHostConnectException) {
                    mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
                } else {
                    mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                }
                mRequestFailView.setVisibility(View.VISIBLE);
            }
        });
    }


    /** 更新计时器 **/
    private void updateTimer(SpecialAutoInfo autoInfo) {
        if ("30".equals(autoInfo.autoStatus)) {
            mBuyCarView.setEnabled(false);
            mBuyCarView.setBackgroundColor(Color.parseColor("#292929"));
            hasSaled = true;
        }

        if (!autoInfo.isEnded) {
            if (autoInfo.isStarted && !mTimeCountView.isRun()) {
                mTimeCountView.setDate(autoInfo.isStarted, (Long.valueOf(autoInfo.endTime) - Long.valueOf(autoInfo.serverTime)));
                mTimeCountView.run();
                isStarted = true;
            } else if (!autoInfo.isStarted && !mTimeCountView.isRun()) {
                mTimeCountView.setDate(autoInfo.isStarted, (Long.valueOf(autoInfo.startTime) - Long.valueOf(autoInfo.serverTime)));
                mTimeCountView.run();
                isStarted = false;

                if (!hasSaled) {
                    mBuyCarView.setEnabled(false);
                    mBuyCarView.setBackgroundColor(Color.parseColor("#292929"));
                }
            }
        } else {
            mTimeCountView.showEndStyle();
            mTimeCountView.setVisibility(View.GONE);
            isStarted = true;

            if (!hasSaled) {
                mBuyCarView.setBackgroundColor(Color.parseColor("#292929"));
                mBuyCarView.setEnabled(false);
                mBuyCarView.setText("售罄");
            }
        }
    }


    /** 初始化显示数据 **/
    private void initData(SpecialCarDetailBean specialCarDetailBean) {
        if (specialCarDetailBean.autoInfo != null) {
            final SpecialAutoInfo autoInfo = specialCarDetailBean.autoInfo;
            ImageUtil.setImage(mContext, mCarImageView, autoInfo.itemThumb, R.drawable.sliderimage_pic_normal_slider);
            if ("10".equals(autoInfo.salesArea)) {
                mSaleAreaView.setImageResource(R.drawable.spcial_car_province);
            } else if ("20".equals(autoInfo.salesArea)) {
                mSaleAreaView.setImageResource(R.drawable.spcial_car_country);
            } else {
                mSaleAreaView.setImageResource(R.drawable.icon_range);
            }

            updateTimer(autoInfo);

            mCarNameView.setText(autoInfo.itemName);

            mCarPriceView.setText(formatPrice(autoInfo.getItemPrice()));
            mFactoryPriceView.setText(formatPrice(autoInfo.getItemOrigPrice()));
            mPriceSaleView.setText(formatPrice(autoInfo.getItemFall()));
            mCarDepositView.setText(formatPrice(autoInfo.subscription));

            mAppearanceView.setColors(autoInfo.appearance != null ? !TextUtils.isEmpty(autoInfo.appearance.color) ? new String[]{autoInfo.appearance.color} : new String[]{"#ffffff"} : new String[]{"#ffffff"});
            mInteriorView.setColors(autoInfo.interior != null ? !TextUtils.isEmpty(autoInfo.interior.color) ? autoInfo.interior.color.replaceAll(";", "").split(",") : new String[]{"#ffffff"} : new String[]{"#ffffff"});

            if (autoInfo.realityPhoto != null && autoInfo.realityPhoto.size() > 0) {
                imageModels = new ArrayList<>();
                for (int i = 0; i < autoInfo.realityPhoto.size(); i++) {
                    ImageModel imageModel = new ImageModel();
                    imageModel.largeImage = autoInfo.realityPhoto.get(i);
                    imageModels.add(imageModel);
                }

                mCarPicturesView.getmRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int location[] = new int[2];
                        Bundle bundle = new Bundle();
                        bundle.putInt("locationX", location[0]);
                        bundle.putInt("locationY", location[1] = getResources().getDisplayMetrics().heightPixels);
                        bundle.putInt("width", v.getWidth());
                        bundle.putInt("height", v.getHeight());
                        ActivitySwitchBase.toViewLagerPic(0, imageModels, SpecialCarDetailMainActivity.this, bundle);
                    }
                });
            } else {
                mCarPicturesView.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(autoInfo.introduce)) {
                mThirdAutoDetailIntroduceView.setVisibility(View.GONE);
            } else {
                BaseInfo baseInfo = new BaseInfo();
                baseInfo.introduce = autoInfo.introduce;
                mThirdAutoDetailIntroduceView.setAutoDetail(baseInfo);
            }

            BaseOrder baseOrder = new BaseOrder();
            baseOrder.itemThumb = autoInfo.getItemThumb();
            baseOrder.itemName = autoInfo.getItemName();
            baseOrder.itemPrice = autoInfo.getItemPrice();
            baseOrder.itemColor = autoInfo.appearance != null ? TextUtils.isEmpty(autoInfo.appearance.color) ? "#ffffff" : autoInfo.appearance.color : "#ffffff";
            baseOrder.itemInterior = autoInfo.interior != null ? TextUtils.isEmpty(autoInfo.interior.color) ? "#ffffff" : autoInfo.interior.color : "#ffffff";
            carDetailBuyParamFragment.initData(baseOrder, TextUtils.isEmpty(autoInfo.subscription) ? "具体订金请咨询客服" : "订金：" + autoInfo.subscription + "元", specialCarDetailBean.shopInfo.shopTel);
            if (specialCarDetailBean.purchaseArg != null) {
                carDetailBuyParamFragment.initFragmentData(specialCarDetailBean.purchaseArg);
            }

            hashMap.put("carName", autoInfo.getItemName());
            hashMap.put("itemID", autoInfo.getItemID());
            hashMap.put("amount", autoInfo.subscription);
            hashMap.put("shopTel", specialCarDetailBean.shopInfo != null ? !TextUtils.isEmpty(specialCarDetailBean.shopInfo.shopTel) ? specialCarDetailBean.shopInfo.shopTel : "未知号码" : "未知号码");
        }

        if (specialCarDetailBean.shopInfo != null) {
            hashMap.put("shopName", specialCarDetailBean.shopInfo.shopName);
            setTitle(specialCarDetailBean.shopInfo.shopTitle);
        }

        if (carDetailBuyParamFragment != null) carDetailBuyParamFragment.setHashMap(hashMap);
    }


    private String formatPrice(String price) {
        try {
            double num = Float.valueOf(price);
            if (num > 10000) {
                num = num / 10000.00;
                DecimalFormat df = new DecimalFormat("#.00");
                return "￥" + df.format(num) + "万";
            } else if (num < 0) {
                return "0";
            } else {
                return "￥" + price;
            }
        } catch (Exception e) {
            return price;
        }
    }


    /** 图集 **/
    public void toPictures(View view) {
//        ActivitySwitcherThirdPartShop.toAtlas(mContext, TextUtils.isEmpty(specialCarDetailBean.autoInfo.itemID) ? "" : specialCarDetailBean.autoInfo.itemID);
    }


    /** 参数详情 **/
    public void clickToParameter(View view) {
        if (specialCarDetailBean != null && specialCarDetailBean.autoInfo != null) {
            ActivitySwitcherThirdPartShop.toParameter(this, TextUtils.isEmpty(specialCarDetailBean.autoInfo.itemID) ? "" : specialCarDetailBean.autoInfo.itemID);
        }
    }


    /** 车辆详情 **/
    public void clickToCarDetail(View view) {
        if (specialCarDetailBean != null && specialCarDetailBean.autoInfo != null) {
            if (mTimeCountView.isRun()) {
                mTimeCountView.setSyncTimer(true);
            } else {
                TIMER = 0;
                if (!isStarted) isStarted = true;
            }
            SpecialAutoInfo autoInfo = specialCarDetailBean.autoInfo;
            ActivitySwitcherThirdPartShop.toSpecialCarDetail(this, isStarted, TextUtils.isEmpty(autoInfo.salesArea) ? "10" : autoInfo.salesArea, autoInfo.getItemThumb(), autoInfo.getItemName(), autoInfo.extID, autoInfo.autoDetailInfo);
        }
    }


    public void clickToEvaluate(View view) {
        Intent intent = new Intent();
        intent.setClassName(mContext, "com.hxqc.carcompare.ui.discuss.UserDiscussActivity");
//        Bundle bundle = new Bundle();
//        bundle.putString("ext_id", specialCarDetailBean.autoInfo.extID);
//        bundle.putString("brand", specialCarDetailBean.shopInfo.brand);
//        bundle.putString("series", "待填车系名称");
//        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        intent.putExtra("extID", specialCarDetailBean.autoInfo.extID);
        intent.putExtra("brand", specialCarDetailBean.autoInfo.brand);
        intent.putExtra("series", specialCarDetailBean.autoInfo.series);
        startActivity(intent);
    }


    /** 跳转店铺首页 **/
    public void toShopHome(View view) {
        if (specialCarDetailBean != null && specialCarDetailBean.autoInfo != null) {
            ActivitySwitcherThirdPartShop.toShopHome(specialCarDetailBean.shopInfo.shopID, this);
        }
    }


    /** 呼叫客服 **/
    public void clickCallService(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MaterialDialog);
        builder.setTitle("拨打电话").setMessage(specialCarDetailBean.shopInfo.shopTel).setPositiveButton("拨打", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri uri = Uri.parse("tel:" + specialCarDetailBean.shopInfo.shopTel);
                intent.setData(uri);
                mContext.startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }


    /** 咨询 **/
    public void clickAsk(View view) {
        if (userInfoHelper.isLogin(mContext)) {
            initAskAlertView();
        } else {
            userInfoHelper.loginAction(mContext, SpecialCarDetailMainActivity.this);
        }
    }


    /** 预约看车 **/
    public void clickAppointment(View view) {
        if (mAppointmentAlertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (null == mAppointmentAlertView) {
                mAppointmentAlertView = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_appointment_alert_view, null);
            }

            builder.setView(mAppointmentAlertView);
            builder.setCancelable(true);
            mAppointmentAlertDialog = builder.create();
            mAppointmentAlertDialog.show();

            initAppointmentAlertView(mAppointmentAlertView);
        } else {
            mAppointmentAlertDialog.show();
        }
    }


    /** 立即购买 **/
    public void clickBuyCar(View view) {
        if (specialCarDetailBean != null && specialCarDetailBean.autoInfo != null && !specialCarDetailBean.autoInfo.isEnded) {
            mMenuDrawer.openMenu();
        }
    }


    public void initAskAlertView() {
        if (mAskAlertDailog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (null == mAskAlertView) {
                mAskAlertView = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_ask_alert_view, null);
            }

            builder.setView(mAskAlertView);
            builder.setCancelable(true);
            mAskAlertDailog = builder.create();
            mAskAlertDailog.show();
        } else {
            mAskAlertDailog.show();
            return;
        }

        mAskCloseView = (ImageView) mAskAlertView.findViewById(R.id.close_btn);
        mMessageView = (EditText) mAskAlertView.findViewById(R.id.submit_message);
        mAskSubmitView = (Button) mAskAlertView.findViewById(R.id.submit);
        mValidityView = (TextView) mAskAlertView.findViewById(R.id.invalid_num_count);

        mAskCloseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAskAlertDailog.dismiss();
            }
        });

        mMessageView.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                DebugLog.e(TAG, " beforeTextChanged ==========" + "s : " + s.toString() + " start : " + start + " count : " + count + " after : " + after);
                temp = s;
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DebugLog.e(TAG, "onTextChanged ============== " + "s : " + s.toString() + " start : " + start + " count : " + count);
            }


            @Override
            public void afterTextChanged(Editable s) {
                DebugLog.e(TAG, " afterTextChanged ======== s :" + s.toString());
                editStart = mMessageView.getSelectionStart();
                editEnd = mMessageView.getSelectionEnd();
                if (temp.length() > 250) {
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mMessageView.setText(s);
                    mMessageView.setSelection(tempSelection);
                } else {
                    mValidityView.setText(250 - mMessageView.length() + "");
                }
            }
        });


        mAskSubmitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMessageView.getText().length() <= 0) {
                    ToastHelper.showRedToast(mContext, "请输入您要咨询的问题");
                    return;
                }

                submitMessage();
            }
        });
    }


    @Override
    public void onLoginSuccess() {
        initAskAlertView();
    }


    private void submitMessage() {
        ThirdPartShopClient.submitAsk(specialCarDetailBean.shopInfo == null ? "" : specialCarDetailBean.shopInfo.shopID, itemID, mMessageView.getText() + "", "20", new DialogResponseHandler(mContext) {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(mContext, "数据提交成功");
                mMessageView.setText("");
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastHelper.showRedToast(mContext, "信息提交失败，请再试一次，如有问题请反馈，我们会及时处理，谢谢您的配合");
            }
        });

        mAskAlertDailog.dismiss();
    }


    /** 初始化预约弹出相关View **/
    public void initAppointmentAlertView(ViewGroup view) {
        mAppointmentCloseView = (ImageView) view.findViewById(R.id.close_btn);

        mUserNameView = (MaterialEditText) view.findViewById(R.id.user_name);
        mUserPhoneView = (MaterialEditText) view.findViewById(R.id.user_phone);

        mRadioGroupView = (RadioGroup) view.findViewById(R.id.radiogroup_sex);

        mAppointmentSubmitView = (Button) view.findViewById(R.id.submit_message);

        mAppointTipView = (TextView) view.findViewById(R.id.appoint_tip_message);

        mAppointmentCloseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppointmentAlertDialog.dismiss();
            }
        });

        mRadioGroupView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.man) {
                    sex = 10;
                } else if (checkedId == R.id.woman) {
                    sex = 20;
                }
            }
        });

        userInfoHelper.getUserInfo(this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                mUserNameView.setText(meData.fullname);
                mUserPhoneView.setText(meData.phoneNumber);
                if (meData.gender.contains("男")) {
                    mRadioGroupView.check(R.id.man);
                } else if (meData.gender.contains("女")) {
                    mRadioGroupView.check(R.id.woman);
                }
            }


            @Override
            public void onFinish() {

            }
        }, false);


        mAppointmentSubmitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FormatCheck.checkPhoneNumber(mUserPhoneView.getText().toString().trim(), mContext) != 0) {
                    return;
                }

                String nicknameRegex = "^[A-Za-z0-9\\-\\u4e00-\\u9fa5]{2,20}$";
                if (!mUserNameView.getText().toString().matches(nicknameRegex)) {
                    ToastHelper.showYellowToast(mContext, "请检查姓名格式：2~20个字,可由中英文,字母,数字组成");
                    return;
                }

                ThirdPartShopClient.submitMessage(specialCarDetailBean.shopInfo == null ? "" : specialCarDetailBean.shopInfo.shopID, specialCarDetailBean.autoInfo == null ? "" : specialCarDetailBean.autoInfo.itemID, mUserPhoneView.getText() + "", mUserNameView.getText() + "", sex + "", new LoadingAnimResponseHandler(mContext) {
                    @Override
                    public void onSuccess(String response) {
                        ToastHelper.showGreenToast(mContext, "预约看车成功");
                        mUserNameView.setText("");
                        mUserPhoneView.setText("");
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        ToastHelper.showRedToast(mContext, "信息提交失败，请再试一次，如有问题请反馈，我们会及时处理，谢谢您的配合");
                    }
                });

                mAppointmentAlertDialog.dismiss();
            }
        });


        final String clickableStr = specialCarDetailBean != null ? specialCarDetailBean.shopInfo != null ? specialCarDetailBean.shopInfo.shopTel : "" : "";
        mAppointTipView.setText(getString(R.string.appointment_tip_msg));

        SpannableString spStr = new SpannableString(clickableStr);
        spStr.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#FF7143"));       // 设置文件颜色
                ds.setUnderlineText(false);      // 设置下划线
            }


            @Override
            public void onClick(View widget) {
                DebugLog.e("", "onTextClick........");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + clickableStr);
                intent.setData(data);
                startActivity(intent);
            }
        }, 0, clickableStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mAppointTipView.setHighlightColor(Color.TRANSPARENT); // 设置点击后的颜色为透明，否则会一直出现高亮
        mAppointTipView.append(spStr);
        mAppointTipView.append("）。");
        mAppointTipView.setMovementMethod(LinkMovementMethod.getInstance()); // 开始响应点击事件
    }


    @Override
    public void callBack() {
        updateFlag = true;
        getData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tohome, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_to_home) {
            ActivitySwitchBase.toMain(this, 0);
            return true;
        }
        return false;
    }
}
