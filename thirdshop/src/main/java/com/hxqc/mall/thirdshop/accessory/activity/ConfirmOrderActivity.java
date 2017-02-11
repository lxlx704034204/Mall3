package com.hxqc.mall.thirdshop.accessory.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.fragment.AccessoryShopListFragment;
import com.hxqc.mall.thirdshop.accessory.model.ConfirmOrderItem;
import com.hxqc.mall.thirdshop.accessory.model.OrderSubmitObject;
import com.hxqc.mall.thirdshop.accessory.model.ShoppingCartSubmitObject;
import com.hxqc.mall.thirdshop.accessory.model.SubmitOrderInfo;
import com.hxqc.mall.thirdshop.accessory.model.SubmitProduct;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;
import com.hxqc.mall.thirdshop.accessory.views.ShopInOrder;
import com.hxqc.mall.thirdshop.model.InvoiceInfo;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 说明:确认订单
 *
 * @author: 吕飞
 * @since: 2016-02-24
 * Copyright:恒信汽车电子商务有限公司
 */
public class ConfirmOrderActivity extends AccessoryBackActivity implements View.OnClickListener {
    ConfirmOrderItem mConfirmOrderItem;//订单数据
    EditText mContactNameView;//联系人名字
    EditText mContactPhoneView;//联系人电话
    TextView mPickupShopView;//选门店
    ShopInOrder mShopInOrderView;//商品
    TextView mProductNumView;
    TextView mAmountOrderView;
    TextView mPackageCutView;
    TextView mAmountToPayView;
    EditText mNotesView;
    TextView mAmountView;
    TextView mInvoiceSummaryView;
    Button mSubmitView;//提交按钮
    RelativeLayout mInvoiceInfoView;
    ArrayList<SubmitOrderInfo> mSubmitOrderInfos = new ArrayList<>();//需要提交的数据
    String mShopID;
    String mShopName;
    InvoiceInfo mInvoiceInfo = new InvoiceInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConfirmOrderItem = JSONUtils.fromJson(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ActivitySwitcherAccessory.CONFIRM_ORDER_ITEMS), ConfirmOrderItem.class);
        setContentView(R.layout.activity_accessory_confirm_order);
        initView();
        fillData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ActivitySwitcherAccessory.TO_SHOP_LIST) {
                mShopID = data.getStringExtra(AccessoryShopListFragment.CHOOSE_SHOP_ID);
                mShopName = data.getStringExtra(AccessoryShopListFragment.CHOOSE_SHOP_NAME);
                mPickupShopView.setText(mShopName);
            } else if (requestCode == ActivitySwitcherThirdPartShop.TO_INVOICE) {
                mInvoiceInfo = data.getParcelableExtra(ActivitySwitcherThirdPartShop.INVOICE_INFO);
                mInvoiceSummaryView.setText(mInvoiceInfo.getInvoiceSummary());
            }
        }
    }

    //填充数据
    private void fillData() {
        UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                if (meData != null && !TextUtils.isEmpty(meData.fullname)) {
                    mContactNameView.setText(meData.fullname);
                }
            }

            @Override
            public void onFinish() {

            }
        }, false);
        mContactPhoneView.setText(UserInfoHelper.getInstance().getPhoneNumber(this));
        mProductNumView.setText(mConfirmOrderItem.getTotalCount());
        mAmountOrderView.setText(OtherUtil.amountFormat(mConfirmOrderItem.orderTotalAmount, true));
        mPackageCutView.setText(OtherUtil.amountFormat(mConfirmOrderItem.packageDiscount, true));
        mAmountToPayView.setText(OtherUtil.amountFormat(mConfirmOrderItem.actualPaymentAmount, true));
        mAmountView.setText("应付总金额：" + OtherUtil.amountFormat(mConfirmOrderItem.actualPaymentAmount, true));
        mShopInOrderView.fillData(mConfirmOrderItem.itemList, false);
    }

    private void initView() {
        mInvoiceSummaryView = (TextView) findViewById(R.id.invoice_summary);
        mInvoiceInfoView = (RelativeLayout) findViewById(R.id.invoice_info);
        mContactNameView = (EditText) findViewById(R.id.contact_name);
        mContactPhoneView = (EditText) findViewById(R.id.contact_phone);
        mPickupShopView = (TextView) findViewById(R.id.pickup_shop);
        mShopInOrderView = (ShopInOrder) findViewById(R.id.shop_in_order);
        mProductNumView = (TextView) findViewById(R.id.product_num);
        mAmountOrderView = (TextView) findViewById(R.id.amount_order);
        mPackageCutView = (TextView) findViewById(R.id.package_cut);
        mAmountToPayView = (TextView) findViewById(R.id.amount_to_pay);
        mNotesView = (EditText) findViewById(R.id.notes);
        mAmountView = (TextView) findViewById(R.id.amount);
        mSubmitView = (Button) findViewById(R.id.submit);
        mSubmitView.setOnClickListener(this);
        mPickupShopView.setOnClickListener(this);
        mInvoiceInfoView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.submit) {
            if (FormatCheck.checkNameForAcc(mContactNameView.getText().toString(), this) && FormatCheck.checkPhoneNumber(mContactPhoneView.getText().toString().trim(), this) == FormatCheck.CHECK_SUCCESS && checkShop()) {
                mAccessoryApiClient.submitAccessoryOrder(mContactNameView.getText().toString(), mContactPhoneView.getText().toString(), mShopID, mNotesView.getText().toString().trim(), mConfirmOrderItem.actualPaymentAmount, getSubmitJson(), mInvoiceInfo, new LoadingAnimResponseHandler(this) {
                    @Override
                    public void onSuccess(String response) {
                        new BaseSharedPreferencesHelper(ConfirmOrderActivity.this).setOrderChange(true);
                        mSubmitOrderInfos = JSONUtils.fromJson(response, new TypeToken<ArrayList<SubmitOrderInfo>>() {
                        });
                        if (mSubmitOrderInfos != null && mSubmitOrderInfos.size() > 0) {
                            ShoppingCartActivity.sNeedRefresh = true;
                            ActivitySwitcherAccessory.toPaySubscription(ConfirmOrderActivity.this, mSubmitOrderInfos, mConfirmOrderItem.actualPaymentAmount);
                            finish();
                        }
                    }
                });
            }
        } else if (i == R.id.pickup_shop) {
            ActivitySwitcherAccessory.toShopListForResult(this);
        } else if (i == R.id.invoice_info) {
            ActivitySwitcherThirdPartShop.toInvoice(true, mInvoiceInfo, this);
        }
    }

    private boolean checkShop() {
        if (TextUtils.isEmpty(mShopID)) {
            ToastHelper.showYellowToast(this, "请选择门店");
            return false;
        } else {
            return true;
        }
    }

    //获取提交json
    private String getSubmitJson() {
        ArrayList<ShoppingCartSubmitObject> submitObjects = new ArrayList<>();
        ShoppingCartSubmitObject submitObject = new OrderSubmitObject();
        submitObject.shopID = mShopID;
        submitObject.shopName = mShopName;
        submitObject.itemList = new ArrayList<>();
        for (int j = 0; j < mConfirmOrderItem.itemList.size(); j++) {
            SubmitProduct submitProduct = new SubmitProduct();
            if (mConfirmOrderItem.itemList.get(j).isPackage.equals("1")) {
                submitProduct.isPackage = "1";
                submitProduct.model = "";
                submitProduct.itemID = mConfirmOrderItem.itemList.get(j).packageInfo.productList.get(0).packageID;
                submitProduct.itemNum = mConfirmOrderItem.itemList.get(j).packageInfo.productList.get(0).packageNum;
                submitProduct.idList = mConfirmOrderItem.itemList.get(j).packageInfo.getIdList();
            } else {
                submitProduct.isPackage = "0";
                submitProduct.itemID = mConfirmOrderItem.itemList.get(j).productInfo.productID;
                submitProduct.itemNum = mConfirmOrderItem.itemList.get(j).productInfo.productNum;
                submitProduct.idList = "";
                submitProduct.model = mConfirmOrderItem.itemList.get(j).productInfo.model;
            }
            submitObject.itemList.add(submitProduct);
        }
        submitObjects.add(submitObject);
        return JSONUtils.toJson(submitObjects).replace(" ", "");
    }
}
