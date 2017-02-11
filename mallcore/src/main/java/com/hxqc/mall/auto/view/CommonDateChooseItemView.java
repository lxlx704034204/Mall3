package com.hxqc.mall.auto.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.hxqc.mall.core.util.OtherUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

/**
 * Author:李烽
 * Date:2016-06-21
 * FIXME
 * Todo
 */
public class CommonDateChooseItemView extends CommonEditInfoItemView {
    public CommonDateChooseItemView(Context context) {
        this(context, null);
    }

    public CommonDateChooseItemView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        getmContentView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherUtil.showDialog((Activity) context, -1, -1, -1, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        getmContentView().setText(String.format("%s年%s月%s日", year, monthOfYear + 1, dayOfMonth));
                    }
                });
            }
        });
    }
}
