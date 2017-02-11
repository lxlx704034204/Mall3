package com.hxqc.mall.thirdshop.accessory4s.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.RecyclerViewHeader;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.adapter.ConfirmOrder4SAdapter;
import com.hxqc.mall.thirdshop.accessory4s.model.ConfirmOrderItem4S;
import com.hxqc.mall.thirdshop.accessory4s.model.ShoppingCartSubmitObject4S;
import com.hxqc.mall.thirdshop.accessory4s.model.SubmitOrderInfo4S;
import com.hxqc.mall.thirdshop.accessory4s.model.SubmitProduct4S;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 说明:确认订单
 *
 * @author: 吕飞
 * @since: 2016-02-24
 * Copyright:恒信汽车电子商务有限公司
 */
public class ConfirmOrder4SActivity extends Accessory4SBackActivity implements View.OnClickListener {
    ArrayList<ConfirmOrderItem4S> mConfirmOrderItems;//订单数据
    EditText mContactNameView;//联系人名字
    RecyclerView.LayoutManager mLayoutManager;
    EditText mContactPhoneView;//联系人电话
    RecyclerView mListView;
    TextView mAmountView;
    Button mSubmitView;//提交按钮
    SubmitOrderInfo4S mSubmitOrderInfo4S;//需要提交的数据
    public ArrayList<String> mRemarks = new ArrayList<>();
    String mAmount;
    RecyclerViewHeader mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConfirmOrderItems = JSONUtils.fromJson(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ActivitySwitcherAccessory4S.CONFIRM_ORDER_ITEMS), new TypeToken<ArrayList<ConfirmOrderItem4S>>() {
        });
        setContentView(R.layout.activity_accessory_confirm_order_4s);
        initView();
        fillData();
    }

    //填充数据
    private void fillData() {
        mLayoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mLayoutManager);
        mListView.setHasFixedSize(true);
        mHeaderView.attachTo(mListView);
        mListView.setAdapter(new ConfirmOrder4SAdapter(this, mConfirmOrderItems));
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
        mAmountView.setText("应付总金额：" + OtherUtil.amountFormat(getAmount(), true));
    }

    private float getAmount() {
        float amount = 0;
        for (int i = 0; i < mConfirmOrderItems.size(); i++) {
            amount = amount + Float.parseFloat(mConfirmOrderItems.get(i).payAmount);
            mRemarks.add("");
        }
        mAmount = amount + "";
        return amount;
    }

    private void initView() {
        mHeaderView = RecyclerViewHeader.fromXml(this, R.layout.layout_confirm_order_header);
        mContactNameView = (EditText) mHeaderView.findViewById(R.id.contact_name);
        mContactPhoneView = (EditText) mHeaderView.findViewById(R.id.contact_phone);
        mListView = (RecyclerView) findViewById(R.id.order_list);
        mAmountView = (TextView) findViewById(R.id.amount);
        mSubmitView = (Button) findViewById(R.id.submit);
        mSubmitView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.submit) {
            if (FormatCheck.checkNameForAcc(mContactNameView.getText().toString(), this) && FormatCheck.checkPhoneNumber(mContactPhoneView.getText().toString().trim(), this) == FormatCheck.CHECK_SUCCESS) {
                mAccessoryApiClient.submitOrder(mAmount, getSubmitJson(), mContactNameView.getText().toString(), mContactPhoneView.getText().toString(), new LoadingAnimResponseHandler(this) {
                    @Override
                    public void onSuccess(String response) {
                        new BaseSharedPreferencesHelper(ConfirmOrder4SActivity.this).setOrderChange(true);
                        mSubmitOrderInfo4S = JSONUtils.fromJson(response, SubmitOrderInfo4S.class);
                        if (mSubmitOrderInfo4S != null) {
                            ShoppingCart4SActivity.sNeedRefresh = true;
                            ActivitySwitcherAccessory4S.toPay(ConfirmOrder4SActivity.this, mSubmitOrderInfo4S);
                            finish();
                        }
                    }
                });
            }
        }
    }

    //获取提交json
    private String getSubmitJson() {
        ArrayList<ShoppingCartSubmitObject4S> submitObjects = new ArrayList<>();
        for (int i = 0; i < mConfirmOrderItems.size(); i++) {
            ShoppingCartSubmitObject4S submitObject = new ShoppingCartSubmitObject4S();
            submitObject.shopID = mConfirmOrderItems.get(i).shopInfo.shopID;
            submitObject.remark = mRemarks.get(i);
            submitObject.itemList = new ArrayList<>();
            for (int j = 0; j < mConfirmOrderItems.get(i).productList.size(); j++) {
                SubmitProduct4S submitProduct = new SubmitProduct4S();
                if (mConfirmOrderItems.get(i).productList.get(j).isPackage.equals("1")) {
                    submitProduct.isPackage = "1";
                    submitProduct.itemID = mConfirmOrderItems.get(i).productList.get(j).packageInfo.productList.get(0).packageID;
                    submitProduct.itemNum = mConfirmOrderItems.get(i).productList.get(j).packageInfo.productList.get(0).packageNum;
                    submitProduct.idList = mConfirmOrderItems.get(i).productList.get(j).packageInfo.getIdList();
                } else {
                    submitProduct.isPackage = "0";
                    submitProduct.itemID = mConfirmOrderItems.get(i).productList.get(j).productInfo.productID;
                    submitProduct.itemNum = mConfirmOrderItems.get(i).productList.get(j).productInfo.productNum;
                    submitProduct.idList = "";
                }
                submitObject.itemList.add(submitProduct);
            }
            submitObjects.add(submitObject);
        }
        return JSONUtils.toJson(submitObjects).replace(" ", "");
    }
}
