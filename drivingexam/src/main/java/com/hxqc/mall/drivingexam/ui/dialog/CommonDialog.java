package com.hxqc.mall.drivingexam.ui.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hxqc.mall.drivingexam.R;


/**
 * @author zhaofan'
 */
public class CommonDialog extends DialogFragment implements View.OnClickListener {
    private Context context;
    private TextView title, content;
    private TextView cancel, ok;
    private boolean touchcancel = false;   //不可以取消
    private boolean backcancel = false;    //可以取消
    private String titletext, contenttext, rightBtn, leftBtn;
    private int visibility;
    private boolean noTitle = false;
    private int mDuration = 300;
    private RightBtnClickListener listener = null;
    private LeftBtnClickListener listener2 = null;


    public CommonDialog() {
    }


    public static CommonDialog getInstance(Activity context) {
        CommonDialog instance = new CommonDialog();
        instance.context = context;
        return instance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.MyDialogStyle2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(R.layout.fragment_login_dialog2);
        dialog.setCanceledOnTouchOutside(true);


        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.4f;
        dialog.getWindow().setAttributes(lp);

        inti(dialog);
        return dialog;

    }


    private void inti(Dialog dialog) {
        title = (TextView) dialog.findViewById(R.id.title);
        content = (TextView) dialog.findViewById(R.id.content);
        cancel = (TextView) dialog.findViewById(R.id.cancel);
        ok = (TextView) dialog.findViewById(R.id.ok);
        cancel.setOnClickListener(this);

        cancel.setVisibility(visibility);
        // title.setVisibility(visibility);

        //   dialog.findViewById(R.id.top_line).setVisibility(visibility);


        ok.setOnClickListener(this);
        ok.setText(TextUtils.isEmpty(rightBtn) ? "确定" : rightBtn);
        cancel.setText(TextUtils.isEmpty(leftBtn) ? "取消" : leftBtn);

        if (noTitle)
            title.setVisibility(View.GONE);

        title.setText(titletext);
        content.setText(contenttext);


        dialog.setCanceledOnTouchOutside(touchcancel);
        dialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return backcancel;
                }
                return false;
            }
        });


    }

    public CommonDialog setTitle(String title) {
        this.titletext = title;
        return this;
    }


    public CommonDialog noTitle() {
        this.noTitle = true;
        return this;

    }

    public CommonDialog setContent(String content) {
        this.contenttext = content;
        return this;
    }

    public CommonDialog setRightButton(String btn, RightBtnClickListener listener) {
        this.rightBtn = btn;
        this.listener = listener;
        return this;
    }


    public CommonDialog setLeftButton(String btn, LeftBtnClickListener listener2) {
        this.leftBtn = btn;
        this.listener2 = listener2;
        return this;
    }


    public CommonDialog setTouchCancel() {
        this.touchcancel = true;
        return this;
    }

    public CommonDialog banBackCancel() {
        this.backcancel = true;
        return this;
    }


    public CommonDialog setAlertDialog(String title, String content1) {

        this.contenttext = content1;
        this.titletext = title;
        this.visibility = View.GONE;
        this.touchcancel = false;
        this.backcancel = true;
        return this;
    }


    public CommonDialog setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }


    public void show(String tag) {
        if (!((Activity) context).isFinishing())
            this.show(((Activity) context).getFragmentManager(), tag);
    }


    public interface RightBtnClickListener {
        void onRightBtnClickListener(View v);
    }

    public interface LeftBtnClickListener {
        void onLeftBtnClickListener(View v);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            dismiss();
            if (listener2 != null)
                listener2.onLeftBtnClickListener(v);
        } else if (v.getId() == R.id.ok) {
            dismiss();
            if (listener != null)
                listener.onRightBtnClickListener(v);
        }


    }

    @Override
    public void onStart() {
        super.onStart();
    }


}
