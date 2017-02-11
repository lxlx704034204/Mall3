package com.hxqc.mall.fragment.auto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;

import hxqc.mall.R;


/**
 * Author: HuJunJie Date: 2015-04-15 FIXME Todo 特卖车辆
 */
public class AutoBuyVerifyPromotionFragment extends AutoBuyVerifyFragment {


    public AutoBuyVerifyPromotionFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.to_pay_order)).setText("立即抢购");
    }

    @Override
    public void toPay() {
        switch (autoDetail.transactionStatus()) {
            case PREPARE:
                ToastHelper.showYellowToast(getActivity(), R.string.promotion_not_start);
                break;
            case NORMAL:
//                AutoItemDataControl  mAutoItemDataControl = AutoItemDataControl.getInstance();
                ActivitySwitcher.toQueueWait(getActivity());
                break;
            case SELLOUT:
                break;
            case END:
                break;
        }
    }


    @Override
    public String fragmentDescription() {
        return "活动确认购买";
    }
}
