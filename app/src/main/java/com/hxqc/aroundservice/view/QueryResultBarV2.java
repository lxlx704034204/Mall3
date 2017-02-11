package com.hxqc.aroundservice.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.auto.view.CommonTwoTextView;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 12
 * FIXME
 * Todo 底部显示条目控件
 */
public class QueryResultBarV2 extends LinearLayout implements View.OnClickListener {

    private CommonTwoTextView mForecastView;
    private CommonTwoTextView mCountView;
    private CommonTwoTextView mFineView;
    private CommonTwoTextView mServiceChargeView;
    private Button mProcessingView;
    private OnClickProcessingListener mOnClickProcessingListener;
    private LinearLayout mInfoView;
    private TextView mInfoBtnView;
    private LinearLayout mV1View;
    private LinearLayout mV2View;
    private LinearLayout mInfoBarView;
    private TextView mIllegalView;
    private TextView mMoneyView;
    private TextView mPointView;
    private CommonTwoTextView mIllegalMoneyView;
    private Button mPayBtnView;
    private OnClickPayListener imOnClickPayListener;


    public interface OnClickProcessingListener {
        void clickProcessingListener(View v);
    }

    public interface OnClickPayListener {
        void clickPayListener(View v);
    }

    public void setOnClickProcessingListener(OnClickProcessingListener l) {
        this.mOnClickProcessingListener = l;
    }

    public void setOnClickPayListener(OnClickPayListener l) {
        this.imOnClickPayListener = l;
    }

    public QueryResultBarV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_query_result_v2, this);

        mV1View = (LinearLayout) findViewById(R.id.query_result_v1);
        mIllegalView = (TextView) findViewById(R.id.query_result_illegal);
        mMoneyView = (TextView) findViewById(R.id.query_result_money);
        mPointView = (TextView) findViewById(R.id.query_result_point);

        mV2View = (LinearLayout) findViewById(R.id.query_result_v2);
        mForecastView = (CommonTwoTextView) findViewById(R.id.query_result_v2_forecast);
        mInfoView = (LinearLayout) findViewById(R.id.query_result_v2_info);
        mInfoBtnView = (TextView) findViewById(R.id.query_result_v2_info_btn);
        mProcessingView = (Button) findViewById(R.id.query_result_v2_processing);
        mCountView = (CommonTwoTextView) findViewById(R.id.query_result_v2_count);
        mFineView = (CommonTwoTextView) findViewById(R.id.query_result_v2_fine);
        mServiceChargeView = (CommonTwoTextView) findViewById(R.id.query_result_v2_service_charge);
        mInfoBtnView.setOnClickListener(showInfoListener);
        mProcessingView.setOnClickListener(this);

        mInfoBarView = (LinearLayout) findViewById(R.id.query_result_illegal_info_bar);
        mIllegalMoneyView = (CommonTwoTextView) findViewById(R.id.query_result_illegal_info_money);
        mPayBtnView = (Button) findViewById(R.id.query_result_illegal_info_btn);

        mPayBtnView.setOnClickListener(clickPayListener);

        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setClickable(true);
    }

    /**
     * @param v1Visibility
     * @param v2Visibility
     * @param infoVisibility
     */
    public void setBarVisibility(int v1Visibility, int v2Visibility, int infoVisibility) {
        mV1View.setVisibility(v1Visibility);
        mV2View.setVisibility(v2Visibility);
        mInfoBarView.setVisibility(infoVisibility);
    }

    /**
     * @param illegal
     * @param money
     * @param point
     */
    public void setV1Text(CharSequence illegal, CharSequence money, CharSequence point) {
        mIllegalView.setText(illegal);
        mMoneyView.setText(money);
        mPointView.setText(point);
    }

    /**
     * @param visibility
     */
    public void setInfoBtnViewState(int visibility) {
        mInfoBtnView.setVisibility(visibility);
    }

    /**
     * @param visibility
     */
    public void setInfoViewState(int visibility, boolean isShow) {
        mInfoView.setVisibility(visibility);
        if (isShow) {
//            mInfoBtnView.setBackgroundResource(R.drawable.ic_cbb_arrow_down);
            Drawable drawable = getResources().getDrawable(R.drawable.ic_cbb_arrow_down);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mInfoBtnView.setCompoundDrawables(null, null, drawable, null);
        } else {
//            mInfoBtnView.setBackgroundResource(R.drawable.ic_cbb_arrow_up);
            Drawable drawable = getResources().getDrawable(R.drawable.ic_cbb_arrow_up);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mInfoBtnView.setCompoundDrawables(null, null, drawable, null);
        }

    }

    /**
     * @param visibility
     */
    public void setProcessingViewState(int visibility) {
        mProcessingView.setVisibility(visibility);
    }

    /**
     * @param enabled
     */
    public void setProcessingViewEnabled(boolean enabled) {
        mProcessingView.setEnabled(enabled);
    }

    /**
     * @param visibility
     */
    public void setPayBtnState(int visibility) {
        mPayBtnView.setVisibility(visibility);
    }

    /**
     * @param l
     */
    public void setInfoBtnViewOnClickListener(OnClickListener l) {
        mInfoBtnView.setOnClickListener(l);
    }

    /**
     * @param text
     */
    public void setForecastViewText(CharSequence text) {
        mForecastView.setTwoText("￥" + text);
    }

    /**
     * @param fine
     * @param serviceCharge
     * @param count
     * @param amount
     */
    public void setV2Text(CharSequence fine, CharSequence serviceCharge, CharSequence count, CharSequence amount) {
        mForecastView.setTwoText(amount);
        mCountView.setTwoText(count);
        mFineView.setTwoText(fine);
        mServiceChargeView.setTwoText(serviceCharge);
    }

    private OnClickShowInfoListener imOnClickShowInfoListener;

    public interface OnClickShowInfoListener {
        void onClickShowInfo(View v);
    }

    public void setOnClickShowInfoListener(OnClickShowInfoListener l) {
        this.imOnClickShowInfoListener = l;
    }

    private boolean isShow = false;
    private OnClickListener showInfoListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (imOnClickShowInfoListener != null) {
                imOnClickShowInfoListener.onClickShowInfo(v);
            /*    if (isShow) {
                    mInfoView.setVisibility(GONE);
                    isShow = false;
                } else {
                    mInfoView.setVisibility(VISIBLE);
                    isShow = true;
                }*/
            }
        }
    };

    /**
     * @param forecast
     * @param processing
     * @param processingVisibility
     */
    public void illegalOrderInfo(CharSequence forecast, CharSequence processing, int processingVisibility) {
        mInfoBtnView.setVisibility(GONE);
        mForecastView.setTwoText(forecast);
        mForecastView.setTwoTextColor(getResources().getColor(R.color.text_color_red));
        mProcessingView.setText(processing);
        mProcessingView.setVisibility(processingVisibility);
    }

    /**
     * @param visibility
     * @param content
     * @param title
     * @param ems
     * @param money
     */
    public void setIllegalText(int visibility, CharSequence content, CharSequence title, int ems, CharSequence money) {
        mPayBtnView.setVisibility(visibility);
        mPayBtnView.setText(content);
        mIllegalMoneyView.setFirstText(title);
        mIllegalMoneyView.setFirstTextColor(getResources().getColor(R.color.text_color_red));
        mIllegalMoneyView.setFirstTextEms(ems);
        mIllegalMoneyView.setTwoText(money);
    }

    /**
     * @param money
     */
    public void setIllegalMoney(CharSequence money) {
        mIllegalMoneyView.setTwoText(money);
    }

    private View.OnClickListener clickPayListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (imOnClickPayListener != null) {
                imOnClickPayListener.clickPayListener(v);
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (mOnClickProcessingListener != null) {
            mOnClickProcessingListener.clickProcessingListener(v);
        }
    }
}
