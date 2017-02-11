package com.hxqc.mall.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;


import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ToastHelper;

/**
 * liaoguilong
 * 申请退款基类
 * 2016-03-30 09:49:16
 */
public abstract class BaseRefundActivity extends BackActivity {
    InputMethodManager inputMethodManager;
    ScrollView mScrollView;
    EditText mMemo;
    Button mCommit;
    int count = 0;
    String memo = "";
    TextView  mReasonLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_refund);
        initView();
    }

    /**
     * 文本输入提示
     * @param hint
     */
    public void setTextHint(String hint){
        mMemo.setHint(hint);
    }

    private void initView() {
        mScrollView = (ScrollView) findViewById(R.id.sv_refund);
        mReasonLabel= (TextView) findViewById(R.id.tv_reason_label);
        mMemo = (EditText) findViewById(R.id.met_write_memo);
        mCommit = (Button) findViewById(R.id.btn_commit);
        mScrollView.setVerticalScrollBarEnabled(false);
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
                if (count > 100) {
                    ToastHelper.showRedToast(BaseRefundActivity.this, "超出字数限制");
                } else {
                    if (TextUtils.isEmpty(memo)) {
                        ToastHelper.showRedToast(BaseRefundActivity.this, "请输入退款说明");
                        return;
                    }
                    toRefund(memo);
                }
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

    /**
     * 退款原因
     * @param refundText
     */
    protected abstract void toRefund(String refundText);
}
