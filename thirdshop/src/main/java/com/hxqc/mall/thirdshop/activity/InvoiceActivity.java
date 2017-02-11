package com.hxqc.mall.thirdshop.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.fragment.CityChooseFragment;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.InvoiceInfo;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DisplayTools;

/**
 * 说明:发票信息
 *
 * @author: 吕飞
 * @since: 2016-07-12
 * Copyright:恒信汽车电子商务有限公司
 */
public class InvoiceActivity extends BackActivity implements View.OnClickListener, CityChooseFragment.OnAreaChooseInteractionListener, RadioGroup.OnCheckedChangeListener {
    InvoiceInfo mInvoiceInfo;
    String mType;//用品1，保养2
    CityChooseFragment mCityChooseFragment;
    RadioGroup mContentGroupView, mTitleGroupView;
    RadioButton mContentDetailView, mContentAccessoryView, mContentNoneView, mTitlePersonalView, mTitleCompanyView;
    LinearLayout mInvoiceInfoView;
    EditText mCompanyNameView, mReceiverNameView, mReceiverPhoneView, mReceiverAreaView, mReceiverAddressView;
    Button mConfirmView;
    FrameLayout mRightView;
    DrawerLayout mDrawerLayoutView;
    Animation mShowAnimation;
    Animation mHideAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        mInvoiceInfo = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelable(ActivitySwitcherThirdPartShop.INVOICE_INFO);
        mType = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ActivitySwitcherThirdPartShop.FROM);
        initView();
        fillData();
    }

    private void fillData() {
        if (mType.equals("1")) {
            mContentAccessoryView.setText("用品");
        } else {
            mContentAccessoryView.setText("保养");
        }
        mContentDetailView.setChecked(mInvoiceInfo.getInvoiceContent().equals("0"));
        mContentAccessoryView.setChecked(mInvoiceInfo.getInvoiceContent().equals("1") || mInvoiceInfo.getInvoiceContent().equals("2"));
        mContentNoneView.setChecked(mInvoiceInfo.getInvoiceContent().equals("-1"));
        OtherUtil.setVisible(mInvoiceInfoView, !mInvoiceInfo.getInvoiceContent().equals("-1"));
        mTitlePersonalView.setChecked(mInvoiceInfo.getInvoiceTitle().equals("个人"));
        mTitleCompanyView.setChecked(!mTitlePersonalView.isChecked());
        OtherUtil.setVisible(mCompanyNameView, mTitleCompanyView.isChecked());
        mCompanyNameView.setText(mInvoiceInfo.getCompanyName());
        mReceiverNameView.setText(mInvoiceInfo.getReceivableser());
        mReceiverPhoneView.setText(mInvoiceInfo.getReceivablesPhone());
        mReceiverAreaView.setText(mInvoiceInfo.getReceivablesArea());
        mReceiverAddressView.setText(mInvoiceInfo.getReceivablesAddress());
        if (!TextUtils.isEmpty(mInvoiceInfo.getReceivablesArea())) {
            mCityChooseFragment.setDefaultAreaData(mInvoiceInfo.receivablesProvince, mInvoiceInfo.receivablesCity, mInvoiceInfo.receivablesCity);
        }
    }

    private void initView() {
        mShowAnimation = AnimationUtils.loadAnimation(this, R.anim.invoice_show);
        mHideAnimation = AnimationUtils.loadAnimation(this, R.anim.invoice_hidden);
        mContentGroupView = (RadioGroup) findViewById(R.id.content_group);
        mTitleGroupView = (RadioGroup) findViewById(R.id.title_group);
        mContentDetailView = (RadioButton) findViewById(R.id.content_detail);
        mContentAccessoryView = (RadioButton) findViewById(R.id.content_accessory);
        mContentNoneView = (RadioButton) findViewById(R.id.content_none);
        mTitlePersonalView = (RadioButton) findViewById(R.id.title_personal);
        mTitleCompanyView = (RadioButton) findViewById(R.id.title_company);
        mInvoiceInfoView = (LinearLayout) findViewById(R.id.invoice_info);
        mCompanyNameView = (EditText) findViewById(R.id.company_name);
        mReceiverNameView = (EditText) findViewById(R.id.receiver_name);
        mReceiverPhoneView = (EditText) findViewById(R.id.receiver_phone);
        mReceiverAreaView = (EditText) findViewById(R.id.receiver_area);
        mReceiverAddressView = (EditText) findViewById(R.id.receiver_address);
        mConfirmView = (Button) findViewById(R.id.confirm);
        mRightView = (FrameLayout) findViewById(R.id.right);
        mDrawerLayoutView = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCityChooseFragment = new CityChooseFragment();
        mContentGroupView.setOnCheckedChangeListener(this);
        mTitleGroupView.setOnCheckedChangeListener(this);
        mReceiverAreaView.setOnClickListener(this);
        mConfirmView.setOnClickListener(this);
        mCityChooseFragment.setAreaChooseListener(this);
        initRight();
        OtherUtil.setDrawerMode(mDrawerLayoutView);
    }

    //    浮层初始化
    @SuppressLint("RtlHardcoded")
    private void initRight() {
        mRightView.setLayoutParams(new DrawerLayout.LayoutParams(DisplayTools.getScreenWidth(this) * 4 / 5, DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.right, mCityChooseFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.receiver_area) {
            com.hxqc.mall.core.util.OtherUtil.closeSoftKeyBoard(this, v);
            mDrawerLayoutView.openDrawer(Gravity.RIGHT);
        } else if (i == R.id.confirm) {
            if (check()) {
                if (mTitleCompanyView.isChecked()) {
                    mInvoiceInfo.invoiceTitle = mCompanyNameView.getText().toString().trim();
                }
                mInvoiceInfo.companyName = mCompanyNameView.getText().toString().trim();
                mInvoiceInfo.receivableser = mReceiverNameView.getText().toString().trim();
                mInvoiceInfo.receivablesPhone = mReceiverPhoneView.getText().toString().trim();
                mInvoiceInfo.receivablesAddress = mReceiverAddressView.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra(ActivitySwitcherThirdPartShop.INVOICE_INFO, mInvoiceInfo);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    private boolean check() {
        if (!mInvoiceInfo.getInvoiceContent().equals("-1")) {
            if (mTitleCompanyView.isChecked()) {
                if (!FormatCheck.checkNameForInvoice(mCompanyNameView.getText().toString().trim(), this)) {
                    return false;
                }
            }
            if (!FormatCheck.checkReceiverName(mReceiverNameView.getText().toString().trim(), this)) {
                return false;
            } else if (FormatCheck.checkPhoneNumberForInvoice(mReceiverPhoneView.getText().toString().trim(), this) != FormatCheck.CHECK_SUCCESS) {
                return false;
            } else if (TextUtils.isEmpty(mInvoiceInfo.getReceivablesArea())) {
                ToastHelper.showYellowToast(this, "请选择所在区域");
                return false;
            } else if (TextUtils.isEmpty(mReceiverAddressView.getText().toString().trim())) {
                ToastHelper.showYellowToast(this, "请输入详细地址");
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_invoice, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        if (item.getItemId() == R.id.action_invoice) {
//
//        }
//        return false;
//    }
    @Override
    public void OnAreaChooseInteraction(String provinces, String city, String district, String pID, String cID, String dID) {
        mInvoiceInfo.receivablesProvince = provinces;
        mInvoiceInfo.receivablesCity = city;
        mInvoiceInfo.receivablesRegion = district;
        mReceiverAreaView.setText(mInvoiceInfo.getReceivablesArea());
        mDrawerLayoutView.closeDrawer(Gravity.RIGHT);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.content_group) {
            if (checkedId == R.id.content_detail) {
                if (mInvoiceInfoView.getVisibility() == View.GONE) {
                    mInvoiceInfoView.setVisibility(View.VISIBLE);
                    mInvoiceInfoView.startAnimation(mShowAnimation);
                }
                mInvoiceInfo.invoiceContent = "0";
            } else if (checkedId == R.id.content_none) {
                if (mInvoiceInfoView.getVisibility() == View.VISIBLE) {
                    mInvoiceInfoView.startAnimation(mHideAnimation);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mInvoiceInfoView.setVisibility(View.GONE);
                        }
                    }, 200);
                }
                mInvoiceInfo.invoiceContent = "-1";
            } else {
                if (mInvoiceInfoView.getVisibility() == View.GONE) {
                    mInvoiceInfoView.setVisibility(View.VISIBLE);
                    mInvoiceInfoView.startAnimation(mShowAnimation);
                }
                mInvoiceInfo.invoiceContent = mType;
            }
        } else {
            OtherUtil.setVisible(mCompanyNameView, checkedId == R.id.title_company);
            if (checkedId == R.id.title_company) {
                mInvoiceInfo.invoiceTitle = mCompanyNameView.getText().toString().trim();
            } else {
                mInvoiceInfo.invoiceTitle = "个人";
            }
        }
    }
}
