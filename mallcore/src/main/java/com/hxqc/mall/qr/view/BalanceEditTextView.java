package com.hxqc.mall.qr.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.util.DebugLog;

/**
 * Author:  wh
 * Date:  2016/11/14
 * FIXME
 * Todo
 */

public class BalanceEditTextView extends RelativeLayout {


    private TextView mBalanceFlagView;
    private EditText mETView;
    private String hintText = "";
    private String maxBalance = "0";

    public BalanceEditTextView(Context context) {
        super(context);
        initViews();
    }

    public BalanceEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_balance_edit_view, this);
        mBalanceFlagView = (TextView) findViewById(R.id.flag_tag);
        mETView = (EditText) findViewById(R.id.edit_balance);
        //屏蔽粘贴------------------------------------------------
        mETView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mETView.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        //屏蔽粘贴------------------------------------------------
        mETView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mETView.setHint("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //判断是不是小数  然后取小数点后两位
                save2Point(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                if (TextUtils.isEmpty(s1)) {
                    setHint(hintText);
                } else {
                    try {
                        double aDouble = Double.parseDouble(s1);
                        DebugLog.i("balance", maxBalance + "");
                        //判断是否 大于可支付数额
                        if (aDouble > Double.parseDouble(maxBalance)) {
                            mETView.setText(maxBalance);
                            mETView.setSelection(mETView.getText().toString().length());
                        }
                    } catch (Exception e) {
                        mETView.setText(maxBalance);
                        mETView.setSelection(mETView.getText().toString().length());
                    }
                }
            }
        });
    }

    private void save2Point(CharSequence s) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + 3);
                mETView.setText(s);
                mETView.setSelection(s.length());
            }
        }

        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            mETView.setText(s);
            mETView.setSelection(2);
        }

        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                mETView.setText(s.subSequence(0, 1));
                mETView.setSelection(1);
                return;
            }
        }
    }

    public void setMaxBalance(String maxBalance) {
        this.maxBalance = maxBalance;
    }

    public void setHint(String balance) {
        if ("0".equals(maxBalance)) {
            setMaxBalance(balance);
        }

        this.hintText = balance;
        if (mETView != null) {
            mETView.setHint(hintText);
        }
    }

    public String getPayMoney() {
        try {
            String eText = mETView.getText().toString().trim();
            if (TextUtils.isEmpty(eText)) {
                return maxBalance;
            } else {
                return eText;
            }
        } catch (Exception e) {
            return maxBalance;
        }
    }

}
