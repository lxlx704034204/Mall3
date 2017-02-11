package com.hxqc.mall.usedcar.views.SellCar;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.VMETValidator;
import com.hxqc.mall.core.views.vedit.ValidatorTech;
import com.hxqc.mall.core.views.vedit.tech.VMallDivNotNull;
import com.hxqc.mall.core.views.vedit.tech.VMallRange;
import com.hxqc.mall.usedcar.R;

/**
 * 说明:平台发布
 *
 * @author: 吕飞
 * @since: 2016-05-04
 * Copyright:恒信汽车电子商务有限公司
 */
public class SellCarEditText extends RelativeLayout {
    public static final int DECIMAL = 0;
    public static final int PHONE = 1;
    public static final int TEXT = 2;
    public static final int CONTACT_NAME = 1;
    public static final int CONTACT_PHONE = 2;
    public static final int RANGE = 3;
    TextView mStarView;
    TextView mEditNameView;
    EditTextValidatorView mInputView;
    String mStar = "";//星号
    String mEditName;
    int mInputType;
    int mEditMaxLength;
    String mEditHint = "";
    TextView mEditUnitView;
    String mEditUnit = "";
    int mValidatorEdit;
    String mValidatorEditString;

    public SellCarEditText(Context context) {
        super(context);
        initView();
    }

    public SellCarEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SellCarEditText);
        mStar = typedArray.getString(R.styleable.SellCarEditText_editStar);
        mEditName = typedArray.getString(R.styleable.SellCarEditText_editName);
        mEditHint = typedArray.getString(R.styleable.SellCarEditText_editHint);
        mEditUnit = typedArray.getString(R.styleable.SellCarEditText_editUnit);
        mInputType = typedArray.getInt(R.styleable.SellCarEditText_editInputType, 0);
        mEditMaxLength = typedArray.getInt(R.styleable.SellCarEditText_editMaxLength, 12);
        mValidatorEdit = typedArray.getInt(R.styleable.SellCarEditText_validatorEdit, 0);
        mValidatorEditString = typedArray.getString(R.styleable.SellCarEditText_validatorEditString);
        typedArray.recycle();
        initView();
        mInputView.setHint(mEditHint);
        mStarView.setText(mStar);
        mEditUnitView.setText(mEditUnit);
        mEditNameView.setText(mEditName);
        mInputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mEditMaxLength)});
        switch (mInputType) {
            case PHONE:
                mInputView.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case TEXT:
                mInputView.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case DECIMAL:
                com.hxqc.mall.usedcar.utils.OtherUtil.control2Decimal(mInputView);
                break;
        }
        switch (mValidatorEdit) {
            case CONTACT_NAME:
                mInputView.addValidator(ValidatorTech.RealName);
                break;
            case CONTACT_PHONE:
                mInputView.addValidator(ValidatorTech.PhoneNumber);
                break;
            case RANGE:
                mInputView.addValidator(new VMallRange());
                break;
        }
        if (!TextUtils.isEmpty(mValidatorEditString)) {
            mInputView.addValidator(new VMallDivNotNull(mValidatorEditString, ""));
        }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_sell_edit_text, this);
        mStarView = (TextView) findViewById(R.id.star);
        mEditNameView = (TextView) findViewById(R.id.edit_name);
        mInputView = (EditTextValidatorView) findViewById(R.id.input);
        mEditUnitView = (TextView) findViewById(R.id.edit_unit);
    }

    public EditTextValidatorView getInputView() {
        return mInputView;
    }

    public void addValidator(ValidatorTech validatorTech) {
        mInputView.addValidator(validatorTech);
    }

    public void addValidator(VMETValidator validator) {
        mInputView.addValidator(validator);
    }

    public void setText(String text) {
        mInputView.setText(text);
    }
}
