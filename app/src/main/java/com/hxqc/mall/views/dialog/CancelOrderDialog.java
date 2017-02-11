package com.hxqc.mall.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hxqc.mall.api.ApiClient;
import com.hxqc.mall.core.adapter.CancelReasonAdapter;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.CancelReason;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:取消订单对话框
 * <p/>
 * author: 吕飞
 * since: 2015-04-14
 * Copyright:恒信汽车电子商务有限公司
 */
public class CancelOrderDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    public TextView mOkView;//确定
    ApiClient mApiClient;
    ListView mReasonGroupView;
    TextView mOffView;//关闭
    CancelReasonAdapter mCancelReasonAdapter;
    ArrayList< CancelReason > mCancelReasons;
    int mReasonID = -1;
    String mOrderID;
    CancelOrderListener mCancelOrderListener;

    public CancelOrderDialog(Context context, ApiClient mApiClient, ArrayList< CancelReason > cancelReasons, String orderID, CancelOrderListener mCancelOrderListener) {
        super(context);
        this.mApiClient = mApiClient;
        this.mCancelReasons = cancelReasons;
        this.mOrderID = orderID;
        this.mCancelOrderListener = mCancelOrderListener;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cancel_order);
        mReasonGroupView = (ListView) findViewById(R.id.reason_group);
        mOkView = (TextView) findViewById(R.id.ok);
        mOffView = (TextView) findViewById(R.id.off);
        mOkView.setOnClickListener(this);
        mOffView.setOnClickListener(this);
        mCancelReasonAdapter = new CancelReasonAdapter(cancelReasons, getContext());
        mReasonGroupView.setAdapter(mCancelReasonAdapter);
        mReasonGroupView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.ok:
                if (mReasonID == -1) {
                    ToastHelper.showYellowToast(getContext(), R.string.me_cancel_reason_hint);
                } else {
                    mApiClient.cancelOrder(UserInfoHelper.getInstance().getToken(getContext()), mOrderID, mReasonID,
                            new DialogResponseHandler(getContext(), getContext().getResources().getString(R.string.me_submitting)) {
                                @Override
                                public void onSuccess(String response) {
                                    new SharedPreferencesHelper(getContext()).setOrderChange(true);
                                    dismiss();
                                    mCancelOrderListener.cancelOrder();
                                }
                            });
                }
                break;
            case R.id.off:
                dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
        for (int i = 0; i < mCancelReasons.size(); i++) {
            mCancelReasons.get(i).isChosen = false;
        }
        mCancelReasons.get(position).isChosen = true;
        mCancelReasonAdapter.notifyDataSetChanged();
        mReasonID = mCancelReasons.get(position).reasonID;
    }

    public interface CancelOrderListener {
        void cancelOrder();
    }
}
