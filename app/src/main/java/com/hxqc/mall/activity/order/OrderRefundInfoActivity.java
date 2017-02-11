package com.hxqc.mall.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.util.OtherUtil;

import hxqc.mall.R;

/**
 * liaoguilong
 * 2017年1月12日 15:10:02
 * 对于已付款的订单且付款方式选择支付宝的超过三个月的 点击申请退款 进入界面
 */
public class OrderRefundInfoActivity extends BackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_refund_info);
    }

    /**
     * 拨打客服电话
     * @param view
     */
    public void butClick(View view){
        Button btn= (Button) view;
        OtherUtil.callPhone(view.getContext(), btn.getText().toString());
    }
}
