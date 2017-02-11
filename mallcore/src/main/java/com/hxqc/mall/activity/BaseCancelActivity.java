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

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ToastHelper;

/**
 * 取消订单基类
 */
public abstract class BaseCancelActivity extends BackActivity {
    InputMethodManager inputMethodManager;
    ScrollView mScrollView;
    EditText mMemo;
    Button mCommit;
    int count = 0;
    String memo = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_cancel);
        initView();
    }





    private void initView() {
        mScrollView = (ScrollView) findViewById(R.id.sv_refund);
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
                if (count > 200) {
                    ToastHelper.showRedToast(BaseCancelActivity.this, "超出字数限制");
                } else {
                    if(TextUtils.isEmpty(memo)) {
                        ToastHelper.showRedToast(BaseCancelActivity.this, "请输入取消原因");
                        return;
                    }
                    toCancel(memo);
                }
            }
        });
    }

    /**
     * 接口调用
     * @param cancelText 取消原因
     */
    protected abstract void toCancel(String cancelText);

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
