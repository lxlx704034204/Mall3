package com.hxqc.mall.activity.order;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.AppBackActivity;
import com.hxqc.mall.core.adapter.RefundReasonChooseAdapter;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.model.RefundReason;
import com.hxqc.mall.core.model.RefundRequest;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.SpinnerPopWindow;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 申请退款
 */
public class  ApplicationRefundActivity extends AppBackActivity {

    InputMethodManager inputMethodManager;

    SpinnerPopWindow mChooseReason;
    TextView mReason;
    EditText mMemo;
    Button mCommit;
    RefundReason currentReason;
    String mOrderId;
    int count = 0;
    String memo = "";
    ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_refund);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mOrderId = getIntent().getStringExtra(ActivitySwitcher.ORDER_ID);
        initView();
        getRefundReason(mOrderId);
        mScrollView.setVerticalScrollBarEnabled(false);
        mChooseReason.setOnItemClickList(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentReason = (RefundReason) parent.getAdapter().getItem(position);
                mReason.setText(currentReason.reasonContent);
            }
        });

        mMemo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                memo = mMemo.getText().toString().trim();
                count = memo.length();
            }
        });

        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 200) {
                    ToastHelper.showRedToast(ApplicationRefundActivity.this, "超出字数限制");
                } else {
                    if (currentReason == null) {
                        ToastHelper.showRedToast(ApplicationRefundActivity.this, "请选择退款原因");
                    } else {
                        DebugLog.e("egg", memo + "--" + currentReason.toString());
                        commitRefund(mOrderId);
                    }
                }
            }
        });

    }

    private void initView() {
        mScrollView = (ScrollView) findViewById(R.id.sv_refund);
        mChooseReason = (SpinnerPopWindow) findViewById(R.id.spw_reason);
        mReason = (TextView) findViewById(R.id.tv_reason);
        mMemo = (EditText) findViewById(R.id.met_write_memo);
        mCommit = (Button) findViewById(R.id.btn_commit);
    }

    //拿到退款原因
    private void getRefundReason(String id) {
        mApiClient.refundReason(id, new BaseMallJsonHttpResponseHandler(ApplicationRefundActivity.this) {
            @Override
            public void onSuccess(String response) {
                DebugLog.i("egg", response);
                ArrayList<RefundReason> reasons = JSONUtils.fromJson(response, new TypeToken<ArrayList<RefundReason>>() {
                });
                mChooseReason.setAdapter(new RefundReasonChooseAdapter(reasons, ApplicationRefundActivity.this, mReason));

            }
        });
    }

    //提交申请
    private void commitRefund(String id1) {
        RefundRequest request = new RefundRequest();
        request.reasonID = currentReason.reasonID;
        request.memo = memo;
        request.orderID = id1;

        mApiClient.applicationRefund(request, new DialogResponseHandler(ApplicationRefundActivity.this, getResources().getString(R.string.me_submitting)) {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(getApplicationContext(), "申请成功");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mSharedPreferencesHelper.setOrderChange(true);
                        ActivitySwitchBase.toOrderDetail(ApplicationRefundActivity.this, mOrderId);
                        ApplicationRefundActivity.this.finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }, 2300);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

}
