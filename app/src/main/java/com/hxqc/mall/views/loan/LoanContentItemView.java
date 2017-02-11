package com.hxqc.mall.views.loan;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.loan.LoanKeyValueModel;

import hxqc.mall.R;

/**
 * Author: wanghao
 * Date: 2015-10-16
 * FIXME  金融公司 包括条目内容
 * Todo
 */
public class LoanContentItemView extends RelativeLayout {

    private TextView keyView;
    private TextView valueView;
    private View vUnderline;


    public LoanContentItemView(Context context) {
        super(context);
        initView();
    }

    public LoanContentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_loan_content, this);
        keyView = (TextView) findViewById(R.id.tv_loan_content_key);
        valueView = (TextView) findViewById(R.id.tv_loan_content_value);
        vUnderline = findViewById(R.id.v1111);
    }

    public void setvUnderlineVisible(int visible) {
        this.vUnderline.setVisibility(visible);
    }

    public void setModel(LoanKeyValueModel model) {
        if (model != null) {
            keyView.setText(model.getTitle());
            valueView.setText(model.getValue());
        }
    }

}
