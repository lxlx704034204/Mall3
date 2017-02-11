package com.hxqc.mall.paymethodlibrary.manager;

import com.hxqc.mall.paymethodlibrary.inter.OnPayFinishCallBackListening;
import com.hxqc.mall.paymethodlibrary.inter.PayResultCallBack;
import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;
import com.hxqc.mall.paymethodlibrary.util.PayCallBackTag;


/**
 * Author: wanghao
 * Date: 2016-03-21
 * FIXME  支付回调管理类       待测试
 * Todo
 */
public class PayCallBackManager implements OnPayFinishCallBackListening {

    /**
     * 获取支付回调
     */
    PayResultCallBack payResultCallBack;

    public void setPayResultCallBack(PayResultCallBack payResultCallBack) {
        this.payResultCallBack = payResultCallBack;
    }

    protected static PayCallBackManager callbackInstance;

    protected PayCallBackManager() {
    }

    public static PayCallBackManager getInstance() {
        if (callbackInstance == null) {
            synchronized (PayCallBackManager.class) {
                if (callbackInstance == null) {
                    callbackInstance = new PayCallBackManager();
                }
            }
        }
        return callbackInstance;
    }

    public void onDestroy() {
        if (payResultCallBack != null) {
            payResultCallBack = null;
        }

        if (callbackInstance != null) {
            callbackInstance = null;
        }
    }

    @Override
    public void onPayCallBack(EventGetSuccessModel event) {
        if (event.getPay_status() == PayCallBackTag.PAY_SUCCESS) {
            payResultCallBack.paySuccess(event);
        } else if (event.getPay_status() == PayCallBackTag.PAY_FAIL) {
            payResultCallBack.payFail(event);
        } else if (event.getPay_status() == PayCallBackTag.PAY_CANCEL) {
            payResultCallBack.payCancel(event);
        } else if (event.getPay_status() == PayCallBackTag.PAY_EXCEPTION) {
            payResultCallBack.payException(event);
        } else if (event.getPay_status() == PayCallBackTag.PAY_PROGRESSING) {
            payResultCallBack.payProgressing(event);
        }
    }
}
