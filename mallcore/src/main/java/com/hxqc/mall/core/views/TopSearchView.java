package com.hxqc.mall.core.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ToastHelper;


/**
 * Function:第三方店铺搜索框
 *
 * @author 袁秉勇
 * @since 2015年12月23日
 */
public class TopSearchView extends LinearLayout implements View.OnClickListener, TextWatcher {
    Context mContext;
    TextView mChangeSearchTypeView;
    EditText mSearchContentView;
    LinearLayout mSearchButtonView;
    ImageView mClearSearchTipView;

    public TopSearchView(Context context) {
        this(context, null);
    }

    public TopSearchView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.filter_top_search_layout, this);

        mChangeSearchTypeView = (TextView) findViewById(R.id.change_search_type);

        mSearchContentView = (EditText) findViewById(R.id.search_tip);

        mClearSearchTipView = (ImageView) findViewById(R.id.clear_search_tip);

        mSearchButtonView = (LinearLayout) findViewById(R.id.search);

        mChangeSearchTypeView.setOnClickListener(this);
        mClearSearchTipView.setOnClickListener(this);
        mSearchButtonView.setOnClickListener(this);
        mSearchContentView.addTextChangedListener(this);

        mSearchContentView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(mSearchContentView.getText())) {
                        ToastHelper.showRedToast(context, "请输入搜索条件");
                        return false;
                    }
                    mSearchButtonView.performClick();
                    return true;
                }
                return false;
            }
        });

        mSearchContentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mSearchContentView.getText().length() > 0) {
                        mClearSearchTipView.setVisibility(View.VISIBLE);
                    }
                } else {
                    mClearSearchTipView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.clear_search_tip) {
            mSearchContentView.setText("");
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(mSearchContentView.getText()) && mSearchContentView.hasFocus()) {
            mClearSearchTipView.setVisibility(View.VISIBLE);
        } else {
            mClearSearchTipView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
