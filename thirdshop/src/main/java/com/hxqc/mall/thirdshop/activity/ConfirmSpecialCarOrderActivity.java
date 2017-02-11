package com.hxqc.mall.thirdshop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.OrderID;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.HashMap;

/**
 * Function: 特卖车辆订单确定页面
 *
 * @author 袁秉勇
 * @since 2016年05月13日
 */
public class ConfirmSpecialCarOrderActivity extends BackActivity implements UserInfoHelper.OnLoginListener {
    private final static String TAG = ConfirmSpecialCarOrderActivity.class.getSimpleName();
    private Context mContext;

    public static final String DATA = "data";
    public static final String SHOPTEL = "shopTel";

    private TextView mTipTextView;
    private MaterialEditText mCarNameView, mShopNameView, mCustomerNameView, mCustomerPhoneView;
    private Button mSubmitView;
    private CheckBox mAgreeView;
    private CallBar mCallBarView;

    private ThirdPartShopClient ThirdPartShopClient;

    private AreaSiteUtil areaSiteUtil;

    private HashMap< String, String > hashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ThirdPartShopClient = new ThirdPartShopClient();

        hashMap = (HashMap) getIntent().getSerializableExtra(DATA);

        setContentView(R.layout.activity_confirm_special_car_order);

        mTipTextView = (TextView) findViewById(R.id.tip_textview);
        mCarNameView = (MaterialEditText) findViewById(R.id.car_name);
        mShopNameView = (MaterialEditText) findViewById(R.id.shop_name);
        mCustomerNameView = (MaterialEditText) findViewById(R.id.customer_name);
        mCustomerPhoneView = (MaterialEditText) findViewById(R.id.customer_phone);
        mSubmitView = (Button) findViewById(R.id.submit);
        mAgreeView = (CheckBox) findViewById(R.id.clause_checkbox);
        mCallBarView = (CallBar) findViewById(R.id.call_bar);

        mCallBarView.setNumber(getIntent().getStringExtra(SHOPTEL));
        mCallBarView.setTitle("咨询电话");

        SpannableString spannableString = new SpannableString(mTipTextView.getText());
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.third_tip_gray)), 15, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mTipTextView.setText(spannableString);

        mCarNameView.setText(TextUtils.isEmpty(hashMap.get("carName")) ? "未知" : hashMap.get("carName"));
        mShopNameView.setText(TextUtils.isEmpty(hashMap.get("shopName")) ? "未知" : hashMap.get("shopName"));

        mSubmitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaSiteUtil = AreaSiteUtil.getInstance(mContext);
                if (UserInfoHelper.getInstance().isLogin(mContext)) {
                    submitMessage();
                } else {
                    UserInfoHelper.getInstance().loginAction(mContext, ConfirmSpecialCarOrderActivity.this);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            finishSelf();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           finishSelf();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void finishSelf() {
        Intent intent = new Intent();
        this.setResult(1, intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishSelf();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void submitMessage() {
        if (TextUtils.isEmpty(mCarNameView.getText()) || TextUtils.isEmpty(mShopNameView.getText())) {
            ToastHelper.showRedToast(mContext, "数据存在异常，请稍后重试");
            return;
        }

        String nicknameRegex = "^[A-Za-z0-9\\-\\u4e00-\\u9fa5]{2,6}$";
        if (!mCustomerNameView.getText().toString().matches(nicknameRegex)) {
            ToastHelper.showYellowToast(mContext, "请检查姓名格式：2~6个字,可由中英文,字母,数字组成");
            return;
        }

        if (FormatCheck.checkPhoneNumber(mCustomerPhoneView.getText().toString().trim(), mContext) != 0) {
            return;
        }

        if (!mAgreeView.isChecked()) {
            ToastHelper.showRedToast(mContext, "请勾选个人信息保护声明");
            return;
        }

        ThirdPartShopClient.confirmSpecialCarOrder(hashMap, mCustomerNameView.getText() + "", mCustomerPhoneView.getText() + "", areaSiteUtil.getSiteID(), areaSiteUtil.getCityGroup(""), new LoadingAnimResponseHandler(mContext) {
            @Override
            public void onSuccess(String response) {
                OrderID orderID = JSONUtils.fromJson(response, OrderID.class);
                if (orderID != null && !TextUtils.isEmpty(orderID.orderID)) {
                    ActivitySwitcherThirdPartShop.toPayDeposit(hashMap.get("amount"), orderID.orderID, hashMap.get("shopTel"), ConfirmSpecialCarOrderActivity.this);
                    new BaseSharedPreferencesHelper(ConfirmSpecialCarOrderActivity.this).setOrderChange(true);
                } else {
                    ToastHelper.showRedToast(ConfirmSpecialCarOrderActivity.this, "未知错误");
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastHelper.showRedToast(mContext, "信息提交失败");
            }
        });
    }


    /**
     * 跳转到协议详情页面
     *
     * @param view
     */
    public void GotoClauseDetail(View view) {
        ActivitySwitcherThirdPartShop.toDeclares(this);
    }


    @Override
    public void onLoginSuccess() {
        submitMessage();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (areaSiteUtil != null) areaSiteUtil.destroy();
    }
}
