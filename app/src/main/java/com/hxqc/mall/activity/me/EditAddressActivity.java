package com.hxqc.mall.activity.me;

import android.os.Bundle;
import android.text.Html;

import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;

import hxqc.mall.R;

/**
 * 说明:编辑地址
 *
 * author: 吕飞
 * since: 2015-03-31
 * Copyright:恒信汽车电子商务有限公司
 */
@Deprecated
public class EditAddressActivity extends AddAddressActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsEditAddressActivity=true;
        mDeliveryAddress = getIntent().getParcelableExtra(ActivitySwitcher.DELIVERY_ADDRESS);
        mAreaView.setTextColor(getResources().getColor(R.color.title_and_main_text));
        fillData();
    }

    private void fillData() {
        mConsigneeView.mEditTextView.setText(mDeliveryAddress.consignee);
        mMobileView.mEditTextView.setText(mDeliveryAddress.phone);
        mDetailAddressView.setText(mDeliveryAddress.detailedAddress);
        String mAddress = mDeliveryAddress.province + "&nbsp;&nbsp;&nbsp;&nbsp;" + mDeliveryAddress.city + "&nbsp;&nbsp;&nbsp;&nbsp;" + mDeliveryAddress.district;
        mAreaView.setText(Html.fromHtml(mAddress));
    }
}
