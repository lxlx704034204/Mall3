package com.hxqc.mall.views.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.core.anim.Techniques;
import com.hxqc.mall.core.anim.YoYo;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.NormalDialog;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-06-03
 * FIXME
 * Todo 到货通知对话框
 */
public class ArrivalDialog extends NormalDialog {
    private EditText mPhoneView;
    private EditText mEmailView;
    private String itemID;

    public ArrivalDialog(Context context, String itemID) {
        super(context);
        this.itemID = itemID;
        setDialogTitle("到货通知");
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_auto_arriva_notificationl, null);
        setBodyView(view);
        mPhoneView = (EditText) view.findViewById(R.id.arrival_phone_view);
        mEmailView = (EditText) view.findViewById(R.id.arrival_email_view);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == com.hxqc.mall.core.R.id.cancel) {
            YoYo.with(Techniques.ZoomOutDown).duration(375).onEnd(new YoYo.AnimatorCallback() {
                @Override
                public void call(com.nineoldandroids.animation.Animator animator) {
                    dismiss();
                }
            }).playOn(mDialogView);

        } else if (i == com.hxqc.mall.core.R.id.ok) {
            doNext();
        }
    }

    @Override
    protected void doNext() {
        String phone = mPhoneView.getText().toString();
        String email = mEmailView.getText().toString();
        if (FormatCheck.checkPhoneNumber(phone, getContext()) != FormatCheck.CHECK_SUCCESS) {
            return;
        }
        if(email.length()>100|!FormatCheck.checkEmail(email, getContext())){
            ToastHelper.showYellowToast(getContext(), R.string.me_email_error);
            return;
        }
        NewAutoClient client = new NewAutoClient();
        client.arrival(itemID, phone, email, new LoadingAnimResponseHandler(getContext()) {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(getContext(), "通知提交成功");
            }

            @Override
            public void onFinish() {
                super.onFinish();

                YoYo.with(Techniques.ZoomOutDown).duration(375).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(com.nineoldandroids.animation.Animator animator) {
                        dismiss();
                    }
                }).playOn(mDialogView);
            }
        });
    }
}
