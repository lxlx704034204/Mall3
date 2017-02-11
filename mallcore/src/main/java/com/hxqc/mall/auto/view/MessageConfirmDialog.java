package com.hxqc.mall.auto.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hxqc.mall.core.R;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年06月21日
 */
public class MessageConfirmDialog extends Dialog implements View.OnClickListener {
    private final static String TAG = MessageConfirmDialog.class.getSimpleName();
    private Context mContext;

    public TextView mOkView;//确定
    protected TextView mTitleView;//标题
    protected TextView mContentView;//提示内容
    public TextView mOffView;//关闭
    protected ConfirmListener confirmListener;

    public MessageConfirmDialog(Context context, String title, String tipContent, ConfirmListener confirmListener) {
        super(context);
        this.mContext = context;
        this.confirmListener = confirmListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_comfirm_message);
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(title);
        mContentView = (TextView) findViewById(R.id.content);
        mContentView.setText(tipContent);
        mOkView = (TextView) findViewById(R.id.ok);
        mOffView = (TextView) findViewById(R.id.off);
        mOffView.setVisibility(View.GONE);
        mOffView.setOnClickListener(this);
        mOkView.setOnClickListener(this);
//        this.setCancelable(false);
//        this.setCanceledOnTouchOutside(false);
    }



    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ok) {
            confirmListener.confirm();
            dismiss();

        } else if (i == R.id.off) {
            dismiss();

        }
    }

    public interface ConfirmListener {
        void confirm();
    }
}
