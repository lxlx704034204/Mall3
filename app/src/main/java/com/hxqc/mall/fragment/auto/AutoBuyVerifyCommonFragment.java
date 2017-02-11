package com.hxqc.mall.fragment.auto;

import com.hxqc.pay.util.Switchhelper;


/**
 * Author: HuJunJie Date: 2015-04-15 FIXME Todo 普通车辆
 */
public class AutoBuyVerifyCommonFragment extends AutoBuyVerifyFragment {
    public AutoBuyVerifyCommonFragment() {

    }

    @Override
    public void toPay() {

        Switchhelper.toPayNormal(mAutoDataControl.mOrderPayRequest.getPayType(),
                mAutoDataControl.mOrderPayRequest, getActivity());
    }

    @Override
    public String fragmentDescription() {
        return "普通选择购买";
    }
}
