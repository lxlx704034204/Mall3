package com.hxqc.mall.thirdshop.views;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;


/**
 * Author:李烽
 * Date:2015-12-10
 * FIXME
 * Todo 打电话dialog
 */
@Deprecated
public class CallPhoneDialog extends AlertDialog implements View.OnClickListener {

    private String number = "";
    private Context context;

    public CallPhoneDialog(Context context, String number) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_call_dialog);
        this.context = context;
        this.number = number;
        TextView numberTextView = (TextView) findViewById(R.id.call_dialog_number);
        numberTextView.setText(number);
        findViewById(R.id.call_dialog_to_call).setOnClickListener(this);
        findViewById(R.id.call_dialog_cancel).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.call_dialog_to_call) {//拨打
            dismiss();
            OtherUtil.callPhone(context, number);

        } else if (i == R.id.call_dialog_cancel) {//取消
            dismiss();

        }
    }
}
