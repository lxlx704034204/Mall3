package com.hxqc.mall.drivingexam.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.hxqc.mall.drivingexam.R;


/**
 * 定义ProgressDialog
 */
@Deprecated
public class CustomProgress extends Dialog {
    private Activity context;
    private CustomProgress dialog;

    public CustomProgress(Context context, String message, final boolean cancelable) {
        super(context);
        this.context = (Activity) context;
        dialog = new CustomProgress(context , R.style.Custom_Progress);
        init(message, cancelable);
    }

    public CustomProgress(Context context, int theme) {
        super(context, theme);

    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {

    }

    /**
     * @param message    提示消息
     * @param cancelable 是否按返回键取消
     * @param
     * @return
     */
    public   CustomProgress init( String message, final boolean cancelable) {
        dialog.setTitle("");
        dialog.setContentView(R.layout.myrossdiaog2);

        TextView message_tv = (TextView) dialog.findViewById(R.id.message);
        message_tv.setText(message);
        // 按返回键是否取消  
        dialog.setCancelable(cancelable);
        // 监听返回键处理  
        dialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (cancelable) {
                    //     dialog.dismiss();
                    if (!context.isFinishing())
                        context.finish();
                }

            }
        });

        // 设置居中  
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度  
        lp.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);  
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    public void showing() {
        if (dialog != null)
            if (!context.isFinishing())
                dialog.show();
    }

    public void hide() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
 


}  
