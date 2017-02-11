package com.hxqc.mall.usedcar.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.VoiceCaptchaView;
import com.hxqc.mall.launch.view.CountdownButton;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.model.CarDetail;
import com.hxqc.mall.usedcar.model.NewPrice;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.mall.usedcar.views.CarDetailTopView;
import com.hxqc.mall.usedcar.views.ScrollViewBottom;
import com.hxqc.mall.usedcar.views.ScrollViewContainer;
import com.hxqc.mall.usedcar.views.ScrollViewTop;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;


/**
 * 平台车辆详情页
 * Created by huangyi on 15/10/23.
 */
public class PlatformCarDetailActivity extends NoBackActivity implements ScrollViewTop.OnScrollListener,
        View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final String CAR_SOURCE_NO = "car_source_no";
    public static final int REQUEST_CODE = 0;
    UsedCarApiClient mClient;
    CarDetail mCarDetail; //数据源

    Toolbar mToolbar;
    ScrollViewContainer mContainerView;
    CarDetailTopView mTopView;
    TextView mFirstView, mMileageView, mAgeView, mAddressView, mInstalmentView, mQAView, mOrderView, mOrderDateView;
    CountdownButton mOrderSendView;
    ScrollViewBottom mWebView;
    LinearLayout mBottomView;
    RequestFailView mFailView;

    String mInstalmentID = "", mQAID = "";
    int year = -1, monthOfYear = -1, dayOfMonth = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_car_detail);
        mClient = new UsedCarApiClient();
        initView();
        initData(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == InstalmentActivity.RESULT_CODE) {
            mInstalmentID = data.getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(InstalmentActivity.INSTALMENT_ID);
            mInstalmentView.setSelected(!TextUtils.isEmpty(mInstalmentID));
            updatePrice();

        } else if (requestCode == REQUEST_CODE && resultCode == QAActivity.RESULT_CODE) {
            mQAID = data.getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(QAActivity.QA_ID);
            mQAView.setSelected(!TextUtils.isEmpty(mQAID));
            updatePrice();
        }
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.platform_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setTitleTextColor(Color.parseColor("#00ffffff"));
        setSupportActionBar(mToolbar);

        mContainerView = (ScrollViewContainer) findViewById(R.id.platform_container);
        mContainerView.initActionBarHeight(56);
        mContainerView.initBottomViewHeight(45);
        ScrollViewTop scroll = (ScrollViewTop) findViewById(R.id.platform_scroll);
        int height = (int) (DisplayTools.getScreenWidth(this) / 1.75 - DisplayTools.dip2px(this, 56));
        scroll.setHeight(height / 3);
        scroll.setOnScrollListener(mToolbar,this);
        mTopView = (CarDetailTopView) findViewById(R.id.platform_top);
        mFirstView = (TextView) findViewById(R.id.platform_first);
        mMileageView = (TextView) findViewById(R.id.platform_mileage);
        mAgeView = (TextView) findViewById(R.id.platform_age);
        mAddressView = (TextView) findViewById(R.id.platform_address);
        mInstalmentView = (TextView) findViewById(R.id.platform_instalment);
        mInstalmentView.setOnClickListener(this);
        mQAView = (TextView) findViewById(R.id.platform_qa);
        mQAView.setOnClickListener(this);

        mWebView = (ScrollViewBottom) findViewById(R.id.platform_wv);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mBottomView = (LinearLayout) findViewById(R.id.platform_bottom);
        mOrderView = (TextView) findViewById(R.id.platform_order);
        mOrderView.setOnClickListener(this);
        findViewById(R.id.platform_phone).setOnClickListener(this);

        mFailView = (RequestFailView) findViewById(R.id.platform_fail);
        mFailView.setEmptyDescription("没有数据");
        mFailView.setEmptyButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(false);
            }
        });
        mFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(false);
            }
        });

        mBottomView.setVisibility(View.GONE);
        mContainerView.setVisibility(View.GONE);
    }

    private void initData(boolean showAnim) {
        mClient.getProductDetail(UserInfoHelper.getInstance().getPhoneNumber(this), getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(CAR_SOURCE_NO), new LoadingAnimResponseHandler(this, showAnim) {
            @Override
            public void onSuccess(String response) {
                mCarDetail = JSONUtils.fromJson(response, CarDetail.class);

                if (mCarDetail == null) {
                    mBottomView.setVisibility(View.GONE);
                    mContainerView.setVisibility(View.GONE);
                    mFailView.setVisibility(View.VISIBLE);
                    mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                    return;
                } else {
                    mBottomView.setVisibility(View.VISIBLE);
                    mContainerView.setVisibility(View.VISIBLE);
                    mFailView.setVisibility(View.GONE);
                }

                //非上架状态 不能预约看车
                if (Integer.valueOf(mCarDetail.state) != 0) {
                    mOrderView.setEnabled(false);
                    mOrderView.setTextColor(ContextCompat.getColor(PlatformCarDetailActivity.this, R.color.divider));
                    mOrderView.setBackgroundColor(Color.parseColor("#999999"));
                    if (Integer.valueOf(mCarDetail.state) == 1) {
                        mOrderView.setText("已   售");
                    } else if (Integer.valueOf(mCarDetail.state) == 2) {
                        mOrderView.setText("已下架");
                    } else if (Integer.valueOf(mCarDetail.state) == 3) {
                        mOrderView.setText("待上架");
                    }
                }

                mToolbar.setTitle(mCarDetail.car_name);
                mTopView.initData(mCarDetail);
                mFirstView.setText("上牌时间: " + mCarDetail.first_on_card.substring(0, 7));
                mMileageView.setText("行驶里程: " + String.format("%s万公里", mCarDetail.car_mileage));
                mAgeView.setText("车        龄: " + mCarDetail.getCarAgeStr());
                mAddressView.setText("看车地点: " + mCarDetail.look_address);
                mWebView.loadUrl(mCarDetail.detail_url);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mBottomView.setVisibility(View.GONE);
                mContainerView.setVisibility(View.GONE);
                mFailView.setVisibility(View.VISIBLE);
                mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.platform_instalment && null != mCarDetail) { //分期购车
            UsedCarActivitySwitcher.toInstalmentForResult(this, REQUEST_CODE, mCarDetail.qa.item_instalment, mInstalmentID,
                    mCarDetail.detail_url.substring(0, mCarDetail.detail_url.lastIndexOf("/")) + "/item_instalment");

        } else if (id == R.id.platform_qa && null != mCarDetail) { //恒信质保
            UsedCarActivitySwitcher.toQAForResult(this, REQUEST_CODE, mCarDetail.qa.item_qa, mCarDetail.qa.detail_table, mQAID,
                    mCarDetail.detail_url.substring(0, mCarDetail.detail_url.lastIndexOf("/")) + "/item_qa");

        } else if (id == R.id.platform_order && null != mCarDetail) { //预约看车
            showOrderDialog();

        } else if (id == R.id.platform_phone && null != mCarDetail) { //客服电话
            new android.support.v7.app.AlertDialog.Builder(this, R.style.MaterialDialog)
                    .setTitle("拨打电话")
                    .setMessage("400-1868-555")
                    .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:400-1868-555")));
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        }
    }

    /**
     * 预约看车Dialog
     **/
    private void showOrderDialog() {
        View root = LayoutInflater.from(this).inflate(R.layout.dialog_car_order, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(root, 0, 0, 0, 0); //去除自定义对话框的黑色边框
        dialog.show();

        final EditText mOrderPhoneView = (EditText) root.findViewById(R.id.order_phone);
        final ImageView mOrderClearView = (ImageView) root.findViewById(R.id.order_clear);
        final EditText mOrderCodeView = (EditText) root.findViewById(R.id.order_code);
        final VoiceCaptchaView mOrderVoiceView = (VoiceCaptchaView) root.findViewById(R.id.order_voice);
        mOrderSendView = (CountdownButton) root.findViewById(R.id.order_send);
        mOrderDateView = (TextView) root.findViewById(R.id.order_date);

        mOrderPhoneView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mOrderPhoneView.getText().length() != 0 && mOrderPhoneView.hasFocus()) {
                    mOrderClearView.setVisibility(View.VISIBLE);
                } else {
                    mOrderClearView.setVisibility(View.GONE);
                }
                //验证手机号
                String phoneStr = mOrderPhoneView.getText().toString().trim();
                if (FormatCheck.isPhoneNumber(phoneStr) && mOrderPhoneView.hasFocus()) {
                    mOrderSendView.setTextColor(ContextCompat.getColor(PlatformCarDetailActivity.this, R.color.title_and_main_text));
                } else {
                    mOrderSendView.setTextColor(ContextCompat.getColor(PlatformCarDetailActivity.this, R.color.text_color_subheading));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mOrderClearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderPhoneView.setText("");
            }
        });
        if (UserInfoHelper.getInstance().isLogin(this)) {
            mOrderVoiceView.setVisibility(View.GONE);
            mOrderCodeView.setVisibility(View.GONE);
            mOrderSendView.setVisibility(View.GONE);
        } else {
            mOrderVoiceView.setVisibility(View.VISIBLE);
            mOrderCodeView.setVisibility(View.VISIBLE);
            mOrderSendView.setVisibility(View.VISIBLE);
        }
        mOrderSendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneStr = mOrderPhoneView.getText().toString().trim();
                if (FormatCheck.isPhoneNumber(phoneStr))
                    mOrderSendView.identifyingStart3(phoneStr, 20);
            }
        });
        mOrderDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        mOrderVoiceView.setVoiceCaptchaListener(new VoiceCaptchaView.VoiceCaptchaListener() {
            @Override
            public void getVoiceCaptcha() {
                mClient.getVoiceCaptcha(mOrderPhoneView.getText().toString().trim(), UsedCarApiClient.VC_APPOINTMENT,
                        mOrderVoiceView.getVoiceCaptchaResponseHandler());
            }

            @Override
            public String getPhoneNumber() {
                return mOrderPhoneView.getText().toString().trim();
            }
        });

        root.findViewById(R.id.order_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String phoneStr = mOrderPhoneView.getText().toString().trim();
                String dateStr = mOrderDateView.getText().toString().trim();

                //验证手机号
                if (!FormatCheck.isPhoneNumber(phoneStr)) {
                    ToastHelper.showYellowToast(PlatformCarDetailActivity.this, "请输入正确的手机号");
                    return;
                }

                //验证预约时间
                if (TextUtils.isEmpty(dateStr)) {
                    ToastHelper.showYellowToast(PlatformCarDetailActivity.this, "请选择大于或等于当前时间的日期");
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                int nowYear = calendar.get(Calendar.YEAR);
                int nowMonth = calendar.get(Calendar.MONTH);
                int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
                if ((nowYear > year) || (nowYear == year && nowMonth > monthOfYear) || (nowYear == year && nowMonth == monthOfYear && nowDay > dayOfMonth)) {
                    ToastHelper.showYellowToast(PlatformCarDetailActivity.this, "请选择大于或等于当前时间的日期");
                    return;
                }

                mClient.appointment(phoneStr, mOrderCodeView.getText().toString(), dateStr, mCarDetail.car_source_no, mInstalmentID, mQAID,
                        new LoadingAnimResponseHandler(PlatformCarDetailActivity.this, true) {
                            @Override
                            public void onStart() {
                                super.onStart();
                                v.setEnabled(false);
                            }

                            @Override
                            public void onSuccess(String response) {
                                ToastHelper.showGreenToast(mContext, "提交预约成功");
                                dialog.dismiss();
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                v.setEnabled(true);
                            }
                        });
            }
        });
    }

    private void showDateDialog() {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                PlatformCarDetailActivity.this,
                this.year != -1 ? this.year : now.get(Calendar.YEAR),
                this.monthOfYear != -1 ? this.monthOfYear : now.get(Calendar.MONTH),
                this.dayOfMonth != -1 ? this.dayOfMonth : now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(true);
        dpd.vibrate(true);
        dpd.dismissOnPause(false);
        dpd.showYearPickerFirst(false);
        dpd.setAccentColor(ContextCompat.getColor(PlatformCarDetailActivity.this, R.color.cursor_orange));
        dpd.show(getFragmentManager(), "DatePickerDialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
        if (monthOfYear < 9) {
            if (dayOfMonth < 10) {
                mOrderDateView.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
            } else {
                mOrderDateView.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        } else {
            if (dayOfMonth < 10) {
                mOrderDateView.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
            } else {
                mOrderDateView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }
    }

    private void updatePrice() {
        mClient.getNewPrice(mQAID, mInstalmentID, mCarDetail.car_source_no, new LoadingAnimResponseHandler(PlatformCarDetailActivity.this, false) {
            @Override
            public void onSuccess(String response) {
                NewPrice price = JSONUtils.fromJson(response, NewPrice.class);
                mCarDetail.estimate_price = price.new_price;
                mTopView.updatePrice(mCarDetail);
            }
        });
    }

    @Override
    public void onScrollChange(float f1) {
        mToolbar.getBackground().setAlpha((int) (f1 * 255));
        String alpha = Integer.toHexString((int) (f1 * 255));
        if (alpha.length() == 1) {
            alpha = "0" + alpha;
        }
        mToolbar.setTitleTextColor(Color.parseColor("#" + alpha + "ffffff"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tohome_hy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_to_home) {
            ActivitySwitchBase.toMain(this, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
