package com.hxqc.mall.views.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.core.anim.Techniques;
import com.hxqc.mall.core.anim.YoYo;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.auto.AutoDetail;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.NormalDialog;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-06-03
 * FIXME
 * Todo 降价通知对话框
 */
public class PriceCutDialog extends NormalDialog {
    EditText mPriceView;//价格
    EditText mPhoneView;//手机号
    EditText mEmailView;//邮箱
    AutoDetail autoDetail;

    public PriceCutDialog(Context context, AutoDetail autoDetail) {
        super(context);
        this.autoDetail = autoDetail;
        setDialogTitle("降价通知");
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_auto_price_cut_notification, null);
        setBodyView(view);
        mPriceView = (EditText) view.findViewById(R.id.price_cut_price_view);
        mPhoneView = (EditText) view.findViewById(R.id.price_cut_phone_view);
        mEmailView = (EditText) view.findViewById(R.id.price_cut_email_view);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == com.hxqc.mall.core.R.id.cancel) {
            zoomDismiss(mDialogView);

        } else if (i == com.hxqc.mall.core.R.id.ok) {
            doNext();
        }
    }

    @Override
    protected void doNext() {
        if (checkValueIntegrity()) {
            new NewAutoClient().priceCut(autoDetail.getItemID(), mPhoneView.getText().toString().trim(),
                    mEmailView.getText().toString().trim(), mPriceView.getText().toString().trim(),
                    new LoadingAnimResponseHandler(getContext()) {
                        @Override
                        public void onSuccess(String response) {
                            ToastHelper.showGreenToast(getContext(), "通知提交成功");
                            zoomDismiss(mDialogView);
                        }

                    });
        }
    }

    /**
     * 关闭
     *
     * @param mDialogView
     */
    private void zoomDismiss(View mDialogView) {
        YoYo.with(Techniques.ZoomOutDown).duration(375).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(com.nineoldandroids.animation.Animator animator) {
                cancel();
            }
        }).playOn(mDialogView);
    }

    /**
     * 判断内容完整性
     */
    boolean checkValueIntegrity() {
        if (TextUtils.isEmpty(mPriceView.getText().toString().trim())) {
            ToastHelper.showYellowToast(getContext(), "请输入期望金额");
            return false;
        }
        try {
            float price = Float.parseFloat(mPriceView.getText().toString().trim())*10000;
            if (price == 0) {
                ToastHelper.showYellowToast(getContext(), "请输入大于0的金额");
                return false;
            }
            if (price >= autoDetail.getItemPrice()) {
                ToastHelper.showYellowToast(getContext(), "请输入小于当前车辆价格");
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ToastHelper.showYellowToast(getContext(), "请输入期望金额");
            return false;
        }

        return FormatCheck.checkPhoneNumber(mPhoneView.getText().toString().trim(), getContext()) == FormatCheck.CHECK_SUCCESS;
    }
}
