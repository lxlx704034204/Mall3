package com.hxqc.mall.thirdshop.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.VariousShowView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.ThirdCarDetailModel;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.mall.thirdshop.views.MySeekBar;
import com.hxqc.mall.thirdshop.views.ReferenceDetailsDialog;
import com.hxqc.mall.thirdshop.views.T_AutoCarDetailColor;
import com.hxqc.mall.thirdshop.views.ThirdAutoDetailIntroduce;
import com.hxqc.util.DateUtil;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

/**
 * Function:第三方店铺车辆详情
 *
 * @author 袁秉勇
 * @since 2015年12月01日
 */
public class FavorableCarDetailActivity extends BackActivity implements UserInfoHelper.OnLoginListener {
    public final static String SHOPID = "shopID";
    public final static String ITEMID = "itemID";
    public final static String SHOPTITLE = "shopTitle";
    private final static String TAG = FavorableCarDetailActivity.class.getSimpleName();
    ImageView mBannerView;
    TextView mAutoDescriptionsView;

    ThirdPartShopClient ThirdPartShopClient;
    RelativeLayout mContentView;
    RequestFailView requestFailView;
    RelativeLayout mFavorablePriceDetailView;
    TextView mFavorablePriceView;
    TextView mFavorableTimeLeftView;
    TextView mReferencePriceTextView;
    TextView mReferencePriceView;
    TextView mreferenceRealPriceTextView;
    TextView mReferenceRealPriceView;
    T_AutoCarDetailColor mColorGroup;
    ThirdCarDetailModel thirdCarDetailModel;
    VariousShowView mPickupPointView;//自提点
    ThirdAutoDetailIntroduce thirdAutoDetailIntroduce;
    CountDownTimer countDownTimer;

    CallBar mCallBarView;

    String shopID;
    String itemID;
    Bundle bundle;

    private AlertDialog mAskAlertDailog;
    private ViewGroup mAskAlertView;
    private ImageView mAskCloseView;
    private EditText mMessageView;
    private Button mAskSubmitView;
    private TextView mValidityView;

    private MySeekBar downPaymentSeekBar;
    private MySeekBar monthSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_activity_favorable_car_detail);

        bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
        ThirdPartShopClient = new ThirdPartShopClient();

        mContentView = (RelativeLayout) findViewById(R.id.content_view);
        requestFailView = (RequestFailView) findViewById(R.id.refresh_fail_view);

        String shopTitle = bundle.getString(SHOPTITLE);

        if (!TextUtils.isEmpty(shopTitle)) {
            if (!TextUtils.isEmpty(shopTitle.split(" ")[0])) {
                this.setTitle(shopTitle.split(" ")[0]);
            } else {
                this.setTitle("恒信汽车");
            }
        } else {
            this.setTitle("恒信汽车");
        }

        getData();
    }


    /**
     * 获取车辆详情数据
     */
    private void getData() {
        if (TextUtils.isEmpty(bundle.getString(FavorableCarDetailActivity.SHOPID))) {
            requestFailView.setVisibility(View.VISIBLE);
            requestFailView.setEmptyDescription("获取数据失败，请重试");
            return;
        } else {
            shopID = bundle.getString(FavorableCarDetailActivity.SHOPID);
        }

        if (TextUtils.isEmpty(bundle.getString(FavorableCarDetailActivity.ITEMID))) {
            requestFailView.setVisibility(View.VISIBLE);
            requestFailView.setEmptyDescription("获取数据失败，请重试");
            return;
        } else {
            itemID = bundle.getString(FavorableCarDetailActivity.ITEMID);
        }

        ThirdPartShopClient.getThirdFavorableCarDetail(shopID, itemID, new LoadingAnimResponseHandler(this, true) {
            @Override
            public void onSuccess(String response) {
                thirdCarDetailModel = JSONUtils.fromJson(response, ThirdCarDetailModel.class);
                if (thirdCarDetailModel == null || thirdCarDetailModel.baseInfo == null || thirdCarDetailModel.priceInfo == null) return;
                initView();
                initData(thirdCarDetailModel);
                requestFailView.setVisibility(View.GONE);
                mContentView.setVisibility(View.VISIBLE);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mContentView.setVisibility(View.GONE);

                requestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestFailView.setVisibility(View.GONE);
                        getData();
                    }
                });
                requestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
                requestFailView.setVisibility(View.VISIBLE);
            }
        });
    }


    private void initView() {
        //顶部图片展示和图片标题
        mBannerView = (ImageView) findViewById(R.id.banner);
        mAutoDescriptionsView = (TextView) findViewById(R.id.auto_descriptions);
        //价格相关
        mFavorablePriceDetailView = (RelativeLayout) findViewById(R.id.favorable_price_detail);
        mFavorablePriceView = (TextView) findViewById(R.id.favorable_price);
        mFavorableTimeLeftView = (TextView) findViewById(R.id.favorable_time_left);
        mReferencePriceTextView = (TextView) findViewById(R.id.reference_price_text);
        mReferencePriceView = (TextView) findViewById(R.id.reference_price);
        mreferenceRealPriceTextView = (TextView) findViewById(R.id.reference_real_price_text);
        mReferenceRealPriceView = (TextView) findViewById(R.id.reference_real_price);
        //自提点
        mPickupPointView = (VariousShowView) findViewById(R.id.pickup_points);
        //颜色
        mColorGroup = (T_AutoCarDetailColor) findViewById(R.id.color_view_group);
        //图文介绍
        thirdAutoDetailIntroduce = (ThirdAutoDetailIntroduce) findViewById(R.id.introduce);
        //打电话view
        mCallBarView = (CallBar) findViewById(R.id.call_bar);

        mLoanView = findViewById(R.id.auto_loan_view);

        downPaymentSeekBar = (MySeekBar) findViewById(R.id.myseekbar1);
        downPaymentSeekBar.setOnSeekBarChangeListener(new MySeekBar.onSeekBarChangeListener() {
            @Override
            public void onProgressChanged(int currentSection, String sectionText) {

            }
        });

        monthSeekBar = (MySeekBar) findViewById(R.id.myseekbar2);
        monthSeekBar.setOnSeekBarChangeListener(new MySeekBar.onSeekBarChangeListener() {
            @Override
            public void onProgressChanged(int currentSection, String sectionText) {

            }
        });
    }


    /**
     * 根据返回的数据展示内容
     */
    private void initData(ThirdCarDetailModel thirdCarDetailModel) {
        DebugLog.e("test", "initData");
        if (thirdCarDetailModel.baseInfo != null) {
            ImageUtil.setImage(this, mBannerView, thirdCarDetailModel.baseInfo.getItemPic(), R.drawable.sliderimage_pic_normal_slider);
            mAutoDescriptionsView.setText(thirdCarDetailModel.baseInfo.getItemName());

            //价格相关
            if (thirdCarDetailModel.baseInfo.isInPromotion.equals("0")) {
                mFavorablePriceDetailView.setVisibility(View.GONE);
                mReferencePriceTextView.setVisibility(View.GONE);

                mReferencePriceView.setTextColor(getResources().getColor(R.color.text_color_red));

                mReferencePriceView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                mReferencePriceView.setText(getPrice(thirdCarDetailModel.baseInfo.getItemPrice(), true));

                mReferencePriceView.getPaint().setFakeBoldText(true);

                mreferenceRealPriceTextView.setTextColor(getResources().getColor(R.color.text_color_orange));

                mReferenceRealPriceView.setTextColor(getResources().getColor(R.color.text_color_orange));

                mReferenceRealPriceView.setText(getPrice(thirdCarDetailModel.baseInfo.getItemTotalPrice(), false));

            } else if (thirdCarDetailModel.baseInfo.isInPromotion.equals("1")) {
                mFavorablePriceView.setText(getPrice(thirdCarDetailModel.baseInfo.getItemPrice(), true));

                if (getTimeLeft(thirdCarDetailModel.baseInfo.getPromotionEnd()) != 0) {
                    mFavorableTimeLeftView.setText(String.format("还剩 %d 天结束", Math.ceil(getTimeLeft(thirdCarDetailModel.baseInfo.getPromotionEnd()) * 1.0 / 86400)));
                } else {
                    mFavorableTimeLeftView.setText("活动已结束");
                }

                mReferencePriceView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

                mReferencePriceView.setText(getPrice(thirdCarDetailModel.baseInfo.getItemOrigPrice(), false));

                mReferenceRealPriceView.setText(getPrice(thirdCarDetailModel.baseInfo.getItemTotalPrice(), false));
            }

            //展示车身颜色数据
            if (thirdCarDetailModel.baseInfo.appearance.size() > 0) mColorGroup.initData(thirdCarDetailModel.baseInfo.appearance);

            //图文详情
            thirdAutoDetailIntroduce.setAutoDetail(thirdCarDetailModel.baseInfo);

            mCallBarView.setTitle("咨询电话");
        }

        if (thirdCarDetailModel.shopInfo != null) {
            final ArrayList< PickupPointT > shopLocations = new ArrayList<>();
            if (thirdCarDetailModel.shopInfo.getShopLocation() != null) shopLocations.add(thirdCarDetailModel.shopInfo.getShopLocation());
            mPickupPointView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitySwitcherThirdPartShop.toAMapNvai(FavorableCarDetailActivity.this, 0, shopLocations.get(0));
                }
            });

            //设置店铺电话
            mCallBarView.setNumber(thirdCarDetailModel.shopInfo.shopTel);
            this.setTitle(thirdCarDetailModel.shopInfo.shopTitle);
        }


        String[] strings = {"10%", "20%", "50%", "70%"};
        downPaymentSeekBar.setSectionData(Arrays.asList(strings));

        String[] strings1 = {"12期", "24期", "36期"};
        monthSeekBar.setSectionData(Arrays.asList(strings1));
    }


    public long getTimeLeft(String str) {
        if (TextUtils.isEmpty(str)) return 0;

        long endTime = DateUtil.getTime(str);

        DebugLog.e(TAG, " endTime " + endTime);

        long currentTime = DateUtil.getTime(thirdCarDetailModel.baseInfo.getServerTime());

        DebugLog.e(TAG, " currentTime " + currentTime);

        if (endTime < 0 || currentTime < 0) return 0;

        long timeLeft = endTime - currentTime;

        if (timeLeft <= 0) return 0;

        return timeLeft;
    }


    /**
     * 取得格式化后的金钱值
     *
     * @param price
     * @return
     */
    public String getPrice(String price, boolean flag) {
        return OtherUtil.amountFormat(price, flag);
    }


    /**
     * 图集
     */
    public void toPictures(View view) {
        try {
            String itemID = thirdCarDetailModel.baseInfo.itemID;
            if (!TextUtils.isEmpty(itemID)) ActivitySwitcherThirdPartShop.toAtlas(this, itemID);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public void toShowPriceDetail(View view) {
        if (thirdCarDetailModel.priceInfo == null) return;
        new ReferenceDetailsDialog(this, thirdCarDetailModel.priceInfo).show();
    }


    /**
     * 跳转参数详情
     *
     * @param view
     */
    public void clickToParameter(View view) {
        ActivitySwitcherThirdPartShop.toParameter(true, FavorableCarDetailActivity.this, thirdCarDetailModel.baseInfo.getExtID());
    }


    /**
     * 跳转到商铺首页
     *
     * @param view
     */
    public void toShopHome(View view) {
        ActivitySwitcherThirdPartShop.toShopDetails(FavorableCarDetailActivity.this, thirdCarDetailModel.shopInfo.shopID);
    }


    /** 咨询 **/
    public void clickAsk(View view) {
        if (UserInfoHelper.getInstance().isLogin(this)) {
            initAskAlertView();
        } else {
            UserInfoHelper.getInstance().loginAction(this, this);
        }
    }


    /**
     * 跳转到预约试驾
     *
     * @param view
     */
    public void toApplyTestDrive(View view) {
        ActivitySwitcherThirdPartShop.toTestDrive(this, thirdCarDetailModel.shopInfo.shopID, thirdCarDetailModel.baseInfo.itemID, thirdCarDetailModel.baseInfo.itemName, thirdCarDetailModel.shopInfo.shopTel, true, null);
    }


    /**
     * 跳转到询问底价
     *
     * @param view
     */
    public void toAskLeastPrice(View view) {
        ActivitySwitcherThirdPartShop.toAskLeastPrice(this, thirdCarDetailModel.shopInfo.shopID, thirdCarDetailModel.baseInfo.itemID, thirdCarDetailModel.baseInfo.itemName, thirdCarDetailModel.shopInfo.shopTel, true, null);
    }


    /** 呼叫客服 **/
    public void clickCallService(View view) {
//        if (specialCarDetailBean != null && specialCarDetailBean.autoInfo != null) {
//            SpecialAutoInfo specialAutoInfo = specialCarDetailBean.autoInfo;
//            ChatManager.getInstance().startChatWithGoods(itemID, specialAutoInfo.getItemName(),
// OtherUtil.amountFormat(specialAutoInfo.getItemPrice(), true), specialAutoInfo.getItemThumb(), "电商直营-车辆详情", "");
//        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialog);
        builder.setTitle("拨打电话").setMessage(thirdCarDetailModel.shopInfo.shopTel).setPositiveButton("拨打", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri uri = Uri.parse("tel:" + thirdCarDetailModel.shopInfo.shopTel);
                intent.setData(uri);
                FavorableCarDetailActivity.this.startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }


    /**
     * 打电话
     *
     * @param view
     */
    public void makePhoneCall(View view) {
        Intent dialIntent = new Intent();
        dialIntent.setAction(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + thirdCarDetailModel.shopInfo.shopTel));
        this.startActivity(dialIntent);
    }


    public void clickToEvaluate(View view) {
        Intent intent = new Intent();
        intent.setClassName(this, "com.hxqc.carcompare.ui.discuss.UserDiscussActivity");
//        Bundle bundle = new Bundle();
//        bundle.putString("ext_id", specialCarDetailBean.autoInfo.extID);
//        bundle.putString("brand", specialCarDetailBean.shopInfo.brand);
//        bundle.putString("series", "待填车系名称");
//        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        intent.putExtra("extID", thirdCarDetailModel.baseInfo.extID);
        intent.putExtra("brand", thirdCarDetailModel.baseInfo.brand);
        intent.putExtra("series", thirdCarDetailModel.baseInfo.series);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_third_shop_auto_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int i = item.getItemId();
        if (i == R.id.action_to_home) {
            ActivitySwitchBase.toMain(FavorableCarDetailActivity.this, 0);
        }

        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }


    @Override
    public void onLoginSuccess() {
        initAskAlertView();
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
                    ToastHelper.showRedToast(FavorableCarDetailActivity.this, "请输入您要咨询的问题");
                    return;
                }

                submitMessage();
            }
        });
    }


    private void submitMessage() {
        ThirdPartShopClient.submitAsk(thirdCarDetailModel.shopInfo == null ? "" : thirdCarDetailModel.shopInfo.shopID, itemID, mMessageView.getText() + "", "20", new DialogResponseHandler(this) {
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


    /****************************** 2016年11月17日20:46:44 4S店改版 ******************************************/
    private View mLoanView;

    private Dialog questionDialog;


    public void clickLoanQuestion(View view) {
//        ToastHelper.showGreenToast(this, "问题");
        if (questionDialog == null) {
            questionDialog = new Dialog(this, R.style.MMaterialDialog);
            questionDialog.setContentView(R.layout.layout_favorable_car_loan_dialog);
            questionDialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionDialog.dismiss();
                }
            });
        }
        questionDialog.show();
    }


    public void clickForAllLoanScheme(View view) {
        ToastHelper.showGreenToast(this, "所有贷款");
    }
}
