package com.hxqc.mall.auto.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.auto.util.MyAllCapTransformationMethod;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.ValidatorTech;
import com.hxqc.mall.core.views.vedit.tech.VMallDivNotNull;

/**
 * Author :胡仲俊
 * Date : 2016-03-07
 * FIXME
 * Todo 通用信息条目
 */
public class CommonEditInfoItemView extends LinearLayout implements View.OnClickListener {

    private TextView mTitleView;//头控件
    private EditTextValidatorView mContentView;//内容控件
    private String titleText;//头文字
    private String contentHint;//内容隐藏文字
    private int titleTextDrawableLeft;//头左侧图片
    private int contentDrawableRight;//内容右边图片
    private boolean contentFocusable;//内容焦点
    private boolean contentFocusableInTouchMode;//内容焦点模式
    private onContentClickListener mOnContentClickListener;//内容点击接口
    private int contentInputType;
    private int contentMaxLength;
    private int titleTextColor;
    private int contentHintColor;
    private int titleTextEms;
    private int contentValidator;
    private String contentDigits;
    private int mMax;

    public CommonEditInfoItemView(Context context) {
        this(context, null);
    }

    public CommonEditInfoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonEditInfoItemView);

        titleText = typedArray.getString(R.styleable.CommonEditInfoItemView_commen_info_item_title_text);
        titleTextColor = typedArray.getColor(R.styleable.CommonEditInfoItemView_commen_info_item_title_textColor, getResources().getColor(R.color.text_color_subheading));
        titleTextDrawableLeft = typedArray.getResourceId(R.styleable.CommonEditInfoItemView_commen_info_item_title_drawableLeft, R.drawable.bg_transparent);
        titleTextEms = typedArray.getInteger(R.styleable.CommonEditInfoItemView_commen_info_item_title_ems, 0);

        contentHint = typedArray.getString(R.styleable.CommonEditInfoItemView_commen_info_item_content_hint);
        contentDrawableRight = typedArray.getResourceId(R.styleable.CommonEditInfoItemView_commen_info_item_content_drawableRight, 0);
        contentFocusable = typedArray.getBoolean(R.styleable.CommonEditInfoItemView_commen_info_item_content_focusable, true);
        contentFocusableInTouchMode = typedArray.getBoolean(R.styleable.CommonEditInfoItemView_commen_info_item_content_focusableInTouchMode, true);
        contentInputType = typedArray.getInteger(R.styleable.CommonEditInfoItemView_commen_info_item_content_inputType, -1);
        contentMaxLength = typedArray.getInteger(R.styleable.CommonEditInfoItemView_commen_info_item_content_maxLength, 0);
        contentHintColor = typedArray.getColor(R.styleable.CommonEditInfoItemView_commen_info_item_content_hintColor, getResources().getColor(R.color.gray_yjf));
        contentValidator = typedArray.getInteger(R.styleable.CommonEditInfoItemView_commen_info_item_content_validator, -1);
        contentDigits = typedArray.getString(R.styleable.CommonEditInfoItemView_commen_info_item_content_digits);
        typedArray.recycle();

        LayoutInflater.from(context).inflate(R.layout.view_commen_info_item, this);
        mTitleView = (TextView) findViewById(R.id.common_info_item_title);
        mContentView = (EditTextValidatorView) findViewById(R.id.common_info_item_content);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);

        initData();

        initEvent();

//        this.addView(view);

    }

    public void setOnContentClickListener(onContentClickListener l) {
        this.mOnContentClickListener = l;
    }

    /*初始化事件*/
    private void initEvent() {
        mContentView.setOnClickListener(this);
    }

    /*初始化数据*/
    private void initData() {

        mTitleView.setText(titleText);
        Drawable drawableLeft = getResources().getDrawable(titleTextDrawableLeft);
        drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
        mTitleView.setCompoundDrawables(drawableLeft, null, null, null);
        mTitleView.setTextColor(titleTextColor);

        if (titleTextEms > 0) {
            mTitleView.setEms(titleTextEms);
        }

        mContentView.setHint(contentHint);
        mContentView.setHintTextColor(contentHintColor);
        if (contentMaxLength != 0) {
            InputFilter[] filters = new InputFilter[]{new InputFilter.LengthFilter(contentMaxLength)};
            mContentView.setFilters(filters);
        }
        mContentView.setFocusable(contentFocusable);
        mContentView.setFocusableInTouchMode(contentFocusableInTouchMode);
        try {
            if (contentDrawableRight != 0) {
                Drawable drawableRight = getResources().getDrawable(contentDrawableRight);
                drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
                mContentView.setCompoundDrawables(null, null, drawableRight, null);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        if (contentInputType == 0) {
            mContentView.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (contentInputType == 1) {
            mContentView.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (contentInputType == 2) {
            mContentView.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        //TODO 校验
        if (contentValidator == 0) {
            mContentView.addValidator(ValidatorTech.ContactMen);
        } else if (contentValidator == 1) {
            mContentView.addValidator(ValidatorTech.PhoneNumber);
        } else if (contentValidator == 2) {
            mContentView.addValidator(new VMallDivNotNull("请选择车辆信息", ""));
        } else if (contentValidator == 3) {
            mContentView.addValidator(new VMallDivNotNull("请选择服务类型", ""));
        } else if (contentValidator == 4) {
            mContentView.addValidator(new VMallDivNotNull("请选择预约时间", ""));
        } else if (contentValidator == 5) {
            mContentView.addValidator(new VMallDivNotNull("请输入发动机号", ""));
        } else if (contentValidator == 6) {
            mContentView.addValidator(new VMallDivNotNull("请输入车架号", ""));
        }

        if(!TextUtils.isEmpty(contentDigits)) {
            mContentView.setKeyListener(new DigitsKeyListener(){
                @Override
                public int getInputType() {
                    return InputType.TYPE_TEXT_VARIATION_PASSWORD;
                }
                @Override
                protected char[] getAcceptedChars() {
                    return contentDigits.toCharArray();
                }
            });
        }

    }

    /*获得内容的控件*/
    public EditTextValidatorView getmContentView() {
        return mContentView;
    }

    /*获取内容的数据*/
    public String getContentText() {

        return mContentView.getText().toString().toUpperCase();
    }

    /*设置内容的数据*/
    public void setContentText(CharSequence text) {
        mContentView.setText(text);
    }

    /*获取内容的数据的长度*/
    public int getContentLength() {
        return mContentView.getText().length();
    }

    /*设置内容的错误显示*/
    public void setContentError(CharSequence text) {
        mContentView.setError(text);
    }

    public void setContentHint(CharSequence hint) {
        mContentView.setHint(hint);
    }

    public void setTitleTextColor(int titleTextColor) {
        mTitleView.setTextColor(titleTextColor);
    }

    public void setContentCursorVisible(boolean visible) {
        mContentView.setCursorVisible(true);
    }

    public boolean getContentFocusable() {
        return mContentView.hasFocus();
    }

    public void setContentFocusable(boolean contentFocusable) {
        mContentView.setFocusable(contentFocusable);
        mContentView.setFocusableInTouchMode(contentFocusable);
        mContentView.requestFocus();
        mContentView.setCursorVisible(contentFocusable);
    }

    public void clearFocus() {
        mContentView.clearFocus();
    }

    public void setContentMaxLength(int max) {
        InputFilter[] filters = null;
        if (max != 0) {
            filters = new InputFilter[]{new InputFilter.LengthFilter(max)};
        } else {
            filters = new InputFilter[]{};
        }
        mContentView.setFilters(filters);
        mMax = max;
    }

    public int getContentMaxLength() {
        return mMax;
    }

    @Override
    public void onClick(View v) {
        if (mOnContentClickListener != null)
            mOnContentClickListener.onContentClick(this);
    }

    public interface onContentClickListener {
        void onContentClick(View v);
    }

    /**
     * 设置大小写
     */
    public void setTransformationMethod() {
        mContentView.setTransformationMethod(new MyAllCapTransformationMethod());
    }

}
