package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.promotion.OrderResponse;
import com.hxqc.mall.thirdshop.model.promotion.SalesDetail;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;


/**
 * 确认订单信息
 */
public class SalesOrderConfirmActivity extends BackActivity implements View.OnClickListener {

    RequestFailView mRequestFailView;
    CallBar mCallBar;
    EditText mEventNameView;
    EditText mShopNameView;
    EditText mInputNameView;
    EditText mInputPhoneView;
    Button mConfirmView;
    CheckBox mIsAgreeView;
    TextView mClaimerView;
    SalesDetail salesDetail;
    ThirdPartShopClient apiClient;
    TextView mTopTips;
//    final String regexStr_phone = "^0?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_activity_sales_order_confirm);
        salesDetail = getIntent().getParcelableExtra(ActivitySwitcherThirdPartShop.CONFIRM_ORDER_DATA_TAG);

        DebugLog.w("4s_pay", salesDetail.toString());

        apiClient = new ThirdPartShopClient();
        initView();
        if (salesDetail != null) {
            initData();
        } else {
            requestFailView();
        }
    }

    private void initView() {
        mRequestFailView = (RequestFailView) findViewById(R.id.refresh_fail_view);
        mCallBar = (CallBar) findViewById(R.id.call_bar);
        mEventNameView = (EditText) findViewById(R.id.tv_event_name);
        mShopNameView = (EditText) findViewById(R.id.tv_shop_name);
        mInputNameView = (EditText) findViewById(R.id.input_customer_name);
        mInputPhoneView = (EditText) findViewById(R.id.input_customer_phone);
        mConfirmView = (Button) findViewById(R.id.submit);
        mIsAgreeView = (CheckBox) findViewById(R.id.claimer_checkbox);
        mClaimerView = (TextView) findViewById(R.id.goto_claimer_detail);
        mTopTips = (TextView) findViewById(R.id.oc_tip_textview);

        mConfirmView.setOnClickListener(this);
        mClaimerView.setOnClickListener(this);

        mTopTips.setText(initTipStr());

    }

    private SpannableStringBuilder initTipStr() {
        String text1 = "简单填写以下信息，方便与您联系";
        String text2 = "（您的私人信息将会得到严格保密）";
        String str = text1 + text2;
        return OtherUtil.toCallText(str, text1.length(), str.length(), "#999997");
    }

    private void initData() {
        mEventNameView.setText(salesDetail.title);
        mShopNameView.setText(salesDetail.shopName);

        if (TextUtils.isEmpty(salesDetail.shopTel)) {
            mCallBar.setVisibility(View.GONE);
        } else {
            mCallBar.setTitle("咨询电话");
            mCallBar.setNumber(salesDetail.shopTel);
        }

        setInfo();
    }

    /**
     * 获取个人信息
     */
    private void setInfo() {
        UserInfoHelper.getInstance().getUserInfo(SalesOrderConfirmActivity.this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {

                if (meData != null) {
                    if (!TextUtils.isEmpty(meData.fullname)) {
                        mInputNameView.setText(meData.fullname);
                        mInputNameView.setSelection(meData.fullname.length());
                    }

                    if (!TextUtils.isEmpty(meData.phoneNumber)) {
                        mInputPhoneView.setText(meData.phoneNumber);
                        mInputPhoneView.setSelection(meData.phoneNumber.length());
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        }, true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit) {
            final String username = mInputNameView.getText().toString().trim();
            final String userPhone = mInputPhoneView.getText().toString().trim();
            boolean isAgree = mIsAgreeView.isChecked();
//            if (TextUtils.isEmpty(username)) {
//                ToastHelper.showRedToast(SalesOrderConfirmActivity.this, "请输入用户名");
//                return;
//            }
//
//            String nicknameRegex = "^[A-Za-z0-9\\-\\u4e00-\\u9fa5]{2,20}$";
//            if (!username.matches(nicknameRegex)) {
//                ToastHelper.showYellowToast(this, "请检查姓名格式：2~20个字,可由中英文,字母,数字组成");
//                return;
//            }

            if (!FormatCheck.checkName(username, SalesOrderConfirmActivity.this)) {
                return;
            }

            if (FormatCheck.checkPhoneNumber(userPhone, SalesOrderConfirmActivity.this) != FormatCheck.CHECK_SUCCESS) {
                return;
            }

            if (isAgree) {
                UserInfoHelper.getInstance().loginAction(this, 20, new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        String siteID=AreaSiteUtil.getInstance(SalesOrderConfirmActivity.this).getSiteID();
                        String siteName=AreaSiteUtil.getInstance(SalesOrderConfirmActivity.this).getSiteName();
                        apiClient.createOrder(
                                siteID,siteName,
                                salesDetail.promotionID, salesDetail.shopID, username, userPhone,
                                new LoadingAnimResponseHandler(SalesOrderConfirmActivity.this, true) {
                                    @Override
                                    public void onSuccess(String response) {
                                        DebugLog.i("test_order", response);
                                        OrderResponse orderResponse = JSONUtils.fromJson(response, new TypeToken<OrderResponse>() {
                                        });
                                        ActivitySwitcherThirdPartShop.toPayDeposit(salesDetail.subscription + "",
                                                orderResponse.orderID, salesDetail.shopTel, SalesOrderConfirmActivity.this);
                                        finish();
                                    }
                                });
                    }
                });

            } else {
                ToastHelper.showRedToast(SalesOrderConfirmActivity.this, "请勾选个人信息保护声明");
            }

        } else if (v.getId() == R.id.goto_claimer_detail) {
//            ToastHelper.showGreenToast(SalesOrderConfirmActivity.this, "打开声明");
            ActivitySwitcherThirdPartShop.toDeclares(SalesOrderConfirmActivity.this);
        }
    }

    /**
     * 获取数据失败  刷新显示
     */
    private void requestFailView() {
        mRequestFailView.setEmptyDescription("获取数据失败");
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
        mRequestFailView.setVisibility(View.VISIBLE);
    }
}
