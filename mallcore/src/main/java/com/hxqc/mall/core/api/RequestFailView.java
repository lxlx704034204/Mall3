package com.hxqc.mall.core.api;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;

/**
 * Author: HuJunJie
 * Date: 2015-05-22
 * FIXME
 * Todo 请求失败view
 */
public class RequestFailView extends RelativeLayout {
    TextView descriptionView;
    String failDescription = "网络连接失败";
    String emptyDescription = "";
    boolean emptyShowButton;
    boolean failShowButton;
    Button mTButton;
    ImageView mImageView;//图标
    int mEmptySrc = R.drawable.no_date;//空白图片
    int mFailSrc = R.drawable.no_line;// 错误图片

    String failButtonString = "去设置";//错误按钮
    String emptyButtonString = "去逛逛";//数据为空按钮

    OnClickListener emptyClickListener;
    OnClickListener failClickListener;
    private LinearLayout mAddButton;

    public enum RequestViewType {
        empty, fail
    }

    public RequestFailView(Context context) {
        super(context);
        init();
    }

    public RequestFailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RequestFailView);
        String tfailDescription = typedArray.getString(R.styleable.RequestFailView_failDescription);
        if (!TextUtils.isEmpty(tfailDescription))
            failDescription = tfailDescription;
        emptyDescription = typedArray.getString(R.styleable.RequestFailView_emptyDescription);
        mEmptySrc = typedArray.getResourceId(R.styleable.RequestFailView_emptySrc, R.drawable.no_date);
        mFailSrc = typedArray.getResourceId(R.styleable.RequestFailView_failSrc, R.drawable.no_line);
        failButtonString = typedArray.getString(R.styleable.RequestFailView_failButtonText);
        emptyButtonString = typedArray.getString(R.styleable.RequestFailView_emptyButtonText);
        typedArray.recycle();

    }

    public RequestFailView(Context context, String description) {
        super(context);
        init();
        descriptionView.setText(description);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_request_fail, this);
        descriptionView = (TextView) findViewById(R.id.fail_notice);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mTButton = (Button) findViewById(R.id.fail_to_see);
        mImageView = (ImageView) findViewById(R.id.fail_image);
        mAddButton = (LinearLayout) findViewById(R.id.fail_to_see_auto);

        findViewById(R.id.main).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    /**
     * 设置数据为空 按钮点击
     *
     * @param value 按钮文字
     */
    public RequestFailView setEmptyButtonClick(String value, OnClickListener onClickListener) {
        emptyShowButton = true;
        if (!TextUtils.isEmpty(value)) {
            emptyButtonString = value;
        }
        emptyClickListener = onClickListener;
        return this;
    }

    /**
     * 设置错误 按钮点击
     *
     * @param value 按钮文字
     */
    public RequestFailView setFailButtonClick(String value, OnClickListener onClickListener) {
        failShowButton = true;
        if (!TextUtils.isEmpty(value)) {
            failButtonString = value;
        }
        failClickListener = onClickListener;
        return this;
    }

    /**
     * 设置数据为空时内容描述
     *
     * @param emptyDescription
     */
    public void setEmptyDescription(String emptyDescription) {
        this.emptyDescription = emptyDescription;
    }

    /**
     * 设置数据为空时内容描述
     *
     * @param emptyDescription
     */
    public void setEmptyDescription(String emptyDescription, int imageSrc) {
        this.emptyDescription = emptyDescription;
        if (imageSrc != 0) {
            this.mEmptySrc = imageSrc;
        }
    }

    /**
     * @param type
     * @return
     */
    public RequestFailView setRequestViewType(RequestViewType type) {
        String buttonString = "";
        OnClickListener onClickListener = null;
        boolean isShowButton = false;
        String descriptionString = null;
        int imageSrc = 0;
        switch (type) {
            case empty:
                buttonString = emptyButtonString;
                onClickListener = emptyClickListener;
                isShowButton = emptyShowButton;
                descriptionString = emptyDescription;
                imageSrc = mEmptySrc;
                break;
            case fail:
                buttonString = failButtonString;
                onClickListener = failClickListener;
                isShowButton = failShowButton;
                descriptionString = failDescription;
                imageSrc = mFailSrc;
                break;
        }
        mTButton = (Button) findViewById(R.id.fail_to_see);
        if (isShowButton) {
            mTButton.setText(buttonString);
            mTButton.setVisibility(View.VISIBLE);
            mTButton.setOnClickListener(onClickListener);
        } else {
            mTButton.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(descriptionString)) {
            descriptionView.setText(descriptionString);
        }
        if (imageSrc != 0) {
            mImageView.setImageResource(imageSrc);
        }
        return this;
    }

    /**
     * @param type
     * @return
     */
    public RequestFailView setRequestType(RequestViewType type) {
        String buttonString = "";
        OnClickListener onClickListener = null;
        boolean isShowButton = false;
        String descriptionString = null;
        switch (type) {
            case empty:
                buttonString = emptyButtonString;
                onClickListener = emptyClickListener;
                isShowButton = emptyShowButton;
                descriptionString = emptyDescription;
                break;
            case fail:
                buttonString = failButtonString;
                onClickListener = failClickListener;
                isShowButton = failShowButton;
                descriptionString = failDescription;
                break;
        }
        mTButton = (Button) findViewById(R.id.fail_to_see);
        if (isShowButton) {
            mTButton.setText(buttonString);
            mTButton.setVisibility(View.VISIBLE);
            mTButton.setOnClickListener(onClickListener);
        } else {
            mTButton.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(descriptionString)) {
            descriptionView.setText(descriptionString);
        }
        return this;
    }

    /**
     * 显示数据空页面
     *
     * @param emptyDescription
     * @param value
     * @param imageSrc
     * @param onClickListener
     */
    public void showEmptyPage(String emptyDescription, int imageSrc, boolean isHave, String value, OnClickListener onClickListener) {
        setEmptyDescription(emptyDescription, imageSrc);
        if (onClickListener != null) {
            setEmptyButtonClick(value, onClickListener);
        }
        setRequestViewType(RequestViewType.empty);
        setVisibility(VISIBLE);
    }

    /**
     * 显示请求失败页面
     *
     * @param value
     * @param onClickListener
     */
    public void showFailedPage(String value, OnClickListener onClickListener) {
        setFailButtonClick(value, onClickListener);
        setRequestViewType(RequestViewType.fail);
        setVisibility(VISIBLE);
    }

    /**
     * 显示无按钮错误页面
     */
    public void showFailPageWithoutButton(String failDescription){
        setEmptyDescription(failDescription);
        setRequestViewType(RequestViewType.empty);
        setVisibility(VISIBLE);
        if (mTButton!=null){
            mTButton.setVisibility(GONE);
        }
    }

    /**
     * @param activity
     * @param emptyDescription
     */
    public void notShop(final Activity activity, String emptyDescription) {
        setEmptyDescription(emptyDescription);
        setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        setRequestViewType(RequestFailView.RequestViewType.empty);
        setVisibility(View.VISIBLE);
    }

    /**
     * @param emptyDescription
     */
    public void notShop(String emptyDescription) {
        setEmptyDescription(emptyDescription);
        setRequestViewType(RequestFailView.RequestViewType.empty);
        setVisibility(View.VISIBLE);
    }

    /**
     * @return
     */
    public LinearLayout setAddButtonClick() {
        mTButton.setVisibility(GONE);
        mAddButton.setVisibility(VISIBLE);
        return mAddButton;
    }

}
