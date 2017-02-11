package com.hxqc.mall.auto.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.OtherUtil;

/**
 * Author:胡仲俊
 * Date: 2016 - 09 - 27
 * FIXME
 * Todo
 */

public class CallBar extends LinearLayout {

    private TextView titleTv, numberTv;
    private String number = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        titleTv.setText(title);
    }

    private String title = "客服电话";

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        numberTv.setText(number);
    }

    public void setNumber(int number) {
        this.number = number + "";
        numberTv.setText(this.number);
    }

    public CallBar(Context context, String title, String number) {
        this(context);
        setTitle(title);
        setNumber(number);
    }

    public CallBar(Context context) {
        this(context, null);
    }

    public CallBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CallBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.t_view_call_bar, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CallBar);
        number = typedArray.getString(R.styleable.CallBar_call_number);
        String string = typedArray.getString(R.styleable.CallBar_call_title);
        if (!TextUtils.isEmpty(string))
            title = string;
        typedArray.recycle();

        titleTv = (TextView) findViewById(R.id.call_bar_title_tv);
        numberTv = (TextView) findViewById(R.id.call_bar_number_tv);
        titleTv.setText(title);
        numberTv.setText(number);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                OtherUtil.callPhone(getContext(), number);
                if (!TextUtils.isEmpty(number))
                    new CallPhoneDialog(getContext(), number).show();
            }
        });
    }

    /**
     * Author:李烽
     * Date:2015-12-10
     * FIXME
     * Todo 打电话dialog
     */
    public static class CallPhoneDialog extends Dialog implements OnClickListener {

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
}
