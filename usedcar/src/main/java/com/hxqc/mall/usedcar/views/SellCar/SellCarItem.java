package com.hxqc.mall.usedcar.views.SellCar;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.tech.VMallDivNotNull;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.utils.OtherUtil;

/**
 * 说明:平台发布
 *
 * @author: 吕飞
 * @since: 2016-05-04
 * Copyright:恒信汽车电子商务有限公司
 */
public class SellCarItem extends RelativeLayout {
    String mItemName;
    TextView mItemNameView;
    EditTextValidatorView mChooseResultView;
    String mStar = "";
    TextView mStarView;
    String mItemHint = "";
    String mValidatorString = "";
    SellCarItemListener mSellCarItemListener;
    int mItemMaxLines;

    public SellCarItem(Context context) {
        super(context);
        initView();
    }

    public SellCarItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SellCarItem);
        mItemName = typedArray.getString(R.styleable.SellCarItem_itemName);
        mStar = typedArray.getString(R.styleable.SellCarItem_itemStar);
        mItemHint = typedArray.getString(R.styleable.SellCarItem_itemHint);
        mValidatorString = typedArray.getString(R.styleable.SellCarItem_validatorString);
        mItemMaxLines = typedArray.getInt(R.styleable.SellCarItem_itemMaxLines, 1);
        typedArray.recycle();
        initView();
        mItemNameView.setText(mItemName);
        mStarView.setText(mStar);
        mChooseResultView.setHint(mItemHint);
        mChooseResultView.setSingleLine(mItemMaxLines==1);
        mChooseResultView.setMaxLines(mItemMaxLines);
        if (!TextUtils.isEmpty(mValidatorString)) {
            mChooseResultView.addValidator(new VMallDivNotNull(mValidatorString, ""));
        }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_sell_item, this);
        mItemNameView = (TextView) findViewById(R.id.item_name);
        mChooseResultView = (EditTextValidatorView) findViewById(R.id.choose_result);
        mStarView = (TextView) findViewById(R.id.star);
        mChooseResultView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                OtherUtil.closeSoftKeyBoard(getContext(),SellCarItem.this);
                mSellCarItemListener.sellCarItemClick(SellCarItem.this);
            }
        });
    }

    public EditTextValidatorView getChooseResultView() {
        return mChooseResultView;
    }

    public void setSellCarItemListener(SellCarItemListener sellCarItemListener) {
        mSellCarItemListener = sellCarItemListener;
    }

    public String getResult() {
        return mChooseResultView.getText().toString();
    }

    public void setResult(String result) {
        mChooseResultView.setText(result);
    }

    public interface SellCarItemListener {
        void sellCarItemClick(View view);
    }
}
