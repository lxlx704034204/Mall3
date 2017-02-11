package com.hxqc.mall.core.views.dialog;

import android.content.Context;
import android.view.View;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.anim.Techniques;
import com.hxqc.mall.core.anim.YoYo;

/**
 * Author: wanghao
 * Date: 2015-10-12
 * FIXME
 * Todo
 */
public abstract class PayStatusDialog extends com.hxqc.mall.core.views.dialog.NormalDialog {


    public PayStatusDialog(Context context, String content) {
        super(context, content);
//        mCancelView.setText("重新请求");
//        mOkView.setText("联系客服");
    }

    public PayStatusDialog(Context context, String title, String content) {
        super(context, title, content);
//        mCancelView.setText("重新请求");
//        mOkView.setText("联系客服");
    }

    @Override
    protected void doNext() {
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel) {
            YoYo.with(Techniques.ZoomOutDown).duration(375).onEnd(new YoYo.AnimatorCallback() {
                @Override
                public void call(com.nineoldandroids.animation.Animator animator) {
                    dismiss();
                    doAgain();
                }
            }).playOn(mDialogView);

        } else if (i == R.id.ok) {
            YoYo.with(Techniques.ZoomOutDown).duration(375).onEnd(new YoYo.AnimatorCallback() {
                @Override
                public void call(com.nineoldandroids.animation.Animator animator) {
                    dismiss();
                    doCall();
                }
            }).playOn(mDialogView);
        }
    }

    //拨打客服
    protected abstract void doCall();

    //重新请求
    protected abstract void doAgain();
}
