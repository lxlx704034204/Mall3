package com.hxqc.mall.thirdshop.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;


/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2015年12月03日
 */
public class InfoSubmitSuccessConfirmDialog extends Dialog implements View.OnClickListener {
    public TextView mOkView;//确定
    TextView mTitleView;//标题
    TextView mContentView;//提示内容
    public TextView mOffView;//关闭
    Context mContext;
    ConfirmListener confirmListener;

    public InfoSubmitSuccessConfirmDialog(Context context, String title, String tipContennt, ConfirmListener confirmListener) {
        super(context);
        this.mContext = context;
        this.confirmListener = confirmListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.t_dialog_cancel_order);
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(title);
        mContentView = (TextView) findViewById(R.id.content);
        mContentView.setText(tipContennt);
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
