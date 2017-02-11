package com.hxqc.mall.usedcar.views.SellCar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.views.UsedCarDatePicker;

import java.util.Calendar;

/**
 * 说明:二手车时间选择
 *
 * @author: 吕飞
 * @since: 2016-05-21
 * Copyright:恒信汽车电子商务有限公司
 */
public class SellCarItemChooseDate extends SellCarItem implements SellCarItem.SellCarItemListener {
    int mMonth;
    int mYear;
    int mDay;
    long maxDate;
    boolean mIsNecessary;
    UsedCarDatePicker mUsedCarDatePicker;

    public SellCarItemChooseDate(Context context) {
        super(context);
    }

    public SellCarItemChooseDate(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SellCarItemChooseDate);
        mIsNecessary = typedArray.getBoolean(R.styleable.SellCarItemChooseDate_isNecessary, false);
        typedArray.recycle();
        initDate();
//        if (mIsNecessary) {
//            setSellCarItemListener(this);
//        }else {
//
//        }
        setSellCarItemListener(this);
    }

    private void initDate() {
        mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mMonth = Calendar.getInstance().get(Calendar.MONTH);
        mYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    public void showDatePiker() {
        switch (mItemName) {
            case "首次上牌时间：":
                maxDate = UsedCarDatePicker.FIRST_ON_CARD;
                break;
            case "年检有效期：":
                maxDate = UsedCarDatePicker.INSPECTION_DATE;
                break;
            case "交强险到期：":
                maxDate = UsedCarDatePicker.SALI_DATE;
                break;
            case "商业保险到期：":
                maxDate = UsedCarDatePicker.INSURANCE_DATE;
                break;
            case "质保有效期：":
                maxDate = UsedCarDatePicker.WARRANTY_DATE;
                break;
        }
        mUsedCarDatePicker = new UsedCarDatePicker((FragmentActivity) getContext(), mYear, mMonth, mDay, maxDate) {
            @Override
            protected void doNext(int year, int monthOfYear, int dayOfMonth, String month, String day) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                setResult(mYear + "-" + month + "-" + day);
            }
        };
        if (mIsNecessary) {
            mUsedCarDatePicker.getDatePickerDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mChooseResultView.validate();
                }
            });
        }
    }

    @Override
    public void sellCarItemClick(View view) {
        showDatePiker();
    }
}
