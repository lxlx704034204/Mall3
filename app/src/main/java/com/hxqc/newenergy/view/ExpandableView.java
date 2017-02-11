package com.hxqc.newenergy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.newenergy.bean.ModelAndSubsidy;

import java.text.DecimalFormat;

import hxqc.mall.R;

/**
 * Function: 包含动画的展开、收起View
 *
 * @author 袁秉勇
 * @since 2016年3月11日
 */
public class ExpandableView extends ExpandCollapseView {
    private static final String TAG = ExpandableView.class.getSimpleName();

    /** 可展开的组group中的相关控件 **/
    private TextView textView;

    /** 展开收缩中的相关view **/
    private TextView mRealSellingPriceTextView;
    private TextView mFactoryGuidePriceTextView;
    private TextView mTotalSubsidyTextView;
    private TextView mCountrySubsidyTextView;
    private TextView mLocalSubsidyTextView;
    private TextView mFactorySubsidyTextView;
    private TextView mSellerSubsidyTextView;
    private TextView mBatteryEnduranceAbilityTextView;
    private TextView mPurchaseTaxTagTextView;
    private RelativeLayout mMoreInfomationView;


    public View getClickableLayout() {
        return titleContentView;
    }


    public LinearLayout getContentLayout() {
        return contentLayout;
    }


    public RelativeLayout getmMoreInfomationView() {
        return mMoreInfomationView;
    }


    public ExpandableView(Context context) {
        super(context);
        init();
    }


    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public ExpandableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.ev_model_subsidy_content_view, contentLayout, true);

        textView = new TextView(getContext());
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setLayoutParams(layoutParams);
        textView.setTextAppearance(getContext(), R.style.BlackText14);
        titleContentView.addView(textView);

        mRealSellingPriceTextView = (TextView) findViewById(R.id.real_selling_price_text);
        mFactoryGuidePriceTextView = (TextView) findViewById(R.id.factory_guide_price_text);
        mTotalSubsidyTextView = (TextView) findViewById(R.id.total_subsidy_text);
        mCountrySubsidyTextView = (TextView) findViewById(R.id.country_subsidy_text);
        mLocalSubsidyTextView = (TextView) findViewById(R.id.local_subsidy_text);
        mFactorySubsidyTextView = (TextView) findViewById(R.id.factory_subsidy_text);
        mSellerSubsidyTextView = (TextView) findViewById(R.id.seller_subsidy_text);
        mBatteryEnduranceAbilityTextView = (TextView) findViewById(R.id.battery_endurance_ability);
        mPurchaseTaxTagTextView = (TextView) findViewById(R.id.purchase_tax_tag);
        mMoreInfomationView = (RelativeLayout) findViewById(R.id.more_information_view);
    }


    public void initData(ModelAndSubsidy modelAndSubsidy) {
        if (modelAndSubsidy == null) return;
        textView.setText(modelAndSubsidy.autoName);

        mRealSellingPriceTextView.setText(formatPriceData(modelAndSubsidy.price));
        mFactoryGuidePriceTextView.setText(formatPriceData(modelAndSubsidy.origPrice));
        mTotalSubsidyTextView.setText(formatPriceData(modelAndSubsidy.subsidyTotal));
        mCountrySubsidyTextView.setText(formatPriceData(modelAndSubsidy.subsidyCountry));
        mLocalSubsidyTextView.setText(formatPriceData(modelAndSubsidy.subsidyLocal));
        mFactorySubsidyTextView.setText(formatPriceData(modelAndSubsidy.subsidyManufacturer));
        mSellerSubsidyTextView.setText(formatPriceData(modelAndSubsidy.subsidyDealer));
        mBatteryEnduranceAbilityTextView.setText(String.format("%s公里", modelAndSubsidy.batteryLife));
        if ("-".equals(modelAndSubsidy.hasPurchaseTax)) {
            mPurchaseTaxTagTextView.setText("—");
            mPurchaseTaxTagTextView.setBackground(null);
        } else if ("免".equals(modelAndSubsidy.hasPurchaseTax)) {
            mPurchaseTaxTagTextView.setText("免");
            mPurchaseTaxTagTextView.setBackground(getResources().getDrawable(R.drawable.purchase_tax_bg));
        } else if ("半免".equals(modelAndSubsidy.hasPurchaseTax)) {
            mPurchaseTaxTagTextView.setText("半免");
            mPurchaseTaxTagTextView.setBackground(null);
        } else {
            mPurchaseTaxTagTextView.setText(modelAndSubsidy.hasPurchaseTax);
            mPurchaseTaxTagTextView.setBackground(null);
        }
    }


    private String formatPriceData(double price) {
        DecimalFormat df = new DecimalFormat("######0.00");
        if (price <= 1000) {
            return df.format(price) + "元";
        } else {
            return df.format(price / 10000) + "万";
        }
    }
}
