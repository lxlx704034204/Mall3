package com.hxqc.mall.thirdshop.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;


/**
 * Function:取消订单的Dialog
 *
 * @author 袁秉勇
 * @since 2015年12月03日
 */
public class T_CancelOrderDialog extends Dialog implements View.OnClickListener {
    public TextView mOkView;//确定
    ThirdPartShopClient mApiClient;
    TextView mOffView;//关闭
    String mOrderID;
    Context mContext;
    CancelOrderListener mCancelOrderListener;
    SharedPreferencesHelper mSharedPreferencesHelper;

    public T_CancelOrderDialog(Context context, ThirdPartShopClient mApiClient, String orderID, CancelOrderListener mCancelOrderListener) {
        super(context);
        this.mContext = context;
        this.mApiClient = mApiClient;
        this.mOrderID = orderID;
        this.mCancelOrderListener = mCancelOrderListener;
        mSharedPreferencesHelper = new SharedPreferencesHelper(mContext);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.t_dialog_cancel_order);
        mOkView = (TextView) findViewById(R.id.ok);
        mOffView = (TextView) findViewById(R.id.off);
        mOkView.setOnClickListener(this);
        mOffView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ok) {
            mApiClient.cancelThirdOrder( mOrderID, new
                    DialogResponseHandler(mContext, "正在提交,请稍等") {
                @Override
                public void onSuccess(String response) {
                    mSharedPreferencesHelper.setOrderChange(true);
                    mCancelOrderListener.cancelOrder();
                }
            });
            dismiss();

        } else if (i == R.id.off) {
            dismiss();

        }
    }

    public interface CancelOrderListener {
        void cancelOrder();
    }
}
