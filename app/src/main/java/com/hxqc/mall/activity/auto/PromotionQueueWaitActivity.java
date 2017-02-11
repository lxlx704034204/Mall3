package com.hxqc.mall.activity.auto;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.control.AutoItemDataControl;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.pay.api.PayApiClient;
import com.hxqc.pay.model.OrderIDResponse;
import com.hxqc.pay.util.Switchhelper;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-05-08
 * FIXME
 * Todo   活动抢购等待
 */
public class PromotionQueueWaitActivity extends BackActivity {
    private static final int MESSAGE_TIME = 1;//计时
    private static final int MESSAGE_PLACE_AN_ORDER = 2;//购买
    TextView mTimeView;
    PayApiClient payApiClient;
    AutoItemDataControl mAutoDataControl;
    int time;
    NormalDialog dialog;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_TIME:
                    mTimeView.setText(String.format(getString(R.string.promotion_queue_wait_time_string), String.valueOf(time)));
                    timerTask();
                    break;
                case MESSAGE_PLACE_AN_ORDER:
                    seckillOrder();
                    break;
            }

            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_queue_wait);
        mTimeView = (TextView) findViewById(R.id.wait_time);
        payApiClient = new PayApiClient();
        mAutoDataControl = AutoItemDataControl.getInstance();

        Message message = handler.obtainMessage();
        message.what = MESSAGE_PLACE_AN_ORDER;
        handler.sendMessage(message);

        message = handler.obtainMessage();
        message.what = MESSAGE_TIME;
        handler.sendMessage(message);
    }

    /**
     * 下单
     */
    public synchronized void seckillOrder() {
        payApiClient.createSeckillOrder(mAutoDataControl.mOrderPayRequest, new BaseMallJsonHttpResponseHandler(this) {

            @Override
            public void onSuccess(String response) {
                handler.removeMessages(MESSAGE_TIME);
                OrderIDResponse orderIDResponse = JSONUtils.fromJson(response, OrderIDResponse.class);
                Switchhelper.toPaySeckill(mAutoDataControl.mOrderPayRequest.getPayType(),
                        orderIDResponse.orderID,  PromotionQueueWaitActivity.this);
                finish();
            }

            @Override
            public void onOtherFailure(int statusCode, Header[] headers, String responseString, com.hxqc.mall.core.model.Error throwable) {

                if (mError.code == 400) {
                    //其他错误
                    ToastHelper.showYellowToast(PromotionQueueWaitActivity.this, mError.message);
                } else if (mError.code == 403) {
                    //暂无库存
                    handler.removeMessages(MESSAGE_TIME);
                    if (dialog == null) {
                        dialog = new NormalDialog(PromotionQueueWaitActivity.this, "抱歉", "该车型已售罄。") {
                            @Override
                            protected void doNext() {
                                finish();
                            }
                        };
                    }
                    dialog.show();
                } else if (mError.code == 405) {
                    //已经生成过订单，特卖订单
                    handler.removeMessages(MESSAGE_TIME);
                    ToastHelper.showYellowToast(PromotionQueueWaitActivity.this, "订单已经存在，请继续未完成的订单。");
                    ActivitySwitcher.toMyOrder(PromotionQueueWaitActivity.this);
                    finish();
                } else if (mError.code == 406) {
                    //排队中
                    Message message = handler.obtainMessage();
                    message.what = MESSAGE_PLACE_AN_ORDER;
                    handler.sendMessageDelayed(message, 2000);
                }
            }
        });
    }

    public void timerTask() {
        Message message = handler.obtainMessage();
        message.what = MESSAGE_TIME;
        message.arg1 = time++;
        handler.sendMessageDelayed(message, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(MESSAGE_TIME);
    }
}
