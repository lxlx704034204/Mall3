package com.hxqc.mall.views.auto;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.AutoDetail;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.model.auto.Subsidy;
import com.hxqc.mall.core.util.OtherUtil;

import hxqc.mall.R;

/**
 * Author:胡俊杰
 * Date: 2015/11/26
 * FIXME
 * Todo 补贴
 */
public class AutoDetailSubsidy extends GridLayout {
    TextView mCountryView;//国家补贴
    TextView mManufacturerView;//厂家补贴
    TextView mLocalView;//地方补贴
    TextView mHengXinView;//恒信补贴
    TextView mTotalView;//补贴总额

    public AutoDetailSubsidy(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AutoDetailSubsidy(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_auto_detail_subsidy, this);
        mCountryView = (TextView) findViewById(R.id.subsidy_country);
        mManufacturerView = (TextView) findViewById(R.id.subsidy_manufacturer);
        mLocalView = (TextView) findViewById(R.id.subsidy_local);
        mHengXinView = (TextView) findViewById(R.id.subsidy_hengxin);
        mTotalView = (TextView) findViewById(R.id.subsidy_total);
    }

    public AutoDetailSubsidy(Context context) {
        super(context);
    }

    public void setSubsidy(AutoDetail autoDetail) {
        switch (autoDetail.getItemCategory()) {
            case AutoItem.CATEGORY_AUTOMOBILE:
                this.setVisibility(View.GONE);
                break;
            case AutoItem.CATEGORY_NEW_ENERGY:
            case AutoItem.MINTURE_AUTO:
                Subsidy subsidy = autoDetail.getSubsidy();
                mCountryView.setText(String.format("国家补贴：%s", OtherUtil.amountFormat(subsidy.country, true)));
                mManufacturerView.setText(String.format("厂家补贴：%s", OtherUtil.amountFormat(subsidy.manufacturer, true)));
                mLocalView.setText(String.format("地方补贴：%s", OtherUtil.amountFormat(subsidy.local, true)));
                mHengXinView.setText(String.format("恒信补贴：%s", OtherUtil.amountFormat(subsidy.hengxin, true)));
                mTotalView.setText(String.format("%s", OtherUtil.amountFormat(subsidy.total, true)));
                break;
            default:
                this.setVisibility(View.GONE);
                break;
        }
    }
}
